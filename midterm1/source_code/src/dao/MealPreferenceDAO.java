package dao;

import database.DBConnection;
import model.MealPreference;
import model.PreferenceSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Meal Preferences and Reports.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MealPreferenceDAO {

    /**
     * Saves or updates a student's preference (Upsert logic).
     */
    public boolean saveOrUpdatePreference(MealPreference pref) throws SQLException {
        String checkSql = "SELECT preference_id FROM meal_preferences WHERE user_id = ? AND preference_date = ? AND meal_type_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, pref.getUserId());
            ps.setDate(2, pref.getPreferenceDate());
            ps.setInt(3, pref.getMealTypeId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("preference_id");
                    pref.setPreferenceId(existingId);
                    return updatePreference(pref);
                } else {
                    return addPreference(pref);
                }
            }
        }
    }

    /**
     * Inserts a new meal preference record.
     */
    public boolean addPreference(MealPreference pref) throws SQLException {
        String sql = "INSERT INTO meal_preferences (user_id, preference_date, meal_type_id, dietary_choice, special_notes) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pref.getUserId());
            ps.setDate(2, pref.getPreferenceDate());
            ps.setInt(3, pref.getMealTypeId());
            ps.setString(4, pref.getDietaryChoice());
            ps.setString(5, pref.getSpecialNotes() == null ? "" : pref.getSpecialNotes().trim());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        pref.setPreferenceId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing meal preference.
     */
    public boolean updatePreference(MealPreference pref) throws SQLException {
        String sql = "UPDATE meal_preferences SET dietary_choice = ?, special_notes = ? WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pref.getDietaryChoice());
            ps.setString(2, pref.getSpecialNotes() == null ? "" : pref.getSpecialNotes().trim());
            ps.setInt(3, pref.getPreferenceId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a meal preference record.
     */
    public boolean deletePreference(int preferenceId) throws SQLException {
        String sql = "DELETE FROM meal_preferences WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves specific preference of a user on a given date and meal type.
     */
    public MealPreference getUserPreference(int userId, Date date, int mealTypeId) throws SQLException {
        String sql = "SELECT p.*, u.full_name, u.roll_number, mt.meal_type_name "
                   + "FROM meal_preferences p "
                   + "JOIN users u ON p.user_id = u.user_id "
                   + "JOIN meal_types mt ON p.meal_type_id = mt.meal_type_id "
                   + "WHERE p.user_id = ? AND p.preference_date = ? AND p.meal_type_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, date);
            ps.setInt(3, mealTypeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPreference(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves student's own preference history with optional filtering.
     */
    public List<MealPreference> getUserPreferencesHistory(int userId, Date fromDate, Date toDate, Integer mealTypeId) throws SQLException {
        List<MealPreference> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(
            "SELECT p.*, u.full_name, u.roll_number, mt.meal_type_name "
          + "FROM meal_preferences p "
          + "JOIN users u ON p.user_id = u.user_id "
          + "JOIN meal_types mt ON p.meal_type_id = mt.meal_type_id "
          + "WHERE p.user_id = ? "
        );
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (fromDate != null) {
            sb.append("AND p.preference_date >= ? ");
            params.add(fromDate);
        }
        if (toDate != null) {
            sb.append("AND p.preference_date <= ? ");
            params.add(toDate);
        }
        if (mealTypeId != null && mealTypeId > 0) {
            sb.append("AND p.meal_type_id = ? ");
            params.add(mealTypeId);
        }

        sb.append("ORDER BY p.preference_date DESC, p.meal_type_id ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPreference(rs));
                }
            }
        }
        return list;
    }

    /**
     * Admin search and filter over all student meal preferences.
     */
    public List<MealPreference> getAllPreferences(Date date, Integer mealTypeId, String dietaryChoice, String rollSearch) throws SQLException {
        List<MealPreference> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(
            "SELECT p.*, u.full_name, u.roll_number, mt.meal_type_name "
          + "FROM meal_preferences p "
          + "JOIN users u ON p.user_id = u.user_id "
          + "JOIN meal_types mt ON p.meal_type_id = mt.meal_type_id "
          + "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (date != null) {
            sb.append("AND p.preference_date = ? ");
            params.add(date);
        }
        if (mealTypeId != null && mealTypeId > 0) {
            sb.append("AND p.meal_type_id = ? ");
            params.add(mealTypeId);
        }
        if (dietaryChoice != null && !dietaryChoice.equalsIgnoreCase("ALL") && !dietaryChoice.trim().isEmpty()) {
            sb.append("AND p.dietary_choice = ? ");
            params.add(dietaryChoice.trim());
        }
        if (rollSearch != null && !rollSearch.trim().isEmpty()) {
            sb.append("AND (u.roll_number LIKE ? OR u.full_name LIKE ?) ");
            String wild = "%" + rollSearch.trim() + "%";
            params.add(wild);
            params.add(wild);
        }

        sb.append("ORDER BY p.preference_date DESC, p.meal_type_id ASC, u.roll_number ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToPreference(rs));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves summary count of dietary preferences per meal type for a selected date.
     */
    public List<PreferenceSummary> getPreferenceSummaryForDate(Date date) throws SQLException {
        List<PreferenceSummary> list = new ArrayList<>();
        String sql = "SELECT mt.meal_type_id, mt.meal_type_name, "
                   + "COUNT(p.preference_id) AS total_count, "
                   + "SUM(CASE WHEN p.dietary_choice = 'VEG' THEN 1 ELSE 0 END) AS veg_count, "
                   + "SUM(CASE WHEN p.dietary_choice = 'NON-VEG' THEN 1 ELSE 0 END) AS non_veg_count, "
                   + "SUM(CASE WHEN p.dietary_choice = 'JAIN' THEN 1 ELSE 0 END) AS jain_count, "
                   + "SUM(CASE WHEN p.dietary_choice = 'SPECIAL' THEN 1 ELSE 0 END) AS special_count, "
                   + "SUM(CASE WHEN p.dietary_choice = 'NOT_EATING' THEN 1 ELSE 0 END) AS not_eating_count "
                   + "FROM meal_types mt "
                   + "LEFT JOIN meal_preferences p ON mt.meal_type_id = p.meal_type_id AND p.preference_date = ? "
                   + "GROUP BY mt.meal_type_id, mt.meal_type_name "
                   + "ORDER BY mt.meal_type_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PreferenceSummary sum = new PreferenceSummary();
                    sum.setDate(date);
                    sum.setMealTypeId(rs.getInt("meal_type_id"));
                    sum.setMealTypeName(rs.getString("meal_type_name"));
                    sum.setTotalPreferences(rs.getInt("total_count"));
                    sum.setVegCount(rs.getInt("veg_count"));
                    sum.setNonVegCount(rs.getInt("non_veg_count"));
                    sum.setJainCount(rs.getInt("jain_count"));
                    sum.setSpecialCount(rs.getInt("special_count"));
                    sum.setNotEatingCount(rs.getInt("not_eating_count"));
                    list.add(sum);
                }
            }
        }
        return list;
    }

    public int getTotalPreferencesToday(Date date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meal_preferences WHERE preference_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getUserPreferenceCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meal_preferences WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private MealPreference mapRowToPreference(ResultSet rs) throws SQLException {
        MealPreference p = new MealPreference();
        p.setPreferenceId(rs.getInt("preference_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setUserName(rs.getString("full_name"));
        p.setRollNumber(rs.getString("roll_number"));
        p.setPreferenceDate(rs.getDate("preference_date"));
        p.setMealTypeId(rs.getInt("meal_type_id"));
        p.setMealTypeName(rs.getString("meal_type_name"));
        p.setDietaryChoice(rs.getString("dietary_choice"));
        p.setSpecialNotes(rs.getString("special_notes"));
        p.setSubmittedAt(rs.getTimestamp("submitted_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}
