package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MedicineManagementPanel extends JPanel {

    private JTextField searchField;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JLabel recordCountLabel;

    public MedicineManagementPanel() {
        createInterface();
        loadMedicines("");
    }

    private void createInterface() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Theme.BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        30, 30, 30, 30
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(
                new BorderLayout(20, 15)
        );
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Medicine Management");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        title.setForeground(Theme.TEXT_PRIMARY);

        recordCountLabel = new JLabel("0 medicines");
        recordCountLabel.setFont(Theme.BODY_FONT);
        recordCountLabel.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(recordCountLabel);

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actionPanel.setOpaque(false);

        JButton addButton = new JButton("ADD MEDICINE");
        Theme.stylePrimaryButton(addButton);

        JButton updateButton =
                createSecondaryButton("UPDATE");

        JButton deleteButton =
                createDangerButton("DELETE");

        addButton.addActionListener(
                event -> addMedicine()
        );

        updateButton.addActionListener(
                event -> updateMedicine()
        );

        deleteButton.addActionListener(
                event -> deleteMedicine()
        );

        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(actionPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(
                new BorderLayout(0, 15)
        );

        panel.setBackground(Theme.SURFACE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        panel.add(createSearchPanel(), BorderLayout.NORTH);
        panel.add(createMedicineTable(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(
                new BorderLayout(12, 0)
        );
        panel.setOpaque(false);

        JLabel label = new JLabel("SEARCH");
        label.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        label.setForeground(Theme.GOLD);

        searchField = new JTextField();
        searchField.setPreferredSize(
                new Dimension(400, 42)
        );
        searchField.setFont(Theme.BODY_FONT);
        searchField.setBackground(Theme.SURFACE_LIGHT);
        searchField.setForeground(Theme.TEXT_PRIMARY);
        searchField.setCaretColor(Theme.GOLD);

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 80, 90)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent event) {
                        search();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent event) {
                        search();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent event) {
                        search();
                    }
                }
        );

        panel.add(label, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createMedicineTable() {
        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Medicine",
                    "Company",
                    "Type",
                    "Price",
                    "Stock",
                    "Reorder",
                    "Expiry",
                    "Supplier"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        medicineTable = new JTable(tableModel);
        medicineTable.setRowHeight(35);
        medicineTable.setFont(Theme.BODY_FONT);
        medicineTable.setBackground(Theme.SURFACE_LIGHT);
        medicineTable.setForeground(Theme.TEXT_PRIMARY);
        medicineTable.setGridColor(new Color(65, 70, 78));

        medicineTable.setSelectionBackground(
                new Color(22, 120, 92)
        );
        medicineTable.setSelectionForeground(Color.WHITE);

        medicineTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        medicineTable.setFillsViewportHeight(true);

        JTableHeader header = medicineTable.getTableHeader();
        header.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.GOLD);
        header.setPreferredSize(new Dimension(0, 38));

        JScrollPane scrollPane =
                new JScrollPane(medicineTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 70, 78)
                )
        );

        return scrollPane;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Theme.SURFACE_LIGHT);
        button.setForeground(Theme.TEXT_PRIMARY);
        button.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
                )
        );

        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(110, 40, 45));
        button.setForeground(Color.WHITE);
        button.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
                )
        );

        return button;
    }

    private void search() {
        loadMedicines(searchField.getText().trim());
    }

    private void loadMedicines(String searchText) {
        String sql =
                "SELECT m.medicine_id, m.name, m.company, "
                + "m.medicine_type, m.price, "
                + "m.quantity_in_stock, m.reorder_level, "
                + "m.expiry_date, m.supplier_id, "
                + "s.name AS supplier_name "
                + "FROM medicines m "
                + "LEFT JOIN suppliers s "
                + "ON m.supplier_id = s.supplier_id "
                + "WHERE m.name LIKE ? "
                + "OR m.company LIKE ? "
                + "OR m.medicine_type LIKE ? "
                + "ORDER BY m.name";

        tableModel.setRowCount(0);

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            String pattern = "%" + searchText + "%";

            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);

            try (ResultSet result =
                    statement.executeQuery()) {

                while (result.next()) {
                    tableModel.addRow(
                            new Object[]{
                                result.getInt("medicine_id"),
                                result.getString("name"),
                                result.getString("company"),
                                result.getString("medicine_type"),
                                result.getBigDecimal("price"),
                                result.getInt(
                                        "quantity_in_stock"
                                ),
                                result.getInt("reorder_level"),
                                result.getDate("expiry_date"),
                                result.getInt("supplier_id")
                                        + " - "
                                        + result.getString(
                                                "supplier_name"
                                        )
                            }
                    );
                }
            }

            recordCountLabel.setText(
                    tableModel.getRowCount()
                            + " medicines"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "Medicine records could not be loaded.",
                    exception
            );
        }
    }

    private void addMedicine() {
        MedicineForm form = new MedicineForm();

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Add Medicine",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            MedicineData data = form.readData();

            String sql =
                    "INSERT INTO medicines "
                    + "(name, company, medicine_type, price, "
                    + "quantity_in_stock, reorder_level, "
                    + "expiry_date, supplier_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                setMedicineParameters(statement, data);
                statement.executeUpdate();
            }

            showSuccess("Medicine added successfully.");
            loadMedicines(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The medicine could not be added.",
                    exception
            );
        }
    }

    private void updateMedicine() {
        int selectedRow = medicineTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a medicine to update."
            );
            return;
        }

        int medicineId =
                (int) tableModel.getValueAt(selectedRow, 0);

        MedicineForm form = new MedicineForm();
        form.loadSelectedRow(selectedRow);

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Update Medicine",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            MedicineData data = form.readData();

            String sql =
                    "UPDATE medicines SET "
                    + "name = ?, company = ?, "
                    + "medicine_type = ?, price = ?, "
                    + "quantity_in_stock = ?, "
                    + "reorder_level = ?, expiry_date = ?, "
                    + "supplier_id = ? "
                    + "WHERE medicine_id = ?";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                setMedicineParameters(statement, data);
                statement.setInt(9, medicineId);
                statement.executeUpdate();
            }

            showSuccess("Medicine updated successfully.");
            loadMedicines(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The medicine could not be updated.",
                    exception
            );
        }
    }

    private void deleteMedicine() {
        int selectedRow = medicineTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a medicine to delete."
            );
            return;
        }

        int medicineId =
                (int) tableModel.getValueAt(selectedRow, 0);

        String medicineName =
                tableModel.getValueAt(
                        selectedRow, 1
                ).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete " + medicineName + "?\n"
                        + "This cannot be undone.",
                "Confirm Medicine Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM medicines "
                + "WHERE medicine_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, medicineId);
            statement.executeUpdate();

            showSuccess("Medicine deleted successfully.");
            loadMedicines(searchField.getText().trim());

        } catch (SQLException exception) {
            showDatabaseError(
                    "This medicine may already belong to a "
                            + "recorded sale and cannot be deleted.",
                    exception
            );
        }
    }

    private void setMedicineParameters(
            PreparedStatement statement,
            MedicineData data
    ) throws SQLException {
        statement.setString(1, data.name);
        statement.setString(2, data.company);
        statement.setString(3, data.type);
        statement.setBigDecimal(4, data.price);
        statement.setInt(5, data.quantity);
        statement.setInt(6, data.reorderLevel);
        statement.setDate(7, data.expiryDate);
        statement.setInt(8, data.supplierId);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "HealthFirst",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Information",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showDatabaseError(
            String message,
            SQLException exception
    ) {
        JOptionPane.showMessageDialog(
                this,
                message + "\n\n" + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private class MedicineForm {

        private final JPanel panel;
        private final JTextField nameField;
        private final JTextField companyField;
        private final JTextField typeField;
        private final JTextField priceField;
        private final JTextField quantityField;
        private final JTextField reorderField;
        private final JTextField expiryField;
        private final JComboBox<SupplierItem> supplierBox;

        MedicineForm() {
            panel = new JPanel(new GridLayout(8, 2, 10, 10));

            nameField = new JTextField();
            companyField = new JTextField();
            typeField = new JTextField();
            priceField = new JTextField();
            quantityField = new JTextField();
            reorderField = new JTextField();
            expiryField = new JTextField();
            supplierBox = new JComboBox<>();

            panel.add(new JLabel("Medicine name:"));
            panel.add(nameField);

            panel.add(new JLabel("Company:"));
            panel.add(companyField);

            panel.add(new JLabel("Medicine type:"));
            panel.add(typeField);

            panel.add(new JLabel("Price:"));
            panel.add(priceField);

            panel.add(new JLabel("Quantity in stock:"));
            panel.add(quantityField);

            panel.add(new JLabel("Reorder level:"));
            panel.add(reorderField);

            panel.add(new JLabel("Expiry date (YYYY-MM-DD):"));
            panel.add(expiryField);

            panel.add(new JLabel("Supplier:"));
            panel.add(supplierBox);

            loadSuppliers();
        }

        private void loadSuppliers() {
            String sql =
                    "SELECT supplier_id, name "
                    + "FROM suppliers ORDER BY name";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet result =
                            statement.executeQuery()
            ) {
                while (result.next()) {
                    supplierBox.addItem(
                            new SupplierItem(
                                    result.getInt("supplier_id"),
                                    result.getString("name")
                            )
                    );
                }

            } catch (SQLException exception) {
                showDatabaseError(
                        "Suppliers could not be loaded.",
                        exception
                );
            }
        }

        private void loadSelectedRow(int row) {
            nameField.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            companyField.setText(
                    tableModel.getValueAt(row, 2).toString()
            );

            typeField.setText(
                    tableModel.getValueAt(row, 3).toString()
            );

            priceField.setText(
                    tableModel.getValueAt(row, 4).toString()
            );

            quantityField.setText(
                    tableModel.getValueAt(row, 5).toString()
            );

            reorderField.setText(
                    tableModel.getValueAt(row, 6).toString()
            );

            expiryField.setText(
                    tableModel.getValueAt(row, 7).toString()
            );

            String supplierValue =
                    tableModel.getValueAt(row, 8).toString();

            int supplierId = Integer.parseInt(
                    supplierValue.split(" - ")[0]
            );

            for (int index = 0;
                    index < supplierBox.getItemCount();
                    index++) {

                if (supplierBox.getItemAt(index).id
                        == supplierId) {

                    supplierBox.setSelectedIndex(index);
                    break;
                }
            }
        }

        private MedicineData readData() {
            String name = nameField.getText().trim();
            String company = companyField.getText().trim();
            String type = typeField.getText().trim();

            if (name.isEmpty()
                    || company.isEmpty()
                    || type.isEmpty()
                    || priceField.getText().trim().isEmpty()
                    || quantityField.getText().trim().isEmpty()
                    || reorderField.getText().trim().isEmpty()
                    || expiryField.getText().trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Please complete every medicine field."
                );
            }

            try {
                BigDecimal price = new BigDecimal(
                        priceField.getText().trim()
                );

                int quantity = Integer.parseInt(
                        quantityField.getText().trim()
                );

                int reorderLevel = Integer.parseInt(
                        reorderField.getText().trim()
                );

                Date expiryDate = Date.valueOf(
                        expiryField.getText().trim()
                );

                SupplierItem supplier =
                        (SupplierItem) supplierBox.getSelectedItem();

                if (price.compareTo(BigDecimal.ZERO) < 0
                        || quantity < 0
                        || reorderLevel < 0) {

                    throw new IllegalArgumentException(
                            "Price, stock and reorder level "
                                    + "cannot be negative."
                    );
                }

                if (supplier == null) {
                    throw new IllegalArgumentException(
                            "Please select a supplier."
                    );
                }

                return new MedicineData(
                        name,
                        company,
                        type,
                        price,
                        quantity,
                        reorderLevel,
                        expiryDate,
                        supplier.id
                );

            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(
                        "Enter valid numbers for price, "
                                + "stock and reorder level."
                );

            } catch (IllegalArgumentException exception) {
                if (exception.getMessage() != null
                        && exception.getMessage().startsWith(
                                "Price"
                        )) {
                    throw exception;
                }

                throw new IllegalArgumentException(
                        "Use YYYY-MM-DD for the expiry date "
                                + "and valid numeric values."
                );
            }
        }
    }

    private static class MedicineData {

        private final String name;
        private final String company;
        private final String type;
        private final BigDecimal price;
        private final int quantity;
        private final int reorderLevel;
        private final Date expiryDate;
        private final int supplierId;

        MedicineData(
                String name,
                String company,
                String type,
                BigDecimal price,
                int quantity,
                int reorderLevel,
                Date expiryDate,
                int supplierId
        ) {
            this.name = name;
            this.company = company;
            this.type = type;
            this.price = price;
            this.quantity = quantity;
            this.reorderLevel = reorderLevel;
            this.expiryDate = expiryDate;
            this.supplierId = supplierId;
        }
    }

    private static class SupplierItem {

        private final int id;
        private final String name;

        SupplierItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}