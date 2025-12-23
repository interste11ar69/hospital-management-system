package marc.Class;

import javafx.beans.property.*;

public class Role {
    private final IntegerProperty roleID = new SimpleIntegerProperty();
    private final StringProperty roleDesc = new SimpleStringProperty();

    // Default constructor
    public Role() {
    }

    // Parameterized constructor for roleID and roleDesc
    public Role(int roleID, String roleDesc) {
        this.roleID.set(roleID);
        this.roleDesc.set(roleDesc);
    }

    // Getters and setters with JavaFX properties
    public int getRoleID() {
        return roleID.get();
    }

    public void setRoleID(int roleID) {
        this.roleID.set(roleID);
    }

    public IntegerProperty roleIDProperty() {
        return roleID;
    }

    public String getRoleDesc() {
        return roleDesc.get();
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc.set(roleDesc);
    }

    public StringProperty roleDescProperty() {
        return roleDesc;
    }

    // Override toString() for proper ComboBox display
    @Override
    public String toString() {
        return roleDesc.get(); // Return the role description for display
    }
}
