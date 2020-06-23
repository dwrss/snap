import atto.ParseResult
import io.{BothMatching, ConfigInput, InputParser, SuitMatching, ValueMatching}
import org.scalatest.{EitherValues, Inside}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Random

class CliSpec extends AnyWordSpec with Matchers with EitherValues with Inside {
	def provided = afterWord("provided")
	def is = afterWord("is")

	"parseNumPlayers" when provided {
		"a number" should {
			"parse" in {
				inside(InputParser.parseNumPlayers("1")) {
					case ParseResult.Done(remainder, result) =>
						remainder shouldBe empty
						inside(result) {
							case ConfigInput(num) => num should be(1)
						}
					case _ => fail("Parsing should have failed")
				}
			}
		}
		"a string" should {
			"not parse" in {
				val randomString = Random.nextString(5)
				inside(InputParser.parseNumPlayers(randomString)) {
					case ParseResult.Fail(input, _, _) =>
						input should be(randomString)
				}
			}
		}
	}
	"parseMatchStrategy" when provided {
		"a single character" should {
			"parse one" which is {
				"'s'" in {
					inside(InputParser.parseMatchStrategy("s")) {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(SuitMatching)
							}
					}
				}
				"'v'" in {
					inside(InputParser.parseMatchStrategy("v")) {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(ValueMatching)
							}
					}
				}
				"'b'" in {
					inside(InputParser.parseMatchStrategy("b")) {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(BothMatching)
							}
					}
				}
			}
			"not parse a character" which is {
				"'t'" in {
					inside(InputParser.parseMatchStrategy("t")){
						case ParseResult.Fail(input, _, _) =>
							input should be ("t")
					}
				}
			}
		}
		"a word" should {
			"parse one" which is {
				"'suit'" in {
					inside(InputParser.parseMatchStrategy("suit")) {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(SuitMatching)
							}
					}
				}
				"'value'" in {
					inside(InputParser.parseMatchStrategy("value")){
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(ValueMatching)
							}
					}
				}
				"'both'" in {
					inside(InputParser.parseMatchStrategy("both")) {
						case ParseResult.Done(remainder, result) =>
							remainder shouldBe empty
							inside(result) {
								case ConfigInput(result) => result should be(BothMatching)
							}
					}
				}
			}
			"not parse one" which is{
				"'fooBar'" in {
					inside(InputParser.parseMatchStrategy("fooBar")){
						case ParseResult.Fail(input, _, _) =>
							input should be ("fooBar")
					}
				}
			}
		}
	}
}