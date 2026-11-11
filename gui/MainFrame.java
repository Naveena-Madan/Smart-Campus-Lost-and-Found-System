package gui;

import exception.InvalidItemException;
import model.FoundItem;
import model.Item;
import model.LostItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {

    private JTextField itemNameField;
    private JTextField locationField;
    private JTextField searchField;

    private JComboBox<String> typeBox;

    private JTable reportTable;
    private DefaultTableModel tableModel;

    // =========================================================
    // LOGGED-IN USER
    // =========================================================

    private String loggedInUsername;

    // =========================================================
    // DATABASE
    // =========================================================

    private final String url = database.DBConfig.DB_URL;

    private final String username = database.DBConfig.DB_USER;

    private final String password = database.DBConfig.DB_PASSWORD;

    // =========================================================
    // COLORS
    // =========================================================

    private final Color NAVY =
            new Color(27, 54, 93);

    private final Color BLUE =
            new Color(52, 112, 190);

    private final Color BACKGROUND =
            new Color(245, 247, 250);

    private final Color WHITE =
            Color.WHITE;

    private final Color TEXT =
            new Color(35, 45, 60);

    private final Color GRAY =
            new Color(105, 115, 130);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MainFrame(String username) {

        loggedInUsername = username;

        setTitle(
                "Smart Campus Lost and Found System"
        );

        setSize(
                1050,
                750
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        // ==================================================
        // MAIN PANEL
        // ==================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        mainPanel.setBorder(
                new EmptyBorder(
                        20,
                        35,
                        20,
                        35
                )
        );

        // ==================================================
        // HEADER
        // ==================================================

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setOpaque(false);

        JPanel headingPanel =
                new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(
                new BoxLayout(
                        headingPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "Lost & Found Reports"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Report lost or found items and search campus records."
                );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                GRAY
        );

        headingPanel.add(
                titleLabel
        );

        headingPanel.add(
                Box.createVerticalStrut(4)
        );

        headingPanel.add(
                subtitleLabel
        );

        JButton backButton =
                createButton(
                        "Back to Dashboard",
                        NAVY,
                        WHITE
                );

        backButton.setPreferredSize(
                new Dimension(
                        155,
                        40
                )
        );

        headerPanel.add(
                headingPanel,
                BorderLayout.WEST
        );

        headerPanel.add(
                backButton,
                BorderLayout.EAST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // ==================================================
        // CENTER AREA
        // ==================================================

        JPanel centerArea =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        centerArea.setOpaque(false);

        centerArea.setBorder(
                new EmptyBorder(
                        18,
                        0,
                        0,
                        0
                )
        );

        // ==================================================
        // REPORT CARD
        // ==================================================

        JPanel reportCard =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        reportCard.setBackground(
                WHITE
        );

        reportCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        225,
                                        232
                                )
                        ),
                        new EmptyBorder(
                                12,
                                18,
                                12,
                                18
                        )
                )
        );

        JLabel reportTitle =
                new JLabel(
                        "Report a Lost or Found Item"
                );

        reportTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        reportTitle.setForeground(
                TEXT
        );

        reportCard.add(
                reportTitle,
                BorderLayout.NORTH
        );

        // ==================================================
        // FORM
        // ==================================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        4,
                        5,
                        4,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ==================================================
        // ITEM NAME
        // ==================================================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        JLabel itemLabel =
                new JLabel(
                        "Item Name"
                );

        itemLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        itemLabel.setForeground(
                TEXT
        );

        formPanel.add(
                itemLabel,
                gbc
        );

        itemNameField =
                createTextField();

        itemNameField.setPreferredSize(
                new Dimension(
                        230,
                        34
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        formPanel.add(
                itemNameField,
                gbc
        );

        // ==================================================
        // LOCATION
        // ==================================================

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;

        JLabel locationLabel =
                new JLabel(
                        "Location"
                );

        locationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        locationLabel.setForeground(
                TEXT
        );

        formPanel.add(
                locationLabel,
                gbc
        );

        locationField =
                createTextField();

        locationField.setPreferredSize(
                new Dimension(
                        210,
                        34
                )
        );

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1;

        formPanel.add(
                locationField,
                gbc
        );

        // ==================================================
        // TYPE
        // ==================================================

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0;

        JLabel typeLabel =
                new JLabel(
                        "Type"
                );

        typeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        typeLabel.setForeground(
                TEXT
        );

        formPanel.add(
                typeLabel,
                gbc
        );

        String[] types = {
                "Lost Item",
                "Found Item"
        };

        typeBox =
                new JComboBox<>(
                        types
                );

        typeBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        typeBox.setPreferredSize(
                new Dimension(
                        130,
                        34
                )
        );

        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(
                typeBox,
                gbc
        );

        // ==================================================
        // SUBMIT
        // ==================================================

        JButton submitButton =
                createButton(
                        "Submit Report",
                        BLUE,
                        WHITE
                );

        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(
                submitButton,
                gbc
        );

        // ==================================================
        // CLEAR
        // ==================================================

        JButton clearButton =
                createButton(
                        "Clear",
                        new Color(
                                110,
                                120,
                                135
                        ),
                        WHITE
                );

        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(
                clearButton,
                gbc
        );

        reportCard.add(
                formPanel,
                BorderLayout.CENTER
        );

        // ==================================================
        // SEARCH CARD
        // ==================================================

        JPanel searchCard =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        searchCard.setBackground(
                WHITE
        );

        searchCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        225,
                                        232
                                )
                        ),
                        new EmptyBorder(
                                10,
                                18,
                                10,
                                18
                        )
                )
        );

        JLabel searchLabel =
                new JLabel(
                        "Search Reports"
                );

        searchLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        searchLabel.setForeground(
                TEXT
        );

        searchCard.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchField =
                createTextField();

        searchField.setPreferredSize(
                new Dimension(
                        0,
                        34
                )
        );

        searchField.setToolTipText(
                "Search by item name or location"
        );

        searchCard.add(
                searchField,
                BorderLayout.CENTER
        );

        JPanel searchButtonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        searchButtonPanel.setOpaque(false);

        JButton searchButton =
                createButton(
                        "Search",
                        BLUE,
                        WHITE
                );

        JButton showAllButton =
                createButton(
                        "Show All",
                        NAVY,
                        WHITE
                );

        searchButtonPanel.add(
                searchButton
        );

        searchButtonPanel.add(
                showAllButton
        );

        searchCard.add(
                searchButtonPanel,
                BorderLayout.EAST
        );

        // ==================================================
        // TOP SECTION
        // ==================================================

        JPanel topSection =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        topSection.setOpaque(false);

        topSection.add(
                reportCard,
                BorderLayout.NORTH
        );

        topSection.add(
                searchCard,
                BorderLayout.SOUTH
        );

        centerArea.add(
                topSection,
                BorderLayout.NORTH
        );

        // ==================================================
        // CAMPUS REPORTS
        // ==================================================

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        tablePanel.setOpaque(false);

        JLabel tableTitle =
                new JLabel(
                        "Campus Reports"
                );

        tableTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        tableTitle.setForeground(
                TEXT
        );

        tablePanel.add(
                tableTitle,
                BorderLayout.NORTH
        );

        // ==================================================
        // TABLE MODEL
        // ==================================================

        String[] columns = {
                "ID",
                "Item Name",
                "Location",
                "Type",
                "Status"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
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

        reportTable =
                new JTable(
                        tableModel
                );

        reportTable.setRowHeight(
                30
        );

        reportTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        reportTable.setForeground(
                TEXT
        );

        reportTable.setBackground(
                WHITE
        );

        reportTable.setSelectionBackground(
                new Color(
                        220,
                        232,
                        248
                )
        );

        reportTable.setSelectionForeground(
                TEXT
        );

        reportTable.setGridColor(
                new Color(
                        225,
                        230,
                        236
                )
        );

        reportTable.setShowVerticalLines(
                false
        );

        reportTable.setAutoCreateRowSorter(
                true
        );

        reportTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        reportTable.getTableHeader()
                .setBackground(
                        NAVY
                );

        reportTable.getTableHeader()
                .setForeground(
                        WHITE
                );

        reportTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );

        // ==================================================
        // SCROLL PANE
        // ==================================================

        JScrollPane tableScrollPane =
                new JScrollPane(
                        reportTable
                );

        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                215,
                                222,
                                230
                        )
                )
        );

        tableScrollPane.setPreferredSize(
                new Dimension(
                        0,
                        225
                )
        );

        tablePanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        // ==================================================
        // TABLE BOTTOM
        // ==================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setOpaque(false);

        bottomPanel.setBorder(
                new EmptyBorder(
                        8,
                        0,
                        0,
                        0
                )
        );

        JButton refreshButton =
                createButton(
                        "Refresh Reports",
                        BLUE,
                        WHITE
                );

        JLabel footerLabel =
                new JLabel(
                        "Logged in as: "
                                + loggedInUsername
                );

        footerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        footerLabel.setForeground(
                GRAY
        );

        bottomPanel.add(
                refreshButton,
                BorderLayout.WEST
        );

        bottomPanel.add(
                footerLabel,
                BorderLayout.EAST
        );

        tablePanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        centerArea.add(
                tablePanel,
                BorderLayout.CENTER
        );

        // ==================================================
        // ADD CENTER
        // ==================================================

        mainPanel.add(
                centerArea,
                BorderLayout.CENTER
        );

        // ==================================================
        // BUTTON ACTIONS
        // ==================================================

        submitButton.addActionListener(
                e -> submitReport()
        );

        clearButton.addActionListener(e -> {

            itemNameField.setText("");

            locationField.setText("");

            searchField.setText("");

            typeBox.setSelectedIndex(0);

            tableModel.setRowCount(0);
        });

        searchButton.addActionListener(
                e -> searchReports()
        );

        showAllButton.addActionListener(e -> {

            searchField.setText("");

            loadReports();
        });

        refreshButton.addActionListener(
                e -> loadReports()
        );

        backButton.addActionListener(e -> {

            dispose();
        });

        // ==================================================
        // ADD MAIN PANEL
        // ==================================================

        add(mainPanel);

        // ==================================================
        // LOAD REPORTS
        // ==================================================

        loadReports();
    }

    // =========================================================
    // CREATE TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        205,
                                        213,
                                        223
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                4,
                                10,
                                4,
                                10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // CREATE BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.setBackground(
                background
        );

        button.setForeground(
                foreground
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        14,
                        8,
                        14
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================================================
    // SUBMIT REPORT
    // =========================================================

    private void submitReport() {

        String itemName =
                itemNameField
                        .getText()
                        .trim();

        String location =
                locationField
                        .getText()
                        .trim();

        String type =
                (String) typeBox
                        .getSelectedItem();

        // ==================================================
        // VALIDATION
        // ==================================================

        if (itemName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the item name.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (location.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the location.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            // ==================================================
            // CREATE OOP OBJECT
            // ==================================================

            Item item;

            if (type.equals("Lost Item")) {

                item =
                        new LostItem(
                                itemName,
                                location,
                                "15-09-2026"
                        );

            } else {

                item =
                        new FoundItem(
                                itemName,
                                location,
                                "15-09-2026"
                        );
            }

            // ==================================================
            // REPORT TYPE
            // ==================================================

            String reportType;

            if (type.equals("Lost Item")) {

                reportType = "Lost";

            } else {

                reportType = "Found";
            }

            // ==================================================
            // SQL
            // ==================================================

            String sql =
                    "INSERT INTO reports "
                            + "(item_name, location, report_type, username) "
                            + "VALUES (?, ?, ?, ?)";

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            PreparedStatement statement =
                    connection.prepareStatement(
                            sql
                    );

            statement.setString(
                    1,
                    itemName
            );

            statement.setString(
                    2,
                    location
            );

            statement.setString(
                    3,
                    reportType
            );

            // ==================================================
            // SAVE LOGGED-IN USER
            // ==================================================

            statement.setString(
                    4,
                    loggedInUsername
            );

            statement.executeUpdate();

            statement.close();

            connection.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Report submitted successfully!\n"
                            + "Reported by: "
                            + loggedInUsername,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            itemNameField.setText("");

            locationField.setText("");

            typeBox.setSelectedIndex(0);

            loadReports();

        } catch (InvalidItemException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD ALL REPORTS
    // =========================================================

    private void loadReports() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, item_name, location, "
                        + "report_type, status "
                        + "FROM reports";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            Statement statement =
                    connection.createStatement();

            ResultSet result =
                    statement.executeQuery(sql);

            while (result.next()) {

                addReportToTable(
                        result
                );
            }

            result.close();

            statement.close();

            connection.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load reports:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SEARCH REPORTS
    // =========================================================

    private void searchReports() {

        String searchText =
                searchField
                        .getText()
                        .trim();

        if (searchText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an item name or location.",
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, item_name, location, "
                        + "report_type, status "
                        + "FROM reports "
                        + "WHERE item_name LIKE ? "
                        + "OR location LIKE ?";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            PreparedStatement statement =
                    connection.prepareStatement(
                            sql
                    );

            String searchPattern =
                    "%" + searchText + "%";

            statement.setString(
                    1,
                    searchPattern
            );

            statement.setString(
                    2,
                    searchPattern
            );

            ResultSet result =
                    statement.executeQuery();

            boolean found = false;

            while (result.next()) {

                found = true;

                addReportToTable(
                        result
                );
            }

            if (!found) {

                JOptionPane.showMessageDialog(
                        this,
                        "No matching reports found.",
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            result.close();

            statement.close();

            connection.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ADD DATABASE ROW TO TABLE
    // =========================================================

    private void addReportToTable(
            ResultSet result
    ) throws SQLException {

        int id =
                result.getInt(
                        "id"
                );

        String itemName =
                result.getString(
                        "item_name"
                );

        String location =
                result.getString(
                        "location"
                );

        String reportType =
                result.getString(
                        "report_type"
                );

        String status =
                result.getString(
                        "status"
                );

        tableModel.addRow(
                new Object[]{
                        id,
                        itemName,
                        location,
                        reportType,
                        status
                }
        );
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            MainFrame frame =
                    new MainFrame(
                            "student1"
                    );

            frame.setVisible(true);
        });
    }
}