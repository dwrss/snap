package model

import cli.MatchChoice

trait GameState
case object NotStarted extends GameState
case class NumDecksSelected(decks: Int) extends GameState
case class NumPlayersSelected(decks: Int, players: Int) extends GameState
case class InProgress(numDecks: Int, numPlayers: Int, matchingMode: MatchChoice) extends GameState
case class Finished(winner: String) extends GameState
