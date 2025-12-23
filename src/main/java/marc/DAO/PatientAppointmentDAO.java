package marc.DAO;

import marc.View.PatientAppointment;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientAppointmentDAO {

    // Insert a new PatientAppointment
    public void createPatientAppointment(PatientAppointment appointment) throws SQLException {
        String query = "INSERT INTO patientappointment (PatientID, EmployeeID, AppointmentDate, IsComplete, IsCancelled, IsNoShow) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, appointment.getPatientID());
            statement.setInt(2, appointment.getEmployeeID());
            statement.setString(3, appointment.getAppointmentDate());
            statement.setBoolean(4, appointment.getIsComplete());
            statement.setBoolean(5, appointment.getIsCancelled());
            statement.setBoolean(6, appointment.getIsNoShow());
            statement.executeUpdate();
        }
    }

    // Retrieve all PatientAppointments
    public List<PatientAppointment> getAllPatientAppointments() throws SQLException {
        String query = "SELECT * FROM patientappointment";
        List<PatientAppointment> appointments = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int patientID = rs.getInt("PatientID");
                int employeeID = rs.getInt("EmployeeID");
                String appointmentDate = rs.getString("AppointmentDate");
                boolean isComplete = rs.getBoolean("IsComplete");
                boolean isCancelled = rs.getBoolean("IsCancelled");
                boolean isNoShow = rs.getBoolean("IsNoShow");
                String createdOn = rs.getString("CreatedOn");

                appointments.add(new PatientAppointment(patientID, employeeID, appointmentDate, isComplete, isCancelled, isNoShow, createdOn));
            }
        }
        return appointments;
    }

    public void updatePatientAppointment(PatientAppointment appointment, String originalAppointmentDate) throws SQLException {
        String query = """
        UPDATE patientappointment 
        SET PatientID = ?, EmployeeID = ?, AppointmentDate = ?, IsComplete = ?, IsCancelled = ?, IsNoShow = ?
        WHERE PatientID = ? AND AppointmentDate = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set new values
            statement.setInt(1, appointment.getPatientID());
            statement.setInt(2, appointment.getEmployeeID());
            statement.setString(3, appointment.getAppointmentDate());
            statement.setBoolean(4, appointment.getIsComplete());
            statement.setBoolean(5, appointment.getIsCancelled());
            statement.setBoolean(6, appointment.getIsNoShow());

            // Use original PatientID and AppointmentDate as unique identifiers
            statement.setInt(7, appointment.getPatientID());
            statement.setString(8, originalAppointmentDate);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No matching records found to update.");
            }
        }
    }



    // Delete a PatientAppointment by AppointmentDate
    public void deletePatientAppointment(String appointmentDate) throws SQLException {
        String query = "DELETE FROM patientappointment WHERE AppointmentDate = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, appointmentDate);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No matching records found to delete.");
            }
        }
    }

}
