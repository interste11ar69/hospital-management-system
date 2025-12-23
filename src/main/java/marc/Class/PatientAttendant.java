package marc.Class;

import javafx.beans.property.*;

public class PatientAttendant {
    private final IntegerProperty patientRegisterID;
    private final IntegerProperty employeeID;
    private final BooleanProperty isActive;
    private final StringProperty createdOn; // Automatically set in SQL
    private final StringProperty modifiedOn; // Automatically set in SQL

    // Default constructor
    public PatientAttendant() {
        this.patientRegisterID = new SimpleIntegerProperty(0);
        this.employeeID = new SimpleIntegerProperty(0);
        this.isActive = new SimpleBooleanProperty(true);
        this.createdOn = new SimpleStringProperty("");  // Automatically managed by SQL
        this.modifiedOn = new SimpleStringProperty("");  // Automatically managed by SQL
    }

    // Constructor without createdOn and modifiedOn
    public PatientAttendant(int patientRegisterID, int employeeID, boolean isActive) {
        this.patientRegisterID = new SimpleIntegerProperty(patientRegisterID);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdOn = new SimpleStringProperty("");  // Automatically managed by SQL
        this.modifiedOn = new SimpleStringProperty("");  // Automatically managed by SQL
    }

    // Constructor with all fields
    public PatientAttendant(int patientRegisterID, int employeeID, boolean isActive, String createdOn, String modifiedOn) {
        this.patientRegisterID = new SimpleIntegerProperty(patientRegisterID);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
    }

    public int getPatientRegisterID() {
        return patientRegisterID.get();
    }

    public void setPatientRegisterID(int patientRegisterID) {
        this.patientRegisterID.set(patientRegisterID);
    }

    public IntegerProperty patientRegisterIDProperty() {
        return patientRegisterID;
    }

    // Getter and Setter for EmployeeID
    public int getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    // Getter and Setter for IsActive
    public boolean getIsActive() {
        return isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    // Getter for CreatedOn
    public String getCreatedOn() {
        return createdOn.get();
    }

    public StringProperty createdOnProperty() {
        return createdOn;
    }

    // Getter for ModifiedOn
    public String getModifiedOn()
    {
        return modifiedOn.get();
    }

    public StringProperty modifiedOnProperty()
    {
        return modifiedOn;
    }
}
