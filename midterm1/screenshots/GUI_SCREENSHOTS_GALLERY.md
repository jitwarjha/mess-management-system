# GUI SCREENSHOTS & VISUAL LAYOUT GALLERY
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester

---

### Screen 1: Login Interface (`LoginFrame.java`)

```
+-----------------------------------------------------------------------------------------------+
| Mess Menu & Meal Preference Management System - Login                               _  [ ]  X |
+------------------------------------+----------------------------------------------------------+
|  MESS MENU &                       |                  Account Login                           |
|  MEAL PREFERENCE                   |  Sign in to access mess menu & preferences               |
|  Management System                 |                                                          |
|  ───────────────────────────       |  Username / Roll Number:                                 |
|  A comprehensive hostel mess       |  [ admin                                           ]     |
|  operations platform for daily     |                                                          |
|  menu scheduling, student dietary  |  Password:                                               |
|  preferences, real-time kitchen    |  [ *******                                         ]     |
|  headcounts, and meal analytics.   |                                                          |
|                                    |  +----------------------------------------------------+  |
|  +------------------------------+  |  |                 🔑 Sign In / Login                 |  |
|  | Minor Project NSP (2026-27)  |  |  +----------------------------------------------------+  |
|  | Student: JITWAR JHA          |  |  |          📝 New Student? Sign Up / Register        |  |
|  | Roll No: PIET25CR022         |  |  +----------------------------------------------------+  |
|  | Section: 3CSD | Sem: III     |  |                                                          |
|  +------------------------------+  |                                  [ ⚙ Database Settings ] |
+------------------------------------+----------------------------------------------------------+
```

---

### Screen 2: Administrator Dashboard (`AdminDashboard.java`)

```
+-----------------------------------------------------------------------------------------------------------+
| Administrator Dashboard - Mess Menu & Meal Preference Management System                         _  [ ]  X |
+-----------------------------------------------------------------------------------------------------------+
| MESS MANAGEMENT SYSTEM — ADMIN CONSOLE                       Logged in as: Mess Head Warden (ADMIN)        |
| Minor Project NSP | Developed by: JITWAR JHA (PIET25CR022, 3CSD)                                          |
+-----------------------------------------------------------------------------------------------------------+
| [👥 Users] [📋 Menus] [🍽️ Preferences] [📊 Kitchen Report] [🔑 Password] [🚪 Logout]                     |
+-----------------------------------------------------------------------------------------------------------+
| +-------------------------+ +-------------------------+ +-------------------------+ +-------------------+ |
| | Total Students       👥 | | Today's Menus        📋 | | Today's Preferences  🍽️ | | DB Connection  ⚡ | |
| | 4                       | | 4 / 4                   | | 12                      | | CONNECTED         | |
| +-------------------------+ +-------------------------+ +-------------------------+ +-------------------+ |
|                                                                                                           |
| +-- System Modules ----------+  +-- Today's Live Mess Menu (05 Sep 2026) -------------------------------+ |
| | [👥 User Management]       |  | Session    | Cat.   | Menu Food Items           | Description         | |
| | [📋 Menu Scheduling]       |  |------------+--------+---------------------------+---------------------| |
| | [📜 Preference Audit]      |  | Breakfast  | ALL    | Aloo Paratha, Curd, Eggs  | Served hot          | |
| | [📊 Kitchen Report]        |  | Lunch      | ALL    | Paneer Butter, Chicken    | Special lunch       | |
| | [⚙ Database Settings]      |  | Snacks     | VEG    | Veg Samosa, Chai, Coffee  | Crispy evening tea  | |
| |                            |  | Dinner     | ALL    | Mix Veg, Dal Tadka, Rice  | Wholesome dinner    | |
| +----------------------------+  +-----------------------------------------------------------------------+ |
+-----------------------------------------------------------------------------------------------------------+
| Mess Menu & Meal Preference Management System | Version 1.0               Database: MySQL | localhost:3306|
+-----------------------------------------------------------------------------------------------------------+
```

---

### Screen 3: Student Portal Dashboard (`UserDashboard.java`)

```
+-----------------------------------------------------------------------------------------------------------+
| Student Mess Portal - Mess Menu & Meal Preference Management System                             _  [ ]  X |
+-----------------------------------------------------------------------------------------------------------+
| STUDENT MESS PORTAL                                                          ROLE: STUDENT                |
| Welcome, Jitwar Jha (PIET25CR022 | Block-C / Room 304)                                                    |
+-----------------------------------------------------------------------------------------------------------+
| +-------------------------+ +--------------------------------+ +----------------------------------------+ |
| | Today's Date         📅 | | My Today's Preferences      🍽️ | | Total Preferences Submitted        📜 | |
| | 05-09-2026              | | 4 / 4 Meals Recorded           | | 14 Submissions                         | |
| +-------------------------+ +--------------------------------+ +----------------------------------------+ |
|                                                                                                           |
| +-- Quick Actions -----------+  +-- Today's Food Menu (05 Sep 2026) ------------------------------------+ |
| | [🍽️ Select Preference]     |  | Meal Session | Category | Menu Items             | Special Notes      | |
| | [📋 Full Schedule View]    |  |--------------+----------+------------------------+--------------------| |
| | [📜 My History Log]        |  | Breakfast    | ALL      | Aloo Paratha, Curd     | Hot breakfast      | |
| | [🔑 Change Password]       |  | Lunch        | ALL      | Paneer Butter, Chicken | Special Monday     | |
| |                            |  | Snacks       | VEG      | Samosa, Chai, Coffee   | Evening snacks     | |
| |                            |  | Dinner       | ALL      | Mix Veg Curry, Rice    | Light dinner       | |
| +----------------------------+  +-----------------------------------------------------------------------+ |
+-----------------------------------------------------------------------------------------------------------+
| Poornima Institute of Engineering & Technology | Minor Project NSP (Session 2026-27)     [Logout Session] |
+-----------------------------------------------------------------------------------------------------------+
```

---

### Screen 4: Student Meal Preference Selection (`MealPreferenceFrame.java`)

```
+-----------------------------------------------------------------------------------------------+
| Daily Meal Preference Selection                                                     _  [ ]  X |
+-----------------------------------------------------------------------------------------------+
| Meal Preference Selection                              Jitwar Jha | PIET25CR022               |
| Choose dietary option for upcoming mess sessions to help reduce food waste                    |
+-----------------------------------------------------------------------------------------------+
| Meal Date:    [ 05-09-2026  ] [Today] [Tomorrow] [Day After]                                  |
| Meal Session: [ Lunch (12:30 PM - 02:30 PM)                                                v] |
+---------------------------------------------------------------+-------------------------------+
|  Scheduled Mess Menu                                          |  Select Your Preference       |
|  Timing: 12:30 PM - 02:30 PM                                  |  Status: ✓ Registered (NON-VEG)|
|  +---------------------------------------------------------+  |  ( ) Vegetarian (Standard)   |
|  | ITEMS:                                                  |  |  (•) Non-Vegetarian (Chicken)|
|  | Paneer Butter Masala, Chicken Curry (Non-Veg option),   |  |  ( ) Jain Meal (No root veg) |
|  | Dal Makhani, Jeera Rice, Butter Roti, Green Salad       |  |  ( ) Special Diet (Medical)  |
|  |                                                         |  |  ( ) Not Eating / Skipping   |
|  | DESCRIPTION:                                            |  |                               |
|  | Special Monday hearty lunch with dessert special.       |  |  Special Notes:               |
|  |                                                         |  |  [ Extra salad portion      ] |
|  +---------------------------------------------------------+  +-------------------------------+
+-----------------------------------------------------------------------------------------------+
| [📜 View My Preference History]         [✓ Save / Update Preference] [✕ Remove Preference] [Close] |
+-----------------------------------------------------------------------------------------------+
```

---

### Screen 5: Mess Menu Management (`MenuManagementFrame.java`)

```
+-----------------------------------------------------------------------------------------------------------+
| Mess Menu Management - Poornima Mess Operations                                                 _  [ ]  X |
+-----------------------------------------------------------------------------------------------------------+
| Mess Menu Management                                                              Role: ADMINISTRATOR     |
| Create, update, search, and manage daily and weekly mess meal schedules                                   |
+-----------------------------------------------------------------------------------------------------------+
| Keyword: [           ] Date: [ 05-09-2026 ] [All Meals v] [All Dietary v] [Search/Filter] [Reset] [Today]  |
+-----------------------------------------------------------------------------------------------------------+
| ID | Date       | Day    | Meal Session | Menu Food Items            | Category | Notes       | By      |
|----+------------+--------+--------------+----------------------------+----------+-------------+---------|
| 1  | 05-09-2026 | MON    | Breakfast    | Aloo Paratha, Curd, Tea    | ALL      | Fresh       | Admin   |
| 2  | 05-09-2026 | MON    | Lunch        | Paneer Butter, Chicken     | ALL      | Special     | Admin   |
| 3  | 05-09-2026 | MON    | Snacks       | Samosa, Green Chutney, Tea | VEG      | Crispy      | Admin   |
| 4  | 05-09-2026 | MON    | Dinner       | Mix Veg, Dal Tadka, Rice   | ALL      | Wholesome   | Admin   |
+-----------------------------------------------------------------------------------------------------------+
| Total Menus: 12                  [+ Add Menu Record] [✎ Edit Selected] [🗑 Delete Selected] [Close]        |
+-----------------------------------------------------------------------------------------------------------+
```

---

### Screen 6: Kitchen Headcount Report (`AdminReportFrame.java`)

```
+-----------------------------------------------------------------------------------------------------------+
| Mess Kitchen Headcount & Preference Report                                                      _  [ ]  X |
+-----------------------------------------------------------------------------------------------------------+
| Mess Headcount & Kitchen Summary Report                                           ADMINISTRATOR           |
| Real-time dietary headcounts to optimize meal preparation and reduce food wastage                         |
+-----------------------------------------------------------------------------------------------------------+
| Report Date: [ 05-09-2026 ] [Today] [Tomorrow] [Generate Report]                                          |
+-----------------------------------------------------------------------------------------------------------+
| 1. Session Headcount Breakdown (Kitchen Portions)                                                         |
| Meal Session | Total Expected | Veg Count | Non-Veg Count | Jain Count | Special Count | Not Eating   |
|--------------+----------------+-----------+---------------+------------+---------------+--------------|
| Breakfast    | 4 (Total: 4)   | 2         | 1             | 1          | 0             | 0            |
| Lunch        | 4 (Total: 4)   | 2         | 2             | 1          | 0             | 0            |
| Snacks       | 3 (Total: 4)   | 3         | 0             | 0          | 0             | 1 (Absent)   |
| Dinner       | 4 (Total: 4)   | 2         | 2             | 1          | 0             | 0            |
|                                                                                                           |
| 2. Resident Detail Log                                                        Filter Session: [All Sessions v]|
| Roll No     | Student Name | Session   | Dietary Choice | Special Instructions / Notes                    |
|-------------+--------------+-----------+----------------+-------------------------------------------------|
| PIET25CR022 | Jitwar Jha   | Lunch     | NON-VEG        | Prefer chicken curry portion                    |
| PIET25CR045 | Rahul Sharma | Lunch     | VEG            | Extra curd requested                            |
| PIET25CR088 | Priya Verma  | Lunch     | JAIN           | Jain dal and paneer                             |
| PIET25CR012 | Aman Gupta   | Lunch     | NON-VEG        | Chicken curry                                   |
+-----------------------------------------------------------------------------------------------------------+
|                                                          [⎙ Print / Kitchen Slip Preview] [Close]         |
+-----------------------------------------------------------------------------------------------------------+
```
