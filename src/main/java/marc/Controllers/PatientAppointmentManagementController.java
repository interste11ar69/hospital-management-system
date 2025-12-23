package marc.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import marc.Class.Patient;
import marc.DAO.PatientAppointmentDAO;
import marc.DAO.EmployeeDAO;
import marc.DAO.PatientDAO;
import marc.View.PatientAppointment;
import marc.Class.Employee;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class PatientAppointmentManagementController {

    @FXML
    private TextField searchAppointmentField;

    @FXML
    private TableView<PatientAppointment> appointmentTable;

    @FXML
    private TableColumn<PatientAppointment, Integer> colAppointmentPatientID;

    @FXML
    private TableColumn<PatientAppointment, String> colAppointmentPatientName;

    @FXML
    private TableColumn<PatientAppointment, Integer> colAppointmentEmployeeID;

    @FXML
    private TableColumn<PatientAppointment, String> colAppointmentEmployeeName;

    @FXML
    private TableColumn<PatientAppointment, String> colAppointmentDate;

    @FXML
    private TableColumn<PatientAppointment, Boolean> colAppointmentIsComplete;

    @FXML
    private TableColumn<PatientAppointment, Boolean> colAppointmentIsCancelled;

    @FXML
    private TableColumn<PatientAppointment, Boolean> colAppointmentIsNoShow;

    @FXML
    private TableColumn<PatientAppointment, String> colAppointmentCreatedOn;

    @FXML
    private ComboBox<Patient> appointmentPatientDropdown;

    @FXML
    private ComboBox<Employee> appointmentEmployeeDropdown;

    @FXML
    private DatePicker appointmentDatePicker;

    @FXML
    private CheckBox appointmentIsCompleteCheckBox;

    @FXML
    private CheckBox appointmentIsCancelledCheckBox;

    @FXML
    private CheckBox appointmentIsNoShowCheckBox;

    private final PatientAppointmentDAO patientAppointmentDAO = new PatientAppointmentDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    private void initialize() {
        initializeColumns();
        loadPatientAppointments();
        populateDropdowns();
        setupCheckboxListeners();
        setupDynamicSearchPatientAppointment();
        setupDropdownRefreshListeners();
        initializeTableClickListener();
    }

    private void initializeColumns() {
        colAppointmentPatientID.setCellValueFactory(cellData -> cellData.getValue().patientIDProperty().asObject());
        colAppointmentPatientName.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientID();
            return new SimpleStringProperty(fetchPatientNameById(patientId));
        });
        colAppointmentEmployeeID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty().asObject());
        colAppointmentEmployeeName.setCellValueFactory(cellData -> {
            int employeeId = cellData.getValue().getEmployeeID();
            return new SimpleStringProperty(fetchEmployeeNameById(employeeId));
        });
        colAppointmentDate.setCellValueFactory(cellData -> cellData.getValue().appointmentDateProperty());
        colAppointmentIsComplete.setCellValueFactory(cellData -> cellData.getValue().isCompleteProperty());
        colAppointmentIsCancelled.setCellValueFactory(cellData -> cellData.getValue().isCancelledProperty());
        colAppointmentIsNoShow.setCellValueFactory(cellData -> cellData.getValue().isNoShowProperty());
        colAppointmentCreatedOn.setCellValueFactory(cellData -> cellData.getValue().createdOnProperty());
    }

    private void setupCheckboxListeners() {
        appointmentIsCompleteCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                appointmentIsCancelledCheckBox.setSelected(false);
                appointmentIsNoShowCheckBox.setSelected(false);
            }
        });

        appointmentIsCancelledCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                appointmentIsCompleteCheckBox.setSelected(false);
                appointmentIsNoShowCheckBox.setSelected(false);
            }
        });

        appointmentIsNoShowCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                appointmentIsCompleteCheckBox.setSelected(false);
                appointmentIsCancelledCheckBox.setSelected(false);
            }
        });
    }

    private void setupDropdownRefreshListeners() {
        appointmentPatientDropdown.setOnMouseClicked(event -> populatePatientDropdown());
        appointmentEmployeeDropdown.setOnMouseClicked(event -> populateEmployeeDropdown());
    }

    private void populateDropdowns() {
        populatePatientDropdown();
        populateEmployeeDropdown();
    }

    private void populatePatientDropdown() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();

            appointmentPatientDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Patient patient) {
                    return patient != null ? patient.getFirstName() + " " + patient.getLastName() + " (ID: " + patient.getPatientID() + ")" : "";
                }

                @Override
                public Patient fromString(String string) {
                    return null; // Not needed for this use case
                }
            });

            appointmentPatientDropdown.setItems(FXCollections.observableArrayList(patients));
        } catch (SQLException e) {
            showError("Error loading patients for dropdown: " + e.getMessage());
        }
    }

    private void populateEmployeeDropdown() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            appointmentEmployeeDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Employee employee) {
                    return employee != null ? employee.getEmployeeName() + " (ID: " + employee.getEmployeeID() + ")" : "";
                }

                @Override
                public Employee fromString(String string) {
                    return null;
                }
            });
            appointmentEmployeeDropdown.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            showError("Error loading employees for dropdown: " + e.getMessage());
        }
    }

    private String fetchPatientNameById(int patientId) {
        try {
            return patientDAO.getPatientNameById(patientId);
        } catch (SQLException e) {
            showError("Error fetching patient name for ID: " + patientId + ". " + e.getMessage());
            return "Unknown";
        }
    }

    private String fetchEmployeeNameById(int employeeId) {
        try {
            return employeeDAO.getEmployeeNameById(employeeId);
        } catch (SQLException e) {
            showError("Error fetching employee name for ID: " + employeeId + ". " + e.getMessage());
            return "Unknown";
        }
    }

    private void loadPatientAppointments() {
        try {
            List<PatientAppointment> appointments = patientAppointmentDAO.getAllPatientAppointments();
            appointmentTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (SQLException e) {
            showError("Error loading appointments: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAppointment(ActionEvent actionEvent) {
        try {
            Patient selectedPatient = appointmentPatientDropdown.getValue();
            Employee selectedEmployee = appointmentEmployeeDropdown.getValue();
            LocalDate appointmentDate = appointmentDatePicker.getValue();

            if (selectedPatient == null || selectedEmployee == null || appointmentDate == null) {
                showError("Please fill in all fields.");
                return;
            }

            PatientAppointment newAppointment = new PatientAppointment();
            newAppointment.setPatientID(selectedPatient.getPatientID());
            newAppointment.setEmployeeID(selectedEmployee.getEmployeeID());
            newAppointment.setAppointmentDate(appointmentDate.toString());
            newAppointment.setIsComplete(appointmentIsCompleteCheckBox.isSelected());
            newAppointment.setIsCancelled(appointmentIsCancelledCheckBox.isSelected());
            newAppointment.setIsNoShow(appointmentIsNoShowCheckBox.isSelected());

            patientAppointmentDAO.createPatientAppointment(newAppointment);
            loadPatientAppointments();
            clearFormFields();
        } catch (SQLException e) {
            showError("Error adding appointment: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAppointment(ActionEvent actionEvent) {
        PatientAppointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            showError("Please select an appointment to update.");
            return;
        }

        try {
            Patient selectedPatient = appointmentPatientDropdown.getValue();
            Employee selectedEmployee = appointmentEmployeeDropdown.getValue();
            LocalDate appointmentDate = appointmentDatePicker.getValue();

            if (selectedPatient != null) selectedAppointment.setPatientID(selectedPatient.getPatientID());
            if (selectedEmployee != null) selectedAppointment.setEmployeeID(selectedEmployee.getEmployeeID());
            if (appointmentDate != null) selectedAppointment.setAppointmentDate(appointmentDate.toString());

            selectedAppointment.setIsComplete(appointmentIsCompleteCheckBox.isSelected());
            selectedAppointment.setIsCancelled(appointmentIsCancelledCheckBox.isSelected());
            selectedAppointment.setIsNoShow(appointmentIsNoShowCheckBox.isSelected());

            patientAppointmentDAO.updatePatientAppointment(selectedAppointment, selectedAppointment.getAppointmentDate());
            loadPatientAppointments();
            clearFormFields();
        } catch (SQLException e) {
            showError("Error updating appointment: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAppointment(ActionEvent actionEvent) {
        try {
            PatientAppointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                showError("Please select an appointment to delete.");
                return;
            }

            patientAppointmentDAO.deletePatientAppointment(selectedAppointment.getAppointmentDate());
            loadPatientAppointments();
        } catch (SQLException e) {
            showError("Error deleting appointment: " + e.getMessage());
        }
    }

    private void setupDynamicSearchPatientAppointment() {
        searchAppointmentField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPatientAppointments(newValue);
        });
    }

    private void filterPatientAppointments(String searchQuery) {
        try {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                loadPatientAppointments();
                return;
            }

            String lowerCaseQuery = searchQuery.trim().toLowerCase();

            List<PatientAppointment> allAppointments = patientAppointmentDAO.getAllPatientAppointments();

            List<PatientAppointment> filteredAppointments = allAppointments.stream()
                    .filter(appointment -> {
                        boolean matchesPatientID = false;
                        boolean matchesEmployeeID = false;
                        boolean matchesDate = false;
                        boolean matchesPatientName = false;
                        boolean matchesEmployeeName = false;

                        try {
                            int patientID = Integer.parseInt(lowerCaseQuery);
                            matchesPatientID = appointment.getPatientID() == patientID;
                        } catch (NumberFormatException e) {
                            // Ignore if not a number
                        }

                        try {
                            int employeeID = Integer.parseInt(lowerCaseQuery);
                            matchesEmployeeID = appointment.getEmployeeID() == employeeID;
                        } catch (NumberFormatException e) {
                            // Ignore if not a number
                        }

                        if (appointment.getAppointmentDate() != null) {
                            matchesDate = appointment.getAppointmentDate().toLowerCase().contains(lowerCaseQuery);
                        }

                        String patientName = fetchPatientNameById(appointment.getPatientID()).toLowerCase();
                        String employeeName = fetchEmployeeNameById(appointment.getEmployeeID()).toLowerCase();

                        matchesPatientName = patientName.contains(lowerCaseQuery);
                        matchesEmployeeName = employeeName.contains(lowerCaseQuery);

                        return matchesPatientID || matchesEmployeeID || matchesDate || matchesPatientName || matchesEmployeeName;
                    })
                    .collect(Collectors.toList());

            appointmentTable.setItems(FXCollections.observableArrayList(filteredAppointments));
        } catch (SQLException e) {
            showError("Error filtering appointments: " + e.getMessage());
        }
    }


    private void initializeTableClickListener() {
        appointmentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                populateAppointmentForm();
            }
        });
    }

    private void populateAppointmentForm() {
        PatientAppointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            for (Patient patient : appointmentPatientDropdown.getItems()) {
                if (patient.getPatientID() == selectedAppointment.getPatientID()) {
                    appointmentPatientDropdown.setValue(patient);
                    break;
                }
            }

            for (Employee emp : appointmentEmployeeDropdown.getItems()) {
                if (emp.getEmployeeID() == selectedAppointment.getEmployeeID()) {
                    appointmentEmployeeDropdown.setValue(emp);
                    break;
                }
            }

            try {
                appointmentDatePicker.setValue(LocalDate.parse(selectedAppointment.getAppointmentDate()));
            } catch (DateTimeParseException e) {
                showError("Invalid appointment date format: " + e.getMessage());
            }

            appointmentIsCompleteCheckBox.setSelected(selectedAppointment.getIsComplete());
            appointmentIsCancelledCheckBox.setSelected(selectedAppointment.getIsCancelled());
            appointmentIsNoShowCheckBox.setSelected(selectedAppointment.getIsNoShow());
        }
    }

    private void clearFormFields() {
        appointmentPatientDropdown.getSelectionModel().clearSelection();
        appointmentEmployeeDropdown.getSelectionModel().clearSelection();
        appointmentDatePicker.setValue(null);
        appointmentIsCompleteCheckBox.setSelected(false);
        appointmentIsCancelledCheckBox.setSelected(false);
        appointmentIsNoShowCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
