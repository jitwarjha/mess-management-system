package dao;

import database.DBConnection;
import model.MessMenu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Mess Menu management.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MenuDAO {

    /**
     * Adds a new mess menu record.
     */
    public boolean addMenu(MessMenu menu) throws SQLException {
        String sql = "INSERT INTO mess_menu (menu_date, meal_type_id, items, dietary_type, description, created_by) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, menu.getMenuDate());
            ps.setInt(2, menu.getMealTypeId());
            ps.setString(3, menu.getItems().trim());
            ps.setString(4, menu.getDietaryType());
            ps.setString(5, menu.getDescription() == null ? "" : menu.getDescription().trim());
            if (menu.getCreatedBy() != null && menu.getCreatedBy() > 0) {
                ps.setInt(6, menu.getCreatedBy());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        menu.setMenuId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing mess menu record.
     */
    public boolean updateMenu(MessMenu menu) throws SQLException {
        String sql = "UPDATE mess_menu SET menu_date = ?, meal_type_id = ?, items = ?, dietary_type = ?, "
                   + "description = ? WHERE menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, menu.getMenuDate());
            ps.setInt(2, menu.getMealTypeId());
            ps.setString(3, menu.getItems().trim());
            ps.setString(4, menu.getDietaryType());
            ps.setString(5, menu.getDescription() == null ? "" : menu.getDescription().trim());
            ps.setInt(6, menu.getMenuId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a menu item by menu_id.
     */
    public boolean deleteMenu(int menuId) throws SQLException {
        String sql = "DELETE FROM mess_menu WHERE menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a single menu by ID.
     */
    public MessMenu getMenuById(int menuId) throws SQLException {
        String sql = "SELECT m.*, mt.meal_type_name, u.full_name AS creator_name "
                   + "FROM mess_menu m "
                   + "JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id "
                   + "LEFT JOIN users u ON m.created_by = u.user_id "
                   + "WHERE m.menu_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMenu(rs);
                }
            }
        }
        return null;
    }

    /**
     * Checks if a menu already exists for date & meal_type (for duplicate validation).
     */
    public boolean existsMenu(Date date, int mealTypeId, int excludeMenuId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mess_menu WHERE menu_date = ? AND meal_type_id = ? AND menu_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            ps.setInt(2, mealTypeId);
            ps.setInt(3, excludeMenuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves all menu items ordered by date descending, meal type ascending.
     */
    public List<MessMenu> getAllMenus() throws SQLException {
        List<MessMenu> list = new ArrayList<>();
        String sql = "SELECT m.*, mt.meal_type_name, u.full_name AS creator_name "
                   + "FROM mess_menu m "
                   + "JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id "
                   + "LEFT JOIN users u ON m.created_by = u.user_id "
                   + "ORDER BY m.menu_date DESC, m.meal_type_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToMenu(rs));
            }
        }
        return list;
    }

    /**
     * Retrieves menus for a specific date.
     */
    public List<MessMenu> getMenusByDate(Date date) throws SQLException {
        List<MessMenu> list = new ArrayList<>();
        String sql = "SELECT m.*, mt.meal_type_name, u.full_name AS creator_name "
                   + "FROM mess_menu m "
                   + "JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id "
                   + "LEFT JOIN users u ON m.created_by = u.user_id "
                   + "WHERE m.menu_date = ? "
                   + "ORDER BY m.meal_type_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToMenu(rs));
                }
            }
        }
        return list;
    }

    /**
     * Search and filter menus with multiple dynamic criteria.
     */
    public List<MessMenu> searchMenus(String keyword, Date date, Integer mealTypeId, String dietaryType) throws SQLException {
        List<MessMenu> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(
            "SELECT m.*, mt.meal_type_name, u.full_name AS creator_name "
          + "FROM mess_menu m "
          + "JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id "
          + "LEFT JOIN users u ON m.created_by = u.user_id "
          + "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (m.items LIKE ? OR m.description LIKE ?) ");
            String wild = "%" + keyword.trim() + "%";
            params.add(wild);
            params.add(wild);
        }

        if (date != null) {
            sb.append("AND m.menu_date = ? ");
            params.add(date);
        }

        if (mealTypeId != null && mealTypeId > 0) {
            sb.append("AND m.meal_type_id = ? ");
            params.add(mealTypeId);
        }

        if (dietaryType != null && !dietaryType.equalsIgnoreCase("ALL") && !dietaryType.trim().isEmpty()) {
            sb.append("AND m.dietary_type = ? ");
            params.add(dietaryType.trim());
        }

        sb.append("ORDER BY m.menu_date DESC, m.meal_type_id ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToMenu(rs));
                }
            }
        }
        return list;
    }

    public int getTodayMenuCount(Date date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mess_menu WHERE menu_date = ?";
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

    private MessMenu mapRowToMenu(ResultSet rs) throws SQLException {
        MessMenu m = new MessMenu();
        m.setMenuId(rs.getInt("menu_id"));
        m.setMenuDate(rs.getDate("menu_date"));
        m.setMealTypeId(rs.getInt("meal_type_id"));
        m.setMealTypeName(rs.getString("meal_type_name"));
        m.setItems(rs.getString("items"));
        m.setDietaryType(rs.getString("dietary_type"));
        m.setDescription(rs.getString("description"));
        m.setCreatedBy((Integer) rs.getObject("created_by"));
        m.setCreatorName(rs.getString("creator_name"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));
        return m;
    }
}
