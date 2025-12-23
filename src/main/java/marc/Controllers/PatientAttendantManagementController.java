package marc.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import marc.Class.Employee;
import marc.Class.PatientRegister;
import marc.Class.PatientAttendant;
import marc.DAO.EmployeeDAO;
import marc.DAO.PatientRegisterDAO;
import marc.DAO.PatientAttendantDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PatientAttendantManagementController {

    @FXML
    private TextField searchAttendantField;

    @FXML
    private TableView<PatientAttendant> patientAttendantTable;

    @FXML
    private TableColumn<PatientAttendant, Integer> colPatientRegisterIDAttendant;

    @FXML
    private TableColumn<PatientAttendant, String> colPatientNameAttendant;

    @FXML
    private TableColumn<PatientAttendant, Integer> colEmployeeID;

    @FXML
    private TableColumn<PatientAttendant, String> colEmployeeNameAttendant;

    @FXML
    private TableColumn<PatientAttendant, Boolean> colIsActiveAttendant;

    @FXML
    private TableColumn<PatientAttendant, String> colCreatedOnAttendant;

    @FXML
    private TableColumn<PatientAttendant, String> colModifiedOnAttendant;

    @FXML
    private ComboBox<PatientRegister> patientRegisterIDAttendantDropdown;

    @FXML
    private ComboBox<Employee> patientAttendantDropdown;

    @FXML
    private CheckBox isActiveAttendantCheckBox;

    private final PatientAttendantDAO patientAttendantDAO = new PatientAttendantDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final PatientRegisterDAO patientRegisterDAO = new PatientRegisterDAO();

    @FXML
    private void initialize() {
        // Set table column bindings
        colPatientRegisterIDAttendant.setCellValueFactory(cellData -> cellData.getValue().patientRegisterIDProperty().asObject());
        colPatientNameAttendant.setCellValueFactory(cellData -> {
            int patientRegisterId = cellData.getValue().getPatientRegisterID();
            return new SimpleStringProperty(getPatientNameByRegisterId(patientRegisterId));
        });
        colEmployeeID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty().asObject());
        colEmployeeNameAttendant.setCellValueFactory(cellData -> {
            int employeeId = cellData.getValue().getEmployeeID();
            return new SimpleStringProperty(getEmployeeNameById(employeeId));
        });
        colIsActiveAttendant.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        colCreatedOnAttendant.setCellValueFactory(cellData -> cellData.getValue().createdOnProperty());
        colModifiedOnAttendant.setCellValueFactory(cellData -> cellData.getValue().modifiedOnProperty());

        // Load data and initialize listeners
        loadPatientAttendants();
        refreshDropdowns();
        setupDynamicSearchPatientAttendant();
        setupDropdownRefreshListeners();
        initializeAttendantTableClickListener();
    }

    private String getPatientNameByRegisterId(int patientRegisterId) {
        String patientName = PatientRegisterDAO.getPatientNameById(patientRegisterId);
        return patientName != null ? patientName : "Unknown";
    }

    private String getEmployeeNameById(int employeeId) {
        try {
            return employeeDAO.getEmployeeNameById(employeeId);
        } catch (SQLException e) {
            showError("Error fetching employee name for ID " + employeeId + ": " + e.getMessage());
            return "Unknown";
        }
    }

    private void refreshDropdowns() {
        refreshPatientRegisterDropdown();
        refreshPatientAttendantDropdown();
    }

    private void refreshPatientRegisterDropdown() {
        try {
            List<PatientRegister> patientRegisters = patientRegisterDAO.getAllPatientRegisters();
            patientRegisterIDAttendantDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(PatientRegister register) {
                    return register != null ? register.getPatientRegisterId() + " - " + getPatientNameByRegisterId(register.getPatientRegisterId()) : "";
                }

                @Override
                public PatientRegister fromString(String string) {
                    return null;
                }
            });

            patientRegisterIDAttendantDropdown.setItems(FXCollections.observableArrayList(patientRegisters));
        } catch (SQLException e) {
            showError("Error refreshing patient register dropdown: " + e.getMessage());
        }
    }

    private void refreshPatientAttendantDropdown() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            patientAttendantDropdown.setConverter(new StringConverter<>() {
                @Override
                public String toString(Employee employee) {
                    return employee != null ? employee.getEmployeeName() + " (ID: " + employee.getEmployeeID() + ")" : "";
                }

                @Override
                public Employee fromString(String string) {
                    return null;
                }
            });

            patientAttendantDropdown.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            showError("Error refreshing employee dropdown: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAttendant(ActionEvent actionEvent) {
        try {
            PatientRegister selectedRegister = patientRegisterIDAttendantDropdown.getValue();
            Employee selectedEmployee = patientAttendantDropdown.getValue();

            if (selectedRegister == null || selectedEmployee == null) {
                showError("Please select both Patient Register ID and Employee.");
                return;
            }

            PatientAttendant newAttendant = new PatientAttendant();
            newAttendant.setPatientRegisterID(selectedRegister.getPatientRegisterId());
            newAttendant.setEmployeeID(selectedEmployee.getEmployeeID());
            newAttendant.setIsActive(isActiveAttendantCheckBox.isSelected());

            patientAttendantDAO.createPatientAttendant(newAttendant);
            loadPatientAttendants();
            refreshDropdowns();
            clearForm();
        } catch (SQLException e) {
            showError("Error adding attendant: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateAttendant(ActionEvent actionEvent) {
        PatientAttendant selectedAttendant = patientAttendantTable.getSelectionModel().getSelectedItem();
        if (selectedAttendant == null) {
            showError("Please select an attendant to update.");
            return;
        }

        PatientRegister selectedRegister = patientRegisterIDAttendantDropdown.getValue();
        Employee selectedEmployee = patientAttendantDropdown.getValue();

        if (selectedRegister == null || selectedEmployee == null) {
            showError("Please select both Patient Register ID and Employee.");
            return;
        }

        try {
            selectedAttendant.setPatientRegisterID(selectedRegister.getPatientRegisterId());
            selectedAttendant.setEmployeeID(selectedEmployee.getEmployeeID());
            selectedAttendant.setIsActive(isActiveAttendantCheckBox.isSelected());

            patientAttendantDAO.updatePatientAttendant(selectedAttendant);
            loadPatientAttendants();
            refreshDropdowns();
            clearForm();
        } catch (SQLException e) {
            showError("Error updating attendant: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteAttendant(ActionEvent actionEvent) {
        try {
            PatientAttendant selectedAttendant = patientAttendantTable.getSelectionModel().getSelectedItem();

            if (selectedAttendant == null) {
                showError("Please select an attendant to delete.");
                return;
            }

            patientAttendantDAO.deletePatientAttendant(selectedAttendant.getPatientRegisterID(), selectedAttendant.getEmployeeID());
            loadPatientAttendants();
            refreshDropdowns();
        } catch (SQLException e) {
            showError("Error deleting attendant: " + e.getMessage());
        }
    }

    private void loadPatientAttendants() {
        try {
            List<PatientAttendant> attendants = patientAttendantDAO.getAllPatientAttendants();
            patientAttendantTable.setItems(FXCollections.observableArrayList(attendants));
        } catch (SQLException e) {
            showError("Error loading patient attendants: " + e.getMessage());
        }
    }

    private void setupDynamicSearchPatientAttendant() {
        searchAttendantField.textProperty().addListener((observable, oldValue, newValue) -> filterPatientAttendants(newValue));
    }

    private void filterPatientAttendants(String searchQuery) {
        try {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                loadPatientAttendants();
                return;
            }

            String lowerCaseQuery = searchQuery.trim().toLowerCase();
            List<PatientAttendant> allPatientAttendants = patientAttendantDAO.getAllPatientAttendants();

            List<PatientAttendant> filteredPatientAttendants = allPatientAttendants.stream()
                    .filter(patientAttendant -> {
                        String patientName = getPatientNameByRegisterId(patientAttendant.getPatientRegisterID()).toLowerCase();
                        String employeeName = getEmployeeNameById(patientAttendant.getEmployeeID()).toLowerCase();
                        return patientName.contains(lowerCaseQuery) || employeeName.contains(lowerCaseQuery);
                    })
                    .collect(Collectors.toList());

            patientAttendantTable.setItems(FXCollections.observableArrayList(filteredPatientAttendants));
        } catch (SQLException e) {
            showError("Error filtering patient attendants: " + e.getMessage());
        }
    }

    private void setupDropdownRefreshListeners() {
        patientRegisterIDAttendantDropdown.setOnShowing(event -> refreshPatientRegisterDropdown());
        patientAttendantDropdown.setOnShowing(event -> refreshPatientAttendantDropdown());
    }

    private void initializeAttendantTableClickListener() {
        patientAttendantTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                populateAttendantForm();
            }
        });
    }

    private void populateAttendantForm() {
        PatientAttendant selectedAttendant = patientAttendantTable.getSelectionModel().getSelectedItem();

        if (selectedAttendant != null) {
            for (PatientRegister register : patientRegisterIDAttendantDropdown.getItems()) {
                if (register.getPatientRegisterId() == selectedAttendant.getPatientRegisterID()) {
                    patientRegisterIDAttendantDropdown.setValue(register);
                    break;
                }
            }

            for (Employee emp : patientAttendantDropdown.getItems()) {
                if (emp.getEmployeeID() == selectedAttendant.getEmployeeID()) {
                    patientAttendantDropdown.setValue(emp);
                    break;
                }
            }

            isActiveAttendantCheckBox.setSelected(selectedAttendant.getIsActive());
        }
    }

    private void clearForm() {
        patientRegisterIDAttendantDropdown.setValue(null);
        patientAttendantDropdown.setValue(null);
        isActiveAttendantCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
