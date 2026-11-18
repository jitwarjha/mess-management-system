import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

def set_cell_border(cell, **kwargs):
    """Set cell borders."""
    tc = cell._tc
    tcPr = tc.get_or_add_tcPr()
    tcBorders = tcPr.first_child_found_in("w:tcBorders")
    if tcBorders is None:
        tcBorders = OxmlElement('w:tcBorders')
        tcPr.append(tcBorders)
    for edge in ('top', 'left', 'bottom', 'right', 'insideH', 'insideV'):
        edge_data = kwargs.get(edge)
        if edge_data:
            tag = 'w:{}'.format(edge)
            element = tcBorders.find(qn(tag))
            if element is None:
                element = OxmlElement(tag)
                tcBorders.append(element)
            for key, val in edge_data.items():
                element.set(qn('w:{}'.format(key)), str(val))

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tc = cell._tc
    tcPr = tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def add_header_styled(doc, text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p.paragraph_format.space_before = Pt(12)
    p.paragraph_format.space_after = Pt(12)
    p.paragraph_format.line_spacing = 1.5
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(14)
    run.font.bold = True
    run.font.color.rgb = RGBColor(0, 0, 0)
    return p

def add_body_p(doc, text, bold_prefix=None, space_after=4):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    p.paragraph_format.line_spacing = 1.0
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.space_before = Pt(2)
    
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = 'Times New Roman'
        r_pre.font.size = Pt(10)
        r_pre.font.bold = True
        
    r_body = p.add_run(text)
    r_body.font.name = 'Times New Roman'
    r_body.font.size = Pt(10)
    r_body.font.bold = False
    return p

def add_subheading(doc, text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    p.paragraph_format.line_spacing = 1.0
    p.paragraph_format.space_before = Pt(8)
    p.paragraph_format.space_after = Pt(3)
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(10)
    run.font.bold = True
    return p

def add_footer_page_number(doc):
    for section in doc.sections:
        footer = section.footer
        p = footer.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        # Add simple page field
        fldSimple = OxmlElement('w:fldSimple')
        fldSimple.set(qn('w:instr'), 'PAGE')
        p._p.append(fldSimple)

def create_report():
    doc = docx.Document()
    
    # Page Setup - A4
    for section in doc.sections:
        section.page_width = Inches(8.27)
        section.page_height = Inches(11.69)
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        
    # ==========================================
    # COVER PAGE
    # ==========================================
    p_a = doc.add_paragraph()
    p_a.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = p_a.add_run("A\nSOFTWARE REQUIREMENT SPECIFICATION\nON")
    r.font.name = 'Times New Roman'
    r.font.size = Pt(12)
    r.font.bold = True
    
    p_title = doc.add_paragraph()
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_title.paragraph_format.space_before = Pt(12)
    p_title.paragraph_format.space_after = Pt(12)
    r_t = p_title.add_run("MESS MENU & MEAL PREFERENCE MANAGEMENT SYSTEM")
    r_t.font.name = 'Times New Roman'
    r_t.font.size = Pt(16)
    r_t.font.bold = True
    
    p_under = doc.add_paragraph()
    p_under.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_u = p_under.add_run("UNDER\nMINI PROJECT LAB (NON-SYLLABUS PROJECT)\n")
    r_u.font.name = 'Times New Roman'
    r_u.font.size = Pt(11)
    r_u.font.bold = True
    
    # Space for Logo
    p_logo = doc.add_paragraph()
    p_logo.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_logo.paragraph_format.space_before = Pt(24)
    p_logo.paragraph_format.space_after = Pt(24)
    r_logo = p_logo.add_run("[ POORNIMA LOGO ]")
    r_logo.font.name = 'Times New Roman'
    r_logo.font.size = Pt(14)
    r_logo.font.bold = True
    r_logo.font.color.rgb = RGBColor(180, 0, 0)
    
    # Submitted To & Submitted By Table
    p_space = doc.add_paragraph()
    p_space.paragraph_format.space_before = Pt(36)
    
    table_sub = doc.add_table(rows=2, cols=2)
    table_sub.alignment = WD_TABLE_ALIGNMENT.CENTER
    table_sub.autofit = False
    
    for row in table_sub.rows:
        row.cells[0].width = Inches(3.1)
        row.cells[1].width = Inches(3.1)
        
    cell_tl = table_sub.cell(0, 0)
    cell_tr = table_sub.cell(0, 1)
    cell_bl = table_sub.cell(1, 0)
    cell_br = table_sub.cell(1, 1)
    
    p = cell_tl.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    r = p.add_run("Submitted To:")
    r.font.name = 'Times New Roman'
    r.font.size = Pt(11)
    r.font.bold = True
    
    p = cell_tr.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    r = p.add_run("Submitted By:")
    r.font.name = 'Times New Roman'
    r.font.size = Pt(11)
    r.font.bold = True
    
    p = cell_bl.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    r = p.add_run("Faculty Coordinator / Evaluator\nAssistant Professor")
    r.font.name = 'Times New Roman'
    r.font.size = Pt(10)
    
    p = cell_br.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    r = p.add_run("JITWAR JHA\n(PIET25CR022)\nSection: 3CSD")
    r.font.name = 'Times New Roman'
    r.font.size = Pt(10)
    r.font.bold = True
    
    # Department Details
    p_dep = doc.add_paragraph()
    p_dep.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_dep.paragraph_format.space_before = Pt(60)
    r_d = p_dep.add_run("DEPARTMENT OF COMPUTER ENGINEERING\nPOORNIMA INSTITUTE OF ENGINEERING & TECHNOLOGY, JAIPUR\nAcademic Year (2026-27)")
    r_d.font.name = 'Times New Roman'
    r_d.font.size = Pt(11)
    r_d.font.bold = True
    
    # ==========================================
    # CHAPTER 1: Introduction of Project
    # ==========================================
    doc.add_page_break()
    add_header_styled(doc, "Chapter 1: Introduction of Project")
    
    add_subheading(doc, "1.1 Objective of Project")
    add_body_p(doc, 
        "The primary objective of developing the Mess Menu & Meal Preference Management System is to streamline, digitize, and automate hostel mess dining operations in collegiate environments. Traditional hostel dining operations face significant operational inefficiencies, primarily resulting from manual notice-board-based menu distribution and rough guess-based cooking estimations. These practices frequently lead to massive food wastage when students unexpectedly dine out or skip meals, or conversely, cause acute food shortages during high-attendance sessions.",
        bold_prefix=None)
    add_body_p(doc,
        "The software aims to solve these core challenges by:\n"
        "1. Providing a centralized digital portal for mess administrators to schedule, manage, and broadcast daily and weekly dining menus categorized by meal sessions (Breakfast, Lunch, Snacks, Dinner) and dietary classes (Veg, Non-Veg, Special, All).\n"
        "2. Providing an interactive student portal that allows hostel residents to view published menus in real-time and log their exact dietary preferences (Pure Veg, Non-Veg, Jain Meal, Special Medical Diet, or Not Eating / Absent) in advance.\n"
        "3. Generating real-time automated kitchen headcount reports and portion slips that provide kitchen chefs with exact portion counts, eliminating food wastage, respecting strict dietary preferences (such as root-vegetable-free Jain preparation), and ensuring optimal operational cost efficiency.",
        bold_prefix=None)
        
    add_subheading(doc, "1.2 Types of Users")
    add_body_p(doc, "The system supports two primary distinct user roles with strict Role-Based Access Control (RBAC):")
    add_body_p(doc, " Mess Wardens, Hostel Incharges, and Mess Administrative Staff. Administrators have full system privileges including user account management (CRUD), daily/weekly mess menu scheduling (CRUD), resident preference auditing, and real-time kitchen headcount slip generation.", bold_prefix="1. Administrator (Admin): ")
    add_body_p(doc, " Hostel Residents and College Students residing in campus hostels. Students have dedicated portal privileges including account self-registration (Sign-Up), viewing published daily/weekly menus, registering and updating their dietary preferences per meal session, and reviewing their personal meal history logs.", bold_prefix="2. Student / Resident (User): ")

    # ==========================================
    # CHAPTER 2: Requirement Analysis
    # ==========================================
    doc.add_page_break()
    add_header_styled(doc, "Chapter 2: Requirement Analysis")
    
    add_subheading(doc, "2.1 Functional Requirement")
    add_body_p(doc, "The system encompasses the following comprehensive functional requirements:")
    add_body_p(doc, " Secure role-based login for Admins and Students with credentials authenticated against the MySQL database. Includes student self-registration with duplicate username/roll number prevention.", bold_prefix="• Authentication & Authorization: ")
    add_body_p(doc, " Dedicated consoles for Administrators and Students displaying real-time metrics, active session counts, and live preview of today's food schedule.", bold_prefix="• Interactive Dashboard: ")
    add_body_p(doc, " Full Create, Read, Update, Delete, and Search operations for student accounts with instant table selection and multi-criteria filters.", bold_prefix="• User Management (Admin): ")
    add_body_p(doc, " Scheduling and updating food menus across Breakfast, Lunch, Snacks, and Dinner with unique date-session constraints to eliminate duplicate records.", bold_prefix="• Mess Menu Management: ")
    add_body_p(doc, " Students can view what's cooking in the kitchen and submit their choices (Veg, Non-Veg, Jain, Special, Not Eating) using smart upsert logic.", bold_prefix="• Meal Preference Portal: ")
    add_body_p(doc, " Date-wise aggregation of meal portion counts generating formatted Kitchen Production Slips for kitchen chefs.", bold_prefix="• Headcount Reporting: ")
    add_body_p(doc, " Verification of current credentials with a minimum 4-character new password rule.", bold_prefix="• Security & Password Management: ")
    add_body_p(doc, " Graphical dialog allowing examiners and administrators to test and reconfigure MySQL connection settings dynamically.", bold_prefix="• Database Configuration: ")

    add_subheading(doc, "2.2 Technology Used")
    add_body_p(doc, "The application is built using standard, robust desktop technologies:")
    add_body_p(doc, " Core object-oriented programming language providing platform independence, multithreading, and strong type safety.", bold_prefix="• Programming Language: Java (JDK 26 / JDK 17+)\n")
    add_body_p(doc, " Native Java Graphical User Interface library utilized for all frames, dialogs, custom table cell renderers, and high-contrast styling.", bold_prefix="• GUI Framework: Java Swing & AWT (`javax.swing.*`, `java.awt.*`)\n")
    add_body_p(doc, " Relational Database Management System providing ACID transactions, 3NF normalized tables, and integrity constraints.", bold_prefix="• Database Management System: MySQL Community Server 8.0\n")
    add_body_p(doc, " Official Type-4 JDBC driver providing parameterized PreparedStatement execution to eliminate SQL Injection.", bold_prefix="• Database Connectivity: JDBC API (MySQL Connector/J 8.3.0)\n")
    add_body_p(doc, " Integrated Development Environment used for project development, compilation, and debugging.", bold_prefix="• Development Environment: IntelliJ IDEA Community Edition\n")
    add_body_p(doc, " Windows 10 / Windows 11 (Cross-Platform Compatible).", bold_prefix="• Operating System: ")

    add_subheading(doc, "2.3 Graphical User Interface")
    add_body_p(doc, "The Graphical User Interface is structured into clean, modular, and responsive desktop windows:")
    add_body_p(doc, " Dual-panel interface featuring branding credentials on the left and secure login/registration controls on the right.", bold_prefix="1. Login & Registration Screen (`LoginFrame.java`, `RegisterDialog.java`): ")
    add_body_p(doc, " Features 4 real-time KPI metric cards, navigation action buttons, quick toolbar, and a live today's mess menu board.", bold_prefix="2. Administrator Dashboard (`AdminDashboard.java`): ")
    add_body_p(doc, " Displays personalized resident greeting, today's submission status badge, quick actions, and full today's menu overview.", bold_prefix="3. Student Portal (`UserDashboard.java`): ")
    add_body_p(doc, " Dual-pane window featuring an input form on the left and a searchable, role-filtered JTable on the right.", bold_prefix="4. User Management Window (`UserManagementFrame.java`): ")
    add_body_p(doc, " Comprehensive scheduling table with search bar, date filters, dietary categories, and Add/Edit modal dialogs.", bold_prefix="5. Mess Menu Management (`MenuManagementFrame.java`, `AddEditMenuDialog.java`): ")
    add_body_p(doc, " Interactive selection screen displaying a live kitchen menu preview alongside dietary radio options (Veg, Non-Veg, Jain, Special, Not Eating).", bold_prefix="6. Meal Preference Portal (`MealPreferenceFrame.java`): ")
    add_body_p(doc, " Searchable table for auditing past resident preferences with summary count dialogs.", bold_prefix="7. Preference History & Audit (`PreferenceHistoryFrame.java`): ")
    add_body_p(doc, " Aggregated kitchen portions table and monospaced Kitchen Production Slip generator.", bold_prefix="8. Kitchen Headcount Report (`AdminReportFrame.java`): ")

    # ==========================================
    # CHAPTER 3: Design
    # ==========================================
    doc.add_page_break()
    add_header_styled(doc, "Chapter 3: Design")
    
    add_subheading(doc, "3.1 Use Case Diagram")
    add_body_p(doc, "The Use Case Diagram defines the interactions between the two primary actors (Administrator and Student) and the system boundaries:")
    
    # Use Case Textual Representation / ASCII Diagram
    add_body_p(doc,
        "                    +-------------------------------------------------------------+\n"
        "                    |       MESS MENU & PREFERENCE MANAGEMENT SYSTEM BOUNDARY     |\n"
        "                    |                                                             |\n"
        "  +---------------+ |   (UC-01: Authenticate & Login) <------------------+        | +-------------+\n"
        "  |               | |   (UC-02: Self-Register Student) <------------------------+ | |             |\n"
        "  |               | |   (UC-03: View Daily / Weekly Food Menu) <---------+------+ | |             |\n"
        "  |               | |   (UC-04: Select / Update Meal Preference) <-------+        | |   STUDENT   |\n"
        "  | ADMINISTRATOR | |   (UC-05: View Personal Preference History) <------+        | |   (User)    |\n"
        "  |    (Warden)   | |   (UC-06: Manage Student & Staff Users [CRUD])     |        | |             |\n"
        "  |               | |   (UC-07: Schedule & Manage Food Menus [CRUD])     |        | +-------------+\n"
        "  |               | |   (UC-08: Audit Resident Meal Preferences)         |        |\n"
        "  |               | |   (UC-09: Generate Kitchen Headcount Slips)        |        |\n"
        "  |               | |   (UC-10: Update Account Password) <---------------+--------+\n"
        "  |               | |   (UC-11: Configure Database Connection)           |        |\n"
        "  +---------------+ +-------------------------------------------------------------+\n",
        bold_prefix=None)
        
    add_body_p(doc, "Brief Explanation: The Administrator actor is responsible for administrative use cases (UC-06, UC-07, UC-08, UC-09, UC-11), while the Student actor interacts with resident use cases (UC-02, UC-03, UC-04, UC-05). Both actors share common security use cases (UC-01 Login, UC-10 Change Password).")

    add_subheading(doc, "3.2 Data Base Tables")
    add_body_p(doc, "The relational database `mess_management_db` contains 4 normalized tables (3NF):")
    
    # Table 1: users
    add_body_p(doc, "Table 3.1: users (Stores Administrator and Student Accounts)", bold_prefix="")
    t_users = doc.add_table(rows=5, cols=4)
    t_users.alignment = WD_TABLE_ALIGNMENT.CENTER
    headers = ["Field Name", "Data Type", "Constraints", "Description"]
    for i, h in enumerate(headers):
        cell = t_users.cell(0, i)
        cell.paragraphs[0].text = h
        cell.paragraphs[0].runs[0].font.bold = True
        cell.paragraphs[0].runs[0].font.size = Pt(9)
        set_cell_border(cell, top={'val': 'single', 'sz': '4'}, bottom={'val': 'single', 'sz': '4'})
        
    user_rows = [
        ["user_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "Unique User Identifier"],
        ["username", "VARCHAR(50)", "NOT NULL, UNIQUE", "Unique Login Username"],
        ["password", "VARCHAR(255)", "NOT NULL", "Account Access Password"],
        ["role", "ENUM('ADMIN','STUDENT')", "NOT NULL, DEFAULT 'STUDENT'", "Role Authorization Level"]
    ]
    for r_idx, r_data in enumerate(user_rows, start=1):
        for c_idx, val in enumerate(r_data):
            cell = t_users.cell(r_idx, c_idx)
            cell.paragraphs[0].text = val
            cell.paragraphs[0].runs[0].font.size = Pt(8.5)
            set_cell_border(cell, bottom={'val': 'single', 'sz': '4', 'color': 'CCCCCC'})

    # Table 2: mess_menu
    doc.add_paragraph().paragraph_format.space_before = Pt(4)
    add_body_p(doc, "Table 3.2: mess_menu (Stores Scheduled Food Items)", bold_prefix="")
    t_menu = doc.add_table(rows=5, cols=4)
    t_menu.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, h in enumerate(headers):
        cell = t_menu.cell(0, i)
        cell.paragraphs[0].text = h
        cell.paragraphs[0].runs[0].font.bold = True
        cell.paragraphs[0].runs[0].font.size = Pt(9)
        set_cell_border(cell, top={'val': 'single', 'sz': '4'}, bottom={'val': 'single', 'sz': '4'})
        
    menu_rows = [
        ["menu_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "Unique Menu Identifier"],
        ["menu_date", "DATE", "NOT NULL", "Scheduled Calendar Date"],
        ["meal_type_id", "INT", "FOREIGN KEY (meal_types)", "Session (Breakfast/Lunch/etc)"],
        ["dietary_type", "ENUM('VEG','NON-VEG','SPECIAL','ALL')", "NOT NULL", "Primary Dietary Tag"]
    ]
    for r_idx, r_data in enumerate(menu_rows, start=1):
        for c_idx, val in enumerate(r_data):
            cell = t_menu.cell(r_idx, c_idx)
            cell.paragraphs[0].text = val
            cell.paragraphs[0].runs[0].font.size = Pt(8.5)
            set_cell_border(cell, bottom={'val': 'single', 'sz': '4', 'color': 'CCCCCC'})

    # Table 3: meal_preferences
    doc.add_paragraph().paragraph_format.space_before = Pt(4)
    add_body_p(doc, "Table 3.3: meal_preferences (Stores Student Dietary Selections)", bold_prefix="")
    t_pref = doc.add_table(rows=5, cols=4)
    t_pref.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, h in enumerate(headers):
        cell = t_pref.cell(0, i)
        cell.paragraphs[0].text = h
        cell.paragraphs[0].runs[0].font.bold = True
        cell.paragraphs[0].runs[0].font.size = Pt(9)
        set_cell_border(cell, top={'val': 'single', 'sz': '4'}, bottom={'val': 'single', 'sz': '4'})
        
    pref_rows = [
        ["preference_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "Unique Preference Record ID"],
        ["user_id", "INT", "FOREIGN KEY (users)", "Resident Student Reference"],
        ["preference_date", "DATE", "NOT NULL", "Target Meal Date"],
        ["dietary_choice", "ENUM('VEG','NON-VEG','JAIN','SPECIAL','NOT_EATING')", "NOT NULL", "Student Dietary Selection"]
    ]
    for r_idx, r_data in enumerate(pref_rows, start=1):
        for c_idx, val in enumerate(r_data):
            cell = t_pref.cell(r_idx, c_idx)
            cell.paragraphs[0].text = val
            cell.paragraphs[0].runs[0].font.size = Pt(8.5)
            set_cell_border(cell, bottom={'val': 'single', 'sz': '4', 'color': 'CCCCCC'})

    # ==========================================
    # CHAPTER 4: Conclusion
    # ==========================================
    doc.add_page_break()
    add_header_styled(doc, "Chapter 4: Conclusion")
    
    add_body_p(doc, 
        "Through the successful development of the Mess Menu & Meal Preference Management System, a complete, end-to-end desktop software solution has been established to revolutionize collegiate hostel mess administration. The system successfully replaces prone-to-error manual record books and physical notice boards with an intuitive, real-time Java Swing application backed by an ACID-compliant MySQL database.",
        bold_prefix=None)
    add_body_p(doc,
        "Key Achievements of the Project include:\n"
        "1. Complete elimination of meal estimation errors through automated daily headcount calculations, directly mitigating food wastage and reducing operational mess budget leakages.\n"
        "2. Empowerment of hostel residents to customize their dietary choices in advance, ensuring that students with strict dietary practices (such as Jain residents requiring root-vegetable-free meals) receive tailored meals safely.\n"
        "3. Implementation of a secure, modular software architecture following the Data Access Object (DAO) pattern, parameterized queries for complete SQL Injection immunity, and high-contrast GUI components with custom anti-aliased button renderers.\n"
        "4. Seamless database connectivity with dynamic configuration options that allow examiners and administrators to test and synchronize MySQL databases without altering source code.",
        bold_prefix=None)
    add_body_p(doc,
        "In conclusion, the project meets all functional, architectural, and quality benchmarks prescribed in the official Minor Project NSP curriculum, providing a robust software asset ready for live hostel deployment.",
        bold_prefix=None)

    # ==========================================
    # CHAPTER 5: Snapshots of Your Project
    # ==========================================
    doc.add_page_break()
    add_header_styled(doc, "Chapter 5: Snapshots of Your project")
    add_body_p(doc, "This chapter contains visual screenshots and interface descriptions of the working Java Swing desktop application:")
    
    snapshots = [
        ("Figure 5.1: Login Page", "Figure_5_1_Login_Page.png", 5.5, "Features dual-panel layout with institutional branding on the left and high-contrast 'Sign In' and 'Sign Up' controls on the right."),
        ("Figure 5.2: Student Registration Page", "Figure_5_2_Student_Registration_Page.png", 4.2, "Modal dialog allowing new residents to register with Full Name, Roll No, Username, Password, Room No, and Contact details."),
        ("Figure 5.3: Administrator Dashboard", "Figure_5_3_Administrator_Dashboard.png", 5.8, "Displays live KPI cards (Total Students, Published Menus, Today's Preferences), quick navigation buttons, and today's live menu board."),
        ("Figure 5.4: Student Portal Dashboard", "Figure_5_4_Student_Portal_Dashboard.png", 5.8, "Presents personalized resident greeting, today's submission status badge, and scheduled food dishes overview."),
        ("Figure 5.5: User Management Page", "Figure_5_5_User_Management_Page.png", 5.8, "Enables full CRUD operations for resident accounts with instant form auto-population and role filtering."),
        ("Figure 5.6: Mess Menu Management Page", "Figure_5_6_Mess_Menu_Management_Page.png", 5.8, "Comprehensive menu scheduling table with multi-criteria search, date filters, and dietary tags."),
        ("Figure 5.7: Meal Preference Selection Page", "Figure_5_7_Meal_Preference_Selection_Page.png", 5.5, "Displays live scheduled kitchen food items alongside dietary options (Veg, Non-Veg, Jain, Special, Not Eating)."),
        ("Figure 5.8: Preference History & Audit Page", "Figure_5_8_Preference_History_Audit_Page.png", 5.8, "Searchable audit log of student preference submissions with summary breakdown metrics."),
        ("Figure 5.9: Kitchen Headcount Report & Slip", "Figure_5_9_Kitchen_Headcount_Report_Page.png", 5.8, "Aggregated kitchen portions table and printable monospaced Kitchen Production Slip generator."),
        ("Figure 5.10: Change Password Dialog", "Figure_5_10_Change_Password_Dialog.png", 3.8, "Modal dialog providing credential verification and minimum 4-character new password rule."),
        ("Figure 5.11: Database Configuration Dialog", "Figure_5_11_Database_Configuration_Dialog.png", 4.0, "Enables dynamic MySQL connection testing, credential updates, and one-click database.sql schema importing.")
    ]
    
    import os
    for fig_caption, img_file, img_w, fig_desc in snapshots:
        add_body_p(doc, fig_desc, bold_prefix=f"{fig_caption}: ")
        
        img_path = os.path.join("midterm1", "screenshots", img_file)
        if os.path.exists(img_path):
            p_img = doc.add_paragraph()
            p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_img.paragraph_format.space_before = Pt(4)
            p_img.paragraph_format.space_after = Pt(8)
            run_img = p_img.add_run()
            run_img.add_picture(img_path, width=Inches(img_w))
            
            p_cap = doc.add_paragraph()
            p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_cap.paragraph_format.space_before = Pt(2)
            p_cap.paragraph_format.space_after = Pt(12)
            r_c = p_cap.add_run(fig_caption)
            r_c.font.name = 'Times New Roman'
            r_c.font.size = Pt(9)
            r_c.font.bold = True
            r_c.font.italic = True
        else:
            p_box = doc.add_paragraph()
            p_box.alignment = WD_ALIGN_PARAGRAPH.CENTER
            r_b = p_box.add_run(f"[{fig_caption} - Working Java Swing UI Prototype]")
            r_b.font.name = 'Times New Roman'
            r_b.font.size = Pt(9)
            r_b.font.italic = True

    # Add Footer Page Numbers
    add_footer_page_number(doc)
    
    # Save Document
    doc.save("midterm1/NSP_Project_Report_3rd_Sem.docx")
    doc.save("NSP_Project_Report_3rd_Sem.docx")
    print("Report generated successfully: midterm1/NSP_Project_Report_3rd_Sem.docx")

if __name__ == '__main__':
    create_report()
