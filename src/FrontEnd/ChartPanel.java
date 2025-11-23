/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package FrontEnd;

import BackEnd.Course;
import Controller.LoginController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author abramehab
 */
public class ChartPanel extends javax.swing.JPanel {
    private Course currentcourse;
    private LoginController controller;
    private double completionRate;
    private double avgQuizScore;
    private int enrolledStudents;
    /**
     * Creates new form ChartPanel
     */
   public ChartPanel(Course course, LoginController controller) {
        this.currentcourse = course;
        this.controller = controller;
        loadData();
        initComponents();
        setupUI();
   }
   
     private void loadData() {
        try {
            completionRate = controller.getCourseCompletionPercentage(currentcourse);
            avgQuizScore = controller.getCourseAverageQuizScore(currentcourse);
            enrolledStudents = controller.getEnrolledStudentCount(currentcourse);
        } catch (Exception e) {
            completionRate = 0;
            avgQuizScore = 0;
            enrolledStudents = 0;
        }
    }
     
      private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
        JLabel title = new JLabel("Analytics for: " + currentcourse.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        
        add(Box.createRigidArea(new Dimension(0, 20)));
        
         JLabel studentsCountLabel = new JLabel("Total Enrolled Students: " + enrolledStudents);
            studentsCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
            studentsCountLabel.setForeground(new Color(46, 139, 87)); // Same green as students bar
            studentsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(studentsCountLabel);
            
        add(Box.createRigidArea(new Dimension(0, 10)));
       
        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph((Graphics2D) g);
            }
        };
        graphPanel.setPreferredSize(new Dimension(500, 400));
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(graphPanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // Legend
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        legendPanel.add(createLegendItem("Completion Rate", new Color(70, 130, 180)));
        legendPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        legendPanel.add(createLegendItem("Avg Quiz Score", new Color(147, 112, 219)));
        legendPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        legendPanel.add(createLegendItem("Enrolled Students", new Color(46, 139, 87)));

        add(legendPanel);
    }

    private void drawBarGraph(Graphics2D g2d) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();
    int barWidth = 60;
    int spacing = 50;
    int startX = 80;
    int baseY = height - 80; 
    int maxBarHeight = height - 140;

    
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
    
   
    g2d.drawLine(50, 40, 50, baseY); 
    
   
    for (int i = 0; i <= 100; i += 20) {
        int y = baseY - (i * maxBarHeight / 100);
        g2d.setColor(Color.GRAY);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(50, y, width - 50, y);
        g2d.setColor(Color.BLACK);
        g2d.drawString(i + "%", 20, y + 4);
    }

    
    g2d.setColor(Color.BLACK);
    g2d.drawLine(50, baseY, width - 50, baseY);

   
    int completionHeight = (int) (completionRate / 100.0 * maxBarHeight);
    int scoreHeight = (int) (avgQuizScore / 100.0 * maxBarHeight);
    
   
    int maxStudentsForDisplay = Math.max(20, enrolledStudents);
    int studentsHeight = (int) ((double) enrolledStudents / maxStudentsForDisplay * maxBarHeight);

  
    drawBar(g2d, startX, baseY - completionHeight, barWidth, completionHeight, 
            new Color(70, 130, 180), "Completion", String.format("%.1f%%", completionRate));

    drawBar(g2d, startX + barWidth + spacing, baseY - scoreHeight, barWidth, scoreHeight, 
            new Color(147, 112, 219), "Avg Score", String.format("%.1f", avgQuizScore));

    drawBar(g2d, startX + 2 * (barWidth + spacing), baseY - studentsHeight, barWidth, studentsHeight, 
            new Color(46, 139, 87), "Students", String.valueOf(enrolledStudents));

   
    if (enrolledStudents > 20) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 10));
        g2d.drawString("Student scale: 0-" + maxStudentsForDisplay, width - 150, baseY + 20);
    }
}
    private void drawBar(Graphics2D g2d, int x, int y, int width, int height, Color color, String label, String value) {
    
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
        
      
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);

      
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        int labelWidth = g2d.getFontMetrics().stringWidth(label);
        g2d.drawString(label, x + (width - labelWidth) / 2, getHeight() - 30);

     
        if (height > 20) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            int valueWidth = g2d.getFontMetrics().stringWidth(value);
            g2d.drawString(value, x + (width - valueWidth) / 2, y - 5);
        }
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel legendItem = new JPanel();
        legendItem.setLayout(new BoxLayout(legendItem, BoxLayout.X_AXIS));
        legendItem.setBackground(Color.WHITE);
        legendItem.setOpaque(false);

        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

      
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));

        legendItem.add(colorBox);
        legendItem.add(Box.createRigidArea(new Dimension(5, 0)));
        legendItem.add(label);

        return legendItem;
    }
    
    public static void showAnalyticsDialog(JFrame parent, Course course, LoginController controller) {
        JDialog dialog = new JDialog(parent, "Course Analytics", true);
        ChartPanel panel = new ChartPanel(course, controller);
        
        dialog.setContentPane(panel);
        dialog.setPreferredSize(new Dimension(500, 500));
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
