package marc.View;

import javafx.beans.property.*;

public class ActivePatient {

    private final StringProperty patientID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty dateOfBirth;
    private final StringProperty gender;
    private final StringProperty phoneNumber;
    private final StringProperty bloodGroup;

    // Constructor
    public ActivePatient(String patientID, String firstName, String lastName, String dateOfBirth, String gender, String phoneNumber, String bloodGroup) {
        this.patientID = new SimpleStringProperty(patientID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.gender = new SimpleStringProperty(gender);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
    }

    // Getters and Setters for StringProperty
    public StringProperty patientIDProperty() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID.set(patientID);
    }

    public String getPatientID() {
        return patientID.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup.set(bloodGroup);
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }
}
