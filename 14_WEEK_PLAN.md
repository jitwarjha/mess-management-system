# 14-WEEK PROGRESSION PLAN
## Minor Project NSP (Session 2026-27)
**Department of Computer Science & Engineering**  
**Poornima Institute of Engineering & Technology, Jaipur**

---

### Student & Project Details
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section:** 3CSD
- **Semester:** III
- **Project Type:** Minor Project NSP (Individual)
- **Project Title:** MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM
- **Technology Stack:** Java (JDK 26), Java Swing, JDBC, MySQL 8.0, IntelliJ IDEA

---

### Weekly Progression Matrix (Weeks 1 – 14)

| Week | Official Topic | Faculty Activities | Student Activities | Deliverable |
| :--- | :--- | :--- | :--- | :--- |
| **Week 1** | **Project Introduction & Java Fundamentals** | • Introduction to Minor Project NSP objectives, guidelines, and evaluation rubrics.<br>• Refresher lecture on Core Java OOP concepts (Classes, Objects, Inheritance, Encapsulation, Polymorphism). | • Selected individual project domain: *Mess Menu & Meal Preference Management System*.<br>• Reviewed Java OOP paradigms and packaging conventions. | • Project Title Registration & Concept Note submission. |
| **Week 2** | **Development Environment Setup** | • Guided laboratory installation of JDK 26, IntelliJ IDEA Community/Ultimate, MySQL Community Server 8.0, and MySQL Workbench.<br>• Verified environment variables (`JAVA_HOME`, `PATH`). | • Installed and configured IntelliJ IDEA IDE.<br>• Setup local MySQL server on port 3306 and created root credentials.<br>• Downloaded MySQL Connector/J JDBC driver library (`mysql-connector-j-8.3.0.jar`). | • Development Environment Verification Sheet.<br>• Working "Hello World" & MySQL ping test in IntelliJ. |
| **Week 3** | **Java Swing Basics & Authentication Forms** | • Demonstrated Swing foundational components: `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JPasswordField`, `JButton`.<br>• Explained Layout Managers (`BorderLayout`, `FlowLayout`, `GridLayout`, `GridBagLayout`). | • Designed initial prototype of `LoginFrame.java`.<br>• Implemented dual-role concept (Admin vs Student accounts) and visual split branding panel. | • First draft of `LoginFrame` GUI layout with responsive form fields. |
| **Week 4** | **Advanced Swing Components & Screen Prototype** | • Introduced advanced Swing widgets: `JComboBox`, `JRadioButton`, `ButtonGroup`, `JCheckBox`, `JScrollPane`, `JTabbedPane`, `BorderFactory`.<br>• Demonstrated card-based design techniques. | • Built screen layouts for `AdminDashboard.java`, `UserDashboard.java`, and `MealPreferenceFrame.java`.<br>• Formulated modern UI color palette (`UIConstants.java`). | • Mid-Term 1 GUI Prototype showcasing all target project screens. |
| **Week 5** | **Event Handling & Window Management** | • Taught Event Delegation Model in Java Swing (`ActionListener`, `KeyAdapter`, `MouseAdapter`, `ListSelectionListener`, `ItemListener`).<br>• Explained Window lifecycle and dialog modality (`JDialog`). | • Wired up action listeners for Login, Navigation buttons, and modal dialogs.<br>• Built `ChangePasswordDialog.java` and `DatabaseConfigDialog.java`. | • Interactive multi-frame navigation prototype with functioning dialog popups. |
| **Week 6** | **Project Allocation & Formal Proposal** | • Evaluated individual student project allocations against college project list (Topic #22).<br>• Reviewed System Requirements Specification (SRS) and modular division. | • Finalized formal Software Requirements Specification (SRS).<br>• Defined module boundaries: User Management, Menu Management, Meal Preferences, and Kitchen Reports. | • **Internal Mid-Term I Evaluation Deliverable:** SRS Document, Project Proposal, and Flowchart diagrams. |
| **Week 7** | **GUI Development & Refinement** | • Conducted UI/UX review of student desktop layouts.<br>• Emphasized consistent typography, field spacing, and user-friendly error banners. | • Standardized all application windows using `UIConstants.java`.<br>• Implemented `UserManagementFrame.java` and `MenuManagementFrame.java` with responsive GridBag forms. | • Comprehensive frontend GUI codebase with complete input controls. |
| **Week 8** | **MySQL Database Design & Normalization** | • Taught Relational Database Design, Normalization (1NF, 2NF, 3NF), Primary/Foreign Key constraints, and Integrity rules.<br>• Supervised SQL schema writing in MySQL Workbench. | • Designed normalized database `mess_management_db` consisting of `users`, `meal_types`, `mess_menu`, and `meal_preferences`.<br>• Authored `database.sql` with sample realistic hostel mess datasets. | • Complete `database.sql` script with table schemas, constraints, and test records. |
| **Week 9** | **JDBC Connectivity Architecture** | • Explained Java Database Connectivity (JDBC) 4-step workflow:<br>1. Driver loading<br>2. `DriverManager.getConnection()`<br>3. `PreparedStatement` execution<br>4. `ResultSet` traversal and safe closure. | • Created `DBConnection.java` utility with dynamic property loading (`db.properties`).<br>• Implemented connection testing and parameterized fallback mechanisms. | • Working JDBC connection manager with automated connection test utility. |
| **Week 10** | **CRUD Operations Implementation (DAO Layer)** | • Supervised implementation of Data Access Object (DAO) pattern.<br>• Mandated parameterized queries using `PreparedStatement` to eliminate SQL Injection. | • Developed `UserDAO.java`, `MealTypeDAO.java`, `MenuDAO.java`, and `MealPreferenceDAO.java`.<br>• Implemented complete Create, Read, Update, Delete, and Search logic across all entities. | • **Internal Mid-Term II Evaluation Deliverable:** Complete JDBC CRUD backend with working DAOs. |
| **Week 11** | **GUI + Database Integration & Form Validation** | • Reviewed end-to-end integration between Swing GUI forms and JDBC DAOs.<br>• Inspected input validation routines and `SQLException` handling. | • Connected all Swing frames to their respective DAOs.<br>• Implemented `ValidationUtils.java` for email, phone, date parsing, empty checks, and duplicate validation.<br>• Replaced silent exceptions with friendly `JOptionPane` feedback. | • Fully integrated desktop application with end-to-end database operations. |
| **Week 12** | **JTable Integration, Search, Filtering & Reports** | • Demonstrated `DefaultTableModel`, `TableCellRenderer`, custom badge pill styling, dynamic multi-parameter SQL filtering, and reporting summaries. | • Integrated `JTable` across all modules with real-time row selection.<br>• Implemented multi-criteria search and date filters.<br>• Developed `AdminReportFrame.java` with kitchen production slip generator. | • Functioning data tables with search, sorting, filtering, and aggregated kitchen reports. |
| **Week 13** | **Debugging, Code Quality & Coding Standards** | • Audited codebase against college marking scheme (variable naming, modularity, resource management, comments).<br>• Performed sanity testing for edge cases. | • Conducted full code cleanup, JavaDoc documentation, and exception handling audit.<br>• Prepared automated Windows scripts (`compile.bat`, `run.bat`) and IntelliJ `.iml` configuration. | • Clean, modular, GitHub-ready source code repository and test execution report. |
| **Week 14** | **Final Evaluation, Demonstration & Viva Voce** | • Conducted final project demonstration and External Viva Voce.<br>• Assessed live application execution, database queries, and technical defense. | • Successfully demonstrated full end-to-end working software to examiners.<br>• Defended system architecture, database normalization, and JDBC workflow in viva voce. | • **Final Deliverables:** Working Application, Project Report, SRS, Database Script, and Presentation. |

---

### Marking Scheme Milestone Alignment

| Milestone | Assessment Component | Target Score | Deliverable Verified |
| :--- | :--- | :---: | :--- |
| **Mid-Term I** | Project Design & Documentation | 20 / 20 | SRS Document, Proposal, Flowcharts, System Architecture |
| **Mid-Term I** | Project GUI Prototype | 20 / 20 | All Swing Screens (`LoginFrame`, `Dashboard`, Forms, Dialogs) |
| **Mid-Term I** | Database Design | 20 / 20 | Normalized Schema, `database.sql`, Constraints, Relationships |
| **Mid-Term II** | CRUD Implementation using JDBC | 20 / 20 | Complete DAOs, Create/Read/Update/Delete/Search Operations |
| **Mid-Term II** | Code Quality, Validation & Exception Handling | 20 / 20 | `ValidationUtils`, `PreparedStatement`, Custom Renderers, Exception Guards |
| **Mid-Term II** | Final Project Demonstration | 20 / 20 | Seamless executable application with zero placeholder code |
| **Mid-Term III** | Attendance & Weekly Progress | 60 / 60 | 14-Week progression logbook & Comprehensive Project Report |
| **External** | Written Exam & Viva Voce | 40 / 40 | System defense, SQL queries, JDBC explanation, live demo |
| **TOTAL** | **Comprehensive Minor Project NSP** | **200 / 200** | **Exemplary Final Project Rating** |
