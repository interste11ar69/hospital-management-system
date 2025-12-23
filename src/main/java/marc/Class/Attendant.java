package marc.Class;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Attendant {

    private final StringProperty patientID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty employeeID;
    private final StringProperty employeeNumber;
    private final StringProperty attendantFirstName;
    private final StringProperty attendantLastName;

    // Constructor
    public Attendant(String patientID, String firstName, String lastName,
                     String employeeID, String employeeNumber,
                     String attendantFirstName, String attendantLastName) {
        this.patientID = new SimpleStringProperty(patientID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.employeeID = new SimpleStringProperty(employeeID);
        this.employeeNumber = new SimpleStringProperty(employeeNumber);
        this.attendantFirstName = new SimpleStringProperty(attendantFirstName);
        this.attendantLastName = new SimpleStringProperty(attendantLastName);
    }

    // Getters and Setters for StringProperty fields

    public StringProperty patientIDProperty() {
        return patientID;
    }

    public String getPatientID() {
        return patientID.get();
    }

    public void setPatientID(String patientID) {
        this.patientID.set(patientID);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty employeeIDProperty() {
        return employeeID;
    }

    public String getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID.set(employeeID);
    }

    public StringProperty employeeNumberProperty() {
        return employeeNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber.get();
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber.set(employeeNumber);
    }

    public StringProperty attendantFirstNameProperty() {
        return attendantFirstName;
    }

    public String getAttendantFirstName() {
        return attendantFirstName.get();
    }

    public void setAttendantFirstName(String attendantFirstName) {
        this.attendantFirstName.set(attendantFirstName);
    }

    public StringProperty attendantLastNameProperty() {
        return attendantLastName;
    }

    public String getAttendantLastName() {
        return attendantLastName.get();
    }

    public void setAttendantLastName(String attendantLastName) {
        this.attendantLastName.set(attendantLastName);
    }
}
