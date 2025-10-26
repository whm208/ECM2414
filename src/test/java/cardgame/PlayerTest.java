package cardgame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class PlayerTest {
    private Player player;

    // Create a new Player before each test
    @BeforeEach
    public void setup() {
        player = new Player(2); // preferred card is value 2
    }

    // Test adding a card to the player's hand. check hand size and card value.
    @Test
    public void addCardTest() {
        player.addCard(new Card(4));
        List<Card> hand = player.getHand();
        Assertions.assertEquals(1, hand.size());
        Assertions.assertEquals(4, hand.get(0).getValue());
    }

    // Test discarding a card when both preferred and non-preferred cards are present.
    @Test
    public void discardPreferenceTest() {
        player.addCard(new Card(2));  // preferred
        player.addCard(new Card(5));  // non-preferred
        Card discarded = player.discardCard();
        Assertions.assertEquals(5, discarded.getValue(), "Should discard non-preferred card");
    }

    // Test discarding a card when only preferred cards are present.
    @Test
    public void discardOnlyPreferredCardsTest() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Card discarded = player.discardCard();
        Assertions.assertNull(discarded, "Should return null when all cards are preferred");
    }

    // Test hasWinningHand method for winning hand.
    @Test
    public void hasWinningHandTrueTest() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Assertions.assertTrue(player.hasWinningHand(), "Hand should be winning if all cards have same value");
    }

    // Test hasWinningHand method for non-winning hand.
    @Test
    public void hasWinningHandFalseTest1() {
        player.addCard(new Card(2));
        player.addCard(new Card(3));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Assertions.assertFalse(player.hasWinningHand(), "Hand should not be winning if values differ");
    }

    // Test hasWinningHand method for wrong card cound.
    @Test
    public void hasWinningHandFalseTest2() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        // only 3 cards
        Assertions.assertFalse(player.hasWinningHand(), "Hand should not be winning if not exactly 4 cards");
    }

    // Test toString method for player's hand representation.
    @Test
    public void toStringFormattingTest() {
        player.addCard(new Card(4));
        player.addCard(new Card(6));
        Assertions.assertEquals("4 6", player.toString(), "toString should list card values separated by space");
    }
}