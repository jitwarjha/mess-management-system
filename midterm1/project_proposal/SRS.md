# SOFTWARE REQUIREMENT SPECIFICATION (SRS)
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Branch / Section:** Computer Science & Engineering / 3CSD
- **Semester:** III Semester (Session 2026-27)

---

### 1. Introduction

#### 1.1 Purpose
This Software Requirement Specification (SRS) document details the functional and non-functional requirements for the **Mess Menu & Meal Preference Management System**. It specifies interface requirements, system models, behavioral constraints, and verification criteria for Mid Term – I evaluation.

#### 1.2 System Scope
The software is an individual desktop-based administrative and student portal developed in Java Swing with a MySQL relational database connected via JDBC. It automates mess food scheduling, student dietary preference registration, and kitchen headcount aggregation to eliminate food wastage and ensure dietary customization.

---

### 2. Overall Description

#### 2.1 Product Architecture
The system employs a multi-tiered desktop architecture:
1. **Presentation Layer (Java Swing / AWT):** Graphical user interface handles input collection, window transitions, and data table rendering (`ui/`, `utils/`).
2. **Business & Validation Layer:** Performs data sanitization, duplicate checks, session authentication, and date logic (`model/`, `utils/ValidationUtils.java`, `utils/SessionManager.java`).
3. **Data Access Layer (DAO Pattern):** Encapsulates parameterized SQL execution using JDBC `PreparedStatement` (`dao/`, `database/DBConnection.java`).
4. **Database Tier (MySQL 8.0):** Normalized relational schema (`mess_management_db`) storing users, meal types, scheduled menus, and resident preferences.

```
 +---------------------------------------------------------+
 |             JAVA SWING PRESENTATION LAYER               |
 |  [LoginFrame] [AdminDashboard] [UserDashboard] [Dialogs]|
 +----------------------------+----------------------------+
                              |
 +----------------------------v----------------------------+
 |         BUSINESS LOGIC & DATA ACCESS OBJECTS (DAO)      |
 |   [UserDAO]  [MenuDAO]  [MealPreferenceDAO]  [MealType] |
 +----------------------------+----------------------------+
                              | JDBC Protocol (TCP/IP Port 3306)
 +----------------------------v----------------------------+
 |              MYSQL RELATIONAL DATABASE TIER             |
 |   `users`   `meal_types`   `mess_menu`   `meal_prefs`   |
 +---------------------------------------------------------+
```

#### 2.2 User Classes & Personas
- **Administrator (Warden / Mess Incharge):**
  - Full system administration rights.
  - Adds/updates mess food menus, manages student accounts, audits preferences, and generates kitchen production slips.
- **Student (Hostel Resident):**
  - Standard user rights.
  - Self-registers, views scheduled menus, selects daily dietary choices (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`), and reviews historical logs.

---

### 3. Functional Requirements

#### 3.1 Module 1: Authentication & Account Management
- **REQ-1.1:** System shall provide role-based authentication using `username` / `roll_number` and `password`.
- **REQ-1.2:** System shall securely redirect `ADMIN` users to the Admin Dashboard and `STUDENT` users to the Student Portal.
- **REQ-1.3:** System shall provide student self-registration (`RegisterDialog.java`) with role strictly assigned as `STUDENT`.
- **REQ-1.4:** System shall support password modification requiring verification of current credentials and a minimum 4-character new password.
- **REQ-1.5:** System shall maintain session context in `SessionManager` and allow clean session logout.

#### 3.2 Module 2: User & Resident Management (Admin)
- **REQ-2.1:** Administrator shall perform Create, Read, Update, and Delete (CRUD) operations on all student and staff accounts.
- **REQ-2.2:** System shall enforce database uniqueness on `username` and `roll_number`.
- **REQ-2.3:** System shall allow real-time multi-field search (Name, Roll No, Username, Email) and filtering by Role.
- **REQ-2.4:** Form selection on JTable shall auto-populate the input form for rapid editing.

#### 3.3 Module 3: Mess Menu Management
- **REQ-3.1:** Administrator shall schedule menus specifying Date (`DATE`), Meal Session (`meal_type_id`), Items (`TEXT`), Dietary Category (`VEG`, `NON-VEG`, `SPECIAL`, `ALL`), and Description.
- **REQ-3.2:** System shall prevent duplicate menus on the same date and meal session via the `UNIQUE(menu_date, meal_type_id)` constraint.
- **REQ-3.3:** Students and Administrators shall filter menus by keyword, date, meal session, and dietary tag.

#### 3.4 Module 4: Student Meal Preference & Smart Upsert
- **REQ-4.1:** Student shall select dietary preference (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`) for any designated date and meal session.
- **REQ-4.2:** System shall dynamically display the live scheduled kitchen menu for the selected date and session.
- **REQ-4.3:** System shall execute smart upsert logic: updating existing preferences or inserting new records without creating duplicate rows.
- **REQ-4.4:** Student shall view personal dining history with date range and session filters.

#### 3.5 Module 5: Kitchen Headcount & Production Reports
- **REQ-5.1:** System shall aggregate dietary choices per session for any target date.
- **REQ-5.2:** System shall compute portion metrics: Total Eating, Veg Count, Non-Veg Count, Jain Count, Special Count, and Absent / Not Eating Count.
- **REQ-5.3:** System shall generate formatted Kitchen Production Slips with preparation instructions ready for kitchen printing.

---

### 4. Non-Functional Requirements

- **NFR-1 (Performance):** Query execution and UI refresh shall complete in under 100 milliseconds for standard hostel volumes (< 50,000 records).
- **NFR-2 (Security):** All database operations must utilize `PreparedStatement` to eliminate SQL Injection vulnerabilities.
- **NFR-3 (Reliability & Robustness):** All SQL and runtime exceptions must be caught and presented via user-friendly `JOptionPane` dialogs without application termination.
- **NFR-4 (Usability & Design):** Interfaces shall feature high-contrast palettes, custom anti-aliased button renderers, and color-coded table badges.
- **NFR-5 (Portability):** System runs seamlessly on any OS with Java 17+ and MySQL Server.

---

### 5. Hardware & Software Requirements

| Component | Minimum Specification | Recommended Specification |
| :--- | :--- | :--- |
| **Operating System** | Windows 10 / 11, Linux, macOS | Windows 11 (64-bit) |
| **Java Environment** | Oracle JDK / OpenJDK 17 | JDK 26 / JDK 21 LTS |
| **Database Server** | MySQL Server 8.0 / MariaDB | MySQL 8.0.36 Community Server |
| **Memory (RAM)** | 4 GB | 8 GB or higher |
| **Disk Storage** | 500 MB free space | 1 GB SSD space |
| **Display Resolution**| 1024 x 768 pixels | 1920 x 1080 Full HD |
