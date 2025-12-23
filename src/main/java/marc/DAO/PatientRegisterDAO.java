package marc.DAO;

import marc.Class.PatientRegister;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRegisterDAO {

    // Create a new PatientRegister record
    public void createPatientRegister(PatientRegister register) throws SQLException {
        String query = """
        INSERT INTO patientregister (PatientID, AdmittedOn, DischargeOn, RoomNumber, IsActive) 
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, register.getPatientId());
            statement.setDate(2, register.getAdmittedOn() != null ? Date.valueOf(register.getAdmittedOn()) : null);
            statement.setDate(3, register.getDischargeOn() != null ? Date.valueOf(register.getDischargeOn()) : null);
            statement.setString(4, register.getRoomNumber());
            statement.setBoolean(5, register.getIsActive());

            statement.executeUpdate();
        }
    }



    // Retrieve all PatientRegister records
    public List<PatientRegister> getAllPatientRegisters() throws SQLException {
        String query = "SELECT * FROM patientregister";
        List<PatientRegister> registers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                registers.add(mapToPatientRegister(rs));
            }
        }
        return registers;
    }

    // Update an existing PatientRegister record
    public void updatePatientRegister(PatientRegister register) throws SQLException {
        String query = """
        UPDATE patientregister 
        SET PatientID = ?, AdmittedOn = ?, DischargeOn = ?, RoomNumber = ?, IsActive = ? 
        WHERE PatientRegisterID = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, register.getPatientId());
            statement.setDate(2, register.getAdmittedOn() != null ? Date.valueOf(register.getAdmittedOn()) : null);
            statement.setDate(3, register.getDischargeOn() != null ? Date.valueOf(register.getDischargeOn()) : null);
            statement.setString(4, register.getRoomNumber());
            statement.setBoolean(5, register.getIsActive());
            statement.setInt(6, register.getPatientRegisterId());

            statement.executeUpdate();
        }
    }

    // Delete a PatientRegister record by ID
    public void deletePatientRegister(int id) throws SQLException {
        String query = "DELETE FROM patientregister WHERE PatientRegisterID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Helper method to map a ResultSet to a PatientRegister object
    private PatientRegister mapToPatientRegister(ResultSet rs) throws SQLException {
        return new PatientRegister(
                rs.getInt("PatientRegisterID"),
                rs.getInt("PatientID"),
                rs.getDate("AdmittedOn") != null ? rs.getDate("AdmittedOn").toLocalDate() : null,
                rs.getDate("DischargeOn") != null ? rs.getDate("DischargeOn").toLocalDate() : null,
                rs.getString("RoomNumber"),
                rs.getBoolean("IsActive"),
                rs.getString("CreatedOn"),
                rs.getString("ModifiedOn")
        );
    }

    public static String getPatientNameById(int patientRegisterID) {
        String patientName = null;

        String query = "SELECT PatientName FROM patientregister WHERE PatientRegisterID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientRegisterID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patientName = rs.getString("PatientName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (you may want to log this properly in production)
        }

        return patientName;  // Return the PatientName, or null if not found
    }




}
