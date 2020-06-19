package game

import model.{Deck, Player}

object Setup {
	def createPlayerList(numPlayers: Int): Vector[Player] = (1 to numPlayers).foldLeft(Vector[Player]()){(players, id) =>
		 players :+ Player(id)
	}

	def createDeck(numToCreate: Int): Deck = {
		import cats.syntax.semigroup._
		List.fill(numToCreate)(Deck()).reduce((a, b) => a.combine(b))
	}

	def getInfiniteIterator[T](vec: Vector[T]): Iterator[T] = {
		// It's important this is a def so we don't hold onto a reference to the head of the list
		// otherwise, we'll leak memory
		def repeatingPlayerList: LazyList[T] = LazyList.from(vec) #::: repeatingPlayerList
		repeatingPlayerList.iterator
	}
}