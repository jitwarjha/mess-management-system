package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a Student's Meal Preference.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MealPreference {

    private int preferenceId;
    private int userId;
    private String userName;
    private String rollNumber;
    private Date preferenceDate;
    private int mealTypeId;
    private String mealTypeName;
    private String dietaryChoice; // "VEG", "NON-VEG", "JAIN", "SPECIAL", "NOT_EATING"
    private String specialNotes;
    private Timestamp submittedAt;
    private Timestamp updatedAt;

    public MealPreference() {
    }

    public MealPreference(int preferenceId, int userId, String userName, String rollNumber,
                          Date preferenceDate, int mealTypeId, String mealTypeName,
                          String dietaryChoice, String specialNotes) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.userName = userName;
        this.rollNumber = rollNumber;
        this.preferenceDate = preferenceDate;
        this.mealTypeId = mealTypeId;
        this.mealTypeName = mealTypeName;
        this.dietaryChoice = dietaryChoice;
        this.specialNotes = specialNotes;
    }

    public MealPreference(int userId, Date preferenceDate, int mealTypeId, String dietaryChoice, String specialNotes) {
        this(0, userId, null, null, preferenceDate, mealTypeId, null, dietaryChoice, specialNotes);
    }

    public int getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Date getPreferenceDate() {
        return preferenceDate;
    }

    public void setPreferenceDate(Date preferenceDate) {
        this.preferenceDate = preferenceDate;
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

    public String getDietaryChoice() {
        return dietaryChoice;
    }

    public void setDietaryChoice(String dietaryChoice) {
        this.dietaryChoice = dietaryChoice;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
