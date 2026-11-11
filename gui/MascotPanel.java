package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MascotPanel extends JPanel {

    // =========================================================
    // ANIMATION
    // =========================================================

    private int bounce = 0;
    private int bounceDirection = 1;

    private float glow = 0.0f;
    private float glowDirection = 0.04f;

    private String message =
            "Hi! I'm your campus assistant!";

    private final Timer animationTimer;

    // =========================================================
    // COLORS
    // =========================================================

    private final Color CYAN =
            new Color(70, 210, 255);

    private final Color BLUE =
            new Color(72, 105, 255);

    private final Color PURPLE =
            new Color(155, 90, 255);

    private final Color DARK_BLUE =
            new Color(20, 25, 65);

    private final Color FACE =
            new Color(13, 18, 45);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MascotPanel() {

        setOpaque(false);

        setPreferredSize(
                new Dimension(
                        440,
                        500
                )
        );

        // =====================================================
        // ANIMATION TIMER
        // =====================================================

        animationTimer =
                new Timer(
                        50,
                        e -> {

                            // -----------------------------
                            // Bounce
                            // -----------------------------

                            bounce += bounceDirection;

                            if (bounce >= 7) {
                                bounceDirection = -1;
                            }

                            if (bounce <= 0) {
                                bounceDirection = 1;
                            }

                            // -----------------------------
                            // Glow
                            // -----------------------------

                            glow += glowDirection;

                            if (glow >= 1.0f) {
                                glowDirection = -0.04f;
                            }

                            if (glow <= 0.0f) {
                                glowDirection = 0.04f;
                            }

                            repaint();
                        }
                );

        animationTimer.start();

        // =====================================================
        // MOUSE INTERACTION
        // =====================================================

        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        setMessage(
                                "Welcome! Let's find your lost item! ✨"
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        setMessage(
                                "Hi! I'm your campus assistant!"
                        );
                    }

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        setMessage(
                                "I'm here to guide you! 🚀"
                        );
                    }
                }
        );
    }

    // =========================================================
    // SET MESSAGE
    // =========================================================

    public void setMessage(
            String message
    ) {

        if (
                message == null ||
                        message.trim().isEmpty()
        ) {

            this.message =
                    "Hi! I'm your campus assistant!";

        } else {

            this.message =
                    message;
        }

        repaint();
    }

    // =========================================================
    // GET MESSAGE
    // =========================================================

    public String getMessage() {

        return message;
    }

    // =========================================================
    // PAINT COMPONENT
    // =========================================================

    @Override
    protected void paintComponent(
            Graphics g
    ) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        // =====================================================
        // SMOOTH GRAPHICS
        // =====================================================

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        int centerX =
                getWidth() / 2;

        // =====================================================
        // SPEECH BUBBLE
        // =====================================================

        int bubbleX = 35;
        int bubbleY = 15;
        int bubbleWidth =
                getWidth() - 70;
        int bubbleHeight = 82;

        // Glow behind bubble

        g2.setColor(
                new Color(
                        80,
                        140,
                        255,
                        35
                )
        );

        g2.fillRoundRect(
                bubbleX - 5,
                bubbleY - 5,
                bubbleWidth + 10,
                bubbleHeight + 10,
                30,
                30
        );

        // Bubble

        g2.setColor(
                new Color(
                        17,
                        23,
                        58,
                        235
                )
        );

        g2.fillRoundRect(
                bubbleX,
                bubbleY,
                bubbleWidth,
                bubbleHeight,
                28,
                28
        );

        // Bubble border

        g2.setColor(
                new Color(
                        110,
                        190,
                        255,
                        150
                )
        );

        g2.setStroke(
                new BasicStroke(1.5f)
        );

        g2.drawRoundRect(
                bubbleX,
                bubbleY,
                bubbleWidth,
                bubbleHeight,
                28,
                28
        );

        // =====================================================
        // SPEECH BUBBLE TAIL
        // =====================================================

        Polygon tail =
                new Polygon();

        tail.addPoint(
                centerX - 14,
                bubbleY + bubbleHeight
        );

        tail.addPoint(
                centerX,
                bubbleY + bubbleHeight + 16
        );

        tail.addPoint(
                centerX + 14,
                bubbleY + bubbleHeight
        );

        g2.setColor(
                new Color(
                        17,
                        23,
                        58,
                        235
                )
        );

        g2.fillPolygon(tail);

        // =====================================================
        // MESSAGE
        // =====================================================

        g2.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        g2.setColor(
                Color.WHITE
        );

        drawCenteredText(
                g2,
                message,
                centerX,
                bubbleY + 48
        );

        // =====================================================
        // MASCOT POSITION
        // =====================================================

        int mascotCenterX =
                centerX;

        int mascotTop =
                135 + bounce;

        // =====================================================
        // OUTER GLOW
        // =====================================================

        int glowAlpha =
                35 + (int) (glow * 35);

        g2.setColor(
                new Color(
                        80,
                        150,
                        255,
                        glowAlpha
                )
        );

        g2.fillOval(
                mascotCenterX - 145,
                mascotTop - 20,
                290,
                300
        );

        // =====================================================
        // FLOATING STARS AROUND MASCOT
        // =====================================================

        drawStar(
                g2,
                mascotCenterX - 150,
                mascotTop + 35,
                10
        );

        drawStar(
                g2,
                mascotCenterX + 140,
                mascotTop + 75,
                8
        );

        drawStar(
                g2,
                mascotCenterX - 125,
                mascotTop + 210,
                7
        );

        drawStar(
                g2,
                mascotCenterX + 125,
                mascotTop + 225,
                10
        );

        // =====================================================
        // BODY
        // =====================================================

        int bodyX =
                mascotCenterX - 72;

        int bodyY =
                mascotTop + 105;

        int bodyWidth = 144;
        int bodyHeight = 145;

        // Body glow

        g2.setColor(
                new Color(
                        75,
                        120,
                        255,
                        50
                )
        );

        g2.fillRoundRect(
                bodyX - 8,
                bodyY - 8,
                bodyWidth + 16,
                bodyHeight + 16,
                55,
                55
        );

        // Body

        GradientPaint bodyGradient =
                new GradientPaint(
                        bodyX,
                        bodyY,
                        BLUE,
                        bodyX + bodyWidth,
                        bodyY + bodyHeight,
                        PURPLE
                );

        g2.setPaint(
                bodyGradient
        );

        g2.fillRoundRect(
                bodyX,
                bodyY,
                bodyWidth,
                bodyHeight,
                45,
                45
        );

        // Body outline

        g2.setColor(
                new Color(
                        120,
                        220,
                        255,
                        180
                )
        );

        g2.setStroke(
                new BasicStroke(2.5f)
        );

        g2.drawRoundRect(
                bodyX,
                bodyY,
                bodyWidth,
                bodyHeight,
                45,
                45
        );

        // =====================================================
        // HEAD
        // =====================================================

        int headX =
                mascotCenterX - 78;

        int headY =
                mascotTop;

        int headWidth = 156;
        int headHeight = 125;

        // Head glow

        g2.setColor(
                new Color(
                        70,
                        210,
                        255,
                        45
                )
        );

        g2.fillOval(
                headX - 8,
                headY - 8,
                headWidth + 16,
                headHeight + 16
        );

        // Head

        GradientPaint headGradient =
                new GradientPaint(
                        headX,
                        headY,
                        Color.WHITE,
                        headX + headWidth,
                        headY + headHeight,
                        new Color(
                                220,
                                235,
                                255
                        )
                );

        g2.setPaint(
                headGradient
        );

        g2.fillOval(
                headX,
                headY,
                headWidth,
                headHeight
        );

        // Head outline

        g2.setColor(
                new Color(
                        90,
                        190,
                        255
                )
        );

        g2.setStroke(
                new BasicStroke(3)
        );

        g2.drawOval(
                headX,
                headY,
                headWidth,
                headHeight
        );

        // =====================================================
        // ANTENNA
        // =====================================================

        g2.setColor(
                CYAN
        );

        g2.setStroke(
                new BasicStroke(
                        4,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawLine(
                mascotCenterX,
                headY,
                mascotCenterX,
                headY - 25
        );

        // Antenna glow

        g2.setColor(
                new Color(
                        80,
                        220,
                        255,
                        70
                )
        );

        g2.fillOval(
                mascotCenterX - 13,
                headY - 38,
                26,
                26
        );

        g2.setColor(
                CYAN
        );

        g2.fillOval(
                mascotCenterX - 7,
                headY - 32,
                14,
                14
        );

        // =====================================================
        // EARS
        // =====================================================

        g2.setColor(
                BLUE
        );

        g2.fillOval(
                headX - 18,
                headY + 40,
                30,
                42
        );

        g2.fillOval(
                headX + headWidth - 12,
                headY + 40,
                30,
                42
        );

        // Ear highlights

        g2.setColor(
                CYAN
        );

        g2.fillOval(
                headX - 11,
                headY + 50,
                12,
                18
        );

        g2.fillOval(
                headX + headWidth - 1,
                headY + 50,
                12,
                18
        );

        // =====================================================
        // FACE SCREEN
        // =====================================================

        int faceX =
                mascotCenterX - 55;

        int faceY =
                headY + 25;

        int faceWidth = 110;
        int faceHeight = 75;

        g2.setColor(
                FACE
        );

        g2.fillRoundRect(
                faceX,
                faceY,
                faceWidth,
                faceHeight,
                30,
                30
        );

        // Screen border

        g2.setColor(
                new Color(
                        80,
                        210,
                        255,
                        150
                )
        );

        g2.drawRoundRect(
                faceX,
                faceY,
                faceWidth,
                faceHeight,
                30,
                30
        );

        // =====================================================
        // EYES
        // =====================================================

        g2.setColor(
                CYAN
        );

        // Left eye

        g2.fillOval(
                mascotCenterX - 34,
                faceY + 22,
                14,
                19
        );

        // Right eye

        g2.fillOval(
                mascotCenterX + 20,
                faceY + 22,
                14,
                19
        );

        // Eye shine

        g2.setColor(
                Color.WHITE
        );

        g2.fillOval(
                mascotCenterX - 31,
                faceY + 25,
                5,
                5
        );

        g2.fillOval(
                mascotCenterX + 23,
                faceY + 25,
                5,
                5
        );

        // =====================================================
        // SMILE
        // =====================================================

        g2.setColor(
                CYAN
        );

        g2.setStroke(
                new BasicStroke(
                        3,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawArc(
                mascotCenterX - 23,
                faceY + 38,
                46,
                25,
                200,
                140
        );

        // =====================================================
        // CHEEK LIGHTS
        // =====================================================

        g2.setColor(
                new Color(
                        255,
                        100,
                        190,
                        120
                )
        );

        g2.fillOval(
                mascotCenterX - 48,
                faceY + 45,
                12,
                7
        );

        g2.fillOval(
                mascotCenterX + 36,
                faceY + 45,
                12,
                7
        );

        // =====================================================
        // CHEST PANEL
        // =====================================================

        int panelX =
                mascotCenterX - 42;

        int panelY =
                bodyY + 30;

        int panelWidth = 84;
        int panelHeight = 70;

        g2.setColor(
                DARK_BLUE
        );

        g2.fillRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                25,
                25
        );

        g2.setColor(
                new Color(
                        120,
                        210,
                        255,
                        150
                )
        );

        g2.drawRoundRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                25,
                25
        );

        // =====================================================
        // MAGNIFYING GLASS
        // =====================================================

        g2.setColor(
                Color.WHITE
        );

        g2.setStroke(
                new BasicStroke(
                        5,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawOval(
                mascotCenterX - 20,
                panelY + 12,
                32,
                32
        );

        g2.drawLine(
                mascotCenterX + 5,
                panelY + 38,
                mascotCenterX + 22,
                panelY + 55
        );

        // =====================================================
        // ARMS
        // =====================================================

        g2.setColor(
                BLUE
        );

        g2.setStroke(
                new BasicStroke(
                        10,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        // Left arm

        g2.drawLine(
                bodyX + 8,
                bodyY + 55,
                bodyX - 28,
                bodyY + 95
        );

        // Right arm waving upward

        g2.drawLine(
                bodyX + bodyWidth - 8,
                bodyY + 50,
                bodyX + bodyWidth + 27,
                bodyY + 15
        );

        // =====================================================
        // HANDS
        // =====================================================

        g2.setColor(
                CYAN
        );

        g2.fillOval(
                bodyX - 39,
                bodyY + 86,
                24,
                24
        );

        g2.fillOval(
                bodyX + bodyWidth + 18,
                bodyY + 4,
                27,
                27
        );

        // =====================================================
        // LEGS
        // =====================================================

        g2.setColor(
                DARK_BLUE
        );

        g2.setStroke(
                new BasicStroke(
                        11,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawLine(
                mascotCenterX - 35,
                bodyY + bodyHeight - 2,
                mascotCenterX - 42,
                bodyY + bodyHeight + 28
        );

        g2.drawLine(
                mascotCenterX + 35,
                bodyY + bodyHeight - 2,
                mascotCenterX + 42,
                bodyY + bodyHeight + 28
        );

        // =====================================================
        // FEET
        // =====================================================

        g2.setColor(
                BLUE
        );

        g2.fillRoundRect(
                mascotCenterX - 60,
                bodyY + bodyHeight + 18,
                38,
                18,
                15,
                15
        );

        g2.fillRoundRect(
                mascotCenterX + 22,
                bodyY + bodyHeight + 18,
                38,
                18,
                15,
                15
        );

        g2.dispose();
    }

    // =========================================================
    // DRAW STAR
    // =========================================================

    private void drawStar(
            Graphics2D g2,
            int x,
            int y,
            int size
    ) {

        int[] xPoints = {
                x,
                x + size / 3,
                x + size,
                x + size / 3,
                x
        };

        int[] yPoints = {
                y - size,
                y - size / 3,
                y,
                y + size / 3,
                y + size
        };

        g2.setColor(
                new Color(
                        255,
                        255,
                        255,
                        210
                )
        );

        g2.fillPolygon(
                xPoints,
                yPoints,
                5
        );
    }

    // =========================================================
    // CENTER TEXT
    // =========================================================

    private void drawCenteredText(
            Graphics2D g2,
            String text,
            int centerX,
            int y
    ) {

        FontMetrics metrics =
                g2.getFontMetrics();

        int width =
                metrics.stringWidth(text);

        g2.drawString(
                text,
                centerX - width / 2,
                y
        );
    }
}