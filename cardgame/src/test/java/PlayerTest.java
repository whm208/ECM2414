
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerTest{
    @Test
    public synchronized void addCardTest(Card card) {
        hand.add(card);
    }

    @Test
    public synchronized void discardCardTest() {
    // Discard the first card which is NOT preferred
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue() != id) { // id is player's preferred card value
                return hand.remove(i);
            }
        }
    // If all cards are preferred, don't discard (return null)
        return null;
    }

    @Test
    public synchronized void getHandTest() {
        return new ArrayList<>(hand);
    }

    @Test
    public void getIdTest() {
        return id;
    }

    @Test
    public synchronized void hasWinningHandTest() {
        if (hand.size() != 4) return false;  // Must have exactly 4 cards
        int value = hand.get(0).getValue();
        for (Card each_card : hand) {
            if (each_card.getValue() != value) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void setLeftDeckTest(Deck leftDeck) {
        this.leftDeck = leftDeck;
    }

    @Test
    public void setRightDeckTest(Deck rightDeck) {
        this.rightDeck = rightDeck;
    }

    @Test
    public void setGameOverFlagTest(AtomicBoolean gameOver) {
        this.gameOver = gameOver;
    }
    
    @Test
    public void setLogWriterTest(BufferedWriter writer) {
    this.logWriter = writer;
    }

    @Test
    public synchronized void logTest(String message) {
        try {
            logWriter.write(message);
            logWriter.newLine();
            logWriter.flush();
        }   
        catch (IOException e) {
            System.out.println("Error writing log for player " + id + ": " + e.getMessage());
        }
    }

    @Test
    public void getLogWriterTest() {
        return logWriter;
    }

    @Test
    public void toStringTest() {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.getValue()).append(" ");
        }
        return sb.toString().trim();  // trim removes trailing space
    }

    @Test
    public void runTest() {
    }
}