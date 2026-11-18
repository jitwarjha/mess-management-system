# SYSTEM FLOWCHARTS & PROCESS MODELS
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester

---

### Flowchart 1: Overall System Execution Flow

```mermaid
flowchart TD
    Start([Launch Main.java]) --> InitEnv[Initialize Anti-Aliasing & Swing Look & Feel]
    InitEnv --> TestDB{Verify MySQL Connectivity}
    TestDB -- Connected --> OpenLogin[Display LoginFrame]
    TestDB -- Not Connected --> ShowAlert[Log DB Info] --> OpenLogin

    OpenLogin --> UserAction{User Action on Login Screen}
    UserAction -- Click 'Sign Up / Register' --> OpenReg[Open RegisterDialog]
    OpenReg --> RegProcess[/Input Student Details & Submit/]
    RegProcess --> ValidateReg{Validate Inputs & Uniqueness}
    ValidateReg -- Valid --> SaveStudent[(INSERT into users)] --> RegSuccess[Show Success Dialog & Pre-fill Login] --> OpenLogin
    ValidateReg -- Invalid --> RegError[Show Alert] --> OpenReg

    UserAction -- Click '⚙ Database Settings' --> OpenDBCfg[Open DatabaseConfigDialog]
    OpenDBCfg --> TestDBCreds{Test Connection / Import Schema} --> OpenLogin

    UserAction -- Enter Credentials & Click 'Sign In' --> Authenticate{Authenticate via UserDAO}
    Authenticate -- Invalid --> AuthFail[Display 'Invalid Credentials' Dialog] --> OpenLogin
    Authenticate -- Valid --> SetSession[Store User in SessionManager]

    SetSession --> CheckRole{Check User Role}
    CheckRole -- Role = ADMIN --> AdminPortal[Open AdminDashboard]
    CheckRole -- Role = STUDENT --> StudentPortal[Open UserDashboard]

    AdminPortal --> AdminOps{Admin Operations}
    AdminOps --> A1[User Management CRUD Frame]
    AdminOps --> A2[Mess Menu Scheduling Frame]
    AdminOps --> A3[Resident Preference Records Audit]
    AdminOps --> A4[Kitchen Headcount Production Report]
    AdminOps --> A5[Change Password Dialog]
    AdminOps --> A6[Logout] --> OpenLogin

    StudentPortal --> StudentOps{Student Operations}
    StudentOps --> S1[Meal Preference Selection Frame]
    StudentOps --> S2[View Full Menu Schedule Frame]
    StudentOps --> S3[My Preference History Frame]
    StudentOps --> S4[Change Password Dialog]
    StudentOps --> S5[Logout] --> OpenLogin
```

---

### Flowchart 2: User Authentication & Role-Based Redirection

```mermaid
flowchart TD
    A([User Opens Login Screen]) --> B[/Enter Username / Roll Number & Password/]
    B --> C{Fields Empty?}
    C -- Yes --> D[Show 'Please enter username and password' Alert] --> B
    C -- No --> E[Execute PreparedStatement via UserDAO]
    E --> F{Direct Username Match?}
    F -- Yes --> G[Verify Password Match]
    F -- No --> H{Roll Number Match?}
    H -- Yes --> G
    H -- No --> I[Show 'Authentication Failed' Error Dialog] --> B

    G -- Incorrect Password --> I
    G -- Correct Password --> J[Initialize Session in SessionManager]
    J --> K{Check Role}
    K -- 'ADMIN' --> L[Launch AdminDashboard & Dispose Login]
    K -- 'STUDENT' --> M[Launch UserDashboard & Dispose Login]
```

---

### Flowchart 3: Mess Menu Management (CRUD Flow)

```mermaid
flowchart TD
    MStart([Admin Opens MenuManagementFrame]) --> MLoad[(Fetch All Menus via MenuDAO.getAllMenus)]
    MLoad --> MDisplay[Populate JTable with Color Badges]

    MDisplay --> MAction{Select Action}

    MAction -- 'Search / Filter' --> MFilter[/Input Keyword, Date, Session, or Dietary Tag/]
    MFilter --> MQuery[(Execute Dynamic Parameterized SQL)]
    MQuery --> MRefreshTable[Update JTable Data Model]

    MAction -- '+ Add Menu Record' --> MAddDlg[Open AddEditMenuDialog]
    MAddDlg --> MAddForm[/Select Date, Meal Session, Items, Dietary Tag/]
    MAddForm --> MAddVal{Check Empty & UNIQUE Date+Session}
    MAddVal -- Duplicate/Missing --> MAddWarn[Show Validation Warning] --> MAddForm
    MAddVal -- Valid --> MInsert[(INSERT INTO mess_menu)] --> MAddOk[Show Success & Reload Table] --> MLoad

    MAction -- '✎ Edit Selected' --> MCheckSel1{Row Selected?}
    MCheckSel1 -- No --> MWarn1[Prompt to Select Row]
    MCheckSel1 -- Yes --> MEditDlg[Open AddEditMenuDialog with Existing Data]
    MEditDlg --> MEditForm[/Modify Menu Items/] --> MUpdate[(UPDATE mess_menu)] --> MEditOk[Show Success & Reload Table] --> MLoad

    MAction -- '🗑 Delete Selected' --> MCheckSel2{Row Selected?}
    MCheckSel2 -- No --> MWarn2[Prompt to Select Row]
    MCheckSel2 -- Yes --> MConfirmDel{Confirm Deletion Dialog}
    MConfirmDel -- Confirmed --> MDelDB[(DELETE FROM mess_menu)] --> MDelOk[Show Success & Reload Table] --> MLoad
```

---

### Flowchart 4: Student Meal Preference & Smart Upsert Flow

```mermaid
flowchart TD
    PStart([Student Opens MealPreferenceFrame]) --> PDate[/Select Meal Date & Meal Session/]
    PDate --> PFetchMenu[(Query mess_menu for selected Date & Session)]
    PFetchMenu --> PShowMenu[Render Kitchen Menu Items in Preview Box]

    PDate --> PFetchPref[(Query meal_preferences for User + Date + Session)]
    PFetchPref --> PCheckExisting{Existing Preference?}
    PCheckExisting -- Found --> PSetSelected[Auto-Select Radio Button & Show 'Registered' Status]
    PCheckExisting -- Not Found --> PSetDefault[Select 'VEG' Default & Show 'Not Logged' Status]

    PSetSelected --> PUserChoice[/Select Dietary Option & Optional Notes/]
    PSetDefault --> PUserChoice

    PUserChoice --> PClickSave[Click 'Save / Update Preference']
    PClickSave --> PUpsertCheck{Preference Record Exists in DB?}
    PUpsertCheck -- Yes --> PUpdateSQL[(UPDATE meal_preferences SET dietary_choice, notes)]
    PUpsertCheck -- No --> PInsertSQL[(INSERT INTO meal_preferences)]
    PUpdateSQL --> PSuccess[Display 'Preference Saved' Alert & Refresh Status]
    PInsertSQL --> PSuccess
```

---

### Flowchart 5: Kitchen Headcount & Production Slip Generation Flow

```mermaid
flowchart TD
    KStart([Admin Opens AdminReportFrame]) --> KDate[/Select Target Calendar Date/]
    KDate --> KQuery[(Execute Grouped Headcount Aggregation Query in MealPreferenceDAO)]
    KQuery --> KProcess[Compute Veg, Non-Veg, Jain, Special, Absent Counts per Session]
    KProcess --> KPopulate[Render Aggregated Kitchen Headcount Table]
    KQuery --> KDetail[(Query Individual Resident Roster Records)]
    KDetail --> KPopulateDetail[Render Resident Log Table with Session Filter]

    KPopulate --> KSlipBtn[Click 'Print / Kitchen Slip Preview']
    KSlipBtn --> KGenSlip[Format Monospaced Kitchen Production Slip with Chef Instructions]
    KGenSlip --> KShowSlip[Display Formatted Production Slip Dialog]
```
