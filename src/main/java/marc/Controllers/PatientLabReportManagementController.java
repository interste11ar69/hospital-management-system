package marc.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import marc.Class.Employee;
import marc.Class.PatientLabReport;
import marc.Class.PatientRegister;
import marc.DAO.EmployeeDAO;
import marc.DAO.LabTestDAO;
import marc.DAO.PatientDAO;
import marc.DAO.PatientRegisterDAO;
import marc.DAO.PatientLabReportDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientLabReportManagementController {

    @FXML
    private TableView<PatientLabReport> labReportTable;

    @FXML
    private TableColumn<PatientLabReport, Integer> colPatientLabReportID;

    @FXML
    private TableColumn<PatientLabReport, Integer> colLabPatientRegisterID;

    @FXML
    private TableColumn<PatientLabReport, String> colPatientName;

    @FXML
    private TableColumn<PatientLabReport, Integer> colLabTestID;

    @FXML
    private TableColumn<PatientLabReport, String> colLabTestName;

    @FXML
    private TableColumn<PatientLabReport, Integer> colEmployeeID;

    @FXML
    private TableColumn<PatientLabReport, String> colEmployeeName;

    @FXML
    private TableColumn<PatientLabReport, String> colResult;

    @FXML
    private TextField searchLabReportField;

    @FXML
    private TextField patientRegisterIDField;

    @FXML
    private TextField labTestIDField;

    @FXML
    private TextField resultField;

    @FXML
    private ComboBox<String> employeeDropdown;
    @FXML
    private ComboBox<String> patientRegisterDropdown;
    @FXML
    private ComboBox<String> labTestDropdown;

    @FXML
    private Button submitLabReportButton;

    private final PatientLabReportDAO patientLabReportDAO = new PatientLabReportDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final LabTestDAO labTestDAO = new LabTestDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final PatientRegisterDAO patientRegisterDAO = new PatientRegisterDAO();

    public PatientLabReportManagementController() throws SQLException {
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        colPatientLabReportID.setCellValueFactory(cellData -> cellData.getValue().patientLabReportIDProperty().asObject());
        colLabPatientRegisterID.setCellValueFactory(cellData -> cellData.getValue().patientRegisterIDProperty().asObject());
        colPatientName.setCellValueFactory(cellData -> {
            int patientRegisterId = cellData.getValue().getPatientRegisterID();
            return new SimpleStringProperty(getPatientNameByRegisterId(patientRegisterId));
        });
        colLabTestID.setCellValueFactory(cellData -> cellData.getValue().labTestIDProperty().asObject());
        colEmployeeID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty().asObject());
        colEmployeeName.setCellValueFactory(cellData -> {
            int employeeId = cellData.getValue().getEmployeeID();
            return new SimpleStringProperty(getEmployeeNameById(employeeId));
        });
        colLabTestName.setCellValueFactory(cellData -> {
            int labTestId = cellData.getValue().getLabTestID();
            return new SimpleStringProperty(getLabTestNameById(labTestId));
        });
        colResult.setCellValueFactory(cellData -> cellData.getValue().labTestResultProperty());

        // Load data into the table and dropdowns
        refreshData();
        setupDynamicSearchLabReport();
        initializeLabReportClickListener();
    }

    @FXML
    private void handleSubmitLabReport(ActionEvent actionEvent) {
        try {
            String selectedPatientRegister = patientRegisterDropdown.getSelectionModel().getSelectedItem();
            String selectedLabTest = labTestDropdown.getSelectionModel().getSelectedItem();
            String selectedEmployee = employeeDropdown.getSelectionModel().getSelectedItem();
            String result = resultField.getText().trim();

            if (selectedPatientRegister == null || selectedLabTest == null || selectedEmployee == null || result.isEmpty()) {
                showError("All fields must be filled.");
                return;
            }

            int patientRegisterID = Integer.parseInt(selectedPatientRegister.split(" - ")[0]);
            int labTestID = Integer.parseInt(selectedLabTest.split(" - ")[0]);
            int employeeID = Integer.parseInt(selectedEmployee.split(" - ")[0]);

            PatientLabReport newLabReport = new PatientLabReport();
            newLabReport.setPatientRegisterID(patientRegisterID);
            newLabReport.setLabTestID(labTestID);
            newLabReport.setLabTestResult(result);
            newLabReport.setEmployeeID(employeeID);

            patientLabReportDAO.createPatientLabReport(newLabReport);

            refreshData();
            clearLabReportForm();
        } catch (SQLException e) {
            showError("Error submitting lab report: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateLabReport(ActionEvent actionEvent) {
        try {
            PatientLabReport selectedLabReport = labReportTable.getSelectionModel().getSelectedItem();
            if (selectedLabReport == null) {
                showError("Please select a lab report to update.");
                return;
            }

            String selectedPatientRegister = patientRegisterDropdown.getSelectionModel().getSelectedItem();
            String selectedLabTest = labTestDropdown.getSelectionModel().getSelectedItem();
            String selectedEmployee = employeeDropdown.getSelectionModel().getSelectedItem();
            String result = resultField.getText().trim();

            if (selectedPatientRegister == null || selectedLabTest == null || selectedEmployee == null || result.isEmpty()) {
                showError("All fields must be filled.");
                return;
            }

            int patientRegisterID = Integer.parseInt(selectedPatientRegister.split(" - ")[0]);
            int labTestID = Integer.parseInt(selectedLabTest.split(" - ")[0]);
            int employeeID = Integer.parseInt(selectedEmployee.split(" - ")[0]);

            selectedLabReport.setPatientRegisterID(patientRegisterID);
            selectedLabReport.setLabTestID(labTestID);
            selectedLabReport.setLabTestResult(result);
            selectedLabReport.setEmployeeID(employeeID);

            patientLabReportDAO.updatePatientLabReport(selectedLabReport);

            refreshData();
            clearLabReportForm();
        } catch (SQLException e) {
            showError("Error updating lab report: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteLabReport(ActionEvent actionEvent) {
        try {
            PatientLabReport selectedLabReport = labReportTable.getSelectionModel().getSelectedItem();
            if (selectedLabReport == null) {
                showError("Please select a lab report to delete.");
                return;
            }

            patientLabReportDAO.deletePatientLabReport(selectedLabReport.getPatientLabReportID());

            refreshData();
        } catch (SQLException e) {
            showError("Error deleting lab report: " + e.getMessage());
        }
    }

    private void setupDynamicSearchLabReport() {
        searchLabReportField.textProperty().addListener((observable, oldValue, newValue) -> filterLabReports(newValue));
    }

    private void filterLabReports(String searchQuery) {
        try {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                loadPatientLabReports();
                return;
            }

            String lowerCaseQuery = searchQuery.trim().toLowerCase();
            List<PatientLabReport> allLabReports = patientLabReportDAO.getAllPatientLabReports();

            List<PatientLabReport> filteredLabReports = allLabReports.stream()
                    .filter(labReport -> {
                        boolean matchesLabReportId = Integer.toString(labReport.getPatientLabReportID()).contains(lowerCaseQuery);
                        boolean matchesPatientRegisterId = Integer.toString(labReport.getPatientRegisterID()).contains(lowerCaseQuery);
                        boolean matchesLabTestId = Integer.toString(labReport.getLabTestID()).contains(lowerCaseQuery);
                        boolean matchesEmployeeName = getEmployeeNameById(labReport.getEmployeeID()).toLowerCase().contains(lowerCaseQuery);
                        boolean matchesPatientName = getPatientNameByRegisterId(labReport.getPatientRegisterID()).toLowerCase().contains(lowerCaseQuery);
                        boolean matchesLabTestName = getLabTestNameById(labReport.getLabTestID()).toLowerCase().contains(lowerCaseQuery);
                        return matchesLabReportId || matchesPatientRegisterId || matchesLabTestId ||
                                matchesEmployeeName || matchesPatientName || matchesLabTestName;
                    })
                    .collect(Collectors.toList());

            labReportTable.setItems(FXCollections.observableArrayList(filteredLabReports));
        } catch (SQLException e) {
            showError("Error filtering lab reports: " + e.getMessage());
        }
    }

    private void initializeLabReportClickListener() {
        labReportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                populateLabReportForm();
            }
        });
    }

    private void populateLabReportForm() {
        PatientLabReport selectedLabReport = labReportTable.getSelectionModel().getSelectedItem();
        if (selectedLabReport != null) {
            try {
                patientRegisterDropdown.setValue(selectedLabReport.getPatientRegisterID() + " - " + getPatientNameByRegisterId(selectedLabReport.getPatientRegisterID()));
                labTestDropdown.setValue(selectedLabReport.getLabTestID() + " - " + getLabTestById(selectedLabReport.getLabTestID()));
                employeeDropdown.setValue(selectedLabReport.getEmployeeID() + " - " + getEmployeeNameById(selectedLabReport.getEmployeeID()));
                resultField.setText(selectedLabReport.getLabTestResult());
            } catch (SQLException e) {
                showError("Error populating form: " + e.getMessage());
            }
        }
    }

    private void refreshData() {
        loadPatientLabReports();
        populateEmployeeDropdown();
        populateLabTestDropdown();
        populatePatientRegisterDropdown();
    }

    private void loadPatientLabReports() {
        try {
            List<PatientLabReport> labReports = patientLabReportDAO.getAllPatientLabReports();
            labReportTable.setItems(FXCollections.observableArrayList(labReports));
        } catch (SQLException e) {
            showError("Error loading lab reports: " + e.getMessage());
        }
    }

    private void populateLabTestDropdown() {
        try {
            List<String> labTestDropdownItems = labTestDAO.getLabTestDropdownData(); // Use the DAO method
            labTestDropdown.setItems(FXCollections.observableArrayList(labTestDropdownItems));
        } catch (SQLException e) {
            showError("Error loading lab test data: " + e.getMessage());
        }
    }

    private void populatePatientRegisterDropdown() {
        try {
            List<PatientRegister> patientRegisters = patientRegisterDAO.getAllPatientRegisters();
            List<String> dropdownItems = new ArrayList<>();
            for (PatientRegister register : patientRegisters) {
                String patientName = getPatientNameByRegisterId(register.getPatientRegisterId());
                dropdownItems.add(register.getPatientRegisterId() + " - " + patientName);
            }
            patientRegisterDropdown.setItems(FXCollections.observableArrayList(dropdownItems));
        } catch (SQLException e) {
            showError("Error loading patient register data: " + e.getMessage());
        }
    }

    private void populateEmployeeDropdown() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            List<String> dropdownItems = employees.stream()
                    .map(emp -> emp.getEmployeeID() + " - " + emp.getEmployeeName())
                    .collect(Collectors.toList());
            employeeDropdown.setItems(FXCollections.observableArrayList(dropdownItems));
        } catch (SQLException e) {
            showError("Error loading employees: " + e.getMessage());
        }
    }

    private void clearLabReportForm() {
        patientRegisterDropdown.getSelectionModel().clearSelection();
        labTestDropdown.getSelectionModel().clearSelection();
        employeeDropdown.getSelectionModel().clearSelection();
        resultField.clear();
    }

    private String getPatientNameByRegisterId(int patientRegisterId) {
        return PatientRegisterDAO.getPatientNameById(patientRegisterId);
    }
    public String getLabTestById(int labTestId) throws SQLException {
        return LabTestDAO.getLabTestNameById(labTestId);
    }


    private String getEmployeeNameById(int employeeId) {
        try {
            return employeeDAO.getEmployeeNameById(employeeId);
        } catch (SQLException e) {
            showError("Error fetching employee name: " + e.getMessage());
            return "Unknown";
        }
    }
    private String getLabTestNameById(int labTestId) {
        try {
            return LabTestDAO.getLabTestNameById(labTestId);
        } catch (SQLException e) {
            showError("Error fetching lab test name: " + e.getMessage());
            return "Unknown";
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
