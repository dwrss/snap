package cli
import atto._
import Atto._
import cats.syntax.either._
import model.Card

trait Msg
case class InvalidInput(message: String) extends Msg
case class NumPlayers(message: Int) extends Msg
case class MatchingMode(choice: MatchChoice) extends Msg

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

object InputParser {
	val deckParser: Parser[Int] = int
	val numParser: Parser[Option[Int]] = opt(int)
	def parseDeck(i: String): ParseResult[Int] = deckParser.parseOnly(i)
	def parseNumPlayers(i: String): ParseResult[Option[Int]] = numParser.parseOnly(i)
//		.either.getOrElse(InvalidInput("Invalid number of players"))

	val suitParser: Parser[SuitMatching.type] = (stringCI("suit") | char('s')) >| SuitMatching
	val valueParser: Parser[ValueMatching.type] = (stringCI("value") | char('v')) >| ValueMatching
	val bothParser: Parser[BothMatching.type] = (stringCI("both") | char('b')) >| BothMatching
	val matchStrategyParser: Parser[MatchChoice] = choice(suitParser, valueParser, bothParser)

	def parseMatchStrategy(i: String): ParseResult[MatchChoice] = matchStrategyParser.parseOnly(i)
//		.either
//		.getOrElse(InvalidInput("Invalid "))
}
