package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import marc.Class.Appointment;
import marc.Class.Attendant;
import marc.View.PatientLabResults;
import marc.DAO.*;
import marc.View.ActivePatient;
import marc.View.EmployeeInfo;
import marc.View.PatientDiseaseHistory;

import java.sql.SQLException;
import java.util.List;

public class HomeController {

    @FXML
    private TableView<EmployeeInfo> employeeInfoTable;
    @FXML
    private TableColumn<EmployeeInfo, Integer> colEmployeeIDEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colFirstNameEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colLastNameEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colGenderEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colDateOfBirthEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colPhoneNumberEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colRoleEmployeeInfo;
    @FXML
    private TableColumn<EmployeeInfo, String> colDepartmentEmployeeInfo;

    @FXML
    private TableView<ActivePatient> activePatientTable;
    @FXML
    private TableColumn<ActivePatient, Integer> colPatientIDActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colFirstNameActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colLastNameActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colDateOfBirthActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colGenderActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colPhoneNumberActivePatient;
    @FXML
    private TableColumn<ActivePatient, String> colBloodGroupActivePatient;

    @FXML
    private TableView<Appointment> patientAppointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> colPatientIDPatientAppointment;
    @FXML
    private TableColumn<Appointment, String> colFirstNamePatientAppointment;
    @FXML
    private TableColumn<Appointment, String> colLastNamePatientAppointment;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDatePatientAppointment;
    @FXML
    private TableColumn<Appointment, String> colAppointmentStatusPatientAppointment;

    @FXML
    private TableView<Attendant> patientAttendantsTable;
    @FXML
    private TableColumn<Attendant, Integer> colPatientIDPatientAttendant;
    @FXML
    private TableColumn<Attendant, String> colFirstNamePatientAttendant;
    @FXML
    private TableColumn<Attendant, String> colLastNamePatientAttendant;
    @FXML
    private TableColumn<Attendant, Integer> colEmployeeIDPatientAttendant;
    @FXML
    private TableColumn<Attendant, String> colEmployeeNumberPatientAttendant;
    @FXML
    private TableColumn<Attendant, String> colAttendantFirstNamePatientAttendant;
    @FXML
    private TableColumn<Attendant, String> colAttendantLastNamePatientAttendant;

    @FXML
    private TableView<PatientDiseaseHistory> patientDiseaseHistoryTable;
    @FXML
    private TableColumn<PatientDiseaseHistory, Integer> colPatientRegisterIDPatientDiseaseHistory;
    @FXML
    private TableColumn<PatientDiseaseHistory, String> colFirstNamePatientDiseaseHistory;
    @FXML
    private TableColumn<PatientDiseaseHistory, String> colLastNamePatientDiseaseHistory;
    @FXML
    private TableColumn<PatientDiseaseHistory, String> colDiseaseNamePatientDiseaseHistory;
    @FXML
    private TableColumn<PatientDiseaseHistory, String> colAdmissionDatePatientDiseaseHistory;
    @FXML
    private TableColumn<PatientDiseaseHistory, String> colDischargeDatePatientDiseaseHistory;

    @FXML
    private TableView<PatientLabResults> patientLabResultsTable;
    @FXML
    private TableColumn<PatientLabResults, Integer> colPatientIDPatientLabResults;
    @FXML
    private TableColumn<PatientLabResults, String> colFirstNamePatientLabResults;
    @FXML
    private TableColumn<PatientLabResults, String> colLastNamePatientLabResults;
    @FXML
    private TableColumn<PatientLabResults, String> colTestNamePatientLabResults;
    @FXML
    private TableColumn<PatientLabResults, String> colResultPatientLabResults;
    @FXML
    private TableColumn<PatientLabResults, String> colTestDatePatientLabResults;

    private final EmployeeInfoDAO employeeInfoDAO = new EmployeeInfoDAO();
    private final ActivePatientDAO activePatientDAO = new ActivePatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final AttendantDAO attendantDAO = new AttendantDAO();
    private final PatientDiseaseHistoryDAO patientDiseaseHistoryDAO = new PatientDiseaseHistoryDAO();
    private final PatientLabResultsDAO patientLabResultsDAO = new PatientLabResultsDAO();

    @FXML
    private void initialize() {
        initializeEmployeeInfoColumns();
        initializeActivePatientColumns();
        initializeAppointmentColumns();
        initializeAttendantColumns();
        initializePatientDiseaseHistoryColumns();
        initializePatientLabResultsColumns();
        loadEmployeeInfo();
        loadActivePatients();
        loadAppointments();
        loadAttendants();
        loadPatientDiseaseHistory();
        loadLabResults();
    }

    private void initializeEmployeeInfoColumns() {
        colEmployeeIDEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        colFirstNameEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNameEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGenderEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDateOfBirthEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPhoneNumberEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRoleEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDepartmentEmployeeInfo.setCellValueFactory(new PropertyValueFactory<>("department"));
    }

    private void initializeActivePatientColumns() {
        colPatientIDActivePatient.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        colFirstNameActivePatient.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNameActivePatient.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDateOfBirthActivePatient.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colGenderActivePatient.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhoneNumberActivePatient.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colBloodGroupActivePatient.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
    }

    private void initializeAppointmentColumns() {
        colPatientIDPatientAppointment.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        colFirstNamePatientAppointment.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNamePatientAppointment.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAppointmentDatePatientAppointment.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        colAppointmentStatusPatientAppointment.setCellValueFactory(new PropertyValueFactory<>("appointmentStatus"));
    }

    private void initializeAttendantColumns() {
        colPatientIDPatientAttendant.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        colFirstNamePatientAttendant.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNamePatientAttendant.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmployeeIDPatientAttendant.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        colEmployeeNumberPatientAttendant.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        colAttendantFirstNamePatientAttendant.setCellValueFactory(new PropertyValueFactory<>("attendantFirstName"));
        colAttendantLastNamePatientAttendant.setCellValueFactory(new PropertyValueFactory<>("attendantLastName"));
    }

    private void initializePatientDiseaseHistoryColumns() {
        colPatientRegisterIDPatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("patientRegisterID"));
        colFirstNamePatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNamePatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDiseaseNamePatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("diseaseName"));
        colAdmissionDatePatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("admissionDate"));
        colDischargeDatePatientDiseaseHistory.setCellValueFactory(new PropertyValueFactory<>("dischargeDate"));
    }

    private void initializePatientLabResultsColumns() {
        colPatientIDPatientLabResults.setCellValueFactory(new PropertyValueFactory<>("patientRegisterID"));
        colFirstNamePatientLabResults.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastNamePatientLabResults.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colTestNamePatientLabResults.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colResultPatientLabResults.setCellValueFactory(new PropertyValueFactory<>("result"));
        colTestDatePatientLabResults.setCellValueFactory(new PropertyValueFactory<>("testDate"));
    }

    private void loadEmployeeInfo() {
        try {
            List<EmployeeInfo> employeeInfoList = employeeInfoDAO.getAllEmployeeInfo();
            employeeInfoTable.setItems(FXCollections.observableArrayList(employeeInfoList));
        } catch (SQLException e) {
            showError("Error loading employee information: " + e.getMessage());
        }
    }

    private void loadActivePatients() {
        try {
            List<ActivePatient> activePatients = activePatientDAO.getAllActivePatients();
            activePatientTable.setItems(FXCollections.observableArrayList(activePatients));
        } catch (SQLException e) {
            showError("Error loading active patients: " + e.getMessage());
        }
    }

    private void loadAppointments() {
        try {
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            patientAppointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (SQLException e) {
            showError("Error loading patient appointments: " + e.getMessage());
        }
    }

    private void loadAttendants() {
        try {
            List<Attendant> attendants = attendantDAO.getAllAttendants();
            patientAttendantsTable.setItems(FXCollections.observableArrayList(attendants));
        } catch (SQLException e) {
            showError("Error loading attendants: " + e.getMessage());
        }
    }

    private void loadPatientDiseaseHistory() {
        try {
            List<PatientDiseaseHistory> diseaseHistoryList = patientDiseaseHistoryDAO.getPatientDiseaseHistory();
            patientDiseaseHistoryTable.setItems(FXCollections.observableArrayList(diseaseHistoryList));
        } catch (SQLException e) {
            showError("Error loading disease history: " + e.getMessage());
        }
    }

    private void loadLabResults() {
        try {
            List<PatientLabResults> labResults = patientLabResultsDAO.getPatientLabResults();
            patientLabResultsTable.setItems(FXCollections.observableArrayList(labResults));
        } catch (SQLException e) {
            showError("Error loading lab results: " + e.getMessage());
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
