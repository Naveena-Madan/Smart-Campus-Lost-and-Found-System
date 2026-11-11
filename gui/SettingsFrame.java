package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.*;
import java.util.Random;

public class SettingsFrame extends JFrame {

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
            new Color(5, 8, 25);

    private static final Color GALAXY_BOTTOM =
            new Color(17, 7, 48);

    private static final Color CARD_BG =
            new Color(10, 20, 55, 235);

    private static final Color CARD_HOVER =
            new Color(15, 30, 75, 245);

    private static final Color CYAN =
            new Color(45, 210, 255);

    private static final Color BLUE =
            new Color(75, 115, 255);

    private static final Color PURPLE =
            new Color(150, 85, 255);

    private static final Color GREEN =
            new Color(70, 225, 150);

    private static final Color RED =
            new Color(255, 90, 110);

    private static final Color WHITE =
            new Color(245, 248, 255);

    private static final Color SECONDARY_TEXT =
            new Color(170, 185, 215);

    private static final Color BORDER =
            new Color(55, 105, 175);

    private static final Color DARK =
            new Color(5, 12, 32);

    // =========================================================
    // USER
    // =========================================================

    private final String username;

    private String fullName;

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JLabel fullNameLabel;

    private JLabel usernameLabel;

    private JLabel roleLabel;

    private JCheckBox matchNotificationBox;

    private JCheckBox claimNotificationBox;

    private JCheckBox generalNotificationBox;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SettingsFrame(
            String username
    ) {

        this.username =
                username;

        this.fullName =
                username;

        setTitle(
                "Settings - Smart Campus"
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
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(
                null
        );

        // Open Settings maximized while preserving the existing UI and behavior.
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

                        String name =
                                result.getString(
                                        "full_name"
                                );

                        if (
                                name != null
                                        &&
                                        !name
                                                .trim()
                                                .isEmpty()
                        ) {

                            fullName =
                                    name;
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
    // BUILD UI
    // =========================================================

    private void buildUI() {

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

        header.setOpaque(
                false
        );

        header.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        20,
                        40
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

        header.add(
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
                        "Settings"
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
                        "Customize your Smart Campus experience"
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

        header.add(
                titlePanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // SETTINGS ICON
        // =====================================================

        JLabel settingsIcon =
                new JLabel(
                        "⚙"
                );

        settingsIcon.setForeground(
                CYAN
        );

        settingsIcon.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        28
                )
        );

        header.add(
                settingsIcon,
                BorderLayout.EAST
        );

        background.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel content =
                new JPanel();

        content.setOpaque(
                false
        );

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBorder(
                new EmptyBorder(
                        5,
                        55,
                        30,
                        55
                )
        );

        // =====================================================
        // ACCOUNT SETTINGS
        // =====================================================

        JPanel accountCard =
                createSectionCard();

        accountCard.add(
                createSectionHeader(
                        "👤",
                        "Account",
                        "Manage your Smart Campus account"
                ),
                BorderLayout.NORTH
        );

        JPanel accountContent =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        accountContent.setOpaque(
                false
        );

        fullNameLabel =
                new JLabel(
                        fullName
                );

        usernameLabel =
                new JLabel(
                        username
                );

        roleLabel =
                new JLabel(
                        "STUDENT"
                );

        accountContent.add(
                createInfoBox(
                        "FULL NAME",
                        fullNameLabel,
                        CYAN
                )
        );

        accountContent.add(
                createInfoBox(
                        "USERNAME",
                        usernameLabel,
                        BLUE
                )
        );

        accountContent.add(
                createInfoBox(
                        "ROLE",
                        roleLabel,
                        PURPLE
                )
        );

        accountCard.add(
                accountContent,
                BorderLayout.CENTER
        );

        JPanel accountButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                15
                        )
                );

        accountButtons.setOpaque(
                false
        );

        JButton editButton =
                createPrimaryButton(
                        "✎ Edit Profile"
                );

        JButton passwordButton =
                createSecondaryButton(
                        "🔐 Change Password"
                );

        editButton.addActionListener(
                e -> editProfile()
        );

        passwordButton.addActionListener(
                e -> changePassword()
        );

        accountButtons.add(
                editButton
        );

        accountButtons.add(
                passwordButton
        );

        accountCard.add(
                accountButtons,
                BorderLayout.SOUTH
        );

        content.add(
                accountCard
        );

        content.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // NOTIFICATION SETTINGS
        // =====================================================

        JPanel notificationCard =
                createSectionCard();

        notificationCard.add(
                createSectionHeader(
                        "🔔",
                        "Notifications",
                        "Choose which notifications you want to receive"
                ),
                BorderLayout.NORTH
        );

        JPanel notificationContent =
                new JPanel();

        notificationContent.setOpaque(
                false
        );

        notificationContent.setLayout(
                new BoxLayout(
                        notificationContent,
                        BoxLayout.Y_AXIS
                )
        );

        matchNotificationBox =
                createCheckBox(
                        "Smart Match Notifications",
                        "Notify me when a possible match is found.",
                        true
                );

        claimNotificationBox =
                createCheckBox(
                        "Claim Notifications",
                        "Notify me when my claim status changes.",
                        true
                );

        generalNotificationBox =
                createCheckBox(
                        "General Notifications",
                        "Receive important campus lost & found updates.",
                        true
                );

        notificationContent.add(
                matchNotificationBox
        );

        notificationContent.add(
                Box.createVerticalStrut(
                        12
                )
        );

        notificationContent.add(
                claimNotificationBox
        );

        notificationContent.add(
                Box.createVerticalStrut(
                        12
                )
        );

        notificationContent.add(
                generalNotificationBox
        );

        notificationCard.add(
                notificationContent,
                BorderLayout.CENTER
        );

        JPanel notificationButtonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                10
                        )
                );

        notificationButtonPanel.setOpaque(
                false
        );

        JButton saveNotificationButton =
                createPrimaryButton(
                        "✓ Save Preferences"
                );

        saveNotificationButton.addActionListener(
                e -> saveNotificationPreferences()
        );

        notificationButtonPanel.add(
                saveNotificationButton
        );

        notificationCard.add(
                notificationButtonPanel,
                BorderLayout.SOUTH
        );

        content.add(
                notificationCard
        );

        content.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // APPEARANCE
        // =====================================================

        JPanel appearanceCard =
                createSectionCard();

        appearanceCard.add(
                createSectionHeader(
                        "🎨",
                        "Appearance",
                        "Customize the look of Smart Campus"
                ),
                BorderLayout.NORTH
        );

        JPanel appearanceContent =
                new JPanel(
                        new BorderLayout()
                );

        appearanceContent.setOpaque(
                false
        );

        JPanel appearanceText =
                new JPanel();

        appearanceText.setOpaque(
                false
        );

        appearanceText.setLayout(
                new BoxLayout(
                        appearanceText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel themeTitle =
                new JLabel(
                        "Galaxy Theme"
                );

        themeTitle.setForeground(
                WHITE
        );

        themeTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JLabel themeDescription =
                new JLabel(
                        "Use the Smart Campus dark galaxy interface."
                );

        themeDescription.setForeground(
                SECONDARY_TEXT
        );

        themeDescription.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        appearanceText.add(
                themeTitle
        );

        appearanceText.add(
                Box.createVerticalStrut(
                        4
                )
        );

        appearanceText.add(
                themeDescription
        );

        JCheckBox galaxyTheme =
                createCheckBox(
                        "Enable Galaxy Theme",
                        "Keep the modern dark interface enabled.",
                        true
                );

        galaxyTheme.setSelected(
                true
        );

        appearanceContent.add(
                appearanceText,
                BorderLayout.CENTER
        );

        appearanceContent.add(
                galaxyTheme,
                BorderLayout.EAST
        );

        appearanceCard.add(
                appearanceContent,
                BorderLayout.CENTER
        );

        content.add(
                appearanceCard
        );

        content.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =====================================================
        // ABOUT
        // =====================================================

        JPanel aboutCard =
                createSectionCard();

        aboutCard.add(
                createSectionHeader(
                        "ℹ",
                        "About Smart Campus",
                        "Information about this application"
                ),
                BorderLayout.NORTH
        );

        JPanel aboutContent =
                new JPanel(
                        new BorderLayout()
                );

        aboutContent.setOpaque(
                false
        );

        JLabel aboutText =
                new JLabel(
                        "<html>"
                                + "<b>Smart Campus Lost & Found System</b>"
                                + "<br>"
                                + "A smart platform for reporting lost items, "
                                + "finding possible matches and managing claims."
                                + "<br><br>"
                                + "Version 1.0"
                                + "</html>"
                );

        aboutText.setForeground(
                SECONDARY_TEXT
        );

        aboutText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        aboutContent.add(
                aboutText,
                BorderLayout.CENTER
        );

        aboutCard.add(
                aboutContent,
                BorderLayout.CENTER
        );

        content.add(
                aboutCard
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        content
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
        // BOTTOM
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
        // CONTENT
        // =====================================================

        setContentPane(
                background
        );
    }

    // =========================================================
    // SECTION CARD
    // =========================================================

    private JPanel createSectionCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
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
                        230
                )
        );

        return card;
    }

    // =========================================================
    // SECTION HEADER
    // =========================================================

    private JPanel createSectionHeader(
            String icon,
            String title,
            String subtitle
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(
                false
        );

        panel.setLayout(
                new BorderLayout(
                        12,
                        0
                )
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
                        24
                )
        );

        JPanel textPanel =
                new JPanel();

        textPanel.setOpaque(
                false
        );

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
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

        JLabel subtitleLabel =
                new JLabel(
                        subtitle
                );

        subtitleLabel.setForeground(
                SECONDARY_TEXT
        );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        textPanel.add(
                titleLabel
        );

        textPanel.add(
                Box.createVerticalStrut(
                        3
                )
        );

        textPanel.add(
                subtitleLabel
        );

        panel.add(
                iconLabel,
                BorderLayout.WEST
        );

        panel.add(
                textPanel,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // INFO BOX
    // =========================================================

    private JPanel createInfoBox(
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                new Color(
                        8,
                        17,
                        45
                )
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
                                new Color(
                                        50,
                                        80,
                                        140
                                )
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
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

        value.setForeground(
                accent
        );

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        panel.add(
                titleLabel
        );

        panel.add(
                Box.createVerticalStrut(
                        5
                )
        );

        panel.add(
                value
        );

        return panel;
    }

    // =========================================================
    // CHECKBOX
    // =========================================================

    private JCheckBox createCheckBox(
            String title,
            String description,
            boolean selected
    ) {

        JCheckBox checkBox =
                new JCheckBox();

        checkBox.setSelected(
                selected
        );

        checkBox.setOpaque(
                false
        );

        checkBox.setFocusPainted(
                false
        );

        checkBox.setForeground(
                WHITE
        );

        checkBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        checkBox.setText(
                "<html>"
                        + "<b>"
                        + title
                        + "</b>"
                        + "<br>"
                        + "<span style='color:#AAB9D7;'>"
                        + description
                        + "</span>"
                        + "</html>"
        );

        checkBox.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return checkBox;
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

                    fullNameLabel.setText(
                            fullName
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Profile updated successfully.",
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
                new JPanel(
                        new GridLayout(
                                6,
                                1,
                                8,
                                8
                        )
                );

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

        panel.add(
                createDialogLabel(
                        "Current Password"
                )
        );

        panel.add(
                currentPassword
        );

        panel.add(
                createDialogLabel(
                        "New Password"
                )
        );

        panel.add(
                newPassword
        );

        panel.add(
                createDialogLabel(
                        "Confirm New Password"
                )
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
    // PASSWORD FIELD
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
    // SAVE NOTIFICATION PREFERENCES
    // =========================================================

    private void saveNotificationPreferences() {

        JOptionPane.showMessageDialog(
                this,
                "Notification preferences saved successfully.",
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // BACK TO DASHBOARD
    // =========================================================

    private void goBackToDashboard() {

        dispose();

        // -----------------------------------------------------
        // FIND EXISTING DASHBOARD
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // CREATE DASHBOARD IF NECESSARY
        // -----------------------------------------------------

        StudentDashboard dashboard =
                new StudentDashboard(
                        fullName,
                        username
                );

        dashboard.setLocationRelativeTo(
                null
        );

        dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);

        dashboard.setVisible(
                true
        );

        dashboard.toFront();

        dashboard.requestFocus();
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
    // GALAXY BACKGROUND
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

            // -------------------------------------------------
            // GRADIENT
            // -------------------------------------------------

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

            // -------------------------------------------------
            // GALAXY GLOW
            // -------------------------------------------------

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

            // -------------------------------------------------
            // STARS
            // -------------------------------------------------

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

                    SettingsFrame frame =
                            new SettingsFrame(
                                    "student1"
                            );

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}
