import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerCount;

        // Let user input number of players between 1 and 8, repeating prompt until valid input.
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
            else if (playerCount > 8) {
                System.out.println("There can be at most 8 players.");
            }
        } while (playerCount < 1 || playerCount > 8);

        // Load and validate card pack from user-specified file.
        // Size of the pack is based on the number of players.
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

        //shuffles the loaded pack, displayes to that to the console.
        Collections.shuffle(cardPack);
        System.out.println("Pack loaded and shuffled successfully!");
        List<Player> players = new ArrayList<>();
        List<Deck> decks = new ArrayList<>();

        // Initializes players and decks, deals cards to players and decks from the shuffled pack.
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
            try {
                deck.addCard(cardPack.get(each_deck_card));
            } catch (InterruptedException e) {
                System.out.println("Error adding card to deck: " + e.getMessage());
            }
        }
        // Gives each player a reference to the list of decks.
        for (Player player : players) {
            player.setDecks(decks);
        }
        // Creates the output files for each player.
        // Starts by giving each player their initial hand.
        for (Player player : players) {
            String filename = "player" + player.getId() + "_output.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("player " + player.getId() + " initial hand: ");
                System.out.print("player " + player.getId() + " initial hand: ");
                for (Card card : player.getHand()) {
                    writer.write(String.valueOf(card.getValue()));
                    System.out.print(card.getValue());
                }
            writer.newLine();
            }
            catch (IOException e) {
                System.out.println("Error writing to file " + filename + ": " + e.getMessage());
            }
            System.out.println();
        }

        // Displays the initial cards in each deck to the console.
        for (Deck deck : decks) {
            System.out.print("deck" + deck.getId() + " initial cards: ");
            for (Card card : deck.getCards()) {
                    System.out.print(card.getValue());
                }
            System.out.println();
        }
        scanner.close();

        // Creates and starts plyayer threads.
        List<Thread> playerThreads = new ArrayList<>();
        for (Player player : players) {
            playerThreads.add(new Thread(player));
            playerThreads.get(player.getId() - 1).start();
        }

        // Monitors for a winning player.
        while (true) {
            boolean gameWon = false;
            for (Player player : players) {
                if (player.hasWinningHand()) {
                    int winningId = player.getId();
                    System.out.println("Player " + player.getId() + " wins!");

                    // Notify all players to end the game.
                    for (Player p : players) {
                        p.endGame();
                    }
                    gameWon = true;
                    
                    // Write winning and exiting messages to winners player's output file.
                    String filename = "player" + winningId + "_output.txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                        writer.write("player " + winningId + " wins");
                        writer.newLine();
                        writer.write("player " + winningId + " exits");
                        writer.newLine();
                    } catch (IOException e) {
                        System.out.println("Error writing to file " + filename + ": " + e.getMessage());
                    }

                    // Write informing and exiting messages to other players' output files.
                    for (Player p : players) {
                        if (p.getId() != winningId) {
                            String otherFilename = "player" + p.getId() + "_output.txt";
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(otherFilename, true))) {
                                writer.write("player " + winningId + " has informed player " + p.getId() + " that player " + winningId + " has won");
                                writer.newLine();
                                writer.write("player " + p.getId() + " exits");
                                writer.newLine();
                            } catch (IOException e) {
                                System.out.println("Error writing to file " + otherFilename + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    break;
                }
            }
            if (gameWon) {
                break;
            }
        }
        // Waits for all player threads to finish.
        for (Thread t : playerThreads) {
            try {
                t.join();  // waits until thread completes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
}
        // Writes the final state of each deck to its output file.
        for (Deck d : decks) {
            String filename = "deck" + d.getId() + "_output.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write(d.toString());
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error writing to file " + filename + ": " + e.getMessage());
            }
        }
    }

    // Loads and validates a card pack from the specified file path.
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
                if (cardValue < 1) {
                    System.out.println("Error: Card value must be larger than 0 " + lineNumber + ": " + cardValue);
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