package marc.DAO;

import marc.Class.PatientAttendant;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientAttendantDAO {

    // Method to create a new PatientAttendant
    public void createPatientAttendant(PatientAttendant attendant) throws SQLException {
        String query = "INSERT INTO patientattendant (PatientRegisterID, EmployeeID, IsActive) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameters for PatientRegisterID, EmployeeID, and IsActive
            statement.setInt(1, attendant.getPatientRegisterID());
            statement.setInt(2, attendant.getEmployeeID());
            statement.setBoolean(3, attendant.getIsActive());

            // Execute the insert query (CreatedOn and ModifiedOn will be handled by the DB)
            statement.executeUpdate();
        }
    }


    // Method to get all PatientAttendants
    public List<PatientAttendant> getAllPatientAttendants() throws SQLException {
        String query = "SELECT * FROM patientattendant";
        List<PatientAttendant> attendants = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int patientRegisterID = rs.getInt("PatientRegisterID");
                int employeeID = rs.getInt("EmployeeID");
                boolean isActive = rs.getBoolean("IsActive");
                String createdOn = rs.getString("CreatedOn");
                String modifiedOn = rs.getString("ModifiedOn");

                attendants.add(new PatientAttendant(patientRegisterID, employeeID, isActive, createdOn, modifiedOn));
            }
        }
        return attendants;
    }

    public void updatePatientAttendant(PatientAttendant attendant) throws SQLException {
        String query = "UPDATE patientattendant SET IsActive = ?, ModifiedOn = ? WHERE PatientRegisterID = ? AND EmployeeID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBoolean(1, attendant.getIsActive());

            // If ModifiedOn is null, the database will set the timestamp automatically
            if (attendant.getModifiedOn() != null) {
                // Use the LocalDateTime converted to Timestamp
                statement.setTimestamp(2, Timestamp.valueOf(attendant.getModifiedOn()));
            } else {
                // If no modified date is provided, you can leave it out or set to NULL
                statement.setNull(2, Types.TIMESTAMP);
            }

            statement.setInt(3, attendant.getPatientRegisterID());
            statement.setInt(4, attendant.getEmployeeID());

            statement.executeUpdate();
        }
    }

    // Method to delete a PatientAttendant by PatientRegisterID and EmployeeID
    public void deletePatientAttendant(int patientRegisterID, int employeeID) throws SQLException {
        String query = "DELETE FROM patientattendant WHERE PatientRegisterID = ? AND EmployeeID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientRegisterID);
            statement.setInt(2, employeeID);
            statement.executeUpdate();
        }
    }

    // Method to fetch all employee names (for ComboBox in the controller)
    public List<String> getAllEmployeeNames() throws SQLException {
        String query = "SELECT name FROM employees"; // Assuming 'employees' is your employee table
        List<String> employeeNames = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                employeeNames.add(rs.getString("name"));
            }
        }
        return employeeNames;
    }

    // Method to get employee ID by employee name (to link ComboBox selection with ID)
    public int getEmployeeIDByName(String name) throws SQLException {
        String query = "SELECT EmployeeID FROM employees WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("EmployeeID");
            }
        }
        return -1; // Return -1 if no matching employee is found
    }
}
