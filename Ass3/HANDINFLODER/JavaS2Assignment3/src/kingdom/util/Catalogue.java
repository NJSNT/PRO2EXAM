package kingdom.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Catalogue {
    private static Catalogue instance;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Catalogue() {
    }

    public static synchronized Catalogue getInstance() {
        if (instance == null) {
            instance = new Catalogue();
        }
        return instance;
    }

    public synchronized void log(String message) {
        System.out.println("[" + LocalTime.now().format(formatter) + "] " + message);
    }

    public synchronized void logIncome(int amount) {
        log("Income: " + amount + " gems or value added.");
    }

    public synchronized void logOutcome(int amount) {
        log("Outcome: " + amount + " gems or value removed.");
    }
}
