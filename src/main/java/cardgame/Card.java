package cardgame;
public class Card {
    private final int value;

    // Constructor
    public Card(int value) {
        this.value = value;
    }

    // Method to get the card's value
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}