# DATABASE DESIGN & SCHEMA SPECIFICATION
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester
- **Database Engine:** MySQL Server 8.0 / InnoDB Engine
- **Database Name:** `mess_management_db`
- **Character Set / Collation:** `utf8mb4` / `utf8mb4_unicode_ci`

---

### 1. Database Architecture & Design Rationale

The database `mess_management_db` is architected following **Relational Database Management System (RDBMS)** principles normalized to **Third Normal Form (3NF)**.

#### Key Architectural Objectives:
1. **Zero Redundancy:** Lookup entities like meal types (`Breakfast`, `Lunch`, `Snacks`, `Dinner`) and timings are segregated into `meal_types` rather than repeating strings across menu schedules.
2. **Referential Integrity:** Foreign keys enforce strict parent-child relationships with appropriate cascade/nullification rules (`ON DELETE CASCADE`, `ON DELETE SET NULL`).
3. **Data Constraint Enforcement:** Uniqueness constraints prevent scheduling multiple menus for the same date/session and prevent duplicate student preference submissions.
4. **Optimized Indexing:** B-Tree indexes on query columns (`menu_date`, `preference_date`, `user_id`, `role`, `roll_number`) ensure sub-millisecond query execution.

---

### 2. Data Dictionary & Table Schemas

#### 2.1 Table: `users`
Stores student residents, wardens, and administrative accounts.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `user_id` | `INT` | `AUTO_INCREMENT`, `PRIMARY KEY` | Unique numeric identifier for each user |
| `username` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | Unique login username |
| `password` | `VARCHAR(255)` | `NOT NULL` | Account password |
| `full_name` | `VARCHAR(100)` | `NOT NULL` | Full name of student or warden |
| `roll_number` | `VARCHAR(50)` | `UNIQUE`, `NULL` | Student college roll number |
| `role` | `ENUM('ADMIN','STUDENT')` | `NOT NULL`, Default `'STUDENT'` | Access authorization level |
| `email` | `VARCHAR(100)` | `NULL` | Contact email address |
| `contact_no` | `VARCHAR(20)` | `NULL` | Mobile phone number (10-12 digits) |
| `hostel_block` | `VARCHAR(30)` | Default `'Block-A'` | Assigned hostel block |
| `room_number` | `VARCHAR(20)` | Default `'101'` | Assigned hostel room number |
| `created_at` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` | Account registration timestamp |
| `updated_at` | `TIMESTAMP` | On Update `CURRENT_TIMESTAMP` | Last profile update timestamp |

**Indexes:**
- `PRIMARY KEY (user_id)`
- `UNIQUE KEY uq_users_username (username)`
- `UNIQUE KEY uq_users_roll (roll_number)`
- `INDEX idx_user_role (role)`

---

#### 2.2 Table: `meal_types`
Master lookup table storing standard mess meal sessions and serving windows.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `meal_type_id` | `INT` | `AUTO_INCREMENT`, `PRIMARY KEY` | Unique ID for meal session |
| `meal_type_name` | `VARCHAR(30)` | `NOT NULL`, `UNIQUE` | Breakfast, Lunch, Snacks, Dinner |
| `start_time` | `VARCHAR(20)` | `NOT NULL` | Serving start time (e.g. 07:30 AM) |
| `end_time` | `VARCHAR(20)` | `NOT NULL` | Serving end time (e.g. 09:30 AM) |
| `description` | `VARCHAR(255)` | `NULL` | Meal session description & guidelines |

---

#### 2.3 Table: `mess_menu`
Stores food dishes and dietary details scheduled for each calendar date and meal session.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `menu_id` | `INT` | `AUTO_INCREMENT`, `PRIMARY KEY` | Unique menu record identifier |
| `menu_date` | `DATE` | `NOT NULL` | Scheduled calendar date |
| `meal_type_id` | `INT` | `NOT NULL`, `FOREIGN KEY` | References `meal_types(meal_type_id)` |
| `items` | `TEXT` | `NOT NULL` | Food items served during the meal |
| `dietary_type` | `ENUM('VEG','NON-VEG','SPECIAL','ALL')` | `NOT NULL`, Default `'VEG'` | Main dietary classification |
| `description` | `TEXT` | `NULL` | Special highlights / chef's notes |
| `created_by` | `INT` | `NULL`, `FOREIGN KEY` | References `users(user_id)` (Admin) |
| `created_at` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` | Record creation timestamp |
| `updated_at` | `TIMESTAMP` | On Update `CURRENT_TIMESTAMP` | Record modification timestamp |

**Constraints:**
- `CONSTRAINT fk_menu_meal_type FOREIGN KEY (meal_type_id) REFERENCES meal_types(meal_type_id) ON DELETE CASCADE`
- `CONSTRAINT fk_menu_creator FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL`
- `CONSTRAINT uq_menu_date_meal UNIQUE (menu_date, meal_type_id)`
- `INDEX idx_menu_date (menu_date)`

---

#### 2.4 Table: `meal_preferences`
Stores individual resident dietary choices and attendance intentions per meal session.

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `preference_id` | `INT` | `AUTO_INCREMENT`, `PRIMARY KEY` | Unique preference record identifier |
| `user_id` | `INT` | `NOT NULL`, `FOREIGN KEY` | References `users(user_id)` |
| `preference_date` | `DATE` | `NOT NULL` | Meal date for which preference applies |
| `meal_type_id` | `INT` | `NOT NULL`, `FOREIGN KEY` | References `meal_types(meal_type_id)` |
| `dietary_choice` | `ENUM('VEG','NON-VEG','JAIN','SPECIAL','NOT_EATING')` | `NOT NULL`, Default `'VEG'` | Student's dining selection |
| `special_notes` | `VARCHAR(255)` | `NULL` | Student instructions (e.g. coffee only) |
| `submitted_at` | `TIMESTAMP` | Default `CURRENT_TIMESTAMP` | Preference submission timestamp |
| `updated_at` | `TIMESTAMP` | On Update `CURRENT_TIMESTAMP` | Preference modification timestamp |

**Constraints:**
- `CONSTRAINT fk_pref_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE`
- `CONSTRAINT fk_pref_meal_type FOREIGN KEY (meal_type_id) REFERENCES meal_types(meal_type_id) ON DELETE CASCADE`
- `CONSTRAINT uq_user_date_meal UNIQUE (user_id, preference_date, meal_type_id)`
- `INDEX idx_pref_date (preference_date)`
- `INDEX idx_pref_user (user_id)`

---

### 3. Normalization Form Justification

1. **First Normal Form (1NF):**
   - Every column contains atomic (indivisible) scalar values.
   - All tables define explicit Primary Keys (`user_id`, `meal_type_id`, `menu_id`, `preference_id`).
   - Multi-valued attributes (e.g. meal timings and session types) are decoupled into lookup tables.
2. **Second Normal Form (2NF):**
   - Tables are in 1NF.
   - All non-key attributes are fully functionally dependent on the primary key without partial dependencies.
3. **Third Normal Form (3NF):**
   - Tables are in 2NF.
   - Transitive dependencies are eliminated. For instance, meal session names and timings exist exclusively in `meal_types`, preventing duplicate copies in `mess_menu` and `meal_preferences`.
