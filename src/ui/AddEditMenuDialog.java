package ui;

import dao.MealTypeDAO;
import dao.MenuDAO;
import model.MealType;
import model.MessMenu;
import utils.DateUtils;
import utils.SessionManager;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Modal Dialog for Adding and Editing Mess Menu items.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class AddEditMenuDialog extends JDialog {

    private JTextField txtDate;
    private JComboBox<MealTypeItem> cmbMealType;
    private JComboBox<String> cmbDietaryType;
    private JTextArea txtItems;
    private JTextArea txtDescription;

    private final MenuDAO menuDAO = new MenuDAO();
    private final MealTypeDAO mealTypeDAO = new MealTypeDAO();

    private final MessMenu menuToEdit;
    private boolean saved = false;

    public static class MealTypeItem {
        private final int id;
        private final String name;

        public MealTypeItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }

        @Override
        public String toString() { return name; }
    }

    public AddEditMenuDialog(JFrame parent, MessMenu menuToEdit) {
        super(parent, menuToEdit == null ? "Add Mess Menu Item" : "Edit Mess Menu Item", true);
        this.menuToEdit = menuToEdit;
        initUI();
    }

    private void initUI() {
        setSize(560, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        String title = menuToEdit == null ? "Add New Mess Menu" : "Edit Mess Menu #" + menuToEdit.getMenuId();
        String sub = menuToEdit == null ? "Create menu items for a specific date & meal session" : "Modify existing food items or dietary category";

        JPanel header = UIConstants.createHeaderBanner(title, sub, null);
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BG_CARD);
        formPanel.setBorder(new EmptyBorder(15, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Date input with quick shortcuts
        txtDate = UIConstants.createTextField(12);
        txtDate.setText(menuToEdit != null ? DateUtils.formatShort(menuToEdit.getMenuDate()) : DateUtils.formatShort(DateUtils.today()));

        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setOpaque(false);
        datePanel.add(txtDate, BorderLayout.CENTER);

        JButton btnToday = UIConstants.createLightButton("Today");
        JButton btnTom = UIConstants.createLightButton("Tomorrow");
        btnToday.setFont(UIConstants.FONT_SMALL);
        btnTom.setFont(UIConstants.FONT_SMALL);

        btnToday.addActionListener(e -> txtDate.setText(DateUtils.formatShort(DateUtils.today())));
        btnTom.addActionListener(e -> txtDate.setText(DateUtils.formatShort(DateUtils.tomorrow())));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnToday);
        btnPanel.add(btnTom);
        datePanel.add(btnPanel, BorderLayout.EAST);

        // Meal Type Combo
        cmbMealType = new JComboBox<>();
        cmbMealType.setFont(UIConstants.FONT_REGULAR);
        loadMealTypes();

        // Dietary Type Combo
        cmbDietaryType = new JComboBox<>(new String[]{"ALL", "VEG", "NON-VEG", "SPECIAL"});
        cmbDietaryType.setFont(UIConstants.FONT_REGULAR);

        // Items Text Area
        txtItems = new JTextArea(4, 25);
        txtItems.setFont(UIConstants.FONT_REGULAR);
        txtItems.setLineWrap(true);
        txtItems.setWrapStyleWord(true);
        txtItems.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scrollItems = new JScrollPane(txtItems);

        // Description Text Area
        txtDescription = new JTextArea(2, 25);
        txtDescription.setFont(UIConstants.FONT_REGULAR);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scrollDesc = new JScrollPane(txtDescription);

        // Populate fields if editing
        if (menuToEdit != null) {
            txtDate.setText(DateUtils.formatShort(menuToEdit.getMenuDate()));
            cmbDietaryType.setSelectedItem(menuToEdit.getDietaryType());
            txtItems.setText(menuToEdit.getItems());
            txtDescription.setText(menuToEdit.getDescription());
        }

        // Layout rows
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Date (DD-MM-YYYY):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(datePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(createLabel("Meal Type:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(cmbMealType, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(createLabel("Dietary Category:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(cmbDietaryType, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Menu Food Items:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(scrollItems, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(createLabel("Description / Note:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(scrollDesc, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actionPanel.setBackground(UIConstants.BG_MAIN);
        actionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR));

        JButton btnSave = UIConstants.createPrimaryButton(menuToEdit == null ? "Save Menu" : "Update Menu");
        JButton btnCancel = UIConstants.createLightButton("Cancel");

        btnSave.addActionListener(e -> saveMenu());
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

    private void loadMealTypes() {
        try {
            List<MealType> list = mealTypeDAO.getAllMealTypes();
            for (MealType mt : list) {
                MealTypeItem item = new MealTypeItem(mt.getMealTypeId(), mt.getMealTypeName() + " (" + mt.getTimingDisplay() + ")");
                cmbMealType.addItem(item);
                if (menuToEdit != null && menuToEdit.getMealTypeId() == mt.getMealTypeId()) {
                    cmbMealType.setSelectedItem(item);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load meal types: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveMenu() {
        String rawDate = txtDate.getText().trim();
        Date parsedDate = parseShortDate(rawDate);

        if (parsedDate == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in format DD-MM-YYYY (e.g., 31-08-2026).", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            txtDate.requestFocus();
            return;
        }

        MealTypeItem selectedMeal = (MealTypeItem) cmbMealType.getSelectedItem();
        if (selectedMeal == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal type.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String items = txtItems.getText().trim();
        if (!ValidationUtils.isNotEmpty(items)) {
            JOptionPane.showMessageDialog(this, "Please enter the food items for this menu.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            txtItems.requestFocus();
            return;
        }

        String dietary = (String) cmbDietaryType.getSelectedItem();
        String desc = txtDescription.getText().trim();
        int currentUserId = SessionManager.getCurrentUser() != null ? SessionManager.getCurrentUser().getUserId() : 1;

        try {
            int excludeId = menuToEdit != null ? menuToEdit.getMenuId() : 0;
            if (menuDAO.existsMenu(parsedDate, selectedMeal.getId(), excludeId)) {
                JOptionPane.showMessageDialog(this,
                        "A menu for " + selectedMeal.getName() + " on " + rawDate + " already exists!\nPlease edit that record instead.",
                        "Duplicate Menu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok;
            if (menuToEdit == null) {
                MessMenu newMenu = new MessMenu(parsedDate, selectedMeal.getId(), items, dietary, desc, currentUserId);
                ok = menuDAO.addMenu(newMenu);
            } else {
                menuToEdit.setMenuDate(parsedDate);
                menuToEdit.setMealTypeId(selectedMeal.getId());
                menuToEdit.setItems(items);
                menuToEdit.setDietaryType(dietary);
                menuToEdit.setDescription(desc);
                ok = menuDAO.updateMenu(menuToEdit);
            }

            if (ok) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Mess Menu saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save menu record. Please check database logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    public boolean isSaved() {
        return saved;
    }
}
