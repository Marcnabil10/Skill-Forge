package FrontEnd;

import BackEnd.Course;
import BackEnd.InstructorService;
import BackEnd.Lesson;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CourseEditorFrame extends javax.swing.JFrame {

    private InstructorService instructorService;
    private Course course; 
    private InstructorDashboard dashboard;

 
    private javax.swing.JLabel lblTitle;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnBack;

    
    private JTable lessonTable;
    private JScrollPane scrollPane;

    public CourseEditorFrame(InstructorService instructorService, Course course) {
        this.instructorService = instructorService;
        this.course = course;

        initComponents(); 
        setLocationRelativeTo(null);
        setTitle("Manage Lessons for: " + course.getTitle());
        lblTitle.setText("Course: " + course.getTitle());

        refreshCourseAndDisplay();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent we) {
                btnBackActionPerformed(null);
            }
        });
    }

    public void setDashboard(InstructorDashboard dashboard) {
        this.dashboard = dashboard;
    }

    private void refreshCourseAndDisplay() {
        
        try {
            List<Course> myCourses = instructorService.getMyCourses(course.getInstructorId());
            Course updated = myCourses.stream()
                .filter(c -> c.getCourseId().equals(course.getCourseId()))
                .findFirst().orElse(null);
            if (updated != null) {
                this.course = updated;
            }
        } catch (Exception ex) {
            
        }

        DefaultTableModel model = (DefaultTableModel) lessonTable.getModel();
        model.setRowCount(0); 

        List<Lesson> lessons = course.getLessons();
        if (lessons != null) {
            for (Lesson l : lessons) {
                String contentPreview = l.getContent() == null ? "" : l.getContent();
                if (contentPreview.length() > 50) {
                    contentPreview = contentPreview.substring(0, 50) + "...";
                }
                
           
                model.addRow(new Object[]{
                    l.getLessonId(), 
                    l.getTitle(), 
                    contentPreview
                });
            }
        }
    }
    

    private Lesson getSelectedLessonFromTable() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return null;
        }
       
        String lessonId = (String) lessonTable.getValueAt(selectedRow, 0);
      
        for (Lesson l : course.getLessons()) {
            if (l.getLessonId().equals(lessonId)) return l;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton("Add Lesson (By ID)");
        btnEdit = new javax.swing.JButton("Edit Selected Lesson");
        btnDelete = new javax.swing.JButton("Delete Selected Lesson");
        btnBack = new javax.swing.JButton("Back");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lessonTable = new JTable();
        lessonTable.setModel(new DefaultTableModel(
            new Object [][] {}, 
            new String [] {"ID", "Title", "Preview"} 
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        lessonTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(lessonTable);
        btnAdd.addActionListener(evt -> btnAddActionPerformed(evt));
        btnEdit.addActionListener(evt -> btnEditActionPerformed(evt));
        btnDelete.addActionListener(evt -> btnDeleteActionPerformed(evt));
        btnBack.addActionListener(evt -> btnBackActionPerformed(evt));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        String lessonId = JOptionPane.showInputDialog(this, "Enter NEW Lesson ID:"); 
        if (lessonId == null || lessonId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lesson ID is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String title = JOptionPane.showInputDialog(this, "Enter lesson title:");
        if (title == null || title.trim().isEmpty()) return;

        String content = JOptionPane.showInputDialog(this, "Enter lesson content:");
        if (content == null) return; 

        boolean ok = instructorService.addLesson(course.getCourseId(), title, content);
        
        if (ok) {
            JOptionPane.showMessageDialog(this, "Lesson added! (ID: " + lessonId + ")");
            refreshCourseAndDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Lesson was not added.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        Lesson selected = getSelectedLessonFromTable(); 
        if (selected == null) return;
        String newTitle = JOptionPane.showInputDialog(this, "New title:", selected.getTitle());
        if (newTitle == null || newTitle.trim().isEmpty()) return;

        String newContent = JOptionPane.showInputDialog(this, "New content:", selected.getContent());
        if (newContent == null) return;
        boolean ok = instructorService.editLesson(course.getCourseId(), selected.getLessonId(), newTitle, newContent);
        
        if (ok) {
            JOptionPane.showMessageDialog(this, "Lesson updated!");
            refreshCourseAndDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating lesson.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        Lesson selected = getSelectedLessonFromTable();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete lesson '" + selected.getTitle() + "' (ID: " + selected.getLessonId() + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = instructorService.deleteLesson(course.getCourseId(), selected.getLessonId());
        
        if (ok) {
            JOptionPane.showMessageDialog(this, "Lesson deleted!");
            refreshCourseAndDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting lesson. Check ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        if (dashboard != null) dashboard.setVisible(true);
    }
    /**
     * @param args the command line arguments
     */
   /**
     * @param args the command line arguments
     */
    // File: lab7-main/src/FrontEnd/CourseEditorFrame.java

// ... (around line 273)
   /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        // ... (look and feel boilerplate)
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              
                BackEnd.Course testCourse = new BackEnd.Course(
                    "C101", 
                    "Sample Dev Course", 
                    "Test Description", 
                    "I123"
                );
                
               
                BackEnd.JsonDatabaseManager db = new BackEnd.JsonDatabaseManager("users.json", "courses.json");
                BackEnd.InstructorService dummyService = new BackEnd.InstructorService(db); 
               
                
                new CourseEditorFrame(dummyService, testCourse).setVisible(true);
            }
        });
    }
}
