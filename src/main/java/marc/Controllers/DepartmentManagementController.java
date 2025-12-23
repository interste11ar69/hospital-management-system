package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import marc.Class.Department;
import marc.DAO.DepartmentDAO;

import java.sql.SQLException;
import java.util.List;

public class DepartmentManagementController {

    @FXML
    private VBox departmentsPanel;

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, Integer> departmentIdColumn;

    @FXML
    private TableColumn<Department, String> departmentNameColumn;

    @FXML
    private TextField departmentNameField;
    @FXML
    private TextField searchDepartmentField;

    @FXML
    private Button addDepartmentButton;

    @FXML
    private Button updateDepartmentButton;

    @FXML
    private Button deleteDepartmentButton;

    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    @FXML
    private void initialize() {
        departmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().departmentIdProperty().asObject());
        departmentNameColumn.setCellValueFactory(cellData -> cellData.getValue().departmentNameProperty());
        loadDepartments();
        setupDynamicDepartmentSearch();
    }

    @FXML
    private void handleAddDepartment() {
        String departmentName = departmentNameField.getText();
        if (departmentName.isEmpty()) {
            showError("Department name cannot be empty.");
            return;
        }

        try {
            Department newDepartment = new Department();
            newDepartment.setDepartmentName(departmentName);
            departmentDAO.createDepartment(newDepartment);
            loadDepartments();
            departmentNameField.clear();
        } catch (SQLException e) {
            showError("Error adding department: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDepartment() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment == null) {
            showError("Please select a department to update.");
            return;
        }

        String newName = departmentNameField.getText();
        if (newName.isEmpty()) {
            showError("Department name cannot be empty.");
            return;
        }

        try {
            selectedDepartment.setDepartmentName(newName);
            departmentDAO.updateDepartment(selectedDepartment);
            loadDepartments();
            departmentNameField.clear();
        } catch (SQLException e) {
            showError("Error updating department: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment == null) {
            showError("Please select a department to delete.");
            return;
        }

        try {
            departmentDAO.deleteDepartment(selectedDepartment.getDepartmentId());
            loadDepartments();
        } catch (SQLException e) {
            showError("Error deleting department: " + e.getMessage());
        }
    }

    @FXML
    private void setupDynamicDepartmentSearch() {
        searchDepartmentField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadDepartments(); // Reset table to show all departments
            } else {
                filterDepartments(newValue); // Perform filtering based on user input
            }
        });
    }

    private void filterDepartments(String searchQuery) {
        try {
            // Get all departments and filter based on the search query
            List<Department> filteredDepartments = departmentDAO.getAllDepartments().stream()
                    .filter(department -> {
                        try {
                            // Check if the query matches the department ID
                            return department.getDepartmentId() == Integer.parseInt(searchQuery);
                        } catch (NumberFormatException e) {
                            // If not a number, check for department name match
                            return department.getDepartmentName().toLowerCase().contains(searchQuery.toLowerCase());
                        }
                    })
                    .toList();

            // Update the table with filtered results
            departmentTable.setItems(FXCollections.observableArrayList(filteredDepartments));
        } catch (SQLException e) {
            showError("Error filtering departments: " + e.getMessage());
        }
    }

    private void loadDepartments() {
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            departmentTable.setItems(FXCollections.observableArrayList(departments));
        } catch (SQLException e) {
            showError("Error loading departments: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
