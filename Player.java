import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.BufferedWriter;
import java.io.IOException;

public class Player implements Runnable {
    private int id;
    private List<Card> hand;
    private Deck leftDeck;
    private Deck rightDeck;
    private AtomicBoolean gameOver;
    private BufferedWriter logWriter;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public synchronized void addCard(Card card) {
        hand.add(card);
    }

    public synchronized Card discardCard() {
    // Discard the first card which is NOT preferred
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue() != id) { // id is player's preferred card value
                return hand.remove(i);
            }
        }
    // If all cards are preferred, don't discard (return null)
        return null;
    }


    public synchronized List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public int getId() {
        return id;
    }

    public synchronized boolean hasWinningHand() {
        if (hand.size() != 4) return false;  // Must have exactly 4 cards
        int value = hand.get(0).getValue();
        for (Card each_card : hand) {
            if (each_card.getValue() != value) {
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
    
    public void setLogWriter(BufferedWriter writer) {
    this.logWriter = writer;
    }

    public synchronized void log(String message) {
        try {
            logWriter.write(message);
            logWriter.newLine();
            logWriter.flush();
        }   
        catch (IOException e) {
            System.out.println("Error writing log for player " + id + ": " + e.getMessage());
        }
    }

    public BufferedWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.getValue()).append(" ");
        }
        return sb.toString().trim();  // trim removes trailing space
    }

    @Override
    public void run() {
    // Check winning hand at start
        if (hasWinningHand() && gameOver.compareAndSet(false, true)) {
            System.out.println("player " + id + " wins");
            log("player " + id + " wins");
            return;
        }
        while (!gameOver.get()) {
            try {
            // Lock both decks in fixed order (by ID) to avoid deadlock
                Deck firstLock = (leftDeck.getId() < rightDeck.getId()) ? leftDeck : rightDeck;
                Deck secondLock = (leftDeck.getId() < rightDeck.getId()) ? rightDeck : leftDeck;
                firstLock.lock();
                secondLock.lock();
                try {
                    if (gameOver.get()) break; // Check again after acquiring lock
                    Card drawnCard = leftDeck.drawCard();
                    if (drawnCard != null) {
                        addCard(drawnCard);
                        //System.out.println("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                        System.out.println("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                        log("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                    }
                    Card discarded = discardCard();
                    if (discarded != null) {
                        rightDeck.addCard(discarded);
                        //System.out.println("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                        System.out.println("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                        log("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                    }
                    if (gameOver.get()) break;
                    System.out.println("player " + id + " current hand: " + this);
                    if (hasWinningHand() && gameOver.compareAndSet(false, true)) {
                        System.out.println("player " + id + " wins");
                        log("player " + id + " wins");
                        return;
                    }
                }
                finally {
                    secondLock.unlock();
                    firstLock.unlock();
                }
            Thread.sleep(1); // Yield control
            }   
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
                }
        }
    }
}