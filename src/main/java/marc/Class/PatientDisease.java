package marc.Class;

import javafx.beans.property.*;

import java.util.Objects;

public class PatientDisease {
    private final IntegerProperty patientRegisterID;
    private final IntegerProperty diseaseID;
    private final BooleanProperty isActive;
    private final StringProperty createdOn;
    private final StringProperty modifiedOn;

    // Default Constructor
    public PatientDisease() {
        this.patientRegisterID = new SimpleIntegerProperty(0);
        this.diseaseID = new SimpleIntegerProperty(0);
        this.isActive = new SimpleBooleanProperty(true);
        this.createdOn = new SimpleStringProperty("");
        this.modifiedOn = new SimpleStringProperty("");
    }

    // Constructor with parameters
    public PatientDisease(int patientRegisterID, int diseaseID, boolean isActive, String createdOn, String modifiedOn) {
        this.patientRegisterID = new SimpleIntegerProperty(patientRegisterID);
        this.diseaseID = new SimpleIntegerProperty(diseaseID);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
    }

    // Copy Constructor
    public PatientDisease(PatientDisease other) {
        this.patientRegisterID = new SimpleIntegerProperty(other.getPatientRegisterID());
        this.diseaseID = new SimpleIntegerProperty(other.getDiseaseID());
        this.isActive = new SimpleBooleanProperty(other.getIsActive());
        this.createdOn = new SimpleStringProperty(other.getCreatedOn());
        this.modifiedOn = new SimpleStringProperty(other.getModifiedOn());
    }

    // Getters and Setters
    public int getPatientRegisterID() {
        return patientRegisterID.get();
    }

    public void setPatientRegisterID(int patientRegisterID) {
        this.patientRegisterID.set(patientRegisterID);
    }

    public IntegerProperty patientRegisterIDProperty() {
        return patientRegisterID;
    }

    public int getDiseaseID() {
        return diseaseID.get();
    }

    public void setDiseaseID(int diseaseID) {
        this.diseaseID.set(diseaseID);
    }

    public IntegerProperty diseaseIDProperty() {
        return diseaseID;
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    public StringProperty createdOnProperty() {
        return createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn.get();
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn.set(modifiedOn);
    }

    public StringProperty modifiedOnProperty() {
        return modifiedOn;
    }

    // toString Method
    @Override
    public String toString() {
        return "PatientDisease{" +
                "patientRegisterID=" + getPatientRegisterID() +
                ", diseaseID=" + getDiseaseID() +
                ", isActive=" + getIsActive() +
                ", createdOn='" + getCreatedOn() + '\'' +
                ", modifiedOn='" + getModifiedOn() + '\'' +
                '}';
    }

    // equals Method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientDisease that = (PatientDisease) o;
        return getPatientRegisterID() == that.getPatientRegisterID() &&
                getDiseaseID() == that.getDiseaseID() &&
                getIsActive() == that.getIsActive() &&
                Objects.equals(getCreatedOn(), that.getCreatedOn()) &&
                Objects.equals(getModifiedOn(), that.getModifiedOn());
    }
}
