package ui;

import database.DBConnection;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Database Configuration Dialog allowing users/examiners to test and configure MySQL connection.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class DatabaseConfigDialog extends JDialog {

    private JTextField txtHost;
    private JTextField txtPort;
    private JTextField txtDbName;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    public DatabaseConfigDialog(JFrame parent) {
        super(parent, "MySQL Database Configuration", true);
        initUI();
    }

    private void initUI() {
        setSize(480, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel header = UIConstants.createHeaderBanner(
                "Database Configuration",
                "Set MySQL connection parameters & test connectivity",
                null
        );
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BG_CARD);
        formPanel.setBorder(new EmptyBorder(20, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        txtHost = UIConstants.createTextField(15);
        txtHost.setText(DBConnection.getHost());

        txtPort = UIConstants.createTextField(15);
        txtPort.setText(DBConnection.getPort());

        txtDbName = UIConstants.createTextField(15);
        txtDbName.setText(DBConnection.getDbName());

        txtUser = UIConstants.createTextField(15);
        txtUser.setText(DBConnection.getUser());

        txtPassword = UIConstants.createPasswordField(15);
        txtPassword.setText(DBConnection.getPassword());

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Host Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtHost, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(createLabel("Port:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtPort, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(createLabel("Database Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtDbName, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(createLabel("MySQL Username:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtUser, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(createLabel("MySQL Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtPassword, gbc);

        // Row 5: Status Label
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        lblStatus = new JLabel("Click 'Test Connection' to verify settings.");
        lblStatus.setFont(UIConstants.FONT_SMALL);
        lblStatus.setForeground(UIConstants.TEXT_MUTED);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblStatus, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Bottom Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        actionPanel.setBackground(UIConstants.BG_MAIN);
        actionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR));

        JButton btnTest = UIConstants.createSecondaryButton("Test Connection");
        JButton btnSave = UIConstants.createPrimaryButton("Save & Apply");
        JButton btnInitDB = UIConstants.createLightButton("Import database.sql");
        JButton btnCancel = UIConstants.createLightButton("Close");

        btnTest.addActionListener(e -> testConn());
        btnSave.addActionListener(e -> saveConfig());
        btnInitDB.addActionListener(e -> initDatabaseFromScript());
        btnCancel.addActionListener(e -> dispose());

        actionPanel.add(btnInitDB);
        actionPanel.add(btnTest);
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

    private boolean testConn() {
        String h = txtHost.getText().trim();
        String p = txtPort.getText().trim();
        String db = txtDbName.getText().trim();
        String u = txtUser.getText().trim();
        String pass = new String(txtPassword.getPassword());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ignored) {}

        boolean ok = DBConnection.testConnection(h, p, db, u, pass);
        if (ok) {
            lblStatus.setText("✓ Connection Successful!");
            lblStatus.setForeground(UIConstants.COLOR_SUCCESS);
            JOptionPane.showMessageDialog(this, "Connection to MySQL database successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblStatus.setText("✗ Connection Failed. Check credentials & ensure MySQL is running.");
            lblStatus.setForeground(UIConstants.COLOR_DANGER);
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to MySQL.\nPlease verify host, port, username, password and ensure the MySQL server is started.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        return ok;
    }

    private void saveConfig() {
        try {
            String h = txtHost.getText().trim();
            String p = txtPort.getText().trim();
            String db = txtDbName.getText().trim();
            String u = txtUser.getText().trim();
            String pass = new String(txtPassword.getPassword());

            DBConnection.saveConfig(h, p, db, u, pass);
            JOptionPane.showMessageDialog(this, "Database configuration saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving configuration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initDatabaseFromScript() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "This will execute database.sql to create tables and sample data.\nDo you wish to proceed?",
                "Confirm Database Setup", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found in classpath:\n" + e.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String h = txtHost.getText().trim();
        String p = txtPort.getText().trim();
        String u = txtUser.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String urlNoDb = "jdbc:mysql://" + h + ":" + p + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&allowMultiQueries=true";

        File sqlFile = new File("database.sql");
        if (!sqlFile.exists()) {
            JOptionPane.showMessageDialog(this, "database.sql file not found in project directory.", "File Missing", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(urlNoDb, u, pass);
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFile))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("--") || line.startsWith("/*") || line.isEmpty()) {
                    continue;
                }
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    stmt.execute(sb.toString());
                    sb = new StringBuilder();
                }
            }

            lblStatus.setText("✓ Database initialized successfully!");
            lblStatus.setForeground(UIConstants.COLOR_SUCCESS);
            JOptionPane.showMessageDialog(this, "Database 'mess_management_db' created & sample data imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to execute database.sql:\n" + e.getMessage(), "Execution Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
