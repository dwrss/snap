package io

import atto.ParseResult

import scala.io.StdIn

trait Printer {
	def display(text: String): Unit
	def blankLine(): Unit = display("")
}

trait Reader {
	def readLine(): String
}

trait AskDetails { this: Printer with Reader =>
	def askDecks(): ParseResult[Msg[_ <: Int]] = {
		this.display("How many decks?")
		InputParser.parseDeck(this.readLine()).done
	}
	def askPlayers(): ParseResult[Msg[_ <: Int]] = {
		display("How many players? [2]")
		InputParser.parseNumPlayers(this.readLine()).done match {
			case p @ ParseResult.Fail(input, _, _) =>
				if (input.isEmpty) ParseResult.Done(input, ConfigInput(2))
				else p
			case p @ ParseResult.Partial(_) => p
			case p @ ParseResult.Done(_, _) => p
		}
	}

	def askMatchingMode(): ParseResult[Msg[_ <: MatchChoice]] = {
		display("How should cards be matched (suit, value, both)?")
		InputParser.parseMatchStrategy(this.readLine()).done
	}
}

object ConsoleIOHandler extends Reader with Printer with AskDetails {
	override def display(text: String): Unit = println(text)

	override def readLine(): String = StdIn.readLine()
}