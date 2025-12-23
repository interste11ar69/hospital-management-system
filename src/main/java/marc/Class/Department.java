package marc.Class;

import javafx.beans.property.*;

public class Department {
    private final IntegerProperty departmentId = new SimpleIntegerProperty(); // Integer property for department ID
    private final StringProperty departmentName = new SimpleStringProperty(); // String property for department name

    // Default constructor
    public Department() {
    }

    // Parameterized constructor for departmentId and departmentName
    public Department(int departmentId, String departmentName) {
        this.departmentId.set(departmentId);
        this.departmentName.set(departmentName);
    }

    // Getters and setters with JavaFX properties
    public int getDepartmentId() {
        return departmentId.get();
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId.set(departmentId);
    }

    public IntegerProperty departmentIdProperty() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName.get();
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName.set(departmentName);
    }

    public StringProperty departmentNameProperty() {
        return departmentName;
    }

    // Override toString() for proper ComboBox display
    @Override
    public String toString() {
        return departmentName.get(); // Return the department name for display
    }
}
