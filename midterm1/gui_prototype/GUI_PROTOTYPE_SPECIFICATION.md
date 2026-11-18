# GUI PROTOTYPE SPECIFICATION
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student & Project Credentials
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester
- **Project Title:** **MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM**

---

### 1. Design System & GUI Standards
The user interface is engineered exclusively in **Java Swing** with custom-rendered components in `UIConstants.java` ensuring high-contrast readability, polished typography, and responsive layouts across platforms.

#### Color Palette
- **Primary Navy Blue (`#183176`):** Header banners, primary buttons, window titles.
- **Secondary Teal (`#0D9488`):** Secondary actions, interactive toggles, highlights.
- **Danger Crimson (`#B91C1C`):** Delete actions, logout buttons, error badges.
- **Light Slate Background (`#F1F5F9`):** Application canvas background.
- **Card Background (`#FFFFFF`):** Form containers and table panels.
- **Border Slate (`#94A3B8` / `#CBD5E1`):** High-contrast component borders.

---

### 2. Comprehensive Screen Inventory

| Screen ID | Frame / Dialog Class | Purpose & Access Level | Layout Manager |
| :--- | :--- | :--- | :--- |
| **SCR-01** | `LoginFrame.java` | Role-based system authentication | `BorderLayout` + `GridBagLayout` |
| **SCR-02** | `RegisterDialog.java` | Student self-registration / sign-up | `BorderLayout` + `GridBagLayout` |
| **SCR-03** | `AdminDashboard.java` | Administrator main console & metrics | `BorderLayout` + `GridLayout` |
| **SCR-04** | `UserDashboard.java` | Student portal & daily status overview | `BorderLayout` + `GridLayout` |
| **SCR-05** | `UserManagementFrame.java` | Student & admin account CRUD + search | `BorderLayout` + `GridBagLayout` |
| **SCR-06** | `MenuManagementFrame.java` | Daily/weekly menu scheduling CRUD | `BorderLayout` + `FlowLayout` |
| **SCR-07** | `AddEditMenuDialog.java` | Modal form to create/modify menu items | `BorderLayout` + `GridBagLayout` |
| **SCR-08** | `MealPreferenceFrame.java` | Student meal selection with live menu | `BorderLayout` + `GridLayout` |
| **SCR-09** | `PreferenceHistoryFrame.java` | Personal history & admin audit records | `BorderLayout` + `FlowLayout` |
| **SCR-10** | `AdminReportFrame.java` | Kitchen headcount & production slips | `BorderLayout` + `GridLayout` |
| **SCR-11** | `ChangePasswordDialog.java`| Credential modification dialog | `BorderLayout` + `GridBagLayout` |
| **SCR-12** | `DatabaseConfigDialog.java`| MySQL connection settings & schema importer | `BorderLayout` + `GridBagLayout` |

---

### 3. Screen Specifications & Structural Layouts

#### 3.1 Screen SCR-01: `LoginFrame.java`
- **Left Branding Panel:** Displays system title, college credentials ("Poornima Institute of Engineering & Technology"), Minor Project NSP badge, and student details ("JITWAR JHA, PIET25CR022, 3CSD").
- **Right Form Panel:**
  - `JTextField` for Username / Roll Number.
  - `JPasswordField` for Password.
  - `CustomStyledButton` for **`🔑 Sign In / Login`** (Primary).
  - `CustomStyledButton` for **`📝 New Student? Sign Up / Register`** (Outline).
  - Bottom Shortcut: **`⚙ Database Settings`** (Secondary).

#### 3.2 Screen SCR-02: `RegisterDialog.java`
- Modal dialog allowing new residents to register.
- Input Fields: Full Name, Roll Number, Username, Password, Confirm Password, Email, Contact No, Hostel Block dropdown, Room Number.
- Automatic constraint validation against duplicate usernames and roll numbers.

#### 3.3 Screen SCR-03: `AdminDashboard.java`
- **Header:** System title, logged-in admin name, and live status badge.
- **Top Metrics Row (4 KPI Cards):**
  1. *Total Registered Students* (Live query from `users` table).
  2. *Today's Scheduled Menus* (Counts published sessions: e.g. 4/4).
  3. *Preferences Logged Today* (Live count of resident choices).
  4. *Database Connection Status* (`CONNECTED` in green).
- **Navigation Module Cards:** User Management, Menu Scheduling, Meal Preferences Audit, Kitchen Headcount Report, Database Settings.
- **Live Today's Menu Board:** Real-time table displaying Breakfast, Lunch, Snacks, and Dinner food items.

#### 3.4 Screen SCR-04: `UserDashboard.java`
- **Header:** Personalized greeting with student name, roll number, hostel block, and room number.
- **Summary Cards:** Today's Date, My Preference Status (e.g. `4 / 4 Meals Recorded`), and Total Historical Submissions.
- **Quick Actions:** Select Meal Preference, View Full Weekly Schedule, My Preference History, Change Password, Logout.
- **Interactive Today's Menu JTable:** Displays scheduled food dishes and dietary tags.

#### 3.5 Screen SCR-05: `UserManagementFrame.java`
- **Left Side Form:** Input fields for ID, Name, Roll No, Username, Password, Role (`STUDENT`/`ADMIN`), Email, Contact, Hostel Block, Room Number. Action buttons: `+ Add User`, `✎ Update`, `🗑 Delete`, `Clear Form`.
- **Right Side Search & Table:** Keyword search bar, Role filter dropdown, and a `JTable` with color-coded badges for roles (`ADMIN` in Indigo, `STUDENT` in Green).
- Selecting any row in the table instantly populates the form for editing.

#### 3.6 Screen SCR-06: `MenuManagementFrame.java`
- **Search & Filter Bar:** Search keyword, Date filter (with `Today` shortcut button), Meal Session filter, Dietary Category filter (`ALL`, `VEG`, `NON-VEG`, `SPECIAL`).
- **JTable:** Columns for ID, Date, Day of Week, Meal Session, Menu Items, Category Badge, Notes, and Creator.
- Action Buttons: `+ Add Menu Record`, `✎ Edit Selected`, `🗑 Delete Selected`, `Close`.

#### 3.7 Screen SCR-07: `AddEditMenuDialog.java`
- Date picker with `Today` and `Tomorrow` shortcuts.
- Meal session selector dropdown (Breakfast, Lunch, Snacks, Dinner with timings).
- Dietary category selector and multi-line food items text area.
- Prevents duplicate menus on the same date and session.

#### 3.8 Screen SCR-08: `MealPreferenceFrame.java`
- Date selector with `Today`, `Tomorrow`, and `Day After` shortcuts.
- Meal session dropdown.
- **Live Scheduled Menu Preview:** Queries and displays the exact food items scheduled for that date/session.
- **Dietary Choice Radio Buttons:**
  - `Vegetarian (Standard Veg Dish / Dal / Rice / Roti)`
  - `Non-Vegetarian (Chicken / Egg / Fish Option)`
  - `Jain Meal (No onion, garlic, or root vegetables)`
  - `Special Diet (Health / Medical dietary requirement)`
  - `Not Eating / Skipping (Will not dine in this meal)`
- Special notes field and Save/Update/Remove buttons.

#### 3.9 Screen SCR-09: `PreferenceHistoryFrame.java`
- Full audit log of student preference submissions.
- Filter by Date From/To, Meal Session, Dietary Choice, and Roll Number search for admins.
- Summary breakdown dialog displaying counts of Veg, Non-Veg, Jain, Special, and Absent students.

#### 3.10 Screen SCR-10: `AdminReportFrame.java`
- Calendar date picker with instant `Today` / `Tomorrow` triggers.
- **Table 1:** Session Headcount Breakdown (Total Expected, Veg, Non-Veg, Jain, Special, Not Eating).
- **Table 2:** Individual Resident Log with session filter.
- **`⎙ Print / Kitchen Slip Preview`:** Generates formatted monospaced slips for kitchen chefs.
