package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // MySQL configuration based on your provided info
    private static final String DB_URL = "jdbc:mysql://c00304527.candept.com:3306/c00304527_CRUD?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "shiruochenroot";
    private static final String DB_PASS = "shiruochenroot";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create Customers table (MySQL Syntax)
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(20)" +
                    ")");

            // Create Products table
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "description TEXT" +
                    ")");

            // Create Invoices table
            stmt.execute("CREATE TABLE IF NOT EXISTS invoices (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "customer_id INT NOT NULL," +
                    "product_id INT NOT NULL," +
                    "quantity INT NOT NULL," +
                    "total_price DOUBLE NOT NULL," +
                    "date DATE NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(id)," +
                    "FOREIGN KEY (product_id) REFERENCES products(id)" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Note: Make sure your MySQL server is running and the database 'c00304527_CRUD' exists.");
        }
    }
}
