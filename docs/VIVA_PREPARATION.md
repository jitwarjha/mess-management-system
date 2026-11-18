# Comprehensive Viva Voce & Technical Defense Guide
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Session 2026-27**

---

### Student & Examination Details
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester
- **Subject:** Minor Project NSP
- **Project Title:** MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM

---

### 1. High-Frequency Viva Questions & Model Answers

#### Q1. What is the core objective of your Minor Project?
**Answer:** The *Mess Menu & Meal Preference Management System* is a desktop application designed to streamline hostel mess food operations. It solves two major problems:
1. Providing an accessible platform for students to view scheduled daily/weekly menus and submit dietary choices (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`) in advance.
2. Providing mess management and kitchen chefs with real-time aggregated headcounts per meal session, preventing food wastage and ensuring tailored meal preparation (e.g., separate Jain cooking).

---

#### Q2. Explain the software architecture and package structure of your project.
**Answer:** The project adheres to a clean, modular **Layered Architecture** following the **Data Access Object (DAO)** design pattern:
- `database/`: Manages database configuration (`db.properties`) and centralized connection handling (`DBConnection.java`).
- `model/`: Contains Plain Old Java Objects (POJOs) representing domain entities (`User`, `MealType`, `MessMenu`, `MealPreference`, `PreferenceSummary`).
- `dao/`: Encapsulates all SQL queries and JDBC database operations (`UserDAO`, `MealTypeDAO`, `MenuDAO`, `MealPreferenceDAO`).
- `ui/`: Houses all Java Swing user interface frames, dashboards, and modal dialogs.
- `utils/`: Reusable validation (`ValidationUtils`), date operations (`DateUtils`), session tracking (`SessionManager`), and UI styling (`UIConstants`, `TableUtils`).
- `Main.java`: Application entry point initializing Look & Feel and launching the login screen.

---

#### Q3. What is JDBC and what are the standard steps involved in connecting Java to MySQL?
**Answer:** JDBC (Java Database Connectivity) is an API provided in `java.sql` allowing Java applications to interact with relational databases. The four fundamental steps are:
1. **Load JDBC Driver:** `Class.forName("com.mysql.cj.jdbc.Driver")` loads the MySQL driver bytecode into JVM memory.
2. **Establish Connection:** `DriverManager.getConnection(url, user, password)` opens a TCP socket connection to MySQL.
3. **Execute SQL via PreparedStatement:** `conn.prepareStatement(sql)` compiles and parameterizes the query.
4. **Process ResultSet & Safe Close:** Traversing `rs.next()` and closing resources in reverse order (`ResultSet` -> `Statement` -> `Connection`) using Java 7+ *try-with-resources*.

---

#### Q4. Why did you use `PreparedStatement` instead of `Statement`?
**Answer:**
1. **Security (SQL Injection Prevention):** `PreparedStatement` automatically sanitizes inputs by treating user values as pure data parameters rather than executable SQL fragments.
2. **Performance (Pre-compilation):** The database server parses, compiles, and optimizes the SQL execution plan once, making parameterized batch executions faster.
3. **Clean Syntax:** Eliminates messy string concatenations with quotes (`'"`).

---

#### Q5. Explain your database schema and normalization level.
**Answer:** The database `mess_management_db` is normalized up to **Third Normal Form (3NF)**:
1. `users`: Stores user accounts with `user_id` (PK), unique `username`, unique `roll_number`, and `role` (`ADMIN`/`STUDENT`).
2. `meal_types`: Lookup table defining Breakfast, Lunch, Snacks, Dinner and timings.
3. `mess_menu`: Stores scheduled items referencing `meal_types(meal_type_id)` and `users(created_by)` with a `UNIQUE(menu_date, meal_type_id)` constraint.
4. `meal_preferences`: Stores resident choices referencing `users(user_id)` and `meal_types(meal_type_id)` with a `UNIQUE(user_id, preference_date, meal_type_id)` constraint.

*Normalization Justification:* Atomic columns satisfy 1NF, full functional dependency on primary keys satisfies 2NF, and elimination of transitive dependencies (e.g. separating meal session timings into `meal_types`) satisfies 3NF.

---

#### Q6. What are the referential integrity actions used in your Foreign Keys?
**Answer:**
- In `mess_menu`: `CONSTRAINT fk_menu_meal_type FOREIGN KEY (...) ON DELETE CASCADE`. If a meal type is deleted, associated menus cascade.
- In `mess_menu`: `CONSTRAINT fk_menu_creator FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL`. If an admin account is deleted, historical menus are preserved with creator set to NULL.
- In `meal_preferences`: `CONSTRAINT fk_pref_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE`. If a student leaves the hostel, their preference records are cleaned up.

---

#### Q7. How does the "Upsert" logic work in your Meal Preference module?
**Answer:** When a student submits a meal preference:
1. The DAO queries `SELECT preference_id FROM meal_preferences WHERE user_id = ? AND preference_date = ? AND meal_type_id = ?`.
2. If a record is found, it performs an `UPDATE meal_preferences SET dietary_choice = ?, special_notes = ? WHERE preference_id = ?`.
3. If no record exists, it executes an `INSERT INTO meal_preferences (...) VALUES (...)`.
This ensures students can freely change their preference before meal time without creating duplicate database rows.

---

#### Q8. What is the Event Dispatch Thread (EDT) in Java Swing?
**Answer:** Java Swing is a single-threaded GUI toolkit. All component painting, layout computations, and event handling (button clicks, key presses) occur on a dedicated thread called the **Event Dispatch Thread (EDT)**. We use `SwingUtilities.invokeLater(Runnable)` in `Main.java` to ensure GUI creation starts safely on the EDT, preventing concurrency and race condition issues.

---

#### Q9. How did you customize `JTable` rendering in your application?
**Answer:** We created custom table cell renderers in `TableUtils.java`:
- `AlternatingRowRenderer`: Enhances readability by alternating background colors between pure white (`#FFFFFF`) and slate (`#F8FAFC`) with cell padding.
- `BadgeCellRenderer`: Extends `DefaultTableCellRenderer` to render color-coded pill badges for roles (`ADMIN` in indigo, `STUDENT` in green) and dietary choices (`VEG` in green, `NON-VEG` in rose red, `JAIN` in yellow, `NOT_EATING` in slate gray).

---

#### Q10. What validations are implemented across your forms?
**Answer:** In `ValidationUtils.java` and UI listeners:
1. **Empty Field Checks:** Mandatory fields (names, dates, items) cannot be blank.
2. **Duplicate Detection:** Checks if `username` or `roll_number` already exists before database insertion.
3. **Duplicate Menu Check:** Prevents creating duplicate menus for the same date and meal type.
4. **Format Validations:** Regex validation for email addresses (`^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`) and contact numbers (10–12 digits).
5. **Date Parsing:** Strict date validation for `DD-MM-YYYY`.
6. **Password Verification:** Minimum 4-character length check and password confirmation matching.

---

### 2. Quick Viva Cheat Sheet

| Topic | Key Term | Explanation |
| :--- | :--- | :--- |
| **GUI Framework** | Java Swing | Desktop GUI library built on top of AWT with lightweight components. |
| **Model View Separation** | DAO Pattern | Decouples business logic/UI from database queries. |
| **Database Security** | `PreparedStatement` | Prevents SQL Injection via parameter substitution. |
| **Data Integrity** | Foreign Key + Constraints | Enforces relational rules and prevents orphaned records. |
| **Session Control** | `SessionManager` | Static session container storing authenticated user object. |
| **Data Representation** | `DefaultTableModel` | Model layer backing `JTable` for dynamic row addition/removal. |
| **Error Handling** | `try-catch` + `JOptionPane` | Graceful database error handling without runtime termination. |
