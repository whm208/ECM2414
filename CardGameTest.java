import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CardGameTest {

    @Test
    void testValidPackLoadsCorrectly() {
        CardGame game = new CardGame();
        List<Integer> pack = game.loadPack("pack_valid.txt");
        assertNotNull(pack);
        assertEquals(16, pack.size());
        assertEquals(1, pack.get(0));
        assertEquals(16, pack.get(15));
    }

    @Test
    void testInvalidLengthFails() {
        CardGame game = new CardGame();
        List<Integer> pack = game.loadPack("pack_invalid_length.txt");
        assertNull(pack);
    }

    @Test
    void testNegativeValueFails() {
        CardGame game = new CardGame();
        List<Integer> pack = game.loadPack("pack_invalid_negative.txt");
        assertNull(pack);
    }

    @Test
    void testInvalidTextFails() {
        CardGame game = new CardGame();
        List<Integer> pack = game.loadPack("pack_invalid_text.txt");
        assertNull(pack);
    }
}