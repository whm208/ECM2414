import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private int id;
    private List<Card> hand;
    private Deck leftDeck;
    private Deck rightDeck;
    private AtomicBoolean gameOver;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public synchronized Card discardCard() {
    if (!hand.isEmpty()) {
        return hand.remove(0);
    }
    return null;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getId() {
        return id;
    }

    public boolean hasWinningHand() {
    if (hand.isEmpty()) return false;
    int value = hand.get(0).getValue();
    for (Card card : hand) {
        if (card.getValue() != value) {
            return false;
        }
    }
    return true;
    }

    public void setLeftDeck(Deck leftDeck) {
        this.leftDeck = leftDeck;
    }

    public void setRightDeck(Deck rightDeck) {
        this.rightDeck = rightDeck;
    }

    public void setGameOverFlag(AtomicBoolean gameOver) {
        this.gameOver = gameOver;
    }
    
    @Override
    public String toString() {
        return "Player " + id + " current hand: " + hand;
    }

    @Override
    public void run() {
        while (!gameOver.get()) {
            try {
                Card drawnCard = leftDeck.drawCard();  // Assume Deck has synchronized drawCard method
                if (drawnCard != null) {
                    synchronized (hand) {
                        hand.add(drawnCard);
                    }
                }
            // Check if player has winning hand
            if (hasWinningHand()) {
                System.out.println("Player " + id + " wins!");
                gameOver.set(true);
                break;
            }

            // Discard a card to rightDeck
            Card discarded = discardCard();
            if (discarded != null) {
                rightDeck.addCard(discarded);  // Assume Deck has synchronized addCard method
            }

            // Sleep or yield so other players can proceed
            Thread.sleep(100);

        }   catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            break;
        }
    }
    System.out.println("Player " + id + " finished.");
    }
}