package marc.View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientDiseaseHistory {

    private final StringProperty patientRegisterID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty diseaseName;
    private final StringProperty admissionDate;
    private final StringProperty dischargeDate;  // Assuming you want this field, even though it's null in the view

    public PatientDiseaseHistory(String patientRegisterID, String firstName, String lastName,
                                 String diseaseName, String admissionDate, String dischargeDate) {
        this.patientRegisterID = new SimpleStringProperty(patientRegisterID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.diseaseName = new SimpleStringProperty(diseaseName);
        this.admissionDate = new SimpleStringProperty(admissionDate);
        this.dischargeDate = new SimpleStringProperty(dischargeDate); // Can be null
    }

    public StringProperty patientRegisterIDProperty() {
        return patientRegisterID;
    }

    public String getPatientRegisterID() {
        return patientRegisterID.get();
    }

    public void setPatientRegisterID(String patientRegisterID) {
        this.patientRegisterID.set(patientRegisterID);
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

    public StringProperty diseaseNameProperty() {
        return diseaseName;
    }

    public String getDiseaseName() {
        return diseaseName.get();
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName.set(diseaseName);
    }

    public StringProperty admissionDateProperty() {
        return admissionDate;
    }

    public String getadmissionDate() {
        return admissionDate.get();
    }

    public void setadmissionDate(String diagnosisDate) {
        this.admissionDate.set(diagnosisDate);
    }

    public StringProperty dischargeDateProperty() {
        return dischargeDate;
    }

    public String getDischargeDate() {
        return dischargeDate.get();
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate.set(dischargeDate);
    }
}
