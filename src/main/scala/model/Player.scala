package model

case class Player(id: Int, stack: List[Card] = Nil) {
	def prependCard(card: Card): Player = copy(stack = card :: this.stack)
	def appendToStack(cards: List[Card]): Player = copy(stack = this.stack ::: cards)
	def removeCards(): Player = copy(stack = Nil)
	def index: Int = id -1
}
