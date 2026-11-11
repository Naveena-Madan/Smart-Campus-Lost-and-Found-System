package gui;

import java.awt.Color;
import java.awt.Font;

public class UITheme {

    // ==========================================
    // MAIN COLORS
    // ==========================================

    public static final Color PRIMARY =
            new Color(27, 54, 93);

    public static final Color PRIMARY_LIGHT =
            new Color(52, 112, 190);

    public static final Color ACCENT =
            new Color(77, 144, 255);

    public static final Color BACKGROUND =
            new Color(245, 247, 250);

    public static final Color WHITE =
            Color.WHITE;

    public static final Color TEXT =
            new Color(35, 42, 52);

    public static final Color TEXT_SECONDARY =
            new Color(100, 110, 125);

    public static final Color BORDER =
            new Color(220, 225, 232);

    public static final Color SUCCESS =
            new Color(46, 160, 67);

    public static final Color WARNING =
            new Color(245, 166, 35);

    public static final Color ERROR =
            new Color(220, 70, 70);


    // ==========================================
    // FONTS
    // ==========================================

    public static final Font TITLE_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    30
            );

    public static final Font HEADING_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    22
            );

    public static final Font SUBHEADING_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    17
            );

    public static final Font NORMAL_FONT =
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    14
            );

    public static final Font SMALL_FONT =
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    12
            );

    public static final Font BUTTON_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    14
            );


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    private UITheme() {

        // Prevent creating objects of this utility class.
    }
}
