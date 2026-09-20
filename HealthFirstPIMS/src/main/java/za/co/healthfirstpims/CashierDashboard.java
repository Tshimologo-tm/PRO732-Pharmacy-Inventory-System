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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class CashierDashboard extends JFrame {

    private final int cashierId;
    private final String cashierName;

    private JTextField searchField;
    private JTable medicinesTable;
    private DefaultTableModel medicinesModel;
    private JLabel productCountLabel;

    public CashierDashboard(
            int cashierId,
            String cashierName
    ) {
        this.cashierId = cashierId;
        this.cashierName = cashierName;

        initialiseWindow();
        createInterface();
        loadMedicines("");
    }

    private void initialiseWindow() {
        setTitle("HealthFirst Pharmacy - Cashier POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1200, 760);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BACKGROUND);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

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
                                16, 24, 16, 24
                        )
                )
        );

        JPanel brandPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0)
        );
        brandPanel.setOpaque(false);

        JLabel logo = new JLabel("H+");
        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        logo.setForeground(Theme.GOLD);

        JLabel brand = new JLabel(
                "HEALTHFIRST PHARMACY  |  POINT OF SALE"
        );
        brand.setFont(
                new Font("Segoe UI", Font.BOLD, 19)
        );
        brand.setForeground(Theme.TEXT_PRIMARY);

        brandPanel.add(logo);
        brandPanel.add(brand);

        JPanel accountPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 16, 0)
        );
        accountPanel.setOpaque(false);

        JLabel cashierLabel = new JLabel(
                cashierName + "  •  Cashier"
        );
        cashierLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        cashierLabel.setForeground(Theme.TEXT_SECONDARY);

        JButton logoutButton = new JButton("LOG OUT");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(110, 40, 45));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        logoutButton.setBorder(
                BorderFactory.createEmptyBorder(
                        9, 16, 9, 16
                )
        );

        logoutButton.addActionListener(
                event -> logout()
        );

        accountPanel.add(cashierLabel);
        accountPanel.add(logoutButton);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(accountPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(
                new BorderLayout(0, 20)
        );

        content.setBackground(Theme.BACKGROUND);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 30, 30, 30
                )
        );

        content.add(createHeadingPanel(), BorderLayout.NORTH);
        content.add(createWorkspacePanel(), BorderLayout.CENTER);

        return content;
    }

    private JPanel createHeadingPanel() {
        JPanel headingPanel = new JPanel(
                new BorderLayout()
        );
        headingPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("New Customer Sale");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "Search available medicine and begin building the customer's order"
        );
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        productCountLabel = new JLabel(
                "0 products available"
        );
        productCountLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );
        productCountLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        productCountLabel.setForeground(Theme.GOLD);

        headingPanel.add(titlePanel, BorderLayout.WEST);
        headingPanel.add(productCountLabel, BorderLayout.EAST);

        return headingPanel;
    }

    private JPanel createWorkspacePanel() {
        JPanel workspace = new JPanel(
                new BorderLayout(0, 15)
        );

        workspace.setBackground(Theme.SURFACE);

        workspace.setBorder(
                BorderFactory.createEmptyBorder(
                        22, 22, 22, 22
                )
        );

        workspace.add(createSearchPanel(), BorderLayout.NORTH);
        workspace.add(createMedicinesTable(), BorderLayout.CENTER);

        JLabel instruction = new JLabel(
                "Select a medicine to add it to the customer cart in the next step."
        );
        instruction.setFont(Theme.BODY_FONT);
        instruction.setForeground(Theme.TEXT_SECONDARY);

        workspace.add(instruction, BorderLayout.SOUTH);

        return workspace;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(
                new BorderLayout(12, 0)
        );
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("SEARCH");
        searchLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        searchLabel.setForeground(Theme.GOLD);

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
                        searchMedicines();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent event) {
                        searchMedicines();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent event) {
                        searchMedicines();
                    }
                }
        );

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        return searchPanel;
    }

    private JScrollPane createMedicinesTable() {
        medicinesModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Medicine",
                    "Company",
                    "Type",
                    "Price",
                    "Available",
                    "Expiry Date"
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

        medicinesTable = new JTable(medicinesModel);
        medicinesTable.setRowHeight(36);
        medicinesTable.setFont(Theme.BODY_FONT);
        medicinesTable.setBackground(Theme.SURFACE_LIGHT);
        medicinesTable.setForeground(Theme.TEXT_PRIMARY);
        medicinesTable.setGridColor(new Color(65, 70, 78));
        medicinesTable.setSelectionBackground(
                new Color(22, 120, 92)
        );
        medicinesTable.setSelectionForeground(Color.WHITE);

        medicinesTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        medicinesTable.setFillsViewportHeight(true);

        JTableHeader tableHeader =
                medicinesTable.getTableHeader();

        tableHeader.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        tableHeader.setBackground(Theme.SURFACE);
        tableHeader.setForeground(Theme.GOLD);
        tableHeader.setPreferredSize(
                new Dimension(0, 38)
        );

        JScrollPane scrollPane =
                new JScrollPane(medicinesTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 70, 78)
                )
        );

        scrollPane.getViewport().setBackground(
                Theme.SURFACE_LIGHT
        );

        return scrollPane;
    }

    private void searchMedicines() {
        loadMedicines(searchField.getText().trim());
    }

    private void loadMedicines(String searchText) {
        String sql =
                "SELECT medicine_id, name, company, "
                + "medicine_type, price, quantity_in_stock, "
                + "expiry_date "
                + "FROM medicines "
                + "WHERE quantity_in_stock > 0 "
                + "AND (name LIKE ? "
                + "OR company LIKE ? "
                + "OR medicine_type LIKE ?) "
                + "ORDER BY name";

        medicinesModel.setRowCount(0);

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            String searchPattern =
                    "%" + searchText + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet result =
                    statement.executeQuery()) {

                while (result.next()) {
                    medicinesModel.addRow(
                            new Object[]{
                                result.getInt("medicine_id"),
                                result.getString("name"),
                                result.getString("company"),
                                result.getString("medicine_type"),
                                String.format(
                                        "R %.2f",
                                        result.getDouble("price")
                                ),
                                result.getInt(
                                        "quantity_in_stock"
                                ),
                                result.getDate("expiry_date")
                            }
                    );
                }
            }

            productCountLabel.setText(
                    medicinesModel.getRowCount()
                            + " products available"
            );

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Medicine records could not be loaded.\n\n"
                            + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

            CashierDashboard dashboard =
                    new CashierDashboard(
                            2,
                            "HealthFirst Cashier"
                    );

            dashboard.setVisible(true);
        });
    }
}