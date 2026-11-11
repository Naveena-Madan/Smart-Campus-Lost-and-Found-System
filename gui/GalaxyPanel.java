package gui;

import javax.swing.*;
import java.awt.*;

public class GalaxyPanel extends JPanel {

    public GalaxyPanel() {

        setOpaque(false);

        setLayout(
                new GridBagLayout()
        );
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        // =========================================
        // GLASS EFFECT
        // =========================================

        g2.setColor(
                new Color(
                        8,
                        12,
                        35,
                        190
                )
        );

        g2.fillRoundRect(
                20,
                20,
                width - 40,
                height - 40,
                30,
                30
        );


        // =========================================
        // BLUE BORDER
        // =========================================

        g2.setColor(
                new Color(
                        100,
                        160,
                        255,
                        100
                )
        );

        g2.setStroke(
                new BasicStroke(1.5f)
        );

        g2.drawRoundRect(
                20,
                20,
                width - 40,
                height - 40,
                30,
                30
        );


        // =========================================
        // INNER GLOW
        // =========================================

        g2.setColor(
                new Color(
                        120,
                        80,
                        255,
                        25
                )
        );

        g2.drawRoundRect(
                25,
                25,
                width - 50,
                height - 50,
                25,
                25
        );

        g2.dispose();
    }
}
