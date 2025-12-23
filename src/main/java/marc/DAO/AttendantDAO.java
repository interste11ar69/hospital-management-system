package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.Class.Attendant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendantDAO {

    // Method to get all attendants from the database
    public List<Attendant> getAllAttendants() throws SQLException {
        String query = "SELECT PatientID, FirstName, LastName, EmployeeID, EmployeeNumber, " +
                "AttendantFirstName, AttendantLastName FROM patientattendants";

        List<Attendant> attendants = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String patientID = rs.getString("PatientID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String employeeID = rs.getString("EmployeeID");
                String employeeNumber = rs.getString("EmployeeNumber");
                String attendantFirstName = rs.getString("AttendantFirstName");
                String attendantLastName = rs.getString("AttendantLastName");

                attendants.add(new Attendant(patientID, firstName, lastName,
                        employeeID, employeeNumber,
                        attendantFirstName, attendantLastName));
            }
        }

        return attendants;
    }

}
