import java.util.*;
import java.util.concurrent.*;

public class CardGame {
    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final List<Card> deck = new CopyOnWriteArrayList<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        buildDeck();
        Collections.shuffle(deck);
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        executor.execute(() -> dealCards(alice));
        executor.execute(() -> dealCards(bob));
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(alice);
        System.out.println(bob);
    }

    private static void buildDeck() {
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    private static void dealCards(Player player) {
        while (true) {
            Card card;
            synchronized (deck) {
                if (deck.isEmpty()) break;
                card = deck.remove(0);
            }
            player.receiveCard(card);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}