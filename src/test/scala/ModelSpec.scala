import ModelSpec.{getSuitFaces, getSuitNonFaces}
import model.CardType.{Rank, Suit}
import model.{Card, Deck}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ModelSpec extends AnyWordSpec with Matchers {
	// We don't really need to test that Enumeratum works, but this ensures confidence in the other tests
	"Rank" should {
		"contain 3 face cards" in {
			Rank.faceCards should have length 3
		}
		"contain Jack" in {
			Rank.faceCards should contain (Rank.Jack)
		}
		"contain Queen" in {
			Rank.faceCards should contain (Rank.Queen)
		}
		"contain King" in {
			Rank.faceCards should contain (Rank.King)
		}
	}

	val faceCardsAllSuits = getSuitFaces(Suit.Hearts) :::
		getSuitFaces(Suit.Clubs) :::
		getSuitFaces(Suit.Diamonds) :::
		getSuitFaces(Suit.Spades)

	val nonFaceCardsAllSuits = getSuitNonFaces(Suit.Hearts) :::
		getSuitNonFaces(Suit.Clubs) :::
		getSuitNonFaces(Suit.Diamonds) :::
		getSuitNonFaces(Suit.Spades)

	"A deck" when {
		"listing all cards" should {
			"return a List" in {
				Deck.allCards shouldBe a[List[_]]
			}
			"have 52 cards" in {
				Deck.allCards should have length 52
			}
			"contain one of each face card" in {
				faceCardsAllSuits map (card => Deck.allCards should contain(card))
			}
			"contain one of each non-face card" in {
				nonFaceCardsAllSuits map (card => Deck.allCards should contain(card))
			}
		}
	}

	"A card" should {
		"equal a card of the same rank and suit" in {
			Card(Rank.Ace, Suit.Spades) should equal (Card(Rank.Ace, Suit.Spades))
		}
		"not equal a card of the same rank but different suit" in {
			Card(Rank.Ace, Suit.Spades) should not equal (Card(Rank.Ace, Suit.Clubs))
		}
		"not equal a card of the same suit but different rank" in {
			Card(Rank.Ace, Suit.Spades) should not equal (Card(Rank.Queen, Suit.Spades))
		}
		"not equal a card of different suit and rank" in {
			Card(Rank.Ace, Suit.Spades) should not equal (Card(Rank.Queen, Suit.Hearts))
		}
	}
}

object ModelSpec {
	def getSuitFaces(suit: Suit): List[Card] = Rank.faceCards map (rank => Card(rank, suit))
	def getSuitNonFaces(suit: Suit): List[Card] = Rank.nonFaceCards map (rank => Card(rank, suit))
}