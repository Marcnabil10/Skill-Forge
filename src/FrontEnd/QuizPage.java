/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FrontEnd;
import Controller.LoginController;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
/**
 *
 * @author Æ® ê¹ Æ Æ´
 */
public class QuizPage extends javax.swing.JFrame {
    private LoginController controller;
    private int currentQuestionIndex = 0;
    private Map<Integer, Integer> studentAnswers = new HashMap<>();
    private ButtonGroup currentGroup;
    private ViewLessons dashboard;
     private Results resultPanel;

    public QuizPage(LoginController controller , ViewLessons dashboard) {
        initComponents();
        System.out.println("=== DEBUG: QuizPage Constructor ===");
        System.out.println("Controller: " + controller);
        System.out.println("Dashboard: " + dashboard);
        
        this.controller = controller;
        this.dashboard = dashboard;
        this.currentGroup = new ButtonGroup();
        this.resultPanel = new Results(controller, this, dashboard);
        resultPanel.setVisible(false);
        System.out.println("Current Lesson: " + controller.getCurrentLesson());
        System.out.println("Current Course: " + controller.getCurrentCourse());
        
        if (controller.getCurrentLesson() != null) {
            System.out.println("Lesson Quiz: " + controller.getCurrentLesson().getQuiz());
            if (controller.getCurrentLesson().getQuiz() != null) {
                System.out.println("Quiz Questions: " + controller.getCurrentLesson().getQuiz().getQuestions().size());
            }
        }

        int total = controller.getQuizQuestionCount();
        System.out.println("Total questions from controller: " + total);
        
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "Error: No questions found.");
            dispose();
            return;
        }
        this.currentGroup = new javax.swing.ButtonGroup();
        lblLesson.setText(controller.getCurrentLessonTitle());
        lblnum.setText(String.valueOf(total));
        this.setSize(600, 600);
        
        loadQuestion(0);
    }
    /**
     * Creates new form QuizPage
     */
    public QuizPage() {
        initComponents();
        System.out.println("=== DEBUG: EMPTY CONSTRUCTOR CALLED ===");
        System.out.println("THIS IS THE PROBLEM! Use the constructor with parameters!");
    }
    private void loadQuestion(int index) {
    try {
        System.out.println("=== DEBUG loadQuestion START ===");
        System.out.println("Current index: " + index);
        
        int total = controller.getQuizQuestionCount();
        System.out.println("Total questions: " + total);
        
        java.util.List<String> options = controller.getQuestionOptions(index);
        System.out.println("Options count: " + options.size());
        System.out.println("Question text: " + controller.getQuestionText(index));
        
        lblQCounter.setText("Question " + (index + 1) + " of " + total);
        lblQText.setText("<html>" + controller.getQuestionText(index) + "</html>"); 
        
        System.out.println("Before removing old components...");
        pnlOptions.removeAll();
        pnlOptions.setLayout(new javax.swing.BoxLayout(pnlOptions, javax.swing.BoxLayout.Y_AXIS));
        currentGroup = new javax.swing.ButtonGroup();
        
        System.out.println("Creating radio buttons...");
        for (int i = 0; i < options.size(); i++) {
            System.out.println("Creating option " + i + ": " + options.get(i));
            javax.swing.JRadioButton rb = new javax.swing.JRadioButton(options.get(i));
            rb.setFont(new java.awt.Font("Segoe UI", 0, 14));
            rb.setActionCommand(String.valueOf(i)); 
            
            rb.setForeground(java.awt.Color.BLACK);
            rb.setOpaque(true);
            rb.setBackground(new java.awt.Color(240, 240, 240));

            if (studentAnswers.getOrDefault(index, -1) == i) {
                rb.setSelected(true);
            }
            
            currentGroup.add(rb); 
            pnlOptions.add(rb);   
            System.out.println("Option " + i + " added to panel");
        }
        
        if (index == total - 1) {
            btnNext.setText("Finish");
        } else {
            btnNext.setText("Next");
        }
        
        System.out.println("Before revalidating panels...");
        pnlOptions.revalidate();
        pnlOptions.repaint();
        pnlQuestionArea.revalidate();
        pnlQuestionArea.repaint();
        
        System.out.println("Components in pnlOptions: " + pnlOptions.getComponentCount());
        System.out.println("=== DEBUG loadQuestion END ===\n");
        
    } catch (Exception e) {
        System.out.println("ERROR in loadQuestion: " + e.getMessage());
        e.printStackTrace();
    }
}
    private void saveAnswer() {
    if (currentGroup == null || currentGroup.getSelection() == null) {
        return;
    }
    
    int ans = Integer.parseInt(currentGroup.getSelection().getActionCommand());
    studentAnswers.put(currentQuestionIndex, ans);
}
   private void submit() {
        java.util.List<Integer> answers = new java.util.ArrayList<>();
        int total = controller.getQuizQuestionCount();

        for (int i = 0; i < total; i++) {
            answers.add(studentAnswers.getOrDefault(i, -1));
        }

        String resultMsg = controller.submitCurrentQuiz(answers);
        boolean passed = resultMsg.contains("PASSED");

        // SWITCH SCREENS
        this.getContentPane().removeAll();
        this.setLayout(new java.awt.BorderLayout());

       this.add(resultPanel);
        resultPanel.setVisible(true);

        resultPanel.showResult(resultMsg, passed, answers);

        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblLesson = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblnum = new javax.swing.JLabel();
        pnlQuestionArea = new javax.swing.JPanel();
        lblQCounter = new javax.swing.JLabel();
        lblQText = new javax.swing.JLabel();
        pnlOptions = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Quiz page");

        jLabel2.setText("Lesson :");

        lblLesson.setText("...");

        jLabel4.setText("Numer of questions :");

        lblnum.setText("...");

        lblQCounter.setText("Question 1 of 5 :");

        lblQText.setText("....");

        // FIXED THIS LINE - Changed from LINE_AXIS to Y_AXIS
        pnlOptions.setLayout(new javax.swing.BoxLayout(pnlOptions, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlQuestionAreaLayout = new javax.swing.GroupLayout(pnlQuestionArea);
        pnlQuestionArea.setLayout(pnlQuestionAreaLayout);
        pnlQuestionAreaLayout.setHorizontalGroup(
            pnlQuestionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuestionAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlQuestionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblQCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQText, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlQuestionAreaLayout.setVerticalGroup(
            pnlQuestionAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQuestionAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQCounter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblQText)
                .addGap(18, 18, 18)
                .addComponent(pnlOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(228, Short.MAX_VALUE))
        );

        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLesson, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblnum, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(pnlQuestionArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(btnNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(100, 100, 100))
            .addGroup(layout.createSequentialGroup()
                .addGap(233, 233, 233)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblLesson))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblnum))
                .addGap(18, 18, 18)
                .addComponent(pnlQuestionArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE) // CHANGED FROM 36 to 50
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(btnNext))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
         
        if (currentGroup == null) {
            return;
        }

        // VALIDATION: Must select an answer
        if (currentGroup.getSelection() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }

        saveAnswer();

        int total = controller.getQuizQuestionCount();
        if (currentQuestionIndex < total - 1) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Finish Quiz?", "Submit", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) submit();
        }

    }//GEN-LAST:event_btnNextActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

        if (dashboard != null) {
            dashboard.setVisible(true);
        }
            dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuizPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuizPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuizPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuizPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuizPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnNext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblLesson;
    private javax.swing.JLabel lblQCounter;
    private javax.swing.JLabel lblQText;
    private javax.swing.JLabel lblnum;
    private javax.swing.JPanel pnlOptions;
    private javax.swing.JPanel pnlQuestionArea;
    // End of variables declaration//GEN-END:variables
}
