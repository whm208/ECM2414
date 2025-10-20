import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Deck {
    private int id; // Deck ID
    private BlockingQueue<Card> cards; // Thread-safe queue for cards

    public Deck(int id) {
        this.id = id;
        this.cards = new LinkedBlockingQueue<>();
    }

    // Adds a card to the deck.
    public void addCard(Card card) throws InterruptedException {
        cards.put(card);
    }

    // Draws a card from the deck.
    public Card drawCard() throws InterruptedException {
        return cards.take();
    }

    // Returns a list of cards currently in the deck.
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    // Returns the deck's ID.
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Deck " + id + " cards: " + cards;
    }
}