package model;

/**
 * Model representing Meal Type (Breakfast, Lunch, Snacks, Dinner).
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class MealType {

    private int mealTypeId;
    private String mealTypeName;
    private String startTime;
    private String endTime;
    private String description;

    public MealType() {
    }

    public MealType(int mealTypeId, String mealTypeName, String startTime, String endTime, String description) {
        this.mealTypeId = mealTypeId;
        this.mealTypeName = mealTypeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimingDisplay() {
        return startTime + " - " + endTime;
    }

    @Override
    public String toString() {
        return mealTypeName;
    }
}
