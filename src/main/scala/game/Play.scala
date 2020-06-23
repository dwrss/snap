package game

import model.{Card, InProgress, Player}

import scala.util.Random

object Play {
	def findPlayersWithMatch(matcher: (Card, Card) => Boolean)(card: Card, players: Seq[Player]): IndexedSeq[Player] = {
		players.foldLeft(Vector[Player]()) {
			(playersWithMatches, player) =>
				if (player.stack.headOption.exists(matcher(_, card))) playersWithMatches :+ player
				else playersWithMatches
		}
	}

	def getRandomPlayer(players : IndexedSeq[Player]): Option[Player] =
		if (players.nonEmpty) {
			val matchingPlayer = players(Random.nextInt(players.size))
			Some(matchingPlayer)
		} else {
			None
		}

	def calcGameStateAfterWin(state: InProgress, winner: Player, loser: Player): InProgress = {
		val updatedWinner = winner.appendToStack(loser.stack)
		val updatedLoser = loser.removeCards()
		state.withUpdatedPlayers(updatedWinner, updatedLoser).incWins()
	}
}
