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

    // FXML Elements
    @FXML
    private VBox patientPanel, registerPatientPanel;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> colPatientID;
    @FXML
    private TableColumn<Patient, String> colFirstNamePatient;
    @FXML
    private TableColumn<Patient, String> colLastNamePatient;
    @FXML
    private TableColumn<Patient, String> colEmailPatient;
    @FXML
    private TableColumn<Patient, String> colPhoneNumberPatient;
    @FXML
    private TableColumn<Patient, LocalDate> colDateOfBirthPatient;
    @FXML
    private TableColumn<Patient, String> colGenderPatient;
    @FXML
    private TableColumn<Patient, Double> colHeightPatient;
    @FXML
    private TableColumn<Patient, Double> colWeightPatient;
    @FXML
    private TableColumn<Patient, String> colBloodGroupPatient;
    @FXML
    private TableColumn<Patient, Boolean> colIsActivePatient;

    @FXML
    private TextField firstNameFieldPatient, lastNameFieldPatient, emailFieldPatient, phoneNumberFieldPatient, searchPatientField, heightFieldPatient, weightFieldPatient;
    @FXML
    private ComboBox<String> genderDropdownPatient, bloodGroupDropdownPatient;
    @FXML
    private DatePicker dateOfBirthPickerPatient;
    @FXML
    private CheckBox isActiveCheckboxPatient;
    @FXML
    private Button submitButtonPatient;

    // DAO for database operations
    private final PatientDAO patientDAO = new PatientDAO();

    // Observable list to hold patient data
    private ObservableList<Patient> patientsData;

    // Selected patient for update
    private Patient selectedPatient;

    @FXML
    private void initialize() {
        setupTableColumns();
        loadPatients();
        setupDynamicPatientSearch();
        submitButtonPatient.setText("Submit"); // Default action for new patients
    }

    // Set up table columns
    private void setupTableColumns() {
        colPatientID.setCellValueFactory(data -> data.getValue().patientIDProperty().asObject());
        colFirstNamePatient.setCellValueFactory(data -> data.getValue().firstNameProperty());
        colLastNamePatient.setCellValueFactory(data -> data.getValue().lastNameProperty());
        colEmailPatient.setCellValueFactory(data -> data.getValue().emailIDProperty());
        colPhoneNumberPatient.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        colDateOfBirthPatient.setCellValueFactory(data -> data.getValue().dateOfBirthProperty());
        colGenderPatient.setCellValueFactory(data -> data.getValue().genderProperty());
        colHeightPatient.setCellValueFactory(data -> data.getValue().heightProperty().asObject());
        colWeightPatient.setCellValueFactory(data -> data.getValue().weightProperty().asObject());
        colBloodGroupPatient.setCellValueFactory(data -> data.getValue().bloodGroupProperty());
        colIsActivePatient.setCellValueFactory(data -> data.getValue().isActiveProperty().asObject());
    }

    // Load patients from the database
    private void loadPatients() {
        try {
            List<Patient> patientList = patientDAO.getAllPatients();
            patientsData = FXCollections.observableArrayList(patientList);
            patientTable.setItems(patientsData);
        } catch (SQLException e) {
            showError("Error loading patients: " + e.getMessage());
        }
    }

    // Handle patient submission (new or update)
    @FXML
    private void handleSubmitPatient() {
        try {
            // Collect form data
            String firstName = firstNameFieldPatient.getText().trim();
            String lastName = lastNameFieldPatient.getText().trim();
            String email = emailFieldPatient.getText().trim();
            String phoneNumber = phoneNumberFieldPatient.getText().trim();
            String gender = genderDropdownPatient.getValue();
            LocalDate dateOfBirth = dateOfBirthPickerPatient.getValue();
            Double height = heightFieldPatient.getText().isEmpty() ? 0 : Double.parseDouble(heightFieldPatient.getText().trim());
            Double weight = weightFieldPatient.getText().isEmpty() ? 0 : Double.parseDouble(weightFieldPatient.getText().trim());
            String bloodGroup = bloodGroupDropdownPatient.getValue();
            boolean isActive = isActiveCheckboxPatient.isSelected();

            // Validate required fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()
                    || gender == null || dateOfBirth == null || bloodGroup == null) {
                showError("Please fill in all required fields.");
                return;
            }

            if ("Update".equalsIgnoreCase(submitButtonPatient.getText())) {
                // Handle update
                if (selectedPatient == null) {
                    showError("No patient selected for update.");
                    return;
                }

                // Update patient fields
                selectedPatient.setFirstName(firstName);
                selectedPatient.setLastName(lastName);
                selectedPatient.setEmailID(email);
                selectedPatient.setPhoneNumber(phoneNumber);
                selectedPatient.setGender(gender);
                selectedPatient.setDateOfBirth(dateOfBirth);
                selectedPatient.setHeight(height);
                selectedPatient.setWeight(weight);
                selectedPatient.setBloodGroup(bloodGroup);
                selectedPatient.setIsActive(isActive);

                // Update patient in database
                patientDAO.updatePatient(selectedPatient);

                // Reload table and clear form
                loadPatients();
                clearFormPatient();
                showPatientPanel();
                showInfo("Patient updated successfully.");

            } else {
                // Handle new patient registration
                Patient newPatient = new Patient(
                        0, // PatientID will be auto-generated by the database
                        firstName, lastName, dateOfBirth, gender, phoneNumber,
                        email, height, weight, bloodGroup, isActive, null, null
                );

                // Insert the new patient into the database
                patientDAO.createPatient(newPatient);

                // Reload table and clear form
                loadPatients();
                clearFormPatient();
                showPatientPanel();
                showInfo("Patient registered successfully.");
            }
        } catch (NumberFormatException e) {
            showError("Invalid numeric input for height or weight.");
        } catch (SQLException e) {
            showError("Error saving patient: " + e.getMessage());
        }
    }

    // Edit a patient
    @FXML
    private void editPatient() {
        selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showError("Please select a patient to edit.");
            return;
        }

        // Populate the form with the selected patient's data
        firstNameFieldPatient.setText(selectedPatient.getFirstName());
        lastNameFieldPatient.setText(selectedPatient.getLastName());
        emailFieldPatient.setText(selectedPatient.getEmailID());
        phoneNumberFieldPatient.setText(selectedPatient.getPhoneNumber());
        genderDropdownPatient.setValue(selectedPatient.getGender());
        dateOfBirthPickerPatient.setValue(selectedPatient.getDateOfBirth());
        heightFieldPatient.setText(String.valueOf(selectedPatient.getHeight()));
        weightFieldPatient.setText(String.valueOf(selectedPatient.getWeight()));
        bloodGroupDropdownPatient.setValue(selectedPatient.getBloodGroup());
        isActiveCheckboxPatient.setSelected(selectedPatient.getIsActive());

        // Change the submit button text to "Update"
        submitButtonPatient.setText("Update");

        // Show the registration panel
        showPanel(registerPatientPanel);
    }

    // Delete a patient
    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showError("Please select a patient to delete.");
            return;
        }

        try {
            patientDAO.deletePatient(selectedPatient.getPatientID());
            loadPatients(); // Reload the patient list
            showInfo("Patient deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting patient: " + e.getMessage());
        }
    }

    // Clear the patient form
    private void clearFormPatient() {
        firstNameFieldPatient.clear();
        lastNameFieldPatient.clear();
        emailFieldPatient.clear();
        phoneNumberFieldPatient.clear();
        genderDropdownPatient.setValue(null);
        dateOfBirthPickerPatient.setValue(null);
        heightFieldPatient.clear();
        weightFieldPatient.clear();
        bloodGroupDropdownPatient.setValue(null);

        submitButtonPatient.setText("Submit");
    }

    // Show the patient management panel
    @FXML
    private void showPatientPanel() {
        showPanel(patientPanel);
    }

    // Show a specific panel
    private void showPanel(VBox panelToShow) {
        patientPanel.setVisible(false);
        registerPatientPanel.setVisible(false);
        panelToShow.setVisible(true);
    }

    // Show an error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an informational message
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Dynamic search for patients
    private void setupDynamicPatientSearch() {
        searchPatientField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                patientTable.setItems(patientsData);
            } else {
                String lowerCaseQuery = newValue.toLowerCase();

                List<Patient> filteredPatients = patientsData.stream()
                        .filter(patient -> {
                            // Combine first and last name for full name search
                            String fullName = (patient.getFirstName() + " " + patient.getLastName()).toLowerCase();

                            return patient.getFirstName().toLowerCase().contains(lowerCaseQuery)
                                    || patient.getLastName().toLowerCase().contains(lowerCaseQuery)
                                    || fullName.contains(lowerCaseQuery); // Match full name
                        })
                        .toList();

                patientTable.setItems(FXCollections.observableArrayList(filteredPatients));
            }
        });
    }

    // Show the Register Patient panel
    @FXML
    private void showRegisterPatientPanel() {
        // Hide the Patient Management Panel
        patientPanel.setVisible(false);

        // Clear the form fields in the Register Patient Panel
        clearFormPatient();

        // Show the Register Patient Panel
        registerPatientPanel.setVisible(true);
    }




}
