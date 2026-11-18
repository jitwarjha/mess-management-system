package ui;

import dao.MealPreferenceDAO;
import dao.MealTypeDAO;
import model.MealPreference;
import model.MealType;
import model.PreferenceSummary;
import utils.DateUtils;
import utils.TableUtils;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Mess Headcount and Meal Preference Report Frame for kitchen operations.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class AdminReportFrame extends JFrame {

    private JTextField txtReportDate;
    private JTable tblSummary;
    private DefaultTableModel summaryModel;

    private JTable tblDetails;
    private DefaultTableModel detailsModel;

    private JComboBox<String> cmbDetailMealFilter;

    private final MealPreferenceDAO prefDAO = new MealPreferenceDAO();
    private final MealTypeDAO mealTypeDAO = new MealTypeDAO();

    private List<PreferenceSummary> currentSummaries = new ArrayList<>();
    private List<MealPreference> currentDetails = new ArrayList<>();
    private List<MealType> mealTypes = new ArrayList<>();

    public AdminReportFrame() {
        super("Mess Kitchen Headcount & Preference Report");
        initUI();
        loadMealTypes();
        loadReportData();
    }

    private void initUI() {
        setSize(1080, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = UIConstants.createHeaderBanner(
                "Mess Headcount & Kitchen Summary Report",
                "Real-time dietary headcounts to optimize meal preparation and reduce food wastage",
                "ADMINISTRATOR"
        );
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(UIConstants.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // TOP DATE BAR
        JPanel dateCard = UIConstants.createCardPanel();
        dateCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        txtReportDate = UIConstants.createTextField(10);
        txtReportDate.setText(DateUtils.formatShort(DateUtils.today()));

        JButton btnToday = UIConstants.createLightButton("Today");
        JButton btnTom = UIConstants.createLightButton("Tomorrow");
        JButton btnFetch = UIConstants.createPrimaryButton("Generate Report");

        btnToday.addActionListener(e -> { txtReportDate.setText(DateUtils.formatShort(DateUtils.today())); loadReportData(); });
        btnTom.addActionListener(e -> { txtReportDate.setText(DateUtils.formatShort(DateUtils.tomorrow())); loadReportData(); });
        btnFetch.addActionListener(e -> loadReportData());

        dateCard.add(new JLabel("Report Date (DD-MM-YYYY):"));
        dateCard.add(txtReportDate);
        dateCard.add(btnToday);
        dateCard.add(btnTom);
        dateCard.add(btnFetch);

        mainPanel.add(dateCard, BorderLayout.NORTH);

        // CENTER SPLIT: Top is Aggregated Table, Bottom is Student Detail List
        JPanel centerSplit = new JPanel(new GridLayout(2, 1, 0, 15));
        centerSplit.setOpaque(false);

        // 1. Aggregated Kitchen Headcount Table
        JPanel summaryCard = UIConstants.createCardPanel();
        summaryCard.setLayout(new BorderLayout(0, 8));

        JLabel lblSumTitle = new JLabel("1. Session Headcount Breakdown (Kitchen Portions)");
        lblSumTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblSumTitle.setForeground(UIConstants.PRIMARY);
        summaryCard.add(lblSumTitle, BorderLayout.NORTH);

        String[] sumCols = {"Meal Session", "Total Expected", "Veg Count", "Non-Veg Count", "Jain Count", "Special Count", "Not Eating (Absent)"};
        summaryModel = new DefaultTableModel(sumCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblSummary = new JTable(summaryModel);
        UIConstants.styleTable(tblSummary);

        for (int i = 0; i < sumCols.length; i++) {
            int align = (i == 0) ? SwingConstants.LEFT : SwingConstants.CENTER;
            tblSummary.getColumnModel().getColumn(i).setCellRenderer(new TableUtils.AlternatingRowRenderer(align));
        }

        JScrollPane scrollSummary = new JScrollPane(tblSummary);
        scrollSummary.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        summaryCard.add(scrollSummary, BorderLayout.CENTER);
        centerSplit.add(summaryCard);

        // 2. Detailed Student Preference Roster Table
        JPanel detailsCard = UIConstants.createCardPanel();
        detailsCard.setLayout(new BorderLayout(0, 8));

        JPanel detailHeader = new JPanel(new BorderLayout());
        detailHeader.setOpaque(false);

        JLabel lblDetTitle = new JLabel("2. Resident Detail Log");
        lblDetTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblDetTitle.setForeground(UIConstants.PRIMARY);
        detailHeader.add(lblDetTitle, BorderLayout.WEST);

        JPanel detailFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        detailFilter.setOpaque(false);
        cmbDetailMealFilter = new JComboBox<>();
        cmbDetailMealFilter.setFont(UIConstants.FONT_REGULAR);
        cmbDetailMealFilter.addItem("All Sessions");
        cmbDetailMealFilter.addActionListener(e -> filterDetailTable());

        detailFilter.add(new JLabel("Filter Session:"));
        detailFilter.add(cmbDetailMealFilter);
        detailHeader.add(detailFilter, BorderLayout.EAST);

        detailsCard.add(detailHeader, BorderLayout.NORTH);

        String[] detCols = {"Roll No", "Student Name", "Session", "Dietary Choice", "Special Instructions / Notes"};
        detailsModel = new DefaultTableModel(detCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblDetails = new JTable(detailsModel);
        UIConstants.styleTable(tblDetails);

        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblDetails.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblDetails.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblDetails.getColumnModel().getColumn(4).setPreferredWidth(220);

        tblDetails.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblDetails.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblDetails.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblDetails.getColumnModel().getColumn(4).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));

        JScrollPane scrollDetails = new JScrollPane(tblDetails);
        scrollDetails.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        detailsCard.add(scrollDetails, BorderLayout.CENTER);
        centerSplit.add(detailsCard);

        mainPanel.add(centerSplit, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // BOTTOM ACTION BAR
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottomBar.setBackground(UIConstants.BG_MAIN);

        JButton btnPrint = UIConstants.createPrimaryButton("⎙ Print / Kitchen Slip Preview");
        JButton btnClose = UIConstants.createLightButton("Close");

        btnPrint.addActionListener(e -> showKitchenSlipPreview());
        btnClose.addActionListener(e -> dispose());

        bottomBar.add(btnPrint);
        bottomBar.add(btnClose);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadMealTypes() {
        try {
            mealTypes = mealTypeDAO.getAllMealTypes();
            for (MealType mt : mealTypes) {
                cmbDetailMealFilter.addItem(mt.getMealTypeName());
            }
        } catch (SQLException e) {
            System.err.println("Error loading meal types: " + e.getMessage());
        }
    }

    private void loadReportData() {
        Date date = parseShortDate(txtReportDate.getText().trim());
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in DD-MM-YYYY format.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Load summaries
            currentSummaries = prefDAO.getPreferenceSummaryForDate(date);
            summaryModel.setRowCount(0);
            for (PreferenceSummary s : currentSummaries) {
                int eatingTotal = s.getVegCount() + s.getNonVegCount() + s.getJainCount() + s.getSpecialCount();
                summaryModel.addRow(new Object[]{
                        s.getMealTypeName(),
                        eatingTotal + " (Total logged: " + s.getTotalPreferences() + ")",
                        s.getVegCount(),
                        s.getNonVegCount(),
                        s.getJainCount(),
                        s.getSpecialCount(),
                        s.getNotEatingCount()
                });
            }

            // Load details
            currentDetails = prefDAO.getAllPreferences(date, null, null, null);
            filterDetailTable();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to generate report: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterDetailTable() {
        String filter = (String) cmbDetailMealFilter.getSelectedItem();
        detailsModel.setRowCount(0);

        for (MealPreference p : currentDetails) {
            if (filter == null || "All Sessions".equalsIgnoreCase(filter) || filter.equalsIgnoreCase(p.getMealTypeName())) {
                detailsModel.addRow(new Object[]{
                        p.getRollNumber() != null ? p.getRollNumber() : "-",
                        p.getUserName(),
                        p.getMealTypeName(),
                        p.getDietaryChoice(),
                        p.getSpecialNotes() == null || p.getSpecialNotes().isEmpty() ? "-" : p.getSpecialNotes()
                });
            }
        }
    }

    private void showKitchenSlipPreview() {
        Date date = parseShortDate(txtReportDate.getText().trim());
        String formatted = DateUtils.formatDisplay(date);

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("          POORNIMA HOSTEL MESS — DAILY KITCHEN PRODUCTION SLIP   \n");
        sb.append("=================================================================\n");
        sb.append(" Date: ").append(formatted).append("\n");
        sb.append(" Generated By: ").append("Mess Head Warden / Admin").append("\n");
        sb.append("-----------------------------------------------------------------\n");
        sb.append(String.format("%-12s | %-8s | %-8s | %-6s | %-8s | %-10s\n",
                "Meal", "Veg", "Non-Veg", "Jain", "Special", "Not Eating"));
        sb.append("-----------------------------------------------------------------\n");

        for (PreferenceSummary s : currentSummaries) {
            sb.append(String.format("%-12s | %-8d | %-8d | %-6d | %-8d | %-10d\n",
                    s.getMealTypeName(),
                    s.getVegCount(),
                    s.getNonVegCount(),
                    s.getJainCount(),
                    s.getSpecialCount(),
                    s.getNotEatingCount()
            ));
        }

        sb.append("=================================================================\n");
        sb.append(" Notes / Kitchen Instructions:\n");
        sb.append(" 1. Ensure Jain meals are prepared separately with zero root vegetables.\n");
        sb.append(" 2. Portion quantities must strictly reflect the above registered counts.\n");
        sb.append(" 3. Food wastage audit will be cross-referenced with this slip.\n");
        sb.append("=================================================================\n");

        JTextArea area = new JTextArea(sb.toString(), 18, 48);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Kitchen Production Slip", JOptionPane.INFORMATION_MESSAGE);
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
