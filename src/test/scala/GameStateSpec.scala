import io.BothMatching
import model.{Deck, GameConfig, InProgress, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers {
	def is = afterWord("is")
	def matchingOn = afterWord("matching on")
	// We don't really need to test that Enumeratum works, but this ensures confidence in the other tests
	"InProgress" when {
		"called twice in a row" should {
			"return sequential players " in {
				val state = InProgress(GameConfig(3, 3, BothMatching), Deck().shuffled, Vector(Player(1), Player(2), Player(3)))
				val firstPlayer = state.currentPlayer
				val secondPlayer = state.nextPlayer.currentPlayer
				firstPlayer should be (Player(1))
				secondPlayer should be (Player(2))
			}
			"return sequential players after updating state" in {
				val state = InProgress(GameConfig(3, 3, BothMatching), Deck().shuffled, Vector(Player(1), Player(2), Player(3)))
				val (card, remainingDeck) = state.deck.dealCard
				val player = state.currentPlayer
				player should be (Player(1))
				val playerWithCard = player.prependCard(card)
				val newState = state.withUpdatedPlayerAndDeck(playerWithCard, remainingDeck).nextPlayer
				newState.currentPlayer.id should be (2)
			}

		}
		"moving to a new round" should {
			"return the updated player from the previous round" in {
				val state = InProgress(GameConfig(3, 3, BothMatching), Deck().shuffled, Vector(Player(1), Player(2), Player(3)))
				val (card, remainingDeck) = state.deck.dealCard
				val player = state.currentPlayer
				player should be (Player(1))
				val playerWithCard = player.prependCard(card)
				val newState = state.withUpdatedPlayerAndDeck(playerWithCard, remainingDeck)
				val cycledState = newState.nextPlayer.nextPlayer.nextPlayer
				val samePlayer = cycledState.currentPlayer
				samePlayer should be (playerWithCard)
			}
		}
	}
}