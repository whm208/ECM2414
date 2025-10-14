import java.util.*;
import java.util.concurrent.*;
import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerCount;
        do {
            System.out.println("Please enter the number of players:");
            playerCount = scanner.nextInt();
            scanner.nextLine();
            if (playerCount < 1) {
                System.out.println("There must be at least 1 player.");
            }
        } while (playerCount < 1);
        System.out.println("Please enter location of pack to load:");
        String packLocation = scanner.nextLine();
    }
}
