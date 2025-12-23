package marc.View;

import javafx.beans.property.*;

public class EmployeeInfo {

    private final StringProperty employeeID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty gender;
    private final StringProperty dateOfBirth;
    private final StringProperty phoneNumber;
    private final StringProperty role;
    private final StringProperty department;

    public EmployeeInfo(String employeeID, String firstName, String lastName, String gender,
                        String dateOfBirth, String phoneNumber, String role, String department) {
        this.employeeID = new SimpleStringProperty(employeeID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.gender = new SimpleStringProperty(gender);
        this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.role = new SimpleStringProperty(role);
        this.department = new SimpleStringProperty(department);
    }

    // Getters and setters (using Property methods for JavaFX bindings)
    public StringProperty employeeIDProperty() {
        return employeeID;
    }

    public String getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID.set(employeeID);
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

    public StringProperty genderProperty() {
        return gender;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty roleProperty() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }
}
