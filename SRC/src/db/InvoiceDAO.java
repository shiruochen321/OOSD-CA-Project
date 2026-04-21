package db;

import models.Invoice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public void addInvoice(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices(customer_id, product_id, quantity, total_price, date) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getProductId());
            pstmt.setInt(3, invoice.getQuantity());
            pstmt.setDouble(4, invoice.getTotalPrice());
            pstmt.setString(5, invoice.getDate());
            pstmt.executeUpdate();
        }
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        // Inner Join requirement
        String sql = "SELECT i.*, c.name as customer_name, p.name as product_name " +
                     "FROM invoices i " +
                     "INNER JOIN customers c ON i.customer_id = c.id " +
                     "INNER JOIN products p ON i.product_id = p.id";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Invoice inv = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getString("date")
                );
                inv.setCustomerName(rs.getString("customer_name"));
                inv.setProductName(rs.getString("product_name"));
                invoices.add(inv);
            }
        }
        return invoices;
    }
}
