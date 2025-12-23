package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.Class.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Method to retrieve all patient appointments from the database
    public List<Appointment> getAllAppointments() throws SQLException {
        String query = "SELECT p.PatientID, p.FirstName, p.LastName, a.AppointmentDate, a.AppointmentStatus " +
                "FROM patient p " +
                "JOIN patientappointments a ON p.PatientID = a.PatientID";

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and create Appointment objects
            while (rs.next()) {
                String patientID = rs.getString("PatientID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String appointmentDate = rs.getString("AppointmentDate");
                String appointmentStatus = rs.getString("AppointmentStatus");

                appointments.add(new Appointment(patientID, firstName, lastName, appointmentDate, appointmentStatus));
            }
        }
        return appointments;
    }

}
