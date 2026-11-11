package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LoginFrame extends JFrame {

    // =========================================================
    // FIELDS
    // =========================================================

    private JTextField usernameField;
    private JPasswordField passwordField;

    private MascotPanel mascotPanel;

    // USER or ADMIN
    private final String selectedRole;

    // =========================================================
    // DATABASE
    // =========================================================

    private final String url = database.DBConfig.DB_URL;
    private final String dbUsername = database.DBConfig.DB_USER;
    private final String dbPassword = database.DBConfig.DB_PASSWORD;

    // =========================================================
    // COLORS
    // =========================================================

    private final Color WHITE =
            Color.WHITE;

    private final Color LIGHT_BLUE =
            new Color(85, 190, 255);

    private final Color BLUE =
            new Color(70, 125, 245);

    private final Color DARK_BLUE =
            new Color(8, 20, 55, 235);

    private final Color FIELD_COLOR =
            new Color(10, 28, 65, 245);

    private final Color BORDER_BLUE =
            new Color(70, 170, 255);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginFrame(String role) {

        selectedRole =
                role == null
                        ? "USER"
                        : role.toUpperCase();

        buildUI();
    }

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public LoginFrame() {

        this("USER");
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        // -----------------------------------------------------
        // WINDOW
        // -----------------------------------------------------

        setTitle(
                selectedRole.equalsIgnoreCase("ADMIN")
                        ? "Admin Login - Smart Campus"
                        : "Student Login - Smart Campus"
        );

        setSize(
                1450,
                900
        );

        setMinimumSize(
                new Dimension(
                        1200,
                        800
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        // -----------------------------------------------------
        // GALAXY BACKGROUND
        // -----------------------------------------------------

        GalaxyBackground background =
                new GalaxyBackground();

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                0,
                                0
                        )
                );

        mainPanel.setOpaque(false);

        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel leftSide =
                new JPanel();

        leftSide.setOpaque(false);

        leftSide.setLayout(
                new BoxLayout(
                        leftSide,
                        BoxLayout.Y_AXIS
                )
        );

        leftSide.setBorder(
                new EmptyBorder(
                        55,
                        45,
                        25,
                        30
                )
        );

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel appTitle =
                new JLabel(
                        "<html>"
                                + "<div style='text-align:left;'>"
                                + "SMART CAMPUS"
                                + "<br>"
                                + "LOST &amp; FOUND"
                                + "</div>"
                                + "</html>"
                );

        appTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        46
                )
        );

        appTitle.setForeground(
                WHITE
        );

        appTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        leftSide.add(
                appTitle
        );

        leftSide.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // -----------------------------------------------------
        // DESCRIPTION
        // -----------------------------------------------------

        JLabel description =
                new JLabel(
                        "<html>"
                                + "<div style='text-align:left;'>"
                                + "Find what you lost."
                                + "<br>"
                                + "Return what you found."
                                + "</div>"
                                + "</html>"
                );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        22
                )
        );

        description.setForeground(
                new Color(
                        225,
                        235,
                        250
                )
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        leftSide.add(
                description
        );

        leftSide.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // ROBOT
        // =====================================================

        mascotPanel =
                new MascotPanel();

        mascotPanel.setPreferredSize(
                new Dimension(
                        560,
                        560
                )
        );

        mascotPanel.setMinimumSize(
                new Dimension(
                        560,
                        560
                )
        );

        mascotPanel.setMaximumSize(
                new Dimension(
                        560,
                        560
                )
        );

        mascotPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        leftSide.add(
                mascotPanel
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel rightOuter =
                new JPanel();

        rightOuter.setOpaque(false);

        rightOuter.setLayout(
                new GridBagLayout()
        );

        // =====================================================
        // LOGIN PANEL
        // =====================================================

        JPanel loginPanel =
                new JPanel();

        loginPanel.setOpaque(true);

        loginPanel.setBackground(
                DARK_BLUE
        );

        loginPanel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDER_BLUE,
                                1
                        ),

                        new EmptyBorder(
                                38,
                                55,
                                35,
                                55
                        )
                )
        );

        loginPanel.setPreferredSize(
                new Dimension(
                        590,
                        720
                )
        );

        loginPanel.setMinimumSize(
                new Dimension(
                        590,
                        720
                )
        );

        loginPanel.setMaximumSize(
                new Dimension(
                        590,
                        720
                )
        );

        loginPanel.setLayout(
                new BoxLayout(
                        loginPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // WELCOME
        // =====================================================

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome Back"
                );

        welcomeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        welcomeLabel.setForeground(
                WHITE
        );

        welcomeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                welcomeLabel
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitleLabel =
                new JLabel(
                        "Sign in to continue to your account."
                );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18
                )
        );

        subtitleLabel.setForeground(
                new Color(
                        215,
                        225,
                        245
                )
        );

        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                subtitleLabel
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // ROLE
        // =====================================================

        JLabel roleLabel =
                new JLabel(
                        selectedRole.equalsIgnoreCase("ADMIN")
                                ? "ADMIN PORTAL"
                                : "STUDENT PORTAL"
                );

        roleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        roleLabel.setForeground(
                LIGHT_BLUE
        );

        roleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                roleLabel
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        35
                )
        );

        // =====================================================
        // USERNAME LABEL
        // =====================================================

        JLabel usernameLabel =
                new JLabel(
                        "Username"
                );

        styleLabel(
                usernameLabel
        );

        usernameLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                usernameLabel
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // USERNAME FIELD
        // =====================================================

        usernameField =
                new JTextField();

        styleTextField(
                usernameField
        );

        loginPanel.add(
                usernameField
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        22
                )
        );

        // =====================================================
        // PASSWORD LABEL
        // =====================================================

        JLabel passwordLabel =
                new JLabel(
                        "Password"
                );

        styleLabel(
                passwordLabel
        );

        passwordLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                passwordLabel
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        passwordField =
                new JPasswordField();

        stylePasswordField(
                passwordField
        );

        loginPanel.add(
                passwordField
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        30
                )
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        JButton loginButton =
                new JButton(
                        selectedRole.equalsIgnoreCase("ADMIN")
                                ? "LOGIN AS ADMIN"
                                : "LOGIN AS STUDENT"
                );

        stylePrimaryButton(
                loginButton
        );

        loginButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                loginButton
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        10
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

        clearButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                clearButton
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        28
                )
        );

        // =====================================================
        // ACCOUNT BUTTON
        // =====================================================

        JButton accountButton;

        if (
                selectedRole.equalsIgnoreCase(
                        "ADMIN"
                )
        ) {

            accountButton =
                    createLinkButton(
                            "Create Admin Account"
                    );

            accountButton.addActionListener(
                    e -> {

                        mascotPanel.setMessage(
                                "🧑‍💼 Let's create your admin account!"
                        );

                        AdminRegisterFrame adminRegisterFrame =
                                new AdminRegisterFrame();

                        adminRegisterFrame.setLocationRelativeTo(this);
                        adminRegisterFrame.setVisible(true);

                        dispose();
                    }
            );

        } else {

            accountButton =
                    createLinkButton(
                            "Create Student Account"
                    );

            accountButton.addActionListener(
                    e -> {

                        mascotPanel.setMessage(
                                "👋 Let's create your student account!"
                        );

                        dispose();

                        RegisterFrame registerFrame =
                                new RegisterFrame();

                        registerFrame.setLocationRelativeTo(
                                null
                        );

                        registerFrame.setVisible(
                                true
                        );
                    }
            );
        }

        accountButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                accountButton
        );

        loginPanel.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        JButton backButton =
                createLinkButton(
                        "← Back to Role Selection"
                );

        backButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(
                backButton
        );

        backButton.addActionListener(
                e -> {

                    mascotPanel.setMessage(
                            "↩️ Let's choose another portal."
                    );

                    dispose();

                    LandingFrame landingFrame =
                            new LandingFrame();

                    landingFrame.setLocationRelativeTo(
                            null
                    );

                    landingFrame.setVisible(
                            true
                    );
                }
        );

        // =====================================================
        // ADD LOGIN PANEL
        // =====================================================

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.anchor =
                GridBagConstraints.CENTER;

        rightOuter.add(
                loginPanel,
                gbc
        );

        // =====================================================
        // ADD SIDES
        // =====================================================

        mainPanel.add(
                leftSide
        );

        mainPanel.add(
                rightOuter
        );

        background.add(
                mainPanel,
                BorderLayout.CENTER
        );

        add(
                background
        );

        // =====================================================
        // DEFAULT ROBOT MESSAGE
        // =====================================================

        mascotPanel.setMessage(
                "😊 Hi! I'm your campus assistant!"
        );

        // =====================================================
        // USERNAME FOCUS
        // =====================================================

        usernameField.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        mascotPanel.setMessage(
                                "😊 Enter your username to continue."
                        );
                    }
                }
        );

        // =====================================================
        // PASSWORD FOCUS
        // =====================================================

        passwordField.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        mascotPanel.setMessage(
                                "🔐 Keep your password secure."
                        );
                    }
                }
        );

        // =====================================================
        // LOGIN HOVER
        // =====================================================

        loginButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "🚀 Ready? Click Login to continue!"
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "😊 I'm ready when you are!"
                        );
                    }
                }
        );

        // =====================================================
        // CLEAR HOVER
        // =====================================================

        clearButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "🧹 Click Clear to start fresh."
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "😊 I'm ready when you are!"
                        );
                    }
                }
        );

        // =====================================================
        // ACCOUNT HOVER
        // =====================================================

        accountButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (
                                selectedRole.equalsIgnoreCase(
                                        "ADMIN"
                                )
                        ) {

                            mascotPanel.setMessage(
                                    "🧑‍💼 New admin? Create an account here!"
                            );

                        } else {

                            mascotPanel.setMessage(
                                    "👋 New student? Create your account here!"
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "😊 I'm here to guide you!"
                        );
                    }
                }
        );

        // =====================================================
        // BACK HOVER
        // =====================================================

        backButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "↩️ Want to choose another portal?"
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "😊 I'm here to guide you!"
                        );
                    }
                }
        );

        // =====================================================
        // ROBOT HOVER
        // =====================================================

        mascotPanel.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "🤖 Hi! I'm your campus assistant! ✨"
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        mascotPanel.setMessage(
                                "💙 Move around and I'll guide you!"
                        );
                    }
                }
        );

        // =====================================================
        // LOGIN ACTION
        // =====================================================

        loginButton.addActionListener(
                e -> login()
        );

        // =====================================================
        // CLEAR ACTION
        // =====================================================
        clearButton.addActionListener(
                e -> {

                    usernameField.setText("");

                    passwordField.setText("");

                    usernameField.requestFocus();

                    mascotPanel.setMessage(
                            "🧹 Everything is cleared. Let's start again!"
                    );
                }
        );

        // =====================================================
        // ENTER KEY
        // =====================================================

        usernameField.addActionListener(
                e -> passwordField.requestFocus()
        );

        passwordField.addActionListener(
                e -> login()
        );

        // =====================================================
        // LIVE MOUSE INSTRUCTION SYSTEM
        // =====================================================

        startMouseInstructionTracker(
                loginButton,
                clearButton,
                backButton,
                accountButton
        );
    }

    // =========================================================
    // LIVE MOUSE MOVEMENT TRACKER
    // =========================================================

    private void startMouseInstructionTracker(
            JButton loginButton,
            JButton clearButton,
            JButton backButton,
            JButton accountButton
    ) {

        Timer mouseTimer =
                new Timer(
                        120,
                        e -> {

                            try {

                                PointerInfo pointerInfo =
                                        MouseInfo.getPointerInfo();

                                if (
                                        pointerInfo == null
                                ) {
                                    return;
                                }

                                Point mousePoint =
                                        pointerInfo.getLocation();

                                SwingUtilities.convertPointFromScreen(
                                        mousePoint,
                                        getContentPane()
                                );

                                Component component =
                                        SwingUtilities.getDeepestComponentAt(
                                                getContentPane(),
                                                mousePoint.x,
                                                mousePoint.y
                                        );

                                if (
                                        component == null
                                ) {
                                    return;
                                }

                                // =====================================
                                // USERNAME
                                // =====================================

                                if (
                                        isInside(
                                                component,
                                                usernameField
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "😊 Enter your username here."
                                    );

                                }

                                // =====================================
                                // PASSWORD
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                passwordField
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "🔐 Enter your password here."
                                    );

                                }

                                // =====================================
                                // LOGIN
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                loginButton
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "🚀 Click Login when you're ready!"
                                    );

                                }

                                // =====================================
                                // CLEAR
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                clearButton
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "🧹 Clear your details and start again."
                                    );

                                }

                                // =====================================
                                // CREATE ACCOUNT
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                accountButton
                                        )
                                ) {

                                    if (
                                            selectedRole.equalsIgnoreCase(
                                                    "ADMIN"
                                            )
                                    ) {

                                        mascotPanel.setMessage(
                                                "🧑‍💼 Create your admin account here."
                                        );

                                    } else {

                                        mascotPanel.setMessage(
                                                "👋 New student? Create your account here!"
                                        );
                                    }
                                }

                                // =====================================
                                // BACK
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                backButton
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "↩️ Go back and choose another portal."
                                    );

                                }

                                // =====================================
                                // ROBOT
                                // =====================================

                                else if (
                                        isInside(
                                                component,
                                                mascotPanel
                                        )
                                ) {

                                    mascotPanel.setMessage(
                                            "🤖 Hi! I'm your campus assistant! ✨"
                                    );

                                }

                                // =====================================
                                // GENERAL AREA
                                // =====================================

                                else {

                                    mascotPanel.setMessage(
                                            "💙 Move around! I'll guide you through the login."
                                    );
                                }

                            } catch (
                                    Exception ignored
                            ) {

                                // Prevent mouse tracking from
                                // affecting the main application.
                            }
                        }
                );

        mouseTimer.start();
    }

    // =========================================================
    // CHECK WHETHER MOUSE IS INSIDE COMPONENT
    // =========================================================

    private boolean isInside(
            Component current,
            Component target
    ) {

        if (
                current == null
                        ||
                        target == null
        ) {

            return false;
        }

        return current == target
                ||
                SwingUtilities.isDescendingFrom(
                        current,
                        target
                );
    }

    // =========================================================
    // LABEL STYLE
    // =========================================================

    private void styleLabel(
            JLabel label
    ) {

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        label.setForeground(
                WHITE
        );
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                WHITE
        );

        field.setBackground(
                FIELD_COLOR
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDER_BLUE,
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
                )
        );

        field.setMaximumSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setPreferredSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setMinimumSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );
    }

    // =========================================================
    // PASSWORD FIELD STYLE
    // =========================================================

    private void stylePasswordField(
            JPasswordField field
    ) {

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                WHITE
        );

        field.setBackground(
                FIELD_COLOR
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDER_BLUE,
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
                )
        );

        field.setMaximumSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setPreferredSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setMinimumSize(
                new Dimension(
                        500,
                        52
                )
        );

        field.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );
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
                        18
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
                        13,
                        25,
                        13,
                        25
                )
        );

        button.setMaximumSize(
                new Dimension(
                        500,
                        58
                )
        );

        button.setPreferredSize(
                new Dimension(
                        500,
                        58
                )
        );

        button.setMinimumSize(
                new Dimension(
                        500,
                        58
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
                        17
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                new Color(
                        28,
                        55,
                        105
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_BLUE,
                        1
                )
        );

        button.setMaximumSize(
                new Dimension(
                        500,
                        50
                )
        );

        button.setPreferredSize(
                new Dimension(
                        500,
                        50
                )
        );

        button.setMinimumSize(
                new Dimension(
                        500,
                        50
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

    private JButton createLinkButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        button.setForeground(
                LIGHT_BLUE
        );

        button.setBackground(
                new Color(
                        0,
                        0,
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

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void login() {

        String enteredUsername =
                usernameField
                        .getText()
                        .trim();

        String enteredPassword =
                new String(
                        passwordField.getPassword()
                );

        // =====================================================
        // USERNAME VALIDATION
        // =====================================================

        if (
                enteredUsername.isEmpty()
        ) {

            mascotPanel.setMessage(
                    "😟 Please enter your username first."
            );

            ModernDialog.showError(
                    this,
                    "Missing Username",
                    "Please enter your username to continue."
            );

            usernameField.requestFocus();

            return;
        }

        // =====================================================
        // PASSWORD VALIDATION
        // =====================================================

        if (
                enteredPassword.isEmpty()
        ) {

            mascotPanel.setMessage(
                    "😟 Please enter your password."
            );

            ModernDialog.showError(
                    this,
                    "Missing Password",
                    "Please enter your password to continue."
            );

            passwordField.requestFocus();

            return;
        }

        // =====================================================
        // REQUIRED ROLE
        // =====================================================

        String requiredRole =
                selectedRole.equalsIgnoreCase(
                        "ADMIN"
                )
                        ? "ADMIN"
                        : "STUDENT";

        // =====================================================
        // SQL
        // =====================================================

        String sql =
                "SELECT full_name, role FROM users "
                        + "WHERE username = ? "
                        + "AND password = ? "
                        + "AND role = ?";

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            try (
                    Connection connection =
                            DriverManager.getConnection(
                                    url,
                                    dbUsername,
                                    dbPassword
                            );

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sql
                            )
            ) {

                statement.setString(
                        1,
                        enteredUsername
                );

                statement.setString(
                        2,
                        enteredPassword
                );

                statement.setString(
                        3,
                        requiredRole
                );

                try (
                        ResultSet result =
                                statement.executeQuery()
                ) {

                    // =================================================
                    // LOGIN SUCCESS
                    // =================================================

                    if (
                            result.next()
                    ) {

                        String fullName =
                                result.getString(
                                        "full_name"
                                );

                        String actualRole =
                                result.getString(
                                        "role"
                                );

                        if (
                                fullName == null
                                        || fullName.trim().isEmpty()
                        ) {
                            fullName = enteredUsername;
                        }

                        mascotPanel.setMessage(
                                "😊 Welcome "
                                        + fullName
                                        + "! You have logged in successfully!"
                        );

                        ModernDialog.showSuccess(
                                this,
                                "Login Successful 😊",
                                "Welcome "
                                        + fullName
                                        + "!\n\n"
                                        + "You have logged in successfully."
                        );

                        dispose();

                        // =================================================
                        // STUDENT
                        // =================================================

                        if (
                                actualRole.equalsIgnoreCase(
                                        "STUDENT"
                                )
                        ) {

                            StudentDashboard dashboard =
                                    new StudentDashboard(
                                            fullName,
                                            enteredUsername
                                    );

                            dashboard.setLocationRelativeTo(
                                    null
                            );

                            dashboard.setVisible(
                                    true
                            );
                        }

                        // =================================================
                        // ADMIN
                        // =================================================

                        else if (
                                actualRole.equalsIgnoreCase(
                                        "ADMIN"
                                )
                        ) {

                            AdminDashboard dashboard =
                                    new AdminDashboard(
                                            enteredUsername
                                    );

                            dashboard.setLocationRelativeTo(
                                    null
                            );

                            dashboard.setVisible(
                                    true
                            );
                        }

                    }

                    // =================================================
                    // LOGIN FAILED
                    // =================================================

                    else {

                        mascotPanel.setMessage(
                                "😢 Oops! Incorrect username or password."
                        );

                        ModernDialog.showError(
                                this,
                                "Login Failed 😢",
                                "Incorrect username or password."
                        );

                        passwordField.setText("");

                        passwordField.requestFocus();
                    }
                }
            }

        } catch (
                ClassNotFoundException e
        ) {

            mascotPanel.setMessage(
                    "😟 MySQL driver could not be found."
            );

            ModernDialog.showError(
                    this,
                    "JDBC Driver Error",
                    "MySQL Connector/J is not connected to the project."
            );

            e.printStackTrace();

        } catch (
                SQLException e
        ) {

            mascotPanel.setMessage(
                    "😟 I couldn't connect to the database."
            );

            ModernDialog.showError(
                    this,
                    "Database Error",
                    "Could not connect to MySQL.\n\n"
                            + e.getMessage()
            );

            e.printStackTrace();

        } catch (
                Exception e
        ) {

            mascotPanel.setMessage(
                    "😟 Something went wrong. Please try again."
            );

            ModernDialog.showError(
                    this,
                    "Unexpected Error",
                    e.getMessage()
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

                    LoginFrame frame =
                            new LoginFrame(
                                    "USER"
                            );

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}