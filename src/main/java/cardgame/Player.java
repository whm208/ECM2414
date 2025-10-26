package cardgame;
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

    // Constructor
    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    // Synchronized method to add a card to the player's hand
    public synchronized void addCard(Card card) {
        hand.add(card);
    }

    // Synchronized method to discard a card that does not match the player's id (preffered card)
    public synchronized Card discardCard() {
        for (int each_card = 0; each_card < hand.size(); each_card++) {
            if (hand.get(each_card).getValue() != id) {
                return hand.remove(each_card);
            }
        }
        return null;
    }

    // Synchronized method to get a copy of the player's hand
    public synchronized List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    // Method to get the player's id
    public int getId() {
        return id;
    }

    // Synchronized method to check if the player has a winning hand
    public synchronized boolean hasWinningHand() {
        if (hand.size() != 4) return false;
        int value = hand.get(0).getValue();
        for (Card each_card : hand) {
            if (each_card.getValue() != value) {
                return false;
            }
        }
        return true;
    }

    // Sets the left and right decks for the player
    public void setLeftDeck(Deck leftDeck) {
        this.leftDeck = leftDeck;
    }

    public void setRightDeck(Deck rightDeck) {
        this.rightDeck = rightDeck;
    }

    // Sets the game over flag for the player
    public void setGameOverFlag(AtomicBoolean gameOver) {
        this.gameOver = gameOver;
    }
    
    // Sets the log writer for the player with given BufferedWriter
    public void setLogWriter(BufferedWriter writer) {
    this.logWriter = writer;
    }

    // Synchronized method to log a message for the player
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

    // Method to get the log writer for the player
    public BufferedWriter getLogWriter() {
        return logWriter;
    }

    // Override toString method to represent the player's hand as a string
    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        for (Card each_card : hand) {
            stringbuilder.append(each_card.getValue()).append(" ");
        }
        return stringbuilder.toString().trim();
    }

    // Run method for the player's thread
    @Override
    public void run() {
        // Check if player already has a winning hand 
        if (hasWinningHand() && gameOver.compareAndSet(false, true)) {
            System.out.println("player " + id + " wins");
            log("player " + id + " wins");
            return;
        }
        // Main game loop
        while (!gameOver.get()) {
            try {
                // Lock decks in consistent order to prevent deadlock
                Deck firstLock = (leftDeck.getId() < rightDeck.getId()) ? leftDeck : rightDeck;
                Deck secondLock = (leftDeck.getId() < rightDeck.getId()) ? rightDeck : leftDeck;
                firstLock.lock();
                secondLock.lock();
                try {
                    // Check if game is already over
                    if (gameOver.get()) break;

                    // Draw a card from the left deck
                    Card drawnCard = leftDeck.drawCard();
                    if (drawnCard != null) {
                        addCard(drawnCard);
                        System.out.println("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                        log("player " + id + " draws a " + drawnCard.getValue() + " from deck " + leftDeck.getId());
                    }
                    if (gameOver.get()) break;

                    // Discard a card to the right deck
                    Card discarded = discardCard();
                    if (discarded != null) {
                        rightDeck.addCard(discarded);
                        System.out.println("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                        log("player " + id + " discards a " + discarded.getValue() + " to deck " + rightDeck.getId());
                    }
                    if (gameOver.get()) break;
                    System.out.println("player " + id + " current hand: " + this);

                    // Check for winning hand
                    if (hasWinningHand() && gameOver.compareAndSet(false, true)) {
                        System.out.println("player " + id + " wins");
                        log("player " + id + " wins");
                        return;
                    }
                }
                finally {
                    // Unlock decks
                    secondLock.unlock();
                    firstLock.unlock();
                }
            Thread.sleep(1);
            }   
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
                }
        }
    }
}
