# MINOR PROJECT PROPOSAL
## Mid Term – I Evaluation (Session 2026-27)
**Department of Computer Science & Engineering**  
**Poornima Institute of Engineering & Technology, Jaipur**

---

### Student & Academic Details
- **Student Name:** JITWAR JHA
- **University Roll Number:** PIET25CR022
- **Branch / Section:** Computer Science & Engineering / 3CSD
- **Semester / Year:** III Semester / 2nd Year
- **Subject Code / Title:** Minor Project NSP
- **Project Category:** Individual Software Application
- **Project Title:** **MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM**
- **Internal Faculty Evaluator:** Department of CSE, PIET

---

### 1. Project Overview & Motivation
Institutional hostel operations cater to hundreds of resident students daily across breakfast, lunch, evening snacks, and dinner. In traditional mess environments, two major bottlenecks exist:
1. **Inefficient Information Flow:** Mess food schedules are handwritten on physical boards, making updates, substitutions, or dietary classifications inaccessible to students remotely.
2. **Severe Food Wastage & Stock Mismanagement:** Kitchen chefs cook based on rough estimates. When students dine out or skip meals, huge volumes of food are wasted; conversely, unpredictable spikes lead to meal shortages. Furthermore, students observing strict religious or dietary restrictions (e.g., Jain meals with zero root vegetables, high-protein non-veg meals, or special medical diets) lack a structured channel to pre-book their requirements.

The **Mess Menu & Meal Preference Management System** is a high-performance Java Swing desktop application connected via JDBC to a MySQL relational database. It automates daily menu broadcasting, individual student preference logging, and real-time kitchen portion calculation to eliminate food wastage and streamline hostel dining management.

---

### 2. Objectives of the Project
- **Digital Menu Scheduling:** Allow mess administrators to dynamically create, edit, and publish weekly/daily menus categorized by meal sessions (Breakfast, Lunch, Snacks, Dinner) and dietary types (`VEG`, `NON-VEG`, `SPECIAL`, `ALL`).
- **Student Dietary Preference Logging:** Empower students to log in, view live food schedules, and register their dietary selection (`VEG`, `NON-VEG`, `JAIN`, `SPECIAL`, `NOT_EATING`) with optional notes.
- **Real-Time Kitchen Headcount Aggregation:** Automatically calculate exact counts of students eating each diet category per meal session to generate printable Kitchen Production Slips.
- **Account & Resident Administration:** Provide role-based management (`ADMIN` vs `STUDENT`), student self-registration, and administrative CRUD operations.
- **Auditability & Traceability:** Maintain historical meal logs for students and administrators to analyze consumption patterns.

---

### 3. Scope of the System
- **Hostel Mess Operations:** Applied to college hostels, institutional dining halls, and residential campus cafeterias.
- **Role-Based Workflows:** Distinct, secure interfaces for Wardens/Mess Incharges (Admins) and Hostel Residents (Students).
- **Desktop Architecture:** Standalone native Java Swing application with zero browser dependencies and local/remote MySQL database synchronization.

---

### 4. Technical Specifications & Feasibility

#### 4.1 Technology Stack
| Layer | Technology / Tool | Version / Details |
| :--- | :--- | :--- |
| **Programming Language** | Java (Standard Edition) | JDK 17+ / JDK 26 |
| **GUI Framework** | Java Swing & AWT | Native desktop GUI |
| **Database Engine** | MySQL Community Server | Version 8.0+ |
| **Database Connectivity** | JDBC API (`java.sql.*`) | MySQL Connector/J 8.3.0 |
| **Development IDE** | IntelliJ IDEA Community / Ultimate | Version 2024.1+ |
| **Operating System** | Windows 10 / 11 | Cross-Platform Compatible |

#### 4.2 Feasibility Analysis
- **Technical Feasibility:** Java Swing provides robust GUI components (`JFrame`, `JTable`, `GridBagLayout`) while JDBC provides deterministic, ACID-compliant database transactions.
- **Operational Feasibility:** Intuitive user interfaces with one-click actions, live menu previews, and high-contrast styling ensure students and kitchen staff can use the system with zero technical training.
- **Economic Feasibility:** Built entirely with open-source technologies (Java SE, MySQL, IntelliJ IDEA Community Edition), incurring zero software licensing costs.

---

### 5. Project Modules & Functional Division

```
 MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM
 ├── 1. Authentication & Security Module
 │    ├── Secure Role-Based Login (Admin vs Student)
 │    ├── Student Self-Registration (Sign-Up Form)
 │    ├── Password Verification & Modification
 │    └── Session Tracking (SessionManager)
 │
 ├── 2. Administrator Console
 │    ├── Real-Time KPI Cards (Total Students, Menus Published, Today's Preferences)
 │    ├── User Management (Full CRUD, Search, Filter by Role)
 │    ├── Mess Menu Management (Full CRUD, Unique Date+Session Constraint)
 │    ├── Resident Preference Audit & Search
 │    └── Kitchen Headcount Production Report (Portion Slips)
 │
 ├── 3. Student Portal
 │    ├── Daily/Weekly Food Menu Schedule Browser
 │    ├── Interactive Meal Preference Selector with Live Kitchen Preview
 │    ├── Smart Upsert (Add/Update Preference without duplicate rows)
 │    └── Personal Meal Preference History
 │
 └── 4. Database & Configuration Utility
      ├── Centralized JDBC Connection Pool (`DBConnection.java`)
      ├── Dynamic Configuration Manager (`db.properties`)
      └── On-the-Fly MySQL Connection Tester & Schema Importer
```

---

### 6. Timeline & Milestones (Mid Term-I Phase)
- **Phase 1 (Week 1–4):** Domain Research, Problem Formulation, SRS Documentation, Initial Swing Layouts.
- **Phase 2 (Week 5–8):** Event Handling, 3NF Normalized Database Schema Design (`database.sql`), Mid Term-I Submission.
- **Phase 3 (Week 9–12):** JDBC DAO Layer, Parameterized CRUD Operations, JTable Custom Renderers, Kitchen Reports.
- **Phase 4 (Week 13–14):** End-to-End System Audit, Windows Batch Scripting, Final Demonstration & External Viva.

---

### 7. Expected Outcomes & Deliverables
1. Complete Software Requirement Specification (SRS) & System Proposal.
2. Comprehensive System Architecture & Flowchart Documentation.
3. Fully functional Java Swing GUI Prototype with working database integration.
4. Normalized MySQL Database Schema script with realistic sample dataset.
5. High-contrast, responsive user interfaces ready for faculty evaluation.

---

**Submitted by:**  
**JITWAR JHA** (Roll No: PIET25CR022)  
Section: 3CSD, Semester III  
Poornima Institute of Engineering & Technology, Jaipur
