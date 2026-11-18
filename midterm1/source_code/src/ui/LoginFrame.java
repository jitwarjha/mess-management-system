package ui;

import dao.UserDAO;
import model.User;
import utils.SessionManager;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Modern login interface supporting Admin and Student role authentication.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        super("Mess Menu & Meal Preference Management System - Login");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 560);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Left Branding / Info Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(UIConstants.PRIMARY);
        leftPanel.setPreferredSize(new Dimension(360, 560));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(35, 30, 35, 30));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));

        JLabel lblAppTitle = new JLabel("<html><b style='font-size:18px;'>MESS MENU &amp;<br>MEAL PREFERENCE</b><br><span style='font-size:13px; color:#93C5FD;'>Management System</span></html>");
        lblAppTitle.setForeground(Color.WHITE);

        JLabel lblDivider = new JLabel("───────────────────────────");
        lblDivider.setForeground(new Color(147, 197, 253, 120));

        JLabel lblDesc = new JLabel("<html><p style='width:240px; color:#E2E8F0; line-height:1.4; font-size:11px;'>"
                + "A comprehensive hostel mess operations platform for daily menu scheduling, "
                + "student dietary preferences, real-time kitchen headcounts, and meal analytics."
                + "</p></html>");

        JPanel studentCard = new JPanel(new GridLayout(4, 1, 3, 3));
        studentCard.setOpaque(true);
        studentCard.setBackground(UIConstants.PRIMARY_DARK);
        studentCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.PRIMARY_LIGHT, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblProject = new JLabel("Minor Project NSP (2026-27)");
        lblProject.setFont(UIConstants.FONT_BOLD_SMALL);
        lblProject.setForeground(new Color(147, 197, 253));

        JLabel lblStudent = new JLabel("Student: JITWAR JHA");
        lblStudent.setFont(UIConstants.FONT_BOLD_SMALL);
        lblStudent.setForeground(Color.WHITE);

        JLabel lblRoll = new JLabel("Roll No: PIET25CR022");
        lblRoll.setFont(UIConstants.FONT_SMALL);
        lblRoll.setForeground(new Color(226, 232, 240));

        JLabel lblSec = new JLabel("Section: 3CSD | Sem: III");
        lblSec.setFont(UIConstants.FONT_SMALL);
        lblSec.setForeground(new Color(226, 232, 240));

        studentCard.add(lblProject);
        studentCard.add(lblStudent);
        studentCard.add(lblRoll);
        studentCard.add(lblSec);

        leftContent.add(lblAppTitle);
        leftContent.add(Box.createVerticalStrut(15));
        leftContent.add(lblDivider);
        leftContent.add(Box.createVerticalStrut(15));
        leftContent.add(lblDesc);
        leftContent.add(Box.createVerticalGlue());
        leftContent.add(studentCard);

        leftPanel.add(leftContent, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Right Login Form Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIConstants.BG_MAIN);
        rightPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel formCard = UIConstants.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblLoginHeader = new JLabel("Account Login");
        lblLoginHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLoginHeader.setForeground(UIConstants.TEXT_MAIN);

        JLabel lblLoginSub = new JLabel("Sign in to access mess menu & preferences");
        lblLoginSub.setFont(UIConstants.FONT_REGULAR);
        lblLoginSub.setForeground(UIConstants.TEXT_MUTED);

        txtUsername = UIConstants.createTextField(18);
        txtPassword = UIConstants.createPasswordField(18);

        // Key listeners for Enter key login
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKey);
        txtPassword.addKeyListener(enterKey);

        btnLogin = UIConstants.createPrimaryButton("🔑 Sign In / Login");
        btnLogin.addActionListener(e -> performLogin());

        JButton btnSignUp = UIConstants.createLightButton("📝 New Student? Sign Up / Register");
        btnSignUp.addActionListener(e -> {
            RegisterDialog regDlg = new RegisterDialog(this);
            regDlg.setVisible(true);
            if (regDlg.getRegisteredUsername() != null) {
                txtUsername.setText(regDlg.getRegisteredUsername());
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        });

        // Row 0: Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblLoginHeader, gbc);

        // Row 1: Subtitle
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        formCard.add(lblLoginSub, gbc);

        // Row 2: Spacer
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formCard.add(Box.createVerticalStrut(10), gbc);

        // Row 3: Username Label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel lblUser = new JLabel("Username / Roll Number:");
        lblUser.setFont(UIConstants.FONT_HEADER);
        formCard.add(lblUser, gbc);

        // Row 4: Username Field
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formCard.add(txtUsername, gbc);

        // Row 5: Password Label
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(UIConstants.FONT_HEADER);
        formCard.add(lblPass, gbc);

        // Row 6: Password Field
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formCard.add(txtPassword, gbc);

        // Row 7: Sign In Button
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 4, 8);
        formCard.add(btnLogin, gbc);

        // Row 8: Sign Up / Register Button
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 8, 8, 8);
        formCard.add(btnSignUp, gbc);

        rightPanel.add(formCard, BorderLayout.CENTER);

        // Footer DB Config Button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnDbConfig = UIConstants.createSecondaryButton("⚙ Database Settings");
        btnDbConfig.setFont(UIConstants.FONT_HEADER);
        btnDbConfig.addActionListener(e -> new DatabaseConfigDialog(this).setVisible(true));
        footerPanel.add(btnDbConfig);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // Set default focus
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (!ValidationUtils.isNotEmpty(username)) {
            JOptionPane.showMessageDialog(this, "Please enter your username.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            JOptionPane.showMessageDialog(this, "Please enter your password.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                // Check if user entered roll number instead of username
                List<User> list = userDAO.searchUsers(username, null);
                for (User u : list) {
                    if (username.equalsIgnoreCase(u.getRollNumber()) && password.equals(u.getPassword())) {
                        user = u;
                        break;
                    }
                }
            }

            if (user != null) {
                SessionManager.setCurrentUser(user);
                this.dispose();

                if (user.isAdmin()) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new UserDashboard().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password.\nPlease check your credentials and try again.",
                        "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                txtPassword.selectAll();
                txtPassword.requestFocus();
            }

        } catch (SQLException ex) {
            int opt = JOptionPane.showConfirmDialog(this,
                    "Database connection error:\n" + ex.getMessage() + "\n\nWould you like to open Database Settings to configure MySQL connection?",
                    "Database Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                new DatabaseConfigDialog(this).setVisible(true);
            }
        }
    }
}
