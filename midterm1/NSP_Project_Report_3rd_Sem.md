# A SOFTWARE REQUIREMENT SPECIFICATION ON
# MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM
### UNDER MINI PROJECT LAB (NON-SYLLABUS PROJECT)

```
                            [ POORNIMA LOGO ]
```

**Submitted To:**  
Faculty Coordinator / Evaluator  
Assistant Professor  

**Submitted By:**  
**JITWAR JHA**  
University Roll No: **PIET25CR022**  
Section: 3CSD | Semester: III  

**DEPARTMENT OF COMPUTER ENGINEERING**  
**POORNIMA INSTITUTE OF ENGINEERING & TECHNOLOGY, JAIPUR**  
**Academic Year (2026-27)**

---

## Chapter 1: Introduction of Project

### 1.1 Objective of Project
The primary objective of developing the **Mess Menu & Meal Preference Management System** is to streamline, digitize, and automate hostel mess dining operations in collegiate environments. Traditional hostel dining operations face significant operational inefficiencies, primarily resulting from manual notice-board-based menu distribution and rough guess-based cooking estimations. These practices frequently lead to massive food wastage when students unexpectedly dine out or skip meals, or conversely, cause acute food shortages during high-attendance sessions.

The software aims to solve these core challenges by:
1. **Centralized Menu Broadcasting:** Providing a digital portal for mess administrators to schedule, manage, and broadcast daily and weekly dining menus categorized by meal sessions (Breakfast, Lunch, Snacks, Dinner) and dietary classes (Veg, Non-Veg, Special, All).
2. **Student Dietary Customization:** Providing an interactive student portal that allows hostel residents to view published menus in real-time and log their exact dietary preferences (Pure Veg, Non-Veg, Jain Meal, Special Medical Diet, or Not Eating / Absent) in advance.
3. **Automated Headcount Portions:** Generating real-time automated kitchen headcount reports and portion slips that provide kitchen chefs with exact portion counts, eliminating food wastage, respecting strict dietary preferences (such as root-vegetable-free Jain preparation), and ensuring optimal operational cost efficiency.

### 1.2 Types of Users
The system supports two primary distinct user roles with strict Role-Based Access Control (RBAC):
1. **Administrator (Admin):** Mess Wardens, Hostel Incharges, and Mess Administrative Staff. Administrators have full system privileges including user account management (CRUD), daily/weekly mess menu scheduling (CRUD), resident preference auditing, and real-time kitchen headcount slip generation.
2. **Student / Resident (User):** Hostel Residents and College Students residing in campus hostels. Students have dedicated portal privileges including account self-registration (Sign-Up), viewing published daily/weekly menus, registering and updating their dietary preferences per meal session, and reviewing their personal meal history logs.

---

## Chapter 2: Requirement Analysis

### 2.1 Functional Requirement
The system encompasses the following comprehensive functional requirements:
- **Authentication & Authorization:** Secure role-based login for Admins and Students with credentials authenticated against the MySQL database. Includes student self-registration with duplicate username/roll number prevention.
- **Interactive Dashboard:** Dedicated consoles for Administrators and Students displaying real-time metrics, active session counts, and live preview of today's food schedule.
- **User Management (Admin):** Full Create, Read, Update, Delete, and Search operations for student accounts with instant table selection and multi-criteria filters.
- **Mess Menu Management:** Scheduling and updating food menus across Breakfast, Lunch, Snacks, and Dinner with unique date-session constraints to eliminate duplicate records.
- **Meal Preference Portal:** Students can view what's cooking in the kitchen and submit their choices (Veg, Non-Veg, Jain, Special, Not Eating) using smart upsert logic.
- **Headcount Reporting:** Date-wise aggregation of meal portion counts generating formatted Kitchen Production Slips for kitchen chefs.
- **Security & Password Management:** Verification of current credentials with a minimum 4-character new password rule.
- **Database Configuration:** Graphical dialog allowing examiners and administrators to test and reconfigure MySQL connection settings dynamically.

### 2.2 Technology Used
The application is built using standard, robust desktop technologies:
- **Programming Language:** Java (JDK 26 / JDK 17+) — Core object-oriented programming language providing platform independence, multithreading, and strong type safety.
- **GUI Framework:** Java Swing & AWT (`javax.swing.*`, `java.awt.*`) — Native Java Graphical User Interface library utilized for all frames, dialogs, custom table cell renderers, and high-contrast styling.
- **Database Management System:** MySQL Community Server 8.0 — Relational Database Management System providing ACID transactions, 3NF normalized tables, and integrity constraints.
- **Database Connectivity:** JDBC API (MySQL Connector/J 8.3.0) — Official Type-4 JDBC driver providing parameterized PreparedStatement execution to eliminate SQL Injection.
- **Development Environment:** IntelliJ IDEA Community Edition — Integrated Development Environment used for project development, compilation, and debugging.
- **Operating System:** Windows 10 / Windows 11 (Cross-Platform Compatible).

### 2.3 Graphical User Interface
The Graphical User Interface is structured into clean, modular, and responsive desktop windows:
1. **Login & Registration Screen (`LoginFrame.java`, `RegisterDialog.java`):** Dual-panel interface featuring branding credentials on the left and secure login/registration controls on the right.
2. **Administrator Dashboard (`AdminDashboard.java`):** Features 4 real-time KPI metric cards, navigation action buttons, quick toolbar, and a live today's mess menu board.
3. **Student Portal (`UserDashboard.java`):** Displays personalized resident greeting, today's submission status badge, quick actions, and full today's menu overview.
4. **User Management Window (`UserManagementFrame.java`):** Dual-pane window featuring an input form on the left and a searchable, role-filtered JTable on the right.
5. **Mess Menu Management (`MenuManagementFrame.java`, `AddEditMenuDialog.java`):** Comprehensive scheduling table with search bar, date filters, dietary categories, and Add/Edit modal dialogs.
6. **Meal Preference Portal (`MealPreferenceFrame.java`):** Interactive selection screen displaying a live kitchen menu preview alongside dietary radio options (Veg, Non-Veg, Jain, Special, Not Eating).
7. **Preference History & Audit (`PreferenceHistoryFrame.java`):** Searchable table for auditing past resident preferences with summary count dialogs.
8. **Kitchen Headcount Report (`AdminReportFrame.java`):** Aggregated kitchen portions table and monospaced Kitchen Production Slip generator.

---

## Chapter 3: Design

### 3.1 Use Case Diagram

```
                    +-------------------------------------------------------------+
                    |       MESS MENU & PREFERENCE MANAGEMENT SYSTEM BOUNDARY     |
                    |                                                             |
  +---------------+ |   (UC-01: Authenticate & Login) <------------------+        | +-------------+
  |               | |   (UC-02: Self-Register Student) <------------------------+ | |             |
  |               | |   (UC-03: View Daily / Weekly Food Menu) <---------+------+ | |             |
  |               | |   (UC-04: Select / Update Meal Preference) <-------+        | |   STUDENT   |
  | ADMINISTRATOR | |   (UC-05: View Personal Preference History) <------+        | |   (User)    |
  |    (Warden)   | |   (UC-06: Manage Student & Staff Users [CRUD])     |        | |             |
  |               | |   (UC-07: Schedule & Manage Food Menus [CRUD])     |        | +-------------+
  |               | |   (UC-08: Audit Resident Meal Preferences)         |        |
  |               | |   (UC-09: Generate Kitchen Headcount Slips)        |        |
  |               | |   (UC-10: Update Account Password) <---------------+--------+
  |               | |   (UC-11: Configure Database Connection)           |        |
  +---------------+ +-------------------------------------------------------------+
```

**Brief Explanation:** The Administrator actor is responsible for administrative use cases (UC-06, UC-07, UC-08, UC-09, UC-11), while the Student actor interacts with resident use cases (UC-02, UC-03, UC-04, UC-05). Both actors share common security use cases (UC-01 Login, UC-10 Change Password).

### 3.2 Data Base Tables
The relational database `mess_management_db` contains 4 normalized tables (3NF):

#### Table 3.1: `users` (Stores Administrator and Student Accounts)
| Field Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `user_id` | `INT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique User Identifier |
| `username` | `VARCHAR(50)` | `NOT NULL, UNIQUE` | Unique Login Username |
| `password` | `VARCHAR(255)` | `NOT NULL` | Account Access Password |
| `full_name` | `VARCHAR(100)` | `NOT NULL` | Full Name of Resident/Warden |
| `roll_number` | `VARCHAR(50)` | `UNIQUE, NULL` | Student College Roll Number |
| `role` | `ENUM('ADMIN','STUDENT')` | `NOT NULL, DEFAULT 'STUDENT'` | Access Authorization Level |
| `email` | `VARCHAR(100)` | `NULL` | Contact Email Address |
| `contact_no` | `VARCHAR(20)` | `NULL` | Mobile Contact Number |
| `hostel_block` | `VARCHAR(30)` | `DEFAULT 'Block-A'` | Assigned Hostel Block |
| `room_number` | `VARCHAR(20)` | `DEFAULT '101'` | Assigned Room Number |

#### Table 3.2: `meal_types` (Stores Standard Meal Sessions)
| Field Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `meal_type_id` | `INT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique Session Identifier |
| `meal_type_name` | `VARCHAR(30)` | `NOT NULL, UNIQUE` | Breakfast, Lunch, Snacks, Dinner |
| `start_time` | `VARCHAR(20)` | `NOT NULL` | Serving Window Start Time |
| `end_time` | `VARCHAR(20)` | `NOT NULL` | Serving Window End Time |

#### Table 3.3: `mess_menu` (Stores Scheduled Food Items)
| Field Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `menu_id` | `INT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique Menu Identifier |
| `menu_date` | `DATE` | `NOT NULL` | Scheduled Calendar Date |
| `meal_type_id` | `INT` | `FOREIGN KEY (meal_types)` | Meal Session Reference |
| `items` | `TEXT` | `NOT NULL` | Food Dishes Served |
| `dietary_type` | `ENUM('VEG','NON-VEG','SPECIAL','ALL')` | `NOT NULL` | Primary Dietary Tag |
| `created_by` | `INT` | `FOREIGN KEY (users)` | Admin Account Reference |

#### Table 3.4: `meal_preferences` (Stores Student Dietary Selections)
| Field Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `preference_id` | `INT` | `PRIMARY KEY, AUTO_INCREMENT` | Unique Preference Record ID |
| `user_id` | `INT` | `FOREIGN KEY (users)` | Resident Student Reference |
| `preference_date` | `DATE` | `NOT NULL` | Target Meal Date |
| `meal_type_id` | `INT` | `FOREIGN KEY (meal_types)` | Meal Session Reference |
| `dietary_choice` | `ENUM('VEG','NON-VEG','JAIN','SPECIAL','NOT_EATING')` | `NOT NULL` | Student Dietary Selection |
| `special_notes` | `VARCHAR(255)` | `NULL` | Optional Resident Instructions |

---

## Chapter 4: Conclusion

Through the successful development of the **Mess Menu & Meal Preference Management System**, a complete, end-to-end desktop software solution has been established to revolutionize collegiate hostel mess administration. The system successfully replaces prone-to-error manual record books and physical notice boards with an intuitive, real-time Java Swing application backed by an ACID-compliant MySQL database.

**Key Achievements of the Project include:**
1. **Elimination of Estimation Errors:** Automated daily headcount calculations eliminate food wastage and prevent sudden meal shortages.
2. **Support for Strict Dietary Restrictions:** Students observing religious or medical diets (e.g. root-vegetable-free Jain meals) receive guaranteed customized preparation.
3. **Robust Security & Architecture:** Clean Data Access Object (DAO) pattern with `PreparedStatement` parameterized queries ensures zero SQL Injection vulnerabilities.
4. **Intuitive High-Contrast UI:** Custom anti-aliased button renderers and color-coded table badges ensure optimal usability for both students and kitchen staff.

In conclusion, the project meets all functional, architectural, and quality benchmarks prescribed in the official Minor Project NSP curriculum, providing a robust software asset ready for live hostel deployment.

---

## Chapter 5: Snapshots of Your project

- **Figure 5.1: Login Page** — Features dual-panel layout with institutional branding on the left and high-contrast 'Sign In' and 'Sign Up' controls on the right.
- **Figure 5.2: Student Registration Page** — Modal dialog allowing new residents to register with Full Name, Roll No, Username, Password, Room No, and Contact details.
- **Figure 5.3: Administrator Dashboard** — Displays live KPI cards (Total Students, Published Menus, Today's Preferences), quick navigation buttons, and today's live menu board.
- **Figure 5.4: Student Portal Dashboard** — Presents personalized resident greeting, today's submission status badge, and scheduled food dishes overview.
- **Figure 5.5: User Management Page** — Enables full CRUD operations for resident accounts with instant form auto-population and role filtering.
- **Figure 5.6: Mess Menu Management Page** — Comprehensive menu scheduling table with multi-criteria search, date filters, and dietary tags.
- **Figure 5.7: Meal Preference Selection Page** — Displays live scheduled kitchen food items alongside dietary options (Veg, Non-Veg, Jain, Special, Not Eating).
- **Figure 5.8: Preference History & Audit Page** — Searchable audit log of student preference submissions with summary breakdown metrics.
- **Figure 5.9: Kitchen Headcount Report & Slip** — Aggregated kitchen portions table and printable monospaced Kitchen Production Slip generator.
- **Figure 5.10: Change Password Dialog** — Modal dialog providing credential verification and minimum 4-character new password rule.
- **Figure 5.11: Database Configuration Dialog** — Enables dynamic MySQL connection testing, credential updates, and one-click database.sql schema importing.
