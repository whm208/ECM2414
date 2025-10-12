import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private final String name;
    private final List<Card> hand = new CopyOnWriteArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public Card playCard() {
        if (hand.isEmpty()) return null;
        return hand.remove(0);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    }
}
