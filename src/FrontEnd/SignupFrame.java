package FrontEnd;

import BackEnd.*;
import Controller.LoginController;
import javax.swing.JOptionPane;

public class SignupFrame extends javax.swing.JFrame {

    private LoginController controller;

    /**
     * Creates new form SignupFrame
     */
    public SignupFrame(LoginController controller) {
        initComponents();
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        signup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24));
        jLabel1.setForeground(new java.awt.Color(102, 0, 0));
        jLabel1.setText("create your account");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 14));
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14));
        jLabel3.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 14));
        jLabel4.setText("Password:");

        jComboBox1.setFont(new java.awt.Font("Helvetica Neue", 0, 14));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"role", "student", "instructor", "Admin"}));

        signup.setForeground(new java.awt.Color(51, 204, 0));
        signup.setText("Signup");
        signup.addActionListener(evt -> signupActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(jLabel1))
                                        .addComponent(signup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(signup)
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }

    private void signupActionPerformed() {
        String username = jTextField1.getText().trim();
        String email = jTextField2.getText().trim();
        String password = jTextField3.getText().trim();
        String role = jComboBox1.getSelectedItem().toString();

        if ("role".equals(role)) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            return;
        }

        if (!ValidationService.isUsernameValid(username)) {
            JOptionPane.showMessageDialog(this, "Invalid username.");
            return;
        }

        if (!ValidationService.isEmailValid(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email.");
            return;
        }

        if (!ValidationService.isPasswordValid(password)) {
            JOptionPane.showMessageDialog(this, "Password too short.");
            return;
        }

        if (controller.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already taken.");
            return;
        }

        if (controller.getUserByEmail(email) != null) {
            JOptionPane.showMessageDialog(this, "Email already registered.");
            return;
        }

        String id = controller.generateUniqueId();
        String hashed = PasswordHasher.hashPassword(password);
        User newUser;

        if ("student".equalsIgnoreCase(role)) {
            newUser = new Student(id, username, email, hashed, "student");
        } else if("instructor".equalsIgnoreCase(role)){
            newUser = new Instructor(id, username, email, hashed, "instructor");
        }
        else{
            newUser = new Admin(id, username, email, hashed, "admin");
        }

        controller.saveUser(newUser);

        JOptionPane.showMessageDialog(this, "Signup successful!");
        new LoginFrame(controller).setVisible(true);
        this.dispose();
    }


    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private javax.swing.JTextField jTextField1, jTextField2, jTextField3;
    private javax.swing.JButton signup;
}
