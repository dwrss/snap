package game

import atto.ParseResult
import io.{BothMatching, ConfigInput, IOHandler, MatchChoice, Msg, Quit}
import model.{Aborted, Finished, GameConfig, GameEndState, GameState, InProgress, NotStarted, NumDecksSelected, NumPlayersSelected}

import scala.annotation.tailrec
import scala.util.chaining._


class GameRunner(ioHandler: IOHandler) {

	@tailrec
	final def run(gameState: GameState): GameEndState = {
//		import cats.implicits._
		def matchParseResult[R](successState: R => GameState)(parseResult: ParseResult[Msg[_ <: R]]): GameState =
			parseResult match {
				case ParseResult.Fail(input, _, message) =>
					if (input.nonEmpty) {
						ioHandler.display(s"Invalid input '$input'. $message")
					}
					ioHandler.display(s"Invalid input. $message")
					gameState
				case ParseResult.Partial(_) =>
					ioHandler.display("Unable to parse input")
					gameState
				case ParseResult.Done(_, numPlayers) =>
					numPlayers match {
						case ConfigInput(num) => successState(num)
						case Quit => Aborted
					}
			}

		gameState match {
			case NotStarted =>
				val decksInput = ioHandler.askDecks()

				val moveToDecksSelected: Int => NumDecksSelected = numDecks => {
					NumDecksSelected(numDecks)
				}
				val nextState = decksInput pipe matchParseResult(moveToDecksSelected)
				run(nextState)
			case NumDecksSelected(num) if num < 1 =>
				ioHandler.display("Please enter a number of decks greater than 0.")
				run(NotStarted)
			case NumDecksSelected(num) =>
				val playersInput = ioHandler.askPlayers()

				val moveToNumPlayers: Int => NumPlayersSelected = numPlayers => NumPlayersSelected(num, numPlayers)
				val nextState = playersInput pipe matchParseResult(moveToNumPlayers)
				run(nextState)
			case NumPlayersSelected(deckNum, players) =>
				val cardInput = ioHandler.askMatchingMode()

				val movedToInProgress: MatchChoice => GameState = matchChoice => {
					ioHandler.display("Creating players...")
					val playerList = Setup.createPlayerList(players)
					val gameConfig = GameConfig(deckNum, players, matchChoice)
					ioHandler.display("Creating decks...")
					val decks = Setup.createDeck(deckNum)
					ioHandler.display("Starting game...")
					InProgress(gameConfig, decks, playerList)
				}
				val nextState = cardInput pipe matchParseResult(movedToInProgress)
				run(nextState)
			case InProgress(config, _, _, _) if config.matchingMode == BothMatching && config.decks == 1 =>
				ioHandler.display("Selecting a 'both' matching strategy with a single deck doesn't make sense")
				run(NumPlayersSelected(config.decks, config.numPlayers))
			case InProgress(_, deck, players, _) if deck.isEmpty =>
				val winner = players.maxBy(_.stack.size)
				val anyOtherEquals = players.filter(otherPlayer => otherPlayer.stack.sizeCompare(winner.stack) == 0 && otherPlayer.id != winner.id)
				run(Finished(winner +: anyOtherEquals))
			case s@InProgress(config, deck, _, round) =>
				val (card, remainingDeck) = deck.dealCard
				val currentPlayer = s.currentPlayer.prependCard(card)
				if (s.isStartOfRound) {
					ioHandler.display(s"[Round $round]")
				}
				ioHandler.display(s"Player ${currentPlayer.id}'s turn")
				ioHandler.display(s"Card dealt: $card")
				val newState = s.withUpdatedPlayerAndDeck(currentPlayer, remainingDeck)
				val playersWithMatches = Play.findPlayersWithMatch(config.matchingMode.matchCards)(card, newState.players)
				val stateAfterResolution = Play.getRandomPlayer(playersWithMatches).flatMap{winningPlayer =>
						Play.getRandomPlayer(playersWithMatches filterNot (_.id == winningPlayer.id)).map{ losingPlayer =>
								ioHandler.display(s"Player ${winningPlayer.id} snap!")
								ioHandler.display(s"Player ${winningPlayer.id} takes: ${losingPlayer.stack.size} card(s)")
								Play.calcGameStateAfterWin(s, winningPlayer, losingPlayer)
						}
				}.getOrElse(newState)
				if (s.isEndOfRound) {
					ioHandler.display(stateAfterResolution.players.map(p => {
						s"Player ${p.id} has ${p.stack.size} card${if (p.stack.sizeIs != 1) "s" else ""}"
					}).toString())
					ioHandler.blankLine()
				}
				run(stateAfterResolution.nextPlayer)
			case f@Finished(_) =>
				f
			case Aborted =>
				Aborted
		}
	}
}

object GameRunner {
	def apply(ioHandler: IOHandler): GameRunner = new GameRunner(ioHandler)
}