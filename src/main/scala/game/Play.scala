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

	/**
	 *
	 * @param players - The list of players from which to select
	 * @return - Some(Player) for a non-empty list. Otherwise, None.
	 */
	def getRandomPlayer(players : IndexedSeq[Player]): Option[Player] =
		if (players.nonEmpty) {
			val matchingPlayer = players(Random.nextInt(players.size))
			Some(matchingPlayer)
		} else {
			None
		}

	/**
	 *
	 * @param players - The players who could potentially call "Snap"
	 * @return - a tuple, where the first element is the "winner" (who takes the cards) and the second player is the
	 *         "loser" (player who loses their cards)
	 */
	def getWinningLosingPair(players: IndexedSeq[Player]): Option[(Player, Player)] =
		Play.getRandomPlayer(players).flatMap { winningPlayer =>
			// The "loser" can be any other matching player other than the winner
			Play.getRandomPlayer(players filterNot (_.id == winningPlayer.id)).map { losingPlayer =>
				winningPlayer -> losingPlayer
			}
		}

	def calcGameStateAfterWin(state: InProgress, winner: Player, loser: Player): InProgress = {
		val updatedWinner = winner.appendToStack(loser.stack)
		val updatedLoser = loser.removeCards()
		state.withUpdatedPlayers(updatedWinner, updatedLoser).incWins()
	}
}
