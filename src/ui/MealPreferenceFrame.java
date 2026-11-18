package ui;

import dao.MealPreferenceDAO;
import dao.MealTypeDAO;
import dao.MenuDAO;
import model.MealPreference;
import model.MealType;
import model.MessMenu;
import model.User;
import utils.DateUtils;
import utils.SessionManager;
import utils.UIConstants;
import utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

/**
 * Meal Preference Selection Frame allowing students to submit and update their daily meal choices.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MealPreferenceFrame extends JFrame {

    private JTextField txtDate;
    private JComboBox<MealTypeItem> cmbMealType;
    private JLabel lblMenuPreviewTitle;
    private JTextArea txtMenuPreview;
    private JLabel lblMenuTiming;
    private JLabel lblCurrentStatus;

    private ButtonGroup dietGroup;
    private JRadioButton rbVeg;
    private JRadioButton rbNonVeg;
    private JRadioButton rbJain;
    private JRadioButton rbSpecial;
    private JRadioButton rbNotEating;

    private JTextField txtSpecialNotes;
    private JButton btnSavePref;
    private JButton btnCancelPref;

    private final MealPreferenceDAO prefDAO = new MealPreferenceDAO();
    private final MealTypeDAO mealTypeDAO = new MealTypeDAO();
    private final MenuDAO menuDAO = new MenuDAO();

    private MealPreference currentPref = null;

    public static class MealTypeItem {
        private final int id;
        private final String name;
        private final String timing;

        public MealTypeItem(int id, String name, String timing) {
            this.id = id;
            this.name = name;
            this.timing = timing;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getTiming() { return timing; }

        @Override
        public String toString() { return name + " (" + timing + ")"; }
    }

    public MealPreferenceFrame() {
        super("Daily Meal Preference Selection");
        initUI();
        loadMealTypes();
        refreshMenuAndPreference();
    }

    private void initUI() {
        setSize(820, 640);
        setMinimumSize(new Dimension(750, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        User user = SessionManager.getCurrentUser();
        String studentInfo = user != null ? (user.getFullName() + " | " + user.getRollNumber()) : "Student";

        JPanel header = UIConstants.createHeaderBanner(
                "Meal Preference Selection",
                "Choose dietary option for upcoming mess sessions to help reduce food waste",
                studentInfo
        );
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(UIConstants.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        // TOP SECTION: Date & Meal Type Selector Card
        JPanel topCard = UIConstants.createCardPanel();
        topCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);

        txtDate = UIConstants.createTextField(10);
        txtDate.setText(DateUtils.formatShort(DateUtils.today()));

        JButton btnToday = UIConstants.createLightButton("Today");
        JButton btnTom = UIConstants.createLightButton("Tomorrow");
        JButton btnDayAfter = UIConstants.createLightButton("Day After");

        btnToday.addActionListener(e -> { txtDate.setText(DateUtils.formatShort(DateUtils.today())); refreshMenuAndPreference(); });
        btnTom.addActionListener(e -> { txtDate.setText(DateUtils.formatShort(DateUtils.tomorrow())); refreshMenuAndPreference(); });
        btnDayAfter.addActionListener(e -> { txtDate.setText(DateUtils.formatShort(DateUtils.dayAfterTomorrow())); refreshMenuAndPreference(); });

        JPanel dateShortcuts = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        dateShortcuts.setOpaque(false);
        dateShortcuts.add(txtDate);
        dateShortcuts.add(btnToday);
        dateShortcuts.add(btnTom);
        dateShortcuts.add(btnDayAfter);

        cmbMealType = new JComboBox<>();
        cmbMealType.setFont(UIConstants.FONT_REGULAR);
        cmbMealType.addActionListener(e -> refreshMenuAndPreference());

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        topCard.add(new JLabel("Meal Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        topCard.add(dateShortcuts, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        topCard.add(new JLabel("Meal Session:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        topCard.add(cmbMealType, gbc);

        mainPanel.add(topCard, BorderLayout.NORTH);

        // CENTER SECTION: Split into Menu Preview Card (Left) and Dietary Preference Selector (Right)
        JPanel centerSplit = new JPanel(new GridLayout(1, 2, 15, 0));
        centerSplit.setOpaque(false);

        // LEFT: Scheduled Kitchen Menu Preview
        JPanel menuCard = UIConstants.createCardPanel();
        menuCard.setLayout(new BorderLayout(0, 8));

        lblMenuPreviewTitle = new JLabel("Scheduled Mess Menu");
        lblMenuPreviewTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblMenuPreviewTitle.setForeground(UIConstants.PRIMARY);

        lblMenuTiming = new JLabel("Timing: --");
        lblMenuTiming.setFont(UIConstants.FONT_SMALL);
        lblMenuTiming.setForeground(UIConstants.TEXT_MUTED);

        JPanel menuHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        menuHeader.setOpaque(false);
        menuHeader.add(lblMenuPreviewTitle);
        menuHeader.add(lblMenuTiming);

        txtMenuPreview = new JTextArea();
        txtMenuPreview.setFont(UIConstants.FONT_REGULAR);
        txtMenuPreview.setLineWrap(true);
        txtMenuPreview.setWrapStyleWord(true);
        txtMenuPreview.setEditable(false);
        txtMenuPreview.setBackground(new Color(248, 250, 252));
        txtMenuPreview.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollMenu = new JScrollPane(txtMenuPreview);
        scrollMenu.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));

        menuCard.add(menuHeader, BorderLayout.NORTH);
        menuCard.add(scrollMenu, BorderLayout.CENTER);
        centerSplit.add(menuCard);

        // RIGHT: Dietary Preference Form
        JPanel prefCard = UIConstants.createCardPanel();
        prefCard.setLayout(new BorderLayout(0, 10));

        JLabel lblPrefTitle = new JLabel("Select Your Preference");
        lblPrefTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblPrefTitle.setForeground(UIConstants.PRIMARY);

        lblCurrentStatus = new JLabel("Status: No preference recorded yet");
        lblCurrentStatus.setFont(UIConstants.FONT_BOLD_SMALL);
        lblCurrentStatus.setForeground(UIConstants.COLOR_WARNING);

        JPanel prefHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        prefHeader.setOpaque(false);
        prefHeader.add(lblPrefTitle);
        prefHeader.add(lblCurrentStatus);

        JPanel choicesPanel = new JPanel(new GridLayout(5, 1, 4, 4));
        choicesPanel.setOpaque(false);

        dietGroup = new ButtonGroup();
        rbVeg = createDietRadio("Vegetarian (Standard Veg Dish / Dal / Rice / Roti)", "VEG");
        rbNonVeg = createDietRadio("Non-Vegetarian (Chicken / Egg / Fish Option)", "NON-VEG");
        rbJain = createDietRadio("Jain Meal (No onion, garlic, or root vegetables, fruits)", "JAIN");
        rbSpecial = createDietRadio("Special Diet (Health / Medical dietary requirement)", "SPECIAL");
        rbNotEating = createDietRadio("Not Eating / Skipping (Will not dine in this meal)", "NOT_EATING");

        dietGroup.add(rbVeg);
        dietGroup.add(rbNonVeg);
        dietGroup.add(rbJain);
        dietGroup.add(rbSpecial);
        dietGroup.add(rbNotEating);
        rbVeg.setSelected(true);

        choicesPanel.add(rbVeg);
        choicesPanel.add(rbNonVeg);
        choicesPanel.add(rbJain);
        choicesPanel.add(rbSpecial);
        choicesPanel.add(rbNotEating);

        JPanel notesPanel = new JPanel(new BorderLayout(0, 4));
        notesPanel.setOpaque(false);
        JLabel lblNotes = new JLabel("Special Instructions / Notes (Optional):");
        lblNotes.setFont(UIConstants.FONT_SMALL);
        txtSpecialNotes = UIConstants.createTextField(15);
        notesPanel.add(lblNotes, BorderLayout.NORTH);
        notesPanel.add(txtSpecialNotes, BorderLayout.CENTER);

        JPanel prefBody = new JPanel(new BorderLayout(0, 10));
        prefBody.setOpaque(false);
        prefBody.add(choicesPanel, BorderLayout.CENTER);
        prefBody.add(notesPanel, BorderLayout.SOUTH);

        prefCard.add(prefHeader, BorderLayout.NORTH);
        prefCard.add(prefBody, BorderLayout.CENTER);
        centerSplit.add(prefCard);

        mainPanel.add(centerSplit, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // BOTTOM ACTIONS BAR
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(UIConstants.BG_MAIN);
        bottomBar.setBorder(new EmptyBorder(0, 25, 15, 25));

        JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftBtns.setOpaque(false);
        JButton btnHistory = UIConstants.createLightButton("📜 View My Preference History");
        btnHistory.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));
        leftBtns.add(btnHistory);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightBtns.setOpaque(false);

        btnSavePref = UIConstants.createPrimaryButton("✓ Save / Update Preference");
        btnCancelPref = UIConstants.createDangerButton("✕ Remove Preference");
        JButton btnClose = UIConstants.createLightButton("Close");

        btnSavePref.addActionListener(e -> handleSavePreference());
        btnCancelPref.addActionListener(e -> handleDeletePreference());
        btnClose.addActionListener(e -> dispose());

        rightBtns.add(btnSavePref);
        rightBtns.add(btnCancelPref);
        rightBtns.add(btnClose);

        bottomBar.add(leftBtns, BorderLayout.WEST);
        bottomBar.add(rightBtns, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JRadioButton createDietRadio(String text, String actionCmd) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(UIConstants.FONT_REGULAR);
        rb.setActionCommand(actionCmd);
        rb.setOpaque(false);
        return rb;
    }

    private void loadMealTypes() {
        try {
            List<MealType> list = mealTypeDAO.getAllMealTypes();
            for (MealType mt : list) {
                cmbMealType.addItem(new MealTypeItem(mt.getMealTypeId(), mt.getMealTypeName(), mt.getTimingDisplay()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading meal types: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshMenuAndPreference() {
        Date date = parseShortDate(txtDate.getText().trim());
        if (date == null) return;

        MealTypeItem item = (MealTypeItem) cmbMealType.getSelectedItem();
        if (item == null) return;

        lblMenuTiming.setText("Timing: " + item.getTiming());

        // 1. Fetch Scheduled Menu for this date and meal
        try {
            List<MessMenu> menus = menuDAO.getMenusByDate(date);
            MessMenu matched = null;
            for (MessMenu m : menus) {
                if (m.getMealTypeId() == item.getId()) {
                    matched = m;
                    break;
                }
            }

            if (matched != null) {
                lblMenuPreviewTitle.setText(item.getName() + " Menu - [" + matched.getDietaryType() + "]");
                txtMenuPreview.setText("ITEMS:\n" + matched.getItems() + "\n\nDESCRIPTION:\n" + (matched.getDescription() == null || matched.getDescription().isEmpty() ? "Standard hostel meal" : matched.getDescription()));
            } else {
                lblMenuPreviewTitle.setText(item.getName() + " Menu (Not Yet Published)");
                txtMenuPreview.setText("No menu published yet by mess administration for this date.\nDefault regular menu will be served.");
            }
        } catch (SQLException e) {
            txtMenuPreview.setText("Error loading menu: " + e.getMessage());
        }

        // 2. Fetch Student's Preference if already submitted
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            try {
                currentPref = prefDAO.getUserPreference(user.getUserId(), date, item.getId());
                if (currentPref != null) {
                    lblCurrentStatus.setText("Status: ✓ Registered (" + currentPref.getDietaryChoice() + ")");
                    lblCurrentStatus.setForeground(UIConstants.COLOR_SUCCESS);
                    setDietSelection(currentPref.getDietaryChoice());
                    txtSpecialNotes.setText(currentPref.getSpecialNotes());
                    btnCancelPref.setEnabled(true);
                } else {
                    lblCurrentStatus.setText("Status: No preference recorded yet");
                    lblCurrentStatus.setForeground(UIConstants.COLOR_WARNING);
                    rbVeg.setSelected(true);
                    txtSpecialNotes.setText("");
                    btnCancelPref.setEnabled(false);
                }
            } catch (SQLException e) {
                lblCurrentStatus.setText("Status: Error fetching preference");
            }
        }
    }

    private void setDietSelection(String choice) {
        if (choice == null) return;
        switch (choice.toUpperCase()) {
            case "VEG": rbVeg.setSelected(true); break;
            case "NON-VEG": rbNonVeg.setSelected(true); break;
            case "JAIN": rbJain.setSelected(true); break;
            case "SPECIAL": rbSpecial.setSelected(true); break;
            case "NOT_EATING": rbNotEating.setSelected(true); break;
        }
    }

    private String getSelectedDietChoice() {
        ButtonModel sel = dietGroup.getSelection();
        if (sel != null) {
            return sel.getActionCommand();
        }
        return "VEG";
    }

    private void handleSavePreference() {
        Date date = parseShortDate(txtDate.getText().trim());
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date format DD-MM-YYYY.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MealTypeItem item = (MealTypeItem) cmbMealType.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal session.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = SessionManager.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Session expired. Please re-login.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String diet = getSelectedDietChoice();
        String notes = txtSpecialNotes.getText().trim();

        MealPreference pref = new MealPreference(user.getUserId(), date, item.getId(), diet, notes);

        try {
            boolean ok = prefDAO.saveOrUpdatePreference(pref);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Your preference for " + item.getName() + " on " + DateUtils.formatShort(date) + " has been recorded as: " + diet,
                        "Preference Saved", JOptionPane.INFORMATION_MESSAGE);
                refreshMenuAndPreference();
            } else {
                JOptionPane.showMessageDialog(this, "Could not save preference.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeletePreference() {
        if (currentPref == null || currentPref.getPreferenceId() == 0) {
            JOptionPane.showMessageDialog(this, "No active preference exists to remove.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove your preference entry for this meal slot?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = prefDAO.deletePreference(currentPref.getPreferenceId());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Preference removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
                    currentPref = null;
                    refreshMenuAndPreference();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not remove preference.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
