import database.DBConnection;
import ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point for Mess Menu & Meal Preference Management System.
 * 
 * =========================================================================
 * STUDENT & PROJECT DETAILS:
 * Student Name : JITWAR JHA
 * Roll Number  : PIET25CR022
 * Section      : 3CSD
 * Semester     : III (Session 2026-27)
 * Project Type : Minor Project NSP
 * Project Title: MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM
 * Technology   : Java, Java Swing, JDBC, MySQL, IntelliJ IDEA
 * =========================================================================
 */
public class Main {

    public static void main(String[] args) {
        // Enable modern font rendering and subpixel anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Print Project Identification Banner in Console
        printBanner();

        // Initialize Swing Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set System Look and Feel: " + e.getMessage());
        }

        // Launch Application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Check initial MySQL connectivity in background
            boolean connected = DBConnection.isConnected();
            if (!connected) {
                System.out.println("[INFO] MySQL connection not established yet with default credentials.");
                System.out.println("[INFO] You can click '⚙ Database Settings' on the Login Screen to test and configure your MySQL host/port/password.");
            } else {
                System.out.println("[SUCCESS] Successfully connected to MySQL database 'mess_management_db'!");
            }

            // Display Login Frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private static void printBanner() {
        System.out.println("========================================================================");
        System.out.println("   MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM — MINOR PROJECT NSP   ");
        System.out.println("========================================================================");
        System.out.println(" Student Name : JITWAR JHA");
        System.out.println(" Roll Number  : PIET25CR022");
        System.out.println(" Section      : 3CSD | Semester: III | Session: 2026-27");
        System.out.println(" College      : Poornima Institute of Engineering & Technology");
        System.out.println(" Technology   : Java (JDK 26), Java Swing, JDBC, MySQL, IntelliJ IDEA");
        System.out.println("========================================================================");
    }
}
