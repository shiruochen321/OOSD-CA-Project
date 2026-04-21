import db.DatabaseManager;

public class InitDB {
    public static void main(String[] args) {
        DatabaseManager.initialize();
        System.out.println("Database initialized.");
    }
}
