package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class SupplierManagementPanel extends JPanel {

    private JTextField searchField;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JLabel recordCountLabel;

    public SupplierManagementPanel() {
        createInterface();
        loadSuppliers("");
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

        JLabel title = new JLabel("Supplier Management");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        title.setForeground(Theme.TEXT_PRIMARY);

        recordCountLabel = new JLabel("0 suppliers");
        recordCountLabel.setFont(Theme.BODY_FONT);
        recordCountLabel.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(recordCountLabel);

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actions.setOpaque(false);

        JButton addButton = new JButton("ADD SUPPLIER");
        Theme.stylePrimaryButton(addButton);

        JButton updateButton =
                createSecondaryButton("UPDATE");

        JButton deleteButton =
                createDangerButton("DELETE");

        addButton.addActionListener(
                event -> addSupplier()
        );

        updateButton.addActionListener(
                event -> updateSupplier()
        );

        deleteButton.addActionListener(
                event -> deleteSupplier()
        );

        actions.add(addButton);
        actions.add(updateButton);
        actions.add(deleteButton);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

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
        panel.add(createSupplierTable(), BorderLayout.CENTER);

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

    private JScrollPane createSupplierTable() {
        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Supplier",
                    "Contact Person",
                    "Phone",
                    "Email",
                    "Address"
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

        supplierTable = new JTable(tableModel);
        supplierTable.setRowHeight(35);
        supplierTable.setFont(Theme.BODY_FONT);
        supplierTable.setBackground(Theme.SURFACE_LIGHT);
        supplierTable.setForeground(Theme.TEXT_PRIMARY);
        supplierTable.setGridColor(new Color(65, 70, 78));

        supplierTable.setSelectionBackground(
                new Color(22, 120, 92)
        );
        supplierTable.setSelectionForeground(Color.WHITE);

        supplierTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        supplierTable.setFillsViewportHeight(true);

        JTableHeader header = supplierTable.getTableHeader();
        header.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.GOLD);
        header.setPreferredSize(new Dimension(0, 38));

        JScrollPane scrollPane =
                new JScrollPane(supplierTable);

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
        loadSuppliers(searchField.getText().trim());
    }

    private void loadSuppliers(String searchText) {
        String sql =
                "SELECT supplier_id, name, contact_person, "
                + "phone, email, address "
                + "FROM suppliers "
                + "WHERE name LIKE ? "
                + "OR contact_person LIKE ? "
                + "OR phone LIKE ? "
                + "OR email LIKE ? "
                + "ORDER BY name";

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
            statement.setString(4, pattern);

            try (ResultSet result =
                    statement.executeQuery()) {

                while (result.next()) {
                    tableModel.addRow(
                            new Object[]{
                                result.getInt("supplier_id"),
                                result.getString("name"),
                                result.getString("contact_person"),
                                result.getString("phone"),
                                result.getString("email"),
                                result.getString("address")
                            }
                    );
                }
            }

            recordCountLabel.setText(
                    tableModel.getRowCount()
                            + " suppliers"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "Supplier records could not be loaded.",
                    exception
            );
        }
    }

    private void addSupplier() {
        SupplierForm form = new SupplierForm();

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Add Supplier",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            SupplierData data = form.readData();

            String sql =
                    "INSERT INTO suppliers "
                    + "(name, contact_person, phone, "
                    + "email, address) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                setSupplierParameters(statement, data);
                statement.executeUpdate();
            }

            showSuccess("Supplier added successfully.");
            loadSuppliers(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The supplier could not be added.",
                    exception
            );
        }
    }

    private void updateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a supplier to update."
            );
            return;
        }

        int supplierId =
                (int) tableModel.getValueAt(selectedRow, 0);

        SupplierForm form = new SupplierForm();
        form.loadSelectedRow(selectedRow);

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Update Supplier",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            SupplierData data = form.readData();

            String sql =
                    "UPDATE suppliers SET "
                    + "name = ?, contact_person = ?, "
                    + "phone = ?, email = ?, address = ? "
                    + "WHERE supplier_id = ?";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                setSupplierParameters(statement, data);
                statement.setInt(6, supplierId);
                statement.executeUpdate();
            }

            showSuccess("Supplier updated successfully.");
            loadSuppliers(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The supplier could not be updated.",
                    exception
            );
        }
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a supplier to delete."
            );
            return;
        }

        int supplierId =
                (int) tableModel.getValueAt(selectedRow, 0);

        String supplierName =
                tableModel.getValueAt(
                        selectedRow, 1
                ).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete " + supplierName + "?\n"
                        + "This cannot be undone.",
                "Confirm Supplier Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM suppliers "
                + "WHERE supplier_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, supplierId);
            statement.executeUpdate();

            showSuccess("Supplier deleted successfully.");
            loadSuppliers(searchField.getText().trim());

        } catch (SQLException exception) {
            showDatabaseError(
                    "This supplier is linked to medicine "
                            + "records and cannot be deleted.",
                    exception
            );
        }
    }

    private void setSupplierParameters(
            PreparedStatement statement,
            SupplierData data
    ) throws SQLException {
        statement.setString(1, data.name);
        statement.setString(2, data.contactPerson);
        statement.setString(3, data.phone);
        statement.setString(4, data.email);
        statement.setString(5, data.address);
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

    private class SupplierForm {

        private final JPanel panel;
        private final JTextField nameField;
        private final JTextField contactField;
        private final JTextField phoneField;
        private final JTextField emailField;
        private final JTextField addressField;

        SupplierForm() {
            panel = new JPanel(
                    new GridLayout(5, 2, 10, 10)
            );

            nameField = new JTextField();
            contactField = new JTextField();
            phoneField = new JTextField();
            emailField = new JTextField();
            addressField = new JTextField();

            panel.add(new JLabel("Supplier name:"));
            panel.add(nameField);

            panel.add(new JLabel("Contact person:"));
            panel.add(contactField);

            panel.add(new JLabel("Phone number:"));
            panel.add(phoneField);

            panel.add(new JLabel("Email address:"));
            panel.add(emailField);

            panel.add(new JLabel("Physical address:"));
            panel.add(addressField);
        }

        private void loadSelectedRow(int row) {
            nameField.setText(
                    valueAt(row, 1)
            );

            contactField.setText(
                    valueAt(row, 2)
            );

            phoneField.setText(
                    valueAt(row, 3)
            );

            emailField.setText(
                    valueAt(row, 4)
            );

            addressField.setText(
                    valueAt(row, 5)
            );
        }

        private String valueAt(int row, int column) {
            Object value = tableModel.getValueAt(row, column);

            return value == null
                    ? ""
                    : value.toString();
        }

        private SupplierData readData() {
            String name = nameField.getText().trim();
            String contact =
                    contactField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address =
                    addressField.getText().trim();

            if (name.isEmpty()
                    || contact.isEmpty()
                    || phone.isEmpty()
                    || email.isEmpty()
                    || address.isEmpty()) {

                throw new IllegalArgumentException(
                        "Please complete every supplier field."
                );
            }

            if (!email.contains("@")
                    || !email.contains(".")) {

                throw new IllegalArgumentException(
                        "Please enter a valid email address."
                );
            }

            return new SupplierData(
                    name,
                    contact,
                    phone,
                    email,
                    address
            );
        }
    }

    private static class SupplierData {

        private final String name;
        private final String contactPerson;
        private final String phone;
        private final String email;
        private final String address;

        SupplierData(
                String name,
                String contactPerson,
                String phone,
                String email,
                String address
        ) {
            this.name = name;
            this.contactPerson = contactPerson;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }
    }
}