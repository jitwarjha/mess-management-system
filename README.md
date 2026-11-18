# Mess Menu & Meal Preference Management System
### Minor Project NSP (Session 2026-27)
**Department of Computer Science & Engineering**  
**Poornima Institute of Engineering & Technology, Jaipur**

---

### Student & Project Credentials
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section:** 3CSD
- **Semester:** III
- **Project Type:** Individual Minor Project (NSP)
- **Project Title:** **MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM**
- **Technology Stack:** Java (JDK 26 / 17+), Java Swing, JDBC, MySQL 8.0, IntelliJ IDEA

---

## 📌 Project Overview
The **Mess Menu & Meal Preference Management System** is an enterprise-grade desktop software application built to modernize institutional hostel mess operations. It facilitates seamless daily and weekly menu publication by administrators while empowering hostel students to view scheduled meal menus and specify their dietary preferences (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`).

The system aggregates dietary selections in real time, delivering precise kitchen headcount reports and production slips to eliminate food wastage and streamline hostel food services.

---

## ✨ Key Features & Modules

### 1. Role-Based Authentication & Access Control
- **Dual User Roles:** `ADMIN` (Warden/Mess Incharge) and `STUDENT` (Resident).
- **Secure MySQL Authentication:** Validates login credentials via `PreparedStatement`.
- **Session Management:** Centralized session controller (`SessionManager`) with clean logout and password update features.

### 2. Administrator Dashboard & Controls
- **Live KPI Metrics:** Displays real-time counts for registered students, published menus, today's submitted preferences, and database status.
- **Today's Live Menu Board:** Interactive table previewing active meal sessions.
- **Quick Action Toolbar & Menu Bar:** Rapid navigation across all administrative operations.

### 3. User & Student Management (CRUD)
- **Full CRUD:** Add new student/admin accounts, update profile details, delete users, and search by Name, Roll No, Username, or Email.
- **Integrity Constraints:** Automatic validation for duplicate usernames and roll numbers.

### 4. Mess Menu Management (CRUD)
- **Meal Sessions:** Breakfast, Lunch, Snacks, and Dinner scheduling with dietary categories (`VEG`, `NON-VEG`, `SPECIAL`, `ALL`).
- **Conflict Prevention:** Database unique constraint on `(menu_date, meal_type_id)` prevents duplicate meal entries.
- **Filtering & Search:** Real-time date and meal session filters.

### 5. Student Meal Preference Portal
- **Interactive Menu Preview:** Selecting any date and meal session dynamically fetches and previews the kitchen's scheduled food items.
- **Dietary Choices:** Pure Veg, Non-Veg, Jain (zero root veggies), Special, or Not Eating / Absent.
- **Smart Upsert:** Add or modify dietary preference anytime before the session cut-off.

### 6. Kitchen Headcount & Production Reports
- **Real-Time Aggregation:** Computes exact portion quantities (Veg, Non-Veg, Jain, Special, Not Eating) per meal session for any calendar date.
- **Kitchen Production Slip:** Generates a formatted, printable slip for mess chefs.

### 7. Database Configuration Utility
- **Dynamic Connection:** Allows users/examiners to test and configure MySQL port/password without altering Java code.
- **Direct Schema Importer:** One-click execution of `database.sql`.

---

## 🛠️ Technology Stack & Libraries
- **Language:** Java Standard Edition (JDK 26 / OpenJDK 17+)
- **GUI Toolkit:** Java Swing (`javax.swing.*`, `java.awt.*`)
- **Database:** MySQL Community Server 8.0
- **Connectivity:** JDBC API (`java.sql.*`)
- **JDBC Driver:** MySQL Connector/J 8.3.0 (`lib/mysql-connector-j-8.3.0.jar`)
- **IDE:** IntelliJ IDEA Community / Ultimate Edition

---

## 🗂️ Project Directory Structure

```
NSP project/
 ├── database.sql               # Complete MySQL schema & initial sample records
 ├── db.properties              # Configurable MySQL host, port, user, and password
 ├── 14_WEEK_PLAN.md            # Official 14-week curriculum progression document
 ├── README.md                  # Main documentation & setup guide
 ├── NSP_Project.iml            # IntelliJ IDEA module definition
 ├── compile.bat                # Windows 1-click compilation script
 ├── run.bat                    # Windows 1-click application launcher
 │
 ├── lib/
 │    └── mysql-connector-j-8.3.0.jar   # MySQL JDBC Driver library
 │
 ├── docs/
 │    ├── 14_WEEK_PLAN.md               # 14-week progression schedule
 │    ├── PROJECT_OVERVIEW.md           # Executive summary & domain analysis
 │    ├── SRS.md                        # Software Requirements Specification
 │    ├── DATABASE_DESIGN.md            # ER diagrams & schema dictionaries
 │    ├── FLOWCHARTS_AND_DIAGRAMS.md    # System flowcharts (Mermaid)
 │    └── VIVA_PREPARATION.md           # Viva Voce questions & technical defense
 │
 └── src/
      ├── database/
      │    └── DBConnection.java        # Centralized JDBC connection manager
      │
      ├── model/
      │    ├── User.java                # User & student entity
      │    ├── MealType.java            # Meal session lookup entity
      │    ├── MessMenu.java            # Food menu entity
      │    ├── MealPreference.java      # Student preference entity
      │    └── PreferenceSummary.java   # Aggregated kitchen metrics entity
      │
      ├── dao/
      │    ├── UserDAO.java             # User CRUD & authentication
      │    ├── MealTypeDAO.java         # Meal session queries
      │    ├── MenuDAO.java             # Mess menu CRUD & filters
      │    └── MealPreferenceDAO.java   # Preference upsert & report summaries
      │
      ├── utils/
      │    ├── ValidationUtils.java     # Form validation routines
      │    ├── DateUtils.java           # Date formatters & helpers
      │    ├── SessionManager.java      # Logged-in user state
      │    ├── UIConstants.java         # Design system & color palette
      │    └── TableUtils.java          # Custom cell & badge renderers
      │
      ├── ui/
      │    ├── LoginFrame.java          # Login interface
      │    ├── AdminDashboard.java      # Administrator dashboard
      │    ├── UserDashboard.java       # Student portal dashboard
      │    ├── UserManagementFrame.java # Student & user CRUD window
      │    ├── MenuManagementFrame.java # Menu scheduling & CRUD window
      │    ├── AddEditMenuDialog.java   # Add/Edit menu modal dialog
      │    ├── MealPreferenceFrame.java # Student preference selection window
      │    ├── PreferenceHistoryFrame.java # Historical preference records
      │    ├── AdminReportFrame.java    # Kitchen headcount report window
      │    ├── ChangePasswordDialog.java# Password update modal dialog
      │    └── DatabaseConfigDialog.java# MySQL settings & test dialog
      │
      └── Main.java                     # Application entry point
```

---

## 🚀 Setup & Execution Guide

### Step 1: Database Setup in MySQL
1. Ensure your **MySQL Server** is running (e.g., via MySQL Workbench, XAMPP, or command line).
2. Open MySQL Workbench or command line and execute `database.sql`:
   ```sql
   SOURCE c:/Users/HP/OneDrive/Documents/NSP project/database.sql;
   ```
   *(Or click **"⚙ Database Settings"** on the Login screen and click **"Import database.sql"**)*

### Step 2: Configure MySQL Credentials (If needed)
If your MySQL password is not blank, edit `db.properties` or use the GUI settings dialog:
```properties
db.host=localhost
db.port=3306
db.name=mess_management_db
db.user=root
db.password=your_password
```

### Step 3: Run the Application
#### Method A: Using IntelliJ IDEA (Recommended)
1. Open IntelliJ IDEA and select **Open** -> Select `NSP project` folder.
2. Ensure Project SDK is set to **Java 17+** (File -> Project Structure -> Project SDK).
3. Right-click on `src/Main.java` and click **Run 'Main.main()'**.

#### Method B: Using Windows 1-Click Scripts
- Double-click **`run.bat`** (or `compile.bat` then `run.bat`).

#### Method C: Using Terminal / Command Line
```powershell
javac -cp ".;lib/mysql-connector-j-8.3.0.jar;src" -d bin src/database/*.java src/model/*.java src/dao/*.java src/utils/*.java src/ui/*.java src/Main.java
java -cp ".;bin;lib/mysql-connector-j-8.3.0.jar;src" Main
```

---

## 🔑 Default Demo Accounts

| Role | Username | Password | Full Name / Details |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | Mess Head Warden (Admin Office) |
| **STUDENT** | `jitwar` | `student123` | Jitwar Jha (Roll: PIET25CR022, Block-C / 304) |
| **STUDENT** | `rahul` | `student123` | Rahul Sharma (Roll: PIET25CR045, Block-B / 210) |
| **STUDENT** | `priya` | `student123` | Priya Verma (Roll: PIET25CR088, Block-G / 105) |
| **STUDENT** | `aman` | `student123` | Aman Gupta (Roll: PIET25CR012, Block-A / 402) |

*(Note: The Login screen includes quick **"Fill Admin"** and **"Fill Student"** demo buttons for instant viva demonstrations).*

---

## 📋 Evaluation Marking Rubric Alignment

| Assessment Component | Marks | Implementation in Project |
| :--- | :---: | :--- |
| **Project Design & SRS** | 20 | Complete `docs/SRS.md`, `docs/PROJECT_OVERVIEW.md`, Flowcharts |
| **GUI Prototype & Design** | 20 | Fully cohesive Swing UI, `UIConstants`, Custom Table Badges |
| **Database Design & SQL** | 20 | 3NF normalized schema, foreign keys, `database.sql`, sample data |
| **CRUD & JDBC Operations** | 20 | Complete User/Menu/Preference DAOs with `PreparedStatement` |
| **Code Quality & Validation** | 20 | Modular OOP, `ValidationUtils`, no hardcoded queries, safe resource closure |
| **Final Demonstration** | 20 | Fully executable, zero placeholder code, runnable in one go |
| **Weekly Log & 14-Week Plan** | 60 | Detailed `14_WEEK_PLAN.md` following official college guidelines |
| **External Exam & Viva Voce** | 40 | Thorough preparation guide in `docs/VIVA_PREPARATION.md` |
| **TOTAL** | **200 / 200** | **Maximum Grade Target** |
