package marc.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import marc.Class.PatientDisease;
import marc.Class.Disease;
import marc.Class.PatientRegister;
import marc.DAO.DiseaseDAO;
import marc.DAO.PatientDiseaseDAO;
import marc.DAO.PatientRegisterDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PatientDiseaseManagementController {

    @FXML
    private VBox patientDiseasePanel;

    @FXML
    private TableView<PatientDisease> patientdiseaseTable;

    @FXML
    private TableColumn<PatientDisease, Integer> colPatientRegisterIDDisease;

    @FXML
    private TableColumn<PatientDisease, String> colPatientRegisterName;

    @FXML
    private TableColumn<PatientDisease, String> colDiseaseName;

    @FXML
    private TableColumn<PatientDisease, Integer> colDiseaseID;

    @FXML
    private TableColumn<PatientDisease, Boolean> colIsActiveDisease;

    @FXML
    private TableColumn<PatientDisease, String> colCreatedOnDisease;

    @FXML
    private TableColumn<PatientDisease, String> colModifiedOnDisease;

    @FXML
    private TextField searchDiseaseField;

    @FXML
    private ComboBox<String> patientRegisterIDComboBox;

    @FXML
    private ComboBox<String> diseaseComboBox;

    @FXML
    private CheckBox isActiveDiseaseCheckBox;

    private final PatientDiseaseDAO patientDiseaseDAO = new PatientDiseaseDAO();
    private final DiseaseDAO diseaseDAO = new DiseaseDAO();
    private final PatientRegisterDAO patientRegisterDAO = new PatientRegisterDAO();

    @FXML
    private void initialize() {
        setupScrollListenerForTable();
        colPatientRegisterIDDisease.setCellValueFactory(cellData -> cellData.getValue().patientRegisterIDProperty().asObject());
        colDiseaseID.setCellValueFactory(cellData -> cellData.getValue().diseaseIDProperty().asObject());
        colIsActiveDisease.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        colCreatedOnDisease.setCellValueFactory(cellData -> cellData.getValue().createdOnProperty());
        colModifiedOnDisease.setCellValueFactory(cellData -> cellData.getValue().modifiedOnProperty());
        colPatientRegisterName.setCellValueFactory(cellData -> {
            int patientRegisterId = cellData.getValue().getPatientRegisterID();
            return new SimpleStringProperty(fetchPatientNameById(patientRegisterId));
        });
        colDiseaseName.setCellValueFactory(cellData -> {
            int diseaseId = cellData.getValue().getDiseaseID();
            return new SimpleStringProperty(fetchDiseaseNameById(diseaseId));
        });

        loadPatientDiseases();
        populatePatientRegisterDropdown();
        populateDiseaseDropdown();
        setupDynamicSearchPatientDisease();
        setupDropdownRefreshListeners();
        initializePatientDiseaseTableClickListener();
    }
    private void setupDropdownRefreshListeners() {
        patientRegisterIDComboBox.setOnMouseClicked(event -> populatePatientRegisterDropdown());
        diseaseComboBox.setOnMouseClicked(event -> populateDiseaseDropdown());
    }


    private void populatePatientRegisterDropdown() {
        try {
            List<String> patientRegisters = patientRegisterDAO.getAllPatientRegisters()
                    .stream()
                    .map(register -> register.getPatientRegisterId() + " - " + fetchPatientNameById(register.getPatientRegisterId()))
                    .collect(Collectors.toList());

            patientRegisterIDComboBox.setItems(FXCollections.observableArrayList(patientRegisters));
        } catch (SQLException e) {
            showError("Error loading patient registers: " + e.getMessage());
        }
    }

    private void populateDiseaseDropdown() {
        try {
            List<String> diseases = diseaseDAO.getAllDiseases()
                    .stream()
                    .map(disease -> disease.getDiseaseID() + " - " + disease.getDiseaseName())
                    .collect(Collectors.toList());

            diseaseComboBox.setItems(FXCollections.observableArrayList(diseases));
        } catch (SQLException e) {
            showError("Error loading diseases: " + e.getMessage());
        }
    }

    private void loadPatientDiseases() {
        try {
            ObservableList<PatientDisease> patientDiseases = patientDiseaseDAO.getAllPatientDiseases();
            patientdiseaseTable.setItems(patientDiseases);
        } catch (SQLException e) {
            showError("Error loading patient diseases: " + e.getMessage());
        }
    }

    @FXML
    private void handleSubmitPatientDisease() {
        try {
            String selectedPatientRegister = patientRegisterIDComboBox.getSelectionModel().getSelectedItem();
            String selectedDisease = diseaseComboBox.getSelectionModel().getSelectedItem();
            boolean isActive = isActiveDiseaseCheckBox.isSelected();

            if (selectedPatientRegister == null || selectedDisease == null) {
                showError("All fields must be filled.");
                return;
            }

            int patientRegisterID = Integer.parseInt(selectedPatientRegister.split(" - ")[0]);
            int diseaseID = Integer.parseInt(selectedDisease.split(" - ")[0]);

            PatientDisease newPatientDisease = new PatientDisease();
            newPatientDisease.setPatientRegisterID(patientRegisterID);
            newPatientDisease.setDiseaseID(diseaseID);
            newPatientDisease.setIsActive(isActive);

            patientDiseaseDAO.createPatientDisease(newPatientDisease);
            loadPatientDiseases();
            clearPatientDiseaseForm();
        } catch (SQLException e) {
            showError("Error adding patient disease: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdatePatientDisease() {
        try {
            PatientDisease selectedPatientDisease = patientdiseaseTable.getSelectionModel().getSelectedItem();

            if (selectedPatientDisease == null) {
                showError("Please select a patient disease to update.");
                return;
            }

            String selectedPatientRegister = patientRegisterIDComboBox.getSelectionModel().getSelectedItem();
            String selectedDisease = diseaseComboBox.getSelectionModel().getSelectedItem();
            boolean isActive = isActiveDiseaseCheckBox.isSelected();

            if (selectedPatientRegister == null || selectedDisease == null) {
                showError("All fields must be filled.");
                return;
            }

            int patientRegisterID = Integer.parseInt(selectedPatientRegister.split(" - ")[0]);
            int diseaseID = Integer.parseInt(selectedDisease.split(" - ")[0]);

            selectedPatientDisease.setPatientRegisterID(patientRegisterID);
            selectedPatientDisease.setDiseaseID(diseaseID);
            selectedPatientDisease.setIsActive(isActive);

            patientDiseaseDAO.updatePatientDisease(selectedPatientDisease);
            loadPatientDiseases();
            clearPatientDiseaseForm();
        } catch (SQLException e) {
            showError("Error updating patient disease: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletePatientDisease() {
        try {
            PatientDisease selectedPatientDisease = patientdiseaseTable.getSelectionModel().getSelectedItem();

            if (selectedPatientDisease == null) {
                showError("Please select a patient disease to delete.");
                return;
            }

            patientDiseaseDAO.deletePatientDisease(selectedPatientDisease.getPatientRegisterID(), selectedPatientDisease.getDiseaseID());
            loadPatientDiseases();
        } catch (SQLException e) {
            showError("Error deleting patient disease: " + e.getMessage());
        }
    }

    private void setupDynamicSearchPatientDisease() {
        searchDiseaseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                loadPatientDiseases();
            } else {
                filterPatientDiseases(newValue.trim().toLowerCase());
            }
        });
    }

    private void filterPatientDiseases(String searchQuery) {
        try {
            List<PatientDisease> allPatientDiseases = patientDiseaseDAO.getAllPatientDiseases();

            List<PatientDisease> filteredPatientDiseases = allPatientDiseases.stream()
                    .filter(patientDisease -> {
                        String patientName = fetchPatientNameById(patientDisease.getPatientRegisterID());
                        String diseaseName = fetchDiseaseNameById(patientDisease.getDiseaseID());
                        return patientName.toLowerCase().contains(searchQuery)
                                || diseaseName.toLowerCase().contains(searchQuery);
                    })
                    .collect(Collectors.toList());

            patientdiseaseTable.setItems(FXCollections.observableArrayList(filteredPatientDiseases));
        } catch (SQLException e) {
            showError("Error filtering patient diseases: " + e.getMessage());
        }
    }

    private String fetchPatientNameById(int patientRegisterId) {
        return PatientRegisterDAO.getPatientNameById(patientRegisterId);
    }

    private String fetchDiseaseNameById(int diseaseId) {
        try {
            return DiseaseDAO.getDiseaseNameById(diseaseId);
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private void initializePatientDiseaseTableClickListener() {
        patientdiseaseTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                populatePatientDiseaseForm();
            }
        });
    }

    private void populatePatientDiseaseForm() {
        PatientDisease selectedPatientDisease = patientdiseaseTable.getSelectionModel().getSelectedItem();

        if (selectedPatientDisease != null) {
            String patientRegisterID = selectedPatientDisease.getPatientRegisterID() + " - " +
                    fetchPatientNameById(selectedPatientDisease.getPatientRegisterID());
            String diseaseID = selectedPatientDisease.getDiseaseID() + " - " +
                    fetchDiseaseNameById(selectedPatientDisease.getDiseaseID());

            patientRegisterIDComboBox.setValue(patientRegisterID);
            diseaseComboBox.setValue(diseaseID);
            isActiveDiseaseCheckBox.setSelected(selectedPatientDisease.getIsActive());
        }
    }

    private void clearPatientDiseaseForm() {
        patientRegisterIDComboBox.getSelectionModel().clearSelection();
        diseaseComboBox.getSelectionModel().clearSelection();
        isActiveDiseaseCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setupScrollListenerForTable() {
        patientdiseaseTable.setOnScroll(event -> {
            refreshPatientDiseaseTable();
        });
    }
    private void refreshPatientDiseaseTable() {
        try {
            ObservableList<PatientDisease> patientDiseases = patientDiseaseDAO.getAllPatientDiseases();
            patientdiseaseTable.setItems(patientDiseases);
            initialize();
        } catch (SQLException e) {
            showError("Error refreshing patient disease table: " + e.getMessage());
        }
    }




}
