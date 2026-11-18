package ui;

import dao.MealPreferenceDAO;
import dao.MenuDAO;
import model.MealPreference;
import model.MessMenu;
import model.User;
import utils.DateUtils;
import utils.SessionManager;
import utils.TableUtils;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Modern Student Dashboard for viewing menus, choosing meal preferences,
 * and reviewing personal history.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class UserDashboard extends JFrame {

    private JLabel lblTodayStatus;
    private JLabel lblTotalHistory;
    private JTable tblTodayMenu;
    private DefaultTableModel menuModel;

    private final MenuDAO menuDAO = new MenuDAO();
    private final MealPreferenceDAO prefDAO = new MealPreferenceDAO();

    public UserDashboard() {
        super("Student Mess Portal - Mess Menu & Meal Preference Management System");
        initUI();
        loadStudentDashboardData();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        User user = SessionManager.getCurrentUser();
        String studentName = user != null ? user.getFullName() : "Student";
        String studentRoll = user != null && user.getRollNumber() != null ? user.getRollNumber() : "PIET25CR022";
        String room = user != null ? (user.getHostelBlock() + " / Room " + user.getRoomNumber()) : "Hostel Block";

        setupMenuBar();

        // Header Banner
        JPanel header = UIConstants.createHeaderBanner(
                "STUDENT MESS PORTAL",
                "Welcome, " + studentName + " (" + studentRoll + " | " + room + ")",
                "ROLE: STUDENT"
        );
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(UIConstants.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Top Summary Cards
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        metricsPanel.setOpaque(false);

        lblTodayStatus = new JLabel("Checking...");
        lblTotalHistory = new JLabel("0");

        JLabel lblDateVal = new JLabel(DateUtils.formatShort(DateUtils.today()));
        metricsPanel.add(createMetricCard("Today's Date", lblDateVal, UIConstants.PRIMARY, "📅"));
        metricsPanel.add(createMetricCard("My Today's Preferences", lblTodayStatus, UIConstants.ACCENT, "🍽️"));
        metricsPanel.add(createMetricCard("Total Preferences Submitted", lblTotalHistory, new Color(217, 119, 6), "📜"));

        mainPanel.add(metricsPanel, BorderLayout.NORTH);

        // Center Content Split: Left Quick Actions, Right Today's Menu Table
        JPanel centerSplit = new JPanel(new BorderLayout(15, 0));
        centerSplit.setOpaque(false);

        // Left Navigation Actions Card
        JPanel navContainer = UIConstants.createCardPanel();
        navContainer.setPreferredSize(new Dimension(300, 0));
        navContainer.setLayout(new BorderLayout(0, 12));

        JLabel lblNavTitle = new JLabel("Quick Actions");
        lblNavTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblNavTitle.setForeground(UIConstants.PRIMARY);
        navContainer.add(lblNavTitle, BorderLayout.NORTH);

        JPanel navButtons = new JPanel(new GridLayout(4, 1, 0, 12));
        navButtons.setOpaque(false);

        JButton btnSelectPref = UIConstants.createPrimaryButton("🍽️  Select Meal Preference");
        JButton btnViewSchedule = UIConstants.createSecondaryButton("📋  View Weekly Menu Schedule");
        JButton btnHistory = UIConstants.createLightButton("📜  My Preference History");
        JButton btnChangePass = UIConstants.createLightButton("🔑  Change Password");

        btnSelectPref.addActionListener(e -> {
            new MealPreferenceFrame().setVisible(true);
            loadStudentDashboardData();
        });
        btnViewSchedule.addActionListener(e -> new MenuManagementFrame().setVisible(true));
        btnHistory.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));
        btnChangePass.addActionListener(e -> new ChangePasswordDialog(this).setVisible(true));

        navButtons.add(btnSelectPref);
        navButtons.add(btnViewSchedule);
        navButtons.add(btnHistory);
        navButtons.add(btnChangePass);

        navContainer.add(navButtons, BorderLayout.CENTER);
        centerSplit.add(navContainer, BorderLayout.WEST);

        // Right Today's Menu Preview Card
        JPanel menuCard = UIConstants.createCardPanel();
        menuCard.setLayout(new BorderLayout(0, 10));

        JPanel menuHeader = new JPanel(new BorderLayout());
        menuHeader.setOpaque(false);

        JLabel lblMenuTitle = new JLabel("Today's Food Menu (" + DateUtils.formatDisplay(DateUtils.today()) + ")");
        lblMenuTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblMenuTitle.setForeground(UIConstants.PRIMARY);

        JButton btnRefresh = UIConstants.createLightButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> loadStudentDashboardData());

        menuHeader.add(lblMenuTitle, BorderLayout.WEST);
        menuHeader.add(btnRefresh, BorderLayout.EAST);
        menuCard.add(menuHeader, BorderLayout.NORTH);

        String[] menuCols = {"Meal Session", "Category", "Menu Items", "Notes / Special"};
        menuModel = new DefaultTableModel(menuCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblTodayMenu = new JTable(menuModel);
        UIConstants.styleTable(tblTodayMenu);

        tblTodayMenu.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblTodayMenu.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblTodayMenu.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblTodayMenu.getColumnModel().getColumn(3).setPreferredWidth(160);

        tblTodayMenu.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblTodayMenu.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblTodayMenu.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblTodayMenu.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));

        JScrollPane scrollMenu = new JScrollPane(tblTodayMenu);
        scrollMenu.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        menuCard.add(scrollMenu, BorderLayout.CENTER);

        centerSplit.add(menuCard, BorderLayout.CENTER);
        mainPanel.add(centerSplit, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom Footer Bar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR),
                new EmptyBorder(8, 25, 8, 25)
        ));

        JLabel lblFooterLeft = new JLabel("Poornima Institute of Engineering & Technology | Minor Project NSP (Session 2026-27)");
        lblFooterLeft.setFont(UIConstants.FONT_SMALL);
        lblFooterLeft.setForeground(UIConstants.TEXT_MUTED);

        JButton btnLogout = UIConstants.createDangerButton("Logout Session");
        btnLogout.setFont(UIConstants.FONT_SMALL);
        btnLogout.addActionListener(e -> handleLogout());

        footer.add(lblFooterLeft, BorderLayout.WEST);
        footer.add(btnLogout, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private void setupMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.setFont(UIConstants.FONT_REGULAR);

        JMenu mFile = new JMenu("File");
        JMenuItem miLogout = new JMenuItem("Logout");
        JMenuItem miExit = new JMenuItem("Exit");
        miLogout.addActionListener(e -> handleLogout());
        miExit.addActionListener(e -> System.exit(0));
        mFile.add(miLogout);
        mFile.add(miExit);

        JMenu mActions = new JMenu("Meal Services");
        JMenuItem miPref = new JMenuItem("Select Daily Preference");
        JMenuItem miMenu = new JMenuItem("View Full Menu");
        JMenuItem miHist = new JMenuItem("My Preference History");

        miPref.addActionListener(e -> new MealPreferenceFrame().setVisible(true));
        miMenu.addActionListener(e -> new MenuManagementFrame().setVisible(true));
        miHist.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));

        mActions.add(miPref);
        mActions.add(miMenu);
        mActions.add(miHist);

        JMenu mTools = new JMenu("Account");
        JMenuItem miPass = new JMenuItem("Change Password");
        miPass.addActionListener(e -> new ChangePasswordDialog(this).setVisible(true));
        mTools.add(miPass);

        JMenu mHelp = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About Student & Project");
        miAbout.addActionListener(e -> showAboutDialog());
        mHelp.add(miAbout);

        mb.add(mFile);
        mb.add(mActions);
        mb.add(mTools);
        mb.add(mHelp);

        setJMenuBar(mb);
    }

    private JPanel createMetricCard(String title, JLabel lblVal, Color accentColor, String icon) {
        JPanel card = UIConstants.createCardPanel();
        card.setLayout(new BorderLayout(8, 6));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_BOLD_SMALL);
        lblTitle.setForeground(UIConstants.TEXT_MUTED);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        top.add(lblTitle, BorderLayout.CENTER);
        top.add(lblIcon, BorderLayout.EAST);

        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVal.setForeground(accentColor);

        card.add(top, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);

        return card;
    }

    public void loadStudentDashboardData() {
        User user = SessionManager.getCurrentUser();
        if (user == null) return;

        try {
            // Load Total History Count
            int totalPrefs = prefDAO.getUserPreferenceCount(user.getUserId());
            lblTotalHistory.setText(totalPrefs + " Submissions");

            // Load Today's Registered Preferences
            List<MealPreference> todayPrefs = prefDAO.getUserPreferencesHistory(user.getUserId(), DateUtils.today(), DateUtils.today(), null);
            if (todayPrefs.isEmpty()) {
                lblTodayStatus.setText("Not Submitted Yet");
                lblTodayStatus.setForeground(UIConstants.COLOR_WARNING);
            } else {
                lblTodayStatus.setText(todayPrefs.size() + " / 4 Meals Recorded");
                lblTodayStatus.setForeground(UIConstants.COLOR_SUCCESS);
            }

            // Load Today's Menu Table
            List<MessMenu> menus = menuDAO.getMenusByDate(DateUtils.today());
            menuModel.setRowCount(0);
            if (menus.isEmpty()) {
                menuModel.addRow(new Object[]{"Today", "INFO", "No menu published yet for today. Check with mess office.", "-"});
            } else {
                for (MessMenu m : menus) {
                    menuModel.addRow(new Object[]{
                            m.getMealTypeName(),
                            m.getDietaryType(),
                            m.getItems(),
                            m.getDescription() == null || m.getDescription().isEmpty() ? "Regular meal" : m.getDescription()
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching student data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Log out from your student account?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private void showAboutDialog() {
        String msg = "====================================================\n"
                + "  MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM    \n"
                + "====================================================\n\n"
                + "Student Name : JITWAR JHA\n"
                + "Roll Number  : PIET25CR022\n"
                + "Section      : 3CSD\n"
                + "Semester     : III (Session 2026-27)\n"
                + "Subject      : Minor Project NSP\n"
                + "Technology   : Java Swing, JDBC, MySQL, IntelliJ IDEA\n";

        JOptionPane.showMessageDialog(this, msg, "About System", JOptionPane.INFORMATION_MESSAGE);
    }
}
