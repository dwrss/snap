package cli

import atto.ParseResult

import scala.io.StdIn

object Input {
	def askDecks(): ParseResult[Int] = {
		println("How many decks?")
		InputParser.parseDeck(StdIn.readLine()).done
	}

	def askPlayers(): ParseResult[Int] = {
		println("How many players? [2]")
		InputParser.parseNumPlayers(StdIn.readLine()).done.map(_.getOrElse(2))
	}

	def askMatchingMode(): ParseResult[MatchChoice] = {
		println("How should cards be matched (suit, value, both)?")
		InputParser.parseMatchStrategy(StdIn.readLine()).done
	}
}
