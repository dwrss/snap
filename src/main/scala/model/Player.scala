package model

case class Player(id: Int, stack: List[Card] = Nil) {
	// Given that we only care about the top card, we could be more efficient and compress duplicates into a
	// card -> count pair, but that would be serious premature optimisation
	def prependCard(card: Card): Player = copy(stack = card :: this.stack)
	def appendToStack(cards: List[Card]): Player = copy(stack = this.stack ::: cards)
	def removeCards(): Player = copy(stack = Nil)
	def index: Int = id -1
}
