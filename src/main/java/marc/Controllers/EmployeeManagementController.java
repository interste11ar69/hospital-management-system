package marc.Controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import marc.Class.Employee;
import marc.Class.Role;
import marc.DAO.EmployeeDAO;
import marc.Class.Department;
import marc.DAO.RoleDAO;
import marc.DAO.DepartmentDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeManagementController {

    @FXML
    private VBox employeePanel;

    @FXML
    private VBox registerEmployeePanel;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> colEmployeeNumber;

    @FXML
    private TableColumn<Employee, String> colFirstName;

    @FXML
    private TableColumn<Employee, String> colLastName;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, String> colRole;

    @FXML
    private TableColumn<Employee, String> colDepartment;

    @FXML
    private TableColumn<Employee, String> colGender;

    @FXML
    private TableColumn<Employee, LocalDate> colDateOfBirth;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private ComboBox<String> genderDropdown;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private ComboBox<Role> roleDropdown;

    @FXML
    private ComboBox<Department> departmentDropdown;

    @FXML
    private Button submitButton;

    @FXML
    private Button btnRegisterEmployee;

    @FXML
    private Button btnUpdateEmployee;

    @FXML
    private Button btnDeleteEmployee;

    @FXML
    private TextField searchEmployeeField;

    private Employee selectedEmployee;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    private final List<Employee> employeesData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colEmployeeNumber.setCellValueFactory(cellData -> cellData.getValue().employeeNumberProperty());
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colRole.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        colDepartment.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        colGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        colDateOfBirth.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());

        loadEmployees();
        setupDynamicSearch();
        populateRoleDropdown();
        populateDepartmentDropdown();
    }

    @FXML
    private void handleSubmit() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String gender = genderDropdown.getValue();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        Role selectedRole = roleDropdown.getValue();
        Department selectedDepartment = departmentDropdown.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()
                || gender == null || dateOfBirth == null || selectedRole == null || selectedDepartment == null) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            if ("Update Employee".equalsIgnoreCase(submitButton.getText())) {
                if (selectedEmployee == null) {
                    showError("No employee selected for update.");
                    return;
                }

                selectedEmployee.setFirstName(firstName);
                selectedEmployee.setLastName(lastName);
                selectedEmployee.setEmail(email);
                selectedEmployee.setPhoneNumber(phoneNumber);
                selectedEmployee.setGender(gender);
                selectedEmployee.setDateOfBirth(dateOfBirth);
                selectedEmployee.setRole(selectedRole.getRoleDesc());
                selectedEmployee.setDepartment(selectedDepartment.getDepartmentName());

                employeeDAO.updateEmployee(selectedEmployee);

                loadEmployees();
                clearForm();
                showEmployeePanel();
                showInfo("Employee updated successfully.");

            } else {
                Employee newEmployee = new Employee(
                        0,
                        generateRandomEmployeeNumber(),
                        email,
                        firstName,
                        lastName,
                        dateOfBirth,
                        gender,
                        phoneNumber,
                        selectedRole.getRoleID(),
                        selectedRole.getRoleDesc(),
                        selectedDepartment.getDepartmentId(),
                        selectedDepartment.getDepartmentName()
                );

                employeeDAO.createEmployee(newEmployee);

                loadEmployees();
                clearForm();
                showEmployeePanel();
                showInfo("Employee registered successfully.");
            }
        } catch (SQLException e) {
            showError("Error saving employee: " + e.getMessage());
        }
    }

    @FXML
    private void editEmployee() {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            showError("Please select an employee to edit.");
            return;
        }

        firstNameField.setText(selectedEmployee.getFirstName());
        lastNameField.setText(selectedEmployee.getLastName());
        emailField.setText(selectedEmployee.getEmail());
        phoneNumberField.setText(selectedEmployee.getPhoneNumber());
        genderDropdown.setValue(selectedEmployee.getGender());
        dateOfBirthPicker.setValue(selectedEmployee.getDateOfBirth());
        roleDropdown.setValue(new Role(selectedEmployee.getRoleID(), selectedEmployee.getRole()));
        departmentDropdown.setValue(new Department(selectedEmployee.getDepartmentID(), selectedEmployee.getDepartment()));

        submitButton.setText("Update Employee");

        showPanel(registerEmployeePanel);
    }

    @FXML
    private void handleDeleteEmployee() {
        selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            showError("Please select an employee to delete.");
            return;
        }

        try {
            employeeDAO.deleteEmployee(selectedEmployee.getEmployeeID());

            loadEmployees();
            selectedEmployee = null;

            showInfo("Employee deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting employee: " + e.getMessage());
        }
    }

    private void setupDynamicSearch() {
        PauseTransition pause = new PauseTransition(Duration.millis(300));

        searchEmployeeField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> {
                if (newValue.isEmpty()) {
                    employeeTable.setItems(FXCollections.observableArrayList(employeesData));
                } else {
                    filterEmployees(newValue);
                }
            });
            pause.playFromStart();
        });
    }

    private void filterEmployees(String searchTerm) {
        List<Employee> filteredEmployees = employeesData.stream()
                .filter(emp -> emp.getFirstName().toLowerCase().contains(searchTerm.toLowerCase())
                        || emp.getLastName().toLowerCase().contains(searchTerm.toLowerCase())
                        || emp.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                        || (emp.getDateOfBirth() != null && emp.getDateOfBirth().toString().contains(searchTerm)))
                .toList();

        employeeTable.setItems(FXCollections.observableArrayList(filteredEmployees));
    }

    private String generateRandomEmployeeNumber() {
        return "EMP" + (int) (Math.random() * 1_000_000);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        genderDropdown.setValue(null);
        dateOfBirthPicker.setValue(null);
        roleDropdown.setValue(null);
        departmentDropdown.setValue(null);

        submitButton.setText("Submit");
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            employeesData.clear();
            employeesData.addAll(employees);
            employeeTable.setItems(FXCollections.observableArrayList(employees));
        } catch (SQLException e) {
            showError("Error loading employees: " + e.getMessage());
        }
    }

    private void populateRoleDropdown() {
        try {
            List<Role> roles = roleDAO.getAllRoles();
            roleDropdown.setItems(FXCollections.observableArrayList(roles));
        } catch (SQLException e) {
            showError("Error loading roles: " + e.getMessage());
        }
    }

    private void populateDepartmentDropdown() {
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            departmentDropdown.setItems(FXCollections.observableArrayList(departments));
        } catch (SQLException e) {
            showError("Error loading departments: " + e.getMessage());
        }
    }

    @FXML
    private void showEmployeePanel() {
        registerEmployeePanel.setVisible(false);
        employeePanel.setVisible(true);
    }

    private void showPanel(VBox panel) {
        employeePanel.setVisible(false);
        registerEmployeePanel.setVisible(false);
        panel.setVisible(true);
    }

    @FXML
    public void showRegisterEmployeePanel(ActionEvent actionEvent) {
        clearForm();
        registerEmployeePanel.setVisible(true);
        employeePanel.setVisible(false);
        submitButton.setText("Submit");
    }
}
