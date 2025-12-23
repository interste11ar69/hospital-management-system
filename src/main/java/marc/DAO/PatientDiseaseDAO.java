package marc.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import marc.Class.PatientDisease;
import marc.Connection.DatabaseConnection;

import java.sql.*;

public class PatientDiseaseDAO {

    // Create a new patient disease record
    public void createPatientDisease(PatientDisease patientDisease) throws SQLException {
        String query = "INSERT INTO patientdisease (PatientRegisterID, DiseaseID, IsActive) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientDisease.getPatientRegisterID());
            statement.setInt(2, patientDisease.getDiseaseID());
            statement.setBoolean(3, patientDisease.getIsActive());
            statement.executeUpdate();
        }
    }

    // Retrieve a patient disease record by ID
    public PatientDisease getPatientDiseaseById(int patientRegisterID, int diseaseID) throws SQLException {
        String query = "SELECT * FROM patientdisease WHERE PatientRegisterID = ? AND DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientRegisterID);
            statement.setInt(2, diseaseID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapToPatientDisease(rs);
            }
        }
        return null;
    }

    // Retrieve all patient disease records
    public ObservableList<PatientDisease> getAllPatientDiseases() throws SQLException {
        String query = "SELECT * FROM patientdisease";
        ObservableList<PatientDisease> diseases = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                diseases.add(mapToPatientDisease(rs));
            }
        }
        return diseases;
    }

    public void updatePatientDisease(PatientDisease patientDisease) throws SQLException {
        // Make sure only the IsActive field gets updated, based on PatientRegisterID and DiseaseID
        String query = "UPDATE patientdisease SET IsActive = ? WHERE PatientRegisterID = ? AND DiseaseID = ?";

        // Make sure the database connection is valid
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters for the PreparedStatement
            statement.setBoolean(1, patientDisease.getIsActive()); // Update only IsActive
            statement.setInt(2, patientDisease.getPatientRegisterID()); // Use PatientRegisterID to identify the record
            statement.setInt(3, patientDisease.getDiseaseID()); // Use DiseaseID to identify the record

            // Execute the update
            int rowsAffected = statement.executeUpdate();

            // Optionally, log the result for debugging
            if (rowsAffected == 0) {
                System.out.println("No records updated, possibly invalid PatientRegisterID or DiseaseID.");
            } else {
                System.out.println("Patient disease updated successfully.");
            }
        } catch (SQLException e) {
            // Handle SQLException
            System.err.println("Error updating patient disease: " + e.getMessage());
            throw e;  // Re-throw exception to handle further up the stack
        }
    }



    // Delete a patient disease record
    public void deletePatientDisease(int patientRegisterID, int diseaseID) throws SQLException {
        String query = "DELETE FROM patientdisease WHERE PatientRegisterID = ? AND DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientRegisterID);
            statement.setInt(2, diseaseID);
            statement.executeUpdate();
        }
    }

    // Map a ResultSet row to a PatientDisease object
    private PatientDisease mapToPatientDisease(ResultSet rs) throws SQLException {
        int patientRegisterID = rs.getInt("PatientRegisterID");
        int diseaseID = rs.getInt("DiseaseID");
        boolean isActive = rs.getBoolean("IsActive");
        String createdOn = rs.getString("CreatedOn");  // Assuming CreatedOn is a column in your DB
        String modifiedOn = rs.getString("ModifiedOn");  // Assuming ModifiedOn is a column in your DB
        return new PatientDisease(patientRegisterID, diseaseID, isActive, createdOn, modifiedOn);
    }
}
