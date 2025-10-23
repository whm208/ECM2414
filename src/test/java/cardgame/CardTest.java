package cardgame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CardTest{
    @Test
    public void getValueTest() {
        Card card = new Card(5);
        Assertions.assertEquals(5, card.getValue(), "Card value should be 5");
    }

    @Test
    public void toStringTest() {
        Card card = new Card(10);
        Assertions.assertEquals("10", card.toString(), "toString should return string of card value");
    }
}