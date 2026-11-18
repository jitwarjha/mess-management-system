# Project Overview
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Minor Project NSP (Session 2026-27) | Semester III | 3CSD**

---

### Student Information
- **Name:** JITWAR JHA
- **Roll No:** PIET25CR022
- **Branch/Section:** Computer Science & Engineering (3CSD)
- **Course:** B.Tech III Semester

---

### 1. Executive Summary
In institutional hostel environments, managing food operations presents dual challenges: providing transparent, up-to-date daily menu information to students, and forecasting accurate kitchen preparation quantities. Without an automated tracking system, messes suffer from severe food wastage or sudden shortages due to unpredictability in resident dietary requirements (Veg, Non-Veg, Jain, Special meals, or absentees).

The **Mess Menu & Meal Preference Management System** is a robust Java Swing desktop application integrated with a MySQL database via JDBC. It empowers mess administrators to schedule, manage, and broadcast meal menus while allowing students to submit daily meal preferences, dietary requirements, and absence notifications in advance. Real-time aggregation provides kitchen staff with accurate production slips, minimizing food waste and improving student satisfaction.

---

### 2. Problem Statement
Traditional hostel mess operations face several critical limitations:
1. **Physical Notice Boards:** Menus are posted on physical notice boards, making updates, substitutions, or special items difficult to communicate promptly.
2. **Food Wastage & Shortage:** Kitchen chefs prepare meals on crude assumptions, leading to massive food wastage or unexpected shortages when students skip meals or dine out.
3. **Special Dietary Neglect:** Students observing dietary restrictions (e.g., strict Jain meals with zero root vegetables, high-protein non-veg requirements, or allergy needs) have no structured channel to notify kitchen managers.
4. **Lack of Auditability:** Absence of historical logs prevents administration from auditing consumption patterns, meal quality feedback, or student participation trends.

---

### 3. Proposed Solution & Objectives
The system addresses these issues through:
- **Role-Based Access Control:** Distinct, protected dashboards for Administrators (Wardens/Mess Managers) and Students.
- **Dynamic Menu Scheduling:** Complete CRUD operations for Breakfast, Lunch, Snacks, and Dinner sessions with dietary categories (`VEG`, `NON-VEG`, `SPECIAL`, `ALL`).
- **Student Preference Portal:** Interactive meal selection enabling students to view the scheduled menu and specify their choice (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`).
- **Kitchen Headcount Analytics:** Instant aggregation of expected portion counts per meal session, exportable as kitchen production slips.
- **Security & Integrity:** Parameterized queries (`PreparedStatement`) eliminating SQL Injection, strict database normalization, and robust input validation.

---

### 4. Key Modules & Features

```
 Mess Menu & Meal Preference Management System
 │
 ├── 1. Authentication & Role Control
 │    ├── Secure login with MySQL authentication
 │    ├── Dual role authorization (ADMIN / STUDENT)
 │    └── Dynamic session manager & password change
 │
 ├── 2. Administrator Dashboard
 │    ├── Real-time metric cards (Students, Published Menus, Preferences, DB Status)
 │    ├── Quick Action Toolbar & JMenuBar navigation
 │    └── Live today's menu preview table
 │
 ├── 3. User & Student Management (CRUD)
 │    ├── Add, Update, Delete student & staff records
 │    ├── Multi-criteria search (Name, Roll, Username, Email)
 │    └── Form validation (duplicate check, email/phone format)
 │
 ├── 4. Mess Menu Management (CRUD)
 │    ├── Add, Update, Delete meal menus
 │    ├── Date and Meal Type filtering
 │    └── Duplicate menu prevention (unique date + meal session)
 │
 ├── 5. Student Meal Preference Portal
 │    ├── Date and meal session picker with quick buttons (Today/Tomorrow)
 │    ├── Live preview of scheduled kitchen food items
 │    ├── Dietary selection (Veg, Non-Veg, Jain, Special, Not Eating)
 │    └── Smart upsert (add or modify preference)
 │
 ├── 6. Preference History & Audit Records
 │    ├── Student personal history review
 │    ├── Admin resident search and date filtering
 │    └── Summary breakdown popup
 │
 ├── 7. Kitchen Production & Headcount Reports
 │    ├── Aggregated session headcounts (Veg, Non-Veg, Jain, Not Eating)
 │    ├── Detailed resident roster per session
 │    └── Exportable/Printable kitchen production slip
 │
 └── 8. Database Administration
      ├── Dynamic MySQL configuration (`db.properties`)
      ├── Live connection test utility
      └── Automated `database.sql` script importer
```

---

### 5. Technology Stack
- **Programming Language:** Java Standard Edition (JDK 26 / JDK 17+ compatible)
- **GUI Framework:** Java Swing (`javax.swing.*`, `java.awt.*`)
- **Database Engine:** MySQL Server 8.0 / MariaDB
- **Database Connectivity:** JDBC API (`java.sql.*`)
- **Driver:** MySQL Connector/J 8.3.0
- **Development Environment:** IntelliJ IDEA
- **Operating System:** Windows 10/11 / Cross-platform
