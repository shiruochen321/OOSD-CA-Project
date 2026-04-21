package ui;

import db.ProductDAO;
import models.Product;
import utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProductPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, descField;
    private ProductDAO productDAO = new ProductDAO();

    public ProductPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Description"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addProduct());
        formPanel.add(addButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteProduct());
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Product p : productDAO.getAllProducts()) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getDescription()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addProduct() {
        String name = nameField.getText();
        String priceStr = priceField.getText();
        String desc = descField.getText();

        if (name.isEmpty() || !Validator.isPositiveNumeric(priceStr)) {
            JOptionPane.showMessageDialog(this, "Invalid name or price.");
            return;
        }

        try {
            productDAO.addProduct(new Product(0, name, Double.parseDouble(priceStr), desc));
            refreshTable();
            nameField.setText("");
            priceField.setText("");
            descField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        try {
            productDAO.deleteProduct((int) tableModel.getValueAt(row, 0));
            refreshTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
