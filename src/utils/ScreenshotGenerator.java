package utils;

import model.User;
import ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Utility to capture high-resolution pixel-perfect PNG screenshots of all application screens.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class ScreenshotGenerator {

    private static final String OUTPUT_DIR_1 = "midterm1/screenshots/";
    private static final String OUTPUT_DIR_2 = "screenshots/";

    public static void main(String[] args) {
        // Enable Anti-Aliasing & Look and Feel
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        new File(OUTPUT_DIR_1).mkdirs();
        new File(OUTPUT_DIR_2).mkdirs();

        // Sample Admin and Student Users for Mock Session
        User adminUser = new User(1, "admin", "admin123", "Mess Head Warden (Admin)", "ADMIN-001", "ADMIN", "admin@piet.edu.in", "9876543210", "Admin Block", "Office-1");
        User studentUser = new User(2, "jitwar", "student123", "Jitwar Jha", "PIET25CR022", "STUDENT", "jitwar.piet25cr022@piet.edu.in", "9123456780", "Block-C", "304");

        System.out.println("[INFO] Starting Screen Capture Routine...");

        // 1. Login Frame
        SessionManager.logout();
        LoginFrame loginFrame = new LoginFrame();
        captureWindow(loginFrame, "Figure_5_1_Login_Page.png", 850, 560);
        loginFrame.dispose();

        // 2. Register Dialog
        JFrame dummyParent = new JFrame();
        dummyParent.setSize(800, 600);
        RegisterDialog regDialog = new RegisterDialog(dummyParent);
        regDialog.setModal(false);
        captureWindow(regDialog, "Figure_5_2_Student_Registration_Page.png", 520, 620);
        regDialog.dispose();

        // 3. Admin Dashboard
        SessionManager.setCurrentUser(adminUser);
        AdminDashboard adminDash = new AdminDashboard();
        captureWindow(adminDash, "Figure_5_3_Administrator_Dashboard.png", 1180, 750);
        adminDash.dispose();

        // 4. User Dashboard
        SessionManager.setCurrentUser(studentUser);
        UserDashboard userDash = new UserDashboard();
        captureWindow(userDash, "Figure_5_4_Student_Portal_Dashboard.png", 1100, 720);
        userDash.dispose();

        // 5. User Management Frame
        SessionManager.setCurrentUser(adminUser);
        UserManagementFrame userMgmt = new UserManagementFrame();
        captureWindow(userMgmt, "Figure_5_5_User_Management_Page.png", 1100, 680);
        userMgmt.dispose();

        // 6. Menu Management Frame
        MenuManagementFrame menuMgmt = new MenuManagementFrame();
        captureWindow(menuMgmt, "Figure_5_6_Mess_Menu_Management_Page.png", 1000, 640);
        menuMgmt.dispose();

        // 7. Meal Preference Frame
        SessionManager.setCurrentUser(studentUser);
        MealPreferenceFrame mealPref = new MealPreferenceFrame();
        captureWindow(mealPref, "Figure_5_7_Meal_Preference_Selection_Page.png", 820, 640);
        mealPref.dispose();

        // 8. Preference History Frame
        SessionManager.setCurrentUser(adminUser);
        PreferenceHistoryFrame prefHist = new PreferenceHistoryFrame();
        captureWindow(prefHist, "Figure_5_8_Preference_History_Audit_Page.png", 1050, 640);
        prefHist.dispose();

        // 9. Admin Report Frame
        AdminReportFrame reportFrame = new AdminReportFrame();
        captureWindow(reportFrame, "Figure_5_9_Kitchen_Headcount_Report_Page.png", 1080, 700);
        reportFrame.dispose();

        // 10. Change Password Dialog
        SessionManager.setCurrentUser(studentUser);
        ChangePasswordDialog pwdDialog = new ChangePasswordDialog(dummyParent);
        pwdDialog.setModal(false);
        captureWindow(pwdDialog, "Figure_5_10_Change_Password_Dialog.png", 440, 360);
        pwdDialog.dispose();

        // 11. Database Config Dialog
        DatabaseConfigDialog dbDialog = new DatabaseConfigDialog(dummyParent);
        dbDialog.setModal(false);
        captureWindow(dbDialog, "Figure_5_11_Database_Configuration_Dialog.png", 480, 420);
        dbDialog.dispose();

        dummyParent.dispose();

        System.out.println("[SUCCESS] All 11 High-Resolution Screenshots Captured Successfully!");
        System.exit(0);
    }

    private static void captureWindow(Window window, String fileName, int w, int h) {
        window.setSize(w, h);
        window.setVisible(true);
        window.validate();
        window.repaint();

        // Small sleep to ensure components are drawn
        try {
            Thread.sleep(150);
        } catch (InterruptedException ignored) {}

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        window.paint(g2d);
        g2d.dispose();

        try {
            ImageIO.write(image, "PNG", new File(OUTPUT_DIR_1 + fileName));
            ImageIO.write(image, "PNG", new File(OUTPUT_DIR_2 + fileName));
            System.out.println("  ✓ Captured: " + fileName);
        } catch (Exception e) {
            System.err.println("  ✗ Error saving " + fileName + ": " + e.getMessage());
        }

        window.setVisible(false);
    }
}
