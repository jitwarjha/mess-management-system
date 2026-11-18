package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a Mess Menu record.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MessMenu {

    private int menuId;
    private Date menuDate;
    private int mealTypeId;
    private String mealTypeName;
    private String items;
    private String dietaryType; // "VEG", "NON-VEG", "SPECIAL", "ALL"
    private String description;
    private Integer createdBy;
    private String creatorName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MessMenu() {
    }

    public MessMenu(int menuId, Date menuDate, int mealTypeId, String mealTypeName,
                    String items, String dietaryType, String description, Integer createdBy) {
        this.menuId = menuId;
        this.menuDate = menuDate;
        this.mealTypeId = mealTypeId;
        this.mealTypeName = mealTypeName;
        this.items = items;
        this.dietaryType = dietaryType;
        this.description = description;
        this.createdBy = createdBy;
    }

    public MessMenu(Date menuDate, int mealTypeId, String items, String dietaryType, String description, Integer createdBy) {
        this(0, menuDate, mealTypeId, null, items, dietaryType, description, createdBy);
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public Date getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(Date menuDate) {
        this.menuDate = menuDate;
    }

    public int getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(int mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public String getMealTypeName() {
        return mealTypeName;
    }

    public void setMealTypeName(String mealTypeName) {
        this.mealTypeName = mealTypeName;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDietaryType() {
        return dietaryType;
    }

    public void setDietaryType(String dietaryType) {
        this.dietaryType = dietaryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
