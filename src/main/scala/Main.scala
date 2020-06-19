import atto.ParseResult
import cli.{Input, MatchChoice}
import game.{Play, Setup}
import model._

import scala.annotation.tailrec
import scala.util.chaining._

object Main {
	def main(args: Array[String]): Unit = {
		val result = runGame(NotStarted)
		if (result.winners.sizeIs > 1) {
			println(s"Drawn between players ${result.winners.map(_.id)}!")
		} else {
			println(s"Player ${result.winners.map(_.id).head} won!")
		}
	}

	@tailrec
	def runGame(gameState: GameState): Finished = {
		import cats.implicits._
		def matchParseResult[R](successState: R => GameState)(parseResult: ParseResult[R]): GameState =
			parseResult match {
				case ParseResult.Fail(input, _, message) =>
					println(s"Invalid input '$input': $message")
					gameState
				case ParseResult.Partial(_) =>
					println("Unable to parse input")
					gameState
				case ParseResult.Done(_, numPlayers) =>
					successState(numPlayers)
			}

		gameState match {
			case NotStarted =>
				val decksInput = Input.askDecks()

				val moveToDeskSelected: Int => NumDecksSelected = numDecks => NumDecksSelected(numDecks)
				val nextState = decksInput pipe matchParseResult(moveToDeskSelected)
				runGame(nextState)
			case NumDecksSelected(num) =>
				val playersInput = Input.askPlayers()

				val moveToNumPlayers: Int => NumPlayersSelected = numPlayers => NumPlayersSelected(num, numPlayers)
				val nextState = playersInput pipe matchParseResult(moveToNumPlayers)
				runGame(nextState)
			case NumPlayersSelected(deckNum, players) =>
				val cardInput = Input.askMatchingMode()

				val movedToInProgress: MatchChoice => InProgress = matchChoice => {
					val playerList = Setup.createPlayerList(players)
					val gameConfig = GameConfig(deckNum, players, matchChoice)
					val decks = Setup.createDeck(deckNum)
					InProgress(gameConfig, decks.shuffled, playerList)
				}
				val nextState = cardInput pipe matchParseResult(movedToInProgress)
				runGame(nextState)
			case InProgress(_, deck, players, _) if deck.isEmpty =>

				val winner = players.maxBy(_.stack.size)
				val anyOtherEquals = players.filter(otherPlayer => otherPlayer.stack.size == winner.stack.size && otherPlayer.id != winner.id)
				runGame(Finished(winner +: anyOtherEquals))
			case s@InProgress(config, deck, players, _) =>
				//				println(s"Players: $players")
				val (card, remainingDeck) = deck.dealCard
				val currentPlayer = s.nextPlayer.addCard(card)
				println(s"Player ${currentPlayer.id}'s turn")
				println(s"Card dealt: $card")
				val newState = s.withUpdatedPlayerAndDeck(currentPlayer, remainingDeck)
				val playersWithMatches = Play.findPlayersWithMatch(config.matchingMode.matchCards)(card, players.filterNot(_.id == currentPlayer.id))
				val stateAfterResolution = Play.getWinningPlayer(playersWithMatches) match {
					case Some(winningPlayer) =>
						println(s"Player ${winningPlayer.id} Snap!")
						val updatedWinner = winningPlayer.addCards(currentPlayer.stack)
						val updatedCurrentPlayer = currentPlayer.removeCards()
						val h = s.withUpdatedPlayers(updatedWinner, updatedCurrentPlayer)
						h
					case None => newState
				}
				runGame(stateAfterResolution)
			case f@Finished(_) =>
				f
		}
	}
}