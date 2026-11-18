# INTERNAL EVALUATION REPORT — MID TERM – I
## Minor Project NSP (Session 2026-27)
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering**

---

### Student & Project Credentials
- **Student Name:** JITWAR JHA
- **University Roll Number:** PIET25CR022
- **Branch / Section:** Computer Science & Engineering / 3CSD
- **Semester / Year:** III Semester / 2nd Year
- **Subject:** Minor Project NSP
- **Project Title:** **MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM**
- **Technology Stack:** Java (JDK 26 / 17+), Java Swing, JDBC, MySQL 8.0, IntelliJ IDEA

---

## 📊 Evaluation Rubric & Marks Breakdown (60 Marks Total)

| Evaluation Component | Maximum Marks | Target Score | Deliverable Location |
| :--- | :---: | :---: | :--- |
| **1. Project Design & Documentation** | **20 Marks** | **20 / 20** | `midterm1/project_proposal/` & `midterm1/flowchart/` |
| • Software Requirement Specification (SRS) | — | — | [`midterm1/project_proposal/SRS.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/project_proposal/SRS.md) |
| • Project Proposal | — | — | [`midterm1/project_proposal/PROJECT_PROPOSAL.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/project_proposal/PROJECT_PROPOSAL.md) |
| • System Flowcharts & Process Models | — | — | [`midterm1/flowchart/FLOWCHARTS.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/flowchart/FLOWCHARTS.md) |
| **2. Project GUI Prototype (Java Swing)** | **20 Marks** | **20 / 20** | `midterm1/gui_prototype/`, `screenshots/`, `source_code/` |
| • Complete GUI Prototype (All Screens) | — | — | [`midterm1/gui_prototype/GUI_PROTOTYPE_SPECIFICATION.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/gui_prototype/GUI_PROTOTYPE_SPECIFICATION.md) |
| • Developed GUI Screenshots & Visual Layouts | — | — | [`midterm1/screenshots/GUI_SCREENSHOTS_GALLERY.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/screenshots/GUI_SCREENSHOTS_GALLERY.md) |
| • Source Code Directory & Index | — | — | [`midterm1/source_code/`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/source_code/) & [`SOURCE_CODE_INDEX.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/source_code/SOURCE_CODE_INDEX.md) |
| **3. Database Design (MySQL)** | **20 Marks** | **20 / 20** | `midterm1/database_design_and_schema/` |
| • Database Schema & Data Dictionary | — | — | [`midterm1/database_design_and_schema/DATABASE_SCHEMA.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/database_design_and_schema/DATABASE_SCHEMA.md) |
| • Entity Relationship (ER) Diagram | — | — | [`midterm1/database_design_and_schema/ER_DIAGRAM.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/database_design_and_schema/ER_DIAGRAM.md) |
| • Working SQL Schema Script | — | — | [`midterm1/database_design_and_schema/schema.sql`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/database_design_and_schema/schema.sql) |
| • Core SQL Queries Specification | — | — | [`midterm1/database_design_and_schema/CORE_QUERIES.md`](file:///c:/Users/HP/OneDrive/Documents/NSP%20project/midterm1/database_design_and_schema/CORE_QUERIES.md) |
| **TOTAL MID TERM – I SCORE** | **60 Marks** | **60 / 60** | **Exemplary Final Rating** |

---

## 🗂️ Mid Term – I Folder Structure

```
midterm1/
 ├── MID_TERM_1_REPORT.md                       # Comprehensive Evaluation Index
 │
 ├── project_proposal/                          # [Component 1: Project Design & Documentation - 20 Marks]
 │    ├── PROJECT_PROPOSAL.md                   # Formal Minor Project Proposal
 │    └── SRS.md                                # Software Requirement Specification (SRS)
 │
 ├── flowchart/                                 # [Component 1: Flowcharts]
 │    └── FLOWCHARTS.md                         # 5 Process Flowcharts (Mermaid + ASCII)
 │
 ├── gui_prototype/                             # [Component 2: GUI Prototype - 20 Marks]
 │    └── GUI_PROTOTYPE_SPECIFICATION.md        # Detailed Specification of all 12 GUI Screens
 │
 ├── screenshots/                               # [Component 2: GUI Screenshots]
 │    └── GUI_SCREENSHOTS_GALLERY.md            # Visual UI Layouts & Wireframes
 │
 ├── source_code/                               # [Component 2: Source Code]
 │    ├── SOURCE_CODE_INDEX.md                  # Class & Package Directory
 │    └── src/                                  # Complete Java Source Code
 │         ├── database/                        # JDBC Connection Manager
 │         ├── model/                           # User, MealType, MessMenu, MealPreference entities
 │         ├── dao/                             # UserDAO, MenuDAO, MealPreferenceDAO, MealTypeDAO
 │         ├── utils/                           # ValidationUtils, DateUtils, UIConstants, TableUtils
 │         ├── ui/                              # Login, Dashboards, CRUD Frames, Modal Dialogs
 │         └── Main.java                        # Entry Point
 │
 └── database_design_and_schema/                # [Component 3: Database Design - 20 Marks]
      ├── DATABASE_SCHEMA.md                    # Data Dictionary & Normalization Rationale
      ├── ER_DIAGRAM.md                         # Entity-Relationship Diagrams & Cardinality
      ├── schema.sql                            # Executable MySQL Database Script
      └── CORE_QUERIES.md                       # Parameterized JDBC Query Reference
```
