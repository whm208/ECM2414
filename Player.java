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

    public synchronized boolean hasWinningHand() {
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
    
    public void setLogWriter(BufferedWriter writer) {
    this.logWriter = writer;
    }

    private synchronized void log(String message) {
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
        return "Player " + id + " current hand: " + hand;
    }

    @Override
    public void run() {
        while (!gameOver.get()) {
            try {
                Card drawnCard = leftDeck.drawCard();  // Assume Deck has synchronized drawCard method
                if (drawnCard != null) {
                    addCard(drawnCard);
                    System.out.println("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                    log("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                }
                // Check if player has winning hand
                if (hasWinningHand()) {
                    System.out.println("player " + id + " wins");
                    log("player " + id + " wins");
                    gameOver.set(true);
                    break;
                }
                // Discard a card to rightDeck
                Card discarded = discardCard();
                if (discarded != null) {
                    rightDeck.addCard(discarded); 
                    System.out.println("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                    log("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                    // Assume Deck has synchronized addCard method
                }
                // Sleep or yield so other players can proceed
                Thread.sleep(100);
                }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            break;
        }
    }
    System.out.println("player " + id + " finished");
    }
}