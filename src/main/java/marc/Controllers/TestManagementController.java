package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import marc.Class.Test;
import marc.DAO.TestDAO;

import java.sql.SQLException;
import java.util.List;

public class TestManagementController {

    @FXML
    private TableView<Test> testTable;

    @FXML
    private TableColumn<Test, Integer> testIdColumn;

    @FXML
    private TableColumn<Test, String> testNameColumn;

    @FXML
    private TableColumn<Test, String> testDescriptionColumn;

    @FXML
    private TextField testNameField;

    @FXML
    private TextField SearchTestField;


    @FXML
    private TextField testDescriptionField;

    private final TestDAO testDAO = new TestDAO(); // DAO for database operations

    @FXML
    private void initialize() {
        setupTable();
        loadTests();
        setupDynamicTestSearch();
        // Add listener for row selection
        testTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleRowClick(); // Call the method when a new row is selected
            }
        });
    }

    // Set up the table columns and behavior
    private void setupTable() {
        testIdColumn.setCellValueFactory(cellData -> cellData.getValue().testIdProperty().asObject());
        testNameColumn.setCellValueFactory(cellData -> cellData.getValue().testNameProperty());
        testDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }

    // Load all tests into the table
    private void loadTests() {
        try {
            List<Test> tests = testDAO.getAllTests();
            ObservableList<Test> testList = FXCollections.observableArrayList(tests);
            testTable.setItems(testList);
        } catch (SQLException e) {
            showError("Error loading tests: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddTest() {
        String testName = testNameField.getText();
        String testDescription = testDescriptionField.getText();

        if (testName.isEmpty()) {
            showError("Test name cannot be empty.");
            return;
        }

        try {
            Test newTest = new Test();
            newTest.setTestName(testName);
            newTest.setDescription(testDescription);
            testDAO.createTest(newTest); // Add the test to the database
            loadTests(); // Reload the table
            testNameField.clear();
            testDescriptionField.clear();
        } catch (SQLException e) {
            showError("Error adding test: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateTest() {
        Test selectedTest = testTable.getSelectionModel().getSelectedItem();
        if (selectedTest == null) {
            showError("Please select a test to update.");
            return;
        }

        String newTestName = testNameField.getText();
        String newTestDescription = testDescriptionField.getText();

        if (newTestName.isEmpty()) {
            showError("Test name cannot be empty.");
            return;
        }

        try {
            selectedTest.setTestName(newTestName);
            selectedTest.setDescription(newTestDescription);
            testDAO.updateTest(selectedTest); // Update the test in the database
            loadTests(); // Reload the table
            testNameField.clear();
            testDescriptionField.clear();
        } catch (SQLException e) {
            showError("Error updating test: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTest() {
        Test selectedTest = testTable.getSelectionModel().getSelectedItem();
        if (selectedTest == null) {
            showError("Please select a test to delete.");
            return;
        }

        try {
            testDAO.deleteTest(selectedTest.getTestId()); // Delete the test from the database
            loadTests(); // Reload the table
        } catch (SQLException e) {
            showError("Error deleting test: " + e.getMessage());
        }
    }

    @FXML
    private void setupDynamicTestSearch() {
        SearchTestField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTests(newValue); // Filter tests immediately as you type
        });
    }

    private void filterTests(String searchQuery) {
        try {
            // Get all tests from the already loaded list, without re-fetching them from the database
            List<Test> allTests = testDAO.getAllTests();

            // Filter the tests based on the search query
            List<Test> filteredTests = allTests.stream()
                    .filter(test -> {
                        String lowerCaseQuery = searchQuery.toLowerCase();
                        return test.getTestName().toLowerCase().contains(lowerCaseQuery) ||
                                (test.getDescription() != null && test.getDescription().toLowerCase().contains(lowerCaseQuery));
                    })
                    .toList();

            // Update the table with filtered results
            ObservableList<Test> filteredObservableList = FXCollections.observableArrayList(filteredTests);
            testTable.setItems(filteredObservableList);

        } catch (SQLException e) {
            showError("Error filtering tests: " + e.getMessage());
        }
    }

    // Utility method to show error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleRowClick() {
        Test selectedTest = testTable.getSelectionModel().getSelectedItem();
        if (selectedTest != null) {
            testNameField.setText(selectedTest.getTestName());
            testDescriptionField.setText(selectedTest.getDescription());
        }
    }

}
