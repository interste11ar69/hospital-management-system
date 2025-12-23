package marc.Class;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {

    private final StringProperty patientID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty appointmentDate;
    private final StringProperty appointmentStatus;

    public Appointment(String patientID, String firstName, String lastName, String appointmentDate, String appointmentStatus) {
        this.patientID = new SimpleStringProperty(patientID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.appointmentDate = new SimpleStringProperty(appointmentDate);
        this.appointmentStatus = new SimpleStringProperty(appointmentStatus);
    }

    public String getPatientID() {
        return patientID.get();
    }

    public StringProperty patientIDProperty() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID.set(patientID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getAppointmentDate() {
        return appointmentDate.get();
    }

    public StringProperty appointmentDateProperty() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate.set(appointmentDate);
    }

    public String getAppointmentStatus() {
        return appointmentStatus.get();
    }

    public StringProperty appointmentStatusProperty() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus.set(appointmentStatus);
    }
}
