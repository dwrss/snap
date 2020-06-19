package model

import model.CardType.{Rank, Suit}

case class Card(rank: Rank, suite: Suit) {
	override def toString: String = s"$rank of $suite"
}
