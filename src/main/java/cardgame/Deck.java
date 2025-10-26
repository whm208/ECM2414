package cardgame;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {
    private int id;
    private LinkedList<Card> cards;
    private ReentrantLock lock = new ReentrantLock();

    // Constructor
    public Deck(int id) {
        this.id = id;
        this.cards = new LinkedList<>();
    }

    // Methods to lock and unlock the deck
    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    // Method to add card to the deck
    public void addCard(Card card) {
        cards.addLast(card);   // ← no internal lock
    }

    // Method to draw a card from the deck, from the "top" (front of the list)
    public Card drawCard() {
        if (!cards.isEmpty()) return cards.removeFirst();
        return null;
    }

    // Method to get a copy of the cards in the deck
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    // Method to get the deck's id
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Deck " + id + " cards: " + cards;
    }
}