package model;

import java.sql.Timestamp;

/**
 * Model representing a User (Admin or Student).
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String rollNumber;
    private String role; // "ADMIN" or "STUDENT"
    private String email;
    private String contactNo;
    private String hostelBlock;
    private String roomNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {
    }

    public User(int userId, String username, String password, String fullName, String rollNumber,
                String role, String email, String contactNo, String hostelBlock, String roomNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.role = role;
        this.email = email;
        this.contactNo = contactNo;
        this.hostelBlock = hostelBlock;
        this.roomNumber = roomNumber;
    }

    public User(String username, String password, String fullName, String rollNumber,
                String role, String email, String contactNo, String hostelBlock, String roomNumber) {
        this(0, username, password, fullName, rollNumber, role, email, contactNo, hostelBlock, roomNumber);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(String hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return fullName + " (" + (rollNumber != null ? rollNumber : username) + ")";
    }
}
