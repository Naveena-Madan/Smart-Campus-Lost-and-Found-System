package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GalaxyBackground extends JPanel {

    private final Random random = new Random();

    private final Star[] stars;

    public GalaxyBackground() {

        setOpaque(true);

        stars = new Star[180];

        for (int i = 0; i < stars.length; i++) {

            stars[i] = new Star(
                    random.nextInt(1200),
                    random.nextInt(800),
                    1 + random.nextInt(3),
                    random.nextFloat()
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        // =========================================
        // SMOOTH GRAPHICS
        // =========================================

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        // =========================================
        // DARK SPACE BACKGROUND
        // =========================================

        GradientPaint background =
                new GradientPaint(
                        0,
                        0,
                        new Color(5, 8, 25),
                        width,
                        height,
                        new Color(25, 12, 55)
                );

        g2.setPaint(background);

        g2.fillRect(
                0,
                0,
                width,
                height
        );


        // =========================================
        // BLUE GALAXY GLOW
        // =========================================

        RadialGradientPaint blueGlow =
                new RadialGradientPaint(
                        new Point(
                                width / 4,
                                height / 3
                        ),
                        Math.max(
                                width,
                                height
                        ) * 0.65f,
                        new float[]{
                                0.0f,
                                0.35f,
                                1.0f
                        },
                        new Color[]{
                                new Color(
                                        45,
                                        80,
                                        180,
                                        100
                                ),
                                new Color(
                                        30,
                                        30,
                                        110,
                                        45
                                ),
                                new Color(
                                        0,
                                        0,
                                        0,
                                        0
                                )
                        }
                );

        g2.setPaint(blueGlow);

        g2.fillRect(
                0,
                0,
                width,
                height
        );


        // =========================================
        // PURPLE GALAXY GLOW
        // =========================================

        RadialGradientPaint purpleGlow =
                new RadialGradientPaint(
                        new Point(
                                width * 3 / 4,
                                height * 2 / 3
                        ),
                        Math.max(
                                width,
                                height
                        ) * 0.60f,
                        new float[]{
                                0.0f,
                                0.40f,
                                1.0f
                        },
                        new Color[]{
                                new Color(
                                        120,
                                        45,
                                        190,
                                        80
                                ),
                                new Color(
                                        80,
                                        25,
                                        130,
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

        g2.setPaint(purpleGlow);

        g2.fillRect(
                0,
                0,
                width,
                height
        );


        // =========================================
        // GALAXY CLOUD
        // =========================================

        GradientPaint galaxy =
                new GradientPaint(
                        0,
                        height * 0.65f,
                        new Color(
                                30,
                                20,
                                100,
                                40
                        ),
                        width,
                        height * 0.35f,
                        new Color(
                                100,
                                30,
                                150,
                                35
                        )
                );

        g2.setPaint(galaxy);

        g2.fillOval(
                -width / 5,
                height / 3,
                width + width / 2,
                height / 3
        );


        // =========================================
        // STARS
        // =========================================

        for (Star star : stars) {

            int x =
                    star.x % Math.max(width, 1);

            int y =
                    star.y % Math.max(height, 1);

            int alpha =
                    100 + (int) (star.brightness * 155);

            alpha =
                    Math.min(
                            255,
                            Math.max(
                                    50,
                                    alpha
                            )
                    );

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            alpha
                    )
            );

            g2.fillOval(
                    x,
                    y,
                    star.size,
                    star.size
            );
        }


        // =========================================
        // SOME BLUE STARS
        // =========================================

        for (int i = 0; i < 25; i++) {

            int x =
                    random.nextInt(
                            Math.max(width, 1)
                    );

            int y =
                    random.nextInt(
                            Math.max(height, 1)
                    );

            g2.setColor(
                    new Color(
                            130,
                            190,
                            255,
                            120
                    )
            );

            g2.fillOval(
                    x,
                    y,
                    2,
                    2
            );
        }


        // =========================================
        // DARK VIGNETTE
        // =========================================

        RadialGradientPaint vignette =
                new RadialGradientPaint(
                        new Point(
                                width / 2,
                                height / 2
                        ),
                        Math.max(
                                width,
                                height
                        ) * 0.75f,
                        new float[]{
                                0.55f,
                                1.0f
                        },
                        new Color[]{
                                new Color(
                                        0,
                                        0,
                                        0,
                                        0
                                ),
                                new Color(
                                        0,
                                        0,
                                        0,
                                        110
                                )
                        }
                );

        g2.setPaint(vignette);

        g2.fillRect(
                0,
                0,
                width,
                height
        );


        g2.dispose();
    }


    // =========================================
    // STAR CLASS
    // =========================================

    private static class Star {

        int x;
        int y;
        int size;
        float brightness;

        Star(
                int x,
                int y,
                int size,
                float brightness
        ) {

            this.x = x;
            this.y = y;
            this.size = size;
            this.brightness = brightness;
        }
    }
}
