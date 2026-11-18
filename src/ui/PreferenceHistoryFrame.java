package ui;

import dao.MealPreferenceDAO;
import dao.MealTypeDAO;
import model.MealPreference;
import model.MealType;
import model.User;
import utils.DateUtils;
import utils.SessionManager;
import utils.TableUtils;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Meal Preference History Frame allowing students to review past selections
 * and admins to audit all student meal requests.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class PreferenceHistoryFrame extends JFrame {

    private JTextField txtSearchRoll;
    private JTextField txtDateFrom;
    private JTextField txtDateTo;
    private JComboBox<String> cmbMealType;
    private JComboBox<String> cmbDietChoice;

    private JTable tblPreferences;
    private DefaultTableModel tableModel;
    private JLabel lblTotalCount;

    private final MealPreferenceDAO prefDAO = new MealPreferenceDAO();
    private final MealTypeDAO mealTypeDAO = new MealTypeDAO();

    private List<MealPreference> currentList = new ArrayList<>();
    private List<MealType> mealTypes = new ArrayList<>();

    public PreferenceHistoryFrame() {
        super("Meal Preference History & Records");
        initUI();
        loadMealTypes();
        loadHistoryData();
    }

    private void initUI() {
        setSize(1050, 640);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        User user = SessionManager.getCurrentUser();
        boolean isAdmin = SessionManager.isAdmin();

        String bannerTitle = isAdmin ? "Meal Preference Audit & History" : "My Meal Preference History";
        String bannerSub = isAdmin ? "View and filter dietary requests from all hostel residents" : "Review your previously logged meal choices and attendance notes";
        String userBadge = user != null ? (user.getFullName() + " (" + user.getRole() + ")") : "User";

        JPanel header = UIConstants.createHeaderBanner(bannerTitle, bannerSub, userBadge);
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(UIConstants.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // FILTER TOOLBAR CARD
        JPanel filterCard = UIConstants.createCardPanel();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        txtDateFrom = UIConstants.createTextField(9);
        txtDateTo = UIConstants.createTextField(9);

        cmbMealType = new JComboBox<>();
        cmbMealType.setFont(UIConstants.FONT_REGULAR);
        cmbMealType.addItem("All Meals");

        cmbDietChoice = new JComboBox<>(new String[]{"All Choices", "VEG", "NON-VEG", "JAIN", "SPECIAL", "NOT_EATING"});
        cmbDietChoice.setFont(UIConstants.FONT_REGULAR);

        JButton btnSearch = UIConstants.createPrimaryButton("Search / Filter");
        JButton btnReset = UIConstants.createLightButton("Reset");

        btnSearch.addActionListener(e -> applyFilters());
        btnReset.addActionListener(e -> resetFilters());

        if (isAdmin) {
            txtSearchRoll = UIConstants.createTextField(10);
            filterCard.add(new JLabel("Student Roll/Name:"));
            filterCard.add(txtSearchRoll);
        }

        filterCard.add(new JLabel("From (DD-MM-YYYY):"));
        filterCard.add(txtDateFrom);
        filterCard.add(new JLabel("To:"));
        filterCard.add(txtDateTo);
        filterCard.add(cmbMealType);
        filterCard.add(cmbDietChoice);
        filterCard.add(btnSearch);
        filterCard.add(btnReset);

        mainPanel.add(filterCard, BorderLayout.NORTH);

        // TABLE SETUP
        String[] columns = {"ID", "Date", "Day", "Student Name", "Roll Number", "Meal Session", "Dietary Choice", "Special Instructions", "Submitted At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblPreferences = new JTable(tableModel);
        UIConstants.styleTable(tblPreferences);

        tblPreferences.getColumnModel().getColumn(0).setPreferredWidth(45);  // ID
        tblPreferences.getColumnModel().getColumn(1).setPreferredWidth(85);  // Date
        tblPreferences.getColumnModel().getColumn(2).setPreferredWidth(75);  // Day
        tblPreferences.getColumnModel().getColumn(3).setPreferredWidth(140); // Student Name
        tblPreferences.getColumnModel().getColumn(4).setPreferredWidth(95);  // Roll No
        tblPreferences.getColumnModel().getColumn(5).setPreferredWidth(95);  // Meal
        tblPreferences.getColumnModel().getColumn(6).setPreferredWidth(95);  // Diet Choice
        tblPreferences.getColumnModel().getColumn(7).setPreferredWidth(180); // Notes
        tblPreferences.getColumnModel().getColumn(8).setPreferredWidth(130); // Submitted

        tblPreferences.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblPreferences.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblPreferences.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblPreferences.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblPreferences.getColumnModel().getColumn(4).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblPreferences.getColumnModel().getColumn(5).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblPreferences.getColumnModel().getColumn(6).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblPreferences.getColumnModel().getColumn(7).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblPreferences.getColumnModel().getColumn(8).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));

        JScrollPane scrollPane = new JScrollPane(tblPreferences);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // BOTTOM ACTION BAR
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(UIConstants.BG_MAIN);
        bottomBar.setBorder(new EmptyBorder(0, 20, 15, 20));

        JPanel leftSummary = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftSummary.setOpaque(false);
        lblTotalCount = new JLabel("Total Records: 0");
        lblTotalCount.setFont(UIConstants.FONT_HEADER);
        leftSummary.add(lblTotalCount);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);

        JButton btnDelete = UIConstants.createDangerButton("🗑 Delete Selected");
        JButton btnExport = UIConstants.createSecondaryButton("⎙ Summary View");
        JButton btnClose = UIConstants.createLightButton("Close");

        btnDelete.addActionListener(e -> handleDelete());
        btnExport.addActionListener(e -> showSummaryDialog());
        btnClose.addActionListener(e -> dispose());

        rightActions.add(btnExport);
        rightActions.add(btnDelete);
        rightActions.add(btnClose);

        bottomBar.add(leftSummary, BorderLayout.WEST);
        bottomBar.add(rightActions, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadMealTypes() {
        try {
            mealTypes = mealTypeDAO.getAllMealTypes();
            for (MealType mt : mealTypes) {
                cmbMealType.addItem(mt.getMealTypeName());
            }
        } catch (SQLException e) {
            System.err.println("Error loading meal types: " + e.getMessage());
        }
    }

    private void loadHistoryData() {
        try {
            User user = SessionManager.getCurrentUser();
            if (user == null) return;

            if (user.isAdmin()) {
                currentList = prefDAO.getAllPreferences(null, null, null, null);
            } else {
                currentList = prefDAO.getUserPreferencesHistory(user.getUserId(), null, null, null);
            }
            renderTable(currentList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load preferences: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderTable(List<MealPreference> list) {
        tableModel.setRowCount(0);
        for (MealPreference p : list) {
            tableModel.addRow(new Object[]{
                    p.getPreferenceId(),
                    DateUtils.formatShort(p.getPreferenceDate()),
                    DateUtils.getDayOfWeek(p.getPreferenceDate()),
                    p.getUserName() != null ? p.getUserName() : "Student",
                    p.getRollNumber() != null ? p.getRollNumber() : "-",
                    p.getMealTypeName(),
                    p.getDietaryChoice(),
                    p.getSpecialNotes() == null || p.getSpecialNotes().isEmpty() ? "-" : p.getSpecialNotes(),
                    p.getSubmittedAt() != null ? p.getSubmittedAt().toString().substring(0, 16) : "-"
            });
        }
        lblTotalCount.setText("Total Records: " + list.size());
    }

    private void applyFilters() {
        User user = SessionManager.getCurrentUser();
        if (user == null) return;

        Date fromDate = parseShortDate(txtDateFrom.getText().trim());
        Date toDate = parseShortDate(txtDateTo.getText().trim());

        Integer mealTypeId = null;
        int mealIdx = cmbMealType.getSelectedIndex();
        if (mealIdx > 0 && mealIdx - 1 < mealTypes.size()) {
            mealTypeId = mealTypes.get(mealIdx - 1).getMealTypeId();
        }

        String diet = (String) cmbDietChoice.getSelectedItem();
        if ("All Choices".equalsIgnoreCase(diet)) {
            diet = null;
        }

        try {
            if (user.isAdmin()) {
                String rollSearch = txtSearchRoll != null ? txtSearchRoll.getText().trim() : null;
                // For admin, if fromDate is provided we filter by that date
                currentList = prefDAO.getAllPreferences(fromDate, mealTypeId, diet, rollSearch);
            } else {
                currentList = prefDAO.getUserPreferencesHistory(user.getUserId(), fromDate, toDate, mealTypeId);
            }
            renderTable(currentList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Filter query error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFilters() {
        if (txtSearchRoll != null) txtSearchRoll.setText("");
        txtDateFrom.setText("");
        txtDateTo.setText("");
        cmbMealType.setSelectedIndex(0);
        cmbDietChoice.setSelectedIndex(0);
        loadHistoryData();
    }

    private void handleDelete() {
        int selected = tblPreferences.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Please select a preference record to delete.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int prefId = (int) tableModel.getValueAt(selected, 0);
        String date = (String) tableModel.getValueAt(selected, 1);
        String meal = (String) tableModel.getValueAt(selected, 5);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete preference record #" + prefId + " (" + meal + " on " + date + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = prefDAO.deletePreference(prefId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Preference record deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadHistoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete preference record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showSummaryDialog() {
        int veg = 0, nonveg = 0, jain = 0, special = 0, notEating = 0;
        for (MealPreference p : currentList) {
            switch (p.getDietaryChoice().toUpperCase()) {
                case "VEG": veg++; break;
                case "NON-VEG": nonveg++; break;
                case "JAIN": jain++; break;
                case "SPECIAL": special++; break;
                case "NOT_EATING": notEating++; break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("     MEAL PREFERENCE FILTER SUMMARY      \n");
        sb.append("=========================================\n\n");
        sb.append(String.format("Total Preference Entries : %d\n\n", currentList.size()));
        sb.append(String.format("  • Vegetarian (Veg)     : %d\n", veg));
        sb.append(String.format("  • Non-Vegetarian       : %d\n", nonveg));
        sb.append(String.format("  • Jain Meal            : %d\n", jain));
        sb.append(String.format("  • Special Diet         : %d\n", special));
        sb.append(String.format("  • Not Eating / Absent  : %d\n\n", notEating));
        sb.append("=========================================\n");

        JTextArea area = new JTextArea(sb.toString(), 14, 32);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Preference Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }

    private Date parseShortDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            java.util.Date d = DateUtils.SHORT_DATE_FORMATTER.parse(dateStr.trim());
            return new Date(d.getTime());
        } catch (Exception e) {
            return ValidationUtils.parseDate(dateStr);
        }
    }
}
