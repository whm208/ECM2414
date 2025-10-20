import java.util.ArrayList;
import java.util.List;

public class Deck {
    private int id;
    private List<Card> cards;

    public Deck(int id) {
        this.id = id;
        this.cards = new ArrayList<>();
    }

    public synchronized void addCard(Card card) {
        cards.add(card);
    }

    public synchronized Card drawCard() {
    if (!cards.isEmpty()) {
        return cards.remove(0);
    }
    return null;
    }

    public synchronized List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public int getId() {
        return id;
    }

    @Override
    public synchronized String toString() {
        return "Deck " + id + " cards: " + cards;
    }
}