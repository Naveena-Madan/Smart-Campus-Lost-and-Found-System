package gui;

import service.OwnershipVerificationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminClaimsFrame extends JFrame {

    // ==============================
    // DATABASE
    // ==============================
    private static final String DB_URL = database.DBConfig.DB_URL;
    private static final String DB_USER = database.DBConfig.DB_USER;
    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;
    // ==============================
    // COLORS
    // ==============================
    private static final Color BG = new Color(7, 10, 25);
    private static final Color PANEL = new Color(18, 23, 45);
    private static final Color PANEL_2 = new Color(25, 31, 58);
    private static final Color BLUE = new Color(80, 150, 255);
    private static final Color GREEN = new Color(60, 210, 140);
    private static final Color RED = new Color(245, 90, 100);
    private static final Color ORANGE = new Color(255, 175, 70);
    private static final Color WHITE = new Color(240, 244, 255);
    private static final Color MUTED = new Color(165, 175, 205);

    // ==============================
    // UI
    // ==============================
    private JTable claimsTable;
    private DefaultTableModel tableModel;

    private JButton refreshButton;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton historyButton;
    private JButton backButton;

    // ==============================
    // CONSTRUCTOR
    // ==============================
    public AdminClaimsFrame() {

        setTitle("Admin - Claim Verification");
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();
        loadClaims();
    }

    // ==============================
    // BUILD UI
    // ==============================
    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==============================
        // HEADER
        // ==============================
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(5, 5, 15, 5));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Claim Verification");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Review ownership claims submitted by students"
        );
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        JLabel adminLabel = new JLabel("ADMIN PANEL");
        adminLabel.setForeground(BLUE);
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        header.add(adminLabel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ==============================
        // TABLE
        // ==============================
        tableModel = new DefaultTableModel(
                new Object[]{
                        "Claim ID",
                        "Report ID",
                        "Username",
                        "Item",
                        "Location",
                        "Ownership Details",
                        "AI Score",
                        "AI Confidence",
                        "Status"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        claimsTable = new JTable(tableModel);

        claimsTable.setRowHeight(48);
        claimsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        claimsTable.setForeground(WHITE);
        claimsTable.setBackground(PANEL);
        claimsTable.setGridColor(new Color(45, 52, 80));
        claimsTable.setSelectionBackground(new Color(55, 85, 135));
        claimsTable.setSelectionForeground(Color.WHITE);
        claimsTable.setShowVerticalLines(false);
        claimsTable.setShowHorizontalLines(true);

        claimsTable.getTableHeader().setBackground(PANEL_2);
        claimsTable.getTableHeader().setForeground(WHITE);
        claimsTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        claimsTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        // Column widths
        claimsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        claimsTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        claimsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        claimsTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        claimsTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        claimsTable.getColumnModel().getColumn(5).setPreferredWidth(280);
        claimsTable.getColumnModel().getColumn(6).setPreferredWidth(90);
        claimsTable.getColumnModel().getColumn(7).setPreferredWidth(110);
        claimsTable.getColumnModel().getColumn(8).setPreferredWidth(100);

        // Center some columns
        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        claimsTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        claimsTable.getColumnModel()
                .getColumn(1)
                .setCellRenderer(centerRenderer);

        claimsTable.getColumnModel()
                .getColumn(8)
                .setCellRenderer(new StatusRenderer());

        // Double click
        claimsTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int selectedRow = claimsTable.getSelectedRow();

                    if (selectedRow >= 0) {
                        int modelRow =
                                claimsTable.convertRowIndexToModel(
                                        selectedRow
                                );
                        showClaimDetails(modelRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(claimsTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(45, 52, 80)
                )
        );

        scrollPane.getViewport().setBackground(PANEL);

        JScrollBar verticalBar =
                scrollPane.getVerticalScrollBar();

        verticalBar.setPreferredSize(
                new Dimension(10, 0)
        );

        verticalBar.setBackground(PANEL);

        root.add(scrollPane, BorderLayout.CENTER);

        // ==============================
        // BUTTON PANEL
        // ==============================
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(
                new EmptyBorder(15, 5, 0, 5)
        );

        JPanel leftButtons = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                10,
                0
        ));

        leftButtons.setOpaque(false);

        refreshButton = createButton(
                "Refresh",
                BLUE
        );

        approveButton = createButton(
                "Approve Claim",
                GREEN
        );

        rejectButton = createButton(
                "Reject Claim",
                RED
        );

        historyButton = createButton(
                "View History",
                new Color(150, 120, 255)
        );

        leftButtons.add(refreshButton);
        leftButtons.add(approveButton);
        leftButtons.add(rejectButton);
        leftButtons.add(historyButton);

        JPanel rightButtons = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        0
                )
        );

        rightButtons.setOpaque(false);

        backButton = createButton(
                "Back",
                new Color(100, 110, 135)
        );

        rightButtons.add(backButton);

        bottomPanel.add(
                leftButtons,
                BorderLayout.WEST
        );

        bottomPanel.add(
                rightButtons,
                BorderLayout.EAST
        );

        root.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ==============================
        // BUTTON ACTIONS
        // ==============================
        refreshButton.addActionListener(e -> loadClaims());

        approveButton.addActionListener(
                e -> updateClaimStatus("Approved")
        );

        rejectButton.addActionListener(
                e -> updateClaimStatus("Rejected")
        );

        historyButton.addActionListener(
                e -> showClaimHistory()
        );

        backButton.addActionListener(
                e -> dispose()
        );

        setContentPane(root);
    }

    // ==============================
    // CREATE BUTTON
    // ==============================
    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                new EmptyBorder(
                        11,
                        18,
                        11,
                        18
                )
        );

        return button;
    }

    // ==============================
    // LOAD CLAIMS
    // ==============================
    private void loadClaims() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT c.id AS claim_id, " +
                        "c.report_id, " +
                        "c.username, " +
                        "r.item_name, " +
                        "r.location, " +
                        "c.answer, " +
                        "c.status " +
                        "FROM claims c " +
                        "JOIN reports r " +
                        "ON c.report_id = r.id " +
                        "WHERE LOWER(c.status) = 'pending' " +
                        "ORDER BY c.id DESC";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USER,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet result =
                            statement.executeQuery()
            ) {

                OwnershipVerificationService verificationService =
                        new OwnershipVerificationService();

                while (result.next()) {

                    int reportId =
                            result.getInt("report_id");

                    String answer =
                            result.getString("answer");

                    OwnershipVerificationService.VerificationResult verificationResult =
                            verificationService.verifyOwnership(
                                    reportId,
                                    answer
                            );

                    String score =
                            String.format(
                                    "%.2f%%",
                                    verificationResult.getScore()
                            );

                    tableModel.addRow(
                            new Object[]{
                                    result.getInt("claim_id"),
                                    reportId,
                                    result.getString("username"),
                                    result.getString("item_name"),
                                    result.getString("location"),
                                    answer,
                                    score,
                                    verificationResult.getConfidence(),
                                    result.getString("status")
                            }
                    );
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showMessage(
                    "Database Error",
                    "Unable to load claims.\n\n"
                            + ex.getMessage(),
                    RED
            );
        }
    }

    // ==============================
    // SHOW CLAIM HISTORY
    // ==============================
    private void showClaimHistory() {

        JDialog historyDialog = new JDialog(
                this,
                "Claim History",
                true
        );

        historyDialog.setSize(1100, 620);
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==============================
        // HEADER
        // ==============================
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        JLabel titleLabel = new JLabel("Claim History");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        JLabel subtitleLabel = new JLabel(
                "Previously processed claims — Approved and Rejected"
        );
        subtitleLabel.setForeground(MUTED);
        subtitleLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // ==============================
        // HISTORY TABLE
        // ==============================
        DefaultTableModel historyModel =
                new DefaultTableModel(
                        new Object[]{
                                "Claim ID",
                                "Report ID",
                                "Username",
                                "Item",
                                "Location",
                                "Ownership Details",
                                "AI Score",
                                "AI Confidence",
                                "Status"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable historyTable =
                new JTable(historyModel);

        historyTable.setRowHeight(44);
        historyTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        historyTable.setForeground(WHITE);
        historyTable.setBackground(PANEL);
        historyTable.setGridColor(
                new Color(45, 52, 80)
        );
        historyTable.setSelectionBackground(
                new Color(55, 85, 135)
        );
        historyTable.setSelectionForeground(
                Color.WHITE
        );
        historyTable.setShowVerticalLines(false);

        historyTable.getTableHeader().setBackground(
                PANEL_2
        );
        historyTable.getTableHeader().setForeground(
                WHITE
        );
        historyTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        historyTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42)
        );

        // Column widths
        historyTable.getColumnModel().getColumn(0)
                .setPreferredWidth(65);
        historyTable.getColumnModel().getColumn(1)
                .setPreferredWidth(70);
        historyTable.getColumnModel().getColumn(2)
                .setPreferredWidth(110);
        historyTable.getColumnModel().getColumn(3)
                .setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(4)
                .setPreferredWidth(130);
        historyTable.getColumnModel().getColumn(5)
                .setPreferredWidth(250);
        historyTable.getColumnModel().getColumn(6)
                .setPreferredWidth(85);
        historyTable.getColumnModel().getColumn(7)
                .setPreferredWidth(105);
        historyTable.getColumnModel().getColumn(8)
                .setPreferredWidth(95);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        historyTable.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);
        historyTable.getColumnModel().getColumn(1)
                .setCellRenderer(centerRenderer);
        historyTable.getColumnModel().getColumn(8)
                .setCellRenderer(new StatusRenderer());

        // ==============================
        // LOAD HISTORY
        // ==============================
        String sql =
                "SELECT c.id AS claim_id, " +
                        "c.report_id, " +
                        "c.username, " +
                        "r.item_name, " +
                        "r.location, " +
                        "c.answer, " +
                        "c.status " +
                        "FROM claims c " +
                        "JOIN reports r " +
                        "ON c.report_id = r.id " +
                        "WHERE LOWER(c.status) IN " +
                        "('approved', 'rejected') " +
                        "ORDER BY c.id DESC";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USER,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet result =
                            statement.executeQuery()
            ) {

                OwnershipVerificationService
                        verificationService =
                        new OwnershipVerificationService();

                while (result.next()) {

                    int reportId =
                            result.getInt("report_id");

                    String answer =
                            result.getString("answer");

                    OwnershipVerificationService
                            .VerificationResult
                            verificationResult =
                            verificationService.verifyOwnership(
                                    reportId,
                                    answer
                            );

                    String score =
                            String.format(
                                    "%.2f%%",
                                    verificationResult.getScore()
                            );

                    historyModel.addRow(
                            new Object[]{
                                    result.getInt("claim_id"),
                                    reportId,
                                    result.getString(
                                            "username"
                                    ),
                                    result.getString(
                                            "item_name"
                                    ),
                                    result.getString(
                                            "location"
                                    ),
                                    answer,
                                    score,
                                    verificationResult
                                            .getConfidence(),
                                    result.getString(
                                            "status"
                                    )
                            }
                    );
                }

            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showMessage(
                    "History Error",
                    "Unable to load claim history.\n\n"
                            + ex.getMessage(),
                    RED
            );

            return;
        }

        JScrollPane scrollPane =
                new JScrollPane(historyTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(45, 52, 80)
                )
        );

        scrollPane.getViewport().setBackground(
                PANEL
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // ==============================
        // CLOSE BUTTON
        // ==============================
        JButton reopenRejectedButton =
                createButton(
                        "Reopen Rejected",
                        ORANGE
                );

        reopenRejectedButton.addActionListener(
                e -> reopenRejectedClaim(
                        historyTable,
                        historyModel,
                        historyDialog
                )
        );

        JButton reopenApprovedButton =
                createButton(
                        "Reopen Approved",
                        BLUE
                );

        reopenApprovedButton.addActionListener(
                e -> reopenApprovedClaim(
                        historyTable,
                        historyModel,
                        historyDialog
                )
        );

        JButton closeButton =
                createButton(
                        "Close",
                        new Color(100, 110, 135)
                );

        closeButton.addActionListener(
                e -> historyDialog.dispose()
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.setOpaque(false);
        bottomPanel.add(reopenRejectedButton);
        bottomPanel.add(reopenApprovedButton);
        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        historyDialog.setContentPane(mainPanel);
        historyDialog.setVisible(true);
    }

    // ==============================
    // REOPEN APPROVED CLAIM
    // ==============================
    private void reopenApprovedClaim(
            JTable historyTable,
            DefaultTableModel historyModel,
            JDialog historyDialog
    ) {

        int selectedRow = historyTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage(
                    "Select a Claim",
                    "Please select an approved claim from history first.",
                    ORANGE
            );
            return;
        }

        int modelRow =
                historyTable.convertRowIndexToModel(selectedRow);

        int claimId =
                Integer.parseInt(
                        historyModel.getValueAt(modelRow, 0).toString()
                );

        int reportId =
                Integer.parseInt(
                        historyModel.getValueAt(modelRow, 1).toString()
                );

        String username =
                historyModel.getValueAt(modelRow, 2).toString();

        String itemName =
                historyModel.getValueAt(modelRow, 3).toString();

        String status =
                historyModel.getValueAt(modelRow, 8).toString();

        if (!"Approved".equalsIgnoreCase(status)) {
            showMessage(
                    "Cannot Reopen",
                    "Only approved claims can be reopened using this option.\n\n"
                            + "Selected claim status: " + status,
                    ORANGE
            );
            return;
        }

        String previousReportStatus = null;
        Connection connection = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD
            );

            connection.setAutoCommit(false);

            String getPreviousStatusSQL =
                    "SELECT previous_report_status "
                            + "FROM claims "
                            + "WHERE id = ? "
                            + "FOR UPDATE";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(getPreviousStatusSQL)
            ) {

                statement.setInt(1, claimId);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        throw new SQLException("Claim not found.");
                    }

                    previousReportStatus =
                            result.getString("previous_report_status");
                }
            }

            // =====================================================
            // LEGACY CLAIM FALLBACK
            // =====================================================
            // Older approved claims were created before
            // previous_report_status was stored. For those claims,
            // the report is restored to Pending instead of blocking
            // the admin with the old "Cannot Reopen Safely" message.
            if (previousReportStatus == null
                    || previousReportStatus.trim().isEmpty()) {

                previousReportStatus = "Pending";
            }

            String message =
                    "Reopen this approved claim?\n\n"
                            + "Student: " + username + "\n"
                            + "Item: " + itemName + "\n"
                            + "Report ID: " + reportId + "\n\n"
                            + "Claim will become Pending.\n"
                            + "Report will be restored to: "
                            + previousReportStatus;

            boolean confirmed =
                    showGalaxyConfirmation(
                            "Reopen Approved Claim",
                            "Reopen this approved claim?",
                            message.replace(
                                    "Reopen this approved claim?\\n\\n",
                                    ""
                            ),
                            "Reopen Claim",
                            BLUE
                    );

            if (!confirmed) {
                connection.rollback();
                return;
            }

            String updateClaimSQL =
                    "UPDATE claims "
                            + "SET status = 'Pending' "
                            + "WHERE id = ? "
                            + "AND LOWER(status) = 'approved'";

            String updateReportSQL =
                    "UPDATE reports "
                            + "SET status = ? "
                            + "WHERE id = ? "
                            + "AND status = 'Returned'";

            String notificationSQL =
                    "INSERT INTO notifications "
                            + "(username, message, status) "
                            + "VALUES (?, ?, 'UNREAD')";

            int updatedClaimRows;

            try (PreparedStatement statement =
                         connection.prepareStatement(updateClaimSQL)) {

                statement.setInt(1, claimId);
                updatedClaimRows = statement.executeUpdate();
            }

            if (updatedClaimRows != 1) {
                throw new SQLException(
                        "The approved claim could not be reopened. "
                                + "It may have already been processed."
                );
            }

            int updatedReportRows;

            try (PreparedStatement statement =
                         connection.prepareStatement(updateReportSQL)) {

                statement.setString(1, previousReportStatus);
                statement.setInt(2, reportId);
                updatedReportRows = statement.executeUpdate();
            }

            if (updatedReportRows != 1) {
                throw new SQLException(
                        "The report could not be restored safely. "
                                + "Its current status is no longer Returned."
                );
            }

            String notificationMessage =
                    "Your approved claim for item '"
                            + itemName
                            + "' (Report ID "
                            + reportId
                            + ") has been reopened for further ownership verification.";

            try (PreparedStatement statement =
                         connection.prepareStatement(notificationSQL)) {

                statement.setString(1, username);
                statement.setString(2, notificationMessage);
                statement.executeUpdate();
            }

            connection.commit();

            historyModel.removeRow(modelRow);
            loadClaims();

            showMessage(
                    "Claim Reopened",
                    "The approved claim has been reopened successfully.\n\n"
                            + "• Claim status: Pending\n"
                            + "• Report restored to: "
                            + previousReportStatus + "\n"
                            + "• Student notification: Created",
                    GREEN
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            showMessage(
                    "Reopen Failed",
                    "The approved claim could not be reopened.\n\n"
                            + ex.getMessage(),
                    RED
            );

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeError) {
                    closeError.printStackTrace();
                }
            }
        }
    }


    // ==============================
    // REOPEN REJECTED CLAIM
    // ==============================
    private void reopenRejectedClaim(
            JTable historyTable,
            DefaultTableModel historyModel,
            JDialog historyDialog
    ) {

        int selectedRow =
                historyTable.getSelectedRow();

        if (selectedRow == -1) {

            showMessage(
                    "Select a Claim",
                    "Please select a rejected claim "
                            + "from history first.",
                    ORANGE
            );

            return;
        }

        int modelRow =
                historyTable.convertRowIndexToModel(
                        selectedRow
                );

        int claimId =
                Integer.parseInt(
                        historyModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        int reportId =
                Integer.parseInt(
                        historyModel.getValueAt(
                                modelRow,
                                1
                        ).toString()
                );

        String username =
                historyModel.getValueAt(
                        modelRow,
                        2
                ).toString();

        String itemName =
                historyModel.getValueAt(
                        modelRow,
                        3
                ).toString();

        String status =
                historyModel.getValueAt(
                        modelRow,
                        8
                ).toString();

        // Only rejected claims can be reopened
        if (!"Rejected".equalsIgnoreCase(status)) {

            showMessage(
                    "Cannot Reopen",
                    "Only rejected claims can be reopened.\n\n"
                            + "Selected claim status: "
                            + status,
                    ORANGE
            );

            return;
        }

        String message =
                "Reopen this rejected claim?\n\n"
                        + "Student: " + username + "\n"
                        + "Item: " + itemName + "\n"
                        + "Report ID: " + reportId
                        + "\n\n"
                        + "The claim will be changed back to "
                        + "Pending and returned to the active "
                        + "verification queue.";

        boolean confirmed =
                showGalaxyConfirmation(
                        "Reopen Claim",
                        "Reopen this rejected claim?",
                        message.replace(
                                "Reopen this rejected claim?\\n\\n",
                                ""
                        ),
                        "Reopen Claim",
                        ORANGE
                );

        if (!confirmed) {
            return;
        }

        String updateClaimSQL =
                "UPDATE claims "
                        + "SET status = 'Pending' "
                        + "WHERE id = ? "
                        + "AND LOWER(status) = 'rejected'";

        String notificationSQL =
                "INSERT INTO notifications "
                        + "(username, message, status) "
                        + "VALUES (?, ?, 'UNREAD')";

        Connection connection = null;

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USER,
                            DB_PASSWORD
                    );

            connection.setAutoCommit(false);

            int updatedRows;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateClaimSQL
                            )
            ) {

                statement.setInt(
                        1,
                        claimId
                );

                updatedRows =
                        statement.executeUpdate();
            }

            if (updatedRows != 1) {

                throw new SQLException(
                        "The claim could not be reopened. "
                                + "It may have already been processed."
                );
            }

            String notificationMessage =
                    "Your claim for item '"
                            + itemName
                            + "' (Report ID "
                            + reportId
                            + ") has been reopened "
                            + "for further ownership verification.";

            try (
                    PreparedStatement notificationStatement =
                            connection.prepareStatement(
                                    notificationSQL
                            )
            ) {

                notificationStatement.setString(
                        1,
                        username
                );

                notificationStatement.setString(
                        2,
                        notificationMessage
                );

                notificationStatement.executeUpdate();
            }

            connection.commit();

            // Remove from history immediately
            historyModel.removeRow(modelRow);

            // Refresh active Pending queue
            loadClaims();

            showMessage(
                    "Claim Reopened",
                    "The claim has been reopened successfully.\n\n"
                            + "• Claim status: Pending\n"
                            + "• Returned to active verification queue\n"
                            + "• Student notification: Created",
                    GREEN
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            if (connection != null) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            showMessage(
                    "Reopen Failed",
                    "The claim could not be reopened.\n\n"
                            + ex.getMessage(),
                    RED
            );

        } finally {

            if (connection != null) {

                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeError) {
                    closeError.printStackTrace();
                }
            }
        }
    }

    // ==============================
    // UPDATE CLAIM STATUS
    // ==============================
    private void updateClaimStatus(
            String newStatus
    ) {

        int selectedRow =
                claimsTable.getSelectedRow();

        if (selectedRow == -1) {

            showMessage(
                    "Select a Claim",
                    "Please select a claim before "
                            + "approving or rejecting it.",
                    ORANGE
            );

            return;
        }

        int claimId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        int reportId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                1
                        ).toString()
                );

        String username =
                tableModel.getValueAt(
                        selectedRow,
                        2
                ).toString();

        String itemName =
                tableModel.getValueAt(
                        selectedRow,
                        3
                ).toString();

        String currentStatus =
                tableModel.getValueAt(
                        selectedRow,
                        6
                ).toString();

        // ==============================
        // PREVENT DUPLICATE PROCESSING
        // ==============================
        if ("Approved".equalsIgnoreCase(
                currentStatus
        ) || "Rejected".equalsIgnoreCase(
                currentStatus
        )) {

            showMessage(
                    "Claim Already Processed",
                    "This claim is already marked as "
                            + currentStatus + ".",
                    ORANGE
            );

            return;
        }

        // ==============================
        // CONFIRMATION
        // ==============================
        String message;

        if ("Approved".equalsIgnoreCase(
                newStatus
        )) {

            message =
                    "Approve this claim?\n\n"
                            + "Student: " + username + "\n"
                            + "Item: " + itemName + "\n"
                            + "Report ID: " + reportId
                            + "\n\n"
                            + "The report will be marked "
                            + "as Returned.";

        } else {

            message =
                    "Reject this claim?\n\n"
                            + "Student: " + username + "\n"
                            + "Item: " + itemName + "\n"
                            + "Report ID: " + reportId;
        }

        String heading =
                "Approved".equalsIgnoreCase(newStatus)
                        ? "Approve this claim?"
                        : "Reject this claim?";

        String confirmationMessage =
                message.replace(
                        "Approve this claim?\\n\\n",
                        ""
                ).replace(
                        "Reject this claim?\\n\\n",
                        ""
                );

        boolean confirmed =
                showGalaxyConfirmation(
                        newStatus + " Claim",
                        heading,
                        confirmationMessage,
                        "Approved".equalsIgnoreCase(newStatus)
                                ? "Approve Claim"
                                : "Reject Claim",
                        "Approved".equalsIgnoreCase(newStatus)
                                ? GREEN
                                : RED
                );

        if (!confirmed) {
            return;
        }

        // ==============================
        // DATABASE TRANSACTION
        // ==============================
        String getReportStatusSQL =
                "SELECT status " +
                        "FROM reports " +
                        "WHERE id = ? " +
                        "FOR UPDATE";

        String updateClaimSQL =
                "UPDATE claims " +
                        "SET status = ?, " +
                        "previous_report_status = ? " +
                        "WHERE id = ?";

        String updateClaimStatusOnlySQL =
                "UPDATE claims " +
                        "SET status = ? " +
                        "WHERE id = ?";

        String updateReportSQL =
                "UPDATE reports " +
                        "SET status = 'Returned' " +
                        "WHERE id = ?";

        String notificationSQL =
                "INSERT INTO notifications "
                        + "(username, message, status) "
                        + "VALUES (?, ?, 'UNREAD')";

        Connection connection = null;

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USER,
                            DB_PASSWORD
                    );

            // Start transaction
            connection.setAutoCommit(false);

            // ==============================
            // 1. IF APPROVED -> SAVE PREVIOUS
            //    REPORT STATUS
            // ==============================
            String previousReportStatus = null;

            if ("Approved".equalsIgnoreCase(
                    newStatus
            )) {

                try (
                        PreparedStatement statusStatement =
                                connection.prepareStatement(
                                        getReportStatusSQL
                                )
                ) {

                    statusStatement.setInt(
                            1,
                            reportId
                    );

                    try (
                            ResultSet statusResult =
                                    statusStatement.executeQuery()
                    ) {

                        if (!statusResult.next()) {
                            throw new SQLException(
                                    "Report not found for Report ID "
                                            + reportId
                            );
                        }

                        previousReportStatus =
                                statusResult.getString(
                                        "status"
                                );
                    }
                }

                // Save the claim decision and the report's
                // previous status in the same transaction.
                try (
                        PreparedStatement claimStatement =
                                connection.prepareStatement(
                                        updateClaimSQL
                                )
                ) {

                    claimStatement.setString(
                            1,
                            newStatus
                    );

                    claimStatement.setString(
                            2,
                            previousReportStatus
                    );

                    claimStatement.setInt(
                            3,
                            claimId
                    );

                    claimStatement.executeUpdate();
                }

            } else {

                // Rejection does not change the report status.
                try (
                        PreparedStatement claimStatement =
                                connection.prepareStatement(
                                        updateClaimStatusOnlySQL
                                )
                ) {

                    claimStatement.setString(
                            1,
                            newStatus
                    );

                    claimStatement.setInt(
                            2,
                            claimId
                    );

                    claimStatement.executeUpdate();
                }
            }

            // ==============================
            // 2. IF APPROVED -> RETURNED
            // ==============================
            if ("Approved".equalsIgnoreCase(
                    newStatus
            )) {

                try (
                        PreparedStatement reportStatement =
                                connection.prepareStatement(
                                        updateReportSQL
                                )
                ) {

                    reportStatement.setInt(
                            1,
                            reportId
                    );

                    reportStatement.executeUpdate();
                }
            }

            // ==============================
            // 3. CREATE NOTIFICATION
            // ==============================
            String notificationMessage;

            if ("Approved".equalsIgnoreCase(
                    newStatus
            )) {

                notificationMessage =
                        "Your claim for item '"
                                + itemName
                                + "' (Report ID "
                                + reportId
                                + ") has been approved. "
                                + "The item has been marked as Returned.";

            } else {

                notificationMessage =
                        "Your claim for item '"
                                + itemName
                                + "' (Report ID "
                                + reportId
                                + ") has been rejected. "
                                + "Please contact the Lost & Found administrator "
                                + "for more information.";
            }

            try (
                    PreparedStatement notificationStatement =
                            connection.prepareStatement(
                                    notificationSQL
                            )
            ) {

                notificationStatement.setString(
                        1,
                        username
                );

                notificationStatement.setString(
                        2,
                        notificationMessage
                );

                notificationStatement.executeUpdate();
            }

            // ==============================
            // COMMIT EVERYTHING
            // ==============================
            connection.commit();

            // ==============================
            // SUCCESS
            // ==============================
            if ("Approved".equalsIgnoreCase(
                    newStatus
            )) {

                showMessage(
                        "Claim Approved",
                        "The claim has been approved successfully.\n\n"
                                + "• Claim status: Approved\n"
                                + "• Report status: Returned\n"
                                + "• Student notification: Created",
                        GREEN
                );

            } else {

                showMessage(
                        "Claim Rejected",
                        "The claim has been rejected successfully.\n\n"
                                + "• Claim status: Rejected\n"
                                + "• Student notification: Created",
                        RED
                );
            }

            // Refresh table
            loadClaims();

        } catch (Exception ex) {

            ex.printStackTrace();

            // ==============================
            // ROLLBACK
            // ==============================
            if (connection != null) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            showMessage(
                    "Operation Failed",
                    "The claim could not be processed.\n\n"
                            + ex.getMessage(),
                    RED
            );

        } finally {

            if (connection != null) {

                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeError) {
                    closeError.printStackTrace();
                }
            }
        }
    }

    // ==============================
    // SHOW CLAIM DETAILS
    // ==============================
    private void showClaimDetails(
            int row
    ) {

        String claimId =
                tableModel.getValueAt(
                        row,
                        0
                ).toString();

        String reportId =
                tableModel.getValueAt(
                        row,
                        1
                ).toString();

        String username =
                tableModel.getValueAt(
                        row,
                        2
                ).toString();

        String item =
                tableModel.getValueAt(
                        row,
                        3
                ).toString();

        String location =
                tableModel.getValueAt(
                        row,
                        4
                ).toString();

        String answer =
                tableModel.getValueAt(
                        row,
                        5
                ).toString();

        String aiScore =
                tableModel.getValueAt(
                        row,
                        6
                ).toString();

        String aiConfidence =
                tableModel.getValueAt(
                        row,
                        7
                ).toString();

        String status =
                tableModel.getValueAt(
                        row,
                        8
                ).toString();

        String details =
                "Claim ID: " + claimId
                        + "\nReport ID: " + reportId
                        + "\nUsername: " + username
                        + "\nItem: " + item
                        + "\nLocation: " + location
                        + "\n\nOwnership Details:\n"
                        + answer
                        + "\n\nAI Ownership Verification:"
                        + "\nSimilarity Score: " + aiScore
                        + "\nConfidence: " + aiConfidence
                        + "\n\nStatus: " + status;

        showMessage(
                "Claim Details",
                details,
                BLUE
        );
    }

    // ==============================
    // GALAXY CONFIRMATION DIALOG
    // ==============================
    private boolean showGalaxyConfirmation(
            String title,
            String heading,
            String message,
            String confirmText,
            Color confirmColor
    ) {

        final boolean[] confirmed = {false};

        JDialog dialog =
                new JDialog(
                        this,
                        title,
                        true
                );

        dialog.setSize(560, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel main =
                new JPanel(new BorderLayout(0, 12));

        main.setBackground(BG);
        main.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        18,
                        25
                )
        );

        JPanel header =
                new JPanel(new BorderLayout(12, 0));

        header.setOpaque(false);

        JLabel icon =
                new JLabel("⚠");

        icon.setForeground(confirmColor);
        icon.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        30
                )
        );

        JLabel titleLabel =
                new JLabel(heading);

        titleLabel.setForeground(WHITE);
        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        header.add(icon, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);

        JPanel card =
                new JPanel(new BorderLayout());

        card.setBackground(PANEL);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(60, 70, 110)
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        JTextArea textArea =
                new JTextArea(message);

        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(WHITE);
        textArea.setBackground(PANEL);
        textArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        card.add(
                textArea,
                BorderLayout.CENTER
        );

        JButton noButton =
                createButton(
                        "Cancel",
                        new Color(85, 95, 125)
                );

        JButton yesButton =
                createButton(
                        confirmText,
                        confirmColor
                );

        noButton.addActionListener(
                e -> dialog.dispose()
        );

        yesButton.addActionListener(
                e -> {
                    confirmed[0] = true;
                    dialog.dispose();
                }
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);
        buttonPanel.add(noButton);
        buttonPanel.add(yesButton);

        main.add(
                header,
                BorderLayout.NORTH
        );

        main.add(
                card,
                BorderLayout.CENTER
        );

        main.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(main);
        dialog.getRootPane().setDefaultButton(
                yesButton
        );

        dialog.setVisible(true);

        return confirmed[0];
    }

    // ==============================
    // MODERN MESSAGE
    // ==============================
    private void showMessage(
            String title,
            String message,
            Color accent
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        title,
                        true
                );

        dialog.setSize(470, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel main =
                new JPanel(new BorderLayout());

        main.setBackground(PANEL);
        main.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        // ==============================
        // TOP
        // ==============================
        JPanel top =
                new JPanel(new BorderLayout());

        top.setOpaque(false);

        JLabel icon =
                new JLabel("●");

        icon.setForeground(accent);
        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        JLabel heading =
                new JLabel(title);

        heading.setForeground(WHITE);
        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        top.add(
                icon,
                BorderLayout.WEST
        );

        top.add(
                heading,
                BorderLayout.CENTER
        );

        // ==============================
        // MESSAGE
        // ==============================
        JTextArea textArea =
                new JTextArea(message);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setForeground(WHITE);
        textArea.setBackground(PANEL);
        textArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        textArea.setBorder(
                new EmptyBorder(
                        15,
                        5,
                        15,
                        5
                )
        );

        // ==============================
        // BUTTON
        // ==============================
        JButton okButton =
                createButton(
                        "OK",
                        accent
                );

        okButton.addActionListener(
                e -> dialog.dispose()
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttonPanel.setOpaque(false);

        buttonPanel.add(okButton);

        main.add(
                top,
                BorderLayout.NORTH
        );

        main.add(
                textArea,
                BorderLayout.CENTER
        );

        main.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(main);
        dialog.setVisible(true);
    }

    // ==============================
    // STATUS RENDERER
    // ==============================
    private static class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            Component component =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            String status =
                    value == null
                            ? ""
                            : value.toString();

            if (isSelected) {

                component.setForeground(
                        Color.WHITE
                );

            } else if ("Approved".equalsIgnoreCase(
                    status
            )) {

                component.setForeground(
                        GREEN
                );

            } else if ("Rejected".equalsIgnoreCase(
                    status
            )) {

                component.setForeground(
                        RED
                );

            } else {

                component.setForeground(
                        ORANGE
                );
            }

            return component;
        }
    }

    // ==============================
    // MAIN
    // ==============================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AdminClaimsFrame frame =
                    new AdminClaimsFrame();

            frame.setVisible(true);
        });
    }
}