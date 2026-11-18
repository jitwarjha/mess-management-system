package ui;

import dao.MealTypeDAO;
import dao.MenuDAO;
import model.MealType;
import model.MessMenu;
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
 * Menu Management Window providing complete CRUD, search, and filtering operations.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MenuManagementFrame extends JFrame {

    private JTable tblMenu;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JTextField txtFilterDate;
    private JComboBox<String> cmbFilterMealType;
    private JComboBox<String> cmbFilterDietary;

    private final MenuDAO menuDAO = new MenuDAO();
    private final MealTypeDAO mealTypeDAO = new MealTypeDAO();

    private List<MessMenu> currentMenuList = new ArrayList<>();
    private List<MealType> mealTypes = new ArrayList<>();

    public MenuManagementFrame() {
        super("Mess Menu Management - Poornima Mess Operations");
        initUI();
        loadMealTypes();
        loadMenuData();
    }

    private void initUI() {
        setSize(1000, 640);
        setMinimumSize(new Dimension(880, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Banner
        String roleStr = SessionManager.isAdmin() ? "ADMINISTRATOR" : "STUDENT";
        JPanel header = UIConstants.createHeaderBanner(
                "Mess Menu Management",
                "Create, update, search, and manage daily and weekly mess meal schedules",
                "Role: " + roleStr
        );
        add(header, BorderLayout.NORTH);

        // Center Panel with Filter bar + Table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(UIConstants.BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Filter / Search Toolbar
        JPanel filterCard = UIConstants.createCardPanel();
        filterCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSearch = UIConstants.createTextField(14);
        txtFilterDate = UIConstants.createTextField(9);
        cmbFilterMealType = new JComboBox<>();
        cmbFilterMealType.setFont(UIConstants.FONT_REGULAR);
        cmbFilterMealType.addItem("All Meal Types");

        cmbFilterDietary = new JComboBox<>(new String[]{"All Dietary", "VEG", "NON-VEG", "SPECIAL", "ALL"});
        cmbFilterDietary.setFont(UIConstants.FONT_REGULAR);

        JButton btnSearch = UIConstants.createPrimaryButton("Search / Filter");
        JButton btnReset = UIConstants.createLightButton("Reset All");
        JButton btnToday = UIConstants.createLightButton("Today Only");

        btnSearch.addActionListener(e -> applyFilter());
        btnReset.addActionListener(e -> resetFilters());
        btnToday.addActionListener(e -> {
            txtFilterDate.setText(DateUtils.formatShort(DateUtils.today()));
            applyFilter();
        });

        // Add filter inputs
        gbc.gridx = 0; gbc.gridy = 0;
        filterCard.add(new JLabel("Keyword:"), gbc);
        gbc.gridx = 1;
        filterCard.add(txtSearch, gbc);

        gbc.gridx = 2;
        filterCard.add(new JLabel("Date (DD-MM-YYYY):"), gbc);
        gbc.gridx = 3;
        filterCard.add(txtFilterDate, gbc);

        gbc.gridx = 4;
        filterCard.add(cmbFilterMealType, gbc);

        gbc.gridx = 5;
        filterCard.add(cmbFilterDietary, gbc);

        gbc.gridx = 6;
        filterCard.add(btnSearch, gbc);

        gbc.gridx = 7;
        filterCard.add(btnReset, gbc);

        gbc.gridx = 8;
        filterCard.add(btnToday, gbc);

        centerPanel.add(filterCard, BorderLayout.NORTH);

        // Table Panel
        String[] columns = {"ID", "Date", "Day", "Meal Session", "Menu Food Items", "Category", "Notes / Description", "Created By"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMenu = new JTable(tableModel);
        UIConstants.styleTable(tblMenu);

        // Renderers & column widths
        tblMenu.getColumnModel().getColumn(0).setPreferredWidth(45);  // ID
        tblMenu.getColumnModel().getColumn(1).setPreferredWidth(85);  // Date
        tblMenu.getColumnModel().getColumn(2).setPreferredWidth(75);  // Day
        tblMenu.getColumnModel().getColumn(3).setPreferredWidth(100); // Meal Session
        tblMenu.getColumnModel().getColumn(4).setPreferredWidth(280); // Items
        tblMenu.getColumnModel().getColumn(5).setPreferredWidth(80);  // Category
        tblMenu.getColumnModel().getColumn(6).setPreferredWidth(160); // Notes
        tblMenu.getColumnModel().getColumn(7).setPreferredWidth(100); // Created by

        tblMenu.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblMenu.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblMenu.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));
        tblMenu.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblMenu.getColumnModel().getColumn(4).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblMenu.getColumnModel().getColumn(5).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblMenu.getColumnModel().getColumn(6).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblMenu.getColumnModel().getColumn(7).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.CENTER));

        JScrollPane scrollPane = new JScrollPane(tblMenu);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Actions Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UIConstants.BG_MAIN);
        bottomPanel.setBorder(new EmptyBorder(0, 20, 15, 20));

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftActions.setOpaque(false);

        JLabel lblCount = new JLabel("Total Menus: 0");
        lblCount.setFont(UIConstants.FONT_HEADER);
        leftActions.add(lblCount);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);

        JButton btnAdd = UIConstants.createPrimaryButton("+ Add Menu Record");
        JButton btnEdit = UIConstants.createSecondaryButton("✎ Edit Selected");
        JButton btnDelete = UIConstants.createDangerButton("🗑 Delete Selected");
        JButton btnClose = UIConstants.createLightButton("Close");

        // Role restriction
        if (!SessionManager.isAdmin()) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnAdd.setToolTipText("Admin access required to modify menu");
            btnEdit.setToolTipText("Admin access required to modify menu");
            btnDelete.setToolTipText("Admin access required to modify menu");
        }

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelectedMenu());
        btnClose.addActionListener(e -> dispose());

        rightActions.add(btnAdd);
        rightActions.add(btnEdit);
        rightActions.add(btnDelete);
        rightActions.add(btnClose);

        bottomPanel.add(leftActions, BorderLayout.WEST);
        bottomPanel.add(rightActions, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMealTypes() {
        try {
            mealTypes = mealTypeDAO.getAllMealTypes();
            for (MealType mt : mealTypes) {
                cmbFilterMealType.addItem(mt.getMealTypeName());
            }
        } catch (SQLException e) {
            System.err.println("Error loading meal types: " + e.getMessage());
        }
    }

    private void loadMenuData() {
        try {
            currentMenuList = menuDAO.getAllMenus();
            renderTable(currentMenuList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load menus from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderTable(List<MessMenu> list) {
        tableModel.setRowCount(0);
        for (MessMenu m : list) {
            tableModel.addRow(new Object[]{
                    m.getMenuId(),
                    DateUtils.formatShort(m.getMenuDate()),
                    DateUtils.getDayOfWeek(m.getMenuDate()),
                    m.getMealTypeName(),
                    m.getItems(),
                    m.getDietaryType(),
                    m.getDescription() == null || m.getDescription().isEmpty() ? "-" : m.getDescription(),
                    m.getCreatorName() == null ? "Admin" : m.getCreatorName()
            });
        }
    }

    private void applyFilter() {
        String keyword = txtSearch.getText().trim();
        String dateStr = txtFilterDate.getText().trim();
        Date date = null;
        if (ValidationUtils.isNotEmpty(dateStr)) {
            date = parseShortDate(dateStr);
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use DD-MM-YYYY (e.g., 31-08-2026)", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Integer mealTypeId = null;
        int selectedIndex = cmbFilterMealType.getSelectedIndex();
        if (selectedIndex > 0 && selectedIndex - 1 < mealTypes.size()) {
            mealTypeId = mealTypes.get(selectedIndex - 1).getMealTypeId();
        }

        String dietary = (String) cmbFilterDietary.getSelectedItem();
        if ("All Dietary".equalsIgnoreCase(dietary)) {
            dietary = null;
        }

        try {
            currentMenuList = menuDAO.searchMenus(keyword, date, mealTypeId, dietary);
            renderTable(currentMenuList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Filter query error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFilters() {
        txtSearch.setText("");
        txtFilterDate.setText("");
        cmbFilterMealType.setSelectedIndex(0);
        cmbFilterDietary.setSelectedIndex(0);
        loadMenuData();
    }

    private void openAddDialog() {
        AddEditMenuDialog dlg = new AddEditMenuDialog(this, null);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            loadMenuData();
        }
    }

    private void openEditDialog() {
        int selectedRow = tblMenu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a menu row from the table to edit.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int menuId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            MessMenu menu = menuDAO.getMenuById(menuId);
            if (menu != null) {
                AddEditMenuDialog dlg = new AddEditMenuDialog(this, menu);
                dlg.setVisible(true);
                if (dlg.isSaved()) {
                    loadMenuData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selected menu record was not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedMenu() {
        int selectedRow = tblMenu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a menu record to delete.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int menuId = (int) tableModel.getValueAt(selectedRow, 0);
        String dateStr = (String) tableModel.getValueAt(selectedRow, 1);
        String mealSession = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the menu for " + mealSession + " on " + dateStr + " (Menu ID: " + menuId + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = menuDAO.deleteMenu(menuId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Menu deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadMenuData();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete the menu record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to delete: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
