import cli.{BothMatching, SuitMatching, ValueMatching}
import game.{Play, Setup}
import model.CardType.{Rank, Suit}
import model.{Card, Deck, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlaySpec extends AnyWordSpec with Matchers {
	def is = afterWord("is")
	def matchingOn = afterWord("matching on")
	// We don't really need to test that Enumeratum works, but this ensures confidence in the other tests
	"Setup" should {
		"Create players in order" in {
			val list = Setup.createPlayerList(10)
			for (i <- 1 to list.size) {
				list(i-1).id should be (i)
			}
		}
		"Create multiple decks correctly" in {
			val list = Setup.createDeck(3)
			list should equal(Deck(Deck().cards ++ Deck().cards ++ Deck().cards))
		}
	}
	"Play" when matchingOn {
		"Suit" should {
			val findPlayersFunc = Play.findPlayersWithMatch(SuitMatching.matchCards) _
			"match multiple players" in {
				val player1 = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val player2 = Player(2, Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(player1, player2)
				findPlayersFunc(Card(Rank.Queen, Suit.Spades), players) should contain allElementsOf(players)
			}
			"match one player" which is {
				"first in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(matchingPlayer, nonMatchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
				"second in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
			}
			"consider only the top card on a players stack" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
			"handle a player with no cards" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
		}
		"Value" should {
			val findPlayersFunc = Play.findPlayersWithMatch(ValueMatching.matchCards) _
			"match multiple players" in {
				val player1 = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val player2 = Player(2, Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(player1, player2)
				findPlayersFunc(Card(Rank.Queen, Suit.Spades), players) should contain allElementsOf(players)
			}
			"match one player" which is {
				"first in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(matchingPlayer, nonMatchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
				"second in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
			}
			"consider only the top card on a players stack" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
			"handle a player with no cards" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
		}
		"both" should {
			val findPlayersFunc = Play.findPlayersWithMatch(BothMatching.matchCards) _
			"match multiple players" in {
				val player1 = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val player2 = Player(2, Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(player1, player2)
				findPlayersFunc(Card(Rank.Queen, Suit.Spades), players) should contain allElementsOf(players)
			}
			"match one player" which is {
				"first in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(matchingPlayer, nonMatchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
				"second in the list" in {
					val cardToMatch = Card(Rank.Queen, Suit.Spades)
					val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
					val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Nil)
					val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Nil)
					val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
					findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
				}
			}
			"consider only the top card on a players stack" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
			"handle a player with no cards" in {
				val cardToMatch = Card(Rank.Queen, Suit.Spades)
				val matchingPlayer = Player(1, Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer = Player(2, Card(Rank.King, Suit.Diamonds) :: Card(Rank.Queen, Suit.Spades) :: Nil)
				val nonMatchingPlayer2 = Player(3, Nil)
				val players = Vector(nonMatchingPlayer, matchingPlayer, nonMatchingPlayer2)
				findPlayersFunc(cardToMatch, players) should contain only (matchingPlayer)
			}
		}
	}
}