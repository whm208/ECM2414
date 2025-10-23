package cardgame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player(2); // preferred card is value 2
    }

    @Test
    public void testAddCard() {
        player.addCard(new Card(4));
        List<Card> hand = player.getHand();
        Assertions.assertEquals(1, hand.size());
        Assertions.assertEquals(4, hand.get(0).getValue());
    }

    @Test
    public void testDiscardCardPrefersNonPreferred() {
        player.addCard(new Card(2));  // preferred
        player.addCard(new Card(5));  // non-preferred
        Card discarded = player.discardCard();
        Assertions.assertEquals(5, discarded.getValue(), "Should discard non-preferred card");
    }

    @Test
    public void testDiscardCardAllPreferred() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Card discarded = player.discardCard();
        Assertions.assertNull(discarded, "Should return null when all cards are preferred");
    }

    @Test
    public void testHasWinningHandTrue() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Assertions.assertTrue(player.hasWinningHand(), "Hand should be winning if all cards have same value");
    }

    @Test
    public void testHasWinningHandFalseDueToDifferentValues() {
        player.addCard(new Card(2));
        player.addCard(new Card(3));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        Assertions.assertFalse(player.hasWinningHand(), "Hand should not be winning if values differ");
    }

    @Test
    public void testHasWinningHandFalseDueToWrongCardCount() {
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        // only 3 cards
        Assertions.assertFalse(player.hasWinningHand(), "Hand should not be winning if not exactly 4 cards");
    }

    @Test
    public void testToStringFormat() {
        player.addCard(new Card(4));
        player.addCard(new Card(6));
        Assertions.assertEquals("4 6", player.toString(), "toString should list card values separated by space");
    }
}