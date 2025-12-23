package marc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // <--- Changed from Pane to Parent
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.io.IOException;

public class GUI_FController {

    @FXML
    private VBox mainPane;

    @FXML
    private Button HomeButton, PatientRegisterButton, PatientsButton, PatientAttendantButton,
            LabReportButton, PatientAppointmentButton, LabTestButton, PatientDiseaseButton,
            EmployeeButton, RoleButton, DepartmentButton, DiseasesButton, TestsButton;

    // Cache stored as Parent to accept SplitPane, ScrollPane, etc.
    private Parent cachedHomePane;
    private Parent cachedPatientRegisterPane;
    private Parent cachedPatientsPane;
    private Parent cachedPatientAttendantPane;
    private Parent cachedLabReportPane;
    private Parent cachedPatientAppointmentPane;
    private Parent cachedLabTestPane;
    private Parent cachedPatientDiseasePane;
    private Parent cachedEmployeePane;
    private Parent cachedRolePane;
    private Parent cachedDepartmentPane;
    private Parent cachedDiseasesPane;
    private Parent cachedTestsPane;

    @FXML
    private void initialize() {
        initializeNavigationButtons();
        loadAndShowHomePane(); // Load the Home pane first
    }

    private void initializeNavigationButtons() {
        HomeButton.setOnAction(event -> loadAndShowHomePane());
        PatientRegisterButton.setOnAction(event -> loadAndShowPatientRegisterPane());
        PatientsButton.setOnAction(event -> loadAndShowPatientsPane());
        PatientAttendantButton.setOnAction(event -> loadAndShowPatientAttendantPane());
        LabReportButton.setOnAction(event -> loadAndShowLabReportPane());
        PatientAppointmentButton.setOnAction(event -> loadAndShowPatientAppointmentPane());
        LabTestButton.setOnAction(event -> loadAndShowLabTestPane());
        PatientDiseaseButton.setOnAction(event -> loadAndShowPatientDiseasePane());
        EmployeeButton.setOnAction(event -> loadAndShowEmployeePane());
        RoleButton.setOnAction(event -> loadAndShowRolePane());
        DepartmentButton.setOnAction(event -> loadAndShowDepartmentPane());
        DiseasesButton.setOnAction(event -> loadAndShowDiseasesPane());
        TestsButton.setOnAction(event -> loadAndShowTestsPane());
    }

    private void loadAndShowHomePane() {
        loadAndSwitchPane("Home.fxml", pane -> cachedHomePane = pane);
    }

    private void loadAndShowPatientRegisterPane() {
        loadAndSwitchPane("PatientRegister.fxml", pane -> cachedPatientRegisterPane = pane);
    }

    private void loadAndShowPatientsPane() {
        loadAndSwitchPane("PatientManagement.fxml", pane -> cachedPatientsPane = pane);
    }

    private void loadAndShowPatientAttendantPane() {
        loadAndSwitchPane("PatientAttendant.fxml", pane -> cachedPatientAttendantPane = pane);
    }

    private void loadAndShowLabReportPane() {
        loadAndSwitchPane("LabReport.fxml", pane -> cachedLabReportPane = pane);
    }

    private void loadAndShowPatientAppointmentPane() {
        loadAndSwitchPane("Appointment.fxml", pane -> cachedPatientAppointmentPane = pane);
    }

    private void loadAndShowLabTestPane() {
        loadAndSwitchPane("LabTest.fxml", pane -> cachedLabTestPane = pane);
    }

    private void loadAndShowPatientDiseasePane() {
        loadAndSwitchPane("PatientDisease.fxml", pane -> cachedPatientDiseasePane = pane);
    }

    private void loadAndShowEmployeePane() {
        loadAndSwitchPane("EmployeeManagement.fxml", pane -> cachedEmployeePane = pane);
    }

    private void loadAndShowRolePane() {
        loadAndSwitchPane("Role.fxml", pane -> cachedRolePane = pane);
    }

    private void loadAndShowDepartmentPane() {
        loadAndSwitchPane("Department.fxml", pane -> cachedDepartmentPane = pane);
    }

    private void loadAndShowDiseasesPane() {
        loadAndSwitchPane("Diseases.fxml", pane -> cachedDiseasesPane = pane);
    }

    private void loadAndShowTestsPane() {
        loadAndSwitchPane("Test.fxml", pane -> cachedTestsPane = pane);
    }

    // Changed Consumer<Pane> to Consumer<Parent>
    private void loadAndSwitchPane(String fxmlFile, java.util.function.Consumer<Parent> cacheSetter) {
        try {
            Parent pane = getCachedPane(fxmlFile);

            if (pane == null) {
                // Load FXML if not cached
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/marc/UI/" + fxmlFile));
                pane = loader.load();
                cacheSetter.accept(pane); // Cache the loaded pane
            }

            // Clear the current view and add the new one
            mainPane.getChildren().clear();
            mainPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + fxmlFile);
        }
    }

    // Changed return type from Pane to Parent
    private Parent getCachedPane(String fxmlFile) {
        // Return the cached Pane if it exists
        switch (fxmlFile) {
            case "Home.fxml":
                return cachedHomePane;
            case "PatientRegister.fxml":
                return cachedPatientRegisterPane;
            case "PatientManagement.fxml":
                return cachedPatientsPane;
            case "PatientAttendant.fxml":
                return cachedPatientAttendantPane;
            case "LabReport.fxml":
                return cachedLabReportPane;
            case "Appointment.fxml":
                return cachedPatientAppointmentPane;
            case "LabTest.fxml":
                return cachedLabTestPane;
            case "PatientDisease.fxml":
                return cachedPatientDiseasePane;
            case "EmployeeManagement.fxml": // Fixed Typo: removed extra .fxml
                return cachedEmployeePane;
            case "Role.fxml":
                return cachedRolePane;
            case "Department.fxml":
                return cachedDepartmentPane;
            case "Diseases.fxml":
                return cachedDiseasesPane;
            case "Test.fxml":
                return cachedTestsPane;
            default:
                return null;
        }
    }
}