# Hospital Management System (HMS)

An industrial-grade desktop application for hospital administration, built with **JavaFX** and **MySQL**. Recently refactored with a modern, data-centric user interface.

## 🚀 Key Features
*   **Command Center Dashboard:** Real-time metrics for total patients, active staff, and bed occupancy.
*   **Industrial UI Design:** Professional Blue/Slate theme, flat design, and consistent typography.
*   **Master-Detail Layouts:** Split-screen interfaces for rapid data entry (replaces slow popup windows).
*   **Core Modules:**
    *   Patient Directory & Medical History
    *   Admissions & Room Management
    *   Staff, Roles, and Departments
    *   Lab Reports & Test Configuration
    *   Appointment Scheduling

## 🛠️ Tech Stack
*   **Language:** Java 22
*   **UI Framework:** JavaFX (FXML + CSS)
*   **Database:** MySQL
*   **Build Tool:** Maven

## ⚡ How to Run
1.  **Database:** Import the SQL schema into your local MySQL server (Database name: `hospital`).
2.  **Configuration:** Update `src/main/java/marc/Connection/DatabaseConnection.java` with your MySQL username and password.
3.  **Launch:**
    ```bash
    mvn clean install
    mvn javafx:run
    ```

## 📸 Screenshots
<img width="1248" height="767" alt="Screenshot_29" src="https://github.com/user-attachments/assets/3bf8b5fd-610e-4189-bbc0-8fe49fc030bc" />
<img width="1252" height="767" alt="Screenshot_28" src="https://github.com/user-attachments/assets/15972599-dbad-46ae-8896-afb36b8f079a" />
<img width="1255" height="767" alt="Screenshot_27" src="https://github.com/user-attachments/assets/e823f87e-9cb2-486f-b6f9-e1f7304b1799" />
