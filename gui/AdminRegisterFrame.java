package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AdminRegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    // =========================================================
    // DATABASE
    // =========================================================

    private final String DB_URL = database.DBConfig.DB_URL;

    private final String DB_USERNAME = database.DBConfig.DB_USER;

    private final String DB_PASSWORD = database.DBConfig.DB_PASSWORD;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminRegisterFrame() {

        setTitle("Admin Registration - Smart Campus Lost & Found");

        setSize(1450, 900);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);


        // =====================================================
        // GALAXY BACKGROUND
        // =====================================================

        GalaxyBackground background =
                new GalaxyBackground();

        background.setLayout(
                new GridLayout(1, 2)
        );


        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel leftPanel = new JPanel();

        leftPanel.setOpaque(false);

        leftPanel.setBorder(
                new EmptyBorder(
                        120,
                        70,
                        80,
                        50
                )
        );

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(
                        "<html>SMART CAMPUS<br>LOST & FOUND</html>"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        52
                )
        );

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        leftPanel.add(titleLabel);

        leftPanel.add(
                Box.createVerticalStrut(35)
        );


        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + "Create your administrator account.<br>"
                                + "Manage and verify campus lost & found activities."
                                + "</html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        22
                )
        );

        descriptionLabel.setForeground(
                new Color(
                        235,
                        240,
                        255
                )
        );

        descriptionLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        leftPanel.add(descriptionLabel);

        leftPanel.add(
                Box.createVerticalStrut(80)
        );


        // =====================================================
        // LEFT INFORMATION BOX
        // =====================================================

        JPanel infoBox =
                new JPanel(
                        new BorderLayout()
                );

        infoBox.setOpaque(false);

        infoBox.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        0,
                                        210,
                                        255
                                ),
                                2
                        ),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );


        JLabel infoTitle =
                new JLabel(
                        "Admin Registration",
                        SwingConstants.CENTER
                );

        infoTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        infoTitle.setForeground(Color.WHITE);


        JLabel infoText =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + "Create your administrator account<br>"
                                + "to manage reports, verify claims,<br>"
                                + "and maintain the campus system."
                                + "</div></html>",
                        SwingConstants.CENTER
                );

        infoText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        infoText.setForeground(
                new Color(
                        235,
                        240,
                        255
                )
        );


        infoBox.add(
                infoTitle,
                BorderLayout.NORTH
        );

        infoBox.add(
                infoText,
                BorderLayout.CENTER
        );


        infoBox.setMaximumSize(
                new Dimension(
                        580,
                        170
                )
        );

        infoBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        leftPanel.add(infoBox);

        background.add(leftPanel);


        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel rightPanel =
                new JPanel();

        rightPanel.setOpaque(false);

        rightPanel.setBorder(
                new EmptyBorder(
                        45,
                        35,
                        45,
                        70
                )
        );

        rightPanel.setLayout(
                new GridBagLayout()
        );


        // =====================================================
        // BIG RIGHT CARD
        // =====================================================

        JPanel card =
                new JPanel();

        card.setBackground(
                new Color(
                        5,
                        15,
                        45,
                        235
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        0,
                                        210,
                                        255
                                ),
                                2
                        ),
                        new EmptyBorder(
                                30,
                                40,
                                25,
                                40
                        )
                )
        );

        card.setLayout(
                new BorderLayout()
        );


        // =====================================================
        // TOP TITLE
        // =====================================================

        JPanel headingPanel =
                new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(
                new BoxLayout(
                        headingPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel heading =
                new JLabel(
                        "Create Admin Account",
                        SwingConstants.CENTER
                );

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        heading.setForeground(Color.WHITE);

        heading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitle =
                new JLabel(
                        "Create your administrator account",
                        SwingConstants.CENTER
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );

        subtitle.setForeground(
                new Color(
                        100,
                        240,
                        255
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        headingPanel.add(heading);

        headingPanel.add(
                Box.createVerticalStrut(8)
        );

        headingPanel.add(subtitle);


        card.add(
                headingPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // CENTERED FORM PANEL
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
                        430,
                        500
                )
        );

        formPanel.setMaximumSize(
                new Dimension(
                        430,
                        500
                )
        );


        // =====================================================
        // FULL NAME
        // =====================================================

        JLabel nameLabel =
                createLabel("Full Name");

        formPanel.add(nameLabel);

        formPanel.add(
                Box.createVerticalStrut(7)
        );


        nameField =
                createTextField();

        formPanel.add(nameField);

        formPanel.add(
                Box.createVerticalStrut(16)
        );


        // =====================================================
        // USERNAME
        // =====================================================

        JLabel usernameLabel =
                createLabel("Username");

        formPanel.add(usernameLabel);

        formPanel.add(
                Box.createVerticalStrut(7)
        );


        usernameField =
                createTextField();

        formPanel.add(usernameField);

        formPanel.add(
                Box.createVerticalStrut(16)
        );


        // =====================================================
        // PASSWORD
        // =====================================================

        JLabel passwordLabel =
                createLabel("Password");

        formPanel.add(passwordLabel);

        formPanel.add(
                Box.createVerticalStrut(7)
        );


        passwordField =
                createPasswordField();

        formPanel.add(passwordField);

        formPanel.add(
                Box.createVerticalStrut(16)
        );


        // =====================================================
        // CONFIRM PASSWORD
        // =====================================================

        JLabel confirmLabel =
                createLabel("Confirm Password");

        formPanel.add(confirmLabel);

        formPanel.add(
                Box.createVerticalStrut(7)
        );


        confirmPasswordField =
                createPasswordField();

        formPanel.add(confirmPasswordField);

        formPanel.add(
                Box.createVerticalStrut(22)
        );


        // =====================================================
        // CREATE BUTTON
        // =====================================================

        JButton createButton =
                new JButton(
                        "CREATE ADMIN ACCOUNT"
                );

        createButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        createButton.setForeground(Color.WHITE);

        createButton.setBackground(
                new Color(
                        0,
                        190,
                        240
                )
        );

        createButton.setFocusPainted(false);

        createButton.setBorderPainted(false);

        createButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        createButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        createButton.setMaximumSize(
                new Dimension(
                        430,
                        52
                )
        );

        createButton.addActionListener(
                e -> createAdminAccount()
        );


        formPanel.add(createButton);

        formPanel.add(
                Box.createVerticalStrut(20)
        );


        // =====================================================
        // ALREADY HAVE ACCOUNT
        // =====================================================

        JLabel alreadyLabel =
                new JLabel(
                        "Already have an admin account?",
                        SwingConstants.CENTER
                );

        alreadyLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        alreadyLabel.setForeground(
                new Color(
                        220,
                        230,
                        245
                )
        );

        alreadyLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        formPanel.add(alreadyLabel);

        formPanel.add(
                Box.createVerticalStrut(5)
        );


        // =====================================================
        // BACK BUTTON
        // =====================================================

        JButton backButton =
                new JButton(
                        "← Back to Admin Login"
                );

        backButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        backButton.setForeground(
                new Color(
                        80,
                        230,
                        255
                )
        );

        backButton.setBackground(
                new Color(
                        5,
                        15,
                        45
                )
        );

        backButton.setBorderPainted(false);

        backButton.setFocusPainted(false);

        backButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        backButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        backButton.addActionListener(
                e -> {

                    LoginFrame loginFrame =
                            new LoginFrame("ADMIN");

                    loginFrame.setLocationRelativeTo(
                            this
                    );

                    loginFrame.setVisible(true);

                    dispose();
                }
        );


        formPanel.add(backButton);


        // =====================================================
        // CENTER FORM
        // =====================================================

        JPanel centerWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        centerWrapper.setOpaque(false);

        centerWrapper.add(formPanel);


        card.add(
                centerWrapper,
                BorderLayout.CENTER
        );


        // =====================================================
        // ADD CARD
        // =====================================================

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.fill =
                GridBagConstraints.BOTH;


        rightPanel.add(
                card,
                gbc
        );


        background.add(rightPanel);


        setContentPane(background);
    }


    // =========================================================
    // LABEL
    // =========================================================

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        label.setForeground(Color.WHITE);

        // LEFT ALIGNMENT FOR LABEL TEXT
        label.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        label.setMaximumSize(
                new Dimension(
                        430,
                        25
                )
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

        field.setForeground(Color.WHITE);

        field.setBackground(
                new Color(
                        15,
                        35,
                        70
                )
        );

        field.setCaretColor(Color.WHITE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        60,
                                        200,
                                        255
                                ),
                                1
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        field.setMaximumSize(
                new Dimension(
                        430,
                        45
                )
        );

        field.setPreferredSize(
                new Dimension(
                        430,
                        45
                )
        );

        field.setAlignmentX(
                Component.CENTER_ALIGNMENT
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

        field.setForeground(Color.WHITE);

        field.setBackground(
                new Color(
                        15,
                        35,
                        70
                )
        );

        field.setCaretColor(Color.WHITE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        60,
                                        200,
                                        255
                                ),
                                1
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        field.setMaximumSize(
                new Dimension(
                        430,
                        45
                )
        );

        field.setPreferredSize(
                new Dimension(
                        430,
                        45
                )
        );

        field.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return field;
    }


    // =========================================================
    // CREATE ADMIN ACCOUNT
    // =========================================================

    private void createAdminAccount() {

        String name =
                nameField.getText().trim();

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );


        // =====================================================
        // EMPTY VALIDATION
        // =====================================================

        if (name.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            ModernDialog.showWarning(
                    this,
                    "Missing Information ⚠️",
                    "Please fill in all the fields."
            );

            return;
        }


        // =====================================================
        // PASSWORD VALIDATION
        // =====================================================

        if (!password.equals(confirmPassword)) {

            ModernDialog.showError(
                    this,
                    "Password Mismatch 😢",
                    "Password and Confirm Password do not match."
            );

            confirmPasswordField.setText("");

            return;
        }


        Connection connection = null;

        PreparedStatement checkStatement = null;

        PreparedStatement insertStatement = null;

        ResultSet resultSet = null;


        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );


            connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USERNAME,
                            DB_PASSWORD
                    );


            // =================================================
            // CHECK USERNAME
            // =================================================

            String checkSql =
                    "SELECT id FROM users WHERE username = ?";


            checkStatement =
                    connection.prepareStatement(
                            checkSql
                    );


            checkStatement.setString(
                    1,
                    username
            );


            resultSet =
                    checkStatement.executeQuery();


            if (resultSet.next()) {

                ModernDialog.showError(
                        this,
                        "Username Already Exists 😢",
                        "This username is already registered.\n"
                                + "Please choose another username."
                );

                usernameField.requestFocus();

                return;
            }


            // =================================================
            // INSERT ADMIN
            // =================================================

            String insertSql =
                    "INSERT INTO users "
                            + "(username, password, role) "
                            + "VALUES (?, ?, 'ADMIN')";


            insertStatement =
                    connection.prepareStatement(
                            insertSql
                    );


            insertStatement.setString(
                    1,
                    username
            );


            insertStatement.setString(
                    2,
                    password
            );


            int rows =
                    insertStatement.executeUpdate();


            if (rows > 0) {

                ModernDialog.showSuccess(
                        this,
                        "Admin Account Created 🟢",
                        "Welcome " + name + "!\n\n"
                                + "Your administrator account "
                                + "has been created successfully."
                );


                LoginFrame loginFrame =
                        new LoginFrame("ADMIN");


                loginFrame.setLocationRelativeTo(
                        this
                );

                loginFrame.setVisible(true);

                dispose();
            }


        } catch (ClassNotFoundException e) {

            ModernDialog.showError(
                    this,
                    "JDBC Error 😢",
                    "MySQL JDBC Driver was not found."
            );


        } catch (SQLException e) {

            ModernDialog.showError(
                    this,
                    "Database Error 😢",
                    "Could not create the admin account.\n\n"
                            + e.getMessage()
            );


        } finally {

            try {

                if (resultSet != null)
                    resultSet.close();

                if (checkStatement != null)
                    checkStatement.close();

                if (insertStatement != null)
                    insertStatement.close();

                if (connection != null)
                    connection.close();

            } catch (SQLException ignored) {
            }
        }
    }
}