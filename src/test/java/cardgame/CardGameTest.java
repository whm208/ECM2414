package cardgame;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class CardGameTest {
    // ---------- loadPack Tests ----------

    // Test loading a valid pack file, where pack contains 8 cards, all with value 5
    @Test
    public void testLoadPackValidFile(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("validPack.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 8; i++) {
                writer.write("5");
                writer.newLine();
            }
        }

        List<Card> cards = CardGame.loadPack(file.getAbsolutePath());
        assertNotNull(cards, "Valid pack file should return non-null list");
        assertEquals(8, cards.size(), "Pack should contain 8 cards");
        assertTrue(cards.stream().allMatch(c -> c.getValue() == 5), "All card values should be 5");
    }

    // Test loading a pack file thats invalid.
    @Test
    public void testLoadPackInvalidCardValue(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("invalidValue.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("5\n");
            writer.write("0\n"); // Invalid
            writer.write("7\n");
        }

        List<Card> cards = CardGame.loadPack(file.getAbsolutePath());
        assertNull(cards, "Pack with invalid card value should return null");
    }

    // Test loading a pack file with an empty line.
    @Test
    public void testLoadPackEmptyLine(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("emptyLine.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("1\n\n3\n");  // empty line
        }

        List<Card> cards = CardGame.loadPack(file.getAbsolutePath());
        assertNull(cards, "Pack with empty line should return null");
    }

    // Test loading a non-existent pack file.
    @Test
    public void testLoadPackNonExistentFile() {
        List<Card> cards = CardGame.loadPack("non_existent_file.txt");
        assertNull(cards, "Non-existent file should return null");
    }

    // ---------- generateInputFile Tests ----------

    // Test generating an input file for 4 players and verify it creates the correct number of cards
    @Test
    public void testGenerateInputFileCreatesCorrectNumberOfCards(@TempDir Path tempDir) throws IOException {
        String filename = tempDir.resolve("generated.txt").toString();
        int playerCount = 4;
        CardGame.generateInputFile(playerCount, filename);

        List<String> lines = java.nio.file.Files.readAllLines(Path.of(filename));
        assertEquals(playerCount * 8, lines.size(), "File should contain exactly playerCount × 8 lines");
    }

    // Test generating an input file and verify all card values are within the valid range (1-13)
    @Test
    public void testGeneratedFileCardValuesInRange(@TempDir Path tempDir) throws IOException {
        String filename = tempDir.resolve("generated.txt").toString();
        CardGame.generateInputFile(4, filename);

        List<String> lines = java.nio.file.Files.readAllLines(Path.of(filename));
        for (String line : lines) {
            int value = Integer.parseInt(line.trim());
            assertTrue(value >= 1 && value <= 13, "Card value should be in range 1-13, found: " + value);
        }
    }
}