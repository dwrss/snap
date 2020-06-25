package model
import cats.Semigroup
import model.CardType.{Rank, Suit}

import scala.util.Random

case class Deck(cards: LazyList[Card] = LazyList.from(Deck.allCards)) extends AnyVal {
	def isEmpty: Boolean = cards.isEmpty
	def shuffled: Deck = copy(cards=Random.shuffle(this.cards))
	def dealCard: (Card, Deck) = cards.head -> copy(cards = this.cards.tail)
}

object Deck {
	lazy val allCards: List[Card] = {
		Suit.values.flatMap {suit =>
			Rank.values.map(rank => Card(rank, suit))
		}
	}.toList

	def shuffled: Deck = Deck(LazyList.from(Random.shuffle(Deck.allCards)))

	implicit val ideckSemigroup: Semigroup[Deck] = (x: Deck, y: Deck) => Deck(x.cards #::: y.cards)
}