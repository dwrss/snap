import atto.ParseResult
import cli.{BothMatching, InputParser, SuitMatching, ValueMatching}
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Random

class CliSpec extends AnyWordSpec with Matchers with EitherValues {
	def provided = afterWord("provided")
	def is = afterWord("is")

	"parseNumPlayers" when provided {
		"a number" should {
			"parse" in {
				InputParser.parseNum("1") match {
					case ParseResult.Done(remainder, result) =>
						remainder shouldBe empty
						result should be(1)
					case _ => fail("Parsing should have failed")
				}
			}
		}
		"a string" should {
			"not parse" in {
				val randomString = Random.nextString(5)
				InputParser.parseNum(randomString) match {
					case ParseResult.Fail(input, _, _) =>
						input should be(randomString)
					case _ => fail("Parsing should have failed")
				}
			}
		}
	}
	"parseMatchStrategy" when provided {
		"a single character" should {
			"parse one" which is {
				"'s'" in {
					InputParser.parseMatchStrategy("s") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(SuitMatching)
						case _ => fail("Parsing should have failed")
					}
				}
				"'v'" in {
					InputParser.parseMatchStrategy("v") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(ValueMatching)
						case _ => fail("Parsing should have failed")
					}
				}
				"'b'" in {
					InputParser.parseMatchStrategy("b") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(BothMatching)
						case _ => fail("Parsing should have failed")
					}
				}
			}
			"not parse a character" which is {
				"'t'" in {
					InputParser.parseMatchStrategy("t") match {
						case ParseResult.Fail(input, _, _) =>
							input should be ("t")
						case _ => fail("Parsing should have failed")
					}
				}
			}
		}
		"a word" should {
			"parse one" which is {
				"'suit'" in {
					InputParser.parseMatchStrategy("suit") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(SuitMatching)
						case _ => fail("Parsing should have failed")
					}
				}
				"'value'" in {
					InputParser.parseMatchStrategy("value") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(ValueMatching)
						case _ => fail("Parsing should have failed")
					}
				}
				"'both'" in {
					InputParser.parseMatchStrategy("both") match {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							result should be(BothMatching)
						case _ => fail("Parsing should have failed")
					}
				}
			}
			"not parse one" which is{
				"'fooBar'" in {
					InputParser.parseMatchStrategy("fooBar") match {
						case ParseResult.Fail(input, _, _) =>
							input should be ("fooBar")
						case _ => fail("Parsing should have failed")
					}
				}
			}
		}
	}
}