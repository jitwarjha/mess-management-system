package model;

import java.sql.Date;

/**
 * Model representing an aggregated summary of meal preferences for reports.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class PreferenceSummary {

    private Date date;
    private int mealTypeId;
    private String mealTypeName;
    private int totalPreferences;
    private int vegCount;
    private int nonVegCount;
    private int jainCount;
    private int specialCount;
    private int notEatingCount;

    public PreferenceSummary() {
    }

    public PreferenceSummary(Date date, int mealTypeId, String mealTypeName,
                             int totalPreferences, int vegCount, int nonVegCount,
                             int jainCount, int specialCount, int notEatingCount) {
        this.date = date;
        this.mealTypeId = mealTypeId;
        this.mealTypeName = mealTypeName;
        this.totalPreferences = totalPreferences;
        this.vegCount = vegCount;
        this.nonVegCount = nonVegCount;
        this.jainCount = jainCount;
        this.specialCount = specialCount;
        this.notEatingCount = notEatingCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getTotalPreferences() {
        return totalPreferences;
    }

    public void setTotalPreferences(int totalPreferences) {
        this.totalPreferences = totalPreferences;
    }

    public int getVegCount() {
        return vegCount;
    }

    public void setVegCount(int vegCount) {
        this.vegCount = vegCount;
    }

    public int getNonVegCount() {
        return nonVegCount;
    }

    public void setNonVegCount(int nonVegCount) {
        this.nonVegCount = nonVegCount;
    }

    public int getJainCount() {
        return jainCount;
    }

    public void setJainCount(int jainCount) {
        this.jainCount = jainCount;
    }

    public int getSpecialCount() {
        return specialCount;
    }

    public void setSpecialCount(int specialCount) {
        this.specialCount = specialCount;
    }

    public int getNotEatingCount() {
        return notEatingCount;
    }

    public void setNotEatingCount(int notEatingCount) {
        this.notEatingCount = notEatingCount;
    }
}
