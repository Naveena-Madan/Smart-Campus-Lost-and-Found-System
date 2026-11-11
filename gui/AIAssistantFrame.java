package gui;

import service.ReportService;
import service.SmartMatchService;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AIAssistantFrame extends JFrame {

    // =========================================================
    // MODE
    // =========================================================

    private final String mode;
    private final String loggedInFullName;
    private final String loggedInUsername;

    // =========================================================
    // UI
    // =========================================================

    private JPanel chatPanel;
    private JScrollPane chatScrollPane;
    private JTextField inputField;
    private MascotPanel mascot;

    // =========================================================
    // CONVERSATION DATA
    // =========================================================

    private final List<String> answers = new ArrayList<>();

    private int questionIndex = 0;

    private String selectedImagePath = null;

    private final ReportService reportService = new ReportService();

    // =========================================================
    // QUESTIONS
    // =========================================================

    private final String[] lostQuestions = {
            "What did you lose?",
            "Where did you last see it?",
            "Approximately when did you lose it?",
            "Can you describe the item?",
            "Does it have any unique identifying features?",
            "Would you like to add a photo? This is optional.",
            "Is there anything else you would like to tell me?"
    };

    private final String[] foundQuestions = {
            "What did you find?",
            "Where did you find it?",
            "When did you find it?",
            "Can you describe the item?",
            "What identifying details does it have?",
            "Please upload a photo of the item. A photo is required for found items.",
            "Where is the item currently kept?",
            "Is there anything else you would like to tell me?"
    };

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(7, 12, 38);

    private static final Color HEADER =
            new Color(10, 16, 40);

    private static final Color CARD =
            new Color(15, 25, 58);

    private static final Color CARD_2 =
            new Color(20, 30, 65);

    private static final Color USER_BUBBLE =
            new Color(50, 80, 160);

    private static final Color BORDER =
            new Color(60, 90, 150);

    private static final Color BLUE =
            new Color(70, 115, 255);

    private static final Color CYAN =
            new Color(60, 210, 255);

    private static final Color GREEN =
            new Color(70, 220, 155);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color SECONDARY_TEXT =
            new Color(175, 195, 230);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AIAssistantFrame(
            String fullName,
            String username,
            String mode
    ) {

        this.loggedInFullName =
                fullName == null || fullName.trim().isEmpty()
                        ? "Student"
                        : fullName;

        this.loggedInUsername =
                username == null || username.trim().isEmpty()
                        ? ""
                        : username;

        this.mode = mode;

        setTitle("Smart Campus - AI Assistant");

        setSize(1180, 780);

        setMinimumSize(
                new Dimension(1050, 700)
        );

        setLocationRelativeTo(null);

        // Open the AI Assistant window maximized.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout()
        );

        initializeUI();

        startConversation();
    }

    // Compatibility constructor for standalone testing.
    public AIAssistantFrame(String mode) {
        this("Student", "", mode);
    }

    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initializeUI() {

        createHeader();

        createMainArea();

        createInputArea();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                HEADER
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        78
                )
        );

        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        10,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "AI Campus Assistant"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        mode.equalsIgnoreCase("LOST")
                                ? "Lost Item Assistant"
                                : "Found Item Assistant"
                );

        subtitle.setForeground(
                CYAN
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JLabel status =
                new JLabel(
                        "● Assistant Online"
                );

        status.setForeground(
                GREEN
        );

        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        status.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        0,
                        25
                )
        );

        header.add(
                status,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // MAIN AREA
    // =========================================================

    private void createMainArea() {

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                18,
                                0
                        )
                );

        main.setBackground(
                BACKGROUND
        );

        main.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        12,
                        18
                )
        );

        // -----------------------------------------------------
        // ROBOT PANEL
        // -----------------------------------------------------

        JPanel robotCard =
                new JPanel(
                        new BorderLayout()
                );

        robotCard.setBackground(
                CARD
        );

        robotCard.setPreferredSize(
                new Dimension(
                        330,
                        0
                )
        );

        robotCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        // Hide only the speech bubble on the Lost/Found AI Assistant.
        // The shared MascotPanel remains unchanged for other screens.
        mascot =
                new MascotPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        Graphics2D bubbleMask = (Graphics2D) g.create();

                        // Cover the bubble and its glow.
                        bubbleMask.setColor(CARD);
                        bubbleMask.fillRect(
                                20,
                                8,
                                Math.max(0, getWidth() - 40),
                                99
                        );

                        // Cover the bubble tail.
                        int centerX = getWidth() / 2;
                        Polygon tailMask = new Polygon();
                        tailMask.addPoint(centerX - 14, 97);
                        tailMask.addPoint(centerX, 113);
                        tailMask.addPoint(centerX + 14, 97);
                        bubbleMask.fillPolygon(tailMask);

                        // Redraw the robot antenna so it stays visible.
                        int headY = 135;
                        bubbleMask.setColor(CYAN);
                        bubbleMask.setStroke(new BasicStroke(4.0f));
                        bubbleMask.drawLine(
                                centerX,
                                headY,
                                centerX,
                                headY - 25
                        );

                        bubbleMask.setColor(
                                new Color(80, 220, 255, 80)
                        );
                        bubbleMask.fillOval(
                                centerX - 13,
                                headY - 38,
                                26,
                                26
                        );

                        bubbleMask.setColor(CYAN);
                        bubbleMask.fillOval(
                                centerX - 7,
                                headY - 32,
                                14,
                                14
                        );

                        bubbleMask.dispose();
                    }
                };

        mascot.setPreferredSize(
                new Dimension(
                        300,
                        500
                )
        );

        mascot.setMinimumSize(
                new Dimension(
                        280,
                        500
                )
        );

        mascot.setMessage(
                ""
        );

        robotCard.add(
                mascot,
                BorderLayout.CENTER
        );

        JLabel robotCaption =
                new JLabel(
                        "Your personal campus companion",
                        SwingConstants.CENTER
                );

        robotCaption.setForeground(
                SECONDARY_TEXT
        );

        robotCaption.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        robotCaption.setBorder(
                new EmptyBorder(
                        4,
                        0,
                        8,
                        0
                )
        );

        robotCard.add(
                robotCaption,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // CHAT
        // -----------------------------------------------------

        chatPanel =
                new JPanel();

        chatPanel.setLayout(
                new BoxLayout(
                        chatPanel,
                        BoxLayout.Y_AXIS
                )
        );

        chatPanel.setBackground(
                BACKGROUND
        );

        chatPanel.setBorder(
                new EmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        chatScrollPane =
                new JScrollPane(
                        chatPanel
                );

        chatScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        chatScrollPane.setBackground(
                BACKGROUND
        );

        chatScrollPane.getViewport()
                .setBackground(
                        BACKGROUND
                );

        chatScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        chatScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        main.add(
                robotCard,
                BorderLayout.WEST
        );

        main.add(
                chatScrollPane,
                BorderLayout.CENTER
        );

        add(
                main,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // INPUT AREA
    // =========================================================

    private void createInputArea() {

        JPanel inputArea =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        inputArea.setBackground(
                HEADER
        );

        inputArea.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        15,
                        20
                )
        );

        inputField =
                new JTextField();

        inputField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        inputField.setBackground(
                CARD_2
        );

        inputField.setForeground(
                WHITE
        );

        inputField.setCaretColor(
                WHITE
        );

        inputField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                11,
                                14,
                                11,
                                14
                        )
                )
        );

        JButton send =
                createModernButton(
                        "Send",
                        BLUE
                );

        send.addActionListener(
                e -> processAnswer()
        );

        inputField.addActionListener(
                e -> processAnswer()
        );

        inputArea.add(
                inputField,
                BorderLayout.CENTER
        );

        inputArea.add(
                send,
                BorderLayout.EAST
        );

        add(
                inputArea,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // START CONVERSATION
    // =========================================================

    private void startConversation() {

        questionIndex = 0;

        answers.clear();

        selectedImagePath = null;

        String welcome;

        if (
                mode.equalsIgnoreCase("LOST")
        ) {

            welcome =
                    "Don't worry 😊 I'm here with you. "
                            + "I'll help you describe your lost belongings "
                            + "carefully so we can find the best possible match.";

        } else {

            welcome =
                    "Thank you for helping the campus community 💙 "
                            + "Don't worry, I'll guide you through everything "
                            + "and make sure we record the found item properly.";
        }

        setRobotMessage(
                welcome
        );

        addBotMessage(
                welcome
        );

        Timer timer =
                new Timer(
                        700,
                        e -> {

                            String secondMessage;

                            if (
                                    mode.equalsIgnoreCase("LOST")
                            ) {

                                secondMessage =
                                        "Take your time. I'll ask you a few simple questions, "
                                                + "one at a time. 🤝";

                            } else {

                                secondMessage =
                                        "I'll ask you a few simple questions, "
                                                + "one at a time. You're doing great! ✨";
                            }

                            setRobotMessage(
                                    secondMessage
                            );

                            addBotMessage(
                                    secondMessage
                            );

                            Timer next =
                                    new Timer(
                                            700,
                                            e2 -> askCurrentQuestion()
                                    );

                            next.setRepeats(false);

                            next.start();
                        }
                );

        timer.setRepeats(false);

        timer.start();

        inputField.requestFocusInWindow();
    }

    // =========================================================
    // ASK QUESTION
    // =========================================================

    private void askCurrentQuestion() {

        String question;

        if (
                mode.equalsIgnoreCase("LOST")
        ) {

            if (
                    questionIndex >=
                            lostQuestions.length
            ) {

                finishConversation();

                return;
            }

            question =
                    lostQuestions[
                            questionIndex
                            ];

        } else {

            if (
                    questionIndex >=
                            foundQuestions.length
            ) {

                finishConversation();

                return;
            }

            question =
                    foundQuestions[
                            questionIndex
                            ];
        }

        setRobotMessage(
                question
        );

        addBotMessage(
                question
        );

        if (
                isPhotoQuestion()
        ) {

            inputField.setEnabled(false);

            showPhotoButtons(
                    mode.equalsIgnoreCase("FOUND")
            );

        } else {

            inputField.setEnabled(true);

            inputField.requestFocusInWindow();
        }
    }

    // =========================================================
    // PROCESS ANSWER
    // =========================================================

    private void processAnswer() {

        if (
                !inputField.isEnabled()
        ) {

            return;
        }

        String answer =
                inputField
                        .getText()
                        .trim();

        if (
                answer.isEmpty()
        ) {

            return;
        }

        addUserMessage(
                answer
        );

        answers.add(
                answer
        );

        inputField.setText("");

        questionIndex++;

        inputField.setEnabled(false);

        Timer thinking =
                new Timer(
                        450,
                        e -> {

                            String acknowledgement =
                                    getAcknowledgement();

                            setRobotMessage(
                                    acknowledgement
                            );

                            addBotMessage(
                                    acknowledgement
                            );

                            Timer next =
                                    new Timer(
                                            500,
                                            e2 -> askCurrentQuestion()
                                    );

                            next.setRepeats(false);

                            next.start();
                        }
                );

        thinking.setRepeats(false);

        thinking.start();
    }

    // =========================================================
    // FRIENDLY ACKNOWLEDGEMENT
    // =========================================================

    private String getAcknowledgement() {

        switch (questionIndex) {

            case 1:
                return "Got it! 👍 I'll keep that in mind.";

            case 2:
                return "Perfect. That location detail can be very useful. 📍";

            case 3:
                return "Thanks! The timing will help narrow down the possibilities. ⏰";

            case 4:
                return "Great description! I'm noting the important details. ✨";

            case 5:
                return "Excellent. Those unique details can help identify the item. 🔎";

            case 6:
                return "Thank you! We're almost done. 💙";

            case 7:
                return "Perfect. Just one more detail and I'll prepare your report. 📝";

            default:
                return "Thanks! I've got it. 😊";
        }
    }

    // =========================================================
    // PHOTO QUESTION
    // =========================================================

    private boolean isPhotoQuestion() {

        return questionIndex == 5;
    }

    // =========================================================
    // PHOTO BUTTONS
    // =========================================================

    private void showPhotoButtons(
            boolean required
    ) {

        JPanel photoPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        photoPanel.setOpaque(false);

        JButton choose =
                createModernButton(
                        "📷  Choose Photo",
                        BLUE
                );

        choose.addActionListener(
                e -> openModernImagePicker(required)
        );

        photoPanel.add(
                choose
        );

        if (!required) {

            JButton skip =
                    createModernButton(
                            "Skip for now",
                            new Color(
                                    65,
                                    75,
                                    100
                            )
                    );

            skip.addActionListener(
                    e -> skipPhoto()
            );

            photoPanel.add(
                    skip
            );
        }

        chatPanel.add(
                photoPanel
        );

        chatPanel.add(
                Box.createVerticalStrut(12)
        );

        refreshChat();
    }

    // =========================================================
    // NATIVE WINDOWS IMAGE PICKER
    // =========================================================

    private void openModernImagePicker(
            boolean required
    ) {

        selectedImagePath = null;

        FileDialog fileDialog =
                new FileDialog(
                        this,
                        "Choose Item Photo",
                        FileDialog.LOAD
                );

        fileDialog.setFilenameFilter(
                (dir, name) -> {

                    String lower =
                            name.toLowerCase();

                    // Accept common camera and phone image formats. Supported inputs are normalized to PNG.
                    // WebP decoding requires the optional ImageIO WebP plugin on the classpath.
                    return lower.endsWith(".jpg")
                            || lower.endsWith(".jpeg")
                            || lower.endsWith(".jfif")
                            || lower.endsWith(".png")
                            || lower.endsWith(".gif")
                            || lower.endsWith(".bmp")
                            || lower.endsWith(".webp")
                            || lower.endsWith(".tif")
                            || lower.endsWith(".tiff");
                }
        );

        fileDialog.setMultipleMode(false);

        fileDialog.setVisible(true);

        String fileName =
                fileDialog.getFile();

        String directory =
                fileDialog.getDirectory();

        if (
                fileName == null
                        ||
                        directory == null
        ) {

            if (required) {

                setRobotMessage(
                        "No problem 😊 Take your time. "
                                + "A photo is required for a found item, "
                                + "so choose one whenever you're ready."
                );

                addBotMessage(
                        "No problem 😊 A photo is required for a found item. "
                                + "Please choose one when you're ready."
                );
            }

            return;
        }

        File selectedFile =
                new File(
                        directory,
                        fileName
                );

        // Keep common formats as PNG when Java can decode them. If Java cannot
        // decode a format such as WebP/HEIC, preserve the original image instead
        // of blocking the student from submitting the report. The Admin viewer
        // can hand unsupported formats to the computer's default image app.
        if (!selectedFile.isFile() || !selectedFile.canRead()) {
            JOptionPane.showMessageDialog(
                    this,
                    "The selected file cannot be read. Please choose another photo.",
                    "Unable to Read Photo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Path imageDirectory = Paths.get(
                    System.getProperty("user.home"),
                    "SmartCampusLostFound",
                    "images"
            );
            Files.createDirectories(imageDirectory);

            BufferedImage decodedImage = null;
            try {
                decodedImage = ImageIO.read(selectedFile);
            } catch (IOException ignored) {
                // Unsupported by the built-in decoder; preserve original below.
            }

            if (decodedImage != null) {
                Path normalizedImage = imageDirectory.resolve(
                        "report_photo_" + UUID.randomUUID() + ".png"
                );
                boolean written = ImageIO.write(decodedImage, "png", normalizedImage.toFile());
                if (written) {
                    selectedImagePath = normalizedImage.toAbsolutePath().toString();
                }
            }

            // Unsupported image formats are still accepted and copied to the
            // application's managed image folder without changing their bytes.
            if (selectedImagePath == null || selectedImagePath.trim().isEmpty()) {
                String originalName = selectedFile.getName();
                String extension = "";
                int dot = originalName.lastIndexOf('.');
                if (dot >= 0) {
                    extension = originalName.substring(dot).replaceAll("[^A-Za-z0-9.]", "");
                }
                Path preservedImage = imageDirectory.resolve(
                        "report_photo_" + UUID.randomUUID() + extension
                );
                Files.copy(selectedFile.toPath(), preservedImage);
                selectedImagePath = preservedImage.toAbsolutePath().toString();
            }
        } catch (IOException | RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "The photo could not be saved. Please choose another image.\n\n"
                            + ex.getMessage(),
                    "Unable to Save Photo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        addUserMessage(
                "📷 Added photo: "
                        + selectedFile.getName()
        );

        questionIndex++;

        inputField.setEnabled(false);

        Timer next =
                new Timer(
                        500,
                        e -> {

                            String message =
                                    "Nice! 📸 I've added the photo to your report.";

                            setRobotMessage(
                                    message
                            );

                            addBotMessage(
                                    message
                            );

                            Timer nextQuestion =
                                    new Timer(
                                            500,
                                            e2 -> askCurrentQuestion()
                                    );

                            nextQuestion.setRepeats(false);

                            nextQuestion.start();
                        }
                );

        next.setRepeats(false);

        next.start();
    }

    // =========================================================
    // SKIP PHOTO
    // =========================================================

    private void skipPhoto() {

        selectedImagePath = null;

        addUserMessage(
                "I'll continue without adding a photo."
        );

        questionIndex++;

        inputField.setEnabled(false);

        Timer next =
                new Timer(
                        500,
                        e -> askCurrentQuestion()
                );

        next.setRepeats(false);

        next.start();
    }

    // =========================================================
    // FINISH
    // =========================================================

    private void finishConversation() {

        inputField.setEnabled(false);

        String ending;

        if (
                mode.equalsIgnoreCase("LOST")
        ) {

            ending =
                    "You're all set! 💙 "
                            + "I've collected the important details about your lost item. "
                            + "Don't worry, I'll organize everything into a clear report "
                            + "so it's easier to search for your belongings.";

        } else {

            ending =
                    "You're all set! 💙 "
                            + "I've collected the important details about the found item. "
                            + "Thank you for helping return someone's belongings. "
                            + "I'll organize everything into a clear report now.";
        }

        setRobotMessage(
                ending
        );

        addBotMessage(
                ending
        );

        Timer timer =
                new Timer(
                        650,
                        e -> showProfessionalReport()
                );

        timer.setRepeats(false);

        timer.start();
    }

    // =========================================================
    // PROFESSIONAL REPORT
    // =========================================================

    private void showProfessionalReport() {

        final JDialog dialog =
                new JDialog(
                        this,
                        "Smart Report Preview",
                        true
                );

        // Open the report as a true screen-sized window.
        GraphicsConfiguration gc =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration();

        Rectangle bounds = gc.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        int reportWidth = bounds.width - insets.left - insets.right;
        int reportHeight = bounds.height - insets.top - insets.bottom;

        dialog.setSize(reportWidth, reportHeight);
        dialog.setLocation(
                bounds.x + insets.left,
                bounds.y + insets.top
        );
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setBorder(new EmptyBorder(18, 28, 18, 28));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Smart Lost & Found Report");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JLabel subtitle = new JLabel(
                "A professional summary created from your AI Assistant conversation"
        );
        subtitle.setForeground(SECONDARY_TEXT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitle);

        header.add(heading, BorderLayout.WEST);

        JLabel badge = new JLabel(
                mode.equalsIgnoreCase("LOST")
                        ? " LOST ITEM "
                        : " FOUND ITEM "
        );
        badge.setOpaque(true);
        badge.setBackground(
                mode.equalsIgnoreCase("LOST")
                        ? new Color(90, 90, 180)
                        : new Color(45, 150, 115)
        );
        badge.setForeground(WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setBorder(new EmptyBorder(9, 14, 9, 14));

        header.add(badge, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // -----------------------------------------------------
        // REPORT CONTENT
        // Everything is placed inside a vertical scroll pane so
        // nothing can ever extend outside the report window.
        // -----------------------------------------------------

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        String item = getAnswer(0);
        String location = getAnswer(1);
        String time = getAnswer(2);
        String description = getAnswer(3);
        String identifying = getAnswer(4);
        String additional = mode.equalsIgnoreCase("LOST")
                ? getAnswer(5)
                : getAnswer(6);

        // -----------------------------------------------------
        // AI SUMMARY
        // -----------------------------------------------------

        JPanel summaryCard = createReportCard(
                "AI Summary",
                "Key information extracted from your conversation."
        );
        summaryCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 180)
        );

        JTextArea summary = createReportTextArea(
                buildSmartSummary(
                        item,
                        location,
                        time,
                        description,
                        identifying
                ),
                13
        );
        summaryCard.add(summary);
        content.add(summaryCard);
        content.add(Box.createVerticalStrut(12));

        // -----------------------------------------------------
        // KEY DETAILS
        // -----------------------------------------------------

        JPanel detailsCard = createReportCard(
                "Key Details",
                "The main information needed to identify and process the report."
        );
        detailsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 420)
        );

        JPanel detailsGrid = new JPanel(new GridLayout(3, 2, 10, 10));
        detailsGrid.setOpaque(false);
        detailsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));

        addDetail(detailsGrid, "Item", item);
        addDetail(
                detailsGrid,
                mode.equalsIgnoreCase("LOST") ? "Last Seen At" : "Found At",
                location
        );
        addDetail(
                detailsGrid,
                mode.equalsIgnoreCase("LOST") ? "Approx. Lost Time" : "Found Time",
                time
        );
        addDetail(detailsGrid, "Description", description);
        addDetail(detailsGrid, "Identifying Details", identifying);
        addDetail(
                detailsGrid,
                mode.equalsIgnoreCase("LOST")
                        ? "Additional Information"
                        : "Currently Kept At",
                additional
        );

        detailsCard.add(detailsGrid);
        content.add(detailsCard);
        content.add(Box.createVerticalStrut(12));

        // -----------------------------------------------------
        // EVIDENCE
        // -----------------------------------------------------

        JPanel evidenceCard = createReportCard(
                "Evidence",
                "Photo information attached to this report."
        );
        evidenceCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        evidenceCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        String evidenceText;
        if (selectedImagePath != null) {
            File image = new File(selectedImagePath);
            evidenceText = "📷 Photo attached: " + image.getName();
        } else {
            evidenceText = mode.equalsIgnoreCase("FOUND")
                    ? "No photo selected."
                    : "No photo added. Photo is optional for lost items.";
        }

        JTextArea evidence = createReportTextArea(evidenceText, 13);
        evidenceCard.add(evidence);
        content.add(evidenceCard);

        JScrollPane reportScroll = new JScrollPane(content);
        reportScroll.setBorder(null);
        reportScroll.setBackground(BACKGROUND);
        reportScroll.getViewport().setBackground(BACKGROUND);
        reportScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        reportScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        // -----------------------------------------------------
        // GALAXY THEME SCROLL BAR
        // -----------------------------------------------------

        JScrollBar reportBar =
                reportScroll.getVerticalScrollBar();

        reportBar.setPreferredSize(
                new Dimension(
                        12,
                        0
                )
        );

        reportBar.setBackground(
                HEADER
        );

        reportBar.setUI(
                new BasicScrollBarUI() {

                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(80, 120, 255);
                        trackColor = new Color(10, 16, 40);
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createInvisibleScrollButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createInvisibleScrollButton();
                    }

                    @Override
                    protected void paintThumb(
                            Graphics g,
                            JComponent c,
                            Rectangle thumbBounds
                    ) {
                        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                            return;
                        }

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                new Color(75, 115, 255)
                        );

                        g2.fillRoundRect(
                                thumbBounds.x + 2,
                                thumbBounds.y + 2,
                                thumbBounds.width - 4,
                                thumbBounds.height - 4,
                                8,
                                8
                        );

                        g2.dispose();
                    }

                    @Override
                    protected void paintTrack(
                            Graphics g,
                            JComponent c,
                            Rectangle trackBounds
                    ) {
                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setColor(
                                new Color(8, 13, 35)
                        );

                        g2.fillRect(
                                trackBounds.x,
                                trackBounds.y,
                                trackBounds.width,
                                trackBounds.height
                        );

                        g2.dispose();
                    }

                    private JButton createInvisibleScrollButton() {
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

                        button.setOpaque(false);
                        button.setContentAreaFilled(false);
                        button.setBorderPainted(false);
                        button.setFocusable(false);

                        return button;
                    }
                }
        );

        root.add(reportScroll, BorderLayout.CENTER);

        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(HEADER);
        footer.setBorder(new EmptyBorder(10, 20, 12, 20));

        JLabel footerText = new JLabel(
                "✓ Information reviewed by AI Assistant"
        );
        footerText.setForeground(GREEN);
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerText, BorderLayout.WEST);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        buttons.setOpaque(false);

        JButton close = createModernButton(
                "Close Preview",
                new Color(65, 75, 100)
        );
        close.addActionListener(e -> dialog.dispose());

        String submitText = mode.equalsIgnoreCase("LOST")
                ? "Submit Lost Report"
                : "Submit Found Report";

        JButton submitButton = createModernButton(
                submitText,
                GREEN
        );

        submitButton.addActionListener(e -> submitReport(dialog));

        buttons.add(close);
        buttons.add(submitButton);
        footer.add(buttons, BorderLayout.EAST);

        root.add(footer, BorderLayout.SOUTH);

        dialog.add(root);
        dialog.setVisible(true);
    }

    // =========================================================
    // MODERN REPORT READY DIALOG
    // =========================================================

    private void showReportReadyDialog() {

        final JDialog readyDialog =
                new JDialog(
                        this,
                        "Report Ready",
                        true
                );

        readyDialog.setSize(520, 310);
        readyDialog.setResizable(false);
        readyDialog.setLocationRelativeTo(this);
        readyDialog.setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(70, 115, 255),
                                1
                        ),
                        new EmptyBorder(0, 0, 0, 0)
                )
        );

        // -----------------------------------------------------
        // TOP ACCENT
        // -----------------------------------------------------

        JPanel accent = new JPanel();
        accent.setBackground(
                mode.equalsIgnoreCase("LOST")
                        ? new Color(90, 90, 180)
                        : new Color(45, 150, 115)
        );
        accent.setPreferredSize(new Dimension(0, 5));
        root.add(accent, BorderLayout.NORTH);

        // -----------------------------------------------------
        // CONTENT
        // -----------------------------------------------------

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );
        content.setBorder(
                new EmptyBorder(24, 28, 18, 28)
        );

        JLabel icon = new JLabel(
                mode.equalsIgnoreCase("LOST")
                        ? "✓"
                        : "✓",
                SwingConstants.CENTER
        );
        icon.setOpaque(true);
        icon.setBackground(GREEN);
        icon.setForeground(BACKGROUND);
        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );
        icon.setPreferredSize(new Dimension(52, 52));
        icon.setMaximumSize(new Dimension(52, 52));
        icon.setMinimumSize(new Dimension(52, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(
                mode.equalsIgnoreCase("LOST")
                        ? "Lost Item Report Ready"
                        : "Found Item Report Ready"
        );
        title.setForeground(WHITE);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel(
                "<html><div style='text-align:center; width:410px;'>"
                        + "Your report has been reviewed by the AI Assistant and is ready for submission."
                        + "</div></html>"
        );
        message.setForeground(SECONDARY_TEXT);
        message.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(icon);
        content.add(Box.createVerticalStrut(14));
        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(message);
        content.add(Box.createVerticalStrut(18));

        JLabel nextStep = new JLabel(
                mode.equalsIgnoreCase("LOST")
                        ? "Your lost item report is ready to be submitted."
                        : "Your found item report is ready to be submitted."
        );
        nextStep.setForeground(GREEN);
        nextStep.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );
        nextStep.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(nextStep);

        root.add(content, BorderLayout.CENTER);

        // -----------------------------------------------------
        // BUTTON
        // -----------------------------------------------------

        JPanel footer = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        12
                )
        );
        footer.setBackground(HEADER);

        JButton ok = createModernButton(
                "✓  OK",
                BLUE
        );

        ok.addActionListener(e -> readyDialog.dispose());

        footer.add(ok);
        root.add(footer, BorderLayout.SOUTH);

        readyDialog.add(root);
        readyDialog.setVisible(true);
    }

    // =========================================================
    // SUBMIT REPORT TO MYSQL
    // =========================================================

    private void submitReport(JDialog reportDialog) {

        String reportType =
                mode.equalsIgnoreCase("LOST")
                        ? "LOST"
                        : "FOUND";

        String itemName = getAnswer(0);
        String location = getAnswer(1);
        String eventTime = getAnswer(2);
        String description = getAnswer(3);
        String identifyingDetails = getAnswer(4);

        String currentLocation = null;
        String additionalDetails = null;

        /*
         * The photo question does not add a text answer to the
         * answers list, so the indexes after the photo are:
         *
         * LOST:
         * 0 item
         * 1 location
         * 2 time
         * 3 description
         * 4 identifying details
         * 5 additional information
         *
         * FOUND:
         * 0 item
         * 1 location
         * 2 time
         * 3 description
         * 4 identifying details
         * 5 current location
         * 6 additional information
         */

        if (mode.equalsIgnoreCase("LOST")) {

            additionalDetails = getAnswer(5);

        } else {

            currentLocation = getAnswer(5);
            additionalDetails = getAnswer(6);
        }

        try {

            // -----------------------------------------------------
            // 1. SAVE REPORT TO MYSQL
            // -----------------------------------------------------

            int reportId =
                    reportService.saveReport(
                            itemName,
                            location,
                            reportType,
                            loggedInUsername,
                            eventTime,
                            description,
                            identifyingDetails,
                            currentLocation,
                            additionalDetails,
                            selectedImagePath
                    );

            reportDialog.dispose();

            showReportSubmittedDialog(
                    reportType,
                    reportId
            );

            // -----------------------------------------------------
            // 2. AUTOMATIC SMART MATCH FOR LOST ITEMS
            // -----------------------------------------------------

            if (mode.equalsIgnoreCase("LOST")) {

                try {

                    SmartMatchService smartMatchService =
                            new SmartMatchService();

                    List<String> matches =
                            smartMatchService.findMatches(
                                    itemName,
                                    location
                            );

                    if (matches.isEmpty()) {

                        showGalaxySmartMatchDialog();

                    } else {

                        SmartMatchResultsDialog resultsDialog =
                                new SmartMatchResultsDialog(
                                        this,
                                        matches,
                                        loggedInUsername
                                );

                        resultsDialog.setLocationRelativeTo(this);
                        resultsDialog.setVisible(true);
                    }

                } catch (Exception matchException) {

                    matchException.printStackTrace();

                    JOptionPane.showMessageDialog(
                            this,
                            "Your lost report was saved successfully,\n"
                                    + "but Smart Match could not be performed right now.\n\n"
                                    + "You can use Smart Match manually later.",
                            "Smart Match",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "The report could not be saved to MySQL.\n\n"
                            + "Please check your database connection and try again.\n\n"
                            + "Error: "
                            + ex.getMessage(),
                    "Submission Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================================
    // GALAXY SMART MATCH DIALOG
    // =========================================================

    private void showGalaxySmartMatchDialog() {

        final JDialog smartMatchDialog =
                new JDialog(
                        this,
                        "Smart Match",
                        true
                );

        smartMatchDialog.setSize(560, 340);
        smartMatchDialog.setResizable(false);
        smartMatchDialog.setLocationRelativeTo(this);
        smartMatchDialog.setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BLUE,
                                1
                        ),
                        new EmptyBorder(0, 0, 0, 0)
                )
        );

        // -----------------------------------------------------
        // GALAXY ACCENT
        // -----------------------------------------------------

        JPanel accent = new JPanel();
        accent.setBackground(CYAN);
        accent.setPreferredSize(new Dimension(0, 5));
        root.add(accent, BorderLayout.NORTH);

        // -----------------------------------------------------
        // CONTENT
        // -----------------------------------------------------

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );
        content.setBorder(
                new EmptyBorder(
                        24,
                        30,
                        18,
                        30
                )
        );

        JLabel icon =
                new JLabel(
                        "✦",
                        SwingConstants.CENTER
                );

        icon.setOpaque(true);
        icon.setBackground(BLUE);
        icon.setForeground(WHITE);
        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );
        icon.setPreferredSize(new Dimension(58, 58));
        icon.setMaximumSize(new Dimension(58, 58));
        icon.setMinimumSize(new Dimension(58, 58));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title =
                new JLabel(
                        "Smart Match"
                );

        title.setForeground(WHITE);
        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message =
                new JLabel(
                        "<html><div style='text-align:center; width:450px;'>"
                                + "Your lost report has been saved successfully.<br><br>"
                                + "No possible found-item matches are available yet.<br>"
                                + "You can check Smart Match again later."
                                + "</div></html>"
                );

        message.setForeground(SECONDARY_TEXT);
        message.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status =
                new JLabel(
                        "●  Smart Match is ready to search again later"
                );

        status.setForeground(CYAN);
        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(icon);
        content.add(Box.createVerticalStrut(13));
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(message);
        content.add(Box.createVerticalStrut(15));
        content.add(status);

        root.add(
                content,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // OK BUTTON
        // -----------------------------------------------------

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                12
                        )
                );

        footer.setBackground(HEADER);

        JButton ok =
                createModernButton(
                        "✓  OK",
                        BLUE
                );

        ok.addActionListener(
                e -> smartMatchDialog.dispose()
        );

        footer.add(ok);

        root.add(
                footer,
                BorderLayout.SOUTH
        );

        smartMatchDialog.add(root);
        smartMatchDialog.setVisible(true);
    }

    // =========================================================
    // MODERN SUBMITTED DIALOG
    // =========================================================

    private void showReportSubmittedDialog(
            String reportType,
            int reportId
    ) {

        final JDialog submittedDialog =
                new JDialog(
                        this,
                        "Report Submitted",
                        true
                );

        submittedDialog.setSize(
                540,
                340
        );

        submittedDialog.setResizable(false);
        submittedDialog.setLocationRelativeTo(this);
        submittedDialog.setLayout(
                new BorderLayout()
        );

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        root.setBorder(
                BorderFactory.createLineBorder(
                        GREEN,
                        1
                )
        );

        JPanel accent =
                new JPanel();

        accent.setBackground(
                GREEN
        );

        accent.setPreferredSize(
                new Dimension(
                        0,
                        5
                )
        );

        root.add(
                accent,
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );

        JLabel icon =
                new JLabel(
                        "✓",
                        SwingConstants.CENTER
                );

        icon.setOpaque(true);
        icon.setBackground(GREEN);
        icon.setForeground(BACKGROUND);

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        icon.setPreferredSize(
                new Dimension(
                        56,
                        56
                )
        );

        icon.setMaximumSize(
                new Dimension(
                        56,
                        56
                )
        );

        icon.setMinimumSize(
                new Dimension(
                        56,
                        56
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        reportType.equals("LOST")
                                ? "Lost Report Submitted"
                                : "Found Report Submitted"
                );

        title.setForeground(
                WHITE
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message =
                new JLabel(
                        "<html><div style='text-align:center; width:430px;'>"
                                + "Your report has been successfully saved "
                                + "to the Smart Campus Lost & Found system."
                                + "</div></html>"
                );

        message.setForeground(
                SECONDARY_TEXT
        );

        message.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel idLabel =
                new JLabel(
                        "Report ID: "
                                + reportId
                );

        idLabel.setForeground(
                CYAN
        );

        idLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        idLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        content.add(icon);

        content.add(
                Box.createVerticalStrut(
                        14
                )
        );

        content.add(title);

        content.add(
                Box.createVerticalStrut(
                        9
                )
        );

        content.add(message);

        content.add(
                Box.createVerticalStrut(
                        14
                )
        );

        content.add(idLabel);

        root.add(
                content,
                BorderLayout.CENTER
        );

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                12
                        )
                );

        footer.setBackground(
                HEADER
        );

        JButton ok =
                createModernButton(
                        "✓  OK",
                        BLUE
                );

        ok.addActionListener(
                e -> submittedDialog.dispose()
        );

        footer.add(ok);

        root.add(
                footer,
                BorderLayout.SOUTH
        );

        submittedDialog.add(root);

        submittedDialog.setVisible(true);
    }

    // =========================================================
    // REPORT CARD
    // =========================================================

    private JPanel createReportCard(
            String title,
            String subtitle
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                9
                        )
                );

        card.setBackground(
                CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                13,
                                15,
                                13,
                                15
                        )
                )
        );

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
                        17
                )
        );

        JLabel sub =
                new JLabel(
                        subtitle
                );

        sub.setForeground(
                SECONDARY_TEXT
        );

        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.add(heading);

        top.add(
                Box.createVerticalStrut(2)
        );

        top.add(sub);

        card.add(
                top,
                BorderLayout.NORTH
        );

        return card;
    }

    // =========================================================
    // DETAIL
    // =========================================================

    private void addDetail(
            JPanel grid,
            String title,
            String value
    ) {

        JPanel item = new JPanel(new BorderLayout(0, 7));
        item.setBackground(CARD_2);
        item.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel label = new JLabel(title);
        label.setForeground(CYAN);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JTextArea valueArea = createReportTextArea(value, 12);
        valueArea.setRows(2);

        item.add(label, BorderLayout.NORTH);
        item.add(valueArea, BorderLayout.CENTER);
        grid.add(item);
    }

    private JTextArea createReportTextArea(
            String text,
            int fontSize
    ) {

        JTextArea area = new JTextArea();
        area.setText(text == null ? "Not provided" : text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(new Color(225, 235, 250));
        area.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        area.setBorder(null);
        area.setMargin(new Insets(0, 0, 0, 0));

        return area;
    }

    // =========================================================
    // WRAPPED LABEL
    // =========================================================

    private JLabel createWrappedLabel(
            String text,
            int width,
            int fontSize
    ) {

        JLabel label =
                new JLabel(
                        "<html><div style='width:"
                                + width
                                + "px;'>"
                                + escapeHtml(text)
                                + "</div></html>"
                );

        label.setForeground(
                LIGHT_TEXT()
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        fontSize
                )
        );

        return label;
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private String buildSmartSummary(
            String item,
            String location,
            String time,
            String description,
            String identifying
    ) {

        if (
                mode.equalsIgnoreCase("LOST")
        ) {

            return "The user reported a lost "
                    + item
                    + ". It was last seen at "
                    + location
                    + " around "
                    + time
                    + ". The item is described as "
                    + description
                    + ". Key identifying features include "
                    + identifying
                    + ".";

        }

        return "The user reported finding a "
                + item
                + " at "
                + location
                + " around "
                + time
                + ". The item is described as "
                + description
                + ". Key identifying features include "
                + identifying
                + ".";
    }

    // =========================================================
    // GET ANSWER
    // =========================================================

    private String getAnswer(
            int index
    ) {

        if (
                index >= 0
                        &&
                        index < answers.size()
        ) {

            return answers.get(index);
        }

        return "Not provided";
    }

    // =========================================================
    // ROBOT MESSAGE
    // =========================================================

    private void setRobotMessage(
            String message
    ) {

        if (mascot != null) {

            mascot.setMessage(message);

            mascot.repaint();
        }
    }

    // =========================================================
    // LIGHT TEXT
    // =========================================================

    private Color LIGHT_TEXT() {

        return new Color(
                225,
                235,
                250
        );
    }

    // =========================================================
    // ESCAPE HTML
    // =========================================================

    private String escapeHtml(
            String text
    ) {

        if (text == null) {

            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
    }

    private JButton createModernButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                color
        );

        button.setForeground(
                WHITE
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                color.brighter()
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                color
                        );
                    }
                }
        );

        return button;
    }


    private void refreshChat() {

        chatPanel.revalidate();

        chatPanel.repaint();

        SwingUtilities.invokeLater(
                () -> {

                    if (
                            chatScrollPane != null
                    ) {

                        JScrollBar bar =
                                chatScrollPane
                                        .getVerticalScrollBar();

                        bar.setValue(
                                bar.getMaximum()
                        );
                    }
                }
        );
    }


    private void addBotMessage(
            String message
    ) {

        JPanel row =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                2
                        )
                );

        row.setOpaque(false);

        JLabel icon =
                new JLabel(
                        "🤖"
                );

        icon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );

        JLabel bubble =
                createBubble(
                        message,
                        CARD
                );

        row.add(icon);

        row.add(bubble);

        chatPanel.add(
                row
        );

        chatPanel.add(
                Box.createVerticalStrut(9)
        );

        refreshChat();
    }


    private void addUserMessage(
            String message
    ) {

        JPanel row =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                2
                        )
                );

        row.setOpaque(false);

        JLabel bubble =
                createBubble(
                        message,
                        USER_BUBBLE
                );

        JLabel icon =
                new JLabel(
                        "You"
                );

        icon.setForeground(
                SECONDARY_TEXT
        );

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        row.add(bubble);

        row.add(icon);

        chatPanel.add(
                row
        );

        chatPanel.add(
                Box.createVerticalStrut(9)
        );

        refreshChat();
    }


    private JLabel createBubble(
            String message,
            Color color
    ) {

        JLabel bubble =
                new JLabel(
                        "<html><div style='width:560px;'>"
                                + escapeHtml(message)
                                + "</div></html>"
                );

        bubble.setOpaque(true);

        bubble.setBackground(
                color
        );

        bubble.setForeground(
                WHITE
        );

        bubble.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        bubble.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                11,
                                14,
                                11,
                                14
                        )
                )
        );

        return bubble;
    }

    // =========================================================
    // MAIN - TEST ONLY
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    AIAssistantFrame frame =
                            new AIAssistantFrame(
                                    "LOST"
                            );

                    frame.setVisible(true);
                }
        );
    }
}