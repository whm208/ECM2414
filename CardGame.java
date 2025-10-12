import java.util.*;
import java.util.concurrent.*;

public class CardGame {
    public static void main(String[] args) {
        int playerCount = Integer.parseInt(args[0]);
        String packLocation = args[1]; 
        System.out.println("Please enter the number of players:" + playerCount);
        System.out.println("Please enter location of pack to load:" + packLocation);
    }
}
