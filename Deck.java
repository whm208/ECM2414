import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Deck {
    private int id;
    private BlockingQueue<Integer> cards;

    public Deck(int id) {
        this.id = id;
        this.cards = new LinkedBlockingQueue<>();
    }

    public void addCard(int card) throws InterruptedException {
        cards.put(card);
    }

    public List<Integer> getCards() {
        return new ArrayList<>(cards);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Deck " + id + " cards: " + cards;
    }
}
