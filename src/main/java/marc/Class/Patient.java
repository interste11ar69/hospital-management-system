package marc.Class;

import java.time.LocalDate;
import javafx.beans.property.*;

public class Patient {
    private final IntegerProperty patientID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> dateOfBirth;  // Changed to LocalDate
    private final StringProperty gender;
    private final StringProperty phoneNumber;
    private final StringProperty emailID;
    private final DoubleProperty height;
    private final DoubleProperty weight;
    private final StringProperty bloodGroup;
    private final BooleanProperty isActive;
    private final StringProperty createdOn;
    private final StringProperty modifiedOn;

    // Constructor with LocalDate for DateOfBirth
    public Patient(int patientID, String firstName, String lastName, LocalDate dateOfBirth,
                   String gender, String phoneNumber, String emailID, double height, double weight,
                   String bloodGroup, boolean isActive, String createdOn, String modifiedOn) {
        this.patientID = new SimpleIntegerProperty(patientID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);  // Using SimpleObjectProperty for LocalDate
        this.gender = new SimpleStringProperty(gender);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.emailID = new SimpleStringProperty(emailID);
        this.height = new SimpleDoubleProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
    }

    // Getters and Setters for properties
    public IntegerProperty patientIDProperty() {
        return patientID;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {  // Returns LocalDate wrapped in ObjectProperty
        return dateOfBirth;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty emailIDProperty() {
        return emailID;
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public StringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public StringProperty createdOnProperty() {
        return createdOn;
    }

    public StringProperty modifiedOnProperty() {
        return modifiedOn;
    }

    // Getters and Setters for raw values
    public int getPatientID() {
        return patientID.get();
    }

    public void setPatientID(int patientID) {
        this.patientID.set(patientID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getEmailID() {
        return emailID.get();
    }

    public void setEmailID(String emailID) {
        this.emailID.set(emailID);
    }

    public double getHeight() {
        return height.get();
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup.set(bloodGroup);
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    public String getModifiedOn() {
        return modifiedOn.get();
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn.set(modifiedOn);
    }
}
