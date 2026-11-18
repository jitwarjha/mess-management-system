package ui;

import dao.UserDAO;
import model.User;
import utils.SessionManager;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Modal dialog allowing users to change their password securely.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class ChangePasswordDialog extends JDialog {

    private JPasswordField txtCurrent;
    private JPasswordField txtNew;
    private JPasswordField txtConfirm;
    private final UserDAO userDAO = new UserDAO();

    public ChangePasswordDialog(JFrame parent) {
        super(parent, "Change Account Password", true);
        initUI();
    }

    private void initUI() {
        setSize(440, 360);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        User current = SessionManager.getCurrentUser();
        String userBadge = current != null ? current.getUsername() : "User";

        JPanel header = UIConstants.createHeaderBanner("Change Password", "Update credentials for: " + userBadge, null);
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BG_CARD);
        formPanel.setBorder(new EmptyBorder(20, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 6, 8, 6);

        txtCurrent = UIConstants.createPasswordField(15);
        txtNew = UIConstants.createPasswordField(15);
        txtConfirm = UIConstants.createPasswordField(15);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        formPanel.add(createLabel("Current Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        formPanel.add(txtCurrent, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        formPanel.add(createLabel("New Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        formPanel.add(txtNew, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        formPanel.add(createLabel("Confirm New Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        formPanel.add(txtConfirm, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actionPanel.setBackground(UIConstants.BG_MAIN);
        actionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR));

        JButton btnSave = UIConstants.createPrimaryButton("Update Password");
        JButton btnCancel = UIConstants.createLightButton("Cancel");

        btnSave.addActionListener(e -> handleChangePassword());
        btnCancel.addActionListener(e -> dispose());

        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_HEADER);
        lbl.setForeground(UIConstants.TEXT_MAIN);
        return lbl;
    }

    private void handleChangePassword() {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Session expired. Please re-login.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        String curPass = new String(txtCurrent.getPassword());
        String newPass = new String(txtNew.getPassword());
        String confPass = new String(txtConfirm.getPassword());

        if (!ValidationUtils.isNotEmpty(curPass)) {
            JOptionPane.showMessageDialog(this, "Please enter your current password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtCurrent.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(newPass)) {
            JOptionPane.showMessageDialog(this, "Please enter a new password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNew.requestFocus();
            return;
        }

        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "New password must be at least 4 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNew.requestFocus();
            return;
        }

        if (!newPass.equals(confPass)) {
            JOptionPane.showMessageDialog(this, "New password and Confirm password do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtConfirm.requestFocus();
            return;
        }

        try {
            boolean success = userDAO.changePassword(user.getUserId(), curPass, newPass);
            if (success) {
                user.setPassword(newPass);
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect current password. Please try again.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                txtCurrent.selectAll();
                txtCurrent.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
