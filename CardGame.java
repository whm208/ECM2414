import java.util.*;
import java.util.concurrent.*;
import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the number of players:");
        int playerCount = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Please enter location of pack to load:");
        String packLocation = scanner.nextLine();
    }
}
