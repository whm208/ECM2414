import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private List<Integer> hand;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public void addCard(int card) {
        hand.add(card);
    }

    public List<Integer> getHand() {
        return hand;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Player " + id + " hand: " + hand;
    }
}
