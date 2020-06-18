package model
import model.CardType.{Rank, Suit}

case class Deck(cards: List[Card])

object Deck {
	lazy val allCards: Seq[Card] = {
		Suit.values.flatMap {suit =>
			Rank.values.map(rank => Card(rank, suit))
		}.toList
	}
}