package marc.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientLabResults {

    // Properties for Patient Lab Results
    private final StringProperty patientRegisterID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty testName;
    private final StringProperty result;
    private final StringProperty testDate;

    // Constructor
    public PatientLabResults(String patientRegisterID, String firstName, String lastName,
                             String testName, String result, String testDate) {
        this.patientRegisterID = new SimpleStringProperty(patientRegisterID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.testName = new SimpleStringProperty(testName);
        this.result = new SimpleStringProperty(result);
        this.testDate = new SimpleStringProperty(testDate);
    }

    // Getter and Setter for patientRegisterID
    public StringProperty patientRegisterIDProperty() {
        return patientRegisterID;
    }

    public String getPatientRegisterID() {
        return patientRegisterID.get();
    }

    public void setPatientRegisterID(String patientRegisterID) {
        this.patientRegisterID.set(patientRegisterID);
    }

    // Getter and Setter for firstName
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    // Getter and Setter for lastName
    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    // Getter and Setter for testName
    public StringProperty testNameProperty() {
        return testName;
    }

    public String getTestName() {
        return testName.get();
    }

    public void setTestName(String testName) {
        this.testName.set(testName);
    }

    // Getter and Setter for result
    public StringProperty resultProperty() {
        return result;
    }

    public String getResult() {
        return result.get();
    }

    public void setResult(String result) {
        this.result.set(result);
    }
    public StringProperty testDateProperty() {
        return testDate;
    }

    public String getTestDate() {
        return testDate.get();
    }

    public void setTestDate(String testDate) {
        this.testDate.set(testDate);
    }
}
