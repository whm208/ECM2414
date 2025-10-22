
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;

public class DeckTest{
    @Test
    public void lockTest() {
        lock.lock();
    }

    @Test
    public void unlockTest() {
        lock.unlock();
    }

    @Test
    public void addCardTest(Card card) {
        lock.lock();
        try {
            cards.addLast(card);  // add to the back (bottom)
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void drawCardTest() {
        lock.lock();
        try {
            if (!cards.isEmpty()) {
                return cards.removeFirst();  // remove from the front (top)
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void getCardsTest() {
        lock.lock();
        try {
            return new ArrayList<>(cards);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void getIdTest() {
        return id;
    }

    @Test
    public void toStringTest() {
        lock.lock();
        try {
            return "Deck " + id + " cards: " + cards;
        } finally {
            lock.unlock();
        }
    }
}