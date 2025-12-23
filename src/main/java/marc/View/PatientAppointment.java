package marc.View;

import javafx.beans.property.*;

public class PatientAppointment {
    private final IntegerProperty patientID;
    private final IntegerProperty employeeID;
    private final StringProperty appointmentDate;
    private final BooleanProperty isComplete;
    private final BooleanProperty isCancelled;
    private final BooleanProperty isNoShow;
    private final StringProperty createdOn;

    // Default Constructor
    public PatientAppointment() {
        this.patientID = new SimpleIntegerProperty(0);         // Default to 0
        this.employeeID = new SimpleIntegerProperty(0);        // Default to 0
        this.appointmentDate = new SimpleStringProperty("");   // Default to empty string
        this.isComplete = new SimpleBooleanProperty(false);    // Default to false
        this.isCancelled = new SimpleBooleanProperty(false);   // Default to false
        this.isNoShow = new SimpleBooleanProperty(false);      // Default to false
        this.createdOn = new SimpleStringProperty("");         // Default to empty string
    }

    // Constructor with parameters
    public PatientAppointment(int patientID, int employeeID, String appointmentDate, boolean isComplete, boolean isCancelled, boolean isNoShow, String createdOn) {
        this.patientID = new SimpleIntegerProperty(patientID);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.appointmentDate = new SimpleStringProperty(appointmentDate);
        this.isComplete = new SimpleBooleanProperty(isComplete);
        this.isCancelled = new SimpleBooleanProperty(isCancelled);
        this.isNoShow = new SimpleBooleanProperty(isNoShow);
        this.createdOn = new SimpleStringProperty(createdOn);
    }

    // Getters and Setters
    public IntegerProperty patientIDProperty() {
        return patientID;
    }

    public int getPatientID() {
        return patientID.get();
    }

    public void setPatientID(int patientID) {
        this.patientID.set(patientID);
    }

    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    public StringProperty appointmentDateProperty() {
        return appointmentDate;
    }

    public String getAppointmentDate() {
        return appointmentDate.get();
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate.set(appointmentDate);
    }

    public BooleanProperty isCompleteProperty() {
        return isComplete;
    }

    public boolean getIsComplete() {
        return isComplete.get();
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete.set(isComplete);
    }

    public BooleanProperty isCancelledProperty() {
        return isCancelled;
    }

    public boolean getIsCancelled() {
        return isCancelled.get();
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled.set(isCancelled);
    }

    public BooleanProperty isNoShowProperty() {
        return isNoShow;
    }

    public boolean getIsNoShow() {
        return isNoShow.get();
    }

    public void setIsNoShow(boolean isNoShow) {
        this.isNoShow.set(isNoShow);
    }

    public StringProperty createdOnProperty() {
        return createdOn;
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }
}
