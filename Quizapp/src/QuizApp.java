import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApp extends JFrame {

    private Question[] questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JButton[] optionButtons;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeLeft;

    private static final int TIME_PER_QUESTION = 15;

    private static final Color BG_COLOR = new Color(30, 33, 48);
    private static final Color PANEL_COLOR = new Color(41, 45, 66);
    private static final Color ACCENT_COLOR = new Color(94, 129, 244);
    private static final Color TEXT_COLOR = new Color(235, 235, 245);
    private static final Color CORRECT_COLOR = new Color(76, 175, 80);
    private static final Color WRONG_COLOR = new Color(229, 57, 53);

    public QuizApp() {
        initializeQuestions();
        setupUI();
        loadQuestion();
    }

    private void initializeQuestions() {
        questions = new Question[]{
                new Question("Which keyword is used to define a class in Java?",
                        new String[]{"class", "Class", "define", "struct"}, 0),
                new Question("Which of these is NOT a Java primitive type?",
                        new String[]{"int", "boolean", "String", "double"}, 2),
                new Question("What is the default value of a boolean in Java?",
                        new String[]{"true", "false", "0", "null"}, 1),
                new Question("Which symbol is used for single-line comments in Java?",
                        new String[]{"//", "/*", "#", "--"}, 0),
                new Question("Which method is the entry point of a Java program?",
                        new String[]{"start()", "run()", "main()", "init()"}, 2),
                new Question("Which keyword is used to inherit a class in Java?",
                        new String[]{"implements", "extends", "inherits", "super"}, 1),
                new Question("What does JVM stand for?",
                        new String[]{"Java Virtual Machine", "Java Variable Method", "Java Verified Module", "Java Visual Machine"}, 0),
                new Question("Which collection allows duplicate elements?",
                        new String[]{"Set", "Map", "List", "TreeSet"}, 2),
                new Question("Which operator is used to compare object references?",
                        new String[]{"==", "equals()", ".compare()", "==="}, 0),
                new Question("Which keyword prevents a class from being inherited?",
                        new String[]{"static", "private", "final", "sealed"}, 2),
                new Question("What is the size of an int in Java?",
                        new String[]{"8 bits", "16 bits", "32 bits", "64 bits"}, 2),
                new Question("Which of these is used for exception handling?",
                        new String[]{"try-catch", "if-else", "for-loop", "switch-case"}, 0),
                new Question("Which keyword is used to create an object in Java?",
                        new String[]{"new", "create", "object", "instance"}, 0),
                new Question("Which access modifier makes a member visible only within its own class?",
                        new String[]{"public", "protected", "private", "default"}, 2),
                new Question("Which of these is a valid way to declare an array in Java?",
                        new String[]{"int arr[] = new int[5];", "array int arr(5);", "int arr = new array(5);", "arr int[5];"}, 0)
        };
    }

    private void setupUI() {
        setTitle("Fundamentals of Programming - Quiz App");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_COLOR);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PANEL_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(PANEL_COLOR);

        progressLabel = new JLabel("Question 1 of " + questions.length);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        progressLabel.setForeground(TEXT_COLOR);
        headerRow.add(progressLabel, BorderLayout.WEST);

        timerLabel = new JLabel("⏱ 15s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        timerLabel.setForeground(ACCENT_COLOR);
        headerRow.add(timerLabel, BorderLayout.EAST);

        topPanel.add(headerRow);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        progressBar = new JProgressBar(0, questions.length);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBackground(new Color(60, 64, 88));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));
        topPanel.add(progressBar);

        add(topPanel, BorderLayout.NORTH);

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        questionLabel.setForeground(TEXT_COLOR);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(questionLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            styleButton(optionButtons[i]);

            final int index = i;
            optionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkAnswer(index);
                }
            });
            buttonPanel.add(optionButtons[i]);
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 74, 100), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }

    private void loadQuestion() {
        Question q = questions[currentQuestionIndex];

        progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.length + "   |   Score: " + score);
        progressBar.setValue(currentQuestionIndex);

        questionLabel.setText("<html><div style='text-align: center;'>" + q.getQuestionText() + "</div></html>");

        String[] options = q.getOptions();
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(labels[i] + ".  " + options[i]);
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackground(PANEL_COLOR);
        }

        startTimer();
    }

    private void startTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        timeLeft = TIME_PER_QUESTION;
        timerLabel.setText("⏱ " + timeLeft + "s");
        timerLabel.setForeground(ACCENT_COLOR);

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("⏱ " + timeLeft + "s");

                if (timeLeft <= 5) {
                    timerLabel.setForeground(WRONG_COLOR);
                }
                if (timeLeft <= 0) {
                    countdownTimer.stop();
                    checkAnswer(-1);
                }
            }
        });
        countdownTimer.start();
    }

    private void checkAnswer(int selectedIndex) {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        Question q = questions[currentQuestionIndex];
        int correctIndex = q.getCorrectAnswerIndex();

        for (JButton b : optionButtons) {
            b.setEnabled(false);
        }

        if (selectedIndex == correctIndex) {
            score++;
            optionButtons[correctIndex].setBackground(CORRECT_COLOR);
        } else {
            optionButtons[correctIndex].setBackground(CORRECT_COLOR);
            if (selectedIndex >= 0) {
                optionButtons[selectedIndex].setBackground(WRONG_COLOR);
            }
        }

        Timer delay = new Timer(700, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop();
                currentQuestionIndex++;

                if (currentQuestionIndex < questions.length) {
                    loadQuestion();
                } else {
                    showFinalScore();
                }
            }
        });
        delay.setRepeats(false);
        delay.start();
    }

    private void showFinalScore() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(BG_COLOR);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel titleLabel = new JLabel("Quiz Complete!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        double percentage = (score * 100.0) / questions.length;
        JLabel scoreLabel = new JLabel("You scored " + score + " out of " + questions.length + " (" + Math.round(percentage) + "%)");
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreLabel.setForeground(ACCENT_COLOR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 0));

        JButton restartButton = new JButton("Restart Quiz");
        restartButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        restartButton.setBackground(ACCENT_COLOR);
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartQuiz();
            }
        });

        resultPanel.add(titleLabel);
        resultPanel.add(scoreLabel);
        resultPanel.add(restartButton);

        add(resultPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void restartQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        getContentPane().removeAll();
        setLayout(new BorderLayout(0, 0));
        setupUI();
        loadQuestion();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp().setVisible(true);
            }
        });
    }
}