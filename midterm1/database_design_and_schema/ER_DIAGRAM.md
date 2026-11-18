# ENTITY RELATIONSHIP (ER) DIAGRAM
## Mess Menu & Meal Preference Management System
**Poornima Institute of Engineering & Technology, Jaipur**  
**Department of Computer Science & Engineering | Mid Term – I Evaluation**

---

### Student Information
- **Student Name:** JITWAR JHA
- **Roll Number:** PIET25CR022
- **Section / Semester:** 3CSD / III Semester

---

### 1. Entity Relationship (ER) Diagram (Mermaid Notation)

```mermaid
erDiagram
    users ||--o{ mess_menu : "publishes (created_by)"
    users ||--o{ meal_preferences : "submits (user_id)"
    meal_types ||--o{ mess_menu : "categorizes (meal_type_id)"
    meal_types ||--o{ meal_preferences : "categorizes (meal_type_id)"

    users {
        int user_id PK "AUTO_INCREMENT"
        string username UK "NOT NULL"
        string password "NOT NULL"
        string full_name "NOT NULL"
        string roll_number UK "NULLable"
        enum role "ADMIN, STUDENT"
        string email "NULLable"
        string contact_no "NULLable"
        string hostel_block "Default Block-A"
        string room_number "Default 101"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "ON UPDATE CURRENT_TIMESTAMP"
    }

    meal_types {
        int meal_type_id PK "AUTO_INCREMENT"
        string meal_type_name UK "Breakfast, Lunch, Snacks, Dinner"
        string start_time "NOT NULL (e.g. 07:30 AM)"
        string end_time "NOT NULL (e.g. 09:30 AM)"
        string description "NULLable"
    }

    mess_menu {
        int menu_id PK "AUTO_INCREMENT"
        date menu_date "NOT NULL"
        int meal_type_id FK "REFERENCES meal_types(meal_type_id)"
        text items "NOT NULL"
        enum dietary_type "VEG, NON-VEG, SPECIAL, ALL"
        text description "NULLable"
        int created_by FK "REFERENCES users(user_id)"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "ON UPDATE CURRENT_TIMESTAMP"
    }

    meal_preferences {
        int preference_id PK "AUTO_INCREMENT"
        int user_id FK "REFERENCES users(user_id)"
        date preference_date "NOT NULL"
        int meal_type_id FK "REFERENCES meal_types(meal_type_id)"
        enum dietary_choice "VEG, NON-VEG, JAIN, SPECIAL, NOT_EATING"
        string special_notes "NULLable"
        timestamp submitted_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "ON UPDATE CURRENT_TIMESTAMP"
    }
```

---

### 2. Relationship Cardinality & Business Rules

1. **`users` to `mess_menu` (1 : N):**
   - One Administrator can publish multiple mess menus over time.
   - Each menu is associated with at most one creating admin (`created_by`).
   - Referential rule: `ON DELETE SET NULL` (historical menus are retained even if an admin account is removed).

2. **`users` to `meal_preferences` (1 : N):**
   - One student can submit multiple meal preferences across different dates and sessions.
   - Each preference belongs to exactly one student (`user_id`).
   - Referential rule: `ON DELETE CASCADE` (if a student account is deleted, their preference records are safely cleaned).

3. **`meal_types` to `mess_menu` (1 : N):**
   - One meal session type (e.g., *Lunch*) can be scheduled across many dates.
   - Each menu record is tied to exactly one meal session (`meal_type_id`).
   - Referential rule: `ON DELETE CASCADE`.

4. **`meal_types` to `meal_preferences` (1 : N):**
   - One meal session type is referenced across many student dietary submissions.
   - Each preference entry specifies exactly one meal session.
   - Referential rule: `ON DELETE CASCADE`.

5. **Composite Unique Constraints:**
   - In `mess_menu`: `UNIQUE (menu_date, meal_type_id)` ensures that no more than one official menu is scheduled for a given meal session on any date.
   - In `meal_preferences`: `UNIQUE (user_id, preference_date, meal_type_id)` guarantees that a student can only have one active preference per meal session on any date.
