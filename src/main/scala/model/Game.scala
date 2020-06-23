package model

import io.MatchChoice
import game.Setup

case class GameConfig(decks: Int, numPlayers: Int, matchingMode: MatchChoice)

sealed trait GameState
sealed trait GameEndState extends GameState
case object NotStarted extends GameState
final case class NumDecksSelected(decks: Int) extends GameState
final case class NumPlayersSelected(decks: Int, players: Int) extends GameState
final class InProgress private (
	val config: GameConfig,
	val deck: Deck,
	val players: Vector[Player],
	val playerIterator: Iterator[Int],
	val round: Int,
	val currentPlayer: Player,
	val winsTotal: Int
) extends GameState {
	def withUpdatedPlayers(playersToUpdate: Player*): InProgress = {
		val updatedPlayerList = playersToUpdate.foldLeft(this.players) { (updatedList, playerToUpdate) =>
			updatedList.updated(playerToUpdate.index, playerToUpdate)
		}
		copy(players = updatedPlayerList)
	}
	def incWins(): InProgress = copy(winsTotal = this.winsTotal + 1)
	def withUpdatedPlayerAndDeck(player: Player, deck: Deck): InProgress = {
		val updatedPlayers = players.updated(player.index, player)
		copy(players=updatedPlayers, deck=deck)
	}
	val isStartOfRound: Boolean = this.currentPlayer.id % config.numPlayers == 1
	val isEndOfRound: Boolean = this.currentPlayer.id % config.numPlayers == 0
	def nextPlayer: InProgress = {
		if (this.isEndOfRound) copy(currentPlayer = this.players(playerIterator.next())).nextRound
		else copy(currentPlayer = this.players(playerIterator.next()))
	}
	private def nextRound: InProgress = {
		copy(round = this.round + 1)
	}

	def copy(
		players: Vector[Player] = this.players,
		deck: Deck = this.deck,
		round: Int = this.round,
		currentPlayer: Player = this.currentPlayer,
		winsTotal: Int = this.winsTotal
	)
	= new InProgress(this.config, deck, players, this.playerIterator, round, currentPlayer, winsTotal)
}
object InProgress {
	def apply(
		config: GameConfig,
		deck: Deck,
		players: Vector[Player]): InProgress = {
		val playerIterator = Setup.getInfiniteIterator(players.map(_.index))
		new InProgress(config, deck, players, playerIterator, 1, players(playerIterator.next()), 0)
	}

	def unapply(arg: InProgress): Option[(GameConfig, Deck, Vector[Player], Int)] = Some((arg.config, arg.deck, arg.players, arg.round))
}
final case class Finished(winners: Vector[Player], totalWins: Int) extends GameState with GameEndState
case object Aborted extends GameState with GameEndState
