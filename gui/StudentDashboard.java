package gui;

import service.SmartMatchService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class StudentDashboard extends JFrame {

    // =========================================================
    // USER INFORMATION
    // =========================================================

    private final String loggedInFullName;
    private final String loggedInUsername;

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USERNAME = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color DARK_BG =
            new Color(7, 12, 38);

    private static final Color SIDEBAR_BG =
            new Color(10, 16, 40);

    private static final Color CARD_BG =
            new Color(15, 25, 58);

    private static final Color CARD_HOVER =
            new Color(25, 40, 82);

    private static final Color CYAN =
            new Color(50, 210, 255);

    private static final Color BLUE =
            new Color(70, 115, 255);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color LIGHT_TEXT =
            new Color(220, 235, 255);

    private static final Color SECONDARY_TEXT =
            new Color(175, 195, 230);

    private static final Color BORDER =
            new Color(60, 90, 150);

    // =========================================================
    // UI
    // =========================================================

    private JPanel sidebar;
    private JPanel contentPanel;

    private boolean sidebarExpanded = true;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public StudentDashboard(
            String fullName,
            String username
    ) {

        loggedInFullName =
                (fullName == null ||
                        fullName.trim().isEmpty())
                        ? username
                        : fullName;

        loggedInUsername = username;

        setTitle(
                "Smart Campus - Student Dashboard"
        );

        setSize(
                1350,
                850
        );

        setMinimumSize(
                new Dimension(
                        1100,
                        700
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setResizable(true);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setLayout(
                new BorderLayout()
        );

        initializeUI();
    }

    // =========================================================
    // INITIALIZE UI
    // =========================================================

    private void initializeUI() {

        createTopBar();

        createSidebar();

        createContentPanel();

        showHome();
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private void createTopBar() {

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );

        topBar.setPreferredSize(
                new Dimension(
                        0,
                        72
                )
        );

        topBar.setBackground(
                new Color(
                        8,
                        13,
                        32
                )
        );

        topBar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                13
                        )
                );

        left.setOpaque(false);

        JButton menuButton =
                new JButton("☰");

        menuButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        menuButton.setForeground(
                WHITE
        );

        menuButton.setBackground(
                new Color(
                        20,
                        30,
                        65
                )
        );

        menuButton.setFocusPainted(false);

        menuButton.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        12,
                        5,
                        12
                )
        );

        menuButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        menuButton.addActionListener(
                e -> toggleSidebar()
        );

        JLabel title =
                new JLabel(
                        "Smart Campus"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        left.add(menuButton);

        left.add(title);

        topBar.add(
                left,
                BorderLayout.WEST
        );

        JLabel welcome =
                new JLabel(
                        "Welcome, "
                                + loggedInFullName
                );

        welcome.setForeground(
                LIGHT_TEXT
        );

        welcome.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        welcome.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        0,
                        30
                )
        );

        topBar.add(
                welcome,
                BorderLayout.EAST
        );

        add(
                topBar,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private void createSidebar() {

        sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(
                        250,
                        0
                )
        );

        sidebar.setBackground(
                SIDEBAR_BG
        );

        sidebar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        0,
                        1,
                        BORDER
                )
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        JPanel header =
                new JPanel();

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBackground(
                SIDEBAR_BG
        );

        header.setBorder(
                new EmptyBorder(
                        25,
                        20,
                        20,
                        20
                )
        );

        JLabel robotIcon =
                new JLabel(
                        "🤖"
                );

        robotIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        38
                )
        );

        JLabel assistant =
                new JLabel(
                        "AI Assistant"
                );

        assistant.setForeground(
                WHITE
        );

        assistant.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        19
                )
        );

        JLabel helper =
                new JLabel(
                        "Campus Helper"
                );

        helper.setForeground(
                SECONDARY_TEXT
        );

        helper.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        header.add(robotIcon);

        header.add(
                Box.createVerticalStrut(
                        5
                )
        );

        header.add(assistant);

        header.add(
                Box.createVerticalStrut(
                        3
                )
        );

        header.add(helper);

        sidebar.add(
                header,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // MENU
        // -----------------------------------------------------

        JPanel menu =
                new JPanel();

        menu.setLayout(
                new BoxLayout(
                        menu,
                        BoxLayout.Y_AXIS
                )
        );

        menu.setBackground(
                SIDEBAR_BG
        );

        menu.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        // AI Assistant
        addSidebarButton(
                menu,
                "🏠",
                "AI Assistant",
                this::showHome
        );

        // Smart Match
        addSidebarButton(
                menu,
                "🔎",
                "Smart Matches",
                this::openSmartMatch
        );

        // My Reports
        addSidebarButton(
                menu,
                "📦",
                "My Reports",
                this::openMyReports
        );

        // My Claims - NOW CONNECTED
        addSidebarButton(
                menu,
                "📋",
                "My Claims",
                this::openMyClaims
        );

        // Notifications
        addSidebarButton(
                menu,
                "🔔",
                "Notifications",
                this::openNotifications
        );

        // Profile
        addSidebarButton(
                menu,
                "👤",
                "Profile",
                this::openProfile
        );

        // Settings
        addSidebarButton(
                menu,
                "⚙",
                "Settings",
                this::openSettings
        );

        sidebar.add(
                menu,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // LOGOUT
        // -----------------------------------------------------

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.setBackground(
                SIDEBAR_BG
        );

        bottom.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        20,
                        12
                )
        );

        JButton logout =
                createSidebarButton(
                        "🚪",
                        "Logout"
                );

        logout.addActionListener(
                e -> logout()
        );

        bottom.add(logout);

        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );

        add(
                sidebar,
                BorderLayout.WEST
        );
    }

    // =========================================================
    // ADD SIDEBAR BUTTON
    // =========================================================

    private void addSidebarButton(
            JPanel menu,
            String icon,
            String text,
            Runnable action
    ) {

        JButton button =
                createSidebarButton(
                        icon,
                        text
                );

        button.addActionListener(
                e -> action.run()
        );

        menu.add(button);

        menu.add(
                Box.createVerticalStrut(
                        5
                )
        );
    }

    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private JButton createSidebarButton(
            String icon,
            String text
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        button.setPreferredSize(
                new Dimension(
                        220,
                        48
                )
        );

        button.setBackground(
                SIDEBAR_BG
        );

        button.setForeground(
                WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        row.setOpaque(false);

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );

        JLabel textLabel =
                new JLabel(
                        text
                );

        textLabel.setForeground(
                WHITE
        );

        textLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        row.add(
                iconLabel,
                BorderLayout.WEST
        );

        row.add(
                textLabel,
                BorderLayout.CENTER
        );

        button.add(
                row,
                BorderLayout.CENTER
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                CARD_HOVER
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                SIDEBAR_BG
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TOGGLE SIDEBAR
    // =========================================================

    private void toggleSidebar() {

        sidebarExpanded =
                !sidebarExpanded;

        int width =
                sidebarExpanded
                        ? 250
                        : 72;

        sidebar.setPreferredSize(
                new Dimension(
                        width,
                        0
                )
        );

        sidebar.revalidate();

        sidebar.repaint();
    }

    // =========================================================
    // CONTENT PANEL
    // =========================================================

    private void createContentPanel() {

        contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        contentPanel.setBackground(
                DARK_BG
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // HOME SCREEN
    // =========================================================

    private void showHome() {

        clearContent();

        GalaxyPanel galaxy =
                new GalaxyPanel();

        galaxy.setLayout(
                new BorderLayout()
        );

        // -----------------------------------------------------
        // HEADING
        // -----------------------------------------------------

        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setBorder(
                new EmptyBorder(
                        28,
                        50,
                        5,
                        50
                )
        );

        JLabel title =
                new JLabel(
                        "AI Assistant"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Your smart campus companion for Lost & Found"
                );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(
                        5
                )
        );

        heading.add(subtitle);

        galaxy.add(
                heading,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        JPanel center =
                new JPanel();

        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        // -----------------------------------------------------
        // ROBOT
        // -----------------------------------------------------

        JPanel mascotWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        mascotWrapper.setOpaque(false);

        mascotWrapper.setPreferredSize(
                new Dimension(
                        550,
                        500
                )
        );

        mascotWrapper.setMinimumSize(
                new Dimension(
                        550,
                        500
                )
        );

        mascotWrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        500
                )
        );

        MascotPanel mascot =
                new MascotPanel();

        mascot.setPreferredSize(
                new Dimension(
                        500,
                        500
                )
        );

        mascot.setMinimumSize(
                new Dimension(
                        500,
                        500
                )
        );

        mascot.setMaximumSize(
                new Dimension(
                        500,
                        500
                )
        );

        mascot.setMessage(
                "Hi "
                        + loggedInFullName
                        + "! How can I help you today?"
        );

        mascotWrapper.add(
                mascot
        );

        center.add(
                mascotWrapper
        );

        // -----------------------------------------------------
        // ACTION BUTTONS
        // -----------------------------------------------------

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                25,
                                0
                        )
                );

        actions.setOpaque(false);

        actions.setPreferredSize(
                new Dimension(
                        900,
                        105
                )
        );

        actions.setMinimumSize(
                new Dimension(
                        700,
                        105
                )
        );

        actions.setMaximumSize(
                new Dimension(
                        1000,
                        105
                )
        );

        JButton lostButton =
                createActionCard(
                        "🔎",
                        "I Lost Something",
                        "Report an item you lost"
                );

        JButton foundButton =
                createActionCard(
                        "📦",
                        "I Found Something",
                        "Report an item you found"
                );

        lostButton.addActionListener(
                e -> openAIAssistant("LOST")
        );

        foundButton.addActionListener(
                e -> openAIAssistant("FOUND")
        );

        actions.add(lostButton);

        actions.add(foundButton);

        JPanel actionsWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        actionsWrapper.setOpaque(false);

        actionsWrapper.setPreferredSize(
                new Dimension(
                        0,
                        125
                )
        );

        actionsWrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        actionsWrapper.add(
                actions
        );

        center.add(
                actionsWrapper
        );

        // -----------------------------------------------------
        // SCROLL PANE
        // -----------------------------------------------------

        JScrollPane scrollPane =
                new JScrollPane(
                        center
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
                .setBackground(
                        new Color(
                                8,
                                13,
                                32
                        )
                );

        scrollPane.getVerticalScrollBar()
                .setUI(
                        new ModernScrollBarUI()
                );

        galaxy.add(
                scrollPane,
                BorderLayout.CENTER
        );

        contentPanel.add(
                galaxy,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =========================================================
    // MODERN SCROLLBAR
    // =========================================================

    private static class ModernScrollBarUI
            extends BasicScrollBarUI {

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
    // CLEAR CONTENT
    // =========================================================

    private void clearContent() {

        contentPanel.removeAll();

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =========================================================
    // ACTION CARD
    // =========================================================

    private JButton createActionCard(
            String icon,
            String title,
            String description
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setBackground(
                CARD_BG
        );

        button.setForeground(
                WHITE
        );

        button.setFocusPainted(false);

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
                                18,
                                22,
                                18,
                                22
                        )
                )
        );

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                18,
                                0
                        )
                );

        content.setOpaque(false);

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        38
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setForeground(
                WHITE
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setForeground(
                SECONDARY_TEXT
        );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(
                        5
                )
        );

        text.add(descriptionLabel);

        content.add(
                iconLabel,
                BorderLayout.WEST
        );

        content.add(
                text,
                BorderLayout.CENTER
        );

        button.add(
                content,
                BorderLayout.CENTER
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                CARD_HOVER
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                CARD_BG
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // OPEN AI ASSISTANT
    // =========================================================

    private void openAIAssistant(String mode) {

        AIAssistantFrame frame =
                new AIAssistantFrame(
                        loggedInFullName,
                        loggedInUsername,
                        mode
                );

        frame.setLocationRelativeTo(this);

        frame.setVisible(true);
    }

    // =========================================================
    // OPEN MY REPORTS
    // =========================================================

    private void openMyReports() {

        MyReportsFrame frame =
                new MyReportsFrame(
                        loggedInUsername
                );

        frame.setLocationRelativeTo(
                this
        );

        frame.setVisible(
                true
        );

        dispose();
    }

    // =========================================================
    // OPEN MY CLAIMS
    // =========================================================

    private void openMyClaims() {

        MyClaimsFrame frame =
                new MyClaimsFrame(
                        loggedInUsername
                );

        frame.setLocationRelativeTo(
                this
        );

        frame.setVisible(
                true
        );

        dispose();
    }

    // =========================================================
    // OPEN NOTIFICATIONS
    // =========================================================

    private void openNotifications() {

        NotificationFrame notificationFrame =
                new NotificationFrame(
                        loggedInFullName,
                        loggedInUsername
                );

        notificationFrame.setLocationRelativeTo(
                this
        );

        notificationFrame.setVisible(
                true
        );

        dispose();
    }

    // =========================================================
    // OPEN PROFILE
    // =========================================================

    private void openProfile() {

        MyProfileFrame profileFrame =
                new MyProfileFrame(
                        loggedInUsername
                );

        profileFrame.setLocationRelativeTo(
                this
        );

        profileFrame.setVisible(
                true
        );

        dispose();
    }

    // =========================================================
    // OPEN SETTINGS
    // =========================================================

    private void openSettings() {

        SettingsFrame settingsFrame =
                new SettingsFrame(
                        loggedInUsername
                );

        settingsFrame.setLocationRelativeTo(
                this
        );

        settingsFrame.setVisible(
                true
        );

        dispose();
    }

    // =========================================================
    // SMART MATCH
    // =========================================================

    private void openSmartMatch() {

        SmartMatchSearchDialog dialog =
                new SmartMatchSearchDialog(this);

        dialog.setLocationRelativeTo(this);

        dialog.setVisible(true);

        // User cancelled the search
        if (!dialog.isConfirmed()) {
            return;
        }

        String lostItem =
                dialog.getLostItem();

        String lostLocation =
                dialog.getLostLocation();

        // =====================================================
        // RUN SMART MATCH IN BACKGROUND
        // =====================================================

        new SwingWorker<List<String>, Void>() {

            @Override
            protected List<String> doInBackground()
                    throws Exception {

                SmartMatchService service =
                        new SmartMatchService();

                return service.findMatches(
                        lostItem,
                        lostLocation
                );
            }

            @Override
            protected void done() {

                try {

                    List<String> matches =
                            get();

                    if (matches.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                StudentDashboard.this,
                                "No possible matches found.",
                                "Smart Match Results",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {

                        showMatchResults(matches);
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            StudentDashboard.this,
                            "Unable to perform Smart Match.",
                            "Smart Match Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        }.execute();
    }

    // =========================================================
    // MATCH RESULTS
    // =========================================================

    private void showMatchResults(
            List<String> matches
    ) {

        SmartMatchResultsDialog dialog =
                new SmartMatchResultsDialog(
                        this,
                        matches,
                        loggedInUsername
                );

        dialog.setLocationRelativeTo(this);

        dialog.setVisible(true);
    }

    // =========================================================
    // GALAXY BUTTON
    // =========================================================

    private JButton createGalaxyButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                color
        );

        button.setForeground(
                WHITE
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
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
                        8,
                        15,
                        8,
                        15
                )
        );

        return button;
    }

    // =========================================================
    // EXTRACT SCORE
    // =========================================================

    private String extractMatchScore(
            String line
    ) {

        StringBuilder number =
                new StringBuilder();

        for (
                int i = 0;
                i < line.length();
                i++
        ) {

            char c =
                    line.charAt(i);

            if (
                    Character.isDigit(c)
                            ||
                            c == '%'
            ) {

                number.append(c);
            }
        }

        String result =
                number.toString();

        if (
                !result.isEmpty()
                        &&
                        !result.endsWith("%")
        ) {

            result += "%";
        }

        return result;
    }

    // =========================================================
    // FIND REPORT ID
    // =========================================================

    private int findReportId(
            String itemName,
            String location
    ) {

        String sql =
                "SELECT id FROM reports "
                        + "WHERE item_name = ? "
                        + "AND location = ? "
                        + "AND report_type = 'Found' "
                        + "LIMIT 1";

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
                        itemName
                );

                statement.setString(
                        2,
                        location
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (
                            result.next()
                    ) {

                        return result.getInt(
                                "id"
                        );
                    }
                }
            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();
        }

        return -1;
    }

    // =========================================================
    // COMING SOON
    // =========================================================

    private void showComingSoon(
            String feature
    ) {

        clearContent();

        GalaxyPanel background =
                new GalaxyPanel();

        background.setLayout(
                new GridBagLayout()
        );

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(
                CARD_BG
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                40,
                                70,
                                40,
                                70
                        )
                )
        );

        JLabel robot =
                new JLabel(
                        "🤖"
                );

        robot.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        55
                )
        );

        robot.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        feature
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message =
                new JLabel(
                        "This section will be connected next."
                );

        message.setForeground(
                SECONDARY_TEXT
        );

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JButton back =
                createGalaxyButton(
                        "← Back to AI Assistant",
                        BLUE
                );

        back.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        back.addActionListener(
                e -> showHome()
        );

        card.add(robot);

        card.add(
                Box.createVerticalStrut(
                        15
                )
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(
                        8
                )
        );

        card.add(message);

        card.add(
                Box.createVerticalStrut(
                        25
                )
        );

        card.add(back);

        background.add(card);

        contentPanel.add(
                background,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        final JDialog logoutDialog =
                new JDialog(
                        this,
                        "Logout",
                        true
                );

        logoutDialog.setSize(
                500,
                310
        );

        logoutDialog.setResizable(false);

        logoutDialog.setLocationRelativeTo(this);

        logoutDialog.setLayout(
                new BorderLayout()
        );

        // ---------------------------------------------------------
        // MAIN GALAXY PANEL
        // ---------------------------------------------------------

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                DARK_BG
        );

        root.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                0,
                                0,
                                0,
                                0
                        )
                )
        );

        // ---------------------------------------------------------
        // TOP ACCENT
        // ---------------------------------------------------------

        JPanel accent =
                new JPanel();

        accent.setBackground(
                CYAN
        );

        accent.setPreferredSize(
                new Dimension(
                        0,
                        5
                )
        );

        root.add(
                accent,
                BorderLayout.NORTH
        );

        // ---------------------------------------------------------
        // CENTER CONTENT
        // ---------------------------------------------------------

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBorder(
                new EmptyBorder(
                        22,
                        30,
                        15,
                        30
                )
        );

        JLabel icon =
                new JLabel(
                        "↪",
                        SwingConstants.CENTER
                );

        icon.setOpaque(true);

        icon.setBackground(
                BLUE
        );

        icon.setForeground(
                WHITE
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        icon.setPreferredSize(
                new Dimension(
                        58,
                        58
                )
        );

        icon.setMinimumSize(
                new Dimension(
                        58,
                        58
                )
        );

        icon.setMaximumSize(
                new Dimension(
                        58,
                        58
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "Confirm Logout"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message =
                new JLabel(
                        "Are you sure you want to log out?"
                );

        message.setForeground(
                SECONDARY_TEXT
        );

        message.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(icon);

        content.add(
                Box.createVerticalStrut(
                        12
                )
        );

        content.add(title);

        content.add(
                Box.createVerticalStrut(
                        9
                )
        );

        content.add(message);

        root.add(
                content,
                BorderLayout.CENTER
        );

        // ---------------------------------------------------------
        // BUTTONS
        // ---------------------------------------------------------

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                14,
                                12
                        )
                );

        footer.setBackground(
                new Color(
                        8,
                        13,
                        32
                )
        );

        JButton cancel =
                createGalaxyButton(
                        "Cancel",
                        new Color(
                                45,
                                60,
                                100
                        )
                );

        JButton logout =
                createGalaxyButton(
                        "Logout",
                        BLUE
                );

        cancel.addActionListener(
                e -> logoutDialog.dispose()
        );

        logout.addActionListener(
                e -> {

                    logoutDialog.dispose();

                    dispose();

                    LoginFrame loginFrame =
                            new LoginFrame(
                                    "USER"
                            );

                    loginFrame.setLocationRelativeTo(
                            null
                    );

                    loginFrame.setVisible(
                            true
                    );
                }
        );

        footer.add(cancel);

        footer.add(logout);

        root.add(
                footer,
                BorderLayout.SOUTH
        );

        logoutDialog.add(root);

        logoutDialog.setVisible(true);
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

                    LoginFrame loginFrame =
                            new LoginFrame(
                                    "USER"
                            );

                    loginFrame.setVisible(
                            true
                    );
                }
        );
    }
}