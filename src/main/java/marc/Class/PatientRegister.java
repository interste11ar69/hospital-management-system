package marc.Class;

import javafx.beans.property.*;
import java.time.LocalDate;

public class PatientRegister {
    private final IntegerProperty patientRegisterId;
    private final IntegerProperty patientId;
    private final ObjectProperty<LocalDate> admittedOn;
    private final ObjectProperty<LocalDate> dischargeOn;
    private final StringProperty roomNumber;
    private final BooleanProperty isActive;
    private final StringProperty createdOn;
    private final StringProperty modifiedOn;

    // Constructor
    public PatientRegister(int patientRegisterId, int patientId, LocalDate admittedOn, LocalDate dischargeOn,
                           String roomNumber, boolean isActive, String createdOn, String modifiedOn) {
        this.patientRegisterId = new SimpleIntegerProperty(patientRegisterId);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.admittedOn = new SimpleObjectProperty<>(admittedOn);
        this.dischargeOn = new SimpleObjectProperty<>(dischargeOn);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.isActive = new SimpleBooleanProperty(isActive);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
    }

    // Property Getters
    public IntegerProperty patientRegisterIdProperty() {
        return patientRegisterId;
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public ObjectProperty<LocalDate> admittedOnProperty() {
        return admittedOn;
    }

    public ObjectProperty<LocalDate> dischargeOnProperty() {
        return dischargeOn;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
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

    // Getters for raw values
    public int getPatientRegisterId() {
        return patientRegisterId.get();
    }

    public int getPatientId() {
        return patientId.get();
    }

    public LocalDate getAdmittedOn() {
        return admittedOn.get();
    }

    public LocalDate getDischargeOn() {
        return dischargeOn.get();
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public String getModifiedOn() {
        return modifiedOn.get();
    }

    // Setters for raw values
    public void setPatientRegisterId(int patientRegisterId) {
        this.patientRegisterId.set(patientRegisterId);
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    public void setAdmittedOn(LocalDate admittedOn) {
        this.admittedOn.set(admittedOn);
    }

    public void setDischargeOn(LocalDate dischargeOn) {
        this.dischargeOn.set(dischargeOn);
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn.set(modifiedOn);
    }


}
