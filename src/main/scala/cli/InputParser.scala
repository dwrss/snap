package cli
import atto._, Atto._
import cats.syntax.either._

trait Msg
case class InvalidInput(message: String) extends Msg
case class NumPlayers(message: Int) extends Msg
case class MatchingMode(choice: MatchChoice) extends Msg

trait MatchChoice
case object SuitMatching extends MatchChoice
case object ValueMatching extends MatchChoice
case object BothMatching extends MatchChoice

object InputParser {
	val numParser = int
	def parseNum(i: String): ParseResult[Int] = numParser.parseOnly(i)
//		.either.getOrElse(InvalidInput("Invalid number of players"))

	val suitParser: Parser[SuitMatching.type] = (stringCI("suit") | char('s')) >| SuitMatching
	val valueParser: Parser[ValueMatching.type] = (stringCI("value") | char('v')) >| ValueMatching
	val bothParser: Parser[BothMatching.type] = (stringCI("both") | char('b')) >| BothMatching
	val matchStrategyParser: Parser[MatchChoice] = choice(suitParser, valueParser, bothParser)

	def parseMatchStrategy(i: String): ParseResult[MatchChoice] = matchStrategyParser.parseOnly(i)
//		.either
//		.getOrElse(InvalidInput("Invalid "))
}
