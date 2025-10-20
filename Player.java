import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Player implements Runnable {
    private int id; // Player ID
    private List<Card> hand; // Player's hand of cards
    private List<Deck> decks; // Reference to the list of decks
    private volatile boolean endGame = false; // Flag to indicate if the game has ended
    private int prefferedCard; // Preferred card value
    private int discardIndex = 0; // Index to track which card to discard next

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
        this.prefferedCard = id;
    }

    // Adds a card to the player's hand.
    public void addCard(Card card) {
        hand.add(card);
    }

    // Returns the player's hand.
    public List<Card> getHand() {
        return hand;
    }

    // Returns the player's ID.
    public int getId() {
        return id;
    }

    // Checks if the player has a winning hand (all cards have the same value).
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


    // Plays a turn for the player.
    public void playTurn(List<Deck> decks, int playerCount) throws InterruptedException {
        Deck drawDeck = decks.get(id - 1);
        Deck discardDeck = decks.get(id % playerCount);

        // Ensure atomic operations on drawing and discarding cards.
        synchronized(this){
            Card drawnCard = drawDeck.drawCard();
            if (drawnCard != null) {
                hand.add(drawnCard);
                // Write draw action to player's output file.
                String filename = "player" + id + "_output.txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.write("player " + id + " draws a " + drawnCard.getValue() + " from deck " + drawDeck.getId());
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error writing to file " + filename + ": " + e.getMessage());
                }
                System.out.println("Player " + id + " drew a " + drawnCard.getValue() + " from deck " + drawDeck.getId());
            } else {
                return;
            }
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(discardIndex).getValue() != prefferedCard) {
                    Card discardedCard = hand.remove(discardIndex);
                    discardDeck.addCard(discardedCard);
                    // Write discard action to player's output file.
                    String filename = "player" + id + "_output.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                        writer.write("player " + id + " discards a " + discardedCard.getValue() + " to deck " + discardDeck.getId());
                        writer.newLine();
                    } catch (IOException e) {
                        System.out.println("Error writing to file " + filename + ": " + e.getMessage());
                    }
                    System.out.println("Player " + id + " discarded a " + discardedCard.getValue() + " to deck " + discardDeck.getId());
                    if (discardIndex == 3){
                        discardIndex = 0;
                    } else {
                        discardIndex += 1;
                    }
                    break;
                } else {
                    if (discardIndex == 3){
                        discardIndex = 0;
                    } else {
                        discardIndex += 1;
                    }
                }
            }
            
        }
            
    }

    // The main run method for the player thread.
    @Override
    public void run() {
        while (!endGame){
            try {
                playTurn(decks, decks.size());
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Player " + id + " interrupted: " + e.getMessage());
            }
            

        }
        
    }

    // Sets the reference to the list of decks.
    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    // Signals the player to end the game.
    public void endGame() {
        this.endGame = true;
    }

    @Override
    public String toString() {
        return "Player " + id + " current hand: " + hand;
    }
}