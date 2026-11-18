-- =====================================================================
-- DATABASE SCRIPT: Mess Menu & Meal Preference Management System
-- Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
-- Session: 2026-27 | Semester: III | Minor Project NSP
-- =====================================================================

DROP DATABASE IF EXISTS mess_management_db;
CREATE DATABASE mess_management_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mess_management_db;

-- ---------------------------------------------------------------------
-- 1. Table: users
-- Stores administrative staff, wardens, and student users.
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(50) UNIQUE NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT',
    email VARCHAR(100),
    contact_no VARCHAR(20),
    hostel_block VARCHAR(30) DEFAULT 'Block-A',
    room_number VARCHAR(20) DEFAULT '101',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_role (role),
    INDEX idx_user_username (username),
    INDEX idx_user_roll (roll_number)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 2. Table: meal_types
-- Stores predefined meal types (Breakfast, Lunch, Snacks, Dinner).
-- ---------------------------------------------------------------------
CREATE TABLE meal_types (
    meal_type_id INT AUTO_INCREMENT PRIMARY KEY,
    meal_type_name VARCHAR(30) NOT NULL UNIQUE,
    start_time VARCHAR(20) NOT NULL,
    end_time VARCHAR(20) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 3. Table: mess_menu
-- Stores daily/weekly mess food menu items and dietary details.
-- ---------------------------------------------------------------------
CREATE TABLE mess_menu (
    menu_id INT AUTO_INCREMENT PRIMARY KEY,
    menu_date DATE NOT NULL,
    meal_type_id INT NOT NULL,
    items TEXT NOT NULL,
    dietary_type ENUM('VEG', 'NON-VEG', 'SPECIAL', 'ALL') NOT NULL DEFAULT 'VEG',
    description TEXT,
    created_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_meal_type FOREIGN KEY (meal_type_id) REFERENCES meal_types(meal_type_id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_creator FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT uq_menu_date_meal UNIQUE (menu_date, meal_type_id),
    INDEX idx_menu_date (menu_date),
    INDEX idx_menu_meal (meal_type_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 4. Table: meal_preferences
-- Stores student daily preferences per meal type.
-- ---------------------------------------------------------------------
CREATE TABLE meal_preferences (
    preference_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    preference_date DATE NOT NULL,
    meal_type_id INT NOT NULL,
    dietary_choice ENUM('VEG', 'NON-VEG', 'JAIN', 'SPECIAL', 'NOT_EATING') NOT NULL DEFAULT 'VEG',
    special_notes VARCHAR(255),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pref_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_pref_meal_type FOREIGN KEY (meal_type_id) REFERENCES meal_types(meal_type_id) ON DELETE CASCADE,
    CONSTRAINT uq_user_date_meal UNIQUE (user_id, preference_date, meal_type_id),
    INDEX idx_pref_date (preference_date),
    INDEX idx_pref_user (user_id),
    INDEX idx_pref_choice (dietary_choice)
) ENGINE=InnoDB;

-- =====================================================================
-- INITIAL SAMPLE DATA & DEMO ACCOUNTS
-- =====================================================================

-- Insert Default Meal Types
INSERT INTO meal_types (meal_type_id, meal_type_name, start_time, end_time, description) VALUES
(1, 'Breakfast', '07:30 AM', '09:30 AM', 'Morning breakfast with beverages'),
(2, 'Lunch',     '12:30 PM', '02:30 PM', 'Afternoon nutritious balanced lunch'),
(3, 'Snacks',    '05:00 PM', '06:30 PM', 'Evening tea/coffee with light snacks'),
(4, 'Dinner',    '07:30 PM', '09:30 PM', 'Night dinner with dessert specials');

-- Insert Demo Users
-- Admin: admin / admin123
-- Students: jitwar / student123, rahul / student123, priya / student123, aman / student123
INSERT INTO users (username, password, full_name, roll_number, role, email, contact_no, hostel_block, room_number) VALUES
('admin', 'admin123', 'Mess Head Warden (Admin)', 'ADMIN-001', 'ADMIN', 'messadmin@piet.edu.in', '9876543210', 'Admin Block', 'Office-1'),
('jitwar', 'student123', 'Jitwar Jha', 'PIET25CR022', 'STUDENT', 'jitwar.piet25cr022@piet.edu.in', '9123456780', 'Block-C', '304'),
('rahul', 'student123', 'Rahul Sharma', 'PIET25CR045', 'STUDENT', 'rahul.sharma@piet.edu.in', '9876501234', 'Block-B', '210'),
('priya', 'student123', 'Priya Verma', 'PIET25CR088', 'STUDENT', 'priya.verma@piet.edu.in', '9812345678', 'Block-G', '105'),
('aman', 'student123', 'Aman Gupta', 'PIET25CR012', 'STUDENT', 'aman.gupta@piet.edu.in', '9765432109', 'Block-A', '402');

-- Insert Sample Mess Menus for Current and Upcoming Dates
INSERT INTO mess_menu (menu_date, meal_type_id, items, dietary_type, description, created_by) VALUES
-- Today's Menu (2026-08-31)
('2026-08-31', 1, 'Aloo Paratha, Curd, Pickle, Boiled Eggs (Non-Veg option), Tea, Coffee', 'ALL', 'Fresh homemade breakfast served hot', 1),
('2026-08-31', 2, 'Paneer Butter Masala, Chicken Curry (Non-Veg option), Dal Makhani, Jeera Rice, Butter Roti, Green Salad, Gulab Jamun', 'ALL', 'Special Monday hearty lunch', 1),
('2026-08-31', 3, 'Veg Samosa, Green Chutney, Masala Chai, Filter Coffee', 'VEG', 'Crispy evening snacks', 1),
('2026-08-31', 4, 'Mix Veg Curry, Egg Curry (Non-Veg), Yellow Dal Tadka, Steamed Rice, Phulka, Papad, Ice Cream', 'ALL', 'Light and wholesome dinner', 1),

-- Tomorrow's Menu (2026-09-01)
('2026-09-01', 1, 'Masala Dosa, Sambhar, Coconut Chutney, Boiled Sprouts, Tea, Coffee', 'VEG', 'South Indian breakfast special', 1),
('2026-09-01', 2, 'Chole Bhature, Steamed Rice, Bundi Raita, Onion Salad, Rice Kheer', 'VEG', 'North Indian style feast', 1),
('2026-09-01', 3, 'Pav Bhaji, Lemon Coriander, Tea, Milk', 'VEG', 'Spiced buttery pav bhaji', 1),
('2026-09-01', 4, 'Kadhai Paneer, Butter Chicken (Non-Veg), Dal Fry, Peas Pulao, Tawa Roti, Moong Dal Halwa', 'ALL', 'Rich dinner platter', 1),

-- Day 3 Menu (2026-09-02)
('2026-09-02', 1, 'Poha with Peanuts, Sev, Boiled Eggs / Banana, Tea, Coffee', 'ALL', 'Light Maharashtrian breakfast', 1),
('2026-09-02', 2, 'Rajma Masala, Steamed Basmati Rice, Palak Paneer, Boondi Raita, Chapati', 'VEG', 'Classic Rajma Chawal special', 1),
('2026-09-02', 3, 'Veg Cutlet, Tomato Sauce, Ginger Tea, Coffee', 'VEG', 'Crunchy vegetable cutlets', 1),
('2026-09-02', 4, 'Malai Kofta, Fish Curry (Non-Veg), Dal Palak, Jeera Rice, Tandoori Roti, Rasgulla', 'ALL', 'Special royal dinner', 1);

-- Insert Sample Preferences for Students
INSERT INTO meal_preferences (user_id, preference_date, meal_type_id, dietary_choice, special_notes) VALUES
(2, '2026-08-31', 1, 'VEG', 'No tea, preferring coffee'),
(2, '2026-08-31', 2, 'NON-VEG', 'Prefer chicken curry portion'),
(2, '2026-08-31', 3, 'VEG', 'Standard'),
(2, '2026-08-31', 4, 'NON-VEG', 'With extra salad'),
(3, '2026-08-31', 1, 'VEG', 'Standard veg breakfast'),
(3, '2026-08-31', 2, 'VEG', 'Extra curd requested'),
(3, '2026-08-31', 3, 'NOT_EATING', 'Will be in computer lab'),
(3, '2026-08-31', 4, 'VEG', 'Standard'),
(4, '2026-08-31', 1, 'JAIN', 'Strictly Jain (no root vegetables)'),
(4, '2026-08-31', 2, 'JAIN', 'Jain dal and paneer'),
(4, '2026-08-31', 3, 'VEG', 'Standard'),
(4, '2026-08-31', 4, 'JAIN', 'Jain meal'),
(5, '2026-08-31', 1, 'NON-VEG', 'Boiled eggs'),
(5, '2026-08-31', 2, 'NON-VEG', 'Chicken curry'),
(5, '2026-08-31', 3, 'VEG', 'Tea only'),
(5, '2026-08-31', 4, 'NON-VEG', 'Egg curry');
