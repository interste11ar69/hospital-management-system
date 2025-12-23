package marc.Class;

import javafx.beans.property.*;

public class PatientLabReport {
    private final IntegerProperty patientLabReportID;
    private final IntegerProperty patientRegisterID;
    private final IntegerProperty labTestID;
    private final StringProperty labTestResult;
    private final StringProperty createdOn;
    private final StringProperty modifiedOn;

    // New fields for EmployeeID and EmployeeFullName
    private final IntegerProperty employeeID;
    private final StringProperty employeeFullName;

    // Default Constructor
    public PatientLabReport() {
        this.patientLabReportID = new SimpleIntegerProperty(0); // Default to 0
        this.patientRegisterID = new SimpleIntegerProperty(0);   // Default to 0
        this.labTestID = new SimpleIntegerProperty(0);           // Default to 0
        this.labTestResult = new SimpleStringProperty("");       // Default to empty string
        this.createdOn = new SimpleStringProperty("");           // Default to empty string
        this.modifiedOn = new SimpleStringProperty("");          // Default to empty string
        this.employeeID = new SimpleIntegerProperty(0);          // Default to 0
        this.employeeFullName = new SimpleStringProperty("");    // Default to empty string
    }

    // Constructor with parameters
    public PatientLabReport(int patientLabReportID, int patientRegisterID, int labTestID, String labTestResult,
                            String createdOn, String modifiedOn, int employeeID, String employeeFullName) {
        this.patientLabReportID = new SimpleIntegerProperty(patientLabReportID);
        this.patientRegisterID = new SimpleIntegerProperty(patientRegisterID);
        this.labTestID = new SimpleIntegerProperty(labTestID);
        this.labTestResult = new SimpleStringProperty(labTestResult);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.employeeFullName = new SimpleStringProperty(employeeFullName);
    }

    // Getters and Setters for PatientLabReportID
    public IntegerProperty patientLabReportIDProperty() {
        return patientLabReportID;
    }

    public int getPatientLabReportID() {
        return patientLabReportID.get();
    }

    public void setPatientLabReportID(int patientLabReportID) {
        this.patientLabReportID.set(patientLabReportID);
    }

    // Getters and Setters for PatientRegisterID
    public IntegerProperty patientRegisterIDProperty() {
        return patientRegisterID;
    }

    public int getPatientRegisterID() {
        return patientRegisterID.get();
    }

    public void setPatientRegisterID(int patientRegisterID) {
        this.patientRegisterID.set(patientRegisterID);
    }

    // Getters and Setters for LabTestID
    public IntegerProperty labTestIDProperty() {
        return labTestID;
    }

    public int getLabTestID() {
        return labTestID.get();
    }

    public void setLabTestID(int labTestID) {
        this.labTestID.set(labTestID);
    }

    // Getters and Setters for LabTestResult
    public StringProperty labTestResultProperty() {
        return labTestResult;
    }

    public String getLabTestResult() {
        return labTestResult.get();
    }

    public void setLabTestResult(String labTestResult) {
        this.labTestResult.set(labTestResult);
    }

    // Getters and Setters for CreatedOn
    public StringProperty createdOnProperty() {
        return createdOn;
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    // Getters and Setters for ModifiedOn
    public StringProperty modifiedOnProperty() {
        return modifiedOn;
    }

    public String getModifiedOn() {
        return modifiedOn.get();
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn.set(modifiedOn);
    }

    // Getters and Setters for EmployeeID
    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    // Getters and Setters for EmployeeFullName
    public StringProperty employeeFullNameProperty() {
        return employeeFullName;
    }

    public String getEmployeeFullName() {
        return employeeFullName.get();
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName.set(employeeFullName);
    }
}
