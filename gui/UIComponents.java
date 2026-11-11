package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIComponents {

    // =========================================================
    // PRIVATE CONSTRUCTOR
    // =========================================================

    private UIComponents() {
        // Utility class - no object required.
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    public static JButton createPrimaryButton(String text) {

        JButton button = new JButton(text);

        button.setFont(UITheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(UITheme.PRIMARY_LIGHT);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                new EmptyBorder(
                        12,
                        25,
                        12,
                        25
                )
        );

        // Hover effect

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                button.setBackground(
                        UITheme.ACCENT
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(
                        UITheme.PRIMARY_LIGHT
                );
            }
        });

        return button;
    }

    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    public static JButton createSecondaryButton(String text) {

        JButton button = new JButton(text);

        button.setFont(UITheme.BUTTON_FONT);
        button.setForeground(UITheme.PRIMARY);
        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UITheme.BORDER,
                                1
                        ),
                        new EmptyBorder(
                                11,
                                24,
                                11,
                                24
                        )
                )
        );

        // Hover effect

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                button.setBackground(
                        UITheme.BACKGROUND
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(
                        Color.WHITE
                );
            }
        });

        return button;
    }

    // =========================================================
    // ROLE BUTTON
    // =========================================================

    public static JButton createRoleButton(
            String title,
            String description
    ) {

        JButton button = new JButton();

        button.setLayout(
                new BorderLayout(
                        10,
                        5
                )
        );

        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UITheme.BORDER,
                                1
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        // Title

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                UITheme.SUBHEADING_FONT
        );

        titleLabel.setForeground(
                UITheme.PRIMARY
        );

        // Description

        JLabel descriptionLabel =
                new JLabel(
                        "<html>" +
                                description +
                                "</html>"
                );

        descriptionLabel.setFont(
                UITheme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                UITheme.TEXT_SECONDARY
        );

        JPanel textPanel =
                new JPanel();

        textPanel.setOpaque(false);

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.add(titleLabel);

        textPanel.add(
                Box.createVerticalStrut(5)
        );

        textPanel.add(descriptionLabel);

        button.add(
                textPanel,
                BorderLayout.CENTER
        );

        // Hover effect

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                button.setBackground(
                        new Color(242, 247, 255)
                );

                button.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        UITheme.ACCENT,
                                        1
                                ),
                                new EmptyBorder(
                                        18,
                                        20,
                                        18,
                                        20
                                )
                        )
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(
                        Color.WHITE
                );

                button.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        UITheme.BORDER,
                                        1
                                ),
                                new EmptyBorder(
                                        18,
                                        20,
                                        18,
                                        20
                                )
                        )
                );
            }
        });

        return button;
    }

    // =========================================================
    // TITLE LABEL
    // =========================================================

    public static JLabel createTitle(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.TITLE_FONT
        );

        label.setForeground(
                UITheme.PRIMARY
        );

        return label;
    }

    // =========================================================
    // HEADING LABEL
    // =========================================================

    public static JLabel createHeading(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.HEADING_FONT
        );

        label.setForeground(
                UITheme.PRIMARY
        );

        return label;
    }

    // =========================================================
    // NORMAL LABEL
    // =========================================================

    public static JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.NORMAL_FONT
        );

        label.setForeground(
                UITheme.TEXT
        );

        return label;
    }

    // =========================================================
    // SECONDARY LABEL
    // =========================================================

    public static JLabel createSecondaryLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.NORMAL_FONT
        );

        label.setForeground(
                UITheme.TEXT_SECONDARY
        );

        return label;
    }

    // =========================================================
    // CARD PANEL
    // =========================================================

    public static JPanel createCard() {

        JPanel card =
                new JPanel();

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UITheme.BORDER,
                                1
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        return card;
    }

    // =========================================================
    // ROUNDED CARD
    // =========================================================

    public static JPanel createRoundedCard() {

        JPanel card =
                new JPanel() {

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
                                getBackground()
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                25,
                                25
                        );

                        g2.dispose();
                    }
                };

        card.setOpaque(false);

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        return card;
    }

    // =========================================================
    // DIVIDER
    // =========================================================

    public static JSeparator createDivider() {

        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                UITheme.BORDER
        );

        return separator;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    public static JTextField createTextField(
            String placeholder
    ) {

        JTextField field =
                new JTextField();

        field.setFont(
                UITheme.NORMAL_FONT
        );

        field.setForeground(
                UITheme.TEXT
        );

        field.setBackground(
                Color.WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UITheme.BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        field.setToolTipText(
                placeholder
        );

        return field;
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    public static JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setFont(
                UITheme.NORMAL_FONT
        );

        field.setForeground(
                UITheme.TEXT
        );

        field.setBackground(
                Color.WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                UITheme.BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        return field;
    }

    // =========================================================
    // SUCCESS LABEL
    // =========================================================

    public static JLabel createSuccessLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.NORMAL_FONT
        );

        label.setForeground(
                UITheme.SUCCESS
        );

        return label;
    }

    // =========================================================
    // ERROR LABEL
    // =========================================================

    public static JLabel createErrorLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                UITheme.NORMAL_FONT
        );

        label.setForeground(
                UITheme.ERROR
        );

        return label;
    }
}