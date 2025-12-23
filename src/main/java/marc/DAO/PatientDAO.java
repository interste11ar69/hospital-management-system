package marc.DAO;

import marc.Class.Patient;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void createPatient(Patient patient) throws SQLException {
        String patientQuery = """
        INSERT INTO patient (FirstName, LastName, DateOfBirth, Gender, PhoneNumber, EmailID, Height, Weight, BloodGroup, IsActive) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = connection.prepareStatement(patientQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, patient.getFirstName());
                stmt.setString(2, patient.getLastName());
                stmt.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
                stmt.setString(4, patient.getGender());
                stmt.setString(5, patient.getPhoneNumber());
                stmt.setString(6, patient.getEmailID());
                stmt.setDouble(7, patient.getHeight());
                stmt.setDouble(8, patient.getWeight());
                stmt.setString(9, patient.getBloodGroup());
                stmt.setBoolean(10, patient.getIsActive());

                stmt.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on failure
                throw e;
            } finally {
                connection.setAutoCommit(true); // Restore auto-commit
            }
        }
    }

    public void deletePatient(int patientID) throws SQLException {
        String deletePatientQuery = "DELETE FROM patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = connection.prepareStatement(deletePatientQuery)) {
                stmt.setInt(1, patientID);
                stmt.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on failure
                throw e;
            } finally {
                connection.setAutoCommit(true); // Restore auto-commit
            }
        }
    }

    public Patient getPatientById(int patientID) throws SQLException {
        String query = "SELECT * FROM patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, patientID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        }
        return null;
    }

    public static List<Patient> getAllPatients() throws SQLException {
        String query = "SELECT * FROM patient";
        List<Patient> patients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    public List<Integer> getAllPatientIDs() throws SQLException {
        String query = "SELECT PatientID FROM patient";
        List<Integer> patientIDs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patientIDs.add(rs.getInt("PatientID"));
            }
        }
        return patientIDs;
    }

    public static String getPatientNameById(int patientID) throws SQLException {
        String query = "SELECT CONCAT(FirstName, ' ', LastName) AS FullName FROM patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("FullName");
                }
            }
        }
        return null;
    }

    public void updatePatientIsActive(int patientId, boolean isActive) throws SQLException {
        String query = "UPDATE patient SET IsActive = ? WHERE PatientID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, patientId);
            stmt.executeUpdate();
        }
    }

    public void updatePatient(Patient patient) throws SQLException {
        String updateQuery = """
        UPDATE patient 
        SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, PhoneNumber = ?, EmailID = ?, Height = ?, 
            Weight = ?, BloodGroup = ?, IsActive = ? 
        WHERE PatientID = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, patient.getFirstName());
                stmt.setString(2, patient.getLastName());
                stmt.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
                stmt.setString(4, patient.getGender());
                stmt.setString(5, patient.getPhoneNumber());
                stmt.setString(6, patient.getEmailID());
                stmt.setDouble(7, patient.getHeight());
                stmt.setDouble(8, patient.getWeight());
                stmt.setString(9, patient.getBloodGroup());
                stmt.setBoolean(10, patient.getIsActive());
                stmt.setInt(11, patient.getPatientID());

                stmt.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Patient> searchPatients(String searchQuery) throws SQLException {
        String query = """
        SELECT * FROM patient 
        WHERE FirstName LIKE ? OR LastName LIKE ? OR PhoneNumber LIKE ? OR EmailID LIKE ?
        """;

        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }

    private static Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("DateOfBirth");
        java.time.LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        return new Patient(
                rs.getInt("PatientID"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                localDate,
                rs.getString("Gender"),
                rs.getString("PhoneNumber"),
                rs.getString("EmailID"),
                rs.getDouble("Height"),
                rs.getDouble("Weight"),
                rs.getString("BloodGroup"),
                rs.getBoolean("IsActive"),
                rs.getString("CreatedOn"),
                rs.getString("ModifiedOn")
        );
    }
}
