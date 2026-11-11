package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LandingFrame extends JFrame {

    // =========================================================
    // SELECTED ROLE
    // =========================================================

    private static String selectedRole;

    // =========================================================
    // CARD LAYOUT
    // =========================================================

    private CardLayout cardLayout;

    private JPanel cardPanel;

    // =========================================================
    // MASCOT
    // =========================================================

    private MascotPanel mascotPanel;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LandingFrame() {

        setTitle(
                "Smart Campus Lost and Found"
        );

        setSize(
                1150,
                720
        );

        setMinimumSize(
                new Dimension(
                        950,
                        620
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // Open the landing window maximized without changing its existing UI.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setResizable(true);

        // =====================================================
        // CARD LAYOUT
        // =====================================================

        cardLayout =
                new CardLayout();

        cardPanel =
                new JPanel(
                        cardLayout
                );

        cardPanel.setOpaque(false);

        // =====================================================
        // CREATE PAGES
        // =====================================================

        JPanel welcomePage =
                createWelcomePage();

        JPanel rolePage =
                createRolePage();

        cardPanel.add(
                welcomePage,
                "WELCOME"
        );

        cardPanel.add(
                rolePage,
                "ROLE"
        );

        // =====================================================
        // GALAXY BACKGROUND
        // =====================================================

        GalaxyBackground background =
                new GalaxyBackground();

        background.setLayout(
                new BorderLayout()
        );

        background.add(
                cardPanel,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );

        // =====================================================
        // SHOW WELCOME PAGE
        // =====================================================

        cardLayout.show(
                cardPanel,
                "WELCOME"
        );
    }

    // =========================================================
    // WELCOME PAGE
    // =========================================================

    private JPanel createWelcomePage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setOpaque(false);

        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel leftPanel =
                new JPanel();

        leftPanel.setOpaque(false);

        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        70,
                        75,
                        50,
                        10
                )
        );

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // SMALL TITLE
        // =====================================================

        JLabel smallTitle =
                new JLabel(
                        "✦  SMART CAMPUS"
                );

        smallTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        smallTitle.setForeground(
                new Color(
                        100,
                        220,
                        255
                )
        );

        smallTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // MAIN TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "<html>" +
                                "<font color='#FFFFFF'>" +
                                "Lost something?<br>" +
                                "</font>" +
                                "<font color='#70D6FF'>" +
                                "Let's find it together." +
                                "</font>" +
                                "</html>"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel description =
                new JLabel(
                        "<html>" +
                                "<font color='#D2D8F5'>" +
                                "A smarter way to report, discover and recover<br>" +
                                "lost items around your campus." +
                                "</font>" +
                                "</html>"
                );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // FEATURES
        // =====================================================

        JLabel features =
                new JLabel(
                        "<html>" +
                                "<font color='#B9C5FF'>" +
                                "✦ Smart Matching&nbsp;&nbsp;&nbsp;" +
                                "✦ Secure Claims&nbsp;&nbsp;&nbsp;" +
                                "✦ Campus-wide Search" +
                                "</font>" +
                                "</html>"
                );

        features.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        features.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =====================================================
        // GET STARTED BUTTON
        // =====================================================

        JButton getStarted =
                UIComponents.createPrimaryButton(
                        "Get Started   →"
                );

        getStarted.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        getStarted.setForeground(
                Color.WHITE
        );

        getStarted.setBackground(
                new Color(
                        75,
                        125,
                        255
                )
        );

        getStarted.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        getStarted.setPreferredSize(
                new Dimension(
                        220,
                        58
                )
        );

        getStarted.setMaximumSize(
                new Dimension(
                        240,
                        58
                )
        );

        getStarted.addActionListener(
                e -> {

                    mascotPanel.setMessage(
                            "Awesome! Choose your role 🚀"
                    );

                    cardLayout.show(
                            cardPanel,
                            "ROLE"
                    );
                }
        );

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        leftPanel.add(
                smallTitle
        );

        leftPanel.add(
                Box.createVerticalStrut(18)
        );

        leftPanel.add(
                title
        );

        leftPanel.add(
                Box.createVerticalStrut(20)
        );

        leftPanel.add(
                description
        );

        leftPanel.add(
                Box.createVerticalStrut(18)
        );

        leftPanel.add(
                features
        );

        leftPanel.add(
                Box.createVerticalStrut(35)
        );

        leftPanel.add(
                getStarted
        );

        leftPanel.add(
                Box.createVerticalGlue()
        );

        // =====================================================
        // MASCOT
        // =====================================================

        mascotPanel =
                new MascotPanel();

        JPanel mascotContainer =
                new JPanel(
                        new GridBagLayout()
                );

        mascotContainer.setOpaque(false);

        mascotContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        10,
                        20,
                        50
                )
        );

        mascotContainer.add(
                mascotPanel
        );

        // =====================================================
        // ADD TO PAGE
        // =====================================================

        page.add(
                leftPanel,
                BorderLayout.CENTER
        );

        page.add(
                mascotContainer,
                BorderLayout.EAST
        );

        return page;
    }

    // =========================================================
    // ROLE PAGE
    // =========================================================

    private JPanel createRolePage() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setOpaque(false);

        // =====================================================
        // TOP PANEL
        // =====================================================

        JPanel topPanel =
                new JPanel();

        topPanel.setOpaque(false);

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        55,
                        40,
                        20,
                        40
                )
        );

        topPanel.setLayout(
                new BoxLayout(
                        topPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel heading =
                new JLabel(
                        "How would you like to continue?"
                );

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        heading.setForeground(
                Color.WHITE
        );

        heading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Choose your role to enter the appropriate portal."
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(
                        190,
                        200,
                        235
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        topPanel.add(
                heading
        );

        topPanel.add(
                Box.createVerticalStrut(10)
        );

        topPanel.add(
                subtitle
        );

        // =====================================================
        // ROLE PANEL
        // =====================================================

        JPanel rolesPanel =
                new JPanel(
                        new GridBagLayout()
                );

        rolesPanel.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        15,
                        20,
                        15,
                        20
                );

        gbc.fill =
                GridBagConstraints.BOTH;

        gbc.weightx = 1.0;

        gbc.weighty = 1.0;

        // =====================================================
        // USER BUTTON
        // =====================================================

        JButton userButton =
                createGalaxyRoleButton(
                        "USER",
                        "Report lost or found items",
                        "Find matches and submit claims",
                        "👤"
                );

        userButton.addActionListener(
                e -> {

                    selectedRole = "USER";

                    mascotPanel.setMessage(
                            "Welcome! Let's sign you in ✨"
                    );

                    openLogin(
                            "USER"
                    );
                }
        );

        gbc.gridx = 0;

        gbc.gridy = 0;

        rolesPanel.add(
                userButton,
                gbc
        );

        // =====================================================
        // ADMIN BUTTON
        // =====================================================

        JButton adminButton =
                createGalaxyRoleButton(
                        "ADMIN",
                        "Manage campus reports",
                        "Verify claims and manage the system",
                        "⚙"
                );

        adminButton.addActionListener(
                e -> {

                    selectedRole = "ADMIN";

                    mascotPanel.setMessage(
                            "Admin portal selected! 🚀"
                    );

                    openLogin(
                            "ADMIN"
                    );
                }
        );

        gbc.gridx = 1;

        rolesPanel.add(
                adminButton,
                gbc
        );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        JButton backButton =
                new JButton(
                        "←  Back"
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
                        190,
                        205,
                        245
                )
        );

        backButton.setBackground(
                new Color(
                        25,
                        30,
                        75
                )
        );

        backButton.setFocusPainted(false);

        backButton.setBorderPainted(false);

        backButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        backButton.addActionListener(
                e -> {

                    mascotPanel.setMessage(
                            "Let's go back home! 🌌"
                    );

                    cardLayout.show(
                            cardPanel,
                            "WELCOME"
                    );
                }
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottomPanel.setOpaque(false);

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        10,
                        25,
                        10
                )
        );

        bottomPanel.add(
                backButton
        );

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        page.add(
                topPanel,
                BorderLayout.NORTH
        );

        page.add(
                rolesPanel,
                BorderLayout.CENTER
        );

        page.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        return page;
    }

    // =========================================================
    // ROLE BUTTON
    // =========================================================

    private JButton createGalaxyRoleButton(
            String title,
            String line1,
            String line2,
            String icon
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBackground(
                new Color(
                        24,
                        30,
                        72
                )
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        100,
                                        150,
                                        255
                                ),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        // =====================================================
        // ICON
        // =====================================================

        JLabel iconLabel =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        38
                )
        );

        iconLabel.setForeground(
                new Color(
                        100,
                        220,
                        255
                )
        );

        button.add(
                iconLabel,
                BorderLayout.WEST
        );

        // =====================================================
        // TEXT
        // =====================================================

        JPanel textPanel =
                new JPanel();

        textPanel.setOpaque(false);

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

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        titleLabel.setForeground(
                Color.WHITE
        );

        JLabel line1Label =
                new JLabel(
                        line1
                );

        line1Label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        line1Label.setForeground(
                new Color(
                        190,
                        205,
                        240
                )
        );

        JLabel line2Label =
                new JLabel(
                        line2
                );

        line2Label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        line2Label.setForeground(
                new Color(
                        190,
                        205,
                        240
                )
        );

        textPanel.add(
                titleLabel
        );

        textPanel.add(
                Box.createVerticalStrut(8)
        );

        textPanel.add(
                line1Label
        );

        textPanel.add(
                Box.createVerticalStrut(3)
        );

        textPanel.add(
                line2Label
        );

        button.add(
                textPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // ARROW
        // =====================================================

        JLabel arrow =
                new JLabel(
                        "→"
                );

        arrow.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        arrow.setForeground(
                new Color(
                        100,
                        210,
                        255
                )
        );

        button.add(
                arrow,
                BorderLayout.EAST
        );

        // =====================================================
        // HOVER
        // =====================================================

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        40,
                                        45,
                                        100
                                )
                        );

                        button.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(
                                                        90,
                                                        220,
                                                        255
                                                ),
                                                2
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                24,
                                                29,
                                                24,
                                                29
                                        )
                                )
                        );
                    }

                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        24,
                                        30,
                                        72
                                )
                        );

                        button.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                new Color(
                                                        100,
                                                        150,
                                                        255
                                                ),
                                                1
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                25,
                                                30,
                                                25,
                                                30
                                        )
                                )
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // OPEN LOGIN
    // =========================================================

    private void openLogin(
            String role
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    LoginFrame loginFrame =
                            new LoginFrame(
                                    role
                            );

                    loginFrame.setLocationRelativeTo(
                            this
                    );

                    loginFrame.setVisible(
                            true
                    );

                    dispose();
                }
        );
    }

    // =========================================================
    // GET SELECTED ROLE
    // =========================================================

    public static String getSelectedRole() {

        return selectedRole;
    }

    // =========================================================
    // GALAXY BACKGROUND
    // =========================================================

    private static class GalaxyBackground
            extends JPanel {

        private final int STAR_COUNT = 180;

        private final int[] starX =
                new int[STAR_COUNT];

        private final int[] starY =
                new int[STAR_COUNT];

        private final int[] starSize =
                new int[STAR_COUNT];

        private final int[] starAlpha =
                new int[STAR_COUNT];

        private final Random random =
                new Random(42);

        private float nebulaShift = 0;

        private Timer timer;

        // =====================================================
        // CONSTRUCTOR
        // =====================================================

        public GalaxyBackground() {

            setOpaque(true);

            // =================================================
            // CREATE STARS
            // =================================================

            for (
                    int i = 0;
                    i < STAR_COUNT;
                    i++
            ) {

                starX[i] =
                        random.nextInt(1400);

                starY[i] =
                        random.nextInt(900);

                starSize[i] =
                        1 + random.nextInt(3);

                starAlpha[i] =
                        80 + random.nextInt(150);
            }

            // =================================================
            // ANIMATION
            // =================================================

            timer =
                    new Timer(
                            60,
                            e -> {

                                nebulaShift += 0.15f;

                                if (
                                        nebulaShift > 360
                                ) {

                                    nebulaShift = 0;
                                }

                                repaint();
                            }
                    );

            timer.start();
        }

        // =====================================================
        // PAINT BACKGROUND
        // =====================================================

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

            int width =
                    getWidth();

            int height =
                    getHeight();

            // =================================================
            // DARK SPACE
            // =================================================

            GradientPaint background =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    7,
                                    10,
                                    32
                            ),
                            width,
                            height,
                            new Color(
                                    25,
                                    12,
                                    65
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

            // =================================================
            // PURPLE NEBULA
            // =================================================

            float wave =
                    (float)
                            Math.sin(
                                    Math.toRadians(
                                            nebulaShift
                                    )
                            );

            int purpleX =
                    width / 3
                            + (int)
                            (wave * 80);

            int purpleY =
                    height / 2;

            RadialGradientPaint purpleGlow =
                    new RadialGradientPaint(
                            new Point(
                                    purpleX,
                                    purpleY
                            ),
                            380,
                            new float[]{
                                    0.0f,
                                    0.35f,
                                    1.0f
                            },
                            new Color[]{
                                    new Color(
                                            125,
                                            55,
                                            255,
                                            100
                                    ),
                                    new Color(
                                            80,
                                            55,
                                            210,
                                            50
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
                    width,
                    height
            );

            // =================================================
            // BLUE NEBULA
            // =================================================

            int blueX =
                    width - 180;

            int blueY =
                    180;

            RadialGradientPaint blueGlow =
                    new RadialGradientPaint(
                            new Point(
                                    blueX,
                                    blueY
                            ),
                            300,
                            new float[]{
                                    0.0f,
                                    0.45f,
                                    1.0f
                            },
                            new Color[]{
                                    new Color(
                                            50,
                                            180,
                                            255,
                                            90
                                    ),
                                    new Color(
                                            40,
                                            100,
                                            220,
                                            35
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
                    blueGlow
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            // =================================================
            // STARS
            // =================================================

            for (
                    int i = 0;
                    i < STAR_COUNT;
                    i++
            ) {

                int x =
                        starX[i]
                                % Math.max(
                                width,
                                1
                        );

                int y =
                        starY[i]
                                % Math.max(
                                height,
                                1
                        );

                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                starAlpha[i]
                        )
                );

                g2.fillOval(
                        x,
                        y,
                        starSize[i],
                        starSize[i]
                );
            }

            // =================================================
            // DECORATIVE STARS
            // =================================================

            drawLargeStar(
                    g2,
                    width - 120,
                    100,
                    7
            );

            drawLargeStar(
                    g2,
                    width - 300,
                    height - 100,
                    5
            );

            drawLargeStar(
                    g2,
                    100,
                    height - 130,
                    6
            );

            drawLargeStar(
                    g2,
                    180,
                    100,
                    4
            );

            g2.dispose();
        }

        // =====================================================
        // LARGE STAR
        // =====================================================

        private void drawLargeStar(
                Graphics2D g2,
                int x,
                int y,
                int size
        ) {

            g2.setColor(
                    new Color(
                            210,
                            230,
                            255,
                            210
                    )
            );

            g2.fillOval(
                    x - size / 2,
                    y - size / 2,
                    size,
                    size
            );

            g2.setStroke(
                    new BasicStroke(1)
            );

            g2.drawLine(
                    x - size * 2,
                    y,
                    x + size * 2,
                    y
            );

            g2.drawLine(
                    x,
                    y - size * 2,
                    x,
                    y + size * 2
            );
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

                    LandingFrame frame =
                            new LandingFrame();

                    frame.setVisible(true);
                }
        );
    }
}