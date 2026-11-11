package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterFrame extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private final String url = database.DBConfig.DB_URL;

    private final String dbUsername = database.DBConfig.DB_USER;

    private final String dbPassword = database.DBConfig.DB_PASSWORD;


    // =========================================================
    // FORM FIELDS
    // =========================================================

    private JTextField nameField;
    private JTextField usernameField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;


    // =========================================================
    // COLORS
    // =========================================================

    private final Color WHITE =
            Color.WHITE;

    private final Color LIGHT_BLUE =
            new Color(190, 220, 255);

    private final Color BLUE =
            new Color(72, 125, 245);

    private final Color DARK_FIELD =
            new Color(9, 25, 60);

    private final Color CARD_COLOR =
            new Color(5, 18, 50, 245);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RegisterFrame() {

        setTitle(
                "Create Student Account - Smart Campus"
        );

        setSize(
                1100,
                720
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // Open the registration window maximized.
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        // =====================================================
        // GALAXY BACKGROUND
        // =====================================================

        GalaxyBackground background =
                new GalaxyBackground();

        background.setLayout(
                new BorderLayout()
        );


        // =====================================================
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2
                        )
                );

        mainPanel.setOpaque(false);


        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel leftPanel =
                new JPanel();

        leftPanel.setOpaque(false);

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );

        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        95,
                        35,
                        40,
                        25
                )
        );


        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel(
                        "<html>" +
                                "<div style='text-align:center;'>" +
                                "SMART CAMPUS<br>" +
                                "LOST &amp; FOUND" +
                                "</div>" +
                                "</html>"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        titleLabel.setForeground(
                WHITE
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(
                titleLabel
        );


        leftPanel.add(
                Box.createVerticalStrut(
                        30
                )
        );


        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel descriptionLabel =
                new JLabel(
                        "<html>" +
                                "<div style='text-align:center;'>" +
                                "Create your campus account.<br>" +
                                "Report, discover and recover lost items." +
                                "</div>" +
                                "</html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        19
                )
        );

        descriptionLabel.setForeground(
                LIGHT_BLUE
        );

        descriptionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        leftPanel.add(
                descriptionLabel
        );


        leftPanel.add(
                Box.createVerticalStrut(
                        40
                )
        );


        // =====================================================
        // INFORMATION BOX
        // =====================================================

        JPanel infoBox =
                new JPanel(
                        new GridBagLayout()
                );

        infoBox.setOpaque(false);

        infoBox.setPreferredSize(
                new Dimension(
                        400,
                        130
                )
        );

        infoBox.setMaximumSize(
                new Dimension(
                        400,
                        130
                )
        );

        infoBox.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        65,
                                        160,
                                        255
                                ),
                                2
                        ),

                        BorderFactory.createEmptyBorder(
                                12,
                                18,
                                12,
                                18
                        )
                )
        );

        infoBox.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel infoLabel =
                new JLabel(
                        "<html>" +
                                "<div style='text-align:center;'>" +
                                "<b>Student Registration</b><br><br>" +
                                "Create your account and start using<br>" +
                                "the Smart Campus Lost & Found system." +
                                "</div>" +
                                "</html>"
                );

        infoLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        infoLabel.setForeground(
                WHITE
        );

        infoBox.add(
                infoLabel
        );

        leftPanel.add(
                infoBox
        );


        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel rightPanel =
                new JPanel(
                        new GridBagLayout()
                );

        rightPanel.setOpaque(false);

        rightPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        25
                )
        );


        // =====================================================
        // ACCOUNT CARD
        // =====================================================

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_COLOR
        );

        card.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                70,
                                155,
                                255
                        ),
                        1
                )
        );

        card.setPreferredSize(
                new Dimension(
                        520,
                        670
                )
        );

        card.setMaximumSize(
                new Dimension(
                        520,
                        670
                )
        );


        // =====================================================
        // CARD CONTENT
        // =====================================================

        JPanel cardContent =
                new JPanel();

        cardContent.setOpaque(false);

        cardContent.setLayout(
                new BoxLayout(
                        cardContent,
                        BoxLayout.Y_AXIS
                )
        );

        cardContent.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        30,
                        12,
                        30
                )
        );


        // =====================================================
        // TITLE
        // =====================================================

        JLabel cardTitle =
                new JLabel(
                        "Create Student Account"
                );

        cardTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        cardTitle.setForeground(
                WHITE
        );

        cardTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cardContent.add(
                cardTitle
        );


        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel cardSubtitle =
                new JLabel(
                        "Join the Smart Campus Lost & Found system"
                );

        cardSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        cardSubtitle.setForeground(
                LIGHT_BLUE
        );

        cardSubtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cardContent.add(
                Box.createVerticalStrut(
                        5
                )
        );

        cardContent.add(
                cardSubtitle
        );


        cardContent.add(
                Box.createVerticalStrut(
                        17
                )
        );


        // =====================================================
        // FORM PANEL
        // =====================================================

        JPanel formPanel =
                new JPanel();

        formPanel.setOpaque(false);

        formPanel.setLayout(
                new BoxLayout(
                        formPanel,
                        BoxLayout.Y_AXIS
                )
        );

        formPanel.setPreferredSize(
                new Dimension(
                        390,
                        365
                )
        );

        formPanel.setMaximumSize(
                new Dimension(
                        390,
                        365
                )
        );

        formPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        // =====================================================
        // FULL NAME
        // =====================================================

        JLabel nameLabel =
                createLabel(
                        "Full Name"
                );

        formPanel.add(
                nameLabel
        );

        formPanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        nameField =
                createTextField();

        formPanel.add(
                nameField
        );

        formPanel.add(
                Box.createVerticalStrut(
                        9
                )
        );


        // =====================================================
        // USERNAME
        // =====================================================

        JLabel usernameLabel =
                createLabel(
                        "Username"
                );

        formPanel.add(
                usernameLabel
        );

        formPanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        usernameField =
                createTextField();

        formPanel.add(
                usernameField
        );

        formPanel.add(
                Box.createVerticalStrut(
                        9
                )
        );


        // =====================================================
        // PASSWORD
        // =====================================================

        JLabel passwordLabel =
                createLabel(
                        "Password"
                );

        formPanel.add(
                passwordLabel
        );

        formPanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        passwordField =
                createPasswordField();

        formPanel.add(
                passwordField
        );

        formPanel.add(
                Box.createVerticalStrut(
                        9
                )
        );


        // =====================================================
        // CONFIRM PASSWORD
        // =====================================================

        JLabel confirmLabel =
                createLabel(
                        "Confirm Password"
                );

        formPanel.add(
                confirmLabel
        );

        formPanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        confirmPasswordField =
                createPasswordField();

        formPanel.add(
                confirmPasswordField
        );


        // =====================================================
        // ADD FORM
        // =====================================================

        cardContent.add(
                formPanel
        );


        cardContent.add(
                Box.createVerticalStrut(
                        5
                )
        );


        // =====================================================
        // CREATE ACCOUNT BUTTON
        // =====================================================

        JButton createButton =
                new JButton(
                        "CREATE ACCOUNT"
                );

        stylePrimaryButton(
                createButton
        );

        createButton.setPreferredSize(
                new Dimension(
                        390,
                        52
                )
        );

        createButton.setMinimumSize(
                new Dimension(
                        390,
                        52
                )
        );

        createButton.setMaximumSize(
                new Dimension(
                        390,
                        52
                )
        );

        createButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cardContent.add(
                createButton
        );


        // =====================================================
        // SMALL GAP
        // =====================================================

        cardContent.add(
                Box.createVerticalStrut(
                        7
                )
        );


        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        JButton clearButton =
                new JButton(
                        "Clear"
                );

        styleSecondaryButton(
                clearButton
        );

        clearButton.setPreferredSize(
                new Dimension(
                        390,
                        40
                )
        );

        clearButton.setMinimumSize(
                new Dimension(
                        390,
                        40
                )
        );

        clearButton.setMaximumSize(
                new Dimension(
                        390,
                        40
                )
        );

        clearButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cardContent.add(
                clearButton
        );


        // =====================================================
        // BACK LINK
        // =====================================================

        cardContent.add(
                Box.createVerticalStrut(
                        5
                )
        );

        JButton backButton =
                new JButton(
                        "← Back to Student Login"
                );

        styleLinkButton(
                backButton
        );

        backButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        cardContent.add(
                backButton
        );


        // =====================================================
        // ADD CARD CONTENT
        // =====================================================

        card.add(
                cardContent,
                BorderLayout.CENTER
        );


        // =====================================================
        // CENTER CARD
        // =====================================================

        rightPanel.add(
                card
        );


        // =====================================================
        // MAIN PANEL
        // =====================================================

        mainPanel.add(
                leftPanel
        );

        mainPanel.add(
                rightPanel
        );

        background.add(
                mainPanel,
                BorderLayout.CENTER
        );

        add(
                background
        );


        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        createButton.addActionListener(
                e -> createAccount()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        backButton.addActionListener(
                e -> {

                    dispose();

                    LoginFrame loginFrame =
                            new LoginFrame("USER");

                    loginFrame.setLocationRelativeTo(
                            null
                    );

                    loginFrame.setVisible(
                            true
                    );
                }
        );

        confirmPasswordField.addActionListener(
                e -> createAccount()
        );
    }


    // =========================================================
    // LABEL
    // =========================================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        label.setForeground(
                WHITE
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }


    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                WHITE
        );

        field.setBackground(
                DARK_FIELD
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        65,
                                        150,
                                        240
                                ),
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        field.setPreferredSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setMinimumSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setMaximumSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return field;
    }


    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                WHITE
        );

        field.setBackground(
                DARK_FIELD
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        65,
                                        150,
                                        240
                                ),
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        field.setPreferredSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setMinimumSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setMaximumSize(
                new Dimension(
                        390,
                        50
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return field;
    }


    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private void stylePrimaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                BLUE
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        20,
                        5,
                        20
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }


    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    private void styleSecondaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                new Color(
                        28,
                        57,
                        105
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                65,
                                150,
                                240
                        ),
                        1
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }


    // =========================================================
    // LINK BUTTON
    // =========================================================

    private void styleLinkButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        button.setForeground(
                new Color(
                        75,
                        190,
                        255
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

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }


    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        nameField.setText("");

        usernameField.setText("");

        passwordField.setText("");

        confirmPasswordField.setText("");

        nameField.requestFocus();
    }


    // =========================================================
    // CREATE ACCOUNT
    // =========================================================

    private void createAccount() {

        String name =
                nameField
                        .getText()
                        .trim();

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );


        // =====================================================
        // VALIDATION
        // =====================================================

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your full name.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            nameField.requestFocus();

            return;
        }


        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a username.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            usernameField.requestFocus();

            return;
        }


        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a password.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }


        if (confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please confirm your password.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }


        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            confirmPasswordField.requestFocus();

            return;
        }


        // =====================================================
        // SQL
        // =====================================================

        String checkSql =
                "SELECT id FROM users WHERE username = ?";


        /*
         * IMPORTANT CHANGE:
         *
         * Previously:
         *
         * INSERT INTO users
         * (username, password, role)
         *
         * Now:
         *
         * INSERT INTO users
         * (full_name, username, password, role)
         *
         * This stores the name entered by the user.
         */

        String insertSql =
                "INSERT INTO users " +
                        "(full_name, username, password, role) " +
                        "VALUES (?, ?, ?, 'STUDENT')";


        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );


            Connection connection =
                    DriverManager.getConnection(
                            url,
                            dbUsername,
                            dbPassword
                    );


            // =================================================
            // CHECK USERNAME
            // =================================================

            PreparedStatement checkStatement =
                    connection.prepareStatement(
                            checkSql
                    );

            checkStatement.setString(
                    1,
                    username
            );


            ResultSet result =
                    checkStatement.executeQuery();


            if (result.next()) {

                result.close();

                checkStatement.close();

                connection.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Username already exists.\n" +
                                "Please choose another username.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );

                usernameField.requestFocus();

                return;
            }


            result.close();

            checkStatement.close();


            // =================================================
            // INSERT USER
            // =================================================

            PreparedStatement insertStatement =
                    connection.prepareStatement(
                            insertSql
                    );


            // Full Name
            insertStatement.setString(
                    1,
                    name
            );


            // User-chosen Username
            insertStatement.setString(
                    2,
                    username
            );


            // User-chosen Password
            insertStatement.setString(
                    3,
                    password
            );


            insertStatement.executeUpdate();


            insertStatement.close();

            connection.close();


            // =================================================
            // SUCCESS
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Student account created successfully!\n\n" +
                            "Name: " + name +
                            "\nUsername: " + username +
                            "\nRole: STUDENT",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );


            dispose();


            LoginFrame loginFrame =
                    new LoginFrame("USER");

            loginFrame.setLocationRelativeTo(
                    null
            );

            loginFrame.setVisible(
                    true
            );


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error:\n" +
                            e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
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

                    RegisterFrame frame =
                            new RegisterFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}