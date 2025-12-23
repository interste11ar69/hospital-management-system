package marc.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import marc.Class.Patient;
import marc.Class.PatientRegister;
import marc.DAO.PatientRegisterDAO;
import marc.DAO.PatientDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PatientRegisterManagementController {

    @FXML
    private VBox RegisteredpatientPanel;

    @FXML
    private VBox AdmitPatient;

    @FXML
    private TableView<PatientRegister> patientRegisterTable;

    @FXML
    private TableColumn<PatientRegister, Integer> colPatientRegisterID;

    @FXML
    private TableColumn<PatientRegister, Integer> colPatientId;

    @FXML
    private TableColumn<PatientRegister, String> colPatientName; // New column for patient name

    @FXML
    private TableColumn<PatientRegister, LocalDate> colAdmittedOn;

    @FXML
    private TableColumn<PatientRegister, LocalDate> colDischargeOn;

    @FXML
    private TableColumn<PatientRegister, String> colRoomNumber;

    @FXML
    private TableColumn<PatientRegister, Boolean> colIsActive;

    @FXML
    private TableColumn<PatientRegister, String> colCreatedOn;

    @FXML
    private TableColumn<PatientRegister, String> colModifiedOn;

    @FXML
    private TextField searchRegisteredPatientField;

    @FXML
    private ComboBox<String> patientDropdown; // For selecting patients from a dropdown

    @FXML
    private TextField patientIDField;

    @FXML
    private DatePicker admittedOnPicker;

    @FXML
    private DatePicker dischargeOnPicker;

    @FXML
    private TextField roomNumberField;

    @FXML
    private CheckBox isActiveCheckbox;

    @FXML
    private Button submitPatientRegisterButton;

    private PatientRegister selectedPatientRegister;

    private final PatientRegisterDAO patientRegisterDAO = new PatientRegisterDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    private void initialize() {
        // Initialize table columns
        colPatientRegisterID.setCellValueFactory(cellData -> cellData.getValue().patientRegisterIdProperty().asObject());
        colPatientId.setCellValueFactory(cellData -> cellData.getValue().patientIdProperty().asObject());
        colPatientName.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            return new SimpleStringProperty(getPatientNameById(patientId));
        });
        colAdmittedOn.setCellValueFactory(cellData -> cellData.getValue().admittedOnProperty());
        colDischargeOn.setCellValueFactory(cellData -> cellData.getValue().dischargeOnProperty());
        colRoomNumber.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        colCreatedOn.setCellValueFactory(cellData -> cellData.getValue().createdOnProperty());
        colModifiedOn.setCellValueFactory(cellData -> cellData.getValue().modifiedOnProperty());
        colIsActive.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());

        // Load patient register data and populate the patient dropdown
        loadPatientRegisters();
        populatePatientDropdown();
        setupDynamicSearchPatientRegister();
    }

    @FXML
    private void handleSubmitPatientRegister() {
        try {
            String selectedPatient = patientDropdown.getValue();
            if (selectedPatient == null || selectedPatient.trim().isEmpty()) {
                showError("Please select a patient from the dropdown.");
                return;
            }

            int patientID = Integer.parseInt(selectedPatient.split(" - ")[0]); // Extract ID from dropdown
            LocalDate admittedOn = admittedOnPicker.getValue();
            LocalDate dischargeOn = dischargeOnPicker.getValue();
            String roomNumber = roomNumberField.getText().trim();
            boolean isActive = isActiveCheckbox.isSelected();

            if (admittedOn == null || roomNumber.isEmpty()) {
                showError("Please fill in all required fields.");
                return;
            }

            if ("Update Patient Register".equalsIgnoreCase(submitPatientRegisterButton.getText())) {
                if (selectedPatientRegister == null) {
                    showError("No patient register selected for update.");
                    return;
                }

                selectedPatientRegister.setPatientId(patientID);
                selectedPatientRegister.setAdmittedOn(admittedOn);
                selectedPatientRegister.setDischargeOn(dischargeOn);
                selectedPatientRegister.setRoomNumber(roomNumber);
                selectedPatientRegister.setIsActive(isActive);

                patientRegisterDAO.updatePatientRegister(selectedPatientRegister);

                // Update patient IsActive status in the patient table
                patientDAO.updatePatientIsActive(patientID, isActive);

                showInfo("Patient register updated successfully.");
            } else {
                // Automatically set "Is Active" to true for new registrations
                isActiveCheckbox.setSelected(true);

                PatientRegister newRegister = new PatientRegister(
                        0, patientID, admittedOn, dischargeOn, roomNumber, true, null, null
                );

                patientRegisterDAO.createPatientRegister(newRegister);

                // Set patient as active in the patient table
                patientDAO.updatePatientIsActive(patientID, true);

                showInfo("Patient register added successfully.");
            }

            loadPatientRegisters();
            clearFormAdmitPatient();
            showRegisteredPatientPanel();

        } catch (NumberFormatException e) {
            showError("Invalid input: Patient ID must be a number.");
        } catch (SQLException e) {
            showError("Error saving patient register: " + e.getMessage());
        }
    }

    @FXML
    private void showRegisteredPatientPanel() {
        AdmitPatient.setVisible(false);
        RegisteredpatientPanel.setVisible(true);
    }

    @FXML
    private void handleDeletePatientRegister() {

        selectedPatientRegister = patientRegisterTable.getSelectionModel().getSelectedItem();

        if (selectedPatientRegister == null) {
            showError("Please select a patient register to delete.");
            return;
        }

        try {
            patientRegisterDAO.deletePatientRegister(selectedPatientRegister.getPatientRegisterId());

            loadPatientRegisters();
            showInfo("Patient register deleted successfully.");

            selectedPatientRegister = null;

        } catch (SQLException e) {
            showError("Error deleting patient register: " + e.getMessage());
        }

    }

    @FXML
    private void editPatientRegister() {
        selectedPatientRegister = patientRegisterTable.getSelectionModel().getSelectedItem();

        if (selectedPatientRegister == null) {
            showError("Please select a patient register to edit.");
            return;
        }

        // Populate form with selected patient's details
        patientDropdown.setValue(selectedPatientRegister.getPatientId() + " - " + getPatientNameById(selectedPatientRegister.getPatientId()));
        admittedOnPicker.setValue(selectedPatientRegister.getAdmittedOn());
        dischargeOnPicker.setValue(selectedPatientRegister.getDischargeOn());
        roomNumberField.setText(selectedPatientRegister.getRoomNumber());
        isActiveCheckbox.setSelected(selectedPatientRegister.getIsActive());
        lockFieldsExceptDischargeDate();
        isActiveCheckbox.setSelected(false);

        submitPatientRegisterButton.setText("Update Patient Register");
        showPanel(AdmitPatient);
    }



    @FXML
    private void showAdmitPatientPanel(ActionEvent actionEvent) {
        patientDropdown.setDisable(false);
        roomNumberField.setDisable(false);
        clearFormAdmitPatient();
        prepareFormForNewRegistration();
        showPanel(AdmitPatient);
    }

    private void prepareFormForNewRegistration() {
        admittedOnPicker.setDisable(false); // Enable admission date for new registration
        admittedOnPicker.setValue(LocalDate.now()); // Default admission date to today
        dischargeOnPicker.setDisable(true); // Lock discharge date
        dischargeOnPicker.setValue(null); // Clear discharge date
        isActiveCheckbox.setSelected(true); // Automatically set "Is Active" to true
        isActiveCheckbox.setDisable(true); // Make "Is Active" read-only
        submitPatientRegisterButton.setText("Submit");
    }

    private void setupDynamicSearchPatientRegister() {
        searchRegisteredPatientField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPatientRegisters(newValue);
        });
    }

    private void filterPatientRegisters(String searchQuery) {
        try {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                loadPatientRegisters();
                return;
            }

            String lowerCaseQuery = searchQuery.trim().toLowerCase();

            // Fetch all PatientRegisters and Patients
            List<PatientRegister> allRegisters = patientRegisterDAO.getAllPatientRegisters();
            List<Patient> allPatients = patientDAO.getAllPatients();

            // Filter PatientRegisters based on the search query
            List<PatientRegister> filteredRegisters = allRegisters.stream()
                    .filter(register -> {
                        boolean matchesId = false;
                        boolean matchesPatientName = false;

                        // Match PatientRegisterId or PatientId if the query is numeric
                        try {
                            int id = Integer.parseInt(lowerCaseQuery);
                            matchesId = register.getPatientRegisterId() == id || register.getPatientId() == id;
                        } catch (NumberFormatException e) {
                            // Ignore if query is not numeric
                        }

                        // Match Patient Name
                        String patientName = getPatientNameById(register.getPatientId(), allPatients); // Fetch patient name using PatientID
                        if (patientName != null) {
                            matchesPatientName = patientName.toLowerCase().contains(lowerCaseQuery);
                        }

                        return matchesId || matchesPatientName;
                    })
                    .collect(Collectors.toList());

            // Update the TableView with filtered results
            patientRegisterTable.setItems(FXCollections.observableArrayList(filteredRegisters));
        } catch (SQLException e) {
            showError("Error filtering patient registers: " + e.getMessage());
        }
    }

    // Helper method to fetch Patient Name by PatientID from the list of all patients
    private String getPatientNameById(int patientId, List<Patient> allPatients) {
        return allPatients.stream()
                .filter(patient -> patient.getPatientID() == patientId)
                .map(patient -> patient.getFirstName() + " " + patient.getLastName())
                .findFirst()
                .orElse("Unknown");
    }



    private void loadPatientRegisters() {
        try {
            List<PatientRegister> registers = patientRegisterDAO.getAllPatientRegisters();
            patientRegisterTable.setItems(FXCollections.observableArrayList(registers));
        } catch (SQLException e) {
            showError("Error loading patient registers: " + e.getMessage());
        }
    }

    private void populatePatientDropdown() {
        try {
            List<String> patients = patientDAO.getAllPatients()
                    .stream()
                    .map(patient -> patient.getPatientID() + " - " + patient.getFirstName() + " " + patient.getLastName())
                    .collect(Collectors.toList());

            patientDropdown.setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            showError("Error loading patients for dropdown: " + e.getMessage());
        }
    }

    private String getPatientNameById(int patientId) {
        try {
            return patientDAO.getPatientNameById(patientId);
        } catch (SQLException e) {
            showError("Error fetching patient name: " + e.getMessage());
            return "Unknown";
        }
    }

    private void clearFormAdmitPatient() {
        patientDropdown.setValue(null);
        admittedOnPicker.setValue(null);
        dischargeOnPicker.setValue(null);
        roomNumberField.clear();
        isActiveCheckbox.setSelected(false);
        submitPatientRegisterButton.setText("Submit");
    }

    private void showPanel(VBox panel) {
        RegisteredpatientPanel.setVisible(false);
        AdmitPatient.setVisible(false);
        panel.setVisible(true);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void lockFieldsExceptDischargeDate() {
        patientDropdown.setDisable(true);
        admittedOnPicker.setDisable(true);
        roomNumberField.setDisable(true);
        isActiveCheckbox.setDisable(true);
        dischargeOnPicker.setDisable(false);
    }



}
