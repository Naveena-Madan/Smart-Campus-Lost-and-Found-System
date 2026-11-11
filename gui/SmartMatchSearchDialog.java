package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class SmartMatchSearchDialog extends JDialog {

    // ==============================
    // GALAXY COLORS
    // ==============================

    private static final Color GALAXY_TOP =
            new Color(5, 8, 25);

    private static final Color GALAXY_BOTTOM =
            new Color(17, 7, 48);

    private static final Color CYAN =
            new Color(45, 210, 255);

    private static final Color BLUE =
            new Color(75, 115, 255);

    private static final Color WHITE =
            new Color(245, 248, 255);

    private static final Color SECONDARY_TEXT =
            new Color(170, 185, 215);

    private static final Color FIELD_BG =
            new Color(8, 18, 48);

    private static final Color FIELD_BORDER =
            new Color(55, 105, 175);

    // ==============================
    // FORM SIZE
    // ==============================

    private static final int FORM_WIDTH = 600;

    private static final int FIELD_HEIGHT = 55;

    // ==============================
    // FIELDS
    // ==============================

    private JTextField itemField;

    private JTextField locationField;

    private boolean confirmed = false;

    // ==============================
    // CONSTRUCTOR
    // ==============================

    public SmartMatchSearchDialog(
            JFrame parent
    ) {

        super(
                parent,
                "Smart Match Search",
                true
        );

        setUndecorated(true);

        setSize(
                720,
                570
        );

        setBackground(
                new Color(
                        0,
                        0,
                        0,
                        0
                )
        );

        setShape(
                new RoundRectangle2D.Double(
                        0,
                        0,
                        720,
                        570,
                        28,
                        28
                )
        );

        setLocationRelativeTo(
                parent
        );

        setResizable(false);

        buildUI();
    }

    // ==============================
    // BUILD UI
    // ==============================

    private void buildUI() {

        GalaxyPanel background =
                new GalaxyPanel();

        background.setLayout(
                new BorderLayout()
        );

        background.setBorder(
                new EmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        // ==============================
        // HEADER
        // ==============================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "✦  Smart Match Search"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Tell us what you lost and where you last saw it"
                );

        subtitle.setForeground(
                SECONDARY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        titlePanel.add(
                subtitle
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // ==============================
        // CLOSE BUTTON
        // ==============================

        JButton closeButton =
                new JButton(
                        "×"
                );

        closeButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        32
                )
        );

        closeButton.setForeground(
                SECONDARY_TEXT
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
                closeButton,
                BorderLayout.EAST
        );

        background.add(
                header,
                BorderLayout.NORTH
        );

        // ==============================
        // FORM
        // ==============================

        JPanel form =
                new JPanel();

        form.setOpaque(false);

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );

        form.setBorder(
                new EmptyBorder(
                        45,
                        0,
                        10,
                        0
                )
        );

        // ==============================
        // ITEM LABEL
        // ==============================

        JLabel itemLabel =
                createLabel(
                        "LOST ITEM NAME"
                );

        // ==============================
        // ITEM FIELD
        // ==============================

        itemField =
                createTextField();

        // ==============================
        // LOCATION LABEL
        // ==============================

        JLabel locationLabel =
                createLabel(
                        "LOST LOCATION"
                );

        // ==============================
        // LOCATION FIELD
        // ==============================

        locationField =
                createTextField();

        // ==============================
        // ITEM LABEL
        // ==============================

        form.add(
                itemLabel
        );

        form.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // ==============================
        // ITEM FIELD
        // ==============================

        form.add(
                itemField
        );

        form.add(
                Box.createVerticalStrut(
                        28
                )
        );

        // ==============================
        // LOCATION LABEL
        // ==============================

        form.add(
                locationLabel
        );

        form.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // ==============================
        // LOCATION FIELD
        // ==============================

        form.add(
                locationField
        );

        form.add(
                Box.createVerticalStrut(
                        35
                )
        );

        // ==============================
        // BUTTON PANEL
        // ==============================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                0
                        )
                );

        buttons.setOpaque(false);

        buttons.setMaximumSize(
                new Dimension(
                        FORM_WIDTH,
                        60
                )
        );

        buttons.setPreferredSize(
                new Dimension(
                        FORM_WIDTH,
                        60
                )
        );

        JButton cancelButton =
                createButton(
                        "Cancel",
                        new Color(
                                35,
                                48,
                                85
                        )
                );

        JButton searchButton =
                createButton(
                        "Find Matches",
                        BLUE
                );

        cancelButton.addActionListener(
                e -> dispose()
        );

        searchButton.addActionListener(
                e -> confirmSearch()
        );

        buttons.add(
                cancelButton
        );

        buttons.add(
                searchButton
        );

        form.add(
                buttons
        );

        // ==============================
        // CENTER THE FORM
        // ==============================

        JPanel formWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        formWrapper.setOpaque(false);

        formWrapper.add(
                form
        );

        background.add(
                formWrapper,
                BorderLayout.CENTER
        );

        // ==============================
        // SET CONTENT
        // ==============================

        setContentPane(
                background
        );
    }

    // ==============================
    // LABEL
    // ==============================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setForeground(
                CYAN
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        label.setVerticalAlignment(
                SwingConstants.CENTER
        );

        // Give the label the same width
        // as the text field.

        label.setPreferredSize(
                new Dimension(
                        FORM_WIDTH,
                        25
                )
        );

        label.setMinimumSize(
                new Dimension(
                        FORM_WIDTH,
                        25
                )
        );

        label.setMaximumSize(
                new Dimension(
                        FORM_WIDTH,
                        25
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return label;
    }

    // ==============================
    // TEXT FIELD
    // ==============================

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

        field.setForeground(
                WHITE
        );

        field.setCaretColor(
                CYAN
        );

        field.setBackground(
                FIELD_BG
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                FIELD_BORDER,
                                1
                        ),
                        new EmptyBorder(
                                12,
                                16,
                                12,
                                16
                        )
                )
        );

        // Wide and slightly thick.

        field.setPreferredSize(
                new Dimension(
                        FORM_WIDTH,
                        FIELD_HEIGHT
                )
        );

        field.setMinimumSize(
                new Dimension(
                        FORM_WIDTH,
                        FIELD_HEIGHT
                )
        );

        field.setMaximumSize(
                new Dimension(
                        FORM_WIDTH,
                        FIELD_HEIGHT
                )
        );

        field.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return field;
    }

    // ==============================
    // BUTTON
    // ==============================

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
                        14
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

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        100,
                                        170,
                                        255,
                                        100
                                ),
                                1
                        ),
                        new EmptyBorder(
                                11,
                                25,
                                11,
                                25
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // ==============================
    // SEARCH
    // ==============================

    private void confirmSearch() {

        String item =
                itemField
                        .getText()
                        .trim();

        String location =
                locationField
                        .getText()
                        .trim();

        if (
                item.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the lost item name.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                location.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the lost location.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        confirmed = true;

        dispose();
    }

    // ==============================
    // GETTERS
    // ==============================

    public boolean isConfirmed() {

        return confirmed;
    }

    public String getLostItem() {

        return itemField
                .getText()
                .trim();
    }

    public String getLostLocation() {

        return locationField
                .getText()
                .trim();
    }

    // ==============================
    // GALAXY BACKGROUND
    // ==============================

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
                                1
                                        + random.nextFloat()
                                        * 2.4f,
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

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    28,
                    28
            );

            // ==============================
            // STARS
            // ==============================

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

            // ==============================
            // BORDER
            // ==============================

            g2.setColor(
                    new Color(
                            45,
                            210,
                            255,
                            100
                    )
            );

            g2.drawRoundRect(
                    1,
                    1,
                    getWidth() - 3,
                    getHeight() - 3,
                    28,
                    28
            );

            g2.dispose();
        }
    }

    // ==============================
    // STAR
    // ==============================

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
}