package cardgame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class DeckTest {
    // ---------- Deck Tests ----------
    private Deck deck;

    // Create a new Deck before each test
    @BeforeEach
    public void setup() {
        deck = new Deck(1);
    }

    // Test adding a card and drawing it
    @Test
    public void testAddAndDrawCard() {
        Card card = new Card(7);
        deck.addCard(card);
        Card drawn = deck.drawCard();
        Assertions.assertEquals(7, drawn.getValue(), "Should draw the card added"); // FIFO 
    }

    // Test drawing from an empty deck, should return null
    @Test
    public void testDrawFromEmptyDeck() {
        Card drawn = deck.drawCard();
        Assertions.assertNull(drawn, "Drawing from empty deck should return null");
    }

    // Test that getCards returns a copy of the internal list
    @Test
    public void testGetCardsReturnsCopy() {
        deck.addCard(new Card(5));
        List<Card> cards1 = deck.getCards();
        List<Card> cards2 = deck.getCards();
        Assertions.assertNotSame(cards1, cards2, "getCards should return a new list each time");
        Assertions.assertEquals(cards1, cards2, "Both copies should have the same contents");
    }

    // Test deck ID is set correctly
    @Test
    public void testDeckId() {
        Assertions.assertEquals(1, deck.getId(), "Deck ID should be set correctly");
    }

    // Test toString method formats correctly
    @Test
    public void testToStringFormat() {
        deck.addCard(new Card(3));
        deck.addCard(new Card(8));
        String expected = "Deck 1 cards: [3, 8]";
        Assertions.assertEquals(expected, deck.toString(), "Deck toString should format cards properly");
    }
}