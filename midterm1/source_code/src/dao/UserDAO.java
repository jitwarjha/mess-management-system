package dao;

import database.DBConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User management and authentication.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class UserDAO {

    /**
     * Authenticates a user by username and password.
     */
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves user by user_id.
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves user by username.
     */
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    /**
     * Returns list of all users.
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY role ASC, full_name ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToUser(rs));
            }
        }
        return list;
    }

    /**
     * Searches users by query keyword (name, roll, username, email) and optional role filter.
     */
    public List<User> searchUsers(String keyword, String roleFilter) throws SQLException {
        List<User> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM users WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (full_name LIKE ? OR username LIKE ? OR roll_number LIKE ? OR email LIKE ?) ");
            String wild = "%" + keyword.trim() + "%";
            params.add(wild);
            params.add(wild);
            params.add(wild);
            params.add(wild);
        }

        if (roleFilter != null && !roleFilter.equalsIgnoreCase("ALL") && !roleFilter.trim().isEmpty()) {
            sb.append("AND role = ? ");
            params.add(roleFilter.trim().toUpperCase());
        }

        sb.append("ORDER BY role ASC, full_name ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToUser(rs));
                }
            }
        }
        return list;
    }

    /**
     * Adds a new user record to MySQL.
     */
    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, full_name, roll_number, role, email, contact_no, hostel_block, room_number) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername().trim());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName().trim());
            ps.setString(4, (user.getRollNumber() == null || user.getRollNumber().trim().isEmpty()) ? null : user.getRollNumber().trim());
            ps.setString(5, user.getRole().toUpperCase());
            ps.setString(6, user.getEmail() == null ? null : user.getEmail().trim());
            ps.setString(7, user.getContactNo() == null ? null : user.getContactNo().trim());
            ps.setString(8, user.getHostelBlock() == null ? "Block-A" : user.getHostelBlock().trim());
            ps.setString(9, user.getRoomNumber() == null ? "101" : user.getRoomNumber().trim());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setUserId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing user record.
     */
    public boolean updateUser(User user) throws SQLException {
        boolean updatePassword = user.getPassword() != null && !user.getPassword().trim().isEmpty();
        String sql;
        if (updatePassword) {
            sql = "UPDATE users SET full_name = ?, roll_number = ?, role = ?, email = ?, contact_no = ?, "
                + "hostel_block = ?, room_number = ?, password = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE users SET full_name = ?, roll_number = ?, role = ?, email = ?, contact_no = ?, "
                + "hostel_block = ?, room_number = ? WHERE user_id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName().trim());
            ps.setString(2, (user.getRollNumber() == null || user.getRollNumber().trim().isEmpty()) ? null : user.getRollNumber().trim());
            ps.setString(3, user.getRole().toUpperCase());
            ps.setString(4, user.getEmail() == null ? null : user.getEmail().trim());
            ps.setString(5, user.getContactNo() == null ? null : user.getContactNo().trim());
            ps.setString(6, user.getHostelBlock() == null ? "Block-A" : user.getHostelBlock().trim());
            ps.setString(7, user.getRoomNumber() == null ? "101" : user.getRoomNumber().trim());

            if (updatePassword) {
                ps.setString(8, user.getPassword());
                ps.setInt(9, user.getUserId());
            } else {
                ps.setInt(8, user.getUserId());
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes user by user_id.
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Updates user password after verifying current password.
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) throws SQLException {
        String verifySql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(verifySql)) {
            ps.setInt(1, userId);
            ps.setString(2, currentPassword);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false; // Wrong current password
                }
            }
        }

        String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isUsernameTaken(String username, int excludeUserId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND user_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isRollNumberTaken(String rollNumber, int excludeUserId) throws SQLException {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM users WHERE roll_number = ? AND user_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNumber.trim());
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int getTotalUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getStudentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'STUDENT'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("full_name"));
        u.setRollNumber(rs.getString("roll_number"));
        u.setRole(rs.getString("role"));
        u.setEmail(rs.getString("email"));
        u.setContactNo(rs.getString("contact_no"));
        u.setHostelBlock(rs.getString("hostel_block"));
        u.setRoomNumber(rs.getString("room_number"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
}
