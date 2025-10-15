import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerCount;
        // Ask the user to input the number of players until a valid integer has been inputted
        do {
            System.out.println("Please enter the number of players:");
            playerCount = scanner.nextInt();
            scanner.nextLine();
            if (playerCount < 1) { 
                System.out.println("There must be at least 1 player.");
            }
        } while (playerCount < 1);
        // Ask the user to input a file until a valid .txt file has been inputted
        System.out.println("Please enter location of pack to load:");
        String packLocation = scanner.nextLine();
        cardPack = loadPack(packLocation);
        int requiredCards = playerCount * 8;
        if (cardPack == null || cardPack.size() != requiredCards) {
            System.out.println("Error: Pack must contain exactly " + requiredCards + " cards.");
            scanner.close();
            return;
        }
    }
    private List<Integer> loadPack(String filePath) {
    File packFile = new File(filePath);
    if (!packFile.exists()) {
        System.out.println("Pack file not found.");
        return null;
    }
    List<Integer> pack = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(packFile))) {
        String line;
        int lineNumber = 1;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            // Check for empty lines
            if (line.isEmpty()) {
                System.out.println("Error: Empty line at line " + lineNumber);
                return null;
            }
            try {
                int cardValue = Integer.parseInt(line);
                if (cardValue < 0) {
                    System.out.println("Error: Negative card value at line " + lineNumber + ": " + cardValue);
                    return null;
                }
                pack.add(cardValue);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer at line " + lineNumber + ": " + line);
                return null;
            }
            lineNumber++;
        }
    } catch (IOException e) {
        System.out.println("Error reading pack file: " + e.getMessage());
        return null;
    }
    Collections.shuffle(pack);
    System.out.println("Pack loaded and shuffled successfully!");
    return pack;
    }
}