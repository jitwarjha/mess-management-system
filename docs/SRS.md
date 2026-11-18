# Software Requirements Specification (SRS)
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Minor Project NSP (Session 2026-27)**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section:** 3CSD
- **Semester:** III

---

### 1. Introduction

#### 1.1 Purpose
This document provides a formal Software Requirements Specification (SRS) for the **Mess Menu & Meal Preference Management System**, specifying functional and non-functional requirements, interface definitions, user roles, and data constraints.

#### 1.2 Scope of the System
The software is an individual desktop-based administrative and student portal developed in Java Swing with a MySQL backend via JDBC. It standardizes hostel food management, including menu broadcasting, student preference logging, headcount calculations, and administrative auditing.

#### 1.3 Definitions & Acronyms
- **GUI:** Graphical User Interface (Java Swing)
- **JDBC:** Java Database Connectivity
- **DAO:** Data Access Object Pattern
- **CRUD:** Create, Read, Update, Delete
- **SRS:** Software Requirements Specification
- **RBAC:** Role-Based Access Control

---

### 2. Overall Description

#### 2.1 Product Perspective
The system operates as a standalone Java client communicating over TCP/IP with a MySQL relational database server running on `localhost:3306` (or configured host).

```
 ┌────────────────────────────────────────────────────────┐
 │                   JAVA SWING CLIENT                    │
 │  ┌─────────────────┐ ┌──────────────────────────────┐  │
 │  │ Presentation UI │ │   Validation & Utilities     │  │
 │  └────────┬────────┘ └──────────────┬───────────────┘  │
 │           │                         │                  │
 │  ┌────────┴─────────────────────────┴───────────────┐  │
 │  │        Data Access Object (DAO) Layer            │  │
 │  └────────────────────────┬─────────────────────────┘  │
 └───────────────────────────┼────────────────────────────┘
                             │  JDBC Protocol (Port 3306)
 ┌───────────────────────────▼────────────────────────────┐
 │                MYSQL DATABASE SERVER                   │
 │           Database: mess_management_db                 │
 │  [users]  [meal_types]  [mess_menu]  [meal_preferences]│
 └────────────────────────────────────────────────────────┘
```

#### 2.2 User Characteristics
1. **Administrator (Warden / Mess Incharge):**
   - Access to full administrative controls.
   - Responsible for managing user accounts, publishing food menus, and reviewing headcount slips.
2. **Student (Hostel Resident):**
   - Access to student portal.
   - Views daily/weekly food menus and registers dietary choices.

---

### 3. Functional Requirements

#### 3.1 Authentication & Session Management
- **FR-1.1:** System shall authenticate users against the `users` table via username/password or roll number/password.
- **FR-1.2:** System shall enforce role-based redirection (`ADMIN` -> `AdminDashboard`, `STUDENT` -> `UserDashboard`).
- **FR-1.3:** System shall reject invalid credentials with descriptive error dialogs.
- **FR-1.4:** System shall provide session termination (Logout) returning to the login interface.
- **FR-1.5:** System shall support password changes requiring current password verification and minimum 4-character new password validation.

#### 3.2 User Management Module (Admin Only)
- **FR-2.1:** Admin shall create new student or administrator accounts.
- **FR-2.2:** Admin shall update existing user records (name, roll, role, email, phone, hostel block, room number).
- **FR-2.3:** Admin shall delete user records with confirmation safeguards.
- **FR-2.4:** System shall validate unique constraints on `username` and `roll_number`.
- **FR-2.5:** System shall provide real-time search across names, usernames, roll numbers, and emails with role filtering.

#### 3.3 Mess Menu Management Module
- **FR-3.1:** Admin shall schedule meal menus with fields: Date (`DATE`), Meal Session (`meal_type_id`), Items (`TEXT`), Dietary Category (`VEG`, `NON-VEG`, `SPECIAL`, `ALL`), and Description.
- **FR-3.2:** System shall prevent duplicate menus for the same date and meal session via database unique constraints.
- **FR-3.3:** Admin shall update or delete scheduled menus from the JTable view.
- **FR-3.4:** Students and Admins shall search and filter menus by keyword, date, meal session, and dietary tag.

#### 3.4 Meal Preference Module (Student Portal)
- **FR-4.1:** Student shall select dietary preferences (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`) for any designated date and meal session.
- **FR-4.2:** System shall display the live scheduled menu corresponding to the selected date and session.
- **FR-4.3:** System shall perform smart upsert (inserting if new, updating if existing).
- **FR-4.4:** Student shall view personal historical preference records with date and meal filters.

#### 3.5 Kitchen Headcount & Summary Reports (Admin)
- **FR-5.1:** System shall aggregate dietary choices per session for any target date.
- **FR-5.2:** System shall compute portion metrics (Veg, Non-Veg, Jain, Special, Not Eating).
- **FR-5.3:** System shall generate formatted Kitchen Production Slips ready for kitchen printing.

---

### 4. Non-Functional Requirements

#### 4.1 Performance & Responsiveness
- All database queries shall execute in under 100 milliseconds for standard institutional data volumes (< 50,000 records).
- Swing GUI components shall execute on the Event Dispatch Thread (EDT) ensuring smooth UI rendering.

#### 4.2 Security
- Input parameterization via `PreparedStatement` on all SQL queries.
- Password change requires current password validation.
- Role checks prevent student access to administrative frames.

#### 4.3 Maintainability & Modularity
- Strict package separation: `database`, `model`, `dao`, `utils`, `ui`.
- Reusable connection pooling and resource closing routines.

#### 4.4 Reliability & Exception Handling
- Zero unhandled runtime crashes.
- All `SQLException` instances handled with informative user prompts and graceful degradation.

---

### 5. Hardware & Software Requirements

#### 5.1 Software Requirements
- **OS:** Windows 10/11, Linux, or macOS
- **JDK:** Oracle JDK / OpenJDK 17 or higher (tested with JDK 26)
- **Database:** MySQL Community Server 8.0 or higher
- **IDE:** IntelliJ IDEA Community / Ultimate Edition

#### 5.2 Hardware Requirements
- **Processor:** Intel Core i3 / AMD Ryzen 3 or higher
- **RAM:** Minimum 4 GB (8 GB recommended)
- **Disk Space:** 500 MB free disk space for JDK, MySQL, and project files
