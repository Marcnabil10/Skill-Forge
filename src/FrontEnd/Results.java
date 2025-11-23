package FrontEnd;

import BackEnd.Quiz;
import BackEnd.Certificate;
import BackEnd.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Result extends javax.swing.JPanel {
    private Quiz quiz;
    private int score;
    private int totalQuestions;
    private double percentage;
    private StudentDashboard dashboard;
    private DatabaseManager dbManager;

    public Result(Quiz quiz, int score, int totalQuestions, double percentage, StudentDashboard dashboard) {
        this.quiz = quiz;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.dashboard = dashboard;
        this.dbManager = new DatabaseManager();
        
        initComponents();
        displayResult();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(800, 600));

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Result Icon
        JLabel iconLabel = new JLabel();
        if (percentage >= 70) {
            iconLabel.setIcon(new ImageIcon("success_icon.png")); // Use your own icon
            iconLabel.setText("🎉"); // Fallback emoji
        } else {
            iconLabel.setIcon(new ImageIcon("fail_icon.png")); // Use your own icon
            iconLabel.setText("😞"); // Fallback emoji
        }
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Result Title
        JLabel titleLabel = new JLabel("Quiz Completed!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score Display
        JLabel scoreLabel = new JLabel(score + " / " + totalQuestions);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        scoreLabel.setForeground(new Color(34, 139, 34));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Percentage
        JLabel percentageLabel = new JLabel(String.format("%.1f%%", percentage));
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        percentageLabel.setForeground(new Color(70, 130, 180));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pass/Fail Message
        JLabel messageLabel = new JLabel();
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (percentage >= 70) {
            messageLabel.setText("Congratulations! You passed the quiz!");
            messageLabel.setForeground(new Color(34, 139, 34));
        } else {
            messageLabel.setText("Keep practicing! You can do better next time.");
            messageLabel.setForeground(new Color(220, 20, 60));
        }

        // Quiz Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        infoPanel.setBackground(Color.WHITE);

        JLabel quizNameLabel = new JLabel("Quiz: " + quiz.getQuizTitle());
        quizNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        quizNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel courseLabel = new JLabel("Course: " + quiz.getCourseName());
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        courseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(quizNameLabel);
        infoPanel.add(courseLabel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton retryButton = new JButton("Retry Quiz");
        JButton certificateButton = new JButton("Get Certificate");
        JButton dashboardButton = new JButton("Back to Dashboard");

        // Style buttons
        styleButton(retryButton, new Color(70, 130, 180));
        styleButton(certificateButton, new Color(34, 139, 34));
        styleButton(dashboardButton, new Color(128, 128, 128));

        // Add action listeners
        retryButton.addActionListener(e -> retryQuiz());
        certificateButton.addActionListener(e -> generateCertificate());
        dashboardButton.addActionListener(e -> backToDashboard());

        // Only show certificate button if passed
        if (percentage >= 70) {
            buttonPanel.add(certificateButton);
        }
        buttonPanel.add(retryButton);
        buttonPanel.add(dashboardButton);

        // Add components to main panel
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(scoreLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(percentageLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(buttonPanel);

        // Add main panel to center
        add(mainPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(180, 45));
    }

    private void displayResult() {
        // Save result to database
        dbManager.saveQuizResult(quiz.getQuizId(), dashboard.getCurrentStudent().getStudentId(), score, percentage);
        
        // Update student progress
        if (percentage >= 70) {
            dbManager.updateCourseProgress(dashboard.getCurrentStudent().getStudentId(), quiz.getCourseName());
        }
    }

    private void retryQuiz() {
        // Create new quiz instance and restart
        QuizPage quizPage = new QuizPage(quiz, dashboard);
        
        Container container = getParent();
        container.removeAll();
        container.add(quizPage);
        container.revalidate();
        container.repaint();
    }

    private void generateCertificate() {
        if (percentage >= 70) {
            try {
                // Generate certificate
                String studentName = dashboard.getCurrentStudent().getName();
                String courseName = quiz.getCourseName();
                String certificateId = "CERT-" + System.currentTimeMillis();
                String issueDate = LocalDate.now().toString();
                
                Certificate certificate = new Certificate(certificateId, studentName, courseName, issueDate, percentage);
                
                // Save certificate to database
                dbManager.saveCertificate(certificate);
                
                // Show certificate
                ViewCertificate certificateView = new ViewCertificate(dashboard.getCurrentStudent());
                JFrame frame = new JFrame("Certificate");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(certificateView);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                JOptionPane.showMessageDialog(this, 
                    "Certificate generated successfully!\nYou can view it in your dashboard.",
                    "Certificate Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error generating certificate: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "You need to score 70% or higher to get a certificate.",
                "Certificate Not Available",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void backToDashboard() {
        // Return to student dashboard
        Container container = getParent();
        if (container != null) {
            container.removeAll();
            container.add(dashboard);
            container.revalidate();
            container.repaint();
        }
        
        // Close the current window if it's a separate frame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();
        }
    }

    // Getters for result information
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getPercentage() { return percentage; }
    public boolean isPassed() { return percentage >= 70; }
}
