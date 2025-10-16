import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player extends Thread {
    private final int playerId;
    private final List<Integer> hand = Collections.synchronizedList(new ArrayList<>());
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final AtomicBoolean gameWon;

    public Player(int playerId, Deck leftDeck, Deck rightDeck, AtomicBoolean gameWon) {
        this.playerId = playerId;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.gameWon = gameWon;
    }

    // Adds a card to the player's hand (thread-safe because of synchronizedList)
    public void addCardToHand(int card) {
        hand.add(card);
    }

    // Check if player has a winning hand (e.g., all cards have the same value)
    private boolean hasWinningHand() {
        synchronized (hand) {
            if (hand.isEmpty()) return false;
            int first = hand.get(0);
            for (int card : hand) {
                if (card != first) return false;
            }
            return true;
        }
    }

    @Override
    public void run() {
        while (!gameWon.get()) {
            // Draw from left deck
            Integer drawnCard = leftDeck.drawCard();
            if (drawnCard != null) {
                addCardToHand(drawnCard);
            }

            // Discard a card (e.g., random or non-preferred card)
            int discardIndex = chooseCardToDiscard();
            int discardedCard = -1;
            synchronized (hand) {
                if (hand.size() > discardIndex) {
                    discardedCard = hand.remove(discardIndex);
                }
            }

            if (discardedCard != -1) {
                rightDeck.addCard(discardedCard);
            }

            // Check if this player has a winning hand
            if (hasWinningHand()) {
                if (gameWon.compareAndSet(false, true)) {
                    System.out.println("Player " + playerId + " wins!");
                }
            }

            // Slight delay for realism and to reduce thread contention
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Optionally print final hand
        synchronized (hand) {
            System.out.println("Player " + playerId + " final hand: " + hand);
        }
    }

    // Strategy for discarding a card (simplified)
    private int chooseCardToDiscard() {
        synchronized (hand) {
            if (hand.size() <= 1) return 0;
            // Discard a card that doesn't match the first card value
            int preferred = hand.get(0);
            for (int i = 1; i < hand.size(); i++) {
                if (hand.get(i) != preferred) return i;
            }
            return hand.size() - 1; // fallback
        }
    }
}