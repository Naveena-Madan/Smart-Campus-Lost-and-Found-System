package gui;

import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.util.List;
import service.AIScannerService;

/**
 * Admin Dashboard
 *
 * Step 1 of the new Admin Dashboard redesign:
 * - Modern admin shell
 * - Collapsible sidebar
 * - Dashboard Home
 * - Item Reports section
 * - Existing Verify / Reject / Claims / Logout functionality preserved
 *
 * The remaining sections are placed in the navigation now so they can be
 * implemented one by one without disturbing the existing database flow.
 */
public class AdminDashboard extends JFrame {

    private JTable reportsTable;
    private DefaultTableModel tableModel;


    private final String url = database.DBConfig.DB_URL;
    private final String username = database.DBConfig.DB_USER;
    private final String password = database.DBConfig.DB_PASSWORD;

    // ---------- Same Galaxy theme as StudentDashboard ----------
    private final Color BACKGROUND = new Color(7, 12, 38);
    private final Color SIDEBAR = new Color(10, 16, 40);
    private final Color CARD = new Color(15, 25, 58);
    private final Color CARD_2 = new Color(25, 40, 82);
    private final Color BLUE = new Color(70, 115, 255);
    private final Color PURPLE = new Color(120, 40, 200);
    private final Color CYAN = new Color(50, 210, 255);
    private final Color GREEN = new Color(70, 205, 135);
    private final Color RED = new Color(235, 86, 105);
    private final Color ORANGE = new Color(245, 166, 70);
    private final Color TEXT = Color.WHITE;
    private final Color MUTED = new Color(175, 195, 230);
    private final Color BORDER = new Color(60, 90, 150);

    private final CardLayout cardLayout = new CardLayout();
    private JPanel contentCards;

    private JPanel sidebar;
    private JPanel sidebarItems;
    private boolean sidebarExpanded = true;

    // Dynamic dashboard AI match list.
    private JPanel recentAIMatchesList;

    // Dynamic dashboard overview values loaded from MySQL.
    private JLabel dashboardLostValue;
    private JLabel dashboardFoundValue;
    private JLabel dashboardReturnedValue;

    private JLabel pageTitle;
    private JLabel pageSubtitle;

    private final String adminUsername;

    public AdminDashboard(String adminUsername) {

        this.adminUsername = adminUsername;

        setTitle("Admin Dashboard - Smart Campus Lost and Found");
        setSize(1350, 850);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Same full-screen startup behavior as StudentDashboard.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildUI();
    }

    // =========================================================
    // BUILD MAIN UI
    // =========================================================

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        // ---------- Top header ----------
        root.add(createTopHeader(), BorderLayout.NORTH);

        // ---------- Main body ----------
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BACKGROUND);

        sidebar = createSidebar();
        body.add(sidebar, BorderLayout.WEST);

        JPanel rightArea = new JPanel(new BorderLayout());
        rightArea.setOpaque(false);

        JPanel pageHeader = createPageHeader();
        rightArea.add(pageHeader, BorderLayout.NORTH);

        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);

        // Main pages may be taller than the screen.
        // Keep the sidebar fixed and put the scrollbar ONLY here.
        JScrollPane contentScroll = new JScrollPane(contentCards);
        contentScroll.setBorder(null);
        contentScroll.setOpaque(false);
        contentScroll.getViewport().setOpaque(false);
        contentScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        contentScroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        contentScroll.getVerticalScrollBar().setUnitIncrement(18);
        contentScroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(10, 0)
        );
        contentScroll.getVerticalScrollBar().setUI(
                createDarkScrollBarUI()
        );

        contentCards.add(createDashboardHome(), "HOME");
        contentCards.add(createItemReportsPanel(), "REPORTS");
        contentCards.add(
                createAIScannerPanel(),
                "AI"
        );

        contentCards.add(
                createPendingVerificationPanel(),
                "PENDING"
        );

        contentCards.add(
                createNotificationsPanel(),
                "NOTIFICATIONS"
        );

        contentCards.add(
                createStudentsPanel(),
                "STUDENTS"
        );

        contentCards.add(
                createAnalyticsPanel(),
                "ANALYTICS"
        );

        contentCards.add(
                createAdminProfilePanel(),
                "PROFILE"
        );

        rightArea.add(contentScroll, BorderLayout.CENTER);

        body.add(rightArea, BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);

        add(root);

        showPage("HOME", "Dashboard", "Welcome back, " + adminUsername + " ");
    }

    // =========================================================
    // TOP HEADER
    // =========================================================

    private JPanel createTopHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 72));
        header.setBackground(new Color(8, 13, 32));
        header.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, BORDER
                )
        );

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 11));
        left.setOpaque(false);

        JButton menuButton = createIconButton("☰");
        menuButton.setToolTipText("Collapse / Expand Sidebar");
        menuButton.addActionListener(e -> toggleSidebar());

        JLabel brand = new JLabel("Smart Campus Lost & Found");
        brand.setForeground(TEXT);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel aiBadge = new JLabel("  AI ADMIN  ");
        aiBadge.setOpaque(true);
        aiBadge.setBackground(new Color(44, 53, 92));
        aiBadge.setForeground(new Color(185, 205, 255));
        aiBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        aiBadge.setBorder(new EmptyBorder(5, 8, 5, 8));

        left.add(menuButton);
        left.add(brand);
        left.add(aiBadge);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 11));
        right.setOpaque(false);

        JPanel adminChip = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        adminChip.setBackground(CARD_2);
        adminChip.setBorder(BorderFactory.createLineBorder(BORDER));

        JLabel avatar = new JLabel("A");
        avatar.setOpaque(true);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setBackground(PURPLE);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setPreferredSize(new Dimension(28, 28));

        JLabel adminText = new JLabel("Admin");
        adminText.setForeground(TEXT);
        adminText.setFont(new Font("Segoe UI", Font.BOLD, 12));

        adminChip.add(avatar);
        adminChip.add(adminText);

        adminChip.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        adminChip.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {
                        showPage(
                                "PROFILE",
                                "Admin Profile",
                                "Administrator account information."
                        );
                    }
                }
        );

        right.add(adminChip);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER)
        );

        // ---------- Sidebar top ----------
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(24, 18, 12, 18));

        JLabel mascot = new JLabel("✧");
        mascot.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        mascot.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel assistant = new JLabel("AI Assistant");
        assistant.setForeground(TEXT);
        assistant.setFont(new Font("Segoe UI", Font.BOLD, 16));
        assistant.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel assistantSub = new JLabel("Admin control center");
        assistantSub.setForeground(MUTED);
        assistantSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        assistantSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(mascot);
        top.add(Box.createVerticalStrut(4));
        top.add(assistant);
        top.add(Box.createVerticalStrut(3));
        top.add(assistantSub);

        sidebar.add(top, BorderLayout.NORTH);

        // ---------- Sidebar navigation ----------
        sidebarItems = new JPanel();
        sidebarItems.setOpaque(false);
        sidebarItems.setLayout(new BoxLayout(sidebarItems, BoxLayout.Y_AXIS));
        sidebarItems.setBorder(new EmptyBorder(10, 10, 10, 10));

        addNavButton("⌂", "Dashboard", "HOME", false);

        addNavButton("⌕", "AI Scanner & Matches", "AI", false);

        addSectionLabel("ITEM REPORTS");

        addNavButton("◇", "Lost Items", "REPORTS", true);
        addNavButton("◈", "Found Items", "REPORTS", true);

        addNavButton("♧", "Notifications", "NOTIFICATIONS", false);
        addNavButton("♙", "Students", "STUDENTS", false);
        addNavButton("▥", "Analytics", "ANALYTICS", false);
        addNavButton("♟", "Admin Profile", "PROFILE", false);

        // Logout is now part of the normal navigation order.
        JButton logoutNav =
                createNavButton(
                        "↪",
                        "Logout",
                        false
                );

        logoutNav.addActionListener(
                e -> logout()
        );

        sidebarItems.add(logoutNav);
        sidebarItems.add(
                Box.createVerticalStrut(3)
        );

        // Sidebar stays fixed. No vertical scrollbar is shown here.
        sidebar.add(
                sidebarItems,
                BorderLayout.CENTER
        );

        // ---------- Fixed admin identity footer ----------
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                new EmptyBorder(
                        8,
                        10,
                        14,
                        10
                )
        );

        JLabel userLabel =
                new JLabel(
                        "<html><center>"
                                + adminUsername
                                + "<br>"
                                + "<font color='#8FA0C0'>"
                                + "ADMINISTRATOR"
                                + "</font></center></html>"
                );

        userLabel.setForeground(MUTED);
        userLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        userLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        userLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        bottom.add(userLabel);

        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );

        return sidebar;
    }

    private javax.swing.plaf.basic.BasicScrollBarUI createDarkScrollBarUI() {

        return new javax.swing.plaf.basic.BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(120, 75, 220);
                this.trackColor = new Color(10, 14, 34);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        };
    }

    private void addSectionLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(new Color(104, 125, 165));
        label.setFont(new Font("Segoe UI", Font.BOLD, 9));
        label.setBorder(new EmptyBorder(12, 12, 5, 5));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebarItems.add(label);
    }

    private void addNavButton(
            String icon,
            String text,
            String page,
            boolean child) {

        JButton button = createNavButton(icon, text, child);

        button.addActionListener(
                e -> {

                    if ("CLAIMS".equals(page)) {

                        AdminClaimsFrame frame =
                                new AdminClaimsFrame();

                        frame.setLocationRelativeTo(this);
                        frame.setVisible(true);
                        return;
                    }

                    showPage(
                            page,
                            getPageTitle(page),
                            getPageSubtitle(page)
                    );
                }
        );

        sidebarItems.add(button);
        sidebarItems.add(Box.createVerticalStrut(3));
    }

    private JButton createNavButton(
            String icon,
            String text,
            boolean child) {

        String display =
                child
                        ? "     " + icon + "  " + text
                        : icon + "   " + text;

        JButton button = new JButton(display);

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setPreferredSize(new Dimension(200, 42));
        button.setMinimumSize(new Dimension(50, 42));

        // Use Segoe UI Symbol for reliable outline-style navigation glyphs.
        // These are intentionally similar in style, but distinct from student icons.
        button.setFont(
                new Font(
                        "Segoe UI Symbol",
                        child ? Font.PLAIN : Font.BOLD,
                        child ? 13 : 15
                )
        );

        button.setForeground(TEXT);
        button.setBackground(SIDEBAR);
        button.setFocusPainted(false);
        button.setBorder(
                new EmptyBorder(9, 10, 9, 8)
        );
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // =========================================================
    // COLLAPSIBLE SIDEBAR
    // =========================================================

    private void toggleSidebar() {

        sidebarExpanded = !sidebarExpanded;

        if (sidebarExpanded) {

            sidebar.setPreferredSize(new Dimension(250, 0));

        } else {

            sidebar.setPreferredSize(new Dimension(65, 0));
        }

        sidebar.revalidate();
        sidebar.getParent().revalidate();
        sidebar.getParent().repaint();
    }

    // =========================================================
    // PAGE HEADER
    // =========================================================

    private JPanel createPageHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(22, 28, 10, 28));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        pageTitle = new JLabel("Dashboard");
        pageTitle.setForeground(TEXT);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        pageSubtitle =
                new JLabel("Welcome back, Admin ");
        pageSubtitle.setForeground(new Color(175, 195, 230));
        pageSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        titles.add(pageTitle);
        titles.add(Box.createVerticalStrut(4));
        titles.add(pageSubtitle);

        header.add(titles, BorderLayout.WEST);

        return header;
    }

    // =========================================================
    // DASHBOARD HOME
    // =========================================================

    private JPanel createDashboardHome() {

        // Use the exact same Galaxy background used by StudentDashboard.
        GalaxyPanel panel = new GalaxyPanel();
        panel.setLayout(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(0, 28, 22, 28));

        // =====================================================
        // TOP: 3 overview numbers
        // =====================================================

        JPanel overview = new JPanel(
                new GridLayout(1, 3, 12, 0)
        );
        overview.setOpaque(false);

        dashboardLostValue = new JLabel("-");
        dashboardFoundValue = new JLabel("-");
        dashboardReturnedValue = new JLabel("-");

        overview.add(
                createDynamicStatCard(
                        "▤",
                        "Lost Items",
                        dashboardLostValue,
                        BLUE
                )
        );

        overview.add(
                createDynamicStatCard(
                        "●",
                        "Found Items",
                        dashboardFoundValue,
                        GREEN
                )
        );

        overview.add(
                createDynamicStatCard(
                        "✓",
                        "Returned",
                        dashboardReturnedValue,
                        CYAN
                )
        );

        panel.add(overview, BorderLayout.NORTH);

        // Load the real overview values from MySQL without freezing the UI.
        SwingUtilities.invokeLater(this::loadDashboardStats);

        // =====================================================
        // MAIN: Fully visible robot + action center + matches
        // =====================================================

        JPanel mainArea = new JPanel(
                new BorderLayout(16, 0)
        );
        mainArea.setOpaque(false);

        // -----------------------------------------------------
        // ROBOT CHARACTER
        // -----------------------------------------------------

        JPanel robotCard = new JPanel(
                new BorderLayout()
        );

        robotCard.setBackground(CARD);
        robotCard.setPreferredSize(
                new Dimension(330, 0)
        );

        robotCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(8, 8, 8, 8)
                )
        );

        MascotPanel mascot = new MascotPanel();

        mascot.setPreferredSize(
                new Dimension(300, 500)
        );

        mascot.setMinimumSize(
                new Dimension(280, 480)
        );

        mascot.setMaximumSize(
                new Dimension(320, 520)
        );

        // Admin-specific robot message.
        mascot.setMessage(
                "Welcome, Admin! ◆"
        );

        // Override the generic MascotPanel hover/click messages with
        // admin-specific messages for this dashboard.
        mascot.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        mascot.setMessage(
                                "✦ Ready to review cases?"
                        );
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        mascot.setMessage(
                                "Welcome, Admin! ◆"
                        );
                    }

                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        mascot.setMessage(
                                "✦ I'm ready to help!"
                        );
                    }
                }
        );

        robotCard.add(
                mascot,
                BorderLayout.CENTER
        );

        JLabel robotCaption = new JLabel(
                "AI Campus Assistant",
                SwingConstants.CENTER
        );

        robotCaption.setForeground(TEXT);
        robotCaption.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        robotCaption.setBorder(
                new EmptyBorder(5, 0, 5, 0)
        );

        robotCard.add(
                robotCaption,
                BorderLayout.SOUTH
        );

        mainArea.add(
                robotCard,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT SIDE
        // -----------------------------------------------------

        JPanel rightContent = new JPanel(
                new GridLayout(2, 1, 0, 14)
        );
        rightContent.setOpaque(false);

        rightContent.add(createActionCenterCard());
        rightContent.add(createRecentAIMatchesCard());

        mainArea.add(
                rightContent,
                BorderLayout.CENTER
        );

        panel.add(
                mainArea,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createActionCenterCard() {

        JPanel card = createSectionCard("✦ Action Center");

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.add(
                createActionItem(
                        "△",
                        "Claims waiting for review",
                        "4 claims need your decision",
                        ORANGE,
                        "CLAIMS"
                )
        );

        list.add(Box.createVerticalStrut(7));

        list.add(
                createActionItem(
                        "◎",
                        "AI matches ready",
                        "2 possible matches found",
                        PURPLE,
                        "AI"
                )
        );

        list.add(Box.createVerticalStrut(7));

        list.add(
                createActionItem(
                        "▤",
                        "Reports waiting for verification",
                        "3 reports need attention",
                        BLUE,
                        "PENDING"
                )
        );

        card.add(list, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActionItem(
            String icon,
            String title,
            String subtitle,
            Color accent,
            String page) {

        JPanel row = new JPanel(
                new BorderLayout(10, 0)
        );

        row.setBackground(CARD_2);

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(7, 9, 7, 9)
                )
        );

        JPanel left = new JPanel(
                new BorderLayout(9, 0)
        );

        left.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.BOLD,
                        18
                )
        );

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(MUTED);
        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(subtitleLabel);

        left.add(
                iconLabel,
                BorderLayout.WEST
        );

        left.add(
                text,
                BorderLayout.CENTER
        );

        JButton reviewButton = createActionButton(
                "Review",
                accent
        );

        reviewButton.addActionListener(e -> {

            if ("CLAIMS".equals(page)) {

                AdminClaimsFrame frame =
                        new AdminClaimsFrame();

                frame.setLocationRelativeTo(this);
                frame.setVisible(true);

            } else {

                showPage(
                        page,
                        getPageTitle(page),
                        getPageSubtitle(page)
                );
            }
        });

        row.add(
                left,
                BorderLayout.CENTER
        );

        row.add(
                reviewButton,
                BorderLayout.EAST
        );

        return row;
    }

    private JPanel createRecentAIMatchesCard() {

        JPanel card =
                createSectionCard("✦ Recent AI Matches");

        recentAIMatchesList = new JPanel();
        recentAIMatchesList.setOpaque(false);
        recentAIMatchesList.setLayout(
                new BoxLayout(
                        recentAIMatchesList,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel loading = new JLabel("Loading recent AI matches...");
        loading.setForeground(MUTED);
        loading.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );
        recentAIMatchesList.add(loading);

        card.add(
                recentAIMatchesList,
                BorderLayout.CENTER
        );

        SwingUtilities.invokeLater(
                this::loadRecentAIMatches
        );

        return card;
    }

    // =========================================================
    // LOAD REAL RECENT AI MATCHES
    // =========================================================

    private void loadRecentAIMatches() {

        if (recentAIMatchesList == null) {
            return;
        }

        recentAIMatchesList.removeAll();

        JLabel loading =
                new JLabel("Scanning recent Found reports...");

        loading.setForeground(MUTED);
        loading.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        recentAIMatchesList.add(loading);
        recentAIMatchesList.revalidate();
        recentAIMatchesList.repaint();

        new SwingWorker<List<RecentAIMatch>, Void>() {

            @Override
            protected List<RecentAIMatch> doInBackground()
                    throws Exception {

                List<RecentAIMatch> recentMatches =
                        new java.util.ArrayList<>();

                String sql =
                        "SELECT id, item_name, location "
                                + "FROM reports "
                                + "WHERE LOWER(report_type) = 'found' "
                                + "AND (status IS NULL "
                                + "OR LOWER(status) <> 'returned') "
                                + "ORDER BY id DESC "
                                + "LIMIT 8";

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                try (
                        Connection connection =
                                DriverManager.getConnection(
                                        url,
                                        username,
                                        password
                                );

                        PreparedStatement statement =
                                connection.prepareStatement(sql);

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    AIScannerService scanner =
                            new AIScannerService();

                    while (result.next()) {

                        int foundId =
                                result.getInt("id");

                        String foundItem =
                                result.getString("item_name");

                        String foundLocation =
                                result.getString("location");

                        List<AIScannerService.MatchResult> matches =
                                scanner.scanFoundReport(foundId);

                        if (!matches.isEmpty()) {

                            AIScannerService.MatchResult best =
                                    matches.get(0);

                            recentMatches.add(
                                    new RecentAIMatch(
                                            foundItem,
                                            foundLocation,
                                            best.getLostItem(),
                                            best.getLostLocation(),
                                            best.getMatchScore(),
                                            best.getConfidence()
                                    )
                            );
                        }
                    }
                }

                recentMatches.sort(
                        java.util.Comparator.comparingDouble(
                                RecentAIMatch::getScore
                        ).reversed()
                );

                if (recentMatches.size() > 3) {
                    return new java.util.ArrayList<>(
                            recentMatches.subList(0, 3)
                    );
                }

                return recentMatches;
            }

            @Override
            protected void done() {

                try {

                    List<RecentAIMatch> matches = get();

                    recentAIMatchesList.removeAll();

                    if (matches.isEmpty()) {

                        JLabel empty =
                                new JLabel(
                                        "No AI matches found yet."
                                );

                        empty.setForeground(MUTED);
                        empty.setFont(
                                new Font(
                                        "Segoe UI",
                                        Font.PLAIN,
                                        11
                                )
                        );

                        recentAIMatchesList.add(empty);

                    } else {

                        for (int i = 0; i < matches.size(); i++) {

                            RecentAIMatch match = matches.get(i);

                            Color scoreColor =
                                    match.getScore() >= 80
                                            ? GREEN
                                            : match.getScore() >= 60
                                            ? CYAN
                                            : ORANGE;

                            recentAIMatchesList.add(
                                    createDynamicMatchRow(
                                            match,
                                            scoreColor
                                    )
                            );

                            if (i < matches.size() - 1) {
                                recentAIMatchesList.add(
                                        Box.createVerticalStrut(7)
                                );
                            }
                        }
                    }

                    recentAIMatchesList.revalidate();
                    recentAIMatchesList.repaint();

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    recentAIMatchesList.removeAll();

                    JLabel error =
                            new JLabel(
                                    "Unable to load AI matches."
                            );

                    error.setForeground(
                            new Color(235, 86, 105)
                    );
                    error.setFont(
                            new Font(
                                    "Segoe UI",
                                    Font.PLAIN,
                                    11
                            )
                    );

                    recentAIMatchesList.add(error);
                    recentAIMatchesList.revalidate();
                    recentAIMatchesList.repaint();

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    private JPanel createDynamicMatchRow(
            RecentAIMatch match,
            Color scoreColor) {

        JPanel row = new JPanel(
                new BorderLayout(9, 0)
        );

        row.setBackground(CARD_2);
        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(7, 9, 7, 9)
                )
        );

        JLabel iconLabel =
                new JLabel("◎");
        iconLabel.setForeground(scoreColor);
        iconLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel itemLabel =
                new JLabel(
                        match.getFoundItem()
                                + "  ↔  "
                                + match.getLostItem()
                );

        itemLabel.setForeground(TEXT);
        itemLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel locationLabel =
                new JLabel(
                        "Found: "
                                + safeText(match.getFoundLocation())
                                + "  |  Lost: "
                                + safeText(match.getLostLocation())
                );

        locationLabel.setForeground(MUTED);
        locationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        JLabel confidenceLabel =
                new JLabel(
                        "Confidence: "
                                + match.getConfidence()
                );

        confidenceLabel.setForeground(MUTED);
        confidenceLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        info.add(itemLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(locationLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(confidenceLabel);

        JLabel scoreLabel =
                new JLabel(
                        String.format(
                                "%.0f%%",
                                match.getScore()
                        )
                );

        scoreLabel.setForeground(scoreColor);
        scoreLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        row.add(iconLabel, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(scoreLabel, BorderLayout.EAST);

        return row;
    }

    private String safeText(String value) {
        return value == null || value.trim().isEmpty()
                ? "-"
                : value;
    }

    private static class RecentAIMatch {

        private final String foundItem;
        private final String foundLocation;
        private final String lostItem;
        private final String lostLocation;
        private final double score;
        private final String confidence;

        RecentAIMatch(
                String foundItem,
                String foundLocation,
                String lostItem,
                String lostLocation,
                double score,
                String confidence) {

            this.foundItem = foundItem;
            this.foundLocation = foundLocation;
            this.lostItem = lostItem;
            this.lostLocation = lostLocation;
            this.score = score;
            this.confidence = confidence;
        }

        String getFoundItem() {
            return foundItem;
        }

        String getFoundLocation() {
            return foundLocation;
        }

        String getLostItem() {
            return lostItem;
        }

        String getLostLocation() {
            return lostLocation;
        }

        double getScore() {
            return score;
        }

        String getConfidence() {
            return confidence;
        }
    }

    private JPanel createMatchRow(
            String icon,
            String item,
            String location,
            String score,
            Color scoreColor) {

        JPanel row = new JPanel(
                new BorderLayout(9, 0)
        );

        row.setBackground(CARD_2);

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(7, 9, 7, 9)
                )
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        17
                )
        );

        JPanel info = new JPanel();
        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel itemLabel = new JLabel(item);
        itemLabel.setForeground(TEXT);
        itemLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel locationLabel =
                new JLabel("⌖ " + location);

        locationLabel.setForeground(MUTED);
        locationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        info.add(itemLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(locationLabel);

        JLabel scoreLabel = new JLabel(score);
        scoreLabel.setForeground(scoreColor);
        scoreLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        row.add(
                iconLabel,
                BorderLayout.WEST
        );

        row.add(
                info,
                BorderLayout.CENTER
        );

        row.add(
                scoreLabel,
                BorderLayout.EAST
        );

        return row;
    }

    private void loadDashboardStats() {

        if (dashboardLostValue == null
                || dashboardFoundValue == null
                || dashboardReturnedValue == null) {
            return;
        }

        dashboardLostValue.setText("...");
        dashboardFoundValue.setText("...");
        dashboardReturnedValue.setText("...");

        String sql =
                "SELECT " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(report_type) = 'LOST') AS lost, " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(report_type) = 'FOUND') AS found, " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(status) = 'RETURNED') AS returned";

        new SwingWorker<Object[], Void>() {

            @Override
            protected Object[] doInBackground() throws Exception {

                Class.forName("com.mysql.cj.jdbc.Driver");

                try (
                        Connection connection =
                                DriverManager.getConnection(
                                        url,
                                        username,
                                        password
                                );
                        PreparedStatement statement =
                                connection.prepareStatement(sql);
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (result.next()) {
                        return new Object[]{
                                result.getInt("lost"),
                                result.getInt("found"),
                                result.getInt("returned")
                        };
                    }
                }

                return new Object[]{0, 0, 0};
            }

            @Override
            protected void done() {

                try {
                    Object[] values = get();

                    dashboardLostValue.setText(
                            String.valueOf(values[0])
                    );
                    dashboardFoundValue.setText(
                            String.valueOf(values[1])
                    );
                    dashboardReturnedValue.setText(
                            String.valueOf(values[2])
                    );

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();
                    if (cause == null) {
                        cause = ex;
                    }

                    dashboardLostValue.setText("-");
                    dashboardFoundValue.setText("-");
                    dashboardReturnedValue.setText("-");

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    private JPanel createDynamicStatCard(
            String icon,
            String title,
            JLabel valueLabel,
            Color accent) {

        JPanel card = new JPanel(new BorderLayout(12, 5));
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(14, 15, 14, 15)
                )
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 22)
        );

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatCard(
            String icon,
            String title,
            String value,
            Color accent) {

        JPanel card = new JPanel(new BorderLayout(12, 5));
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(14, 15, 14, 15)
                )
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 22)
        );

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRecentReportsCard() {

        JPanel card = createSectionCard("Recent Reports");

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(createReportRow("▤", "Laptop", "Library", "Verified"));
        list.add(Box.createVerticalStrut(9));
        list.add(createReportRow("▱", "Phone", "Canteen", "Matched"));
        list.add(Box.createVerticalStrut(9));
        list.add(createReportRow("♫", "Headphones", "Block A", "Pending"));

        card.add(list, BorderLayout.CENTER);

        return card;
    }

    private JPanel createReportRow(
            String icon,
            String item,
            String location,
            String status) {

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(9, 10, 9, 10)
                )
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(
                new Font("Segoe UI Emoji", Font.PLAIN, 18)
        );

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel itemLabel = new JLabel(item);
        itemLabel.setForeground(TEXT);
        itemLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel locationLabel = new JLabel("⌖ " + location);
        locationLabel.setForeground(MUTED);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        info.add(itemLabel);
        info.add(locationLabel);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setForeground(
                "Verified".equals(status)
                        ? GREEN
                        : "Matched".equals(status)
                        ? CYAN
                        : ORANGE
        );
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));

        row.add(iconLabel, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(statusLabel, BorderLayout.EAST);

        return row;
    }

    private JPanel createPendingActionsCard() {

        JPanel card = createSectionCard("Pending Actions");

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(createActionRow("△", "4 claims pending", ORANGE));
        list.add(Box.createVerticalStrut(12));
        list.add(createActionRow("△", "3 reports pending", ORANGE));
        list.add(Box.createVerticalStrut(12));
        list.add(createActionRow("✓", "2 matches found", GREEN));

        card.add(list, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActionRow(
            String icon,
            String text,
            Color color) {

        JPanel row = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                8,
                8
        ));

        row.setBackground(CARD_2);
        row.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(color);
        iconLabel.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 16)
        );

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(TEXT);
        textLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        row.add(iconLabel);
        row.add(textLabel);

        return row;
    }

    private JPanel createSectionCard(String title) {

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel label = new JLabel(title);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));

        card.add(label, BorderLayout.NORTH);

        return card;
    }

    // =========================================================
    // ITEM REPORTS
    // =========================================================

    private JPanel createItemReportsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 28, 20, 28));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JLabel info = new JLabel(
                "Manage Lost and Found reports from MySQL"
        );
        info.setForeground(MUTED);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton refresh = createActionButton(
                "↻ Refresh",
                BLUE
        );

        refresh.addActionListener(e -> loadReports());

        toolbar.add(info, BorderLayout.WEST);
        toolbar.add(refresh, BorderLayout.EAST);

        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Item Name",
                "Location",
                "Type",
                "Status",
                "Photo Path"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        reportsTable = new JTable(tableModel);

        // Keep the image path in the model for the photo viewer,
        // but do not expose the local path as a visible table column.
        reportsTable.removeColumn(
                reportsTable.getColumnModel().getColumn(5)
        );

        reportsTable.setRowHeight(34);
        reportsTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        reportsTable.setForeground(TEXT);
        reportsTable.setBackground(CARD);
        reportsTable.setSelectionBackground(
                new Color(48, 72, 118)
        );
        reportsTable.setSelectionForeground(Color.WHITE);
        reportsTable.setGridColor(BORDER);
        reportsTable.setShowVerticalLines(false);
        reportsTable.setIntercellSpacing(new Dimension(0, 1));
        reportsTable.setAutoCreateRowSorter(true);

        reportsTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        reportsTable.getTableHeader().setBackground(
                new Color(35, 48, 78)
        );
        reportsTable.getTableHeader().setForeground(TEXT);
        reportsTable.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        JScrollPane scrollPane = new JScrollPane(reportsTable);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );
        scrollPane.getViewport().setBackground(CARD);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0)
        );
        buttons.setOpaque(false);

        JButton verify = createActionButton(
                "✓ Verify Report",
                GREEN
        );

        JButton reject = createActionButton(
                "✕ Reject Report",
                RED
        );

        JButton claims = createActionButton(
                "▣ View Claims",
                PURPLE
        );

        JButton viewPhoto = createActionButton(
                "▧ View Report Photo",
                new Color(92, 112, 230)
        );

        viewPhoto.addActionListener(e -> viewSelectedFoundPhoto());

        verify.addActionListener(e -> verifySelectedReport());
        reject.addActionListener(e -> rejectSelectedReport());

        claims.addActionListener(e -> {

            AdminClaimsFrame frame =
                    new AdminClaimsFrame();

            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        });

        buttons.add(verify);
        buttons.add(reject);
        buttons.add(claims);
        buttons.add(viewPhoto);

        panel.add(buttons, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::loadReports);

        return panel;
    }

    // =========================================================
    // AI SCANNER & MATCHES
    // =========================================================

    private JPanel createAIScannerPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        panel.setOpaque(false);
        panel.setBorder(
                new EmptyBorder(
                        0,
                        28,
                        20,
                        28
                )
        );

        // -----------------------------------------------------
        // TOP INFORMATION
        // -----------------------------------------------------

        JPanel top =
                new JPanel(
                        new BorderLayout(12, 0)
                );

        top.setOpaque(false);

        JPanel text =
                new JPanel();

        text.setOpaque(false);
        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("AI Scanner");

        title.setForeground(TEXT);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JLabel description =
                new JLabel(
                        "Select a Found report and scan it against Lost reports."
                );

        description.setForeground(MUTED);
        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        JLabel note =
                new JLabel(
                        "Text-based matching: item similarity 80% + location similarity 20%."
                );

        note.setForeground(
                new Color(
                        150,
                        175,
                        220
                )
        );

        note.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(description);
        text.add(Box.createVerticalStrut(3));
        text.add(note);

        JButton refreshButton =
                createActionButton(
                        "↻ Refresh Found Reports",
                        BLUE
                );

        JButton scanButton =
                createActionButton(
                        "◉ Scan Selected Found Item",
                        PURPLE
                );

        JPanel topButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        topButtons.setOpaque(false);
        topButtons.add(refreshButton);
        topButtons.add(scanButton);

        top.add(
                text,
                BorderLayout.CENTER
        );

        top.add(
                topButtons,
                BorderLayout.EAST
        );

        panel.add(
                top,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // FOUND REPORT TABLE
        // -----------------------------------------------------

        String[] foundColumns = {
                "Report ID",
                "Item",
                "Location",
                "Status"
        };

        DefaultTableModel foundModel =
                new DefaultTableModel(
                        foundColumns,
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

        JTable foundTable =
                new JTable(foundModel);

        styleAdminTable(foundTable);

        foundTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane foundScroll =
                new JScrollPane(foundTable);

        foundScroll.setPreferredSize(
                new Dimension(0, 250)
        );

        foundTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        foundTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        foundTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        foundTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        foundScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        foundScroll.getViewport()
                .setBackground(CARD);

        JPanel foundCard =
                createSectionCard(
                        "Found Reports Available for AI Scan"
                );

        foundCard.add(
                foundScroll,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // MATCH RESULTS TABLE
        // -----------------------------------------------------

        String[] matchColumns = {
                "Lost Report ID",
                "Lost Item",
                "Lost Location",
                "Match Score",
                "Confidence"
        };

        DefaultTableModel matchModel =
                new DefaultTableModel(
                        matchColumns,
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

        JTable matchTable =
                new JTable(matchModel);

        styleAdminTable(matchTable);

        matchTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane matchScroll =
                new JScrollPane(matchTable);

        matchScroll.setPreferredSize(
                new Dimension(0, 250)
        );

        matchTable.getColumnModel().getColumn(0).setPreferredWidth(105);
        matchTable.getColumnModel().getColumn(1).setPreferredWidth(145);
        matchTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        matchTable.getColumnModel().getColumn(3).setPreferredWidth(105);
        matchTable.getColumnModel().getColumn(4).setPreferredWidth(105);

        matchScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        matchScroll.getViewport()
                .setBackground(CARD);

        JPanel resultsCard =
                createSectionCard(
                        "AI Match Results"
                );

        resultsCard.add(
                matchScroll,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // SCANNER ACTIONS
        // -----------------------------------------------------

        scanButton.addActionListener(
                e -> scanSelectedFoundReport(
                        foundTable,
                        foundModel,
                        matchModel
                )
        );

        refreshButton.addActionListener(
                e -> loadFoundReportsForScanner(
                        foundModel
                )
        );

        // -----------------------------------------------------
        // TOP: FOUND REPORTS + AI RESULTS
        // -----------------------------------------------------

        JPanel topTables =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                14,
                                0
                        )
                );

        topTables.setOpaque(false);
        topTables.setPreferredSize(
                new Dimension(0, 270)
        );

        topTables.add(foundCard);
        topTables.add(resultsCard);

        // -----------------------------------------------------
        // SIDE-BY-SIDE COMPARISON
        // -----------------------------------------------------

        JPanel comparisonCard =
                createSectionCard(
                        "⌕ Match Comparison"
                );

        comparisonCard.setPreferredSize(
                new Dimension(0, 440)
        );

        JPanel comparison =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                14,
                                0
                        )
                );

        comparison.setOpaque(false);

        JPanel lostPanel =
                createComparisonColumn(
                        "LOST REPORT",
                        BLUE
                );

        JPanel foundPanel =
                createComparisonColumn(
                        "FOUND REPORT",
                        GREEN
                );

        JLabel lostIdValue =
                createComparisonValue(
                        "-"
                );

        JLabel lostItemValue =
                createComparisonValue(
                        "-"
                );

        JLabel lostLocationValue =
                createComparisonValue(
                        "-"
                );

        JLabel lostDescriptionValue =
                createComparisonValue(
                        "-"
                );

        JLabel lostDetailsValue =
                createComparisonValue(
                        "-"
                );

        lostPanel.add(
                createComparisonRow(
                        "Report ID",
                        lostIdValue
                )
        );

        lostPanel.add(
                createComparisonRow(
                        "Item",
                        lostItemValue
                )
        );

        lostPanel.add(
                createComparisonRow(
                        "Location",
                        lostLocationValue
                )
        );

        lostPanel.add(
                createComparisonRow(
                        "Description",
                        lostDescriptionValue
                )
        );

        lostPanel.add(
                createComparisonRow(
                        "Identifying Details",
                        lostDetailsValue
                )
        );

        JLabel foundIdValue =
                createComparisonValue(
                        "-"
                );

        JLabel foundItemValue =
                createComparisonValue(
                        "-"
                );

        JLabel foundLocationValue =
                createComparisonValue(
                        "-"
                );

        JLabel foundDescriptionValue =
                createComparisonValue(
                        "-"
                );

        JLabel foundDetailsValue =
                createComparisonValue(
                        "-"
                );

        foundPanel.add(
                createComparisonRow(
                        "Report ID",
                        foundIdValue
                )
        );

        foundPanel.add(
                createComparisonRow(
                        "Item",
                        foundItemValue
                )
        );

        foundPanel.add(
                createComparisonRow(
                        "Location",
                        foundLocationValue
                )
        );

        foundPanel.add(
                createComparisonRow(
                        "Description",
                        foundDescriptionValue
                )
        );

        foundPanel.add(
                createComparisonRow(
                        "Identifying Details",
                        foundDetailsValue
                )
        );

        comparison.add(lostPanel);
        comparison.add(foundPanel);

        comparisonCard.add(
                comparison,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // MATCH SCORE + CONFIDENCE
        // -----------------------------------------------------

        JLabel scoreLabel =
                new JLabel(
                        "Match Score: -"
                );

        scoreLabel.setForeground(
                CYAN
        );

        scoreLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel confidenceLabel =
                new JLabel(
                        "Confidence: -"
                );

        confidenceLabel.setForeground(
                MUTED
        );

        confidenceLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JPanel scorePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                18,
                                4
                        )
                );

        scorePanel.setOpaque(false);
        scorePanel.add(scoreLabel);
        scorePanel.add(confidenceLabel);

        // -----------------------------------------------------
        // STEP 3: CASE ACTIONS
        // -----------------------------------------------------

        JButton viewCaseButton =
                createActionButton(
                        "⌕ View Full Case",
                        BLUE
                );

        JButton relatedClaimButton =
                createActionButton(
                        "▤ View Related Claim",
                        PURPLE
                );

        viewCaseButton.setEnabled(false);
        relatedClaimButton.setEnabled(false);

        JPanel caseActions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                4
                        )
                );

        caseActions.setOpaque(false);
        caseActions.add(viewCaseButton);
        caseActions.add(relatedClaimButton);

        JPanel comparisonFooter =
                new JPanel();

        comparisonFooter.setOpaque(false);
        comparisonFooter.setLayout(
                new BoxLayout(
                        comparisonFooter,
                        BoxLayout.Y_AXIS
                )
        );

        comparisonFooter.add(scorePanel);
        comparisonFooter.add(caseActions);

        comparisonCard.add(
                comparisonFooter,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // STEP 3 ACTIONS
        // -----------------------------------------------------

        viewCaseButton.addActionListener(
                e -> showFullAICaseDialog(
                        lostIdValue.getText(),
                        lostItemValue.getText(),
                        lostLocationValue.getText(),
                        lostDescriptionValue.getText(),
                        lostDetailsValue.getText(),
                        foundIdValue.getText(),
                        foundItemValue.getText(),
                        foundLocationValue.getText(),
                        foundDescriptionValue.getText(),
                        foundDetailsValue.getText(),
                        scoreLabel.getText(),
                        confidenceLabel.getText()
                )
        );

        relatedClaimButton.addActionListener(
                e -> showRelatedClaim(
                        foundIdValue.getText()
                )
        );

        // -----------------------------------------------------
        // WHEN A MATCH IS SELECTED
        // -----------------------------------------------------

        matchTable.getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (e.getValueIsAdjusting()) {
                                return;
                            }

                            int row =
                                    matchTable.getSelectedRow();

                            if (row == -1) {
                                return;
                            }

                            int modelRow =
                                    matchTable
                                            .convertRowIndexToModel(
                                                    row
                                            );

                            int lostReportId =
                                    Integer.parseInt(
                                            matchModel
                                                    .getValueAt(
                                                            modelRow,
                                                            0
                                                    )
                                                    .toString()
                                    );

                            String lostItem =
                                    String.valueOf(
                                            matchModel
                                                    .getValueAt(
                                                            modelRow,
                                                            1
                                                    )
                                    );

                            String lostLocation =
                                    String.valueOf(
                                            matchModel
                                                    .getValueAt(
                                                            modelRow,
                                                            2
                                                    )
                                    );

                            String matchScore =
                                    String.valueOf(
                                            matchModel
                                                    .getValueAt(
                                                            modelRow,
                                                            3
                                                    )
                                    );

                            String confidence =
                                    String.valueOf(
                                            matchModel
                                                    .getValueAt(
                                                            modelRow,
                                                            4
                                                    )
                                    );

                            lostIdValue.setText(
                                    String.valueOf(
                                            lostReportId
                                    )
                            );

                            lostItemValue.setText(
                                    lostItem
                            );

                            lostLocationValue.setText(
                                    lostLocation
                            );

                            scoreLabel.setText(
                                    "Match Score: "
                                            + matchScore
                            );

                            confidenceLabel.setText(
                                    "Confidence: "
                                            + confidence
                            );

                            loadComparisonDetails(
                                    lostReportId,
                                    foundTable,
                                    foundModel,
                                    lostDescriptionValue,
                                    lostDetailsValue,
                                    foundIdValue,
                                    foundItemValue,
                                    foundLocationValue,
                                    foundDescriptionValue,
                                    foundDetailsValue
                            );

                            viewCaseButton.setEnabled(true);
                            relatedClaimButton.setEnabled(true);
                        }
                );

        // -----------------------------------------------------
        // MAIN LAYOUT
        // -----------------------------------------------------

        JPanel center =
                new JPanel(
                        new BorderLayout(0, 14)
                );

        center.setOpaque(false);

        center.add(
                topTables,
                BorderLayout.NORTH
        );

        center.add(
                comparisonCard,
                BorderLayout.CENTER
        );

        panel.add(
                center,
                BorderLayout.CENTER
        );

        SwingUtilities.invokeLater(
                () -> loadFoundReportsForScanner(
                        foundModel
                )
        );

        return panel;
    }

    private JPanel createComparisonColumn(
            String title,
            Color accent
    ) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                CARD_2
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                accent
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel heading =
                new JLabel(
                        title
                );

        heading.setForeground(
                accent
        );

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        heading.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(heading);
        panel.add(
                Box.createVerticalStrut(7)
        );

        return panel;
    }

    private JPanel createComparisonRow(
            String label,
            JLabel value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        row.setBackground(
                CARD
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                7,
                                8,
                                7,
                                8
                        )
                )
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        90
                )
        );

        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelValue =
                new JLabel(
                        label
                );

        labelValue.setForeground(
                MUTED
        );

        labelValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        value.setForeground(
                TEXT
        );

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        row.add(
                labelValue,
                BorderLayout.NORTH
        );

        row.add(
                value,
                BorderLayout.CENTER
        );

        return row;
    }

    private JLabel createComparisonValue(
            String value
    ) {

        JLabel label =
                new JLabel(
                        "<html><div style='width:520px'>"
                                + escapeHtml(safeText(value))
                                + "</div></html>"
                );

        label.setForeground(
                TEXT
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        label.setVerticalAlignment(
                SwingConstants.TOP
        );

        return label;
    }

    private void loadComparisonDetails(
            int lostReportId,
            JTable foundTable,
            DefaultTableModel foundModel,
            JLabel lostDescriptionValue,
            JLabel lostDetailsValue,
            JLabel foundIdValue,
            JLabel foundItemValue,
            JLabel foundLocationValue,
            JLabel foundDescriptionValue,
            JLabel foundDetailsValue
    ) {

        int selectedFoundRow =
                foundTable.getSelectedRow();

        if (selectedFoundRow == -1) {
            return;
        }

        int foundModelRow =
                foundTable.convertRowIndexToModel(
                        selectedFoundRow
                );

        int foundReportId =
                Integer.parseInt(
                        foundModel
                                .getValueAt(
                                        foundModelRow,
                                        0
                                )
                                .toString()
                );

        String sql =
                "SELECT id, item_name, location, "
                        + "description, identifying_details "
                        + "FROM reports "
                        + "WHERE id IN (?, ?)";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    url,
                                    username,
                                    password
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setInt(
                        1,
                        lostReportId
                );

                statement.setInt(
                        2,
                        foundReportId
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        int id =
                                result.getInt(
                                        "id"
                                );

                        String item =
                                result.getString(
                                        "item_name"
                                );

                        String location =
                                result.getString(
                                        "location"
                                );

                        String description =
                                result.getString(
                                        "description"
                                );

                        String details =
                                result.getString(
                                        "identifying_details"
                                );

                        if (description == null) {
                            description = "-";
                        }

                        if (details == null) {
                            details = "-";
                        }

                        if (id == lostReportId) {

                            lostDescriptionValue.setText(
                                    "<html>"
                                            + description
                                            + "</html>"
                            );

                            lostDetailsValue.setText(
                                    "<html>"
                                            + details
                                            + "</html>"
                            );

                        } else if (id == foundReportId) {

                            foundIdValue.setText(
                                    String.valueOf(
                                            id
                                    )
                            );

                            foundItemValue.setText(
                                    item
                            );

                            foundLocationValue.setText(
                                    location
                            );

                            foundDescriptionValue.setText(
                                    "<html>"
                                            + description
                                            + "</html>"
                            );

                            foundDetailsValue.setText(
                                    "<html>"
                                            + details
                                            + "</html>"
                            );
                        }
                    }
                }
            }

        } catch (Exception ex) {

            showErrorDialog(
                    "Unable to load comparison details:\n"
                            + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    // =========================================================
    // STEP 3: FULL CASE VIEW - GALAXY THEME
    // =========================================================

    private void showFullAICaseDialog(
            String lostId,
            String lostItem,
            String lostLocation,
            String lostDescription,
            String lostDetails,
            String foundId,
            String foundItem,
            String foundLocation,
            String foundDescription,
            String foundDetails,
            String score,
            String confidence
    ) {

        if ("-".equals(lostId) ||
                "-".equals(foundId)) {

            showGalaxyInfoDialog(
                    "Please select an AI match first.",
                    "No Case Selected",
                    "◎"
            );

            return;
        }

        JDialog dialog =
                createGalaxyDialog(
                        "AI Case Review",
                        980,
                        720
                );

        JPanel content =
                new JPanel(new BorderLayout(0, 18));

        content.setBackground(BACKGROUND);
        content.setBorder(
                new EmptyBorder(20, 24, 20, 24)
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

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
                new JLabel("AI-ASSISTED CASE REVIEW");

        title.setForeground(TEXT);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Compare the Lost and Found reports before taking action."
                );

        subtitle.setForeground(MUTED);
        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JPanel scorePanel =
                new JPanel();

        scorePanel.setBackground(CARD_2);
        scorePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CYAN),
                        new EmptyBorder(9, 16, 9, 16)
                )
        );
        scorePanel.setLayout(
                new BoxLayout(
                        scorePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel scoreLabel =
                new JLabel(score);

        scoreLabel.setForeground(CYAN);
        scoreLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );
        scoreLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel confidenceLabel =
                new JLabel(confidence);

        confidenceLabel.setForeground(
                confidence.toUpperCase().contains("HIGH")
                        ? GREEN
                        : confidence.toUpperCase().contains("MEDIUM")
                        ? ORANGE
                        : RED
        );

        confidenceLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );
        confidenceLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        scorePanel.add(scoreLabel);
        scorePanel.add(
                Box.createVerticalStrut(2)
        );
        scorePanel.add(confidenceLabel);

        header.add(
                scorePanel,
                BorderLayout.EAST
        );

        content.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // TWO REPORT CARDS
        // =====================================================

        JPanel reports =
                new JPanel(
                        new GridLayout(1, 2, 16, 0)
                );

        reports.setOpaque(false);

        reports.add(
                createCaseReportCard(
                        "LOST REPORT",
                        BLUE,
                        lostId,
                        lostItem,
                        lostLocation,
                        lostDescription,
                        lostDetails
                )
        );

        reports.add(
                createCaseReportCard(
                        "FOUND REPORT",
                        GREEN,
                        foundId,
                        foundItem,
                        foundLocation,
                        foundDescription,
                        foundDetails
                )
        );

        content.add(
                reports,
                BorderLayout.CENTER
        );

        // =====================================================
        // FOOTER
        // =====================================================

        JPanel footer =
                new JPanel(
                        new BorderLayout(0, 10)
                );

        footer.setOpaque(false);

        JPanel note =
                new JPanel(new BorderLayout(8, 0));

        note.setBackground(
                new Color(25, 31, 58)
        );
        note.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 90, 150)
                        ),
                        new EmptyBorder(10, 13, 10, 13)
                )
        );

        JLabel noteIcon =
                new JLabel("◎");

        noteIcon.setForeground(CYAN);
        noteIcon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JLabel noteText =
                new JLabel(
                        "AI assistance only. Final ownership decisions are made by the administrator."
                );

        noteText.setForeground(MUTED);
        noteText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        note.add(noteIcon, BorderLayout.WEST);
        note.add(noteText, BorderLayout.CENTER);

        footer.add(
                note,
                BorderLayout.NORTH
        );

        JButton closeButton =
                createActionButton(
                        "Close",
                        new Color(70, 105, 255)
                );

        closeButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        footer.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        content.add(
                footer,
                BorderLayout.SOUTH
        );

        dialog.add(content);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createCaseReportCard(
            String heading,
            Color accent,
            String reportId,
            String item,
            String location,
            String description,
            String details
    ) {

        JPanel card =
                new JPanel(new BorderLayout(0, 12));

        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accent),
                        new EmptyBorder(16, 16, 16, 16)
                )
        );

        JLabel headingLabel =
                new JLabel(heading);

        headingLabel.setForeground(accent);
        headingLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        card.add(
                headingLabel,
                BorderLayout.NORTH
        );

        JPanel fields =
                new JPanel();

        fields.setOpaque(false);
        fields.setLayout(
                new BoxLayout(
                        fields,
                        BoxLayout.Y_AXIS
                )
        );

        addCaseField(
                fields,
                "Report ID",
                reportId
        );

        addCaseField(
                fields,
                "Item",
                item
        );

        addCaseField(
                fields,
                "Location",
                location
        );

        addCaseField(
                fields,
                "Description",
                stripHtml(description)
        );

        addCaseField(
                fields,
                "Identifying Details",
                stripHtml(details)
        );

        JScrollPane scroll =
                new JScrollPane(fields);

        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        styleScrollBar(
                scroll.getVerticalScrollBar()
        );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        return card;
    }

    private void addCaseField(
            JPanel panel,
            String label,
            String value
    ) {

        JLabel fieldLabel =
                new JLabel(label);

        fieldLabel.setForeground(MUTED);
        fieldLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JLabel fieldValue =
                new JLabel(
                        "<html><div style='width:360px'>"
                                + escapeHtml(safeText(value))
                                + "</div></html>"
                );

        fieldValue.setForeground(TEXT);
        fieldValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JPanel field =
                new JPanel();

        field.setOpaque(false);
        field.setLayout(
                new BoxLayout(
                        field,
                        BoxLayout.Y_AXIS
                )
        );

        field.add(fieldLabel);
        field.add(Box.createVerticalStrut(3));
        field.add(fieldValue);
        field.add(Box.createVerticalStrut(11));

        panel.add(field);
    }

    // =========================================================
    // STEP 3: RELATED CLAIM - GALAXY THEME
    // =========================================================

    private void showRelatedClaim(
            String foundReportIdText
    ) {

        if (foundReportIdText == null ||
                foundReportIdText.trim().isEmpty() ||
                "-".equals(foundReportIdText)) {

            showGalaxyInfoDialog(
                    "Please select an AI match first.",
                    "No Case Selected",
                    "◎"
            );

            return;
        }

        int reportId;

        try {
            reportId = Integer.parseInt(
                    foundReportIdText.trim()
            );
        } catch (NumberFormatException ex) {
            showGalaxyInfoDialog(
                    "Invalid Found report ID.",
                    "Invalid Report",
                    "△"
            );
            return;
        }

        String sql =
                "SELECT c.id AS claim_id, c.username, "
                        + "c.answer, c.status, r.item_name, r.location "
                        + "FROM claims c "
                        + "JOIN reports r ON c.report_id = r.id "
                        + "WHERE c.report_id = ? "
                        + "ORDER BY c.id DESC LIMIT 1";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    url,
                                    username,
                                    password
                            );
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setInt(
                        1,
                        reportId
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (!result.next()) {

                        showGalaxyInfoDialog(
                                "No claim has been submitted for this matched Found item yet.",
                                "No Related Claim",
                                "◎"
                        );

                        return;
                    }

                    showGalaxyClaimDialog(
                            result.getInt("claim_id"),
                            safeText(result.getString("username")),
                            safeText(result.getString("item_name")),
                            safeText(result.getString("location")),
                            safeText(result.getString("answer")),
                            safeText(result.getString("status"))
                    );
                }
            }

        } catch (Exception ex) {

            showErrorDialog(
                    "Unable to load the related claim:\n"
                            + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    private void showGalaxyClaimDialog(
            int claimId,
            String usernameText,
            String itemText,
            String locationText,
            String ownershipText,
            String statusText
    ) {

        JDialog dialog =
                createGalaxyDialog(
                        "Related Claim",
                        720,
                        590
                );

        JPanel root =
                new JPanel(new BorderLayout(0, 15));

        root.setBackground(BACKGROUND);
        root.setBorder(
                new EmptyBorder(20, 24, 20, 24)
        );

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

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
                        "CLAIM #" + claimId
                );

        title.setForeground(TEXT);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Student ownership claim details"
                );

        subtitle.setForeground(MUTED);
        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JLabel status =
                new JLabel(
                        statusText.toUpperCase()
                );

        status.setOpaque(true);
        status.setBackground(
                statusText.equalsIgnoreCase("approved")
                        ? new Color(35, 110, 80)
                        : statusText.equalsIgnoreCase("rejected")
                        ? new Color(125, 45, 60)
                        : new Color(125, 85, 35)
        );
        status.setForeground(Color.WHITE);
        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );
        status.setBorder(
                new EmptyBorder(7, 11, 7, 11)
        );

        header.add(
                status,
                BorderLayout.EAST
        );

        root.add(
                header,
                BorderLayout.NORTH
        );

        JPanel detailsCard =
                new JPanel();

        detailsCard.setBackground(CARD);
        detailsCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(75, 90, 150)
                        ),
                        new EmptyBorder(17, 18, 17, 18)
                )
        );
        detailsCard.setLayout(
                new BoxLayout(
                        detailsCard,
                        BoxLayout.Y_AXIS
                )
        );

        addClaimField(
                detailsCard,
                "USERNAME",
                usernameText
        );

        addClaimField(
                detailsCard,
                "ITEM",
                itemText
        );

        addClaimField(
                detailsCard,
                "LOCATION",
                locationText
        );

        JLabel ownershipLabel =
                new JLabel(
                        "OWNERSHIP DETAILS"
                );

        ownershipLabel.setForeground(CYAN);
        ownershipLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JTextArea ownershipArea =
                new JTextArea(ownershipText);

        ownershipArea.setEditable(false);
        ownershipArea.setLineWrap(true);
        ownershipArea.setWrapStyleWord(true);
        ownershipArea.setBackground(CARD_2);
        ownershipArea.setForeground(TEXT);
        ownershipArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );
        ownershipArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(70, 105, 170)
                        ),
                        new EmptyBorder(10, 11, 10, 11)
                )
        );

        JScrollPane ownershipScroll =
                new JScrollPane(ownershipArea);

        ownershipScroll.setPreferredSize(
                new Dimension(600, 110)
        );
        ownershipScroll.setBorder(null);
        ownershipScroll.getViewport().setBackground(CARD_2);
        styleScrollBar(
                ownershipScroll.getVerticalScrollBar()
        );

        detailsCard.add(ownershipLabel);
        detailsCard.add(Box.createVerticalStrut(6));
        detailsCard.add(ownershipScroll);

        root.add(
                detailsCard,
                BorderLayout.CENTER
        );

        JButton closeButton =
                createActionButton(
                        "Close",
                        new Color(70, 105, 255)
                );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        root.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.add(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addClaimField(
            JPanel parent,
            String label,
            String value
    ) {

        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setForeground(MUTED);
        labelComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        9
                )
        );

        JLabel valueComponent =
                new JLabel(
                        safeText(value)
                );

        valueComponent.setForeground(TEXT);
        valueComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        parent.add(labelComponent);
        parent.add(Box.createVerticalStrut(3));
        parent.add(valueComponent);
        parent.add(Box.createVerticalStrut(12));
    }


    private String stripHtml(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {
            return "-";
        }

        return value
                .replaceAll("<[^>]*>", "")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&");
    }

    // =========================================================
    // TABLE STYLING
    // =========================================================

    private void styleAdminTable(
            JTable table
    ) {

        table.setRowHeight(34);
        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setSelectionBackground(
                new Color(
                        48,
                        72,
                        118
                )
        );
        table.setSelectionForeground(
                Color.WHITE
        );
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(
                new Dimension(
                        0,
                        1
                )
        );
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        table.getTableHeader().setBackground(
                new Color(
                        35,
                        48,
                        78
                )
        );

        table.getTableHeader().setForeground(
                TEXT
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );
    }

    private void loadFoundReportsForScanner(
            DefaultTableModel model
    ) {

        model.setRowCount(0);

        String sql =
                "SELECT id, item_name, location, status "
                        + "FROM reports "
                        + "WHERE LOWER(report_type) = 'found' "
                        + "AND (status IS NULL "
                        + "OR LOWER(status) <> 'returned') "
                        + "ORDER BY id DESC";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
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

                    ResultSet result =
                            statement.executeQuery()
            ) {

                while (result.next()) {

                    model.addRow(
                            new Object[]{
                                    result.getInt(
                                            "id"
                                    ),
                                    result.getString(
                                            "item_name"
                                    ),
                                    result.getString(
                                            "location"
                                    ),
                                    result.getString(
                                            "status"
                                    )
                            }
                    );
                }

            }

        } catch (Exception ex) {

            showErrorDialog(
                    "Unable to load Found reports:\n"
                            + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    private void scanSelectedFoundReport(
            JTable foundTable,
            DefaultTableModel foundModel,
            DefaultTableModel matchModel
    ) {

        int selectedRow =
                foundTable.getSelectedRow();

        if (selectedRow == -1) {

            showInfoDialog(
                    "Please select a Found report first.",
                    "No Found Report Selected"
            );

            return;
        }

        int modelRow =
                foundTable.convertRowIndexToModel(
                        selectedRow
                );

        int foundReportId =
                Integer.parseInt(
                        foundModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        matchModel.setRowCount(0);

        // Run the AI Scanner in a background thread so the
        // Swing Event Dispatch Thread is not blocked while
        // MySQL data is read and matching is performed.
        new SwingWorker<List<AIScannerService.MatchResult>, Void>() {

            @Override
            protected List<AIScannerService.MatchResult> doInBackground()
                    throws Exception {

                AIScannerService scanner =
                        new AIScannerService();

                return scanner.scanFoundReport(
                        foundReportId
                );
            }

            @Override
            protected void done() {

                try {

                    List<AIScannerService.MatchResult> matches =
                            get();

                    for (
                            AIScannerService.MatchResult match
                            : matches
                    ) {

                        matchModel.addRow(
                                new Object[]{
                                        match.getLostReportId(),
                                        match.getLostItem(),
                                        match.getLostLocation(),
                                        String.format(
                                                "%.0f%%",
                                                match.getMatchScore()
                                        ),
                                        match.getConfidence()
                                }
                        );
                    }

                    if (matches.isEmpty()) {

                        showInfoDialog(
                                "No possible Lost-item matches "
                                        + "were found for this Found report.",
                                "AI Scanner"
                        );

                    } else {

                        showSuccessDialog(
                                matches.size()
                                        + " possible match"
                                        + (matches.size() == 1
                                        ? ""
                                        : "es")
                                        + " found."
                        );
                    }

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    showErrorDialog(
                            "AI Scanner error:\n"
                                    + cause.getMessage()
                    );

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    // =========================================================
    // SIMPLE FUTURE PANELS
    // =========================================================

    private JPanel createAdminProfilePanel() {

        GalaxyPanel panel = new GalaxyPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 28, 20, 28));

        ProfileScrollPanel mainContent = new ProfileScrollPanel();
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        // =====================================================
        // PROFILE HERO CARD - SAME STYLE AS STUDENT PROFILE
        // =====================================================

        JPanel profileCard = new JPanel(new BorderLayout(25, 0));
        profileCard.setBackground(CARD);
        profileCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CYAN),
                        new EmptyBorder(25, 30, 25, 30)
                )
        );
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 190)
        );

        JPanel avatar = new JPanel(new GridBagLayout());
        avatar.setOpaque(true);
        avatar.setBackground(PURPLE);
        avatar.setPreferredSize(new Dimension(125, 125));
        avatar.setMaximumSize(new Dimension(125, 125));

        JLabel avatarLetter = new JLabel("A");
        avatarLetter.setForeground(Color.WHITE);
        avatarLetter.setFont(new Font("Segoe UI", Font.BOLD, 52));
        avatar.add(avatarLetter);

        profileCard.add(avatar, BorderLayout.WEST);

        JPanel profileInfo = new JPanel();
        profileInfo.setOpaque(false);
        profileInfo.setLayout(new BoxLayout(profileInfo, BoxLayout.Y_AXIS));

        JLabel accountType = new JLabel("ADMINISTRATOR ACCOUNT");
        accountType.setForeground(CYAN);
        accountType.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel adminName = new JLabel(adminUsername);
        adminName.setForeground(TEXT);
        adminName.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel usernameLabel = new JLabel("@" + adminUsername);
        usernameLabel.setForeground(MUTED);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        roleRow.setOpaque(false);
        roleRow.add(createProfileBadge("ADMINISTRATOR", PURPLE));

        profileInfo.add(accountType);
        profileInfo.add(Box.createVerticalStrut(7));
        profileInfo.add(adminName);
        profileInfo.add(Box.createVerticalStrut(4));
        profileInfo.add(usernameLabel);
        profileInfo.add(Box.createVerticalStrut(13));
        profileInfo.add(roleRow);

        profileCard.add(profileInfo, BorderLayout.CENTER);

        mainContent.add(profileCard);
        mainContent.add(Box.createVerticalStrut(22));

        // =====================================================
        // ACCOUNT INFORMATION
        // =====================================================

        JPanel accountCard = createAdminSectionCard();

        JPanel accountContent = new JPanel(
                new GridLayout(2, 2, 25, 20)
        );
        accountContent.setOpaque(false);

        JLabel fullNameValue = new JLabel(adminUsername);
        JLabel roleValue = new JLabel("ADMINISTRATOR");
        JLabel statusValue = new JLabel("Active");

        loadAdminProfileData(fullNameValue, roleValue);

        accountContent.add(
                createAdminInfoBlock("FULL NAME", fullNameValue, "●")
        );
        accountContent.add(
                createAdminInfoBlock(
                        "USERNAME",
                        new JLabel(adminUsername),
                        "@"
                )
        );
        accountContent.add(
                createAdminInfoBlock("ACCOUNT ROLE", roleValue, "◆")
        );
        accountContent.add(
                createAdminStatusBlock(statusValue)
        );

        accountCard.add(accountContent, BorderLayout.CENTER);
        mainContent.add(accountCard);
        mainContent.add(Box.createVerticalStrut(20));

        // =====================================================
        // ADMIN ACTIVITY
        // =====================================================

        JPanel activityCard = createAdminSectionCard();
        activityCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel activityPanel = new JPanel(
                new GridLayout(1, 3, 15, 0)
        );
        activityPanel.setOpaque(false);

        JLabel reportsLabel = new JLabel("0");
        JLabel claimsLabel = new JLabel("0");
        JLabel returnedLabel = new JLabel("0");

        activityPanel.add(
                createAdminActivityCard(
                        "▤",
                        "Total Reports",
                        reportsLabel,
                        CYAN
                )
        );
        activityPanel.add(
                createAdminActivityCard(
                        "✓",
                        "Total Claims",
                        claimsLabel,
                        PURPLE
                )
        );
        activityPanel.add(
                createAdminActivityCard(
                        "↗",
                        "Returned Items",
                        returnedLabel,
                        GREEN
                )
        );

        activityCard.add(activityPanel, BorderLayout.CENTER);
        mainContent.add(activityCard);
        mainContent.add(Box.createVerticalStrut(20));

        // =====================================================
        // ADMIN ACCESS
        // =====================================================

        JPanel accessCard = createAdminSectionCard();
        accessCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel accessContent = new JPanel(
                new GridLayout(1, 3, 15, 0)
        );
        accessContent.setOpaque(false);

        accessContent.add(
                createAdminAccessBlock(
                        "ADMIN ACCESS",
                        "Full Admin Access",
                        "◆",
                        BLUE
                )
        );
        accessContent.add(
                createAdminAccessBlock(
                        "AI TOOLS",
                        "Enabled",
                        "✦",
                        CYAN
                )
        );
        accessContent.add(
                createAdminAccessBlock(
                        "CLAIM REVIEW",
                        "Enabled",
                        "✓",
                        GREEN
                )
        );

        accessCard.add(accessContent, BorderLayout.CENTER);
        mainContent.add(accessCard);

        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getVerticalScrollBar().setPreferredSize(
                new Dimension(10, 0)
        );
        scrollPane.getVerticalScrollBar().setUI(
                createDarkScrollBarUI()
        );

        panel.add(scrollPane, BorderLayout.CENTER);

        // Load database statistics without freezing the dashboard.
        new SwingWorker<int[], Void>() {
            @Override
            protected int[] doInBackground() throws Exception {

                Class.forName("com.mysql.cj.jdbc.Driver");

                String sql =
                        "SELECT " +
                                "(SELECT COUNT(*) FROM reports) AS reports, " +
                                "(SELECT COUNT(*) FROM claims) AS claims, " +
                                "(SELECT COUNT(*) FROM reports " +
                                "WHERE UPPER(status) = 'RETURNED') AS returned_items";

                try (
                        Connection connection = DriverManager.getConnection(
                                url,
                                username,
                                password
                        );
                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery(sql)
                ) {
                    if (result.next()) {
                        return new int[]{
                                result.getInt("reports"),
                                result.getInt("claims"),
                                result.getInt("returned_items")
                        };
                    }
                }

                return new int[]{0, 0, 0};
            }

            @Override
            protected void done() {
                try {
                    int[] values = get();
                    reportsLabel.setText(String.valueOf(values[0]));
                    claimsLabel.setText(String.valueOf(values[1]));
                    returnedLabel.setText(String.valueOf(values[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

        return panel;
    }

    private static class ProfileScrollPanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle visibleRect,
                int orientation,
                int direction) {
            return 18;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle visibleRect,
                int orientation,
                int direction) {
            return Math.max(50,
                    orientation == SwingConstants.VERTICAL
                            ? visibleRect.height - 30
                            : visibleRect.width - 30);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private JPanel createAdminSectionCard() {

        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(25, 28, 25, 28)
                )
        );
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));

        return card;
    }

    private JPanel createAdminInfoBlock(
            String title,
            JLabel value,
            String icon) {

        JPanel block = new JPanel(new BorderLayout(12, 0));
        block.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(CYAN);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(38, 38));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));

        value.setForeground(TEXT);
        value.setFont(new Font("Segoe UI", Font.BOLD, 16));

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(6));
        text.add(value);

        block.add(iconLabel, BorderLayout.WEST);
        block.add(text, BorderLayout.CENTER);

        return block;
    }

    private JPanel createAdminStatusBlock(JLabel statusValue) {

        JPanel block = createAdminInfoBlock(
                "ACCOUNT STATUS",
                statusValue,
                "●"
        );

        statusValue.setForeground(GREEN);
        return block;
    }

    private JPanel createAdminActivityCard(
            String icon,
            String title,
            JLabel value,
            Color accent) {

        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(new Color(10, 20, 55));
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(18, 15, 18, 15)
                )
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        value.setForeground(accent);
        value.setFont(new Font("Segoe UI", Font.BOLD, 26));
        value.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createAdminAccessBlock(
            String title,
            String value,
            String icon,
            Color accent) {

        JPanel block = new JPanel();
        block.setBackground(CARD_2);
        block.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(16, 15, 16, 15)
                )
        );
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 22));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        block.add(iconLabel);
        block.add(Box.createVerticalStrut(7));
        block.add(titleLabel);
        block.add(Box.createVerticalStrut(5));
        block.add(valueLabel);

        return block;
    }

    private JLabel createProfileBadge(String text, Color color) {

        JLabel badge = new JLabel(text);
        badge.setOpaque(true);
        badge.setBackground(color);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(8, 18, 8, 18));

        return badge;
    }

    private void loadAdminProfileData(
            JLabel fullNameLabel,
            JLabel roleLabel) {

        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() throws Exception {

                String sql =
                        "SELECT full_name, role FROM users " +
                                "WHERE username = ?";

                Class.forName("com.mysql.cj.jdbc.Driver");

                try (
                        Connection connection = DriverManager.getConnection(
                                url,
                                username,
                                password
                        );
                        PreparedStatement statement =
                                connection.prepareStatement(sql)
                ) {
                    statement.setString(1, adminUsername);

                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            String fullName = result.getString("full_name");
                            String role = result.getString("role");

                            if (fullName == null || fullName.trim().isEmpty()) {
                                fullName = adminUsername;
                            }
                            if (role == null || role.trim().isEmpty()) {
                                role = "ADMINISTRATOR";
                            }

                            return new String[]{fullName, role.toUpperCase()};
                        }
                    }
                }

                return new String[]{adminUsername, "ADMINISTRATOR"};
            }

            @Override
            protected void done() {
                try {
                    String[] data = get();
                    fullNameLabel.setText(data[0]);
                    roleLabel.setText(data[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    // =========================================================
    // PENDING VERIFICATION
    // =========================================================

    private JPanel createPendingVerificationPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);
        panel.setBorder(
                new EmptyBorder(0, 28, 20, 28)
        );

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JLabel info = new JLabel(
                "Reports currently waiting for administrator verification."
        );
        info.setForeground(MUTED);
        info.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(BLUE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        refreshButton.setBorder(
                new EmptyBorder(10, 16, 10, 16)
        );

        toolbar.add(info, BorderLayout.WEST);
        toolbar.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "Report ID",
                "Item",
                "Location",
                "Type",
                "Status"
        };

        DefaultTableModel pendingModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable pendingTable = new JTable(pendingModel);

        pendingTable.setRowHeight(38);
        pendingTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        pendingTable.setForeground(TEXT);
        pendingTable.setBackground(CARD);
        pendingTable.setSelectionBackground(
                new Color(48, 72, 118)
        );
        pendingTable.setSelectionForeground(Color.WHITE);
        pendingTable.setGridColor(BORDER);
        pendingTable.setShowVerticalLines(false);
        pendingTable.setAutoCreateRowSorter(true);

        pendingTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        pendingTable.getTableHeader().setBackground(
                new Color(35, 48, 78)
        );
        pendingTable.getTableHeader().setForeground(TEXT);
        pendingTable.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );

        JScrollPane scrollPane =
                new JScrollPane(pendingTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );
        scrollPane.getViewport().setBackground(CARD);

        // ---------------------------------------------------------
        // Mouse-wheel scrolling fix
        // ---------------------------------------------------------
        // The Pending Verification table is inside its own
        // JScrollPane, which itself is inside the Admin Dashboard's
        // main content JScrollPane. When the inner table has nothing
        // left to scroll, forward the mouse-wheel movement to the
        // outer dashboard scrollbar so the page can continue moving.
        java.awt.event.MouseWheelListener pendingWheelListener = e -> {

            JScrollPane outerScroll =
                    (JScrollPane) SwingUtilities.getAncestorOfClass(
                            JScrollPane.class,
                            panel
                    );

            if (outerScroll == null) {
                return;
            }

            JScrollBar innerBar =
                    scrollPane.getVerticalScrollBar();

            int innerValue = innerBar.getValue();
            int innerMax =
                    innerBar.getMaximum()
                            - innerBar.getVisibleAmount();

            boolean scrollingDown =
                    e.getWheelRotation() > 0;

            boolean scrollingUp =
                    e.getWheelRotation() < 0;

            boolean innerCanScrollDown =
                    innerValue < innerMax;

            boolean innerCanScrollUp =
                    innerValue > innerBar.getMinimum();

            // If the inner table can still scroll, let its normal
            // JScrollPane behaviour handle the wheel event.
            if ((scrollingDown && innerCanScrollDown)
                    || (scrollingUp && innerCanScrollUp)) {
                return;
            }

            // Otherwise move the main Admin Dashboard page.
            JScrollBar outerBar =
                    outerScroll.getVerticalScrollBar();

            int amount =
                    e.getUnitsToScroll()
                            * outerBar.getUnitIncrement();

            int newValue =
                    outerBar.getValue()
                            + (scrollingDown ? amount : -amount);

            newValue = Math.max(
                    outerBar.getMinimum(),
                    Math.min(
                            newValue,
                            outerBar.getMaximum()
                                    - outerBar.getVisibleAmount()
                    )
            );

            outerBar.setValue(newValue);
            e.consume();
        };

        pendingTable.addMouseWheelListener(
                pendingWheelListener
        );

        scrollPane.addMouseWheelListener(
                pendingWheelListener
        );

        JButton verifyButton =
                new JButton("✓ Verify Selected");

        JButton rejectButton =
                new JButton("✕ Reject Selected");

        for (JButton button :
                new JButton[]{verifyButton, rejectButton}) {

            button.setFont(
                    new Font("Segoe UI", Font.BOLD, 12)
            );
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );
            button.setBorder(
                    new EmptyBorder(10, 16, 10, 16)
            );
        }

        verifyButton.setBackground(GREEN);
        rejectButton.setBackground(RED);

        JPanel actions = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        0
                )
        );
        actions.setOpaque(false);

        actions.add(verifyButton);
        actions.add(rejectButton);

        JPanel bottom = new JPanel(
                new BorderLayout()
        );
        bottom.setOpaque(false);
        bottom.add(actions, BorderLayout.WEST);

        verifyButton.addActionListener(e -> {

            int selectedRow =
                    pendingTable.getSelectedRow();

            if (selectedRow == -1) {
                showInfoDialog(
                        "Please select a pending report first.",
                        "No Report Selected"
                );
                return;
            }

            int modelRow =
                    pendingTable.convertRowIndexToModel(
                            selectedRow
                    );

            int reportId =
                    Integer.parseInt(
                            pendingModel
                                    .getValueAt(modelRow, 0)
                                    .toString()
                    );

            updateStatus(
                    reportId,
                    "Verified"
            );

            loadPendingVerification(
                    pendingModel
            );
        });

        rejectButton.addActionListener(e -> {

            int selectedRow =
                    pendingTable.getSelectedRow();

            if (selectedRow == -1) {
                showInfoDialog(
                        "Please select a pending report first.",
                        "No Report Selected"
                );
                return;
            }

            int modelRow =
                    pendingTable.convertRowIndexToModel(
                            selectedRow
                    );

            int reportId =
                    Integer.parseInt(
                            pendingModel
                                    .getValueAt(modelRow, 0)
                                    .toString()
                    );

            updateStatus(
                    reportId,
                    "Rejected"
            );

            loadPendingVerification(
                    pendingModel
            );
        });

        refreshButton.addActionListener(
                e -> loadPendingVerification(pendingModel)
        );

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        loadPendingVerification(pendingModel);

        return panel;
    }

    private void loadPendingVerification(
            DefaultTableModel model) {

        model.setRowCount(0);

        String sql =
                "SELECT id, item_name, location, " +
                        "report_type, status, image_path " +
                        "FROM reports " +
                        "WHERE LOWER(COALESCE(status, 'Pending')) = 'pending' " +
                        "ORDER BY id DESC";

        new SwingWorker<List<Object[]>, Void>() {

            @Override
            protected List<Object[]> doInBackground()
                    throws Exception {

                List<Object[]> rows =
                        new java.util.ArrayList<>();

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                try (
                        Connection connection =
                                DriverManager.getConnection(
                                        url,
                                        username,
                                        password
                                );

                        PreparedStatement statement =
                                connection.prepareStatement(sql);

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        rows.add(
                                new Object[]{
                                        result.getInt("id"),
                                        result.getString("item_name"),
                                        result.getString("location"),
                                        result.getString("report_type"),
                                        result.getString("status")
                                }
                        );
                    }
                }

                return rows;
            }

            @Override
            protected void done() {

                try {

                    List<Object[]> rows = get();

                    model.setRowCount(0);

                    for (Object[] row : rows) {
                        model.addRow(row);
                    }

                } catch (Exception ex) {

                    Throwable cause =
                            ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    showErrorDialog(
                            "Unable to load pending verification reports:\n"
                                    + cause.getMessage()
                    );

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    // =========================================================
    // NOTIFICATIONS
    // =========================================================

    private JPanel createNotificationsPanel() {

        JPanel panel = new JPanel(
                new BorderLayout(0, 18)
        );
        panel.setOpaque(false);
        panel.setBorder(
                new EmptyBorder(0, 28, 20, 28)
        );

        JPanel top = new JPanel(
                new BorderLayout(12, 8)
        );
        top.setOpaque(false);

        JPanel titleArea = new JPanel(
                new BorderLayout(0, 4)
        );
        titleArea.setOpaque(false);

        JLabel info = new JLabel(
                "Recent system notifications generated by claims and report activity."
        );
        info.setForeground(MUTED);
        info.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        JLabel unreadLabel = new JLabel(
                "Unread: 0"
        );
        unreadLabel.setForeground(
                new Color(120, 200, 255)
        );
        unreadLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        titleArea.add(info, BorderLayout.NORTH);
        titleArea.add(unreadLabel, BorderLayout.CENTER);

        JButton refreshButton = new JButton("↻ Refresh");
        refreshButton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(BLUE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        refreshButton.setBorder(
                new EmptyBorder(10, 16, 10, 16)
        );

        top.add(titleArea, BorderLayout.CENTER);
        top.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "ID",
                "Username",
                "Message",
                "Status",
                "Created At"
        };

        DefaultTableModel notificationModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable notificationTable =
                new JTable(notificationModel);

        notificationTable.setRowHeight(42);
        notificationTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        notificationTable.setForeground(TEXT);
        notificationTable.setBackground(CARD);
        notificationTable.setSelectionBackground(
                new Color(48, 72, 118)
        );
        notificationTable.setSelectionForeground(Color.WHITE);
        notificationTable.setGridColor(BORDER);
        notificationTable.setShowVerticalLines(false);
        notificationTable.setAutoCreateRowSorter(true);

        notificationTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        notificationTable.getTableHeader().setBackground(
                new Color(35, 48, 78)
        );
        notificationTable.getTableHeader().setForeground(TEXT);
        notificationTable.getTableHeader().setPreferredSize(
                new Dimension(0, 40)
        );

        notificationTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(520);

        JScrollPane scrollPane =
                new JScrollPane(notificationTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        refreshButton.addActionListener(
                e -> loadAdminNotifications(
                        notificationModel,
                        unreadLabel
                )
        );

        loadAdminNotifications(
                notificationModel,
                unreadLabel
        );

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadAdminNotifications(
            DefaultTableModel model,
            JLabel unreadLabel) {

        model.setRowCount(0);

        String sql =
                "SELECT id, username, message, status, created_at " +
                        "FROM notifications " +
                        "ORDER BY created_at DESC";

        new SwingWorker<java.util.List<Object[]>, Void>() {

            @Override
            protected java.util.List<Object[]>
            doInBackground() throws Exception {

                java.util.List<Object[]> rows =
                        new java.util.ArrayList<>();

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                try (
                        Connection connection =
                                DriverManager.getConnection(
                                        url,
                                        username,
                                        password
                                );

                        PreparedStatement statement =
                                connection.prepareStatement(sql);

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        rows.add(
                                new Object[]{
                                        result.getInt("id"),
                                        result.getString("username"),
                                        result.getString("message"),
                                        result.getString("status"),
                                        result.getTimestamp("created_at")
                                }
                        );
                    }
                }

                return rows;
            }

            @Override
            protected void done() {

                try {

                    java.util.List<Object[]> rows = get();

                    model.setRowCount(0);

                    int unreadCount = 0;

                    for (Object[] row : rows) {

                        model.addRow(row);

                        if (row[3] != null
                                && "UNREAD".equalsIgnoreCase(
                                row[3].toString()
                        )) {
                            unreadCount++;
                        }
                    }

                    unreadLabel.setText(
                            "Unread: " + unreadCount
                    );

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    showErrorDialog(
                            "Unable to load notifications:\n"
                                    + cause.getMessage()
                    );

                    cause.printStackTrace();
                }
            }
        }.execute();
    }


    // =========================================================
    // STUDENTS
    // =========================================================

    private JPanel createStudentsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 28, 20, 28));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JLabel info = new JLabel(
                "Registered student accounts from MySQL"
        );
        info.setForeground(MUTED);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton refreshButton = createActionButton(
                "↻ Refresh",
                BLUE
        );

        toolbar.add(info, BorderLayout.WEST);
        toolbar.add(refreshButton, BorderLayout.EAST);

        String[] columns = {
                "ID",
                "Username",
                "Role"
        };

        DefaultTableModel studentModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable studentTable =
                new JTable(studentModel);

        styleAdminTable(studentTable);
        studentTable.setRowHeight(34);

        studentTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(80);

        studentTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(250);

        studentTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(140);

        JScrollPane scrollPane =
                new JScrollPane(studentTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scrollPane.getViewport()
                .setBackground(CARD);

        refreshButton.addActionListener(
                e -> loadStudents(studentModel)
        );

        panel.add(
                toolbar,
                BorderLayout.NORTH
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        SwingUtilities.invokeLater(
                () -> loadStudents(studentModel)
        );

        return panel;
    }

    private void loadStudents(
            DefaultTableModel model) {

        String sql =
                "SELECT id, username, role " +
                        "FROM users " +
                        "WHERE UPPER(role) = 'STUDENT' " +
                        "ORDER BY id DESC";

        new SwingWorker<List<Object[]>, Void>() {

            @Override
            protected List<Object[]> doInBackground()
                    throws Exception {

                List<Object[]> rows =
                        new java.util.ArrayList<>();

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                try (
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

                        ResultSet result =
                                statement.executeQuery()
                ) {

                    while (result.next()) {

                        rows.add(
                                new Object[]{
                                        result.getInt("id"),
                                        result.getString("username"),
                                        result.getString("role")
                                }
                        );
                    }
                }

                return rows;
            }

            @Override
            protected void done() {

                try {

                    List<Object[]> rows = get();

                    model.setRowCount(0);

                    for (Object[] row : rows) {
                        model.addRow(row);
                    }

                } catch (Exception ex) {

                    Throwable cause =
                            ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    showErrorDialog(
                            "Unable to load students:\n"
                                    + cause.getMessage()
                    );

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    // =========================================================
    // ANALYTICS
    // =========================================================

    private JPanel createAnalyticsPanel() {

        GalaxyPanel panel = new GalaxyPanel();
        panel.setLayout(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(0, 28, 20, 28));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel info = new JLabel(
                "Live system statistics from MySQL"
        );
        info.setForeground(MUTED);
        info.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        JButton refreshButton = createActionButton(
                "↻ Refresh",
                BLUE
        );

        top.add(info, BorderLayout.WEST);
        top.add(refreshButton, BorderLayout.EAST);

        JPanel stats = new JPanel(
                new GridLayout(2, 3, 12, 12)
        );
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(1000, 350));

        JPanel statsHolder = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );
        statsHolder.setOpaque(false);

        JLabel totalStudents = createAnalyticsValueLabel();
        JLabel totalReports = createAnalyticsValueLabel();
        JLabel lostReports = createAnalyticsValueLabel();
        JLabel foundReports = createAnalyticsValueLabel();
        JLabel totalClaims = createAnalyticsValueLabel();
        JLabel returnedReports = createAnalyticsValueLabel();

        stats.add(createAnalyticsCard(
                "♧",
                "Students",
                totalStudents,
                CYAN
        ));

        stats.add(createAnalyticsCard(
                "▤",
                "Total Reports",
                totalReports,
                BLUE
        ));

        stats.add(createAnalyticsCard(
                "↘",
                "Lost Reports",
                lostReports,
                ORANGE
        ));

        stats.add(createAnalyticsCard(
                "↗",
                "Found Reports",
                foundReports,
                GREEN
        ));

        stats.add(createAnalyticsCard(
                "✓",
                "Claims",
                totalClaims,
                PURPLE
        ));

        stats.add(createAnalyticsCard(
                "★",
                "Returned Items",
                returnedReports,
                CYAN
        ));

        JPanel statusCard = new JPanel(
                new BorderLayout(0, 10)
        );
        statusCard.setBackground(CARD);
        statusCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(16, 18, 16, 18)
                )
        );

        JLabel statusTitle = new JLabel(
                "Report Status Overview"
        );
        statusTitle.setForeground(TEXT);
        statusTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        JLabel statusLabel = new JLabel(
                "Loading..."
        );
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        statusCard.add(statusTitle, BorderLayout.NORTH);
        statusCard.add(statusLabel, BorderLayout.CENTER);

        statsHolder.add(stats);

        panel.add(top, BorderLayout.NORTH);
        panel.add(statsHolder, BorderLayout.CENTER);
        panel.add(statusCard, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> loadAnalytics(
                        totalStudents,
                        totalReports,
                        lostReports,
                        foundReports,
                        totalClaims,
                        returnedReports,
                        statusLabel
                )
        );

        SwingUtilities.invokeLater(
                () -> loadAnalytics(
                        totalStudents,
                        totalReports,
                        lostReports,
                        foundReports,
                        totalClaims,
                        returnedReports,
                        statusLabel
                )
        );

        return panel;
    }

    private JLabel createAnalyticsValueLabel() {

        JLabel label = new JLabel("-", SwingConstants.CENTER);
        label.setForeground(TEXT);
        label.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        return label;
    }

    private JPanel createAnalyticsCard(
            String iconText,
            String title,
            JLabel value,
            Color accent) {

        JPanel card = new JPanel(
                new BorderLayout(0, 8)
        );
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JLabel icon = new JLabel(
                iconText,
                SwingConstants.CENTER
        );
        icon.setForeground(accent);
        icon.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 18)
        );

        JLabel titleLabel = new JLabel(
                title,
                SwingConstants.CENTER
        );
        titleLabel.setForeground(MUTED);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        card.add(icon, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    private void loadAnalytics(
            JLabel totalStudents,
            JLabel totalReports,
            JLabel lostReports,
            JLabel foundReports,
            JLabel totalClaims,
            JLabel returnedReports,
            JLabel statusLabel) {

        String sql =
                "SELECT " +
                        "(SELECT COUNT(*) FROM users WHERE UPPER(role) = 'STUDENT') AS students, " +
                        "(SELECT COUNT(*) FROM reports) AS reports, " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(report_type) = 'LOST') AS lost, " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(report_type) = 'FOUND') AS found, " +
                        "(SELECT COUNT(*) FROM claims) AS claims, " +
                        "(SELECT COUNT(*) FROM reports WHERE UPPER(status) = 'RETURNED') AS returned";

        String statusSql =
                "SELECT " +
                        "SUM(CASE WHEN status IS NULL OR TRIM(status) = '' THEN 1 ELSE 0 END) AS pending_status, " +
                        "SUM(CASE WHEN UPPER(status) = 'RETURNED' THEN 1 ELSE 0 END) AS returned_status, " +
                        "SUM(CASE WHEN UPPER(status) <> 'RETURNED' AND status IS NOT NULL AND TRIM(status) <> '' THEN 1 ELSE 0 END) AS active_status " +
                        "FROM reports";

        new SwingWorker<Object[], Void>() {

            @Override
            protected Object[] doInBackground()
                    throws Exception {

                Class.forName(
                        "com.mysql.cj.jdbc.Driver"
                );

                try (
                        Connection connection =
                                DriverManager.getConnection(
                                        url,
                                        username,
                                        password
                                );

                        PreparedStatement statement =
                                connection.prepareStatement(sql);

                        PreparedStatement statusStatement =
                                connection.prepareStatement(statusSql)
                ) {

                    int students = 0;
                    int reports = 0;
                    int lost = 0;
                    int found = 0;
                    int claims = 0;
                    int returned = 0;

                    try (ResultSet result =
                                 statement.executeQuery()) {

                        if (result.next()) {
                            students = result.getInt("students");
                            reports = result.getInt("reports");
                            lost = result.getInt("lost");
                            found = result.getInt("found");
                            claims = result.getInt("claims");
                            returned = result.getInt("returned");
                        }
                    }

                    int pendingStatus = 0;
                    int returnedStatus = 0;
                    int activeStatus = 0;

                    try (ResultSet result =
                                 statusStatement.executeQuery()) {

                        if (result.next()) {
                            pendingStatus = result.getInt("pending_status");
                            returnedStatus = result.getInt("returned_status");
                            activeStatus = result.getInt("active_status");
                        }
                    }

                    return new Object[]{
                            students,
                            reports,
                            lost,
                            found,
                            claims,
                            returned,
                            pendingStatus,
                            activeStatus,
                            returnedStatus
                    };
                }
            }

            @Override
            protected void done() {

                try {

                    Object[] values = get();

                    totalStudents.setText(
                            String.valueOf(values[0])
                    );
                    totalReports.setText(
                            String.valueOf(values[1])
                    );
                    lostReports.setText(
                            String.valueOf(values[2])
                    );
                    foundReports.setText(
                            String.valueOf(values[3])
                    );
                    totalClaims.setText(
                            String.valueOf(values[4])
                    );
                    returnedReports.setText(
                            String.valueOf(values[5])
                    );

                    statusLabel.setText(
                            "Pending/Unspecified: "
                                    + values[6]
                                    + "     |     Active: "
                                    + values[7]
                                    + "     |     Returned: "
                                    + values[8]
                    );

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    if (cause == null) {
                        cause = ex;
                    }

                    showErrorDialog(
                            "Unable to load analytics:\n"
                                    + cause.getMessage()
                    );

                    cause.printStackTrace();
                }
            }
        }.execute();
    }

    private JPanel createSimplePanel(
            String title,
            String subtitle,
            String message) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 28, 20, 28));

        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(35, 45, 35, 45)
                )
        );
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel(
                title.startsWith("AI")
                        ? "◎"
                        : "✦"
        );
        icon.setForeground(BLUE);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 35));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(MUTED);
        subtitleLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(new Color(180, 195, 220));
        messageLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(messageLabel);

        panel.add(card);

        return panel;
    }

    // =========================================================
    // PAGE NAVIGATION
    // =========================================================

    private void showPage(
            String page,
            String title,
            String subtitle) {

        pageTitle.setText(title);
        pageSubtitle.setText(subtitle);

        cardLayout.show(contentCards, page);

        if ("REPORTS".equals(page)) {
            loadReports();
        }

        if ("HOME".equals(page)) {
            loadDashboardStats();
            loadRecentAIMatches();
        }
    }

    private String getPageTitle(String page) {

        switch (page) {

            case "AI":
                return "AI Scanner & Matches";

            case "REPORTS":
                return "Item Reports";

            case "PENDING":
                return "Pending Verification";

            case "NOTIFICATIONS":
                return "Notifications";

            case "STUDENTS":
                return "Students";

            case "ANALYTICS":
                return "Analytics";

            case "PROFILE":
                return "Admin Profile";

            default:
                return "Dashboard";
        }
    }

    private String getPageSubtitle(String page) {

        switch (page) {

            case "AI":
                return "Review AI-assisted possible matches.";

            case "REPORTS":
                return "Manage lost and found reports.";

            case "PENDING":
                return "Review reports waiting for verification.";

            case "NOTIFICATIONS":
                return "View important system activity.";

            case "STUDENTS":
                return "Manage student records and activity.";

            case "ANALYTICS":
                return "View automatic system statistics.";

            case "PROFILE":
                return "View administrator account information.";

            default:
                return "Welcome back, " + adminUsername + " ";
        }
    }

    // =========================================================
    // LOAD REPORTS
    // =========================================================

    private void loadReports() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT id, item_name, location, " +
                        "report_type, status, image_path " +
                        "FROM reports " +
                        "ORDER BY id DESC";

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

                Object[] row = {

                        result.getInt("id"),

                        result.getString(
                                "item_name"
                        ),

                        result.getString(
                                "location"
                        ),

                        result.getString(
                                "report_type"
                        ),

                        result.getString(
                                "status"
                        ),

                        result.getString(
                                "image_path"
                        )
                };

                tableModel.addRow(row);
            }

            result.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            showErrorDialog(
                    "Database error:\n" + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // VIEW LOST OR FOUND ITEM PHOTO
    // =========================================================

    private void viewSelectedFoundPhoto() {

        int selectedRow = reportsTable.getSelectedRow();

        if (selectedRow == -1) {
            showInfoDialog(
                    "Please select a report first.",
                    "No Report Selected"
            );
            return;
        }

        int modelRow = reportsTable.convertRowIndexToModel(selectedRow);

        String reportType = String.valueOf(
                tableModel.getValueAt(modelRow, 3)
        );

        Object pathValue = tableModel.getValueAt(modelRow, 5);
        String imagePath = pathValue == null ? "" : pathValue.toString().trim();

        String normalizedType = reportType == null ? "REPORT" : reportType.trim().toUpperCase();
        String itemTypeLabel = "LOST".equals(normalizedType) ? "LOST ITEM" :
                ("FOUND".equals(normalizedType) ? "FOUND ITEM" : "ITEM REPORT");

        if (imagePath.isEmpty()) {
            showInfoDialog(
                    "No photo was uploaded for this " + itemTypeLabel + ".",
                    "Photo Not Available"
            );
            return;
        }

        File imageFile = new File(imagePath);

        if (!imageFile.isFile()) {
            showInfoDialog(
                    "The saved photo could not be found on this computer.\n\n"
                            + "Saved path: " + imagePath,
                    "Photo Not Found"
            );
            return;
        }

        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(imageFile);
        } catch (IOException ignored) {
            // Try the operating system's registered image viewer below.
        }

        if (originalImage == null) {
            // No third-party JAR or manual decoder setup is required. If Java
            // cannot decode the format (for example WebP/HEIC), open the file
            // with the user's default desktop image viewer instead of failing.
            try {
                if (Desktop.isDesktopSupported()
                        && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    // Open unsupported formats directly in the system image viewer.
                    // Do not show a confirmation popup after successfully opening it.
                    Desktop.getDesktop().open(imageFile);
                } else {
                    showInfoDialog(
                            "This photo format cannot be previewed inside the app, and no default image viewer is available.",
                            "Unable to Preview Photo"
                    );
                }
            } catch (Exception ex) {
                showInfoDialog(
                        "The photo could not be opened by the computer's default image viewer.\n\n"
                                + ex.getMessage(),
                        "Unable to Open Photo"
                );
            }
            return;
        }

        // A custom galaxy-themed dialog replaces the old scrollable image view.
        // The photo is scaled proportionally to fit the available area, so the
        // complete image remains visible without horizontal/vertical scrollbars.
        JDialog photoDialog = new JDialog(this, itemTypeLabel + " PHOTO", true);
        photoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        photoDialog.setSize(920, 700);
        photoDialog.setMinimumSize(new Dimension(620, 480));
        photoDialog.setLocationRelativeTo(this);

        Color dialogBackground = new Color(7, 12, 38);
        Color panelBackground = new Color(15, 25, 58);
        Color accentBlue = new Color(70, 115, 255);
        Color accentCyan = new Color(50, 210, 255);
        Color softText = new Color(175, 195, 230);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(dialogBackground);
        root.setBorder(new EmptyBorder(18, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout(12, 4));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(itemTypeLabel + " PHOTO");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel subtitleLabel = new JLabel("Report photo • Complete image fitted to window");
        subtitleLabel.setForeground(softText);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);

        JButton closeButton = new JButton("✕  Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(35, 51, 94));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 100, 160)),
                new EmptyBorder(9, 14, 9, 14)));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> photoDialog.dispose());

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(closeButton, BorderLayout.EAST);

        JPanel imageCard = new JPanel(new BorderLayout());
        imageCard.setBackground(panelBackground);
        imageCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 85, 150), 1),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(5, 9, 27));

        // Scale the source image to fit the preview area while preserving its aspect ratio.
        final BufferedImage sourceImage = originalImage;
        Runnable resizeImage = () -> {
            int availableWidth = imageLabel.getWidth();
            int availableHeight = imageLabel.getHeight();
            if (availableWidth <= 0 || availableHeight <= 0) {
                return;
            }
            int sourceWidth = sourceImage.getWidth();
            int sourceHeight = sourceImage.getHeight();
            double scale = Math.min(
                    (double) availableWidth / sourceWidth,
                    (double) availableHeight / sourceHeight);
            scale = Math.min(scale, 1.0); // Do not enlarge small photos unnecessarily.
            int scaledWidth = Math.max(1, (int) Math.round(sourceWidth * scale));
            int scaledHeight = Math.max(1, (int) Math.round(sourceHeight * scale));
            Image scaledImage = sourceImage.getScaledInstance(
                    scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        };

        imageCard.add(imageLabel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("The image is fitted proportionally; no part of the photo is cropped.");
        footerLabel.setForeground(softText);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(footerLabel, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);
        root.add(imageCard, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        photoDialog.setContentPane(root);

        // Refit the image whenever the dialog or its preview area is resized.
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(resizeImage);
            }
        });
        photoDialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(resizeImage);
            }
        });

        photoDialog.setVisible(true);
        SwingUtilities.invokeLater(resizeImage);
    }

    // =========================================================
    // VERIFY REPORT
    // =========================================================

    private void verifySelectedReport() {

        int selectedRow =
                reportsTable.getSelectedRow();

        if (selectedRow == -1) {

            showInfoDialog(
                    "Please select a report first.",
                    "No Report Selected"
            );

            return;
        }

        int modelRow =
                reportsTable.convertRowIndexToModel(
                        selectedRow
                );

        int id =
                (int) tableModel.getValueAt(
                        modelRow,
                        0
                );

        updateStatus(id, "Verified");
    }

    // =========================================================
    // REJECT REPORT
    // =========================================================

    private void rejectSelectedReport() {

        int selectedRow =
                reportsTable.getSelectedRow();

        if (selectedRow == -1) {

            showInfoDialog(
                    "Please select a report first.",
                    "No Report Selected"
            );

            return;
        }

        int modelRow =
                reportsTable.convertRowIndexToModel(
                        selectedRow
                );

        int id =
                (int) tableModel.getValueAt(
                        modelRow,
                        0
                );

        updateStatus(id, "Rejected");
    }

    // =========================================================
    // UPDATE REPORT STATUS
    // =========================================================

    private void updateStatus(
            int id,
            String newStatus) {

        String sql =
                "UPDATE reports " +
                        "SET status = ? " +
                        "WHERE id = ?";

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
                    connection.prepareStatement(sql);

            statement.setString(1, newStatus);
            statement.setInt(2, id);

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                showSuccessDialog(
                        "Report status changed to " +
                                newStatus
                );
            }

            statement.close();
            connection.close();

            loadReports();

        } catch (Exception e) {

            showErrorDialog(
                    "Database error:\n" +
                            e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // BUTTON HELPERS
    // =========================================================

    private JButton createIconButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI Emoji", Font.PLAIN, 18)
        );
        button.setForeground(TEXT);
        button.setBackground(CARD_2);
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createEmptyBorder(
                        7, 10, 7, 10
                )
        );
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private JButton createActionButton(
            String text,
            Color background) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9, 14, 9, 14
                )
        );
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // =========================================================
    // GALAXY DIALOG HELPERS
    // =========================================================

    private JDialog createGalaxyDialog(
            String title,
            int width,
            int height
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        title,
                        true
                );

        dialog.setUndecorated(true);
        dialog.setSize(width, height);
        dialog.setResizable(false);

        JPanel outer =
                new JPanel(new BorderLayout());

        outer.setBackground(BACKGROUND);
        outer.setBorder(
                BorderFactory.createLineBorder(
                        new Color(75, 105, 190),
                        1
                )
        );

        JPanel titleBar =
                new JPanel(new BorderLayout());

        titleBar.setBackground(
                new Color(10, 15, 38)
        );
        titleBar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        new Color(55, 80, 150)
                )
        );

        JLabel titleLabel =
                new JLabel("  " + title);

        titleLabel.setForeground(TEXT);
        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        JButton close =
                new JButton("×");

        close.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        22
                )
        );
        close.setForeground(MUTED);
        close.setBackground(
                new Color(10, 15, 38)
        );
        close.setFocusPainted(false);
        close.setBorder(
                BorderFactory.createEmptyBorder(
                        3, 12, 3, 12
                )
        );
        close.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        close.addActionListener(
                e -> dialog.dispose()
        );

        titleBar.add(
                titleLabel,
                BorderLayout.CENTER
        );
        titleBar.add(
                close,
                BorderLayout.EAST
        );

        outer.add(
                titleBar,
                BorderLayout.NORTH
        );

        dialog.setContentPane(outer);

        return dialog;
    }

    private void showGalaxyInfoDialog(
            String message,
            String title,
            String icon
    ) {

        JDialog dialog =
                createGalaxyDialog(
                        title,
                        520,
                        270
                );

        JPanel root =
                new JPanel(new BorderLayout(0, 15));

        root.setBackground(BACKGROUND);
        root.setBorder(
                new EmptyBorder(24, 28, 22, 28)
        );

        JPanel messagePanel =
                new JPanel(new BorderLayout(15, 0));

        messagePanel.setBackground(CARD);
        messagePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(65, 90, 165)
                        ),
                        new EmptyBorder(18, 18, 18, 18)
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setForeground(
                icon.equals("△") ? ORANGE : CYAN
        );
        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.BOLD,
                        28
                )
        );

        JLabel text =
                new JLabel(
                        "<html><div style='width:350px'>"
                                + escapeHtml(message)
                                + "</div></html>"
                );

        text.setForeground(TEXT);
        text.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        messagePanel.add(
                iconLabel,
                BorderLayout.WEST
        );
        messagePanel.add(
                text,
                BorderLayout.CENTER
        );

        root.add(
                messagePanel,
                BorderLayout.CENTER
        );

        JButton ok =
                createActionButton(
                        "OK",
                        new Color(70, 105, 255)
                );

        ok.setPreferredSize(
                new Dimension(90, 38)
        );
        ok.addActionListener(
                e -> dialog.dispose()
        );

        JPanel buttons =
                new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttons.setOpaque(false);
        buttons.add(ok);

        root.add(
                buttons,
                BorderLayout.SOUTH
        );

        dialog.add(root);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showInfoDialog(
            String message,
            String title) {

        showGalaxyInfoDialog(
                message,
                title,
                "◎"
        );
    }

    private void showSuccessDialog(String message) {

        showGalaxyInfoDialog(
                message,
                "Success",
                "✓"
        );
    }

    private void showErrorDialog(String message) {

        showGalaxyInfoDialog(
                message,
                "Error",
                "△"
        );
    }

    private void styleScrollBar(
            JScrollBar bar
    ) {

        bar.setPreferredSize(
                new Dimension(9, 0)
        );
        bar.setBackground(CARD);
        bar.setOpaque(true);

        bar.setUI(
                new javax.swing.plaf.basic.BasicScrollBarUI() {

                    @Override
                    protected void configureScrollBarColors() {

                        this.thumbColor =
                                new Color(120, 75, 220);

                        this.trackColor =
                                new Color(10, 14, 34);
                    }

                    @Override
                    protected JButton createDecreaseButton(
                            int orientation) {

                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(
                            int orientation) {

                        return createZeroButton();
                    }

                    private JButton createZeroButton() {

                        JButton button =
                                new JButton();

                        button.setPreferredSize(
                                new Dimension(0, 0)
                        );
                        button.setMinimumSize(
                                new Dimension(0, 0)
                        );
                        button.setMaximumSize(
                                new Dimension(0, 0)
                        );

                        return button;
                    }
                }
        );
    }

    private String escapeHtml(
            String value
    ) {

        if (value == null) {
            return "-";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    // =========================================================
    // GALAXY BACKGROUND
    // Same visual background used by StudentDashboard.
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
    // LOGOUT
    // =========================================================

    private void logout() {

        final JDialog logoutDialog =
                new JDialog(
                        this,
                        "Logout",
                        true
                );

        logoutDialog.setSize(500, 310);
        logoutDialog.setResizable(false);
        logoutDialog.setLocationRelativeTo(this);
        logoutDialog.setLayout(
                new BorderLayout()
        );

        JPanel root =
                new JPanel(new BorderLayout());

        root.setBackground(BACKGROUND);
        root.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BLUE,
                                1
                        ),
                        new EmptyBorder(0, 0, 0, 0)
                )
        );

        JPanel accent = new JPanel();
        accent.setBackground(BLUE);
        accent.setPreferredSize(
                new Dimension(0, 5)
        );

        root.add(
                accent,
                BorderLayout.NORTH
        );

        JPanel content = new JPanel();
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
        icon.setBackground(BLUE);
        icon.setForeground(Color.WHITE);
        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        icon.setPreferredSize(
                new Dimension(58, 58)
        );
        icon.setMinimumSize(
                new Dimension(58, 58)
        );
        icon.setMaximumSize(
                new Dimension(58, 58)
        );
        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel("Confirm Logout");

        title.setForeground(TEXT);
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

        message.setForeground(MUTED);
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
                Box.createVerticalStrut(12)
        );
        content.add(title);
        content.add(
                Box.createVerticalStrut(9)
        );
        content.add(message);

        root.add(
                content,
                BorderLayout.CENTER
        );

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                14,
                                12
                        )
                );

        footer.setBackground(
                new Color(15, 22, 42)
        );

        JButton cancel =
                createActionButton(
                        "Cancel",
                        new Color(90, 105, 130)
                );

        JButton logout =
                createActionButton(
                        "Logout",
                        RED
                );

        cancel.addActionListener(
                e -> logoutDialog.dispose()
        );

        logout.addActionListener(e -> {

            logoutDialog.dispose();
            dispose();

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);
        });

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
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AdminDashboard dashboard =
                    new AdminDashboard("admin1");

            dashboard.setVisible(true);
        });
    }
}