import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerCount;
        do {
            System.out.println("Please enter the number of players:");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer:");
                scanner.next();
                }
            playerCount = scanner.nextInt();
            scanner.nextLine();
            if (playerCount < 1) { 
                System.out.println("There must be at least 1 player.");
            }
            else if (playerCount > 16) {
                System.out.println("There can be at most 16 players.");
            }
        } while (playerCount < 1 || playerCount > 16);
        String generatedFile = "generated_pack.txt";
        generateInputFile(playerCount, generatedFile);
        List<Card> cardPack = null;
        int requiredCards = playerCount * 8;
        while (true) {
            System.out.println("Please enter location of pack to load:");
            String packLocation = scanner.nextLine();
            if (!packLocation.endsWith(".txt")) {
                System.out.println("Invalid file type. Please enter a .txt file.");
                continue;
            }
            cardPack = loadPack(packLocation);
            if (cardPack == null) {
                continue;
            }
            if (cardPack.size() != requiredCards) {
                System.out.println("Error: Pack must contain exactly " + requiredCards + " cards.");
                continue;
            }
            break;
        }
        Collections.shuffle(cardPack);
        System.out.println("Pack loaded and shuffled successfully!");
        List<Player> players = new ArrayList<>();
        List<Deck> decks = new ArrayList<>();
        for (int each_object = 0; each_object < playerCount; each_object++) {
            players.add(new Player(each_object + 1));
            decks.add(new Deck(each_object + 1));
        }
        for (int each_player_card = 0; each_player_card < playerCount * 4; each_player_card++) {
            Player player = players.get(each_player_card % playerCount);
            player.addCard(cardPack.get(each_player_card));
        }
        for (int each_deck_card = playerCount * 4; each_deck_card < playerCount * 8; each_deck_card++) {
            Deck deck = decks.get(each_deck_card % playerCount);
            deck.addCard(cardPack.get(each_deck_card));
        }
        for (Player each_player : players) {
            String filename = "player" + each_player.getId() + "_output.txt";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                writer.write("player " + each_player.getId() + " initial hand:");
                System.out.print("player " + each_player.getId() + " initial hand: ");
                for (Card each_card : each_player.getHand()) {
                    writer.write(each_card.getValue() + " ");
                    System.out.print(each_card.getValue() + " ");
                    }
                writer.newLine();
                writer.flush();
                each_player.setLogWriter(writer);
                }
            catch (IOException e) {
                System.out.println("Error writing to file " + filename + ": " + e.getMessage());
            }
            System.out.println();
        }
        for (Deck each_deck : decks) {
            System.out.print("deck" + each_deck.getId() + " initial cards: ");
            for (Card each_card : each_deck.getCards()) {
                    System.out.print(each_card.getValue() + " ");
                }
            System.out.println();
        }
        AtomicBoolean gameOver = new AtomicBoolean(false);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            Player player = players.get(i);
            Deck leftDeck = decks.get(i);
            Deck rightDeck = decks.get((i + 1) % playerCount);
            player.setLeftDeck(leftDeck);
            player.setRightDeck(rightDeck);
            player.setGameOverFlag(gameOver);
            Thread new_thread = new Thread(player);
            threads.add(new_thread);
            new_thread.start();
        }

        for (Thread each_thread : threads) {
            try {
                each_thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Deck each_deck : decks) {
            String filename = "deck" + each_deck.getId() + "_output.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                List<Card> cards = each_deck.getCards();
                for (Card each_card : cards) {
                    writer.write(each_card.getValue() + " ");
                }       
                writer.newLine();
                System.out.println("Final contents of deck " + each_deck.getId() + " written to " + filename);
            }
            catch (IOException e) {
                System.out.println("Error writing deck file " + filename + ": " + e.getMessage());
            }
        }
        for (Player each_player : players) {
            BufferedWriter writer = each_player.getLogWriter();
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                    System.out.println("Error closing log file for player " + each_player.getId());
                }
            }
        }
        System.out.println("Game over!");
        scanner.close();
    }
    private static void generateInputFile(int playerCount, String filename) {
        int totalCards = playerCount * 8;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        // Write card values cycling from 1 to 13
            for (int each_card = 0; each_card < totalCards; each_card++) {
                int cardValue = ((each_card / 4) % 13) + 1;
                writer.write(String.valueOf(cardValue));
                writer.newLine();
                }
            System.out.println("Generated input file '" + filename + "' with " + totalCards + " cards.");
        } 
        catch (IOException e) {
        System.out.println("Error generating input file: " + e.getMessage());
        }
    }
    private static List<Card> loadPack(String filePath) {
    File packFile = new File(filePath);
    if (!packFile.exists()) {
        System.out.println("Pack file not found.");
        return null;
    }
    List<Card> pack = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(packFile))) {
        String line;
        int lineNumber = 1;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                System.out.println("Error: Empty line at line " + lineNumber);
                return null;
            }
            try {
                int cardValue = Integer.parseInt(line);
                if (cardValue < 1 || cardValue > 13) {
                    System.out.println("Error: Card value must be between 1 and 13 at line " + lineNumber + ": " + cardValue);
                    return null;
                }
                pack.add(new Card(cardValue));
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
    return pack;
    }
}