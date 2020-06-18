package model

import enumeratum._

object CardType {
	sealed trait Suit extends EnumEntry
	object Suit extends Enum[Suit] {
		val values: IndexedSeq[Suit] = findValues
		case object Clubs extends Suit
		case object Diamonds extends Suit
		case object Hearts extends Suit
		case object Spades extends Suit
	}

	sealed trait Rank extends EnumEntry
	object Rank extends Enum[Rank] {
		val values: IndexedSeq[Rank] = findValues
		case object Ace extends Rank
		case object Two extends Rank
		case object Three extends Rank
		case object Four extends Rank
		case object Five extends Rank
		case object Six extends Rank
		case object Seven extends Rank
		case object Eight extends Rank
		case object Nine extends Rank
		case object Ten extends Rank
		case object Jack extends Rank
		case object Queen extends Rank
		case object King extends Rank
		val nonFaceCards = List(Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten)
		val faceCards = List(Jack, Queen, King)
	}
}
