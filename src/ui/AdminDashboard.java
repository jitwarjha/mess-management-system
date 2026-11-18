package ui;

import dao.MealPreferenceDAO;
import dao.MenuDAO;
import dao.UserDAO;
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
 * Modern Administrator Dashboard providing central navigation, metrics, and quick actions.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class AdminDashboard extends JFrame {

    private JLabel lblTotalStudents;
    private JLabel lblTodayMenus;
    private JLabel lblTodayPreferences;
    private JLabel lblDbStatus;

    private JTable tblTodayMenu;
    private DefaultTableModel menuModel;

    private final UserDAO userDAO = new UserDAO();
    private final MenuDAO menuDAO = new MenuDAO();
    private final MealPreferenceDAO prefDAO = new MealPreferenceDAO();

    public AdminDashboard() {
        super("Administrator Dashboard - Mess Menu & Meal Preference Management System");
        initUI();
        loadDashboardMetrics();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        User user = SessionManager.getCurrentUser();
        String adminName = user != null ? user.getFullName() : "Admin";

        // Setup Menu Bar
        setupMenuBar();

        // Top Banner
        JPanel header = UIConstants.createHeaderBanner(
                "MESS MANAGEMENT SYSTEM — ADMIN CONSOLE",
                "Minor Project NSP | Developed by: JITWAR JHA (PIET25CR022, 3CSD)",
                "Logged in as: " + adminName + " (ADMIN)"
        );
        add(header, BorderLayout.NORTH);

        // Main Center Area (Scrollable or Grid)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(UIConstants.BG_MAIN);
        centerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        // 1. Quick Toolbar
        JPanel toolbar = setupToolbar();
        centerPanel.add(toolbar, BorderLayout.NORTH);

        // 2. Center Content: Metrics Cards + Quick Actions + Today's Live Menu
        JPanel contentGrid = new JPanel(new BorderLayout(0, 15));
        contentGrid.setOpaque(false);

        // Top Row: 4 Metric Cards
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        metricsPanel.setOpaque(false);

        lblTotalStudents = new JLabel("0");
        lblTodayMenus = new JLabel("0 / 4");
        lblTodayPreferences = new JLabel("0");
        lblDbStatus = new JLabel("ONLINE");

        metricsPanel.add(createMetricCard("Total Registered Students", lblTotalStudents, new Color(37, 99, 235), "👥"));
        metricsPanel.add(createMetricCard("Today's Scheduled Menus", lblTodayMenus, new Color(13, 148, 136), "📋"));
        metricsPanel.add(createMetricCard("Preferences Logged Today", lblTodayPreferences, new Color(217, 119, 6), "🍽️"));
        metricsPanel.add(createMetricCard("Database Connection", lblDbStatus, new Color(22, 163, 74), "⚡"));

        contentGrid.add(metricsPanel, BorderLayout.NORTH);

        // Center Split: Left Quick Action Cards, Right Live Today Menu Table
        JPanel middleSplit = new JPanel(new BorderLayout(15, 0));
        middleSplit.setOpaque(false);

        // Left Navigation Actions Card
        JPanel navCard = UIConstants.createCardPanel();
        navCard.setPreferredSize(new Dimension(320, 0));
        navCard.setLayout(new GridLayout(5, 1, 0, 10));

        JLabel lblNavTitle = new JLabel("System Modules & Operations");
        lblNavTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblNavTitle.setForeground(UIConstants.PRIMARY);

        JButton btnUserMgmt = UIConstants.createPrimaryButton("👥  User & Student Management");
        JButton btnMenuMgmt = UIConstants.createPrimaryButton("📋  Mess Menu Scheduling");
        JButton btnPrefHistory = UIConstants.createPrimaryButton("📜  Meal Preference Records");
        JButton btnKitchenReport = UIConstants.createSecondaryButton("📊  Kitchen Headcount Report");
        JButton btnDbSettings = UIConstants.createLightButton("⚙  Database Configuration");

        btnUserMgmt.addActionListener(e -> new UserManagementFrame().setVisible(true));
        btnMenuMgmt.addActionListener(e -> new MenuManagementFrame().setVisible(true));
        btnPrefHistory.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));
        btnKitchenReport.addActionListener(e -> new AdminReportFrame().setVisible(true));
        btnDbSettings.addActionListener(e -> new DatabaseConfigDialog(this).setVisible(true));

        JPanel navContainer = UIConstants.createCardPanel();
        navContainer.setPreferredSize(new Dimension(320, 0));
        navContainer.setLayout(new BorderLayout(0, 12));
        navContainer.add(lblNavTitle, BorderLayout.NORTH);

        JPanel navButtons = new JPanel(new GridLayout(5, 1, 0, 10));
        navButtons.setOpaque(false);
        navButtons.add(btnUserMgmt);
        navButtons.add(btnMenuMgmt);
        navButtons.add(btnPrefHistory);
        navButtons.add(btnKitchenReport);
        navButtons.add(btnDbSettings);
        navContainer.add(navButtons, BorderLayout.CENTER);

        middleSplit.add(navContainer, BorderLayout.WEST);

        // Right Live Menu Preview Card
        JPanel menuCard = UIConstants.createCardPanel();
        menuCard.setLayout(new BorderLayout(0, 10));

        JPanel menuHeader = new JPanel(new BorderLayout());
        menuHeader.setOpaque(false);

        JLabel lblMenuTitle = new JLabel("Today's Live Mess Menu (" + DateUtils.formatDisplay(DateUtils.today()) + ")");
        lblMenuTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblMenuTitle.setForeground(UIConstants.PRIMARY);

        JButton btnRefresh = UIConstants.createLightButton("🔄 Refresh Dashboard");
        btnRefresh.addActionListener(e -> loadDashboardMetrics());

        menuHeader.add(lblMenuTitle, BorderLayout.WEST);
        menuHeader.add(btnRefresh, BorderLayout.EAST);
        menuCard.add(menuHeader, BorderLayout.NORTH);

        String[] menuCols = {"Meal Session", "Category", "Food Menu Items", "Description"};
        menuModel = new DefaultTableModel(menuCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblTodayMenu = new JTable(menuModel);
        UIConstants.styleTable(tblTodayMenu);

        tblTodayMenu.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblTodayMenu.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblTodayMenu.getColumnModel().getColumn(2).setPreferredWidth(320);
        tblTodayMenu.getColumnModel().getColumn(3).setPreferredWidth(180);

        tblTodayMenu.getColumnModel().getColumn(0).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblTodayMenu.getColumnModel().getColumn(1).setCellRenderer(new TableUtils.BadgeCellRenderer());
        tblTodayMenu.getColumnModel().getColumn(2).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));
        tblTodayMenu.getColumnModel().getColumn(3).setCellRenderer(new TableUtils.AlternatingRowRenderer(SwingConstants.LEFT));

        JScrollPane scrollMenu = new JScrollPane(tblTodayMenu);
        scrollMenu.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        menuCard.add(scrollMenu, BorderLayout.CENTER);

        middleSplit.add(menuCard, BorderLayout.CENTER);
        contentGrid.add(middleSplit, BorderLayout.CENTER);

        centerPanel.add(contentGrid, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Status Bar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR),
                new EmptyBorder(8, 25, 8, 25)
        ));

        JLabel lblFooterLeft = new JLabel("Mess Menu & Meal Preference Management System | Version 1.0 (Poornima Minor Project NSP)");
        lblFooterLeft.setFont(UIConstants.FONT_SMALL);
        lblFooterLeft.setForeground(UIConstants.TEXT_MUTED);

        JLabel lblFooterRight = new JLabel("Database: MySQL | Host: localhost:3306");
        lblFooterRight.setFont(UIConstants.FONT_SMALL);
        lblFooterRight.setForeground(UIConstants.TEXT_MUTED);

        footer.add(lblFooterLeft, BorderLayout.WEST);
        footer.add(lblFooterRight, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    private void setupMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.setFont(UIConstants.FONT_REGULAR);

        // File Menu
        JMenu mFile = new JMenu("File");
        JMenuItem miLogout = new JMenuItem("Logout Session");
        JMenuItem miExit = new JMenuItem("Exit Application");

        miLogout.addActionListener(e -> handleLogout());
        miExit.addActionListener(e -> System.exit(0));

        mFile.add(miLogout);
        mFile.addSeparator();
        mFile.add(miExit);

        // Manage Menu
        JMenu mManage = new JMenu("Manage");
        JMenuItem miUsers = new JMenuItem("User & Student Management");
        JMenuItem miMenus = new JMenuItem("Mess Menu Management");
        JMenuItem miPrefs = new JMenuItem("Meal Preference Records");
        JMenuItem miReport = new JMenuItem("Kitchen Headcount Report");

        miUsers.addActionListener(e -> new UserManagementFrame().setVisible(true));
        miMenus.addActionListener(e -> new MenuManagementFrame().setVisible(true));
        miPrefs.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));
        miReport.addActionListener(e -> new AdminReportFrame().setVisible(true));

        mManage.add(miUsers);
        mManage.add(miMenus);
        mManage.add(miPrefs);
        mManage.addSeparator();
        mManage.add(miReport);

        // Account / Tools Menu
        JMenu mTools = new JMenu("Tools");
        JMenuItem miPass = new JMenuItem("Change Password");
        JMenuItem miDb = new JMenuItem("Database Configuration");

        miPass.addActionListener(e -> new ChangePasswordDialog(this).setVisible(true));
        miDb.addActionListener(e -> new DatabaseConfigDialog(this).setVisible(true));

        mTools.add(miPass);
        mTools.add(miDb);

        // Help Menu
        JMenu mHelp = new JMenu("Help");
        JMenuItem miAbout = new JMenuItem("About Project & Student Details");
        JMenuItem miDocs = new JMenuItem("Project Guidelines & Viva Help");

        miAbout.addActionListener(e -> showAboutDialog());
        miDocs.addActionListener(e -> showHelpDialog());

        mHelp.add(miAbout);
        mHelp.add(miDocs);

        mb.add(mFile);
        mb.add(mManage);
        mb.add(mTools);
        mb.add(mHelp);

        setJMenuBar(mb);
    }

    private JPanel setupToolbar() {
        JPanel tb = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        tb.setBackground(Color.WHITE);
        tb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER_COLOR, 1),
                new EmptyBorder(4, 10, 4, 10)
        ));

        JButton btn1 = UIConstants.createLightButton("👥 Users");
        JButton btn2 = UIConstants.createLightButton("📋 Menus");
        JButton btn3 = UIConstants.createLightButton("🍽️ Preferences");
        JButton btn4 = UIConstants.createLightButton("📊 Kitchen Report");
        JButton btn5 = UIConstants.createLightButton("🔑 Password");
        JButton btn6 = UIConstants.createLightButton("🚪 Logout");

        btn1.addActionListener(e -> new UserManagementFrame().setVisible(true));
        btn2.addActionListener(e -> new MenuManagementFrame().setVisible(true));
        btn3.addActionListener(e -> new PreferenceHistoryFrame().setVisible(true));
        btn4.addActionListener(e -> new AdminReportFrame().setVisible(true));
        btn5.addActionListener(e -> new ChangePasswordDialog(this).setVisible(true));
        btn6.addActionListener(e -> handleLogout());

        tb.add(btn1);
        tb.add(btn2);
        tb.add(btn3);
        tb.add(btn4);
        tb.add(btn5);
        tb.add(btn6);

        return tb;
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

        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblVal.setForeground(accentColor);

        card.add(top, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);

        return card;
    }

    public void loadDashboardMetrics() {
        try {
            int students = userDAO.getStudentCount();
            lblTotalStudents.setText(String.valueOf(students));

            int todayMenus = menuDAO.getTodayMenuCount(DateUtils.today());
            lblTodayMenus.setText(todayMenus + " / 4");

            int todayPrefs = prefDAO.getTotalPreferencesToday(DateUtils.today());
            lblTodayPreferences.setText(String.valueOf(todayPrefs));

            lblDbStatus.setText("CONNECTED");
            lblDbStatus.setForeground(UIConstants.COLOR_SUCCESS);

            // Load Today's Menu Table
            List<MessMenu> menus = menuDAO.getMenusByDate(DateUtils.today());
            menuModel.setRowCount(0);
            if (menus.isEmpty()) {
                menuModel.addRow(new Object[]{"Today", "INFO", "No menu scheduled for today yet. Use 'Mess Menu Scheduling' to add.", "-"});
            } else {
                for (MessMenu m : menus) {
                    menuModel.addRow(new Object[]{
                            m.getMealTypeName(),
                            m.getDietaryType(),
                            m.getItems(),
                            m.getDescription() == null || m.getDescription().isEmpty() ? "Standard hostel meal" : m.getDescription()
                    });
                }
            }

        } catch (SQLException e) {
            lblDbStatus.setText("ERROR");
            lblDbStatus.setForeground(UIConstants.COLOR_DANGER);
            JOptionPane.showMessageDialog(this, "Failed to refresh dashboard metrics: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out from the Administrator session?",
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
                + "Technology   : Java Swing, JDBC, MySQL, IntelliJ IDEA\n\n"
                + "Description:\n"
                + "A full-featured desktop application designed for hostel mess operations,\n"
                + "enabling menu scheduling, student dietary preference collection, and\n"
                + "live kitchen headcount analytics to minimize food waste.\n";

        JOptionPane.showMessageDialog(this, msg, "About Project", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHelpDialog() {
        String msg = "Quick Operation Guidelines:\n\n"
                + "1. User Management: Add, update, search, or remove student and admin records.\n"
                + "2. Menu Scheduling: Create breakfast, lunch, snacks, and dinner menus with dietary tags.\n"
                + "3. Preference Records: Search and filter student preferences by roll number, date, and meal.\n"
                + "4. Kitchen Report: View aggregated counts of Veg, Non-Veg, and Jain meals for precise cooking portions.\n"
                + "5. Database Settings: Configure MySQL port/password or import sample database schema.\n";

        JOptionPane.showMessageDialog(this, msg, "System Guidelines", JOptionPane.INFORMATION_MESSAGE);
    }
}
