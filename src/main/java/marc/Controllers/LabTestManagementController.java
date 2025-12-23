package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import marc.Class.LabTest;
import marc.Class.Test;
import marc.DAO.LabTestDAO;
import marc.DAO.TestDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class LabTestManagementController {

    @FXML
    private VBox labTestPanel;

    @FXML
    private TableView<LabTest> labTestTable;

    @FXML
    private TableColumn<LabTest, Integer> labTestIdColumn;

    @FXML
    private TableColumn<LabTest, String> labTestNameColumn;

    @FXML
    private TableColumn<LabTest, String> creationDateColumn;

    @FXML
    private TableColumn<LabTest, String> modifiedDateColumn;

    @FXML
    private ComboBox<Test> labTestDropdown;

    @FXML
    private TextField searchLabTestField;

    @FXML
    private Button addLabTestButton;

    @FXML
    private Button updateLabTestButton;

    @FXML
    private Button deleteLabTestButton;

    private final LabTestDAO labTestDAO = new LabTestDAO();
    private final TestDAO testDAO = new TestDAO();

    public LabTestManagementController() throws SQLException {
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        labTestIdColumn.setCellValueFactory(cellData -> cellData.getValue().labTestIDProperty().asObject());
        labTestNameColumn.setCellValueFactory(cellData -> cellData.getValue().testNameProperty());
        creationDateColumn.setCellValueFactory(cellData -> cellData.getValue().createdOnProperty());
        modifiedDateColumn.setCellValueFactory(cellData -> cellData.getValue().modifiedOnProperty());

        // Load lab tests and setup dropdown
        loadLabTests();
        populateLabTestDropdown();
        setupDynamicSearchLabTest();
    }

    // Load lab tests into the TableView
    private void loadLabTests() {
        try {
            List<LabTest> labTests = labTestDAO.getAllLabTests();
            labTestTable.setItems(FXCollections.observableArrayList(labTests));
        } catch (SQLException e) {
            showError("Error loading lab tests: " + e.getMessage());
        }
    }

    @FXML
    private void populateLabTestDropdown() {
        try {
            List<Test> labTests = testDAO.getAllTests();

            // Set the value converter to display the test name in the dropdown
            labTestDropdown.setConverter(new StringConverter<Test>() {
                @Override
                public String toString(Test test) {
                    return test != null ? test.getTestName() : "";
                }

                @Override
                public Test fromString(String string) {
                    return null; // No direct string-to-object conversion needed
                }
            });

            // Populate the dropdown
            labTestDropdown.setItems(FXCollections.observableArrayList(labTests));

        } catch (SQLException e) {
            showError("Error loading test data for dropdown: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmitLabTest() {
        try {
            // Get the selected Test from the dropdown
            Test selectedTestFromDropdown = labTestDropdown.getSelectionModel().getSelectedItem();

            if (selectedTestFromDropdown == null || selectedTestFromDropdown.getTestName().trim().isEmpty()) {
                showError("Please select a valid test from the dropdown.");
                return;
            }

            // Check if we're in "add" or "edit" mode
            LabTest selectedLabTest = labTestTable.getSelectionModel().getSelectedItem();

            if (selectedLabTest == null) {
                // Add mode
                LabTest newLabTest = new LabTest(
                        0,
                        selectedTestFromDropdown.getTestId(),
                        selectedTestFromDropdown.getTestName(),
                        "",
                        ""
                );

                labTestDAO.createLabTest(newLabTest);
                showInfo("Lab test added successfully.");
            } else {
                // Edit mode
                selectedLabTest.setTestId(selectedTestFromDropdown.getTestId());
                selectedLabTest.setTestName(selectedTestFromDropdown.getTestName());

                labTestDAO.updateLabTest(selectedLabTest);
                showInfo("Lab test updated successfully.");
            }

            loadLabTests();

        } catch (SQLException e) {
            showError("Error handling lab test: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteLabTest() {
        LabTest selectedLabTest = labTestTable.getSelectionModel().getSelectedItem();

        if (selectedLabTest == null) {
            showError("Please select a lab test to delete.");
            return;
        }

        try {
            labTestDAO.deleteLabTest(selectedLabTest.getLabTestID());
            loadLabTests();
            showInfo("Lab test deleted successfully.");
        } catch (SQLException e) {
            showError("Error deleting lab test: " + e.getMessage());
        }
    }

    @FXML
    private void editLabTest() {
        try {
            // Get the selected LabTest
            LabTest selectedLabTest = labTestTable.getSelectionModel().getSelectedItem();

            if (selectedLabTest == null) {
                showError("Please select a lab test to edit.");
                return;
            }

            // Get the selected Test from the dropdown
            Test selectedTest = labTestDropdown.getSelectionModel().getSelectedItem();

            if (selectedTest == null) {
                showError("Please select a test from the dropdown.");
                return;
            }

            // Update the selected LabTest
            selectedLabTest.setTestId(selectedTest.getTestId());
            selectedLabTest.setTestName(selectedTest.getTestName());

            labTestDAO.updateLabTest(selectedLabTest);
            refreshLabTestTable();
            showInfo("Lab test updated successfully.");
        } catch (SQLException e) {
            showError("Error updating lab test: " + e.getMessage());
        }
    }

    private void refreshLabTestTable() {
        try {
            List<LabTest> updatedLabTests = labTestDAO.getAllLabTests();
            labTestTable.setItems(FXCollections.observableArrayList(updatedLabTests));
        } catch (SQLException e) {
            showError("Failed to refresh the lab tests table.");
        }
    }

    private void setupDynamicSearchLabTest() {
        searchLabTestField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLabTests(newValue);
        });
    }

    private void filterLabTests(String searchQuery) {
        try {
            String lowerCaseQuery = searchQuery.trim().toLowerCase();

            List<LabTest> allLabTests = labTestDAO.getAllLabTests();

            List<LabTest> filteredLabTests = allLabTests.stream()
                    .filter(labTest -> {
                        boolean matchesId = false;
                        boolean matchesTestName = false;

                        try {
                            int id = Integer.parseInt(lowerCaseQuery);
                            matchesId = labTest.getLabTestID() == id;
                        } catch (NumberFormatException e) {
                        }

                        matchesTestName = labTest.getTestName() != null &&
                                labTest.getTestName().toLowerCase().contains(lowerCaseQuery);

                        return matchesId || matchesTestName;
                    })
                    .collect(Collectors.toList());

            labTestTable.setItems(FXCollections.observableArrayList(filteredLabTests));
        } catch (SQLException e) {
            showError("Error filtering lab tests: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
