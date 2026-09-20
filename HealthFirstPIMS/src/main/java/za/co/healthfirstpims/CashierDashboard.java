package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
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
    private JTable cartTable;

    private DefaultTableModel medicinesModel;
    private DefaultTableModel cartModel;

    private JSpinner quantitySpinner;
    private JLabel productCountLabel;
    private JLabel totalLabel;

    private BigDecimal cartTotal = BigDecimal.ZERO;

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

        setSize(1350, 800);
        setMinimumSize(new Dimension(1100, 700));
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

        JButton logoutButton = createDangerButton("LOG OUT");
        logoutButton.addActionListener(event -> logout());

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
                        25, 28, 28, 28
                )
        );

        content.add(createHeadingPanel(), BorderLayout.NORTH);
        content.add(createPointOfSalePanel(), BorderLayout.CENTER);

        return content;
    }

    private JPanel createHeadingPanel() {
        JPanel headingPanel = new JPanel(new BorderLayout());
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
                "Select medicines, build the cart and complete checkout"
        );
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        productCountLabel = new JLabel("0 products available");
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

    private JSplitPane createPointOfSalePanel() {
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createMedicinePanel(),
                createCartPanel()
        );

        splitPane.setResizeWeight(0.58);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setBackground(Theme.BACKGROUND);

        return splitPane;
    }

    private JPanel createMedicinePanel() {
        JPanel panel = createSectionPanel();

        JLabel heading = createSectionHeading(
                "AVAILABLE MEDICINES"
        );

        panel.add(heading, BorderLayout.NORTH);

        JPanel centre = new JPanel(
                new BorderLayout(0, 14)
        );
        centre.setOpaque(false);

        centre.add(createSearchPanel(), BorderLayout.NORTH);
        centre.add(createMedicinesTable(), BorderLayout.CENTER);

        panel.add(centre, BorderLayout.CENTER);
        panel.add(createAddToCartPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = createSectionPanel();

        JLabel heading = createSectionHeading(
                "CUSTOMER CART"
        );

        panel.add(heading, BorderLayout.NORTH);
        panel.add(createCartTable(), BorderLayout.CENTER);
        panel.add(createCartFooter(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Theme.SURFACE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        return panel;
    }

    private JLabel createSectionHeading(String text) {
        JLabel heading = new JLabel(text);
        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        heading.setForeground(Theme.GOLD);

        return heading;
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
        searchLabel.setForeground(Theme.TEXT_SECONDARY);

        searchField = new JTextField();
        styleTextField(searchField);

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
                    "Type",
                    "Price (R)",
                    "Stock",
                    "Expiry"
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
        styleTable(medicinesTable);

        return createScrollPane(medicinesTable);
    }

    private JScrollPane createCartTable() {
        cartModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Medicine",
                    "Qty",
                    "Price (R)",
                    "Subtotal (R)"
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

        cartTable = new JTable(cartModel);
        styleTable(cartTable);

        return createScrollPane(cartTable);
    }

    private JPanel createAddToCartPanel() {
        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        panel.setOpaque(false);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(Theme.BODY_FONT);
        quantityLabel.setForeground(Theme.TEXT_PRIMARY);

        quantitySpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 999, 1)
        );

        quantitySpinner.setPreferredSize(
                new Dimension(75, 40)
        );

        JButton addButton = new JButton("ADD TO CART");
        Theme.stylePrimaryButton(addButton);

        addButton.setPreferredSize(
                new Dimension(150, 42)
        );

        addButton.addActionListener(
                event -> addSelectedMedicine()
        );

        panel.add(quantityLabel);
        panel.add(quantitySpinner);
        panel.add(addButton);

        return panel;
    }

    private JPanel createCartFooter() {
        JPanel footer = new JPanel(
                new BorderLayout(0, 16)
        );
        footer.setOpaque(false);

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);

        JLabel totalTitle = new JLabel("TOTAL");
        totalTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        totalTitle.setForeground(Theme.TEXT_SECONDARY);

        totalLabel = new JLabel("R 0.00");
        totalLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );
        totalLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );
        totalLabel.setForeground(Theme.GOLD);

        totalPanel.add(totalTitle, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);

        JPanel buttons = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );
        buttons.setOpaque(false);

        JButton removeButton =
                createSecondaryButton("REMOVE");

        JButton clearButton =
                createDangerButton("CLEAR CART");

        JButton checkoutButton =
                new JButton("CHECKOUT");

        Theme.stylePrimaryButton(checkoutButton);

        removeButton.addActionListener(
                event -> removeSelectedCartItem()
        );

        clearButton.addActionListener(
                event -> clearCart()
        );

        checkoutButton.addActionListener(
                event -> completeCheckout()
        );

        buttons.add(removeButton);
        buttons.add(clearButton);
        buttons.add(checkoutButton);

        footer.add(totalPanel, BorderLayout.NORTH);
        footer.add(buttons, BorderLayout.SOUTH);

        return footer;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(350, 42));
        field.setFont(Theme.BODY_FONT);
        field.setBackground(Theme.SURFACE_LIGHT);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.GOLD);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 80, 90)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(Theme.BODY_FONT);
        table.setBackground(Theme.SURFACE_LIGHT);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(new Color(65, 70, 78));

        table.setSelectionBackground(
                new Color(22, 120, 92)
        );
        table.setSelectionForeground(Color.WHITE);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.GOLD);
        header.setPreferredSize(new Dimension(0, 38));
    }

    private JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);

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
                        10, 14, 10, 14
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
                        10, 14, 10, 14
                )
        );

        return button;
    }

    private void searchMedicines() {
        loadMedicines(searchField.getText().trim());
    }

    private void loadMedicines(String searchText) {
        String sql =
                "SELECT medicine_id, name, medicine_type, "
                + "price, quantity_in_stock, expiry_date "
                + "FROM medicines "
                + "WHERE quantity_in_stock > 0 "
                + "AND expiry_date >= CURDATE() "
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
            String pattern = "%" + searchText + "%";

            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);

            try (ResultSet result =
                    statement.executeQuery()) {

                while (result.next()) {
                    medicinesModel.addRow(
                            new Object[]{
                                result.getInt("medicine_id"),
                                result.getString("name"),
                                result.getString("medicine_type"),
                                result.getBigDecimal("price"),
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
            showDatabaseError(
                    "Medicine records could not be loaded.",
                    exception
            );
        }
    }

    private void addSelectedMedicine() {
        int selectedRow =
                medicinesTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage(
                    "Please select a medicine first.",
                    "No Medicine Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int medicineId = (int) medicinesModel.getValueAt(
                selectedRow, 0
        );

        String medicineName =
                medicinesModel.getValueAt(
                        selectedRow, 1
                ).toString();

        BigDecimal unitPrice = new BigDecimal(
                medicinesModel.getValueAt(
                        selectedRow, 3
                ).toString()
        );

        int availableStock = (int) medicinesModel.getValueAt(
                selectedRow, 4
        );

        int requestedQuantity =
                (int) quantitySpinner.getValue();

        int cartRow = findMedicineInCart(medicineId);

        int existingQuantity = 0;

        if (cartRow >= 0) {
            existingQuantity =
                    (int) cartModel.getValueAt(
                            cartRow, 2
                    );
        }

        int newQuantity =
                existingQuantity + requestedQuantity;

        if (newQuantity > availableStock) {
            showMessage(
                    "Only " + availableStock
                            + " units are currently available.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal subtotal = unitPrice.multiply(
                BigDecimal.valueOf(newQuantity)
        );

        if (cartRow >= 0) {
            cartModel.setValueAt(
                    newQuantity,
                    cartRow,
                    2
            );

            cartModel.setValueAt(
                    subtotal.setScale(
                            2,
                            RoundingMode.HALF_UP
                    ),
                    cartRow,
                    4
            );

        } else {
            cartModel.addRow(
                    new Object[]{
                        medicineId,
                        medicineName,
                        requestedQuantity,
                        unitPrice.setScale(
                                2,
                                RoundingMode.HALF_UP
                        ),
                        unitPrice.multiply(
                                BigDecimal.valueOf(
                                        requestedQuantity
                                )
                        ).setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                    }
            );
        }

        quantitySpinner.setValue(1);
        calculateCartTotal();
    }

    private int findMedicineInCart(int medicineId) {
        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            int cartMedicineId =
                    (int) cartModel.getValueAt(row, 0);

            if (cartMedicineId == medicineId) {
                return row;
            }
        }

        return -1;
    }

    private void removeSelectedCartItem() {
        int selectedRow = cartTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage(
                    "Please select a cart item to remove.",
                    "No Cart Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        cartModel.removeRow(selectedRow);
        calculateCartTotal();
    }

    private void clearCart() {
        if (cartModel.getRowCount() == 0) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Remove all items from the cart?",
                "Clear Cart",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            cartModel.setRowCount(0);
            calculateCartTotal();
        }
    }

    private void calculateCartTotal() {
        cartTotal = BigDecimal.ZERO;

        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            BigDecimal subtotal = new BigDecimal(
                    cartModel.getValueAt(
                            row, 4
                    ).toString()
            );

            cartTotal = cartTotal.add(subtotal);
        }

        totalLabel.setText(
                "R " + cartTotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );
    }

    private void completeCheckout() {
        if (cartModel.getRowCount() == 0) {
            showMessage(
                    "Add at least one medicine before checkout.",
                    "Cart Is Empty",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Complete this sale for R "
                        + cartTotal.setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                        + "?",
                "Confirm Checkout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            int saleId = createSale(connection);

            for (int row = 0;
                    row < cartModel.getRowCount();
                    row++) {

                saveSaleItem(connection, saleId, row);
                reduceMedicineStock(connection, row);
            }

            connection.commit();

            showReceipt(saleId);

            cartModel.setRowCount(0);
            calculateCartTotal();
            loadMedicines(searchField.getText().trim());

        } catch (SQLException exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(
                            rollbackException
                    );
                }
            }

            showDatabaseError(
                    "The sale could not be completed.",
                    exception
            );

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException exception) {
                    System.err.println(
                            exception.getMessage()
                    );
                }
            }
        }
    }

    private int createSale(
            Connection connection
    ) throws SQLException {
        String sql =
                "INSERT INTO sales "
                + "(total_amount, user_id) "
                + "VALUES (?, ?)";

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            statement.setBigDecimal(1, cartTotal);
            statement.setInt(2, cashierId);
            statement.executeUpdate();

            try (ResultSet keys =
                    statement.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException(
                "A sale number could not be generated."
        );
    }

    private void saveSaleItem(
            Connection connection,
            int saleId,
            int row
    ) throws SQLException {
        String sql =
                "INSERT INTO sale_items "
                + "(sale_id, medicine_id, "
                + "quantity_sold, price_at_sale) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, saleId);
            statement.setInt(
                    2,
                    (int) cartModel.getValueAt(row, 0)
            );
            statement.setInt(
                    3,
                    (int) cartModel.getValueAt(row, 2)
            );
            statement.setBigDecimal(
                    4,
                    new BigDecimal(
                            cartModel.getValueAt(
                                    row, 3
                            ).toString()
                    )
            );

            statement.executeUpdate();
        }
    }

    private void reduceMedicineStock(
            Connection connection,
            int row
    ) throws SQLException {
        String sql =
                "UPDATE medicines "
                + "SET quantity_in_stock = "
                + "quantity_in_stock - ? "
                + "WHERE medicine_id = ? "
                + "AND quantity_in_stock >= ?";

        int quantity =
                (int) cartModel.getValueAt(row, 2);

        int medicineId =
                (int) cartModel.getValueAt(row, 0);

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, medicineId);
            statement.setInt(3, quantity);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException(
                        "Stock changed before checkout. "
                                + "Please reload the sale."
                );
            }
        }
    }

    private void showReceipt(int saleId) {
        StringBuilder receipt = new StringBuilder();

        receipt.append("HEALTHFIRST PHARMACY\n");
        receipt.append("Sale number: ").append(saleId);
        receipt.append("\nCashier: ").append(cashierName);
        receipt.append("\n\n");

        for (int row = 0;
                row < cartModel.getRowCount();
                row++) {

            receipt.append(
                    cartModel.getValueAt(row, 1)
            );

            receipt.append(" x");
            receipt.append(
                    cartModel.getValueAt(row, 2)
            );

            receipt.append("   R ");
            receipt.append(
                    cartModel.getValueAt(row, 4)
            );

            receipt.append("\n");
        }

        receipt.append("\nTOTAL: R ");
        receipt.append(
                cartTotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        receipt.append(
                "\n\nThank you for choosing HealthFirst."
        );

        showMessage(
                receipt.toString(),
                "Sale Completed",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logout() {
        if (cartModel.getRowCount() > 0) {
            int cartChoice = JOptionPane.showConfirmDialog(
                    this,
                    "The current cart will be lost. "
                            + "Continue logging out?",
                    "Unsaved Cart",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (cartChoice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        dispose();

        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }

    private void showDatabaseError(
            String message,
            SQLException exception
    ) {
        showMessage(
                message + "\n\n" + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showMessage(
            String message,
            String title,
            int type
    ) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                type
        );
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