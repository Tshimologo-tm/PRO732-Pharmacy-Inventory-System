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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class UserManagementPanel extends JPanel {

    private JTextField searchField;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel recordCountLabel;

    public UserManagementPanel() {
        createInterface();
        loadUsers("");
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

        JLabel title = new JLabel("User Management");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        title.setForeground(Theme.TEXT_PRIMARY);

        recordCountLabel = new JLabel("0 users");
        recordCountLabel.setFont(Theme.BODY_FONT);
        recordCountLabel.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(recordCountLabel);

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actions.setOpaque(false);

        JButton addButton = new JButton("ADD USER");
        Theme.stylePrimaryButton(addButton);

        JButton updateButton =
                createSecondaryButton("UPDATE");

        JButton deleteButton =
                createDangerButton("DELETE");

        addButton.addActionListener(
                event -> addUser()
        );

        updateButton.addActionListener(
                event -> updateUser()
        );

        deleteButton.addActionListener(
                event -> deleteUser()
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
        panel.add(createUserTable(), BorderLayout.CENTER);

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

    private JScrollPane createUserTable() {
        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Username",
                    "Full Name",
                    "Role"
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

        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setFont(Theme.BODY_FONT);
        userTable.setBackground(Theme.SURFACE_LIGHT);
        userTable.setForeground(Theme.TEXT_PRIMARY);
        userTable.setGridColor(new Color(65, 70, 78));

        userTable.setSelectionBackground(
                new Color(22, 120, 92)
        );
        userTable.setSelectionForeground(Color.WHITE);

        userTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        userTable.setFillsViewportHeight(true);

        JTableHeader header = userTable.getTableHeader();
        header.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.GOLD);
        header.setPreferredSize(new Dimension(0, 38));

        JScrollPane scrollPane = new JScrollPane(userTable);

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
        loadUsers(searchField.getText().trim());
    }

    private void loadUsers(String searchText) {
        String sql =
                "SELECT user_id, username, full_name, role "
                + "FROM users "
                + "WHERE username LIKE ? "
                + "OR full_name LIKE ? "
                + "OR role LIKE ? "
                + "ORDER BY full_name";

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
                                result.getInt("user_id"),
                                result.getString("username"),
                                result.getString("full_name"),
                                result.getString("role")
                            }
                    );
                }
            }

            recordCountLabel.setText(
                    tableModel.getRowCount() + " users"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "User records could not be loaded.",
                    exception
            );
        }
    }

    private void addUser() {
        UserForm form = new UserForm(false);

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Add User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            UserData data = form.readData();

            if (data.password.isEmpty()) {
                throw new IllegalArgumentException(
                        "A password is required for a new user."
                );
            }

            String sql =
                    "INSERT INTO users "
                    + "(username, password, role, full_name) "
                    + "VALUES (?, SHA2(?, 256), ?, ?)";

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                statement.setString(1, data.username);
                statement.setString(2, data.password);
                statement.setString(3, data.role);
                statement.setString(4, data.fullName);
                statement.executeUpdate();
            }

            showSuccess("User created successfully.");
            loadUsers(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The user could not be created. "
                            + "The username may already exist.",
                    exception
            );
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a user to update."
            );
            return;
        }

        int userId =
                (int) tableModel.getValueAt(selectedRow, 0);

        UserForm form = new UserForm(true);
        form.loadSelectedRow(selectedRow);

        int result = JOptionPane.showConfirmDialog(
                this,
                form.panel,
                "Update User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            UserData data = form.readData();

            String sql;

            if (data.password.isEmpty()) {
                sql = "UPDATE users SET username = ?, "
                        + "role = ?, full_name = ? "
                        + "WHERE user_id = ?";
            } else {
                sql = "UPDATE users SET username = ?, "
                        + "password = SHA2(?, 256), "
                        + "role = ?, full_name = ? "
                        + "WHERE user_id = ?";
            }

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {
                if (data.password.isEmpty()) {
                    statement.setString(1, data.username);
                    statement.setString(2, data.role);
                    statement.setString(3, data.fullName);
                    statement.setInt(4, userId);

                } else {
                    statement.setString(1, data.username);
                    statement.setString(2, data.password);
                    statement.setString(3, data.role);
                    statement.setString(4, data.fullName);
                    statement.setInt(5, userId);
                }

                statement.executeUpdate();
            }

            showSuccess("User updated successfully.");
            loadUsers(searchField.getText().trim());

        } catch (IllegalArgumentException exception) {
            showValidationError(exception.getMessage());

        } catch (SQLException exception) {
            showDatabaseError(
                    "The user could not be updated.",
                    exception
            );
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            showValidationError(
                    "Please select a user to delete."
            );
            return;
        }

        int userId =
                (int) tableModel.getValueAt(selectedRow, 0);

        String username =
                tableModel.getValueAt(
                        selectedRow, 1
                ).toString();

        String role =
                tableModel.getValueAt(
                        selectedRow, 3
                ).toString();

        if ("Admin".equalsIgnoreCase(role)
                && countAdministrators() <= 1) {

            showValidationError(
                    "The final administrator account "
                            + "cannot be deleted."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the account \"" + username + "\"?\n"
                        + "This cannot be undone.",
                "Confirm User Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM users WHERE user_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.executeUpdate();

            showSuccess("User deleted successfully.");
            loadUsers(searchField.getText().trim());

        } catch (SQLException exception) {
            showDatabaseError(
                    "This user may be linked to recorded sales "
                            + "and cannot be deleted.",
                    exception
            );
        }
    }

    private int countAdministrators() {
        String sql =
                "SELECT COUNT(*) FROM users "
                + "WHERE role = 'Admin'";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {
            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException exception) {
            showDatabaseError(
                    "Administrator accounts could not be checked.",
                    exception
            );
        }

        return 0;
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

    private class UserForm {

        private final JPanel panel;
        private final JTextField usernameField;
        private final JTextField fullNameField;
        private final JComboBox<String> roleBox;
        private final JPasswordField passwordField;

        UserForm(boolean editing) {
            panel = new JPanel(
                    new GridLayout(4, 2, 10, 10)
            );

            usernameField = new JTextField();
            fullNameField = new JTextField();

            roleBox = new JComboBox<>(
                    new String[]{"Admin", "Cashier"}
            );

            passwordField = new JPasswordField();

            panel.add(new JLabel("Username:"));
            panel.add(usernameField);

            panel.add(new JLabel("Full name:"));
            panel.add(fullNameField);

            panel.add(new JLabel("Role:"));
            panel.add(roleBox);

            panel.add(
                    new JLabel(
                            editing
                                    ? "New password (optional):"
                                    : "Password:"
                    )
            );

            panel.add(passwordField);
        }

        private void loadSelectedRow(int row) {
            usernameField.setText(
                    tableModel.getValueAt(
                            row, 1
                    ).toString()
            );

            fullNameField.setText(
                    tableModel.getValueAt(
                            row, 2
                    ).toString()
            );

            roleBox.setSelectedItem(
                    tableModel.getValueAt(
                            row, 3
                    ).toString()
            );
        }

        private UserData readData() {
            String username =
                    usernameField.getText().trim();

            String fullName =
                    fullNameField.getText().trim();

            String role =
                    roleBox.getSelectedItem().toString();

            String password = new String(
                    passwordField.getPassword()
            );

            if (username.isEmpty()
                    || fullName.isEmpty()) {

                throw new IllegalArgumentException(
                        "Username and full name are required."
                );
            }

            if (!password.isEmpty()
                    && password.length() < 6) {

                throw new IllegalArgumentException(
                        "Passwords must contain at least "
                                + "six characters."
                );
            }

            return new UserData(
                    username,
                    fullName,
                    role,
                    password
            );
        }
    }

    private static class UserData {

        private final String username;
        private final String fullName;
        private final String role;
        private final String password;

        UserData(
                String username,
                String fullName,
                String role,
                String password
        ) {
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.password = password;
        }
    }
}