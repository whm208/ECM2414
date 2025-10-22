import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGameTest{
    @Test
    public static void loadPackTest(String filePath) {
    File packFile = new File(filePath);
    if (!packFile.exists()) {
        System.out.println("Pack file not found.");
        return null;
        }
    }
}