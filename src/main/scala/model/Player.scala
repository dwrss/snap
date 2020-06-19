package model

case class Player(id: Int, stack: List[Card] = Nil) {
	def addCard(card: Card): Player = copy(stack=card :: stack)
	def addCards(cards: List[Card]): Player = copy(stack= stack ::: cards)
	def removeCards(): Player = copy(stack = Nil)
	def index: Int = id -1
}
