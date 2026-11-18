package utils;

import model.User;

/**
 * SessionManager holds the global logged-in user state.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class SessionManager {

    private static User currentUser = null;

    public static synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    public static synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public static synchronized boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public static synchronized boolean isStudent() {
        return currentUser != null && currentUser.isStudent();
    }

    public static synchronized void logout() {
        currentUser = null;
    }
}
