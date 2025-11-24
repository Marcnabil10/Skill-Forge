package FrontEnd;

import BackEnd.Certificate;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CertificateDetails extends javax.swing.JPanel {
    private Certificate cert;
    private ViewCertificate dash;

    public CertificateDetails(Certificate cert, ViewCertificate dash) {
        this.cert = cert;
        this.dash = dash;
        initComponents();

        lblId.setText("Certificate ID: " + cert.getCertificateId());
        lblCourse.setText("Course: " + cert.getCourseName());
        lblDate.setText("Issue Date: " + cert.getIssueDate());
        lblStudent.setText("Student Name: " + cert.getStudent());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblCourse = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblStudent = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        lblId.setFont(new java.awt.Font("Helvetica Neue", 0, 16));
        lblId.setText("Certificate ID:");

        lblCourse.setFont(new java.awt.Font("Helvetica Neue", 0, 16));
        lblCourse.setText("Course Name:");

        lblDate.setFont(new java.awt.Font("Helvetica Neue", 0, 16));
        lblDate.setText("Issue Date:");

        lblStudent.setFont(new java.awt.Font("Helvetica Neue", 0, 16));
        lblStudent.setText("Student Name: ");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Certificate");

        jButton1.setText("Download as Json");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setText("Back");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        jButton3.setText("Download as PDF");
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));

        // Layout code (unchanged)
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblStudent))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblDate))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblCourse))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblId))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(232, 232, 232)
                                                .addComponent(jLabel1)))
                                .addContainerGap(234, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1)
                                .addGap(128, 128, 128)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(66, 66, 66)
                                .addComponent(lblId)
                                .addGap(47, 47, 47)
                                .addComponent(lblCourse)
                                .addGap(41, 41, 41)
                                .addComponent(lblDate)
                                .addGap(43, 43, 43)
                                .addComponent(lblStudent)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addGap(16, 16, 16))
        );
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Certificate as JSON");

        String filename = "Certificate_" + cert.getCourseName().replaceAll(" ", "_") + ".json";
        fileChooser.setSelectedFile(new File(filename));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".json")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
            }
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(cert.toJSON().toString(4));
                JOptionPane.showMessageDialog(this, "Certificate saved as JSON successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
            }
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        SwingUtilities.getWindowAncestor(this).dispose();
        dash.setVisible(true);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Certificate as PDF");

        String filename = "Certificate_" + cert.getCourseName() + ".pdf";
        fileChooser.setSelectedFile(new File(filename));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            try {
                generatePDF(fileToSave);
                JOptionPane.showMessageDialog(this, "Certificate saved as PDF successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void generatePDF(File file) throws Exception {
        String pdfContent =
                "Certificate Details:\n" +
                        "ID: " + cert.getCertificateId() + "\n" +
                        "Course: " + cert.getCourseName() + "\n" +
                        "Date: " + cert.getIssueDate() + "\n" +
                        "Student: " + cert.getStudent();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(pdfContent);
        }
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblStudent;
}
