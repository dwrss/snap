package model

import cli.MatchChoice

trait GameState
case object NotStarted extends GameState
case class NumPlayersSelected(num: Int) extends GameState
case class InProgress(numPlayers: Int, matchingMode: MatchChoice) extends GameState
case class Finished(winner: String) extends GameState
