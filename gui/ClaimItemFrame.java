package gui;

import service.OwnershipVerificationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.*;
import java.util.Random;

public class ClaimItemFrame extends JFrame {

    // =========================================================
    // CLAIM INFORMATION
    // =========================================================

    private final int reportId;
    private final String username;


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
            new Color(17, 8, 48);

    private static final Color CARD =
            new Color(
                    9,
                    18,
                    48,
                    235
            );

    private static final Color CARD_LIGHT =
            new Color(
                    14,
                    27,
                    63,
                    245
            );

    private static final Color CYAN =
            new Color(
                    55,
                    210,
                    255
            );

    private static final Color BLUE =
            new Color(
                    75,
                    120,
                    255
            );

    private static final Color PURPLE =
            new Color(
                    145,
                    90,
                    255
            );

    private static final Color GREEN =
            new Color(
                    60,
                    220,
                    145
            );

    private static final Color RED =
            new Color(
                    245,
                    90,
                    110
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
                    180,
                    255,
                    150
            );


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ClaimItemFrame(
            int reportId,
            String username
    ) {

        this.reportId = reportId;
        this.username = username;

        setTitle(
                "Ownership Verification - Smart Campus Lost & Found"
        );

        setSize(
                760,
                650
        );

        setMinimumSize(
                new Dimension(
                        700,
                        600
                )
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        buildUI();
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        GalaxyPanel galaxyPanel =
                new GalaxyPanel();

        galaxyPanel.setLayout(
                new BorderLayout()
        );

        galaxyPanel.setBorder(
                new EmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel icon =
                new JLabel("✦");

        icon.setForeground(
                CYAN
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel title =
                new JLabel(
                        "Ownership Verification"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitle =
                new JLabel(
                        "Verify your ownership before submitting a claim"
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

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        header.add(icon);

        header.add(
                Box.createVerticalStrut(2)
        );

        header.add(title);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(subtitle);


        galaxyPanel.add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // MAIN GLASS CARD
        // =====================================================

        JPanel mainCard =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                CARD
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                22,
                                22
                        );

                        g2.setColor(
                                BORDER
                        );

                        g2.setStroke(
                                new BasicStroke(1.2f)
                        );

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                22,
                                22
                        );

                        g2.dispose();

                        super.paintComponent(g);
                    }
                };

        mainCard.setOpaque(false);

        mainCard.setBorder(
                new EmptyBorder(
                        20,
                        22,
                        20,
                        22
                )
        );


        // =====================================================
        // INFORMATION BOXES
        // =====================================================

        JPanel informationPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        informationPanel.setOpaque(false);


        JPanel reportPanel =
                createInfoBox(
                        "REPORT ID",
                        String.valueOf(
                                reportId
                        ),
                        CYAN
                );


        JPanel userPanel =
                createInfoBox(
                        "CLAIMANT",
                        username,
                        PURPLE
                );


        informationPanel.add(
                reportPanel
        );

        informationPanel.add(
                userPanel
        );


        mainCard.add(
                informationPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // CENTER CONTENT
        // =====================================================

        JPanel centerPanel =
                new JPanel();

        centerPanel.setOpaque(false);

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );


        // =====================================================
        // VERIFICATION TITLE
        // =====================================================

        JLabel verificationTitle =
                new JLabel(
                        "Why is ownership verification required?"
                );

        verificationTitle.setForeground(
                WHITE
        );

        verificationTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        verificationTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        centerPanel.add(
                verificationTitle
        );


        centerPanel.add(
                Box.createVerticalStrut(8)
        );


        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel instructionLabel =
                new JLabel(
                        "<html>"
                                + "Provide a specific detail that only "
                                + "the genuine owner is likely to know."
                                + "<br>"
                                + "<span style='color:#AAB9D7'>"
                                + "Examples: colour, sticker, scratch, "
                                + "unique mark, password hint, or special feature."
                                + "</span>"
                                + "</html>"
                );

        instructionLabel.setForeground(
                SECONDARY_TEXT
        );

        instructionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        instructionLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        centerPanel.add(
                instructionLabel
        );


        centerPanel.add(
                Box.createVerticalStrut(18)
        );


        // =====================================================
        // ANSWER LABEL
        // =====================================================

        JLabel answerLabel =
                new JLabel(
                        "Ownership Detail"
                );

        answerLabel.setForeground(
                WHITE
        );

        answerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        answerLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        centerPanel.add(
                answerLabel
        );


        centerPanel.add(
                Box.createVerticalStrut(8)
        );


        // =====================================================
        // ANSWER TEXT AREA
        // =====================================================

        JTextArea answerArea =
                new JTextArea();


        answerArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );


        answerArea.setForeground(
                WHITE
        );


        answerArea.setBackground(
                new Color(
                        5,
                        12,
                        32
                )
        );


        answerArea.setCaretColor(
                CYAN
        );


        answerArea.setLineWrap(
                true
        );


        answerArea.setWrapStyleWord(
                true
        );


        answerArea.setBorder(
                new EmptyBorder(
                        12,
                        14,
                        12,
                        14
                )
        );


        answerArea.setRows(5);


        // =====================================================
        // ANSWER SCROLL
        // =====================================================

        JScrollPane answerScroll =
                new JScrollPane(
                        answerArea
                );


        answerScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        answerScroll.setPreferredSize(
                new Dimension(
                        0,
                        125
                )
        );


        answerScroll.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );


        answerScroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                55,
                                180,
                                255,
                                120
                        ),
                        1
                )
        );


        answerScroll.setBackground(
                new Color(
                        5,
                        12,
                        32
                )
        );


        answerScroll.getViewport()
                .setBackground(
                        new Color(
                                5,
                                12,
                                32
                        )
                );


        // Modern scrollbar

        JScrollBar answerBar =
                answerScroll.getVerticalScrollBar();

        answerBar.setPreferredSize(
                new Dimension(
                        9,
                        0
                )
        );

        answerBar.setUI(
                new GalaxyScrollBarUI()
        );


        centerPanel.add(
                answerScroll
        );


        mainCard.add(
                centerPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);


        JButton cancelButton =
                createButton(
                        "Cancel",
                        new Color(
                                45,
                                55,
                                85
                        ),
                        WHITE
                );


        JButton submitButton =
                createButton(
                        "Submit Claim",
                        BLUE,
                        WHITE
                );


        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                submitButton
        );


        mainCard.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // ADD MAIN CARD
        // =====================================================

        galaxyPanel.add(
                mainCard,
                BorderLayout.CENTER
        );


        // =====================================================
        // FOOTER
        // =====================================================

        JLabel footer =
                new JLabel(
                        "🔐  Your ownership evidence will be reviewed by an administrator"
                );

        footer.setForeground(
                SECONDARY_TEXT
        );

        footer.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        JPanel footerPanel =
                new JPanel(
                        new BorderLayout()
                );

        footerPanel.setOpaque(false);

        footerPanel.setBorder(
                new EmptyBorder(
                        10,
                        0,
                        0,
                        0
                )
        );

        footerPanel.add(
                footer,
                BorderLayout.CENTER
        );


        galaxyPanel.add(
                footerPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        cancelButton.addActionListener(
                e -> dispose()
        );


        submitButton.addActionListener(
                e -> {

                    String answer =
                            answerArea
                                    .getText()
                                    .trim();


                    if (answer.isEmpty()) {

                        showModernDialog(
                                "Information Required",
                                "Please enter an ownership detail "
                                        + "before submitting your claim.",
                                Color.ORANGE
                        );

                        answerArea.requestFocus();

                        return;
                    }


                    submitClaim(
                            answer
                    );
                }
        );


        // =====================================================
        // SET CONTENT
        // =====================================================

        setContentPane(
                galaxyPanel
        );
    }


    // =========================================================
    // INFORMATION BOX
    // =========================================================

    private JPanel createInfoBox(
            String title,
            String value,
            Color accent
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
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        accent.getRed(),
                                        accent.getGreen(),
                                        accent.getBlue(),
                                        100
                                )
                        ),
                        new EmptyBorder(
                                10,
                                14,
                                10,
                                14
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


        panel.add(
                titleLabel
        );

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(
                valueLabel
        );


        return panel;
    }


    // =========================================================
    // CREATE BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );


        button.setForeground(
                foreground
        );


        button.setBackground(
                background
        );


        button.setFocusPainted(
                false
        );


        button.setBorderPainted(
                false
        );


        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        button.setBorder(
                new EmptyBorder(
                        11,
                        22,
                        11,
                        22
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

                        if (background.equals(BLUE)) {

                            button.setBackground(
                                    new Color(
                                            95,
                                            145,
                                            255
                                    )
                            );

                        } else {

                            button.setBackground(
                                    new Color(
                                            65,
                                            80,
                                            120
                                    )
                            );
                        }
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
    // SUBMIT CLAIM
    // =========================================================

    private void submitClaim(
            String answer
    ) {

        // ---------------------------------------------------------
        // 1. RUN AI-ASSISTED OWNERSHIP VERIFICATION
        // ---------------------------------------------------------
        // The AI result is used only as supporting information.
        // It does NOT approve or reject the claim.
        // The administrator will make the final decision.

        OwnershipVerificationService verificationService =
                new OwnershipVerificationService();

        OwnershipVerificationService.VerificationResult
                verificationResult =
                verificationService.verifyOwnership(
                        reportId,
                        answer
                );

        System.out.println(
                "\n===== AI OWNERSHIP VERIFICATION ====="
        );

        System.out.println(
                "Report ID: " + reportId
        );

        System.out.println(
                "Claimant: " + username
        );

        System.out.println(
                "Similarity Score: "
                        + String.format(
                        "%.2f",
                        verificationResult.getScore()
                )
                        + "%"
        );

        System.out.println(
                "Confidence: "
                        + verificationResult.getConfidence()
        );

        System.out.println(
                "Recommendation: "
                        + verificationResult.getRecommendation()
        );

        System.out.println(
                "Final Decision: ADMIN"
        );

        System.out.println(
                "=====================================\n"
        );


        // ---------------------------------------------------------
        // 2. SAVE CLAIM AS PENDING
        // ---------------------------------------------------------
        // Existing claim flow is preserved.
        // The AI result does not change the Pending status.

        String sql =
                "INSERT INTO claims "
                        + "(report_id, username, answer, status) "
                        + "VALUES (?, ?, ?, 'Pending')";


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

                statement.setInt(
                        1,
                        reportId
                );


                statement.setString(
                        2,
                        username
                );


                statement.setString(
                        3,
                        answer
                );


                int rows =
                        statement.executeUpdate();


                if (rows > 0) {

                    showModernDialog(
                            "Claim Submitted",
                            "Your ownership evidence has been "
                                    + "sent for administrator verification.\n\n"
                                    + "AI-assisted verification completed.\n"
                                    + "Claim Status: Pending\n\n"
                                    + "The administrator will make the final decision.",
                            GREEN
                    );


                    dispose();
                }
            }


        } catch (Exception e) {

            e.printStackTrace();


            showModernDialog(
                    "Database Error",
                    "Unable to submit your claim.\n\n"
                            + e.getMessage(),
                    RED
            );
        }
    }


    // =========================================================
    // MODERN MESSAGE DIALOG
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
                470,
                270
        );


        dialog.setLocationRelativeTo(
                this
        );


        dialog.setResizable(
                false
        );


        GalaxyPanel background =
                new GalaxyPanel();


        background.setLayout(
                new BorderLayout()
        );


        background.setBorder(
                new EmptyBorder(
                        22,
                        25,
                        20,
                        25
                )
        );


        // =====================================================
        // ICON
        // =====================================================

        JLabel icon =
                new JLabel(
                        accent == GREEN
                                ? "✓"
                                : accent == RED
                                ? "!"
                                : "!"
                );


        icon.setForeground(
                accent
        );


        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );


        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        // =====================================================
        // TITLE
        // =====================================================

        JLabel heading =
                new JLabel(
                        title
                );


        heading.setForeground(
                WHITE
        );


        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );


        heading.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        // =====================================================
        // MESSAGE
        // =====================================================

        JTextArea messageArea =
                new JTextArea(
                        message
                );


        messageArea.setEditable(
                false
        );


        messageArea.setLineWrap(
                true
        );


        messageArea.setWrapStyleWord(
                true
        );


        messageArea.setForeground(
                SECONDARY_TEXT
        );


        messageArea.setBackground(
                new Color(
                        8,
                        14,
                        35
                )
        );


        messageArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );


        messageArea.setBorder(
                new EmptyBorder(
                        12,
                        8,
                        12,
                        8
                )
        );


        // =====================================================
        // OK BUTTON
        // =====================================================

        JButton okButton =
                createButton(
                        "OK",
                        accent,
                        WHITE
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


        background.add(
                icon,
                BorderLayout.NORTH
        );


        background.add(
                heading,
                BorderLayout.CENTER
        );


        JPanel center =
                new JPanel(
                        new BorderLayout()
                );


        center.setOpaque(false);


        center.add(
                heading,
                BorderLayout.NORTH
        );


        center.add(
                messageArea,
                BorderLayout.CENTER
        );


        background.add(
                center,
                BorderLayout.CENTER
        );


        background.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        dialog.setContentPane(
                background
        );


        dialog.setVisible(
                true
        );
    }


    // =========================================================
    // GALAXY BACKGROUND
    // =========================================================

    private static class GalaxyPanel
            extends JPanel {

        private final Star[] stars;


        GalaxyPanel() {

            setOpaque(false);


            Random random =
                    new Random(42);


            stars =
                    new Star[110];


            for (
                    int i = 0;
                    i < stars.length;
                    i++
            ) {

                stars[i] =
                        new Star(
                                random.nextFloat(),
                                random.nextFloat(),
                                1 + random.nextFloat() * 2.5f,
                                0.35f
                                        + random.nextFloat()
                                        * 0.65f
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
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            // =================================================
            // BACKGROUND GRADIENT
            // =================================================

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            GALAXY_TOP,
                            0,
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
            // PURPLE NEBULA
            // =================================================

            RadialGradientPaint purpleGlow =
                    new RadialGradientPaint(
                            new Point(
                                    getWidth() * 3 / 4,
                                    getHeight() / 4
                            ),
                            Math.max(
                                    250,
                                    getWidth() / 2
                            ),
                            new float[]{
                                    0f,
                                    0.45f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            100,
                                            50,
                                            180,
                                            70
                                    ),
                                    new Color(
                                            70,
                                            30,
                                            140,
                                            25
                                    ),
                                    new Color(
                                            0,
                                            0,
                                            0,
                                            0
                                    )
                            }
                    );


            g2.setPaint(
                    purpleGlow
            );


            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );


            // =================================================
            // CYAN NEBULA
            // =================================================

            RadialGradientPaint cyanGlow =
                    new RadialGradientPaint(
                            new Point(
                                    getWidth() / 5,
                                    getHeight() * 3 / 4
                            ),
                            Math.max(
                                    250,
                                    getWidth() / 2
                            ),
                            new float[]{
                                    0f,
                                    0.5f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            20,
                                            120,
                                            200,
                                            45
                                    ),
                                    new Color(
                                            20,
                                            80,
                                            180,
                                            15
                                    ),
                                    new Color(
                                            0,
                                            0,
                                            0,
                                            0
                                    )
                            }
                    );


            g2.setPaint(
                    cyanGlow
            );


            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );


            // =================================================
            // STARS
            // =================================================

            for (Star star : stars) {

                float x =
                        star.x
                                * getWidth();

                float y =
                        star.y
                                * getHeight();


                float size =
                        star.size;


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
                                size,
                                size
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

            this.x = x;
            this.y = y;
            this.size = size;
            this.brightness = brightness;
        }
    }


    // =========================================================
    // GALAXY SCROLLBAR
    // =========================================================

    private static class GalaxyScrollBarUI
            extends BasicScrollBarUI {

        private final Color TRACK =
                new Color(
                        7,
                        12,
                        30
                );


        private final Color THUMB =
                new Color(
                        60,
                        90,
                        160
                );


        private final Color THUMB_HOVER =
                new Color(
                        75,
                        190,
                        255
                );


        @Override
        protected void configureScrollBarColors() {

            trackColor =
                    TRACK;

            thumbColor =
                    THUMB;
        }


        @Override
        protected JButton createDecreaseButton(
                int orientation
        ) {

            return createEmptyButton();
        }


        @Override
        protected JButton createIncreaseButton(
                int orientation
        ) {

            return createEmptyButton();
        }


        private JButton createEmptyButton() {

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


            button.setBorderPainted(
                    false
            );


            button.setContentAreaFilled(
                    false
            );


            button.setOpaque(
                    false
            );


            return button;
        }


        @Override
        protected void paintTrack(
                Graphics g,
                JComponent c,
                Rectangle bounds
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setColor(
                    TRACK
            );


            g2.fillRect(
                    bounds.x,
                    bounds.y,
                    bounds.width,
                    bounds.height
            );


            g2.dispose();
        }


        @Override
        protected void paintThumb(
                Graphics g,
                JComponent c,
                Rectangle bounds
        ) {

            if (
                    bounds.isEmpty()
                            ||
                            !scrollbar.isEnabled()
            ) {

                return;
            }


            Graphics2D g2 =
                    (Graphics2D) g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            Color color =
                    isThumbRollover()
                            ? THUMB_HOVER
                            : THUMB;


            g2.setColor(
                    color
            );


            g2.fillRoundRect(
                    bounds.x + 2,
                    bounds.y + 2,
                    bounds.width - 4,
                    bounds.height - 4,
                    8,
                    8
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

                    ClaimItemFrame frame =
                            new ClaimItemFrame(
                                    1,
                                    "student1"
                            );


                    frame.setVisible(
                            true
                    );


                    frame.toFront();

                    frame.requestFocus();
                }
        );
    }
}