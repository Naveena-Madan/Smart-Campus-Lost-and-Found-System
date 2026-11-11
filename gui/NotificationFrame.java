package gui;

import service.SmartMatchService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.sql.*;


/**
 * Smart Campus Lost & Found System
 *
 * NotificationFrame
 *
 * Displays notifications belonging to the
 * currently logged-in student.
 */
public class NotificationFrame extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USER = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;


    // =========================================================
    // USER INFORMATION
    // =========================================================

    private final String username;
    private final String fullName;


    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG_COLOR =
            new Color(8, 12, 28);

    private static final Color HEADER_COLOR =
            new Color(10, 15, 32);

    private static final Color CARD_COLOR =
            new Color(20, 27, 52);

    private static final Color CARD_HOVER =
            new Color(30, 40, 72);

    private static final Color ACCENT =
            new Color(90, 120, 255);

    private static final Color ACCENT_BRIGHT =
            new Color(75, 145, 255);

    private static final Color TEXT_PRIMARY =
            new Color(245, 247, 255);

    private static final Color TEXT_SECONDARY =
            new Color(170, 180, 205);

    private static final Color BORDER =
            new Color(55, 65, 100);

    private static final Color UNREAD_COLOR =
            new Color(90, 120, 255);


    // =========================================================
    // MAIN PANEL
    // =========================================================

    private JPanel notificationPanel;

    private JScrollPane scrollPane;


    // =========================================================
    // CONSTRUCTOR 1
    // =========================================================

    public NotificationFrame(String username) {

        this.username = username;

        this.fullName = username;

        initializeFrame();

        initializeUI();

        loadNotifications();
    }


    // =========================================================
    // CONSTRUCTOR 2
    // =========================================================

    public NotificationFrame(
            String fullName,
            String username
    ) {

        this.username = username;

        this.fullName =
                (fullName == null ||
                        fullName.trim().isEmpty())
                        ? username
                        : fullName;

        initializeFrame();

        initializeUI();

        loadNotifications();
    }


    // =========================================================
    // FRAME SETTINGS
    // =========================================================

    private void initializeFrame() {

        setTitle(
                "Smart Campus - Notifications"
        );

        setSize(
                1000,
                700
        );

        setMinimumSize(
                new Dimension(
                        850,
                        600
                )
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // Screen-size change only: open this frame maximized.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                BG_COLOR
        );
    }


    // =========================================================
    // INITIALIZE UI
    // =========================================================

    private void initializeUI() {

        createHeader();

        createNotificationArea();

        createBottomBar();
    }


    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                HEADER_COLOR
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        115
                )
        );

        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );


        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setOpaque(false);

        left.setBorder(
                new EmptyBorder(
                        20,
                        45,
                        15,
                        10
                )
        );


        JLabel title =
                new JLabel(
                        "🔔  Notifications"
                );

        title.setForeground(
                TEXT_PRIMARY
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );


        JLabel subtitle =
                new JLabel(
                        "Stay updated with your Lost & Found activity"
                );

        subtitle.setForeground(
                TEXT_SECONDARY
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        left.add(title);

        left.add(
                Box.createVerticalStrut(6)
        );

        left.add(subtitle);


        header.add(
                left,
                BorderLayout.WEST
        );


        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JLabel userLabel =
                new JLabel(
                        "Welcome, " + fullName
                );

        userLabel.setForeground(
                TEXT_PRIMARY
        );

        userLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        userLabel.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        0,
                        40
                )
        );


        header.add(
                userLabel,
                BorderLayout.EAST
        );


        add(
                header,
                BorderLayout.NORTH
        );
    }


    // =========================================================
    // NOTIFICATION AREA
    // =========================================================

    private void createNotificationArea() {

        notificationPanel =
                new JPanel();

        notificationPanel.setLayout(
                new BoxLayout(
                        notificationPanel,
                        BoxLayout.Y_AXIS
                )
        );

        notificationPanel.setBackground(
                BG_COLOR
        );

        notificationPanel.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );


        // =====================================================
        // SCROLL PANE
        // =====================================================

        scrollPane =
                new JScrollPane(
                        notificationPanel
                );


        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setBackground(
                BG_COLOR
        );

        scrollPane.getViewport()
                .setBackground(
                        BG_COLOR
                );


        // =====================================================
        // REMOVE OLD HORIZONTAL SCROLLBAR
        // =====================================================

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );


        // =====================================================
        // MODERN VERTICAL SCROLLBAR
        // =====================================================

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );


        JScrollBar verticalBar =
                scrollPane.getVerticalScrollBar();

        verticalBar.setPreferredSize(
                new Dimension(
                        10,
                        0
                )
        );

        verticalBar.setBackground(
                new Color(12, 17, 35)
        );

        verticalBar.setUI(
                new ModernScrollBarUI()
        );


        // =====================================================
        // ADD TO FRAME
        // =====================================================

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // BOTTOM BAR
    // =========================================================

    private void createBottomBar() {

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                15
                        )
                );

        bottom.setBackground(
                HEADER_COLOR
        );

        bottom.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDER
                )
        );


        // =====================================================
        // REFRESH
        // =====================================================

        JButton refreshButton =
                createBottomButton(
                        "↻  Refresh",
                        CARD_COLOR
                );

        refreshButton.addActionListener(
                e -> loadNotifications()
        );


        // =====================================================
        // MARK ALL
        // =====================================================

        JButton markAllButton =
                createBottomButton(
                        "✓  Mark All as Read",
                        CARD_COLOR
                );

        markAllButton.addActionListener(
                e -> markAllAsRead()
        );


        // =====================================================
        // BACK
        // =====================================================

        JButton backButton =
                createBottomButton(
                        "←  Back",
                        CARD_COLOR
                );

        backButton.addActionListener(
                e -> goBack()
        );


        bottom.add(
                refreshButton
        );

        bottom.add(
                markAllButton
        );

        bottom.add(
                backButton
        );


        add(
                bottom,
                BorderLayout.SOUTH
        );
    }


    // =========================================================
    // LOAD NOTIFICATIONS
    // =========================================================

    private void loadNotifications() {

        notificationPanel.removeAll();


        String sql =
                "SELECT id, report_id, message, status, created_at " +
                        "FROM notifications " +
                        "WHERE username = ? " +
                        "ORDER BY created_at DESC";


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
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setString(
                        1,
                        username
                );


                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    boolean found =
                            false;


                    while (
                            resultSet.next()
                    ) {

                        found = true;


                        int id =
                                resultSet.getInt(
                                        "id"
                                );


                        int reportId =
                                resultSet.getInt(
                                        "report_id"
                                );


                        String message =
                                resultSet.getString(
                                        "message"
                                );


                        String status =
                                resultSet.getString(
                                        "status"
                                );


                        Timestamp timestamp =
                                resultSet.getTimestamp(
                                        "created_at"
                                );


                        JPanel card =
                                createNotificationCard(
                                        id,
                                        reportId,
                                        message,
                                        status,
                                        timestamp
                                );


                        notificationPanel.add(
                                card
                        );


                        notificationPanel.add(
                                Box.createVerticalStrut(
                                        12
                                )
                        );
                    }


                    if (!found) {

                        showEmptyMessage();
                    }
                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();

            showDatabaseError();
        }


        notificationPanel.revalidate();

        notificationPanel.repaint();


        // Always return to top after refresh
        SwingUtilities.invokeLater(
                () -> scrollPane
                        .getVerticalScrollBar()
                        .setValue(0)
        );
    }


    // =========================================================
    // NOTIFICATION CARD
    // =========================================================

    private JPanel createNotificationCard(
            int notificationId,
            int reportId,
            String message,
            String status,
            Timestamp timestamp
    ) {

        boolean unread =
                status != null &&
                        status.equalsIgnoreCase(
                                "UNREAD"
                        );


        JPanel card =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );


        card.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );


        card.setBackground(
                CARD_COLOR
        );


        // =====================================================
        // BORDER
        // =====================================================

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                unread
                                        ? UNREAD_COLOR
                                        : BORDER,
                                unread ? 2 : 1
                        ),
                        new EmptyBorder(
                                16,
                                20,
                                16,
                                20
                        )
                )
        );


        // =====================================================
        // ICON
        // =====================================================

        JLabel icon =
                new JLabel(
                        unread
                                ? "🔔"
                                : "✓"
                );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        30
                )
        );


        icon.setPreferredSize(
                new Dimension(
                        45,
                        45
                )
        );


        card.add(
                icon,
                BorderLayout.WEST
        );


        // =====================================================
        // TEXT PANEL
        // =====================================================

        JPanel textPanel =
                new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setOpaque(false);


        // =====================================================
        // STATUS
        // =====================================================

        JLabel statusLabel =
                new JLabel(
                        unread
                                ? "NEW NOTIFICATION"
                                : "READ"
                );


        statusLabel.setForeground(
                unread
                        ? UNREAD_COLOR
                        : TEXT_SECONDARY
        );


        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );


        // =====================================================
        // MESSAGE
        // =====================================================

        JLabel messageLabel =
                new JLabel(
                        "<html>"
                                + escapeHtml(
                                message
                        )
                                + "</html>"
                );


        messageLabel.setForeground(
                TEXT_PRIMARY
        );


        messageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );


        // =====================================================
        // TIME
        // =====================================================

        String timeText =
                timestamp == null
                        ? ""
                        : timestamp.toString();


        JLabel timeLabel =
                new JLabel(
                        timeText
                );


        timeLabel.setForeground(
                TEXT_SECONDARY
        );


        timeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );


        // =====================================================
        // ADD TEXT
        // =====================================================

        textPanel.add(
                statusLabel
        );


        textPanel.add(
                Box.createVerticalStrut(6)
        );


        textPanel.add(
                messageLabel
        );


        textPanel.add(
                Box.createVerticalStrut(6)
        );


        textPanel.add(
                timeLabel
        );


        card.add(
                textPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new GridBagLayout()
                );

        buttonPanel.setOpaque(false);

        JPanel buttonColumn =
                new JPanel();

        buttonColumn.setLayout(
                new BoxLayout(
                        buttonColumn,
                        BoxLayout.Y_AXIS
                )
        );

        buttonColumn.setOpaque(false);

        if (reportId > 0) {

            JButton viewMatchButton =
                    createSmallButton(
                            "View Match"
                    );

            viewMatchButton.addActionListener(
                    e ->
                            openMatchFromNotification(
                                    reportId
                            )
            );

            buttonColumn.add(
                    viewMatchButton
            );
        }

        if (unread) {

            if (reportId > 0) {
                buttonColumn.add(
                        Box.createVerticalStrut(8)
                );
            }

            JButton readButton =
                    createSmallButton(
                            "Mark as Read"
                    );

            readButton.addActionListener(
                    e ->
                            markAsRead(
                                    notificationId
                            )
            );

            buttonColumn.add(
                    readButton
            );
        }

        if (buttonColumn.getComponentCount() > 0) {

            buttonPanel.add(
                    buttonColumn
            );

            card.add(
                    buttonPanel,
                    BorderLayout.EAST
            );
        }


        // =====================================================
        // HOVER EFFECT
        // =====================================================

        card.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                CARD_HOVER
                        );
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                CARD_COLOR
                        );
                    }
                }
        );


        return card;
    }


    // =========================================================
    // OPEN MATCH FROM NOTIFICATION
    // =========================================================

    private void openMatchFromNotification(
            int foundReportId
    ) {

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            String sql =
                    "SELECT id, item_name, location, username " +
                            "FROM reports " +
                            "WHERE id = ? " +
                            "AND report_type = 'FOUND'";

            String foundItemName = null;
            String foundLocation = null;

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USER,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setInt(
                        1,
                        foundReportId
                );

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (resultSet.next()) {

                        foundItemName =
                                resultSet.getString(
                                        "item_name"
                                );

                        foundLocation =
                                resultSet.getString(
                                        "location"
                                );
                    }
                }
            }

            if (foundItemName == null) {

                showModernDialog(
                        "Match Unavailable",
                        "The found-item report is no longer available.",
                        new Color(
                                245,
                                90,
                                100
                        )
                );

                return;
            }

            SmartMatchService service =
                    new SmartMatchService();

            java.util.List<String> bestMatches =
                    new java.util.ArrayList<>();

            String lostSql =
                    "SELECT item_name, location " +
                            "FROM reports " +
                            "WHERE username = ? " +
                            "AND report_type = 'LOST' " +
                            "AND (status IS NULL OR status <> 'Returned') " +
                            "ORDER BY id DESC";

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USER,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    lostSql
                            )
            ) {

                statement.setString(
                        1,
                        username
                );

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    while (resultSet.next()) {

                        String candidateItem =
                                resultSet.getString(
                                        "item_name"
                                );

                        String candidateLocation =
                                resultSet.getString(
                                        "location"
                                );

                        java.util.List<String> matches =
                                service.findMatches(
                                        candidateItem,
                                        candidateLocation
                                );

                        for (String match : matches) {

                            if (match != null
                                    && match.contains(
                                    "Report ID: " + foundReportId
                            )) {

                                bestMatches.clear();
                                bestMatches.add(match);
                                break;
                            }
                        }

                        if (!bestMatches.isEmpty()) {
                            break;
                        }
                    }
                }
            }

            if (bestMatches.isEmpty()) {

                showModernDialog(
                        "Match Not Available",
                        "This notification was created earlier, but the current Smart Match engine could not reproduce the match.",
                        ACCENT
                );

                return;
            }

            SmartMatchResultsDialog dialog =
                    new SmartMatchResultsDialog(
                            this,
                            bestMatches,
                            username
                    );

            dialog.setLocationRelativeTo(
                    this
            );

            dialog.setVisible(true);

        } catch (Exception ex) {

            ex.printStackTrace();

            showModernDialog(
                    "Match Error",
                    "Unable to open the matched found item.",
                    new Color(
                            245,
                            90,
                            100
                    )
            );
        }
    }


    // =========================================================
    // MARK ONE AS READ
    // =========================================================

    private void markAsRead(
            int notificationId
    ) {

        String sql =
                "UPDATE notifications " +
                        "SET status = 'READ' " +
                        "WHERE id = ? " +
                        "AND username = ?";


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
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setInt(
                        1,
                        notificationId
                );


                statement.setString(
                        2,
                        username
                );


                statement.executeUpdate();
            }


            loadNotifications();


        } catch (Exception ex) {

            ex.printStackTrace();

            showModernDialog(
                    "Notification Error",
                    "Unable to update notification.",
                    new Color(
                            245,
                            90,
                            100
                    )
            );
        }
    }


    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    private void markAllAsRead() {

        String sql =
                "UPDATE notifications " +
                        "SET status = 'READ' " +
                        "WHERE username = ? " +
                        "AND status = 'UNREAD'";


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
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setString(
                        1,
                        username
                );


                int updated =
                        statement.executeUpdate();


                loadNotifications();


                if (updated > 0) {

                    showModernDialog(
                            "Notifications Updated",
                            updated
                                    + " notification(s) "
                                    + "marked as read.",
                            ACCENT
                    );
                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();

            showModernDialog(
                    "Notification Error",
                    "Unable to update notifications.",
                    new Color(
                            245,
                            90,
                            100
                    )
            );
        }
    }


    // =========================================================
    // EMPTY MESSAGE
    // =========================================================

    private void showEmptyMessage() {

        JPanel emptyPanel =
                new JPanel();


        emptyPanel.setLayout(
                new BoxLayout(
                        emptyPanel,
                        BoxLayout.Y_AXIS
                )
        );


        emptyPanel.setBackground(
                BG_COLOR
        );


        emptyPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel icon =
                new JLabel(
                        "🔔"
                );


        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        55
                )
        );


        JLabel title =
                new JLabel(
                        "No Notifications"
                );


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        title.setForeground(
                TEXT_PRIMARY
        );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );


        JLabel message =
                new JLabel(
                        "You're all caught up!"
                );


        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        message.setForeground(
                TEXT_SECONDARY
        );


        message.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        emptyPanel.add(
                Box.createVerticalStrut(
                        100
                )
        );


        emptyPanel.add(
                icon
        );


        emptyPanel.add(
                Box.createVerticalStrut(
                        15
                )
        );


        emptyPanel.add(
                title
        );


        emptyPanel.add(
                Box.createVerticalStrut(
                        7
                )
        );


        emptyPanel.add(
                message
        );


        notificationPanel.add(
                emptyPanel
        );
    }


    // =========================================================
    // DATABASE ERROR
    // =========================================================

    private void showDatabaseError() {

        JPanel panel =
                new JPanel();


        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );


        panel.setBackground(
                BG_COLOR
        );


        JLabel icon =
                new JLabel(
                        "⚠"
                );


        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        50
                )
        );


        JLabel title =
                new JLabel(
                        "Unable to load notifications"
                );


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        title.setForeground(
                TEXT_PRIMARY
        );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );


        JLabel message =
                new JLabel(
                        "Please check your database connection."
                );


        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        message.setForeground(
                TEXT_SECONDARY
        );


        panel.add(
                Box.createVerticalStrut(
                        100
                )
        );


        panel.add(
                icon
        );


        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );


        panel.add(
                title
        );


        panel.add(
                Box.createVerticalStrut(
                        7
                )
        );


        panel.add(
                message
        );


        notificationPanel.add(
                panel
        );
    }


    // =========================================================
    // BOTTOM BUTTON
    // =========================================================

    private JButton createBottomButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setForeground(
                TEXT_PRIMARY
        );


        button.setBackground(
                background
        );


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setContentAreaFilled(true);

        button.setOpaque(true);


        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                18,
                                10,
                                18
                        )
                )
        );


        // =====================================================
        // HOVER
        // =====================================================

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                ACCENT
                        );
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                background
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // SMALL BUTTON
    // =========================================================

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setForeground(
                Color.WHITE
        );


        button.setBackground(
                ACCENT
        );


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );


        button.setFocusPainted(false);

        button.setBorderPainted(false);


        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        button.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );


        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                ACCENT_BRIGHT
                        );
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                ACCENT
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // MODERN SCROLLBAR UI
    // =========================================================

    private static class ModernScrollBarUI
            extends BasicScrollBarUI {

        private final Color TRACK =
                new Color(
                        10,
                        15,
                        32
                );

        private final Color THUMB =
                new Color(
                        65,
                        85,
                        145
                );

        private final Color THUMB_HOVER =
                new Color(
                        90,
                        120,
                        255
                );


        @Override
        protected void configureScrollBarColors() {

            trackColor = TRACK;

            thumbColor = THUMB;
        }


        @Override
        protected JButton createDecreaseButton(
                int orientation
        ) {

            return createZeroButton();
        }


        @Override
        protected JButton createIncreaseButton(
                int orientation
        ) {

            return createZeroButton();
        }


        private JButton createZeroButton() {

            JButton button =
                    new JButton();

            button.setPreferredSize(
                    new Dimension(
                            0,
                            0
                    )
            );

            button.setMinimumSize(
                    new Dimension(
                            0,
                            0
                    )
            );

            button.setMaximumSize(
                    new Dimension(
                            0,
                            0
                    )
            );

            button.setBorderPainted(false);

            button.setContentAreaFilled(false);

            button.setOpaque(false);

            return button;
        }


        @Override
        protected void paintTrack(
                Graphics g,
                JComponent c,
                Rectangle trackBounds
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setColor(
                    TRACK
            );


            g2.fillRect(
                    trackBounds.x,
                    trackBounds.y,
                    trackBounds.width,
                    trackBounds.height
            );


            g2.dispose();
        }


        @Override
        protected void paintThumb(
                Graphics g,
                JComponent c,
                Rectangle thumbBounds
        ) {

            if (thumbBounds.isEmpty() ||
                    !scrollbar.isEnabled()) {

                return;
            }


            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            Color currentColor =
                    isThumbRollover()
                            ? THUMB_HOVER
                            : THUMB;


            g2.setColor(
                    currentColor
            );


            g2.fillRoundRect(
                    thumbBounds.x + 2,
                    thumbBounds.y + 2,
                    thumbBounds.width - 4,
                    thumbBounds.height - 4,
                    8,
                    8
            );


            g2.dispose();
        }
    }


    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHtml(
            String text
    ) {

        if (text == null) {

            return "";
        }


        return text
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                );
    }


    // =========================================================
    // MODERN MESSAGE
    // =========================================================

    private void showModernDialog(
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


        dialog.setSize(
                430,
                220
        );


        dialog.setLocationRelativeTo(
                this
        );


        dialog.setResizable(false);


        JPanel main =
                new JPanel(
                        new BorderLayout()
                );


        main.setBackground(
                CARD_COLOR
        );


        main.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );


        JLabel heading =
                new JLabel(
                        title
                );


        heading.setForeground(
                TEXT_PRIMARY
        );


        heading.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );


        JTextArea messageArea =
                new JTextArea(
                        message
                );


        messageArea.setEditable(false);

        messageArea.setLineWrap(true);

        messageArea.setWrapStyleWord(true);

        messageArea.setForeground(
                TEXT_SECONDARY
        );

        messageArea.setBackground(
                CARD_COLOR
        );

        messageArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        JButton okButton =
                createSmallButton(
                        "OK"
                );


        okButton.setBackground(
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


        buttonPanel.add(
                okButton
        );


        main.add(
                heading,
                BorderLayout.NORTH
        );


        main.add(
                messageArea,
                BorderLayout.CENTER
        );


        main.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        dialog.setContentPane(
                main
        );


        dialog.setVisible(true);
    }


    // =========================================================
    // BACK TO DASHBOARD
    // =========================================================

    private void goBack() {

        dispose();


        StudentDashboard dashboard =
                new StudentDashboard(
                        fullName,
                        username
                );


        dashboard.setLocationRelativeTo(
                null
        );


        dashboard.setVisible(
                true
        );
    }


    // =========================================================
    // TEST MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }


                    NotificationFrame frame =
                            new NotificationFrame(
                                    "teststudent"
                            );


                    frame.setVisible(
                            true
                    );
                }
        );
    }
}
