package ui;

import dao.UserDAO;
import model.User;
import utils.SessionManager;
import utils.TableUtils;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User Management Frame providing complete CRUD operations, search and filtering for students and admins.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class UserManagementFrame extends JFrame {

    private JTextField txtUserId;
    private JTextField txtFullName;
    private JTextField txtRollNumber;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JTextField txtEmail;
    private JTextField txtContact;
    private JTextField txtHostelBlock;
    private JTextField txtRoomNumber;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private JTextField txtSearch;
    private JComboBox<String> cmbFilterRole;
    private JTable tblUsers;
    private DefaultTableModel tableModel;

    private final UserDAO userDAO = new UserDAO();
    private List<User> currentUsers = new ArrayList<>();

    public UserManagementFrame() {
        super("User & Student Management - Mess Operations");
        initUI();
        loadUserData();
    }

    private void initUI() {
        setSize(1100, 680);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = UIConstants.createHeaderBanner(
                "User & Student Management",
                "Register, update, delete, and search student and mess staff accounts",
                "Role: " + (SessionManager.isAdmin() ? "ADMIN" : "STUDENT")
        );
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(UIConstants.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT PANEL: Form Inputs
        JPanel leftFormCard = UIConstants.createCardPanel();
        leftFormCard.setPreferredSize(new Dimension(380, 0));
        leftFormCard.setLayout(new BorderLayout(0, 10));

        JLabel lblFormTitle = new JLabel("User Details Form");
        lblFormTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblFormTitle.setForeground(UIConstants.TEXT_MAIN);
        leftFormCard.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        txtUserId = UIConstants.createTextField(10);
        txtUserId.setEditable(false);
        txtUserId.setBackground(new Color(241, 245, 249));

        txtFullName = UIConstants.createTextField(15);
        txtRollNumber = UIConstants.createTextField(15);
        txtUsername = UIConstants.createTextField(15);
        txtPassword = UIConstants.createPasswordField(15);
        cmbRole = new JComboBox<>(new String[]{"STUDENT", "ADMIN"});
        cmbRole.setFont(UIConstants.FONT_REGULAR);
        txtEmail = UIConstants.createTextField(15);
        txtContact = UIConstants.createTextField(15);
        txtHostelBlock = UIConstants.createTextField(15);
        txtRoomNumber = UIConstants.createTextField(15);

        // Populate Form Rows
        int r = 0;
        addFormRow(formGrid, gbc, r++, "User ID (Auto):", txtUserId);
        addFormRow(formGrid, gbc, r++, "Full Name *:", txtFullName);
        addFormRow(formGrid, gbc, r++, "Roll Number:", txtRollNumber);
        addFormRow(formGrid, gbc, r++, "Username *:", txtUsername);
        addFormRow(formGrid, gbc, r++, "Password *:", txtPassword);
        addFormRow(formGrid, gbc, r++, "Role *:", cmbRole);
        addFormRow(formGrid, gbc, r++, "Email:", txtEmail);
        addFormRow(formGrid, gbc, r++, "Contact No:", txtContact);
        addFormRow(formGrid, gbc, r++, "Hostel Block:", txtHostelBlock);
        addFormRow(formGrid, gbc, r++, "Room Number:", txtRoomNumber);

        JScrollPane formScroll = new JScrollPane(formGrid);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        leftFormCard.add(formScroll, BorderLayout.CENTER);

        // Form Buttons
        JPanel formButtons = new JPanel(new GridLayout(2, 2, 8, 8));
        formButtons.setOpaque(false);

        btnAdd = UIConstants.createPrimaryButton("+ Add User");
        btnUpdate = UIConstants.createSecondaryButton("✎ Update");
        btnDelete = UIConstants.createDangerButton("🗑 Delete");
        btnClear = UIConstants.createLightButton("Clear Form");

        btnAdd.addActionListener(e -> handleAddUser());
        btnUpdate.addActionListener(e -> handleUpdateUser());
        btnDelete.addActionListener(e -> handleDeleteUser());
        btnClear.addActionListener(e -> clearForm());

        formButtons.add(btnAdd);
        formButtons.add(btnUpdate);
        formButtons.add(btnDelete);
        formButtons.add(btnClear);

        leftFormCard.add(formButtons, BorderLayout.SOUTH);
        mainPanel.add(leftFormCard, BorderLayout.WEST);

        // RIGHT PANEL: Search & JTable
        JPanel rightTablePanel = new JPanel(new BorderLayout(0, 10));
        rightTablePanel.setOpaque(false);

        // Search Bar Card
        JPanel searchCard = UIConstants.createCardPanel();
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        txtSearch = UIConstants.createTextField(14);
        cmbFilterRole = new JComboBox<>(new String[]{"All Roles", "STUDENT", "ADMIN"});
        cmbFilterRole.setFont(UIConstants.FONT_REGULAR);

        JButton btnSearch = UIConstants.createPrimaryButton("Search");
        JButton btnReset = UIConstants.createLightButton("Reset");

        btnSearch.addActionListener(e -> handleSearch());
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            cmbFilterRole.setSelectedIndex(0);
            loadUserData();
        });

        searchCard.add(new JLabel("Search:"));
        searchCard.add(txtSearch);
        searchCard.add(new JLabel("Role:"));
        searchCard.add(cmbFilterRole);
        searchCard.add(btnSearch);
        searchCard.add(btnReset);

        rightTablePanel.add(searchCard, BorderLayout.NORTH);

        // Users JTable
        String[] colNames = {"ID", "Full Name", "Roll No", "Username", "Role", "Email", "Contact", "Hostel / Room"};
        tableModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        UIConstants.styleTable(tblUsers);

        tblUsers.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblUsers.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblUsers.getColumnModel().getColumn(2).setPreferredWidth(95);
        tblUsers.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblUsers.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblUsers.getColumnModel().getColumn(5).setPreferredWidth(150);
        tblUsers.getColumnModel().getColumn(6).setPreferredWidth(95);
        tblUsers.getColumnModel().getColumn(7).setPreferredWidth(95);

        tblUsers.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblUsers.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblUsers.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblUsers.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblUsers.getColumnModel().getColumn(4).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblUsers.getColumnModel().getColumn(5).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblUsers.getColumnModel().getColumn(6).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblUsers.getColumnModel().getColumn(7).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));

        tblUsers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tblUsers);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        rightTablePanel.add(scrollTable, BorderLayout.CENTER);

        mainPanel.add(rightTablePanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Bottom close bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomBar.setBackground(UIConstants.BG_MAIN);
        JButton btnClose = UIConstants.createLightButton("Close Window");
        btnClose.addActionListener(e -> dispose());
        bottomBar.add(btnClose);
        add(bottomBar, BorderLayout.SOUTH);
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

    private void loadUserData() {
        try {
            currentUsers = userDAO.getAllUsers();
            renderTable(currentUsers);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderTable(List<User> list) {
        tableModel.setRowCount(0);
        for (User u : list) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getFullName(),
                    u.getRollNumber() == null || u.getRollNumber().isEmpty() ? "-" : u.getRollNumber(),
                    u.getUsername(),
                    u.getRole(),
                    u.getEmail() == null ? "-" : u.getEmail(),
                    u.getContactNo() == null ? "-" : u.getContactNo(),
                    (u.getHostelBlock() != null ? u.getHostelBlock() : "") + " / " + (u.getRoomNumber() != null ? u.getRoomNumber() : "")
            });
        }
    }

    private void populateFormFromSelectedRow() {
        int selected = tblUsers.getSelectedRow();
        if (selected >= 0 && selected < currentUsers.size()) {
            User u = currentUsers.get(selected);
            txtUserId.setText(String.valueOf(u.getUserId()));
            txtFullName.setText(u.getFullName());
            txtRollNumber.setText(u.getRollNumber() != null ? u.getRollNumber() : "");
            txtUsername.setText(u.getUsername());
            txtPassword.setText(u.getPassword());
            cmbRole.setSelectedItem(u.getRole());
            txtEmail.setText(u.getEmail() != null ? u.getEmail() : "");
            txtContact.setText(u.getContactNo() != null ? u.getContactNo() : "");
            txtHostelBlock.setText(u.getHostelBlock() != null ? u.getHostelBlock() : "Block-A");
            txtRoomNumber.setText(u.getRoomNumber() != null ? u.getRoomNumber() : "101");
        }
    }

    private void clearForm() {
        txtUserId.setText("");
        txtFullName.setText("");
        txtRollNumber.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        txtEmail.setText("");
        txtContact.setText("");
        txtHostelBlock.setText("Block-A");
        txtRoomNumber.setText("101");
        tblUsers.clearSelection();
        txtFullName.requestFocus();
    }

    private void handleAddUser() {
        String name = txtFullName.getText().trim();
        String roll = txtRollNumber.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String block = txtHostelBlock.getText().trim();
        String room = txtRoomNumber.getText().trim();

        if (!ValidationUtils.isNotEmpty(name)) {
            JOptionPane.showMessageDialog(this, "Please enter Full Name.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(username)) {
            JOptionPane.showMessageDialog(this, "Please enter Username.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            JOptionPane.showMessageDialog(this, "Please enter Password.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid Email address format.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Username '" + username + "' is already taken. Please choose another.", "Duplicate Username", JOptionPane.WARNING_MESSAGE);
                txtUsername.requestFocus();
                return;
            }

            if (ValidationUtils.isNotEmpty(roll) && userDAO.isRollNumberTaken(roll, 0)) {
                JOptionPane.showMessageDialog(this, "Roll number '" + roll + "' is already assigned to another student.", "Duplicate Roll Number", JOptionPane.WARNING_MESSAGE);
                txtRollNumber.requestFocus();
                return;
            }

            User newUser = new User(username, password, name, roll, role, email, contact, block, room);
            boolean ok = userDAO.addUser(newUser);
            if (ok) {
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Could not add user.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateUser() {
        String idStr = txtUserId.getText().trim();
        if (!ValidationUtils.isNotEmpty(idStr)) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table to update.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(idStr);
        String name = txtFullName.getText().trim();
        String roll = txtRollNumber.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String block = txtHostelBlock.getText().trim();
        String room = txtRoomNumber.getText().trim();

        if (!ValidationUtils.isNotEmpty(name)) {
            JOptionPane.showMessageDialog(this, "Please enter Full Name.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid Email address format.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidPhone(contact)) {
            JOptionPane.showMessageDialog(this, "Contact number must be 10-12 digits.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (ValidationUtils.isNotEmpty(roll) && userDAO.isRollNumberTaken(roll, userId)) {
                JOptionPane.showMessageDialog(this, "Roll number '" + roll + "' is already assigned to another student.", "Duplicate Roll Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User(userId, username, password, name, roll, role, email, contact, block, room);
            boolean ok = userDAO.updateUser(user);
            if (ok) {
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update user record.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteUser() {
        String idStr = txtUserId.getText().trim();
        if (!ValidationUtils.isNotEmpty(idStr)) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table to delete.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(idStr);
        String name = txtFullName.getText().trim();

        if (SessionManager.getCurrentUser() != null && SessionManager.getCurrentUser().getUserId() == userId) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own active administrator account!", "Action Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete user '" + name + "' (ID: " + userId + ")?\nThis will also remove associated meal preferences.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = userDAO.deleteUser(userId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSearch() {
        String query = txtSearch.getText().trim();
        String roleFilter = (String) cmbFilterRole.getSelectedItem();

        try {
            currentUsers = userDAO.searchUsers(query, roleFilter);
            renderTable(currentUsers);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
