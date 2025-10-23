package cardgame;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {
    private int id;
    private LinkedList<Card> cards;
    private ReentrantLock lock = new ReentrantLock();

    public Deck(int id) {
        this.id = id;
        this.cards = new LinkedList<>();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void addCard(Card card) {
        lock.lock();
        try {
            cards.addLast(card);
        } finally {
            lock.unlock();
        }
    }

    public Card drawCard() {
        lock.lock();
        try {
            if (!cards.isEmpty()) {
                return cards.removeFirst();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public List<Card> getCards() {
        lock.lock();
        try {
            return new ArrayList<>(cards);
        } finally {
            lock.unlock();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return "Deck " + id + " cards: " + cards;
        } finally {
            lock.unlock();
        }
    }
}
