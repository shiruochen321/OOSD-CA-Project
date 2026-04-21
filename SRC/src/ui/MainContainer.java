package ui;

import javax.swing.*;
import db.DatabaseManager;

public class MainContainer extends JFrame {
    public MainContainer() {
        setTitle("OOSD-CA Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Database
        DatabaseManager.initialize();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Customers", new CustomerPanel());
        tabbedPane.addTab("Products", new ProductPanel());
        tabbedPane.addTab("Invoices", new InvoicePanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainContainer().setVisible(true);
        });
    }
}
