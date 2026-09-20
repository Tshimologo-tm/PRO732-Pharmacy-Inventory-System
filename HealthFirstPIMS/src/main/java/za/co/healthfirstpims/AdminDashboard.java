package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AdminDashboard extends JFrame {

    private final String administratorName;

    private JPanel contentPanel;
    private CardLayout contentLayout;

    private JLabel medicinesValue;
    private JLabel suppliersValue;
    private JLabel usersValue;
    private JLabel salesValue;

    public AdminDashboard(String administratorName) {
        this.administratorName = administratorName;

        initialiseWindow();
        createInterface();
        loadDashboardStatistics();
    }

    private void initialiseWindow() {
        setTitle("HealthFirst Pharmacy - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BACKGROUND);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createContentArea(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.SURFACE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, Theme.GOLD
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 25, 18, 25
                        )
                )
        );

        JLabel brand = new JLabel("H+  HEALTHFIRST PHARMACY");
        brand.setFont(
                new Font("Segoe UI", Font.BOLD, 21)
        );
        brand.setForeground(Theme.TEXT_PRIMARY);

        JLabel user = new JLabel(
                administratorName + "  •  Administrator"
        );
        user.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        user.setForeground(Theme.TEXT_SECONDARY);

        header.add(brand, BorderLayout.WEST);
        header.add(user, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(Theme.SURFACE);

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 18, 25, 18
                )
        );

        JLabel menuTitle = new JLabel("ADMINISTRATION");
        menuTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        menuTitle.setForeground(Theme.GOLD);
        menuTitle.setAlignmentX(LEFT_ALIGNMENT);

        sidebar.add(menuTitle);
        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(createNavigationButton(
                "Dashboard", "DASHBOARD"
        ));

        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(createNavigationButton(
                "Medicines", "MEDICINES"
        ));

        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(createNavigationButton(
                "Suppliers", "SUPPLIERS"
        ));

        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(createNavigationButton(
                "Users", "USERS"
        ));

        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(createNavigationButton(
                "Reports", "REPORTS"
        ));

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("LOG OUT");
        logoutButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );
        logoutButton.setAlignmentX(LEFT_ALIGNMENT);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        logoutButton.setBackground(new Color(110, 40, 45));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 15
                )
        );

        logoutButton.addActionListener(event -> logout());

        sidebar.add(logoutButton);

        return sidebar;
    }

    private JButton createNavigationButton(
            String text,
            String screenName
    ) {
        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        button.setBackground(Theme.SURFACE_LIGHT);
        button.setForeground(Theme.TEXT_PRIMARY);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 3, 0, 0, Theme.GOLD
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 15, 10, 15
                        )
                )
        );

        button.addActionListener(event -> {
            contentLayout.show(contentPanel, screenName);

            if ("DASHBOARD".equals(screenName)) {
                loadDashboardStatistics();
            }
        });

        return button;
    }

    private JPanel createContentArea() {
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(Theme.BACKGROUND);

        contentPanel.add(
                createDashboardPanel(),
                "DASHBOARD"
        );

        contentPanel.add(
        new MedicineManagementPanel(),
        "MEDICINES"
);

   contentPanel.add(
        new SupplierManagementPanel(),
        "SUPPLIERS"
);

        contentPanel.add(
                createPlaceholderPanel(
                        "User Management",
                        "Manage administrator and cashier accounts."
                ),
                "USERS"
        );

        contentPanel.add(
                createPlaceholderPanel(
                        "Reports",
                        "View sales, stock and expiry reports."
                ),
                "REPORTS"
        );

        return contentPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 25));
        dashboard.setBackground(Theme.BACKGROUND);

        dashboard.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 35, 35, 35
                )
        );

        JPanel headingPanel = new JPanel(
                new BorderLayout()
        );
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("Dashboard Overview");
        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );
        heading.setForeground(Theme.TEXT_PRIMARY);

        JLabel description = new JLabel(
                "Live information from the HealthFirst database"
        );
        description.setFont(Theme.BODY_FONT);
        description.setForeground(Theme.TEXT_SECONDARY);

        headingPanel.add(heading, BorderLayout.NORTH);
        headingPanel.add(description, BorderLayout.SOUTH);

        JPanel statisticsPanel = new JPanel(
                new GridLayout(2, 2, 20, 20)
        );
        statisticsPanel.setOpaque(false);

        medicinesValue = new JLabel("0");
        suppliersValue = new JLabel("0");
        usersValue = new JLabel("0");
        salesValue = new JLabel("R 0.00");

        statisticsPanel.add(
                createStatisticCard(
                        "MEDICINES",
                        medicinesValue,
                        "Products currently registered"
                )
        );

        statisticsPanel.add(
                createStatisticCard(
                        "SUPPLIERS",
                        suppliersValue,
                        "Active supplier records"
                )
        );

        statisticsPanel.add(
                createStatisticCard(
                        "SYSTEM USERS",
                        usersValue,
                        "Authorised staff accounts"
                )
        );

        statisticsPanel.add(
                createStatisticCard(
                        "TODAY'S SALES",
                        salesValue,
                        "Revenue recorded today"
                )
        );

        dashboard.add(headingPanel, BorderLayout.NORTH);
        dashboard.add(statisticsPanel, BorderLayout.CENTER);

        return dashboard;
    }

    private JPanel createStatisticCard(
            String title,
            JLabel valueLabel,
            String description
    ) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Theme.SURFACE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 4, 0, 0, Theme.GOLD
                        ),
                        BorderFactory.createEmptyBorder(
                                25, 25, 25, 25
                        )
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        titleLabel.setForeground(Theme.GOLD);

        valueLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );
        valueLabel.setForeground(Theme.TEXT_PRIMARY);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(Theme.BODY_FONT);
        descriptionLabel.setForeground(Theme.TEXT_SECONDARY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(descriptionLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createPlaceholderPanel(
            String title,
            String description
    ) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 35, 35, 35
                )
        );

        JPanel messageCard = new JPanel(
                new GridLayout(2, 1, 0, 10)
        );
        messageCard.setBackground(Theme.SURFACE);

        messageCard.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 35, 35, 35
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        titleLabel.setForeground(Theme.TEXT_PRIMARY);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(Theme.BODY_FONT);
        descriptionLabel.setForeground(Theme.TEXT_SECONDARY);

        messageCard.add(titleLabel);
        messageCard.add(descriptionLabel);

        panel.add(messageCard, BorderLayout.NORTH);

        return panel;
    }

    private void loadDashboardStatistics() {
        try (Connection connection =
                DatabaseConnection.getConnection()) {

            medicinesValue.setText(
                    readValue(
                            connection,
                            "SELECT COUNT(*) FROM medicines"
                    )
            );

            suppliersValue.setText(
                    readValue(
                            connection,
                            "SELECT COUNT(*) FROM suppliers"
                    )
            );

            usersValue.setText(
                    readValue(
                            connection,
                            "SELECT COUNT(*) FROM users"
                    )
            );

            String salesTotal = readValue(
                    connection,
                    "SELECT COALESCE(SUM(total_amount), 0) "
                            + "FROM sales "
                            + "WHERE DATE(sale_date) = CURDATE()"
            );

            salesValue.setText("R " + salesTotal);

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dashboard information could not be loaded.\n\n"
                            + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String readValue(
            Connection connection,
            String sql
    ) throws SQLException {
        try (
                Statement statement =
                        connection.createStatement();

                ResultSet result =
                        statement.executeQuery(sql)
        ) {
            if (result.next()) {
                return result.getString(1);
            }

            return "0";
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.applyGlobalStyle();

            AdminDashboard dashboard =
                    new AdminDashboard("HealthFirst Administrator");

            dashboard.setVisible(true);
        });
    }
}