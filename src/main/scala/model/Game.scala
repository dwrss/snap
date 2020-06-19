package model

import cli.MatchChoice
import game.Setup

case class GameConfig(decks: Int, numPlayers: Int, matchingMode: MatchChoice)

sealed trait GameState
case object NotStarted extends GameState
final case class NumDecksSelected(decks: Int) extends GameState
final case class NumPlayersSelected(decks: Int, players: Int) extends GameState
final case class InProgress private (config: GameConfig, deck: Deck, players: Vector[Player], playerIterator: Iterator[Player]) extends GameState {
//	private def repeatingPlayerList: LazyList[Player] = LazyList.from(this.players) #::: repeatingPlayerList
	def nextPlayer: Player = playerIterator.next()
	def withUpdatedPlayers(playerToUpdate: Player): InProgress = copy(players = this.players.updated(playerToUpdate.index, playerToUpdate))
	def withUpdatedPlayers(playersToUpdate: Player*): InProgress = {
		val updatedPlayerList = playersToUpdate.foldLeft(this.players) { (updatedList, playerToUpdate) =>
			updatedList.updated(playerToUpdate.index, playerToUpdate)
		}
		copy(players = updatedPlayerList)
	}
	def withUpdatedPlayerAndDeck(player: Player, deck: Deck): InProgress = copy(players=players.updated(player.index, player), deck=deck)
}
object InProgress {
	def apply(
		config: GameConfig,
		deck: Deck,
		players: Vector[Player]): InProgress = new InProgress(config, deck, players, Setup.getInfiniteIterator(players))
}
final case class Finished(winners: Vector[Player]) extends GameState
