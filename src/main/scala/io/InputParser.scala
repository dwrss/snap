package io
import atto._
import Atto._
import cats.syntax.either._
import model.Card

sealed trait Msg[R]
final case class ConfigInput[R](num: R) extends Msg[R]
//case class InvalidInput(message: String) extends Msg[Nothing]
//sealed trait MatchInput extends Msg
//final case class MatchingMode(choice: MatchChoice) extends MatchInput
case object Quit extends Msg[Nothing]

trait MatchChoice {
	def matchCards(a: Card, b: Card): Boolean
}
case object SuitMatching extends MatchChoice {
	override def matchCards(a: Card, b: Card): Boolean = a.suite == b.suite
}
case object ValueMatching extends MatchChoice {
	override def matchCards(a: Card, b: Card): Boolean = a.rank == b.rank
}
case object BothMatching extends MatchChoice {
	override def matchCards(a: Card, b: Card): Boolean = a == b
}

object CommandParsers {
	val quit: Parser[Quit.type] = token(string("quit") | string("q")) >| Quit
	val cmd: Parser[Msg[Nothing]] = quit
}

object ConfigParsers {
	val deckParser: Parser[ConfigInput[Int]] = int.map(ConfigInput[Int])
	val playersParser: Parser[ConfigInput[Int]] = int.map(ConfigInput[Int])
}


object InputParser {
	type ParametisedMsg[R] = Msg[_ <: R]
	val commandOrDeckParser: Parser[ParametisedMsg[Int]] = CommandParsers.cmd | ConfigParsers.deckParser
	val commandOrPlayersParser: Parser[ParametisedMsg[Int]] = CommandParsers.cmd | ConfigParsers.playersParser
	def parseDeck(i: String): ParseResult[ParametisedMsg[Int]] = commandOrDeckParser.parseOnly(i)
	def parseNumPlayers(i: String): ParseResult[ParametisedMsg[Int]] = commandOrPlayersParser.parseOnly(i)
//		.either.getOrElse(InvalidInput("Invalid number of players"))

	val suitParser: Parser[SuitMatching.type] = (stringCI("suit") | char('s')) >| SuitMatching
	val valueParser: Parser[ValueMatching.type] = (stringCI("value") | char('v')) >| ValueMatching
	val bothParser: Parser[BothMatching.type] = (stringCI("both") | char('b')) >| BothMatching
	val matchStrategyParser: Parser[ConfigInput[MatchChoice]] = choice(suitParser, valueParser, bothParser).map(ConfigInput[MatchChoice])

	val commandOrMatchStrategyParser: Parser[ParametisedMsg[MatchChoice]] = CommandParsers.cmd | matchStrategyParser
	def parseMatchStrategy(i: String): ParseResult[ParametisedMsg[MatchChoice]] = commandOrMatchStrategyParser.parseOnly(i)
//		.either
//		.getOrElse(InvalidInput("Invalid "))
}
