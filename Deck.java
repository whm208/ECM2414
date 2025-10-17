import java.util.ArrayList;
import java.util.List;

public class Deck {
    private int id;
    private List<Integer> cards;

    public Deck(int id) {
        this.id = id;
        this.cards = new ArrayList<>();
    }

    public void addCard(int card) {
        cards.add(card);
    }

    public List<Integer> getCards() {
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
