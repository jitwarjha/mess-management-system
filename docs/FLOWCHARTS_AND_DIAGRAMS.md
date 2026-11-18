# System Architecture, Flowcharts & Diagrams
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

### 1. Overall System Execution Flow

```mermaid
flowchart TD
    Start([Launch Main.java]) --> InitLook[Initialize Look & Feel and Fonts]
    InitLook --> CheckDB{Verify MySQL Connection}
    CheckDB -- Success --> ShowLogin[Display LoginFrame]
    CheckDB -- Failure --> PromptDB[Show DB Configuration Option] --> ShowLogin

    ShowLogin --> UserInput[/Enter Username & Password/]
    UserInput --> AuthCheck{Authenticate via UserDAO}

    AuthCheck -- Invalid Credentials --> ErrorDialog[Show Error Dialog] --> ShowLogin
    AuthCheck -- Valid Credentials --> CheckRole{Check User Role}

    CheckRole -- Role = ADMIN --> AdminDash[Open AdminDashboard]
    CheckRole -- Role = STUDENT --> UserDash[Open UserDashboard]

    AdminDash --> AdminActions{Admin Module Actions}
    AdminActions --> M1[User Management CRUD]
    AdminActions --> M2[Mess Menu Scheduling CRUD]
    AdminActions --> M3[Meal Preference Audit]
    AdminActions --> M4[Kitchen Headcount Report]
    AdminActions --> M5[Change Password / DB Config]
    AdminActions --> M6[Logout] --> ShowLogin

    UserDash --> StudentActions{Student Module Actions}
    StudentActions --> S1[Select / Update Meal Preference]
    StudentActions --> S2[View Full Weekly Menu Schedule]
    StudentActions --> S3[View Personal History]
    StudentActions --> S4[Change Password]
    StudentActions --> S5[Logout] --> ShowLogin
```

---

### 2. Login & Authentication Flow

```mermaid
flowchart TD
    A([User Opens Login Screen]) --> B[/Inputs Username & Password/]
    B --> C{Inputs Empty?}
    C -- Yes --> D[Show Validation Alert] --> B
    C -- No --> E[Execute PreparedStatement via JDBC]
    E --> F{Record Found in users Table?}
    F -- No --> G[Check if Roll Number was entered]
    G -- Not Found --> H[Display 'Invalid Credentials' Message] --> B
    G -- Found --> I[Establish User Session in SessionManager]
    F -- Yes --> I
    I --> J{Evaluate Role}
    J -- ADMIN --> K[Navigate to AdminDashboard]
    J -- STUDENT --> L[Navigate to UserDashboard]
```

---

### 3. Mess Menu Management (CRUD) Flow

```mermaid
flowchart TD
    MStart([Admin opens MenuManagementFrame]) --> MLoad[Load Menus into JTable from DB]
    MLoad --> MSelect{User Action}

    MSelect -- Filter / Search --> FilterAction[/Enter Keyword, Date, or Meal Type/] --> ExecSearch[Execute Dynamic SQL via MenuDAO] --> UpdateTable[Refresh JTable]

    MSelect -- Add Menu --> OpenAdd[Open AddEditMenuDialog]
    OpenAdd --> FormInput[/Input Date, Session, Items, Dietary Tag/]
    FormInput --> ValCheck{Check Inputs & Unique Date+Meal}
    ValCheck -- Duplicate/Invalid --> WarnDialog[Show Alert Dialog] --> FormInput
    ValCheck -- Valid --> InsertDB[(INSERT into mess_menu)] --> Refresh1[Reload Menu Table]

    MSelect -- Edit Selected --> CheckRow1{Row Selected?}
    CheckRow1 -- No --> SelectPrompt1[Prompt Row Selection]
    CheckRow1 -- Yes --> OpenEdit[Open AddEditMenuDialog with Row Data]
    OpenEdit --> EditInput[/Modify Details/] --> UpdateDB[(UPDATE mess_menu)] --> Refresh2[Reload Menu Table]

    MSelect -- Delete Selected --> CheckRow2{Row Selected?}
    CheckRow2 -- No --> SelectPrompt2[Prompt Row Selection]
    CheckRow2 -- Yes --> ConfirmDel{Confirm Deletion?}
    ConfirmDel -- Yes --> DelDB[(DELETE from mess_menu)] --> Refresh3[Reload Menu Table]
```

---

### 4. Student Meal Preference Submission Flow

```mermaid
flowchart TD
    PStart([Student opens MealPreferenceFrame]) --> PDate[/Select Meal Date & Session/]
    PDate --> QueryMenu[(Query Scheduled Menu for Date & Session)]
    QueryMenu --> ShowMenu[Display Food Items Preview in Text Area]
    PDate --> QueryPref[(Query Existing Preference for User + Date + Session)]

    QueryPref --> CheckExisting{Preference Found?}
    CheckExisting -- Yes --> SetRadio[Set Radio Button & Display 'Registered' Status]
    CheckExisting -- No --> SetDefault[Set Default 'Veg' & Display 'Not Logged']

    SetRadio --> StudentChoice[/Select Veg, Non-Veg, Jain, Special, Not Eating/]
    SetDefault --> StudentChoice
    StudentChoice --> NoteInput[/Optional Special Instructions/]
    NoteInput --> ClickSave[Click 'Save / Update Preference']

    ClickSave --> UpsertPref{Exists in DB?}
    UpsertPref -- Yes --> UpdateSQL[(UPDATE meal_preferences)]
    UpsertPref -- No --> InsertSQL[(INSERT into meal_preferences)]
    UpdateSQL --> ShowSuccess[Show Confirmation Dialog] --> ReloadStatus[Update Status Banner]
    InsertSQL --> ShowSuccess --> ReloadStatus
```

---

### 5. Kitchen Headcount & Report Aggregation Flow

```mermaid
flowchart TD
    RStart([Admin opens AdminReportFrame]) --> RDate[/Select Target Date/]
    RDate --> RQuery[Execute Grouped Aggregation Query in MealPreferenceDAO]
    RQuery --> RCalc[Calculate Total Expected Portions & Diet Breakdown]
    RCalc --> RSumTable[Populate Session Headcount JTable]
    RQuery --> RDetailQuery[Fetch Resident Level Detail Records]
    RDetailQuery --> RDetailTable[Populate Resident Roster JTable]
    RSumTable --> RExport[Click 'Print / Kitchen Slip Preview']
    RExport --> RSlip[Generate Formatted Monospaced Kitchen Slip with Instructions]
```
