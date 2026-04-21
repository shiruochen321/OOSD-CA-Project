package ui;

import db.CustomerDAO;
import db.InvoiceDAO;
import db.ProductDAO;
import models.Customer;
import models.Invoice;
import models.Product;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Customer> customerCombo;
    private JComboBox<Product> productCombo;
    private JTextField quantityField;
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private ProductDAO productDAO = new ProductDAO();

    public InvoicePanel() {
        setLayout(new BorderLayout());

        // Table at the top
        tableModel = new DefaultTableModel(new String[]{"ID", "Customer", "Product", "Quantity", "Total Price", "Date"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form at the bottom
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Select Customer:"));
        customerCombo = new JComboBox<>();
        formPanel.add(customerCombo);

        formPanel.add(new JLabel("Select Product:"));
        productCombo = new JComboBox<>();
        formPanel.add(productCombo);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        JButton addButton = new JButton("Generate Invoice");
        addButton.addActionListener(e -> addInvoice());
        formPanel.add(addButton);

        JButton refreshDataButton = new JButton("Refresh Selections");
        refreshDataButton.addActionListener(e -> loadCombos());
        formPanel.add(refreshDataButton);

        add(formPanel, BorderLayout.SOUTH);

        loadCombos();
        refreshTable();
    }

    private void loadCombos() {
        try {
            customerCombo.removeAllItems();
            for (Customer c : customerDAO.getAllCustomers()) customerCombo.addItem(c);

            productCombo.removeAllItems();
            for (Product p : productDAO.getAllProducts()) productCombo.addItem(p);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Invoice i : invoiceDAO.getAllInvoices()) {
                tableModel.addRow(new Object[]{
                        i.getId(),
                        i.getCustomerName(),
                        i.getProductName(),
                        i.getQuantity(),
                        "$" + i.getTotalPrice(),
                        i.getDate()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addInvoice() {
        Customer customer = (Customer) customerCombo.getSelectedItem();
        Product product = (Product) productCombo.getSelectedItem();
        String qtyStr = quantityField.getText();

        if (customer == null || product == null || !Validator.isPositiveNumeric(qtyStr)) {
            JOptionPane.showMessageDialog(this, "Invalid selections or quantity.");
            return;
        }

        int qty = Integer.parseInt(qtyStr);
        double total = qty * product.getPrice();
        String date = LocalDate.now().toString();

        try {
            invoiceDAO.addInvoice(new Invoice(0, customer.getId(), product.getId(), qty, total, date));
            refreshTable();
            quantityField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
