package marc.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import marc.Class.PatientRegister;
import marc.DAO.DiseaseDAO;
import marc.Class.Disease;
import marc.DAO.PatientRegisterDAO;

import java.sql.SQLException;
import java.util.List;

import static marc.DAO.PatientRegisterDAO.getPatientNameById;

public class DiseaseManagementController {

    @FXML
    private VBox diseasesPanel;

    @FXML
    private TableView<Disease> diseaseTable;

    @FXML
    private TableColumn<Disease, Integer> diseaseIdColumn;

    @FXML
    private TableColumn<Disease, String> diseaseNameColumn;

    @FXML
    private TextField diseaseNameField;
    @FXML
    private TextField searchDiseaseField;

    @FXML
    private Button addDiseaseButton;

    @FXML
    private Button updateDiseaseButton;

    @FXML
    private Button deleteDiseaseButton;

    private final PatientRegisterDAO patientRegisterDAO = new PatientRegisterDAO();
    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private List<Disease> allDiseases;
    private List<PatientRegister> allPatientRegisters;

    @FXML
    private void initialize() {
        diseaseIdColumn.setCellValueFactory(cellData -> cellData.getValue().diseaseIDProperty().asObject());
        diseaseNameColumn.setCellValueFactory(cellData -> cellData.getValue().diseaseNameProperty());
        loadDiseases();
        setupDynamicDiseaseSearch();
    }

    @FXML
    private void handleAddDisease() {
        String diseaseName = diseaseNameField.getText();
        if (diseaseName.isEmpty()) {
            showError("Disease name cannot be empty.");
            return;
        }
        try {
            Disease newDisease = new Disease();
            newDisease.setDiseaseName(diseaseName);
            diseaseDAO.createDisease(newDisease);
            loadDiseases();
            diseaseNameField.clear();
        } catch (SQLException e) {
            showError("Error adding disease: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDisease() {
        Disease selectedDisease = diseaseTable.getSelectionModel().getSelectedItem();
        if (selectedDisease == null) {
            showError("Please select a disease to update.");
            return;
        }
        String newName = diseaseNameField.getText();
        if (newName.isEmpty()) {
            showError("Disease name cannot be empty.");
            return;
        }
        try {
            selectedDisease.setDiseaseName(newName);
            diseaseDAO.updateDisease(selectedDisease);
            loadDiseases();
            diseaseNameField.clear();
        } catch (SQLException e) {
            showError("Error updating disease: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDisease() {
        Disease selectedDisease = diseaseTable.getSelectionModel().getSelectedItem();
        if (selectedDisease == null) {
            showError("Please select a disease to delete.");
            return;
        }
        try {
            diseaseDAO.deleteDisease(selectedDisease.getDiseaseID());
            loadDiseases();
        } catch (SQLException e) {
            showError("Error deleting disease: " + e.getMessage());
        }
    }

    @FXML
    private void setupDynamicDiseaseSearch() {
        try {
            allDiseases = diseaseDAO.getAllDiseases();
            allPatientRegisters = patientRegisterDAO.getAllPatientRegisters();
        } catch (SQLException e) {
            showError("Error loading data: " + e.getMessage());
            return;
        }
        searchDiseaseField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDiseases(newValue.trim());
        });
    }

    private void filterDiseases(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            diseaseTable.setItems(FXCollections.observableArrayList(allDiseases));
            return;
        }
        String lowerCaseQuery = searchQuery.toLowerCase();
        List<Disease> filteredDiseases = allDiseases.stream()
                .filter(disease -> {
                    boolean matchesDiseaseId = false;
                    boolean matchesDiseaseName = false;
                    try {
                        int id = Integer.parseInt(lowerCaseQuery);
                        matchesDiseaseId = disease.getDiseaseID() == id;
                    } catch (NumberFormatException e) {
                        // Ignored: Query is not a number
                    }
                    if (disease.getDiseaseName() != null) {
                        matchesDiseaseName = disease.getDiseaseName().toLowerCase().contains(lowerCaseQuery);
                    }
                    return matchesDiseaseId || matchesDiseaseName;
                })
                .toList();
        diseaseTable.setItems(FXCollections.observableArrayList(filteredDiseases));
    }


    private void loadDiseases() {
        try {
            List<Disease> diseases = diseaseDAO.getAllDiseases();
            diseaseTable.setItems(FXCollections.observableArrayList(diseases));
        } catch (SQLException e) {
            showError("Error loading diseases: " + e.getMessage());
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
