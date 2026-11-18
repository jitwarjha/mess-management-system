package dao;

import database.DBConnection;
import model.MealType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Meal Types.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MealTypeDAO {

    public List<MealType> getAllMealTypes() throws SQLException {
        List<MealType> list = new ArrayList<>();
        String sql = "SELECT * FROM meal_types ORDER BY meal_type_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MealType(
                        rs.getInt("meal_type_id"),
                        rs.getString("meal_type_name"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("description")
                ));
            }
        }
        return list;
    }

    public MealType getMealTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM meal_types WHERE meal_type_id = " + id;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new MealType(
                        rs.getInt("meal_type_id"),
                        rs.getString("meal_type_name"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("description")
                );
            }
        }
        return null;
    }
}
