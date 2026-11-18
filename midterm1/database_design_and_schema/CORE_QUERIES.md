# CORE SQL QUERIES SPECIFICATION
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester

---

### 1. Authentication & User Management Queries

#### 1.1 Secure User Authentication
```sql
SELECT * FROM users WHERE username = ? AND password = ?;
```
*Purpose:* Validates student/admin credentials via `PreparedStatement`.

#### 1.2 Insert New Student Account (Self-Registration)
```sql
INSERT INTO users (username, password, full_name, roll_number, role, email, contact_no, hostel_block, room_number)
VALUES (?, ?, ?, ?, 'STUDENT', ?, ?, ?, ?);
```

#### 1.3 Multi-Criteria User Search & Role Filtering
```sql
SELECT * FROM users 
WHERE (full_name LIKE ? OR username LIKE ? OR roll_number LIKE ? OR email LIKE ?)
  AND role = ?
ORDER BY role ASC, full_name ASC;
```

---

### 2. Mess Menu Management Queries

#### 2.1 Fetch All Scheduled Menus (Ordered)
```sql
SELECT m.*, mt.meal_type_name, u.full_name AS creator_name 
FROM mess_menu m 
JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id 
LEFT JOIN users u ON m.created_by = u.user_id 
ORDER BY m.menu_date DESC, m.meal_type_id ASC;
```

#### 2.2 Dynamic Menu Search & Date Filter
```sql
SELECT m.*, mt.meal_type_name, u.full_name AS creator_name 
FROM mess_menu m 
JOIN meal_types mt ON m.meal_type_id = mt.meal_type_id 
LEFT JOIN users u ON m.created_by = u.user_id 
WHERE (m.items LIKE ? OR m.description LIKE ?)
  AND m.menu_date = ?
  AND m.meal_type_id = ?
ORDER BY m.menu_date DESC, m.meal_type_id ASC;
```

#### 2.3 Duplicate Menu Prevention Check
```sql
SELECT COUNT(*) FROM mess_menu 
WHERE menu_date = ? AND meal_type_id = ? AND menu_id != ?;
```

---

### 3. Student Meal Preference Queries (Smart Upsert)

#### 3.1 Check Existing Preference
```sql
SELECT preference_id FROM meal_preferences 
WHERE user_id = ? AND preference_date = ? AND meal_type_id = ?;
```

#### 3.2 Insert Preference
```sql
INSERT INTO meal_preferences (user_id, preference_date, meal_type_id, dietary_choice, special_notes)
VALUES (?, ?, ?, ?, ?);
```

#### 3.3 Update Existing Preference
```sql
UPDATE meal_preferences 
SET dietary_choice = ?, special_notes = ? 
WHERE preference_id = ?;
```

---

### 4. Kitchen Headcount & Report Aggregation Query

#### 4.1 Grouped Headcount Aggregation (Kitchen Production Slip)
```sql
SELECT mt.meal_type_id, mt.meal_type_name,
       COUNT(p.preference_id) AS total_count,
       SUM(CASE WHEN p.dietary_choice = 'VEG' THEN 1 ELSE 0 END) AS veg_count,
       SUM(CASE WHEN p.dietary_choice = 'NON-VEG' THEN 1 ELSE 0 END) AS non_veg_count,
       SUM(CASE WHEN p.dietary_choice = 'JAIN' THEN 1 ELSE 0 END) AS jain_count,
       SUM(CASE WHEN p.dietary_choice = 'SPECIAL' THEN 1 ELSE 0 END) AS special_count,
       SUM(CASE WHEN p.dietary_choice = 'NOT_EATING' THEN 1 ELSE 0 END) AS not_eating_count
FROM meal_types mt
LEFT JOIN meal_preferences p ON mt.meal_type_id = p.meal_type_id AND p.preference_date = ?
GROUP BY mt.meal_type_id, mt.meal_type_name
ORDER BY mt.meal_type_id ASC;
```
*Purpose:* Computes exact kitchen portion requirements (Veg, Non-Veg, Jain, Special, Absent) for any designated date.
