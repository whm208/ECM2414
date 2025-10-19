import java.util.ArrayList;
import java.util.List;

public class Deck {
    private int id;
    private List<Card> cards;

    public Deck(int id) {
        this.id = id;
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Deck " + id + " cards: " + cards;
    }
}