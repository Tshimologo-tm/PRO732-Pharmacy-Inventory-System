package za.co.healthfirstpims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ReportsPanel extends JPanel {

    private DefaultTableModel salesModel;
    private DefaultTableModel itemSalesModel;
    private DefaultTableModel lowStockModel;
    private DefaultTableModel expiryModel;

    private JLabel salesSummaryLabel;
    private JLabel itemSummaryLabel;
    private JLabel lowStockSummaryLabel;
    private JLabel expirySummaryLabel;

    public ReportsPanel() {
        createInterface();
        refreshAllReports();
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
        add(createReportTabs(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Business Reports");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "Live operational information from HealthFirst Pharmacy"
        );
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        JButton refreshButton =
                new JButton("REFRESH REPORTS");

        Theme.stylePrimaryButton(refreshButton);

        refreshButton.addActionListener(
                event -> refreshAllReports()
        );

        header.add(titlePanel, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        return header;
    }

    private JTabbedPane createReportTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        tabs.setBackground(Theme.SURFACE);
        tabs.setForeground(Theme.TEXT_PRIMARY);

        salesModel = createModel(
                "Sale ID",
                "Date and Time",
                "Cashier",
                "Total (R)"
        );

        itemSalesModel = createModel(
                "Medicine ID",
                "Medicine",
                "Units Sold",
                "Sales Total (R)"
        );

        lowStockModel = createModel(
                "Medicine ID",
                "Medicine",
                "Current Stock",
                "Reorder Level",
                "Supplier"
        );

        expiryModel = createModel(
                "Medicine ID",
                "Medicine",
                "Stock",
                "Expiry Date",
                "Status"
        );

        salesSummaryLabel = createSummaryLabel();
        itemSummaryLabel = createSummaryLabel();
        lowStockSummaryLabel = createSummaryLabel();
        expirySummaryLabel = createSummaryLabel();

        tabs.addTab(
                "Sales",
                createReportPage(
                        salesModel,
                        salesSummaryLabel
                )
        );

        tabs.addTab(
                "Item-Wise Sales",
                createReportPage(
                        itemSalesModel,
                        itemSummaryLabel
                )
        );

        tabs.addTab(
                "Low Stock",
                createReportPage(
                        lowStockModel,
                        lowStockSummaryLabel
                )
        );

        tabs.addTab(
                "Expiry",
                createReportPage(
                        expiryModel,
                        expirySummaryLabel
                )
        );

        return tabs;
    }

    private DefaultTableModel createModel(
            String... columnNames
    ) {
        return new DefaultTableModel(
                columnNames,
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
    }

    private JLabel createSummaryLabel() {
        JLabel label = new JLabel("Loading report...");
        label.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        label.setForeground(Theme.GOLD);

        return label;
    }

    private JPanel createReportPage(
            DefaultTableModel model,
            JLabel summaryLabel
    ) {
        JPanel page = new JPanel(
                new BorderLayout(0, 15)
        );

        page.setBackground(Theme.SURFACE);

        page.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JPanel summaryPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        summaryPanel.setOpaque(false);
        summaryPanel.add(summaryLabel);

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 70, 78)
                )
        );

        scrollPane.getViewport().setBackground(
                Theme.SURFACE_LIGHT
        );

        page.add(summaryPanel, BorderLayout.NORTH);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
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
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.GOLD);
        header.setPreferredSize(new Dimension(0, 38));
    }

    private void refreshAllReports() {
        loadSalesReport();
        loadItemWiseSalesReport();
        loadLowStockReport();
        loadExpiryReport();
    }

    private void loadSalesReport() {
        salesModel.setRowCount(0);

        String sql =
                "SELECT s.sale_id, s.sale_date, "
                + "u.full_name, s.total_amount "
                + "FROM sales s "
                + "JOIN users u ON s.user_id = u.user_id "
                + "ORDER BY s.sale_date DESC";

        BigDecimal totalRevenue = BigDecimal.ZERO;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {
            while (result.next()) {
                BigDecimal amount =
                        result.getBigDecimal("total_amount");

                totalRevenue = totalRevenue.add(amount);

                salesModel.addRow(
                        new Object[]{
                            result.getInt("sale_id"),
                            result.getTimestamp("sale_date"),
                            result.getString("full_name"),
                            amount
                        }
                );
            }

            salesSummaryLabel.setText(
                    salesModel.getRowCount()
                            + " sales  •  Total revenue: R "
                            + totalRevenue
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "The Sales Report could not be loaded.",
                    exception
            );
        }
    }

    private void loadItemWiseSalesReport() {
        itemSalesModel.setRowCount(0);

        String sql =
                "SELECT m.medicine_id, m.name, "
                + "COALESCE(SUM(si.quantity_sold), 0) "
                + "AS units_sold, "
                + "COALESCE(SUM(si.quantity_sold "
                + "* si.price_at_sale), 0) "
                + "AS sales_total "
                + "FROM medicines m "
                + "LEFT JOIN sale_items si "
                + "ON m.medicine_id = si.medicine_id "
                + "GROUP BY m.medicine_id, m.name "
                + "ORDER BY units_sold DESC, m.name";

        int totalUnits = 0;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {
            while (result.next()) {
                int units = result.getInt("units_sold");
                totalUnits += units;

                itemSalesModel.addRow(
                        new Object[]{
                            result.getInt("medicine_id"),
                            result.getString("name"),
                            units,
                            result.getBigDecimal("sales_total")
                        }
                );
            }

            itemSummaryLabel.setText(
                    itemSalesModel.getRowCount()
                            + " medicine records  •  "
                            + totalUnits
                            + " total units sold"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "The Item-Wise Sales Report "
                            + "could not be loaded.",
                    exception
            );
        }
    }

    private void loadLowStockReport() {
        lowStockModel.setRowCount(0);

        String sql =
                "SELECT m.medicine_id, m.name, "
                + "m.quantity_in_stock, m.reorder_level, "
                + "s.name AS supplier_name "
                + "FROM medicines m "
                + "LEFT JOIN suppliers s "
                + "ON m.supplier_id = s.supplier_id "
                + "WHERE m.quantity_in_stock "
                + "<= m.reorder_level "
                + "ORDER BY m.quantity_in_stock, m.name";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {
            while (result.next()) {
                lowStockModel.addRow(
                        new Object[]{
                            result.getInt("medicine_id"),
                            result.getString("name"),
                            result.getInt(
                                    "quantity_in_stock"
                            ),
                            result.getInt("reorder_level"),
                            result.getString("supplier_name")
                        }
                );
            }

            lowStockSummaryLabel.setText(
                    lowStockModel.getRowCount()
                            + " medicines require restocking"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "The Low Stock Report could not be loaded.",
                    exception
            );
        }
    }

    private void loadExpiryReport() {
        expiryModel.setRowCount(0);

        String sql =
                "SELECT medicine_id, name, "
                + "quantity_in_stock, expiry_date, "
                + "CASE "
                + "WHEN expiry_date < CURDATE() "
                + "THEN 'EXPIRED' "
                + "WHEN expiry_date <= DATE_ADD("
                + "CURDATE(), INTERVAL 30 DAY) "
                + "THEN 'EXPIRING SOON' "
                + "ELSE 'VALID' "
                + "END AS expiry_status "
                + "FROM medicines "
                + "WHERE expiry_date <= DATE_ADD("
                + "CURDATE(), INTERVAL 30 DAY) "
                + "ORDER BY expiry_date";

        int expiredCount = 0;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {
            while (result.next()) {
                String status =
                        result.getString("expiry_status");

                if ("EXPIRED".equals(status)) {
                    expiredCount++;
                }

                expiryModel.addRow(
                        new Object[]{
                            result.getInt("medicine_id"),
                            result.getString("name"),
                            result.getInt(
                                    "quantity_in_stock"
                            ),
                            result.getDate("expiry_date"),
                            status
                        }
                );
            }

            expirySummaryLabel.setText(
                    expiryModel.getRowCount()
                            + " expiry alerts  •  "
                            + expiredCount
                            + " already expired"
            );

        } catch (SQLException exception) {
            showDatabaseError(
                    "The Expiry Report could not be loaded.",
                    exception
            );
        }
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
}