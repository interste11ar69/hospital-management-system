package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import marc.Class.Role;
import marc.DAO.RoleDAO;

import java.sql.SQLException;
import java.util.List;

public class RoleManagementController {

    // FXML Elements
    @FXML
    private TableView<Role> roleTable;

    @FXML
    private TableColumn<Role, Integer> roleIdColumn;

    @FXML
    private TableColumn<Role, String> roleDescColumn;

    @FXML
    private TextField roleDescField;
    @FXML
    private TextField searchRoleField;


    @FXML
    private Button addRoleButton, updateRoleButton, deleteRoleButton;

    // DAO for database operations
    private final RoleDAO roleDAO = new RoleDAO();

    // Observable list to hold role data
    private ObservableList<Role> roleData;

    @FXML
    private void initialize() {
        setupTableColumns();
        loadRoles();
        setupDynamicRoleSearch();
    }

    // Set up table columns
    private void setupTableColumns() {
        roleIdColumn.setCellValueFactory(data -> data.getValue().roleIDProperty().asObject());
        roleDescColumn.setCellValueFactory(data -> data.getValue().roleDescProperty());
    }

    // Load roles from the database
    private void loadRoles() {
        try {
            List<Role> roles = roleDAO.getAllRoles();
            roleData = FXCollections.observableArrayList(roles);
            roleTable.setItems(roleData);
        } catch (SQLException e) {
            showError("Error loading roles: " + e.getMessage());
        }
    }

    // Handle adding a new role
    @FXML
    private void handleAddRole() {
        String roleDesc = roleDescField.getText().trim();
        if (roleDesc.isEmpty()) {
            showError("Role description cannot be empty.");
            return;
        }

        try {
            Role newRole = new Role();
            newRole.setRoleDesc(roleDesc);
            roleDAO.createRole(newRole);
            loadRoles(); // Reload the roles table
            roleDescField.clear();
            showInfo("Role added successfully.");
        } catch (SQLException e) {
            showError("Error adding role: " + e.getMessage());
        }
    }

    // Handle updating a selected role
    @FXML
    private void handleUpdateRole() {
        Role selectedRole = roleTable.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showError("Please select a role to update.");
            return;
        }

        String newDesc = roleDescField.getText().trim();
        if (newDesc.isEmpty()) {
            showError("Role description cannot be empty.");
            return;
        }

        try {
            selectedRole.setRoleDesc(newDesc);
            roleDAO.updateRole(selectedRole);
            loadRoles(); // Reload the roles table
            roleDescField.clear();
            showInfo("Role updated successfully.");
        } catch (SQLException e) {
            showError("Error updating role: " + e.getMessage());
        }
    }

    // Handle deleting a selected role
    @FXML
    private void handleDeleteRole() {
        Role selectedRole = roleTable.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            showError("Please select a role to delete.");
            return;
        }

        try {
            roleDAO.deleteRole(selectedRole.getRoleID());
            loadRoles(); // Reload the roles table
            showInfo("Role deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting role: " + e.getMessage());
        }
    }

    // Dynamic role search
    private void setupDynamicRoleSearch() {
        searchRoleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadRoles(); // Reset table to show all roles
            } else {
                filterRoles(newValue);
            }
        });
    }

    // Filter roles by search query
    private void filterRoles(String searchQuery) {
        try {
            // Filter roles by ID or description
            List<Role> filteredRoles = roleDAO.getAllRoles().stream()
                    .filter(role -> {
                        boolean matchesId = false;
                        try {
                            int id = Integer.parseInt(searchQuery); // Check if query matches ID
                            matchesId = role.getRoleID() == id;
                        } catch (NumberFormatException e) {
                            // Ignore if not a number
                        }
                        boolean matchesDesc = role.getRoleDesc().toLowerCase().contains(searchQuery.toLowerCase());
                        return matchesId || matchesDesc;
                    })
                    .toList();

            roleTable.setItems(FXCollections.observableArrayList(filteredRoles));
        } catch (SQLException e) {
            showError("Error filtering roles: " + e.getMessage());
        }
    }

    // Show an error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an informational message
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
