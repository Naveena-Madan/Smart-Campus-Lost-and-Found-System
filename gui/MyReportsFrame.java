package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class MyReportsFrame extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USERNAME = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    // =========================================================
    // USER
    // =========================================================

    private final String loggedInUsername;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color DARK_BG =
            new Color(7, 12, 38);

    private static final Color CARD_BG =
            new Color(15, 25, 58);

    private static final Color BLUE =
            new Color(70, 115, 255);

    private static final Color CYAN =
            new Color(50, 210, 255);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color LIGHT_TEXT =
            new Color(220, 235, 255);

    private static final Color SECONDARY_TEXT =
            new Color(175, 195, 230);

    private static final Color BORDER =
            new Color(60, 90, 150);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MyReportsFrame(
            String username
    ) {

        loggedInUsername =
                username;

        setTitle(
                "Smart Campus - My Reports"
        );

        setSize(
                1100,
                750
        );

        setMinimumSize(
                new Dimension(
                        900,
                        650
                )
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(
                null
        );

        // Open this window maximized for a full-screen dashboard experience.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(
                new BorderLayout()
        );

        initializeUI();

        loadReports();
    }

    // =========================================================
    // INITIALIZE UI
    // =========================================================

    private void initializeUI() {

        GalaxyPanel background =
                new GalaxyPanel();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        30,
                        40,
                        20,
                        40
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "📦  My Reports"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "View all the Lost & Found reports you submitted"
                );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        titlePanel.add(
                subtitle
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JLabel usernameLabel =
                new JLabel(
                        "User: "
                                + loggedInUsername
                );

        usernameLabel.setForeground(
                LIGHT_TEXT
        );

        usernameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        header.add(
                usernameLabel,
                BorderLayout.EAST
        );

        background.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel reportsPanel =
                new JPanel();

        reportsPanel.setOpaque(false);

        reportsPanel.setLayout(
                new BoxLayout(
                        reportsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        reportsPanel.setBorder(
                new EmptyBorder(
                        10,
                        40,
                        30,
                        40
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        reportsPanel
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setOpaque(false);

        scrollPane.getViewport()
                .setOpaque(false);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setPreferredSize(
                        new Dimension(
                                10,
                                0
                        )
                );

        scrollPane.getVerticalScrollBar()
                .setUI(
                        new ModernScrollBarUI()
                );

        background.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                15,
                                15
                        )
                );

        bottom.setOpaque(false);

        JButton refreshButton =
                createButton(
                        "⟳  Refresh",
                        BLUE
                );

        refreshButton.addActionListener(
                e -> loadReports()
        );

        JButton backButton =
                createButton(
                        "←  Back",
                        new Color(
                                35,
                                50,
                                90
                        )
                );

        backButton.addActionListener(
                e -> goBack()
        );

        bottom.add(
                refreshButton
        );

        bottom.add(
                backButton
        );

        background.add(
                bottom,
                BorderLayout.SOUTH
        );

        add(
                background,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // LOAD REPORTS
    // =========================================================

    private void loadReports() {

        Container root =
                getContentPane();

        JScrollPane scrollPane =
                findScrollPane(
                        root
                );

        if (
                scrollPane == null
        ) {

            return;
        }

        JViewport viewport =
                scrollPane.getViewport();

        if (
                viewport == null
        ) {

            return;
        }

        Component component =
                viewport.getView();

        if (
                !(component instanceof JPanel)
        ) {

            return;
        }

        JPanel reportsPanel =
                (JPanel) component;

        reportsPanel.removeAll();

        String sql =
                "SELECT id, item_name, location, "
                        + "report_type, status "
                        + "FROM reports "
                        + "WHERE username = ? "
                        + "ORDER BY id DESC";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    DB_URL,
                                    DB_USERNAME,
                                    DB_PASSWORD
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setString(
                        1,
                        loggedInUsername
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    int count = 0;

                    while (
                            result.next()
                    ) {

                        count++;

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

                        reportsPanel.add(
                                createReportCard(
                                        id,
                                        itemName,
                                        location,
                                        reportType,
                                        status
                                )
                        );

                        reportsPanel.add(
                                Box.createVerticalStrut(
                                        15
                                )
                        );
                    }

                    if (
                            count == 0
                    ) {

                        reportsPanel.add(
                                createEmptyPanel()
                        );
                    }
                }
            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();

            reportsPanel.add(
                    createErrorPanel(
                            "Unable to load your reports."
                    )
            );
        }

        reportsPanel.revalidate();

        reportsPanel.repaint();
    }

    // =========================================================
    // FIND SCROLL PANE
    // =========================================================

    private JScrollPane findScrollPane(
            Container container
    ) {

        for (
                Component component
                : container.getComponents()
        ) {

            if (
                    component instanceof JScrollPane
            ) {

                return (JScrollPane)
                        component;
            }

            if (
                    component instanceof Container
            ) {

                JScrollPane result =
                        findScrollPane(
                                (Container)
                                        component
                        );

                if (
                        result != null
                ) {

                    return result;
                }
            }
        }

        return null;
    }

    // =========================================================
    // REPORT CARD
    // =========================================================

    private JPanel createReportCard(
            int id,
            String itemName,
            String location,
            String reportType,
            String status
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                20,
                                10
                        )
                );

        card.setBackground(
                CARD_BG
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        // =====================================================
        // LEFT ICON
        // =====================================================

        JLabel icon =
                new JLabel(
                        reportType.equalsIgnoreCase(
                                "Lost"
                        )
                                ? "🔎"
                                : "📦"
                );

        icon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        42
                )
        );

        card.add(
                icon,
                BorderLayout.WEST
        );

        // =====================================================
        // DETAILS
        // =====================================================

        JPanel details =
                new JPanel();

        details.setOpaque(false);

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel item =
                new JLabel(
                        itemName
                );

        item.setForeground(
                WHITE
        );

        item.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        JLabel type =
                new JLabel(
                        reportType
                );

        type.setForeground(
                CYAN
        );

        type.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JLabel locationLabel =
                new JLabel(
                        "Location: "
                                + location
                );

        locationLabel.setForeground(
                LIGHT_TEXT
        );

        locationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel idLabel =
                new JLabel(
                        "Report ID: "
                                + id
                );

        idLabel.setForeground(
                SECONDARY_TEXT
        );

        idLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        details.add(
                item
        );

        details.add(
                Box.createVerticalStrut(
                        3
                )
        );

        details.add(
                type
        );

        details.add(
                Box.createVerticalStrut(
                        8
                )
        );

        details.add(
                locationLabel
        );

        details.add(
                Box.createVerticalStrut(
                        5
                )
        );

        details.add(
                idLabel
        );

        card.add(
                details,
                BorderLayout.CENTER
        );

        // =====================================================
        // STATUS
        // =====================================================

        JPanel statusPanel =
                new JPanel();

        statusPanel.setOpaque(false);

        statusPanel.setLayout(
                new BoxLayout(
                        statusPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel statusTitle =
                new JLabel(
                        "STATUS"
                );

        statusTitle.setForeground(
                SECONDARY_TEXT
        );

        statusTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        statusTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel statusLabel =
                new JLabel(
                        status == null
                                ? "Pending"
                                : status
                );

        statusLabel.setForeground(
                getStatusColor(
                        status
                )
        );

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        statusPanel.add(
                statusTitle
        );

        statusPanel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        statusPanel.add(
                statusLabel
        );

        card.add(
                statusPanel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // STATUS COLOR
    // =========================================================

    private Color getStatusColor(
            String status
    ) {

        if (
                status == null
        ) {

            return new Color(
                    255,
                    190,
                    70
            );
        }

        if (
                status.equalsIgnoreCase(
                        "Approved"
                )
                        ||
                        status.equalsIgnoreCase(
                                "Returned"
                        )
        ) {

            return new Color(
                    80,
                    220,
                    130
            );
        }

        if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            return new Color(
                    255,
                    90,
                    100
            );
        }

        return new Color(
                255,
                190,
                70
        );
    }

    // =========================================================
    // EMPTY PANEL
    // =========================================================

    private JPanel createEmptyPanel() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        100,
                        20,
                        100,
                        20
                )
        );

        JLabel icon =
                new JLabel(
                        "📦"
                );

        icon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        55
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "No Reports Yet"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message =
                new JLabel(
                        "You haven't submitted any Lost or Found reports."
                );

        message.setForeground(
                SECONDARY_TEXT
        );

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(icon);

        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        panel.add(message);

        return panel;
    }

    // =========================================================
    // ERROR PANEL
    // =========================================================

    private JPanel createErrorPanel(
            String message
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        100,
                        20,
                        100,
                        20
                )
        );

        JLabel icon =
                new JLabel(
                        "⚠"
                );

        icon.setForeground(
                new Color(
                        255,
                        190,
                        70
                )
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        50
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel text =
                new JLabel(
                        message
                );

        text.setForeground(
                WHITE
        );

        text.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        text.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(icon);

        panel.add(
                Box.createVerticalStrut(
                        15
                )
        );

        panel.add(text);

        return panel;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                background
        );

        button.setForeground(
                WHITE
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        return button;
    }

    // =========================================================
    // BACK
    // =========================================================

    private void goBack() {

        dispose();

        StudentDashboard dashboard =
                new StudentDashboard(
                        loggedInUsername,
                        loggedInUsername
                );

        dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);

        dashboard.setVisible(
                true
        );
    }

    // =========================================================
    // MODERN SCROLLBAR
    // =========================================================

    private static class ModernScrollBarUI
            extends javax.swing.plaf.basic.BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {

            thumbColor =
                    new Color(
                            70,
                            115,
                            255
                    );

            trackColor =
                    new Color(
                            8,
                            13,
                            32
                    );
        }

        @Override
        protected JButton createDecreaseButton(
                int orientation
        ) {

            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(
                int orientation
        ) {

            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {

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

            button.setOpaque(false);

            button.setContentAreaFilled(
                    false
            );

            button.setBorderPainted(
                    false
            );

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
                    new Color(
                            8,
                            13,
                            32,
                            220
                    )
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

            if (
                    thumbBounds.width <= 0
                            ||
                            thumbBounds.height <= 0
            ) {

                return;
            }

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    new Color(
                            70,
                            115,
                            255
                    )
            );

            g2.fillRoundRect(
                    thumbBounds.x + 2,
                    thumbBounds.y + 2,
                    Math.max(
                            1,
                            thumbBounds.width - 4
                    ),
                    Math.max(
                            1,
                            thumbBounds.height - 4
                    ),
                    10,
                    10
            );

            g2.dispose();
        }
    }

    // =========================================================
    // GALAXY BACKGROUND
    // =========================================================

    private static class GalaxyPanel
            extends JPanel {

        private final Star[] stars;

        GalaxyPanel() {

            setOpaque(true);

            stars =
                    new Star[90];

            java.util.Random random =
                    new java.util.Random(
                            2026
                    );

            for (
                    int i = 0;
                    i < stars.length;
                    i++
            ) {

                stars[i] =
                        new Star(
                                random.nextFloat(),
                                random.nextFloat(),
                                1 + random.nextInt(3),
                                80 + random.nextInt(176)
                        );
            }
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            GradientPaint background =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    7,
                                    12,
                                    38
                            ),
                            width,
                            height,
                            new Color(
                                    35,
                                    10,
                                    75
                            )
                    );

            g2.setPaint(
                    background
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            g2.setColor(
                    new Color(
                            50,
                            50,
                            180,
                            45
                    )
            );

            g2.fillOval(
                    -180,
                    height / 2 - 120,
                    600,
                    420
            );

            g2.setColor(
                    new Color(
                            120,
                            40,
                            200,
                            35
                    )
            );

            g2.fillOval(
                    width - 450,
                    height / 2 - 180,
                    650,
                    500
            );

            for (
                    Star star
                    : stars
            ) {

                int x =
                        (int)
                                (
                                        star.x
                                                * width
                                );

                int y =
                        (int)
                                (
                                        star.y
                                                * height
                                );

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                star.alpha
                        )
                );

                g2.fillOval(
                        x,
                        y,
                        star.size,
                        star.size
                );
            }

            g2.dispose();
        }
    }

    // =========================================================
    // STAR
    // =========================================================

    private static class Star {

        float x;
        float y;
        int size;
        int alpha;

        Star(
                float x,
                float y,
                int size,
                int alpha
        ) {

            this.x = x;
            this.y = y;
            this.size = size;
            this.alpha = alpha;
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    new MyReportsFrame(
                            "teststudent"
                    ).setVisible(
                            true
                    );
                }
        );
    }
}
