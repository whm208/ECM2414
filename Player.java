import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Player {
    private int id;
    private List<Card> hand;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
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


    
    public void playTurn(List<Deck> decks, int playerCount) throws InterruptedException {
        Deck drawDeck = decks.get(id - 1);
        Deck discardDeck = decks.get(id % playerCount);

        synchronized(this){
            Card drawnCard = drawDeck.drawCard();
            if (drawnCard != null) {
                hand.add(drawnCard);
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
            if (!hand.isEmpty()) {
                Card discardedCard = hand.remove(0); // doesnt have "strategy" yet
                discardDeck.addCard(discardedCard);
                String filename = "player" + id + "_output.txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.write("player " + id + " discards a " + discardedCard.getValue() + " to deck " + discardDeck.getId());
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error writing to file " + filename + ": " + e.getMessage());
                }
                System.out.println("Player " + id + " discarded a " + discardedCard.getValue() + " to deck " + discardDeck.getId());
            }
        }
            
    }

    @Override
    public String toString() {
        return "Player " + id + " current hand: " + hand;
    }
}