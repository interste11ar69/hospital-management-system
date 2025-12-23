package marc.Class;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Employee {
    private final IntegerProperty employeeID = new SimpleIntegerProperty();
    private final StringProperty employeeNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final IntegerProperty roleID = new SimpleIntegerProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final IntegerProperty departmentID = new SimpleIntegerProperty();
    private final StringProperty department = new SimpleStringProperty();
    private final BooleanProperty isActive = new SimpleBooleanProperty();

    // Default constructor
    public Employee() {}

    // Parameterized constructor
    public Employee(int employeeID, String employeeNumber, String email, String firstName, String lastName,
                    LocalDate dateOfBirth, String gender, String phoneNumber, int roleID, String role,
                    int departmentID, String department) {
        this.employeeID.set(employeeID);
        this.employeeNumber.set(employeeNumber);
        this.email.set(email);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.dateOfBirth.set(dateOfBirth);
        this.gender.set(gender);
        this.phoneNumber.set(phoneNumber);
        this.roleID.set(roleID);
        this.role.set(role);
        this.departmentID.set(departmentID);
        this.department.set(department);
        this.isActive.set(true); // Default value for isActive
    }

    // Getters and Setters with JavaFX properties
    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public void setEmployeeID(int employeeID) {
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

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
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

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public IntegerProperty roleIDProperty() {
        return roleID;
    }

    public int getRoleID() {
        return roleID.get();
    }

    public void setRoleID(int roleID) {
        this.roleID.set(roleID);
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

    public IntegerProperty departmentIDProperty() {
        return departmentID;
    }

    public int getDepartmentID() {
        return departmentID.get();
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID.set(departmentID);
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

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public boolean isActive() {
        return isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
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

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    // New method to return the employee's full name for dropdown usage
    public String getEmployeeName() {
        return getFirstName() + " " + getLastName();  // Returns the full name
    }

    // Overriding toString method to provide a custom display in ComboBox
    @Override
    public String toString() {
        return firstName.get() + " " + lastName.get() + " (ID: " + employeeID.get() + ")";
    }
}
