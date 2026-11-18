package ui;

import dao.UserDAO;
import model.User;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Student Registration / Sign Up Dialog allowing new hostel residents to create an account.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class RegisterDialog extends JDialog {

    private JTextField txtFullName;
    private JTextField txtRollNumber;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtContact;
    private JComboBox<String> cmbHostelBlock;
    private JTextField txtRoomNumber;

    private final UserDAO userDAO = new UserDAO();
    private String registeredUsername = null;

    public RegisterDialog(JFrame parent) {
        super(parent, "Student Registration / Sign Up", true);
        initUI();
    }

    private void initUI() {
        setSize(520, 620);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel header = UIConstants.createHeaderBanner(
                "Student Registration",
                "Create a new student account to access hostel mess services",
                null
        );
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BG_CARD);
        formPanel.setBorder(new EmptyBorder(15, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 6, 5, 6);

        txtFullName = UIConstants.createTextField(15);
        txtRollNumber = UIConstants.createTextField(15);
        txtUsername = UIConstants.createTextField(15);
        txtPassword = UIConstants.createPasswordField(15);
        txtConfirmPassword = UIConstants.createPasswordField(15);
        txtEmail = UIConstants.createTextField(15);
        txtContact = UIConstants.createTextField(15);
        cmbHostelBlock = new JComboBox<>(new String[]{"Block-A", "Block-B", "Block-C", "Block-D", "Block-G"});
        cmbHostelBlock.setFont(UIConstants.FONT_REGULAR);
        txtRoomNumber = UIConstants.createTextField(15);
        txtRoomNumber.setText("101");

        int r = 0;
        addFormRow(formPanel, gbc, r++, "Full Name *:", txtFullName);
        addFormRow(formPanel, gbc, r++, "Roll Number *:", txtRollNumber);
        addFormRow(formPanel, gbc, r++, "Username *:", txtUsername);
        addFormRow(formPanel, gbc, r++, "Password *:", txtPassword);
        addFormRow(formPanel, gbc, r++, "Confirm Password *:", txtConfirmPassword);
        addFormRow(formPanel, gbc, r++, "Email Address:", txtEmail);
        addFormRow(formPanel, gbc, r++, "Contact Number:", txtContact);
        addFormRow(formPanel, gbc, r++, "Hostel Block:", cmbHostelBlock);
        addFormRow(formPanel, gbc, r++, "Room Number:", txtRoomNumber);

        add(formPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        actionPanel.setBackground(UIConstants.BG_MAIN);
        actionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR));

        JButton btnRegister = UIConstants.createPrimaryButton("Create Account");
        JButton btnCancel = UIConstants.createLightButton("Cancel");

        btnRegister.addActionListener(e -> handleRegistration());
        btnCancel.addActionListener(e -> dispose());

        actionPanel.add(btnRegister);
        actionPanel.add(btnCancel);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_HEADER);
        lbl.setForeground(UIConstants.TEXT_MAIN);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }

    private void handleRegistration() {
        String fullName = txtFullName.getText().trim();
        String roll = txtRollNumber.getText().trim();
        String username = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String block = (String) cmbHostelBlock.getSelectedItem();
        String room = txtRoomNumber.getText().trim();

        if (!ValidationUtils.isNotEmpty(fullName)) {
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(roll)) {
            JOptionPane.showMessageDialog(this, "Please enter your College Roll Number.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtRollNumber.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(username)) {
            JOptionPane.showMessageDialog(this, "Please choose a Username.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(pass)) {
            JOptionPane.showMessageDialog(this, "Please enter a Password.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtConfirmPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address format.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPhone(contact)) {
            JOptionPane.showMessageDialog(this, "Contact number must be 10-12 digits.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtContact.requestFocus();
            return;
        }

        try {
            if (userDAO.isUsernameTaken(username, 0)) {
                JOptionPane.showMessageDialog(this, "Username '" + username + "' is already registered. Please choose another.", "Duplicate Username", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }

            if (userDAO.isRollNumberTaken(roll, 0)) {
                JOptionPane.showMessageDialog(this, "Roll number '" + roll + "' is already registered.", "Duplicate Roll Number", JOptionPane.WARNING_MESSAGE);
                txtRollNumber.requestFocus();
                return;
            }

            User newStudent = new User(username, pass, fullName, roll, "STUDENT", email, contact, block, room);
            boolean ok = userDAO.addUser(newStudent);
            if (ok) {
                registeredUsername = username;
                JOptionPane.showMessageDialog(this,
                        "Student Account registered successfully!\nYou can now log in with your credentials.",
                        "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not register account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }
}
