# SOURCE CODE ARCHITECTURE & DIRECTORY INDEX
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student & Project Details
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester
- **Project Title:** **MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM**
- **Technology:** Java (JDK 26 / 17+), Java Swing, JDBC, MySQL 8.0

---

### 1. Source Package Hierarchy

```
midterm1/source_code/src/
 ├── database/
 │    └── DBConnection.java         # Centralized JDBC connection pool & config manager
 │
 ├── model/
 │    ├── User.java                 # User & student data model entity
 │    ├── MealType.java             # Meal session lookup model (Breakfast/Lunch/etc)
 │    ├── MessMenu.java             # Daily & weekly food menu entity
 │    ├── MealPreference.java       # Student dietary preference entity
 │    └── PreferenceSummary.java    # Aggregated kitchen metrics entity
 │
 ├── dao/
 │    ├── UserDAO.java              # User authentication, CRUD, and role queries
 │    ├── MealTypeDAO.java          # Meal session lookup queries
 │    ├── MenuDAO.java              # Mess menu CRUD, search, and date filters
 │    └── MealPreferenceDAO.java    # Preference upsert, student history, and kitchen metrics
 │
 ├── utils/
 │    ├── ValidationUtils.java      # Input validation (email regex, phone, date parser)
 │    ├── DateUtils.java            # Date calculations, formatting, and day-of-week
 │    ├── SessionManager.java       # Global logged-in user state & authorization
 │    ├── UIConstants.java          # Modern color palette, custom anti-aliased button renderer
 │    └── TableUtils.java           # JTable alternating row renderers & badge pill renderers
 │
 ├── ui/
 │    ├── LoginFrame.java           # Role-based login window with secure navigation
 │    ├── RegisterDialog.java       # Student registration / sign-up modal dialog
 │    ├── AdminDashboard.java       # Administrator main console & real-time KPI cards
 │    ├── UserDashboard.java        # Student portal dashboard & status cards
 │    ├── UserManagementFrame.java  # Student & user CRUD + search table
 │    ├── MenuManagementFrame.java  # Mess menu scheduling & CRUD window
 │    ├── AddEditMenuDialog.java    # Add/Edit menu modal form dialog
 │    ├── MealPreferenceFrame.java  # Student daily preference selection with live menu preview
 │    ├── PreferenceHistoryFrame.java # Historical preference records & audit log
 │    ├── AdminReportFrame.java     # Kitchen headcount report & production slip generator
 │    ├── ChangePasswordDialog.java # Password modification modal dialog
 │    └── DatabaseConfigDialog.java # MySQL connection configuration & schema importer
 │
 └── Main.java                      # Application entry point with anti-aliasing initialization
```

---

### 2. Comprehensive Class Directory & Function Summary

| Package | Class Name | Responsibilities / Key Methods |
| :--- | :--- | :--- |
| **`database`** | `DBConnection.java` | `getConnection()`, `testConnection()`, `loadConfig()`, `saveConfig()`, `close()`. Loads `db.properties` dynamically. |
| **`model`** | `User.java` | POJO storing user ID, username, password, full name, roll number, role (`ADMIN`/`STUDENT`), email, contact, hostel block, room number. |
| **`model`** | `MealType.java` | POJO storing `meal_type_id`, `meal_type_name`, `start_time`, `end_time`, `description`. |
| **`model`** | `MessMenu.java` | POJO storing `menu_id`, `menu_date`, `meal_type_id`, `items`, `dietary_type`, `description`, `created_by`. |
| **`model`** | `MealPreference.java`| POJO storing `preference_id`, `user_id`, `preference_date`, `meal_type_id`, `dietary_choice`, `special_notes`. |
| **`model`** | `PreferenceSummary.java`| Aggregated metrics entity storing Veg, Non-Veg, Jain, Special, and Absent counts per session. |
| **`dao`** | `UserDAO.java` | `authenticate()`, `addUser()`, `updateUser()`, `deleteUser()`, `searchUsers()`, `changePassword()`, `isUsernameTaken()`, `isRollNumberTaken()`. |
| **`dao`** | `MealTypeDAO.java` | `getAllMealTypes()`, `getMealTypeById()`. |
| **`dao`** | `MenuDAO.java` | `addMenu()`, `updateMenu()`, `deleteMenu()`, `getMenuById()`, `getMenusByDate()`, `searchMenus()`, `existsMenu()`. |
| **`dao`** | `MealPreferenceDAO.java`| `saveOrUpdatePreference()` (Smart Upsert), `getUserPreference()`, `getUserPreferencesHistory()`, `getAllPreferences()`, `getPreferenceSummaryForDate()`. |
| **`utils`** | `ValidationUtils.java` | `isNotEmpty()`, `isValidEmail()`, `isValidPhone()`, `isValidDate()`, `parseDate()`, `formatDate()`. |
| **`utils`** | `DateUtils.java` | `today()`, `tomorrow()`, `formatDisplay()`, `formatShort()`, `getDayOfWeek()`. |
| **`utils`** | `SessionManager.java` | `setCurrentUser()`, `getCurrentUser()`, `isAdmin()`, `isStudent()`, `logout()`. |
| **`utils`** | `UIConstants.java` | Palette constants (`PRIMARY`, `ACCENT`, `COLOR_DANGER`), typography, and `CustomStyledButton` anti-aliased renderer. |
| **`utils`** | `TableUtils.java` | `AlternatingRowRenderer` (cell padding & striped rows), `BadgeCellRenderer` (colored pill tags for roles & diets). |
| **`ui`** | `LoginFrame.java` | Dual-role authentication screen, Sign In button, Sign Up button, Database settings shortcut. |
| **`ui`** | `RegisterDialog.java` | Student sign-up modal form with duplicate username and roll number validation. |
| **`ui`** | `AdminDashboard.java`| Admin control center, live KPI metrics (students, menus, preferences, DB status), today's menu preview table. |
| **`ui`** | `UserDashboard.java` | Student portal, today's preference submission status, weekly menu browser trigger. |
| **`ui`** | `UserManagementFrame.java`| Complete user CRUD with left-side form and right-side searchable JTable. |
| **`ui`** | `MenuManagementFrame.java`| Mess menu scheduling CRUD with multi-criteria search and category filters. |
| **`ui`** | `AddEditMenuDialog.java` | Modal dialog to insert/edit menu items with duplicate session check. |
| **`ui`** | `MealPreferenceFrame.java`| Student preference screen with live scheduled food item preview and radio choices. |
| **`ui`** | `PreferenceHistoryFrame.java`| Personal dining history for students; full audit log with search for admins. |
| **`ui`** | `AdminReportFrame.java`| Kitchen portion aggregation report and monospaced slip preview. |
| **`ui`** | `ChangePasswordDialog.java`| Credential modification modal with current password check and 4+ character constraint. |
| **`ui`** | `DatabaseConfigDialog.java`| Connection configuration, live ping test, and `database.sql` script importer. |
| **`root`** | `Main.java` | Main application bootstrap: sets system look & feel, checks database, and launches `LoginFrame`. |
