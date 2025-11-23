package FrontEnd;

import BackEnd.Quiz;
import BackEnd.Question;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizPage extends javax.swing.JPanel {
    private Quiz quiz;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<String> userAnswers;
    private StudentDashboard dashboard;

    public QuizPage(Quiz quiz, StudentDashboard dashboard) {
        this.quiz = quiz;
        this.dashboard = dashboard;
        this.questions = quiz.getQuestions();
        this.userAnswers = new ArrayList<>();
        
        initComponents();
        showQuestion();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(quiz.getQuizTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Question Panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionText = new JTextArea();
        questionText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setBackground(new Color(250, 250, 250));
        questionText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(questionText);
        scrollPane.setPreferredSize(new Dimension(600, 100));

        // Options Panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Create radio buttons for options
        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();

        option1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        option2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        option3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        option4.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(option1);
        optionGroup.add(option2);
        optionGroup.add(option3);
        optionGroup.add(option4);

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);

        // Navigation Panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Quiz");

        prevButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        prevButton.setBackground(new Color(70, 130, 180));
        nextButton.setBackground(new Color(34, 139, 34));
        submitButton.setBackground(new Color(220, 20, 60));

        prevButton.setForeground(Color.WHITE);
        nextButton.setForeground(Color.WHITE);
        submitButton.setForeground(Color.WHITE);

        prevButton.setPreferredSize(new Dimension(120, 35));
        nextButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setPreferredSize(new Dimension(150, 35));

        // Progress Label
        progressLabel = new JLabel("", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        navPanel.add(prevButton);
        navPanel.add(progressLabel);
        navPanel.add(nextButton);
        navPanel.add(submitButton);

        // Add action listeners
        prevButton.addActionListener(e -> previousQuestion());
        nextButton.addActionListener(e -> nextQuestion());
        submitButton.addActionListener(e -> submitQuiz());

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.SOUTH);
        add(navPanel, BorderLayout.SOUTH);

        // Initialize user answers list
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add("");
        }
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            
            // Update question text
            questionText.setText((currentQuestionIndex + 1) + ". " + currentQuestion.getQuestionText());
            
            // Update options
            List<String> options = currentQuestion.getOptions();
            option1.setText("A. " + options.get(0));
            option2.setText("B. " + options.get(1));
            option3.setText("C. " + options.get(2));
            option4.setText("D. " + options.get(3));
            
            // Clear selection
            option1.setSelected(false);
            option2.setSelected(false);
            option3.setSelected(false);
            option4.setSelected(false);
            
            // Restore previous answer if exists
            String previousAnswer = userAnswers.get(currentQuestionIndex);
            if (!previousAnswer.isEmpty()) {
                switch (previousAnswer) {
                    case "A": option1.setSelected(true); break;
                    case "B": option2.setSelected(true); break;
                    case "C": option3.setSelected(true); break;
                    case "D": option4.setSelected(true); break;
                }
            }
            
            // Update progress
            progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
            
            // Update button states
            prevButton.setEnabled(currentQuestionIndex > 0);
            nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
            submitButton.setEnabled(currentQuestionIndex == questions.size() - 1);
        }
    }

    private void saveAnswer() {
        if (option1.isSelected()) {
            userAnswers.set(currentQuestionIndex, "A");
        } else if (option2.isSelected()) {
            userAnswers.set(currentQuestionIndex, "B");
        } else if (option3.isSelected()) {
            userAnswers.set(currentQuestionIndex, "C");
        } else if (option4.isSelected()) {
            userAnswers.set(currentQuestionIndex, "D");
        }
    }

    private void previousQuestion() {
        saveAnswer();
        currentQuestionIndex--;
        showQuestion();
    }

    private void nextQuestion() {
        saveAnswer();
        currentQuestionIndex++;
        showQuestion();
    }

    private void submitQuiz() {
        saveAnswer();
        
        // Calculate score
        score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(i);
            String correctAnswer = question.getCorrectAnswer();
            
            if (userAnswer.equals(correctAnswer)) {
                score++;
            }
        }
        
        // Calculate percentage
        double percentage = (double) score / questions.size() * 100;
        
        // Show result
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Result resultPage = new Result(quiz, score, questions.size(), percentage, dashboard);
        
        // Replace current panel with result
        Container container = getParent();
        container.removeAll();
        container.add(resultPage);
        container.revalidate();
        container.repaint();
    }

    // UI Components
    private JTextArea questionText;
    private JPanel optionsPanel;
    private JRadioButton option1, option2, option3, option4;
    private JButton prevButton, nextButton, submitButton;
    private JLabel progressLabel;
}
