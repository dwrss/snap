import cli.BothMatching
import model.{Deck, GameConfig, InProgress, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers {
	def is = afterWord("is")
	def matchingOn = afterWord("matching on")
	// We don't really need to test that Enumeratum works, but this ensures confidence in the other tests
	"GameState" when {
		"InProgress" should {
			"calling nextPlayer twice in a row" in {
				val state = InProgress(GameConfig(3, 3, BothMatching), Deck().shuffled, Vector(Player(1), Player(2), Player(3)))
				val firstPlayer = state.nextPlayer
				val secondPlayer = state.nextPlayer
				firstPlayer should be (Player(1))
				secondPlayer should be (Player(2))
			}
			"calling nextPlayer after updating state" in {
				val state = InProgress(GameConfig(3, 3, BothMatching), Deck().shuffled, Vector(Player(1), Player(2), Player(3)))
				val (card, remainingDeck) = state.deck.dealCard
				val player = state.nextPlayer
				player should be (Player(1))
				val playerWithCard = player.addCard(card)
				println(s"Player ${playerWithCard.id}'s turn'")
				val newState = state.withUpdatedPlayerAndDeck(playerWithCard, remainingDeck)
				newState.nextPlayer.id should be (2)
			}
		}
	}
}