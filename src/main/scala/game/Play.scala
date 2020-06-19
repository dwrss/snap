package game

import model.{Card, Player}

import scala.util.Random

object Play {
	def findPlayersWithMatch(matcher: (Card, Card) => Boolean)(card: Card, players: Seq[Player]): IndexedSeq[Player] = {
		players.foldLeft(Vector[Player]()) {
			(playersWithMatches, player) =>
				if (player.stack.headOption.exists(matcher(_, card))) playersWithMatches :+ player
				else playersWithMatches
		}
	}

	def getWinningPlayer(players: IndexedSeq[Player]): Option[Player] =
		if (players.nonEmpty) {
			val matchingPlayer = players(Random.nextInt(players.size))
			Some(matchingPlayer)
		} else {
			None
		}
}
