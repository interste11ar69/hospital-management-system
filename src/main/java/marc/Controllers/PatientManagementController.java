package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import marc.Class.Patient;
import marc.DAO.PatientDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientManagementController {

    // FXML Elements linked to the View
    @FXML private VBox patientPanel; // Left Side (List)
    @FXML private VBox registerPatientPanel; // Right Side (Form)

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colPatientID;
    @FXML private TableColumn<Patient, String> colFirstNamePatient;
    @FXML private TableColumn<Patient, String> colLastNamePatient;
    @FXML private TableColumn<Patient, String> colEmailPatient;
    @FXML private TableColumn<Patient, String> colPhoneNumberPatient;
    @FXML private TableColumn<Patient, LocalDate> colDateOfBirthPatient;
    @FXML private TableColumn<Patient, String> colGenderPatient;
    @FXML private TableColumn<Patient, Double> colHeightPatient;
    @FXML private TableColumn<Patient, Double> colWeightPatient;
    @FXML private TableColumn<Patient, String> colBloodGroupPatient;
    @FXML private TableColumn<Patient, Boolean> colIsActivePatient;

    @FXML private TextField firstNameFieldPatient, lastNameFieldPatient, emailFieldPatient, phoneNumberFieldPatient, searchPatientField, heightFieldPatient, weightFieldPatient;
    @FXML private ComboBox<String> genderDropdownPatient, bloodGroupDropdownPatient;
    @FXML private DatePicker dateOfBirthPickerPatient;
    @FXML private CheckBox isActiveCheckboxPatient;

    // Buttons
    @FXML private Button submitButtonPatient;
    @FXML private Button btnDeletePatient; // We will enable/disable this

    // Data Handling
    private final PatientDAO patientDAO = new PatientDAO();
    private ObservableList<Patient> patientsData = FXCollections.observableArrayList();
    private Patient selectedPatient = null; // Tracks who is currently being edited

    @FXML
    private void initialize() {
        setupTableColumns();
        loadPatients();
        setupDynamicPatientSearch();
        setupTableSelectionListener();
        prepareNewEntry();
    }

    private void setupTableColumns() {
        // Standard String Columns
        colFirstNamePatient.setCellValueFactory(data -> data.getValue().firstNameProperty());
        colLastNamePatient.setCellValueFactory(data -> data.getValue().lastNameProperty());
        colGenderPatient.setCellValueFactory(data -> data.getValue().genderProperty()); // Now visible

        // --- FIXED STATUS COLUMN (No more "true/false") ---
        colIsActivePatient.setCellValueFactory(data -> data.getValue().isActiveProperty());
        colIsActivePatient.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean isActive, boolean empty) {
                super.updateItem(isActive, empty);
                if (empty || isActive == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // 1. Set Text
                    setText(isActive ? "Active" : "Inactive");

                    // 2. Set Color (Green for Active, Red for Inactive)
                    if (isActive) {
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // These ID columns are needed for logic but hidden
        colPatientID.setCellValueFactory(data -> data.getValue().patientIDProperty().asObject());
        colEmailPatient.setCellValueFactory(data -> data.getValue().emailIDProperty());
        colPhoneNumberPatient.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        colDateOfBirthPatient.setCellValueFactory(data -> data.getValue().dateOfBirthProperty());
        colHeightPatient.setCellValueFactory(data -> data.getValue().heightProperty().asObject());
        colWeightPatient.setCellValueFactory(data -> data.getValue().weightProperty().asObject());
        colBloodGroupPatient.setCellValueFactory(data -> data.getValue().bloodGroupProperty());
    }

    // 2. LOAD DATA
    private void loadPatients() {
        try {
            List<Patient> patientList = patientDAO.getAllPatients();
            patientsData.setAll(patientList);
            patientTable.setItems(patientsData);
        } catch (SQLException e) {
            showError("Error loading patients: " + e.getMessage());
        }
    }

    // 3. MASTER-DETAIL LISTENER (The Magic Part)
    private void setupTableSelectionListener() {
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A row was clicked: Populate the form
                selectedPatient = newSelection;
                populateForm(selectedPatient);

                // Change UI state to "Edit Mode"
                submitButtonPatient.setText("Update Record");
                btnDeletePatient.setDisable(false); // Enable delete
            }
        });
    }

    // Fill the right-side form with data
    private void populateForm(Patient p) {
        firstNameFieldPatient.setText(p.getFirstName());
        lastNameFieldPatient.setText(p.getLastName());
        emailFieldPatient.setText(p.getEmailID());
        phoneNumberFieldPatient.setText(p.getPhoneNumber());
        genderDropdownPatient.setValue(p.getGender());
        dateOfBirthPickerPatient.setValue(p.getDateOfBirth());
        heightFieldPatient.setText(String.valueOf(p.getHeight()));
        weightFieldPatient.setText(String.valueOf(p.getWeight()));
        bloodGroupDropdownPatient.setValue(p.getBloodGroup());
        isActiveCheckboxPatient.setSelected(p.getIsActive());
    }

    // 4. "NEW PATIENT" BUTTON LOGIC
    @FXML
    private void showRegisterPatientPanel() {
        prepareNewEntry();
    }

    // Clears the form so you can type a new person
    private void prepareNewEntry() {
        selectedPatient = null; // No patient selected
        patientTable.getSelectionModel().clearSelection(); // Deselect table

        // Clear fields
        firstNameFieldPatient.clear();
        lastNameFieldPatient.clear();
        emailFieldPatient.clear();
        phoneNumberFieldPatient.clear();
        genderDropdownPatient.setValue(null);
        dateOfBirthPickerPatient.setValue(null);
        heightFieldPatient.clear();
        weightFieldPatient.clear();
        bloodGroupDropdownPatient.setValue(null);
        isActiveCheckboxPatient.setSelected(true); // Default to Active

        // UI State: "Add Mode"
        submitButtonPatient.setText("Save New Patient");
        btnDeletePatient.setDisable(true); // Cannot delete a new entry

        // Ensure both panels are visible (Fixes "Table Disappears" bug)
        patientPanel.setVisible(true);
        registerPatientPanel.setVisible(true);
    }

    // 5. SAVE / UPDATE LOGIC
    @FXML
    private void handleSubmitPatient() {
        try {
            // Collect Data
            String firstName = firstNameFieldPatient.getText().trim();
            String lastName = lastNameFieldPatient.getText().trim();
            // ... (Basic validation)
            if (firstName.isEmpty() || lastName.isEmpty()) {
                showError("Name fields are required.");
                return;
            }

            if (selectedPatient != null) {
                // UPDATE EXISTING
                updatePatientObject(selectedPatient); // Helper to set fields
                patientDAO.updatePatient(selectedPatient);
                showInfo("Patient record updated.");
            } else {
                // CREATE NEW
                Patient newPatient = new Patient(0, firstName, lastName,
                        dateOfBirthPickerPatient.getValue(),
                        genderDropdownPatient.getValue(),
                        phoneNumberFieldPatient.getText(),
                        emailFieldPatient.getText(),
                        parseDoub(heightFieldPatient.getText()),
                        parseDoub(weightFieldPatient.getText()),
                        bloodGroupDropdownPatient.getValue(),
                        isActiveCheckboxPatient.isSelected(), null, null);

                patientDAO.createPatient(newPatient);
                showInfo("New patient registered.");
            }

            // Refresh List
            loadPatients();
            prepareNewEntry(); // Reset form

        } catch (SQLException e) {
            showError("Database Error: " + e.getMessage());
        }
    }

    private void updatePatientObject(Patient p) {
        p.setFirstName(firstNameFieldPatient.getText());
        p.setLastName(lastNameFieldPatient.getText());
        p.setEmailID(emailFieldPatient.getText());
        p.setPhoneNumber(phoneNumberFieldPatient.getText());
        p.setGender(genderDropdownPatient.getValue());
        p.setDateOfBirth(dateOfBirthPickerPatient.getValue());
        p.setHeight(parseDoub(heightFieldPatient.getText()));
        p.setWeight(parseDoub(weightFieldPatient.getText()));
        p.setBloodGroup(bloodGroupDropdownPatient.getValue());
        p.setIsActive(isActiveCheckboxPatient.isSelected());
    }

    // 6. DELETE LOGIC
    @FXML
    private void handleDeletePatient() {
        if (selectedPatient == null) return;

        try {
            patientDAO.deletePatient(selectedPatient.getPatientID());
            showInfo("Record deleted.");
            loadPatients();
            prepareNewEntry();
        } catch (SQLException e) {
            showError("Could not delete: " + e.getMessage());
        }
    }

    // Helper for safe double parsing
    private double parseDoub(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }

    // Unused methods kept empty to prevent FXML errors if old buttons still link to them
    @FXML private void editPatient() {}
    @FXML private void showPatientPanel() {}

    // Search Logic
    private void setupDynamicPatientSearch() {
        searchPatientField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                patientTable.setItems(patientsData);
            } else {
                String lower = newVal.toLowerCase();
                List<Patient> filtered = patientsData.stream()
                        .filter(p -> p.getFirstName().toLowerCase().contains(lower)
                                || p.getLastName().toLowerCase().contains(lower))
                        .toList();
                patientTable.setItems(FXCollections.observableArrayList(filtered));
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}