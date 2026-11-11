package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.*;
import java.util.Random;

public class MyProfileFrame extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String DB_URL = database.DBConfig.DB_URL;

    private static final String DB_USERNAME = database.DBConfig.DB_USER;

    private static final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;

    // =========================================================
    // GALAXY COLORS
    // =========================================================

    private static final Color GALAXY_TOP =
            new Color(
                    5,
                    8,
                    25
            );

    private static final Color GALAXY_BOTTOM =
            new Color(
                    17,
                    7,
                    48
            );

    private static final Color CARD_BG =
            new Color(
                    10,
                    20,
                    55,
                    235
            );

    private static final Color CARD_HOVER =
            new Color(
                    15,
                    30,
                    75,
                    245
            );

    private static final Color CYAN =
            new Color(
                    45,
                    210,
                    255
            );

    private static final Color BLUE =
            new Color(
                    75,
                    115,
                    255
            );

    private static final Color PURPLE =
            new Color(
                    150,
                    85,
                    255
            );

    private static final Color GREEN =
            new Color(
                    70,
                    225,
                    150
            );

    private static final Color WHITE =
            new Color(
                    245,
                    248,
                    255
            );

    private static final Color SECONDARY_TEXT =
            new Color(
                    170,
                    185,
                    215
            );

    private static final Color BORDER =
            new Color(
                    55,
                    105,
                    175
            );

    private static final Color DARK =
            new Color(
                    5,
                    12,
                    32
            );

    // =========================================================
    // USER DATA
    // =========================================================

    private final String username;

    // The exact dashboard window that opened this profile.
    // This prevents Admin Profile from ever guessing StudentDashboard.
    private final JFrame returnFrame;

    private String fullName;

    // =========================================================
    // UI COMPONENTS
    // =========================================================

    private JLabel fullNameValue;

    private JLabel usernameValue;

    private JLabel roleValue;

    private JLabel statusValue;

    private JLabel reportsCountLabel;

    private JLabel claimsCountLabel;

    // Admin profile statistics
    private JLabel adminReportsCountLabel;
    private JLabel adminClaimsCountLabel;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    // Existing student calls remain compatible.
    public MyProfileFrame(
            String username
    ) {

        this(
                username,
                null
        );
    }

    // Parent-aware constructor.
    // AdminDashboard passes itself here.
    public MyProfileFrame(
            String username,
            JFrame returnFrame
    ) {

        this.username =
                username;

        this.returnFrame =
                returnFrame;

        this.fullName =
                username;

        setTitle(
                "My Profile - Smart Campus"
        );

        setSize(
                1000,
                760
        );

        setMinimumSize(
                new Dimension(
                        900,
                        680
                )
        );

        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        addWindowListener(
                new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(
                            java.awt.event.WindowEvent e
                    ) {

                        goBackToDashboard();
                    }
                }
        );

        setLocationRelativeTo(
                null
        );

        // Screen-size change only: open this frame maximized.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setResizable(
                true
        );

        loadUserData();

        buildUI();
    }

    // =========================================================
    // LOAD USER DATA
    // =========================================================

    private void loadUserData() {

        String sql =
                "SELECT full_name, role "
                        + "FROM users "
                        + "WHERE username = ?";

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
                        username
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    if (
                            result.next()
                    ) {

                        String databaseName =
                                result.getString(
                                        "full_name"
                                );

                        if (
                                databaseName != null
                                        &&
                                        !databaseName
                                                .trim()
                                                .isEmpty()
                        ) {

                            fullName =
                                    databaseName;
                        }
                    }
                }
            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();

            fullName =
                    username;
        }
    }


    // =========================================================
    // ADMIN PROFILE UI
    // =========================================================

    private void buildAdminUI() {

        GalaxyPanel background =
                new GalaxyPanel();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );

        topBar.setOpaque(false);

        topBar.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        15,
                        35
                )
        );

        JButton backButton =
                createTransparentButton(
                        "← Back"
                );

        backButton.addActionListener(
                e -> goBackToDashboard()
        );

        topBar.add(
                backButton,
                BorderLayout.WEST
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
                        "Admin Profile"
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
                        "Manage your Smart Campus administrator account"
                );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(4)
        );

        titlePanel.add(subtitle);

        topBar.add(
                titlePanel,
                BorderLayout.CENTER
        );

        JLabel online =
                new JLabel(
                        "● ACTIVE"
                );

        online.setForeground(
                GREEN
        );

        online.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        topBar.add(
                online,
                BorderLayout.EAST
        );

        background.add(
                topBar,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainContent =
                new JPanel();

        mainContent.setOpaque(false);

        mainContent.setLayout(
                new BoxLayout(
                        mainContent,
                        BoxLayout.Y_AXIS
                )
        );

        mainContent.setBorder(
                new EmptyBorder(
                        10,
                        55,
                        35,
                        55
                )
        );

        // =====================================================
        // ADMIN HERO
        // =====================================================

        JPanel hero =
                createSectionCard();

        hero.setLayout(
                new BorderLayout(
                        25,
                        0
                )
        );

        hero.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                PURPLE,
                                1
                        ),
                        new EmptyBorder(
                                28,
                                35,
                                28,
                                35
                        )
                )
        );

        JPanel adminAvatar =
                new JPanel(
                        new GridBagLayout()
                );

        adminAvatar.setPreferredSize(
                new Dimension(
                        125,
                        125
                )
        );

        adminAvatar.setBackground(
                PURPLE
        );

        JLabel avatarLetter =
                new JLabel(
                        "A"
                );

        avatarLetter.setForeground(
                WHITE
        );

        avatarLetter.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        adminAvatar.add(
                avatarLetter
        );

        hero.add(
                adminAvatar,
                BorderLayout.WEST
        );

        JPanel adminInfo =
                new JPanel();

        adminInfo.setOpaque(false);

        adminInfo.setLayout(
                new BoxLayout(
                        adminInfo,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel accountType =
                new JLabel(
                        "ADMIN ACCOUNT"
                );

        accountType.setForeground(
                PURPLE
        );

        accountType.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel name =
                new JLabel(
                        fullName
                );

        name.setForeground(
                WHITE
        );

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        JLabel user =
                new JLabel(
                        "@" + username
                );

        user.setForeground(
                SECONDARY_TEXT
        );

        user.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel adminBadge =
                createBadge(
                        "ADMIN",
                        PURPLE
                );

        adminInfo.add(
                accountType
        );

        adminInfo.add(
                Box.createVerticalStrut(7)
        );

        adminInfo.add(
                name
        );

        adminInfo.add(
                Box.createVerticalStrut(4)
        );

        adminInfo.add(
                user
        );

        adminInfo.add(
                Box.createVerticalStrut(13)
        );

        adminInfo.add(
                adminBadge
        );

        hero.add(
                adminInfo,
                BorderLayout.CENTER
        );

        JPanel accessBox =
                new JPanel();

        accessBox.setOpaque(false);

        accessBox.setLayout(
                new BoxLayout(
                        accessBox,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel accessTitle =
                new JLabel(
                        "ACCESS LEVEL"
                );

        accessTitle.setForeground(
                SECONDARY_TEXT
        );

        accessTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel accessValue =
                new JLabel(
                        "FULL ADMIN ACCESS"
                );

        accessValue.setForeground(
                WHITE
        );

        accessValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        accessBox.add(
                accessTitle
        );

        accessBox.add(
                Box.createVerticalStrut(6)
        );

        accessBox.add(
                accessValue
        );

        hero.add(
                accessBox,
                BorderLayout.EAST
        );

        mainContent.add(hero);

        mainContent.add(
                Box.createVerticalStrut(22)
        );

        // =====================================================
        // ADMIN ACCOUNT INFORMATION
        // =====================================================

        JPanel accountCard =
                createSectionCard();

        JPanel accountGrid =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                25,
                                20
                        )
                );

        accountGrid.setOpaque(false);

        accountGrid.add(
                createInfoBlock(
                        "USERNAME",
                        username,
                        "@"
                )
        );

        accountGrid.add(
                createInfoBlock(
                        "ACCOUNT ROLE",
                        "ADMINISTRATOR",
                        "◆"
                )
        );

        accountGrid.add(
                createInfoBlock(
                        "ACCESS LEVEL",
                        "FULL ADMIN ACCESS",
                        "★"
                )
        );

        accountGrid.add(
                createAdminStatusBlock()
        );

        accountCard.add(
                accountGrid,
                BorderLayout.CENTER
        );

        mainContent.add(
                accountCard
        );

        mainContent.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // ADMIN ACTIVITY
        // =====================================================

        JPanel activityCard =
                createSectionCard();

        JPanel activityPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        activityPanel.setOpaque(false);

        adminReportsCountLabel =
                new JLabel("—");

        adminClaimsCountLabel =
                new JLabel("—");

        activityPanel.add(
                createActivityCard(
                        "▣",
                        "Reports Managed",
                        adminReportsCountLabel,
                        CYAN
                )
        );

        activityPanel.add(
                createActivityCard(
                        "📋",
                        "Pending Claims",
                        adminClaimsCountLabel,
                        PURPLE
                )
        );

        activityPanel.add(
                createActivityCard(
                        "🤖",
                        "AI Matching",
                        new JLabel("ACTIVE"),
                        BLUE
                )
        );

        activityCard.add(
                activityPanel,
                BorderLayout.CENTER
        );

        mainContent.add(
                activityCard
        );

        mainContent.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // ADMIN PERMISSIONS
        // =====================================================

        JPanel permissionsCard =
                createSectionCard();

        JPanel permissions =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        permissions.setOpaque(false);

        permissions.add(
                createPermissionCard(
                        "REPORTS",
                        "Review and verify lost/found reports",
                        CYAN
                )
        );

        permissions.add(
                createPermissionCard(
                        "CLAIMS",
                        "Review ownership claims",
                        PURPLE
                )
        );

        permissions.add(
                createPermissionCard(
                        "AI MATCHES",
                        "Review intelligent item matches",
                        BLUE
                )
        );

        permissionsCard.add(
                permissions,
                BorderLayout.CENTER
        );

        mainContent.add(
                permissionsCard
        );

        mainContent.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // SECURITY
        // =====================================================

        JPanel securityCard =
                createSectionCard();

        JPanel securityContent =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        securityContent.setOpaque(false);

        JPanel securityText =
                new JPanel();

        securityText.setOpaque(false);

        securityText.setLayout(
                new BoxLayout(
                        securityText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel securityTitle =
                new JLabel(
                        "Administrator Security"
                );

        securityTitle.setForeground(
                WHITE
        );

        securityTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JLabel securityDescription =
                new JLabel(
                        "Keep your administrator account secure."
                );

        securityDescription.setForeground(
                SECONDARY_TEXT
        );

        securityDescription.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        securityText.add(
                securityTitle
        );

        securityText.add(
                Box.createVerticalStrut(4)
        );

        securityText.add(
                securityDescription
        );

        JButton passwordButton =
                createSecondaryButton(
                        "🔐 Change Password"
                );

        passwordButton.addActionListener(
                e -> changePassword()
        );

        securityContent.add(
                securityText,
                BorderLayout.CENTER
        );

        securityContent.add(
                passwordButton,
                BorderLayout.EAST
        );

        securityCard.add(
                securityContent,
                BorderLayout.CENTER
        );

        mainContent.add(
                securityCard
        );

        // =====================================================
        // CLOSE BUTTON
        // =====================================================

        JPanel bottomBar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomBar.setOpaque(false);

        JButton closeButton =
                createSecondaryButton(
                        "Close"
                );

        closeButton.addActionListener(
                e -> goBackToDashboard()
        );

        bottomBar.add(
                closeButton
        );

        mainContent.add(
                Box.createVerticalStrut(15)
        );

        mainContent.add(
                bottomBar
        );

        // =====================================================
        // SCROLL
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        mainContent
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setOpaque(false);

        scrollPane.getViewport().setOpaque(false);

        scrollPane.getVerticalScrollBar().setUnitIncrement(
                16
        );

        background.add(
                scrollPane,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );

        loadAdminStatistics();
    }

    // =========================================================
    // ADMIN STATUS
    // =========================================================

    private JPanel createAdminStatusBlock() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "ACCOUNT STATUS"
                );

        title.setForeground(
                SECONDARY_TEXT
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel status =
                new JLabel(
                        "● Active"
                );

        status.setForeground(
                GREEN
        );

        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(status);

        return panel;
    }

    // =========================================================
    // ADMIN PERMISSION CARD
    // =========================================================

    private JPanel createPermissionCard(
            String title,
            String description,
            Color accent
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(
                DARK
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                accent,
                                1
                        ),
                        new EmptyBorder(
                                14,
                                15,
                                14,
                                15
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setForeground(
                accent
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );

        descriptionLabel.setForeground(
                SECONDARY_TEXT
        );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(8)
        );

        card.add(descriptionLabel);

        return card;
    }

    // =========================================================
    // ADMIN STATISTICS
    // =========================================================

    private void loadAdminStatistics() {

        String reportsSql =
                "SELECT COUNT(*) FROM reports";

        String pendingClaimsSql =
                "SELECT COUNT(*) FROM claims "
                        + "WHERE LOWER(status) = 'pending'";

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
                            )
            ) {

                try (
                        Statement statement =
                                connection.createStatement()
                ) {

                    try (
                            ResultSet result =
                                    statement.executeQuery(
                                            reportsSql
                                    )
                    ) {

                        if (result.next()) {

                            adminReportsCountLabel.setText(
                                    String.valueOf(
                                            result.getInt(1)
                                    )
                            );
                        }
                    }
                }

                try (
                        Statement statement =
                                connection.createStatement()
                ) {

                    try (
                            ResultSet result =
                                    statement.executeQuery(
                                            pendingClaimsSql
                                    )
                    ) {

                        if (result.next()) {

                            adminClaimsCountLabel.setText(
                                    String.valueOf(
                                            result.getInt(1)
                                    )
                            );
                        }
                    }
                }
            }

        } catch (Exception e) {

            adminReportsCountLabel.setText("—");

            adminClaimsCountLabel.setText("—");
        }
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        // Admin Profile uses a dedicated administrator layout.
        // Student Profile keeps the existing layout below.
        if (returnFrame instanceof AdminDashboard) {
            buildAdminUI();
            return;
        }

        GalaxyPanel background =
                new GalaxyPanel();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // TOP HEADER
        // =====================================================

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );

        topBar.setOpaque(
                false
        );

        topBar.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        15,
                        35
                )
        );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        JButton backButton =
                createTransparentButton(
                        "← Back"
                );

        backButton.addActionListener(
                e -> goBackToDashboard()
        );

        topBar.add(
                backButton,
                BorderLayout.WEST
        );

        // =====================================================
        // TITLE
        // =====================================================

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(
                false
        );

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "My Profile"
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
                        "Manage your Smart Campus account"
                );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        titlePanel.add(
                subtitle
        );

        topBar.add(
                titlePanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // ACTIVE STATUS
        // =====================================================

        JLabel online =
                new JLabel(
                        "● ACTIVE"
                );

        online.setForeground(
                GREEN
        );

        online.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        topBar.add(
                online,
                BorderLayout.EAST
        );

        background.add(
                topBar,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainContent =
                new JPanel();

        mainContent.setOpaque(
                false
        );

        mainContent.setLayout(
                new BoxLayout(
                        mainContent,
                        BoxLayout.Y_AXIS
                )
        );

        mainContent.setBorder(
                new EmptyBorder(
                        10,
                        55,
                        30,
                        55
                )
        );

        // =====================================================
        // PROFILE HERO CARD
        // =====================================================

        JPanel profileCard =
                new JPanel(
                        new BorderLayout(
                                25,
                                0
                        )
                );

        profileCard.setBackground(
                CARD_BG
        );

        profileCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        45,
                                        210,
                                        255,
                                        100
                                ),
                                1
                        ),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        profileCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // =====================================================
        // AVATAR
        // =====================================================

        JPanel avatar =
                new AvatarPanel();

        avatar.setPreferredSize(
                new Dimension(
                        125,
                        125
                )
        );

        profileCard.add(
                avatar,
                BorderLayout.WEST
        );

        // =====================================================
        // PROFILE INFORMATION
        // =====================================================

        JPanel profileInfo =
                new JPanel();

        profileInfo.setOpaque(
                false
        );

        profileInfo.setLayout(
                new BoxLayout(
                        profileInfo,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel accountType =
                new JLabel(
                        "STUDENT ACCOUNT"
                );

        accountType.setForeground(
                CYAN
        );

        accountType.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        fullNameValue =
                new JLabel(
                        fullName
                );

        fullNameValue.setForeground(
                WHITE
        );

        fullNameValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        usernameValue =
                new JLabel(
                        "@" + username
                );

        usernameValue.setForeground(
                SECONDARY_TEXT
        );

        usernameValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JPanel roleRow =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        roleRow.setOpaque(
                false
        );

        roleValue =
                new JLabel(
                        "STUDENT"
                );

        roleValue.setForeground(
                WHITE
        );

        roleValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JLabel roleBadge =
                createBadge(
                        "STUDENT",
                        BLUE
                );

        roleRow.add(
                roleBadge
        );

        profileInfo.add(
                accountType
        );

        profileInfo.add(
                Box.createVerticalStrut(
                        7
                )
        );

        profileInfo.add(
                fullNameValue
        );

        profileInfo.add(
                Box.createVerticalStrut(
                        4
                )
        );

        profileInfo.add(
                usernameValue
        );

        profileInfo.add(
                Box.createVerticalStrut(
                        13
                )
        );

        profileInfo.add(
                roleRow
        );

        profileCard.add(
                profileInfo,
                BorderLayout.CENTER
        );

        // =====================================================
        // EDIT BUTTON
        // =====================================================

        JButton editButton =
                createPrimaryButton(
                        "✎ Edit Profile"
                );

        editButton.addActionListener(
                e -> editProfile()
        );

        JPanel editWrapper =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        editWrapper.setOpaque(
                false
        );

        editWrapper.add(
                editButton
        );

        profileCard.add(
                editWrapper,
                BorderLayout.EAST
        );

        mainContent.add(
                profileCard
        );

        mainContent.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // PERSONAL INFORMATION
        // =====================================================

        JPanel personalCard =
                createSectionCard();

        JPanel personalContent =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                25,
                                20
                        )
                );

        personalContent.setOpaque(
                false
        );

        personalContent.add(
                createInfoBlock(
                        "FULL NAME",
                        fullName,
                        "👤"
                )
        );

        personalContent.add(
                createInfoBlock(
                        "USERNAME",
                        username,
                        "@"
                )
        );

        personalContent.add(
                createInfoBlock(
                        "ACCOUNT ROLE",
                        "STUDENT",
                        "◆"
                )
        );

        statusValue =
                new JLabel(
                        "Active"
                );

        personalContent.add(
                createStatusBlock()
        );

        personalCard.add(
                personalContent,
                BorderLayout.CENTER
        );

        mainContent.add(
                personalCard
        );

        mainContent.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // ACTIVITY SECTION
        // =====================================================

        JPanel activityCard =
                createSectionCard();

        JPanel activityPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        activityPanel.setOpaque(
                false
        );

        reportsCountLabel =
                new JLabel(
                        "0"
                );

        claimsCountLabel =
                new JLabel(
                        "0"
                );

        activityPanel.add(
                createActivityCard(
                        "📦",
                        "Reports",
                        reportsCountLabel,
                        CYAN
                )
        );

        activityPanel.add(
                createActivityCard(
                        "📋",
                        "Claims",
                        claimsCountLabel,
                        PURPLE
                )
        );

        activityPanel.add(
                createActivityCard(
                        "🤖",
                        "Smart Match",
                        new JLabel(
                                "AI"
                        ),
                        BLUE
                )
        );

        activityCard.add(
                activityPanel,
                BorderLayout.CENTER
        );

        mainContent.add(
                activityCard
        );

        mainContent.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // SECURITY CARD
        // =====================================================

        JPanel securityCard =
                createSectionCard();

        JPanel securityContent =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        securityContent.setOpaque(
                false
        );

        JPanel securityText =
                new JPanel();

        securityText.setOpaque(
                false
        );

        securityText.setLayout(
                new BoxLayout(
                        securityText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel securityTitle =
                new JLabel(
                        "Account Security"
                );

        securityTitle.setForeground(
                WHITE
        );

        securityTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JLabel securityDescription =
                new JLabel(
                        "Keep your Smart Campus account secure."
                );

        securityDescription.setForeground(
                SECONDARY_TEXT
        );

        securityDescription.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        securityText.add(
                securityTitle
        );

        securityText.add(
                Box.createVerticalStrut(
                        4
                )
        );

        securityText.add(
                securityDescription
        );

        JButton passwordButton =
                createSecondaryButton(
                        "🔐 Change Password"
                );

        passwordButton.addActionListener(
                e -> changePassword()
        );

        securityContent.add(
                securityText,
                BorderLayout.CENTER
        );

        securityContent.add(
                passwordButton,
                BorderLayout.EAST
        );

        securityCard.add(
                securityContent,
                BorderLayout.CENTER
        );

        mainContent.add(
                securityCard
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        mainContent
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setOpaque(
                false
        );

        scrollPane.getViewport()
                .setOpaque(
                        false
                );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
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
        // BOTTOM CLOSE BUTTON
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                25,
                                15
                        )
                );

        bottom.setOpaque(
                false
        );

        JButton closeButton =
                createSecondaryButton(
                        "Close"
                );

        // IMPORTANT:
        // Close returns to Student Dashboard.
        // It does NOT close the application.

        closeButton.addActionListener(
                e -> goBackToDashboard()
        );

        bottom.add(
                closeButton
        );

        background.add(
                bottom,
                BorderLayout.SOUTH
        );

        // =====================================================
        // LOAD STATISTICS
        // =====================================================

        loadStatistics();

        // =====================================================
        // SET CONTENT
        // =====================================================

        setContentPane(
                background
        );
    }

    // =========================================================
    // BACK TO PREVIOUS DASHBOARD
    // =========================================================

    private void goBackToDashboard() {

        // Close the profile window.
        dispose();

        // =====================================================
        // RETURN TO THE EXACT WINDOW THAT OPENED THIS PROFILE
        // =====================================================

        if (
                returnFrame != null
        ) {

            returnFrame.setVisible(
                    true
            );

            returnFrame.toFront();

            returnFrame.requestFocus();

            return;
        }

        // =====================================================
        // STUDENT FALLBACK
        // =====================================================

        // This is only used by old student code that opens
        // MyProfileFrame with the one-argument constructor.
        for (
                Window window
                : Window.getWindows()
        ) {

            if (
                    window instanceof StudentDashboard
            ) {

                StudentDashboard dashboard =
                        (StudentDashboard) window;

                dashboard.setVisible(
                        true
                );

                dashboard.toFront();

                dashboard.requestFocus();

                return;
            }
        }

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

        dashboard.toFront();

        dashboard.requestFocus();
    }

    // =========================================================
    // SECTION CARD
    // =========================================================

    private JPanel createSectionCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_BG
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        55,
                                        105,
                                        175,
                                        140
                                ),
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

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        return card;
    }

    // =========================================================
    // INFO BLOCK
    // =========================================================

    private JPanel createInfoBlock(
            String title,
            String value,
            String icon
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        panel.setOpaque(
                false
        );

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setForeground(
                CYAN
        );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        22
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(
                false
        );

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
                SECONDARY_TEXT
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JLabel valueLabel =
                new JLabel(
                        value
                );

        valueLabel.setForeground(
                WHITE
        );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(
                        4
                )
        );

        text.add(
                valueLabel
        );

        panel.add(
                iconLabel,
                BorderLayout.WEST
        );

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // STATUS BLOCK
    // =========================================================

    private JPanel createStatusBlock() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        panel.setOpaque(
                false
        );

        JLabel icon =
                new JLabel(
                        "●"
                );

        icon.setForeground(
                GREEN
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(
                false
        );

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "ACCOUNT STATUS"
                );

        title.setForeground(
                SECONDARY_TEXT
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        statusValue.setForeground(
                GREEN
        );

        statusValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        text.add(
                title
        );

        text.add(
                Box.createVerticalStrut(
                        4
                )
        );

        text.add(
                statusValue
        );

        panel.add(
                icon,
                BorderLayout.WEST
        );

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // ACTIVITY CARD
    // =========================================================

    private JPanel createActivityCard(
            String icon,
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(
                new Color(
                        8,
                        17,
                        45
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        50,
                                        80,
                                        140
                                ),
                                1
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        25
                )
        );

        iconLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        value.setForeground(
                accent
        );

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        value.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setForeground(
                SECONDARY_TEXT
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(
                iconLabel
        );

        card.add(
                Box.createVerticalStrut(
                        6
                )
        );

        card.add(
                value
        );

        card.add(
                Box.createVerticalStrut(
                        2
                )
        );

        card.add(
                titleLabel
        );

        return card;
    }

    // =========================================================
    // BADGE
    // =========================================================

    private JLabel createBadge(
            String text,
            Color color
    ) {

        JLabel badge =
                new JLabel(
                        "  " + text + "  "
                );

        badge.setForeground(
                WHITE
        );

        badge.setBackground(
                color
        );

        badge.setOpaque(
                true
        );

        badge.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        badge.setBorder(
                new EmptyBorder(
                        5,
                        8,
                        5,
                        8
                )
        );

        return badge;
    }

    // =========================================================
    // EDIT PROFILE
    // =========================================================

    private void editProfile() {

        JTextField nameField =
                new JTextField(
                        fullName
                );

        nameField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        nameField.setForeground(
                WHITE
        );

        nameField.setCaretColor(
                CYAN
        );

        nameField.setBackground(
                DARK
        );

        nameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        JPanel panel =
                new JPanel();

        panel.setBackground(
                DARK
        );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label =
                new JLabel(
                        "Enter your full name:"
                );

        label.setForeground(
                WHITE
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                label
        );

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        panel.add(
                nameField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Edit Profile",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
                result !=
                        JOptionPane.OK_OPTION
        ) {

            return;
        }

        String newName =
                nameField
                        .getText()
                        .trim();

        if (
                newName.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Full name cannot be empty.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        updateFullName(
                newName
        );
    }

    // =========================================================
    // UPDATE FULL NAME
    // =========================================================

    private void updateFullName(
            String newName
    ) {

        String sql =
                "UPDATE users "
                        + "SET full_name = ? "
                        + "WHERE username = ?";

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
                        newName
                );

                statement.setString(
                        2,
                        username
                );

                int updated =
                        statement.executeUpdate();

                if (
                        updated > 0
                ) {

                    fullName =
                            newName;

                    fullNameValue.setText(
                            fullName
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Your profile has been updated successfully.",
                            "Profile Updated",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to update your profile.",
                            "Update Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database error while updating profile.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private void changePassword() {

        JPasswordField currentPassword =
                createPasswordField();

        JPasswordField newPassword =
                createPasswordField();

        JPasswordField confirmPassword =
                createPasswordField();

        JPanel panel =
                new JPanel();

        panel.setBackground(
                DARK
        );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.setLayout(
                new GridLayout(
                        6,
                        1,
                        8,
                        8
                )
        );

        JLabel currentLabel =
                createDialogLabel(
                        "Current Password"
                );

        JLabel newLabel =
                createDialogLabel(
                        "New Password"
                );

        JLabel confirmLabel =
                createDialogLabel(
                        "Confirm New Password"
                );

        panel.add(
                currentLabel
        );

        panel.add(
                currentPassword
        );

        panel.add(
                newLabel
        );

        panel.add(
                newPassword
        );

        panel.add(
                confirmLabel
        );

        panel.add(
                confirmPassword
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Change Password",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
                result !=
                        JOptionPane.OK_OPTION
        ) {

            return;
        }

        String current =
                new String(
                        currentPassword
                                .getPassword()
                );

        String newPass =
                new String(
                        newPassword
                                .getPassword()
                );

        String confirm =
                new String(
                        confirmPassword
                                .getPassword()
                );

        if (
                current.isEmpty()
                        ||
                        newPass.isEmpty()
                        ||
                        confirm.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "All password fields are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                !newPass.equals(
                        confirm
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "New passwords do not match.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                newPass.length() < 6
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password must contain at least 6 characters.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        updatePassword(
                current,
                newPass
        );
    }

    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    private void updatePassword(
            String currentPassword,
            String newPassword
    ) {

        String verifySql =
                "SELECT id FROM users "
                        + "WHERE username = ? "
                        + "AND password = ?";

        String updateSql =
                "UPDATE users "
                        + "SET password = ? "
                        + "WHERE username = ?";

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
                            )
            ) {

                try (
                        PreparedStatement verify =
                                connection.prepareStatement(
                                        verifySql
                                )
                ) {

                    verify.setString(
                            1,
                            username
                    );

                    verify.setString(
                            2,
                            currentPassword
                    );

                    try (
                            ResultSet result =
                                    verify.executeQuery()
                    ) {

                        if (
                                !result.next()
                        ) {

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Current password is incorrect.",
                                    "Password Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }
                }

                try (
                        PreparedStatement update =
                                connection.prepareStatement(
                                        updateSql
                                )
                ) {

                    update.setString(
                            1,
                            newPassword
                    );

                    update.setString(
                            2,
                            username
                    );

                    int changed =
                            update.executeUpdate();

                    if (
                            changed > 0
                    ) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Password changed successfully.",
                                "Password Updated",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Unable to change password.",
                                "Update Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database error while changing password.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CREATE PASSWORD FIELD
    // =========================================================

    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setBackground(
                DARK
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                CYAN
        );

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
                                BORDER
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        return field;
    }

    // =========================================================
    // DIALOG LABEL
    // =========================================================

    private JLabel createDialogLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setForeground(
                SECONDARY_TEXT
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        return label;
    }

    // =========================================================
    // LOAD STATISTICS
    // =========================================================

    private void loadStatistics() {

        String reportsSql =
                "SELECT COUNT(*) "
                        + "FROM reports "
                        + "WHERE username = ?";

        String claimsSql =
                "SELECT COUNT(*) "
                        + "FROM claims "
                        + "WHERE username = ?";

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
                            )
            ) {

                // =================================================
                // REPORT COUNT
                // =================================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        reportsSql
                                )
                ) {

                    statement.setString(
                            1,
                            username
                    );

                    try (
                            ResultSet result =
                                    statement.executeQuery()
                    ) {

                        if (
                                result.next()
                        ) {

                            reportsCountLabel.setText(
                                    String.valueOf(
                                            result.getInt(
                                                    1
                                            )
                                    )
                            );
                        }
                    }
                }

                // =================================================
                // CLAIM COUNT
                // =================================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        claimsSql
                                )
                ) {

                    statement.setString(
                            1,
                            username
                    );

                    try (
                            ResultSet result =
                                    statement.executeQuery()
                    ) {

                        if (
                                result.next()
                        ) {

                            claimsCountLabel.setText(
                                    String.valueOf(
                                            result.getInt(
                                                    1
                                            )
                                    )
                            );
                        }
                    }
                }
            }

        } catch (
                Exception e
        ) {

            e.printStackTrace();

            reportsCountLabel.setText(
                    "—"
            );

            claimsCountLabel.setText(
                    "—"
            );
        }
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                BLUE
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        100,
                                        170,
                                        255
                                )
                        ),
                        new EmptyBorder(
                                10,
                                17,
                                10,
                                17
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        95,
                                        135,
                                        255
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                BLUE
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                new Color(
                        30,
                        45,
                        85
                )
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                18,
                                10,
                                18
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
                                new Color(
                                        30,
                                        45,
                                        85
                                )
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TRANSPARENT BUTTON
    // =========================================================

    private JButton createTransparentButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setForeground(
                SECONDARY_TEXT
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(
                false
        );

        button.setContentAreaFilled(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        8,
                        8,
                        15
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
    // AVATAR PANEL
    // =========================================================

    private static class AvatarPanel
            extends JPanel {

        AvatarPanel() {

            setOpaque(
                    false
            );
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(
                    g
            );

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size =
                    Math.min(
                            getWidth(),
                            getHeight()
                    ) - 8;

            int x =
                    (getWidth() - size)
                            / 2;

            int y =
                    (getHeight() - size)
                            / 2;

            GradientPaint gradient =
                    new GradientPaint(
                            x,
                            y,
                            CYAN,
                            x + size,
                            y + size,
                            PURPLE
                    );

            g2.setPaint(
                    gradient
            );

            g2.fill(
                    new Ellipse2D.Double(
                            x,
                            y,
                            size,
                            size
                    )
            );

            // =================================================
            // HEAD
            // =================================================

            g2.setColor(
                    WHITE
            );

            int headSize =
                    size / 4;

            int headX =
                    x
                            + (size - headSize)
                            / 2;

            int headY =
                    y
                            + size / 4;

            g2.fillOval(
                    headX,
                    headY,
                    headSize,
                    headSize
            );

            // =================================================
            // BODY
            // =================================================

            int bodyWidth =
                    size / 2;

            int bodyHeight =
                    size / 3;

            int bodyX =
                    x
                            + (size - bodyWidth)
                            / 2;

            int bodyY =
                    y
                            + size / 2;

            g2.fillRoundRect(
                    bodyX,
                    bodyY,
                    bodyWidth,
                    bodyHeight,
                    30,
                    30
            );

            g2.dispose();
        }
    }

    // =========================================================
    // GALAXY PANEL
    // =========================================================

    private static class GalaxyPanel
            extends JPanel {

        private final Star[] stars;

        GalaxyPanel() {

            setOpaque(
                    true
            );

            Random random =
                    new Random(
                            2026
                    );

            stars =
                    new Star[120];

            for (
                    int i = 0;
                    i < stars.length;
                    i++
            ) {

                stars[i] =
                        new Star(
                                random.nextFloat(),
                                random.nextFloat(),
                                1
                                        + random.nextFloat()
                                        * 2.5f,
                                0.3f
                                        + random.nextFloat()
                                        * 0.7f
                        );
            }
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(
                    g
            );

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            GALAXY_TOP,
                            getWidth(),
                            getHeight(),
                            GALAXY_BOTTOM
                    );

            g2.setPaint(
                    gradient
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            // =================================================
            // GALAXY GLOW
            // =================================================

            g2.setColor(
                    new Color(
                            40,
                            70,
                            190,
                            40
                    )
            );

            g2.fillOval(
                    -250,
                    100,
                    600,
                    500
            );

            g2.setColor(
                    new Color(
                            130,
                            40,
                            200,
                            30
                    )
            );

            g2.fillOval(
                    getWidth() - 400,
                    getHeight() - 400,
                    600,
                    500
            );

            // =================================================
            // STARS
            // =================================================

            for (
                    Star star
                    : stars
            ) {

                float x =
                        star.x
                                * getWidth();

                float y =
                        star.y
                                * getHeight();

                int alpha =
                        (int)
                                (
                                        255
                                                * star.brightness
                                );

                g2.setColor(
                        new Color(
                                190,
                                230,
                                255,
                                alpha
                        )
                );

                g2.fill(
                        new Ellipse2D.Float(
                                x,
                                y,
                                star.size,
                                star.size
                        )
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

        float size;

        float brightness;

        Star(
                float x,
                float y,
                float size,
                float brightness
        ) {

            this.x =
                    x;

            this.y =
                    y;

            this.size =
                    size;

            this.brightness =
                    brightness;
        }
    }

    // =========================================================
    // MODERN SCROLLBAR
    // =========================================================

    private static class ModernScrollBarUI
            extends javax.swing.plaf.basic.BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {

            thumbColor =
                    BLUE;

            trackColor =
                    DARK;
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

            button.setOpaque(
                    false
            );

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
                    (Graphics2D)
                            g.create();

            g2.setColor(
                    DARK
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
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    BLUE
            );

            g2.fillRoundRect(
                    thumbBounds.x + 2,
                    thumbBounds.y + 2,
                    thumbBounds.width - 4,
                    thumbBounds.height - 4,
                    10,
                    10
            );

            g2.dispose();
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

                    MyProfileFrame frame =
                            new MyProfileFrame(
                                    "student1"
                            );

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}
