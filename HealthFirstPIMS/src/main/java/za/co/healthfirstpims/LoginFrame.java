package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        initialiseWindow();
        createInterface();
    }

    private void initialiseWindow() {
        setTitle("HealthFirst Pharmacy - Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(900, 680);
        setMinimumSize(new Dimension(800, 620));
        setLocationRelativeTo(null);
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Theme.BACKGROUND);

        JPanel loginCard = new JPanel(
                new BorderLayout(0, 20)
        );

        loginCard.setBackground(Theme.SURFACE);
        loginCard.setPreferredSize(new Dimension(430, 540));

        loginCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.GOLD,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                35,
                                45,
                                30,
                                45
                        )
                )
        );

        loginCard.add(createHeader(), BorderLayout.NORTH);
        loginCard.add(createForm(), BorderLayout.CENTER);
        loginCard.add(createFooter(), BorderLayout.SOUTH);

        mainPanel.add(loginCard);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JLabel logo = new JLabel(
                "H+",
                SwingConstants.CENTER
        );

        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 42)
        );

        logo.setForeground(Theme.GOLD);

        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 10, 0);
        header.add(logo, constraints);

        JLabel title = new JLabel(
                "HEALTHFIRST PHARMACY",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        title.setForeground(Theme.TEXT_PRIMARY);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 7, 0);
        header.add(title, constraints);

        JLabel subtitle = new JLabel(
                "Inventory Management System",
                SwingConstants.CENTER
        );

        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 0, 0);
        header.add(subtitle, constraints);

        return header;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints constraints =
                new GridBagConstraints();

        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JLabel usernameLabel =
                createFieldLabel("Username");

        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 7, 0);
        form.add(usernameLabel, constraints);

        usernameField = new JTextField();
        styleTextField(usernameField);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 8, 0);
        form.add(usernameField, constraints);

        JLabel passwordLabel =
                createFieldLabel("Password");

        constraints.gridy = 2;
        constraints.insets = new Insets(10, 0, 7, 0);
        form.add(passwordLabel, constraints);

        passwordField = new JPasswordField();
        styleTextField(passwordField);

        constraints.gridy = 3;
        constraints.insets = new Insets(0, 0, 8, 0);
        form.add(passwordField, constraints);

        loginButton = new JButton("SIGN IN SECURELY");
        Theme.stylePrimaryButton(loginButton);

        loginButton.setPreferredSize(
                new Dimension(300, 46)
        );

        loginButton.addActionListener(
                event -> authenticateUser()
        );

        constraints.gridy = 4;
        constraints.insets = new Insets(22, 0, 5, 0);
        form.add(loginButton, constraints);

        getRootPane().setDefaultButton(loginButton);

        return form;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        label.setForeground(Theme.TEXT_PRIMARY);

        return label;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(
                new Dimension(300, 42)
        );

        field.setFont(Theme.BODY_FONT);
        field.setBackground(Theme.SURFACE_LIGHT);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.GOLD);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 80, 90),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        footer.setOpaque(false);

        JLabel footerText = new JLabel(
                "Authorised personnel only | Secure access"
        );

        footerText.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );

        footerText.setForeground(Theme.TEXT_SECONDARY);

        footer.add(footerText);

        return footer;
    }

    private void authenticateUser() {
        String username =
                usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage(
                    "Please enter both your username "
                            + "and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "SELECT user_id, username, role, full_name "
                + "FROM users "
                + "WHERE username = ? "
                + "AND password = SHA2(?, 256)";

        loginButton.setEnabled(false);
        loginButton.setText("VERIFYING...");

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int userId =
                            result.getInt("user_id");

                    String fullName =
                            result.getString("full_name");

                    String role =
                            result.getString("role");

                    handleSuccessfulLogin(
                            userId,
                            fullName,
                            role
                    );
                } else {
                    showMessage(
                            "The username or password is incorrect.",
                            "Access Denied",
                            JOptionPane.ERROR_MESSAGE
                    );

                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            }

        } catch (SQLException exception) {
            showMessage(
                    "HealthFirst could not connect to the "
                            + "database.\n\n"
                            + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("SIGN IN SECURELY");
        }
    }

    private void handleSuccessfulLogin(
            int userId,
            String fullName,
            String role
    ) {
        showMessage(
                "Welcome, " + fullName + ".\n"
                        + "Access level: " + role,
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        System.out.println(
                "Authenticated user ID: " + userId
        );

        System.out.println(
                "Authenticated role: " + role
        );

        passwordField.setText("");

        /*
         * The AdminDashboard and CashierDashboard windows
         * will be opened here once we create them.
         */
    }

    private void showMessage(
            String message,
            String title,
            int messageType
    ) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                messageType
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.applyGlobalStyle();

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}