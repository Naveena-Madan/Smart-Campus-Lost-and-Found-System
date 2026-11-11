package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;

public class SmartMatchResultsDialog extends JDialog {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color GALAXY_TOP =
            new Color(5, 8, 25);

    private static final Color GALAXY_BOTTOM =
            new Color(17, 7, 48);

    private static final Color CARD =
            new Color(
                    10,
                    20,
                    55,
                    245
            );

    private static final Color CARD_HOVER =
            new Color(
                    15,
                    30,
                    75,
                    250
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

    private static final Color DARK =
            new Color(
                    5,
                    12,
                    32
            );


    // =========================================================
    // DATA
    // =========================================================

    private final String loggedInUsername;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SmartMatchResultsDialog(
            JFrame parent,
            List<String> matches,
            String loggedInUsername
    ) {

        super(
                parent,
                "Smart Match Results",
                true
        );

        this.loggedInUsername =
                loggedInUsername;


        setSize(
                900,
                680
        );


        setMinimumSize(
                new Dimension(
                        780,
                        600
                )
        );


        setLocationRelativeTo(
                parent
        );


        setResizable(
                true
        );


        buildUI(
                matches
        );
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI(
            List<String> matches
    ) {

        GalaxyPanel background =
                new GalaxyPanel();


        background.setLayout(
                new BorderLayout()
        );


        background.setBorder(
                new EmptyBorder(
                        22,
                        28,
                        22,
                        28
                )
        );


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel();

        header.setOpaque(
                false
        );


        header.setLayout(
                new BorderLayout()
        );


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
                        "✦  Smart Match Results"
                );


        title.setForeground(
                WHITE
        );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );


        JLabel subtitle =
                new JLabel(
                        "AI-powered matches for your lost item"
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


        // =====================================================
        // CLOSE BUTTON
        // =====================================================

        JButton closeTop =
                new JButton(
                        "×"
                );


        closeTop.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        30
                )
        );


        closeTop.setForeground(
                SECONDARY_TEXT
        );


        closeTop.setBackground(
                new Color(
                        0,
                        0,
                        0,
                        0
                )
        );


        closeTop.setBorderPainted(
                false
        );


        closeTop.setFocusPainted(
                false
        );


        closeTop.setContentAreaFilled(
                false
        );


        closeTop.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        closeTop.addActionListener(
                e -> dispose()
        );


        header.add(
                closeTop,
                BorderLayout.EAST
        );


        background.add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // RESULT PANEL
        // =====================================================

        JPanel resultPanel =
                new JPanel();


        resultPanel.setOpaque(
                false
        );


        resultPanel.setLayout(
                new BoxLayout(
                        resultPanel,
                        BoxLayout.Y_AXIS
                )
        );


        resultPanel.setBorder(
                new EmptyBorder(
                        15,
                        5,
                        15,
                        5
                )
        );


        // =====================================================
        // CREATE MATCH CARDS
        // =====================================================

        for (
                String match : matches
        ) {

            JPanel card =
                    createMatchCard(
                            match
                    );


            resultPanel.add(
                    card
            );


            resultPanel.add(
                    Box.createVerticalStrut(
                            15
                    )
            );
        }


        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        resultPanel
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


        scrollPane.setBackground(
                new Color(
                        0,
                        0,
                        0,
                        0
                )
        );


        // =====================================================
        // MODERN SCROLLBAR
        // =====================================================

        JScrollBar verticalBar =
                scrollPane.getVerticalScrollBar();


        verticalBar.setPreferredSize(
                new Dimension(
                        10,
                        0
                )
        );


        verticalBar.setUI(
                new GalaxyScrollBarUI()
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
                                FlowLayout.CENTER
                        )
                );


        bottom.setOpaque(
                false
        );


        JButton doneButton =
                createButton(
                        "Close",
                        new Color(
                                45,
                                60,
                                100
                        )
                );


        doneButton.addActionListener(
                e -> dispose()
        );


        bottom.add(
                doneButton
        );


        background.add(
                bottom,
                BorderLayout.SOUTH
        );


        setContentPane(
                background
        );
    }


    // =========================================================
    // CREATE MATCH CARD
    // =========================================================

    private JPanel createMatchCard(
            String match
    ) {

        // =====================================================
        // EXTRACT DATA
        // =====================================================

        String reportId =
                extractValue(
                        match,
                        "Report ID:"
                );


        String itemName =
                extractValue(
                        match,
                        "Found Item:"
                );


        String location =
                extractValue(
                        match,
                        "Location:"
                );


        String score =
                extractScore(
                        match
                );


        // =====================================================
        // MAIN CARD
        // =====================================================

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                20,
                                10
                        )
                );


        card.setBackground(
                CARD
        );


        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(
                                        45,
                                        200,
                                        255,
                                        160
                                ),
                                1
                        ),

                        new EmptyBorder(
                                20,
                                22,
                                20,
                                22
                        )
                )
        );


        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );


        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel details =
                new JPanel();


        details.setOpaque(
                false
        );


        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel possible =
                new JLabel(
                        "✦  POSSIBLE MATCH"
                );


        possible.setForeground(
                CYAN
        );


        possible.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );


        JLabel itemLabel =
                new JLabel(
                        "Found Item"
                );


        itemLabel.setForeground(
                SECONDARY_TEXT
        );


        itemLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
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
                        22
                )
        );


        JLabel locationLabel =
                new JLabel(
                        "Location"
                );


        locationLabel.setForeground(
                SECONDARY_TEXT
        );


        locationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );


        JLabel locationValue =
                new JLabel(
                        "📍  " + location
                );


        locationValue.setForeground(
                WHITE
        );


        locationValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );


        details.add(
                possible
        );


        details.add(
                Box.createVerticalStrut(
                        10
                )
        );


        details.add(
                itemLabel
        );


        details.add(
                Box.createVerticalStrut(
                        2
                )
        );


        details.add(
                item
        );


        details.add(
                Box.createVerticalStrut(
                        13
                )
        );


        details.add(
                locationLabel
        );


        details.add(
                Box.createVerticalStrut(
                        2
                )
        );


        details.add(
                locationValue
        );


        card.add(
                details,
                BorderLayout.CENTER
        );


        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel right =
                new JPanel();


        right.setOpaque(
                false
        );


        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );


        // =====================================================
        // SCORE CARD
        // =====================================================

        JPanel scorePanel =
                new JPanel();


        scorePanel.setBackground(
                new Color(
                        20,
                        40,
                        90
                )
        );


        scorePanel.setPreferredSize(
                new Dimension(
                        165,
                        105
                )
        );


        scorePanel.setMaximumSize(
                new Dimension(
                        165,
                        105
                )
        );


        scorePanel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                70,
                                120,
                                255,
                                120
                        )
                )
        );


        scorePanel.setLayout(
                new BoxLayout(
                        scorePanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel scoreTitle =
                new JLabel(
                        "MATCH SCORE"
                );


        scoreTitle.setForeground(
                SECONDARY_TEXT
        );


        scoreTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );


        scoreTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel scoreValue =
                new JLabel(
                        score.isEmpty()
                                ? "Possible"
                                : score
                );


        scoreValue.setForeground(
                CYAN
        );


        scoreValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );


        scoreValue.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        scorePanel.add(
                Box.createVerticalStrut(
                        12
                )
        );


        scorePanel.add(
                scoreTitle
        );


        scorePanel.add(
                Box.createVerticalStrut(
                        4
                )
        );


        scorePanel.add(
                scoreValue
        );


        right.add(
                scorePanel
        );


        right.add(
                Box.createVerticalStrut(
                        12
                )
        );


        // =====================================================
        // CLAIM BUTTON
        // =====================================================

        JButton claimButton =
                createButton(
                        "Claim Item",
                        BLUE
                );


        claimButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        claimButton.addActionListener(
                e -> {

                    if (
                            reportId == null
                                    ||
                                    reportId.trim().isEmpty()
                    ) {

                        showError(
                                "Unable to identify this report."
                        );

                        return;
                    }


                    try {

                        int id =
                                Integer.parseInt(
                                        reportId.trim()
                                );


                        dispose();


                        SwingUtilities.invokeLater(
                                () -> {

                                    ClaimItemFrame claimFrame =
                                            new ClaimItemFrame(
                                                    id,
                                                    loggedInUsername
                                            );


                                    claimFrame.setLocationRelativeTo(
                                            getOwner()
                                    );


                                    claimFrame.setVisible(
                                            true
                                    );


                                    claimFrame.toFront();

                                    claimFrame.requestFocus();
                                }
                        );


                    } catch (
                            NumberFormatException ex
                    ) {

                        showError(
                                "Invalid report ID."
                        );
                    }
                }
        );


        right.add(
                claimButton
        );


        card.add(
                right,
                BorderLayout.EAST
        );


        // =====================================================
        // HOVER EFFECT
        // =====================================================

        card.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                CARD_HOVER
                        );
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                CARD
                        );
                    }
                }
        );


        return card;
    }


    // =========================================================
    // EXTRACT VALUE
    // =========================================================

    private String extractValue(
            String text,
            String key
    ) {

        String[] lines =
                text.split(
                        "\\n"
                );


        for (
                String line : lines
        ) {

            if (
                    line.startsWith(
                            key
                    )
            ) {

                return line
                        .substring(
                                key.length()
                        )
                        .trim();
            }
        }


        return "";
    }


    // =========================================================
    // EXTRACT SCORE
    // =========================================================

    private String extractScore(
            String text
    ) {

        String[] lines =
                text.split(
                        "\\n"
                );


        for (
                String line : lines
        ) {

            if (
                    line.toLowerCase()
                            .contains(
                                    "score"
                            )
            ) {

                int colon =
                        line.indexOf(
                                ":"
                        );


                if (
                        colon >= 0
                ) {

                    return line
                            .substring(
                                    colon + 1
                            )
                            .trim();
                }
            }
        }


        return "";
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


        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );


        button.setForeground(
                WHITE
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


        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
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
                                        100,
                                        145,
                                        255
                                )
                        );
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
    // ERROR
    // =========================================================

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Smart Match",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // =========================================================
    // GALAXY BACKGROUND
    // =========================================================

    private static class GalaxyPanel
            extends JPanel {

        private final Star[] stars;


        GalaxyPanel() {

            setOpaque(
                    false
            );


            Random random =
                    new Random(
                            42
                    );


            stars =
                    new Star[130];


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


            // =================================================
            // MAIN GRADIENT
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
            // PURPLE GLOW
            // =================================================

            RadialGradientPaint purple =
                    new RadialGradientPaint(
                            new Point(
                                    getWidth() * 3 / 4,
                                    getHeight() / 4
                            ),
                            Math.max(
                                    300,
                                    getWidth() / 2
                            ),
                            new float[]{
                                    0f,
                                    0.45f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            110,
                                            45,
                                            200,
                                            70
                                    ),
                                    new Color(
                                            70,
                                            25,
                                            140,
                                            20
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
                    purple
            );


            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );


            // =================================================
            // CYAN GLOW
            // =================================================

            RadialGradientPaint cyanGlow =
                    new RadialGradientPaint(
                            new Point(
                                    getWidth() / 5,
                                    getHeight() * 3 / 4
                            ),
                            Math.max(
                                    300,
                                    getWidth() / 2
                            ),
                            new float[]{
                                    0f,
                                    0.5f,
                                    1f
                            },
                            new Color[]{
                                    new Color(
                                            15,
                                            130,
                                            210,
                                            45
                                    ),
                                    new Color(
                                            15,
                                            80,
                                            170,
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

            for (
                    Star star : stars
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
                        5,
                        10,
                        27
                );


        private final Color THUMB =
                new Color(
                        55,
                        85,
                        150
                );


        private final Color THUMB_HOVER =
                new Color(
                        55,
                        200,
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

            return emptyButton();
        }


        @Override
        protected JButton createIncreaseButton(
                int orientation
        ) {

            return emptyButton();
        }


        private JButton emptyButton() {

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


            return button;
        }


        @Override
        protected void paintTrack(
                Graphics g,
                JComponent c,
                Rectangle bounds
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();


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
                    (Graphics2D)
                            g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    isThumbRollover()
                            ? THUMB_HOVER
                            : THUMB
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
}