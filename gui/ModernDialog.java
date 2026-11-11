package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernDialog extends JDialog {

    // ==================================================
    // COLORS
    // ==================================================

    private static final Color BLUE =
            new Color(65, 125, 230);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color SECONDARY =
            new Color(195, 205, 230);

    private static final Color SUCCESS_GREEN =
            new Color(70, 220, 135);

    private static final Color ERROR_RED =
            new Color(245, 75, 95);

    private static final Color WARNING_ORANGE =
            new Color(255, 190, 70);

    private static final Color INFO_BLUE =
            new Color(90, 170, 255);

    // Confirmation result
    private boolean result = false;


    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    private ModernDialog(
            JFrame parent,
            String title,
            String message,
            String type,
            boolean confirmation
    ) {

        super(
                parent,
                true
        );

        // ==================================================
        // WINDOW
        // ==================================================

        final int WIDTH = 430;

        final int HEIGHT =
                confirmation
                        ? 285
                        : 280;

        setUndecorated(true);

        setSize(
                WIDTH,
                HEIGHT
        );

        setLocationRelativeTo(
                parent
        );

        setShape(
                new RoundRectangle2D.Double(
                        0,
                        0,
                        WIDTH,
                        HEIGHT,
                        26,
                        26
                )
        );


        // ==================================================
        // GALAXY BACKGROUND
        // ==================================================

        JPanel background =
                new JPanel() {

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

                        // Main gradient

                        GradientPaint gradient =
                                new GradientPaint(
                                        0,
                                        0,
                                        new Color(
                                                8,
                                                16,
                                                50
                                        ),
                                        getWidth(),
                                        getHeight(),
                                        new Color(
                                                43,
                                                17,
                                                82
                                        )
                                );

                        g2.setPaint(
                                gradient
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                26,
                                26
                        );


                        // Blue glow

                        RadialGradientPaint blueGlow =
                                new RadialGradientPaint(
                                        new Point(
                                                getWidth() - 40,
                                                20
                                        ),
                                        170,
                                        new float[]{
                                                0f,
                                                1f
                                        },
                                        new Color[]{
                                                new Color(
                                                        70,
                                                        130,
                                                        255,
                                                        65
                                                ),
                                                new Color(
                                                        70,
                                                        130,
                                                        255,
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
                                getWidth(),
                                getHeight()
                        );


                        // Purple glow

                        RadialGradientPaint purpleGlow =
                                new RadialGradientPaint(
                                        new Point(
                                                20,
                                                getHeight() - 20
                                        ),
                                        150,
                                        new float[]{
                                                0f,
                                                1f
                                        },
                                        new Color[]{
                                                new Color(
                                                        140,
                                                        70,
                                                        255,
                                                        40
                                                ),
                                                new Color(
                                                        140,
                                                        70,
                                                        255,
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


                        // Border

                        g2.setColor(
                                new Color(
                                        75,
                                        140,
                                        245,
                                        210
                                )
                        );

                        g2.setStroke(
                                new BasicStroke(
                                        1.5f
                                )
                        );

                        g2.drawRoundRect(
                                1,
                                1,
                                getWidth() - 3,
                                getHeight() - 3,
                                26,
                                26
                        );

                        g2.dispose();
                    }
                };


        background.setLayout(
                new BorderLayout()
        );


        // ==================================================
        // HEADER
        // ==================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(
                false
        );

        header.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        5,
                        15
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
                WHITE
        );


        // ==================================================
        // CLOSE BUTTON
        // ==================================================

        JButton closeButton =
                new JButton(
                        "×"
                );

        closeButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        25
                )
        );

        closeButton.setForeground(
                SECONDARY
        );

        closeButton.setBorderPainted(
                false
        );

        closeButton.setFocusPainted(
                false
        );

        closeButton.setContentAreaFilled(
                false
        );

        closeButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        closeButton.addActionListener(
                e -> dispose()
        );


        header.add(
                titleLabel,
                BorderLayout.WEST
        );

        header.add(
                closeButton,
                BorderLayout.EAST
        );


        background.add(
                header,
                BorderLayout.NORTH
        );


        // ==================================================
        // CENTER CONTENT
        // ==================================================

        JPanel center =
                new JPanel();

        center.setOpaque(
                false
        );

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );


        // ==================================================
        // CUSTOM EMOJI FACE
        // ==================================================

        EmojiPanel emojiPanel =
                new EmojiPanel(
                        type
                );

        emojiPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        center.add(
                emojiPanel
        );


        center.add(
                Box.createVerticalStrut(
                        7
                )
        );


        // ==================================================
        // MESSAGE
        // ==================================================

        JLabel messageLabel =
                new JLabel(
                        "<html>"
                                + "<div style="
                                + "'text-align:center;'>"
                                + message
                                + "</div>"
                                + "</html>"
                );

        messageLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        messageLabel.setForeground(
                SECONDARY
        );

        messageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        center.add(
                messageLabel
        );


        background.add(
                center,
                BorderLayout.CENTER
        );


        // ==================================================
        // BUTTON AREA
        // ==================================================

        JPanel buttonArea =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        buttonArea.setOpaque(
                false
        );

        buttonArea.setBorder(
                new EmptyBorder(
                        0,
                        20,
                        18,
                        20
                )
        );


        // ==================================================
        // CONFIRMATION
        // ==================================================

        if (
                confirmation
        ) {

            JButton cancel =
                    createButton(
                            "Cancel",
                            new Color(
                                    45,
                                    60,
                                    105
                            )
                    );

            cancel.addActionListener(
                    e -> {

                        result = false;

                        dispose();
                    }
            );


            JButton yes =
                    createButton(
                            "Yes",
                            BLUE
                    );

            yes.addActionListener(
                    e -> {

                        result = true;

                        dispose();
                    }
            );


            buttonArea.add(
                    cancel
            );

            buttonArea.add(
                    yes
            );

        } else {

            JButton ok =
                    createButton(
                            "OK",
                            BLUE
                    );

            ok.addActionListener(
                    e -> dispose()
            );

            buttonArea.add(
                    ok
            );
        }


        background.add(
                buttonArea,
                BorderLayout.SOUTH
        );


        // ==================================================
        // ADD BACKGROUND
        // ==================================================

        add(
                background
        );
    }


    // ==================================================
    // CUSTOM EMOJI PANEL
    // ==================================================

    private static class EmojiPanel
            extends JPanel {

        private final String type;

        public EmojiPanel(
                String type
        ) {

            this.type =
                    type.toUpperCase();

            setOpaque(
                    false
            );

            setPreferredSize(
                    new Dimension(
                            70,
                            65
                    )
            );

            setMaximumSize(
                    new Dimension(
                            70,
                            65
                    )
            );

            setMinimumSize(
                    new Dimension(
                            70,
                            65
                    )
            );
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


            // ==================================================
            // SUCCESS FACE
            // ==================================================

            if (
                    type.equals(
                            "SUCCESS"
                    )
            ) {

                drawHappyFace(
                        g2
                );
            }


            // ==================================================
            // ERROR FACE
            // ==================================================

            else if (
                    type.equals(
                            "ERROR"
                    )
            ) {

                drawSadFace(
                        g2
                );
            }


            // ==================================================
            // WARNING FACE
            // ==================================================

            else if (
                    type.equals(
                            "WARNING"
                    )
            ) {

                drawWorriedFace(
                        g2
                );
            }


            // ==================================================
            // INFO
            // ==================================================

            else {

                drawInfoIcon(
                        g2
                );
            }


            g2.dispose();
        }


        // ==================================================
        // HAPPY FACE
        // ==================================================

        private void drawHappyFace(
                Graphics2D g
        ) {

            Color green =
                    SUCCESS_GREEN;

            g.setColor(
                    green
            );

            g.setStroke(
                    new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            // Face circle

            g.drawOval(
                    8,
                    3,
                    54,
                    54
            );


            // Left eye

            g.drawArc(
                    19,
                    17,
                    12,
                    10,
                    0,
                    180
            );


            // Right eye

            g.drawArc(
                    39,
                    17,
                    12,
                    10,
                    0,
                    180
            );


            // Smile

            g.drawArc(
                    18,
                    25,
                    34,
                    23,
                    190,
                    160
            );
        }


        // ==================================================
        // SAD FACE
        // ==================================================

        private void drawSadFace(
                Graphics2D g
        ) {

            Color red =
                    ERROR_RED;

            g.setColor(
                    red
            );

            g.setStroke(
                    new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            // Face circle

            g.drawOval(
                    8,
                    3,
                    54,
                    54
            );


            // Left eye

            g.fillOval(
                    20,
                    18,
                    8,
                    8
            );


            // Right eye

            g.fillOval(
                    42,
                    18,
                    8,
                    8
            );


            // Sad mouth

            g.drawArc(
                    18,
                    31,
                    34,
                    23,
                    20,
                    140
            );
        }


        // ==================================================
        // WORRIED FACE
        // ==================================================

        private void drawWorriedFace(
                Graphics2D g
        ) {

            Color orange =
                    WARNING_ORANGE;

            g.setColor(
                    orange
            );

            g.setStroke(
                    new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            // Face circle

            g.drawOval(
                    8,
                    3,
                    54,
                    54
            );


            // Eyes

            g.fillOval(
                    20,
                    19,
                    8,
                    8
            );

            g.fillOval(
                    42,
                    19,
                    8,
                    8
            );


            // Worried mouth

            g.drawArc(
                    20,
                    31,
                    30,
                    18,
                    20,
                    140
            );
        }


        // ==================================================
        // INFORMATION ICON
        // ==================================================

        private void drawInfoIcon(
                Graphics2D g
        ) {

            Color blue =
                    INFO_BLUE;

            g.setColor(
                    blue
            );

            g.setStroke(
                    new BasicStroke(
                            5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            // Circle

            g.drawOval(
                    8,
                    3,
                    54,
                    54
            );


            // Dot

            g.fillOval(
                    31,
                    15,
                    8,
                    8
            );


            // Vertical line

            g.drawLine(
                    35,
                    29,
                    35,
                    45
            );
        }
    }


    // ==================================================
    // BUTTON
    // ==================================================

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(
                        text
                );

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
                color
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9,
                        32,
                        9,
                        32
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }


    // ==================================================
    // SUCCESS
    // ==================================================

    public static void showSuccess(
            JFrame parent,
            String title,
            String message
    ) {

        ModernDialog dialog =
                new ModernDialog(
                        parent,
                        title,
                        message,
                        "SUCCESS",
                        false
                );

        dialog.setVisible(
                true
        );
    }


    // ==================================================
    // ERROR
    // ==================================================

    public static void showError(
            JFrame parent,
            String title,
            String message
    ) {

        ModernDialog dialog =
                new ModernDialog(
                        parent,
                        title,
                        message,
                        "ERROR",
                        false
                );

        dialog.setVisible(
                true
        );
    }


    // ==================================================
    // WARNING
    // ==================================================

    public static void showWarning(
            JFrame parent,
            String title,
            String message
    ) {

        ModernDialog dialog =
                new ModernDialog(
                        parent,
                        title,
                        message,
                        "WARNING",
                        false
                );

        dialog.setVisible(
                true
        );
    }


    // ==================================================
    // INFORMATION
    // ==================================================

    public static void showInfo(
            JFrame parent,
            String title,
            String message
    ) {

        ModernDialog dialog =
                new ModernDialog(
                        parent,
                        title,
                        message,
                        "INFO",
                        false
                );

        dialog.setVisible(
                true
        );
    }


    // ==================================================
    // CONFIRMATION
    // ==================================================

    public static boolean showConfirm(
            JFrame parent,
            String title,
            String message
    ) {

        ModernDialog dialog =
                new ModernDialog(
                        parent,
                        title,
                        message,
                        "WARNING",
                        true
                );

        dialog.setVisible(
                true
        );

        return dialog.result;
    }
}