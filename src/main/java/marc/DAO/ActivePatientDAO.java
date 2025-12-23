package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.View.ActivePatient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivePatientDAO {


    public List<ActivePatient> getAllActivePatients() throws SQLException {
        String query = "SELECT PatientID, FirstName, LastName, DateOfBirth, Gender, PhoneNumber, BloodGroup FROM activepatients";

        List<ActivePatient> activePatientsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and populate the list
            while (rs.next()) {
                String patientID = rs.getString("PatientID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String dateOfBirth = rs.getString("DateOfBirth");
                String gender = rs.getString("Gender");
                String phoneNumber = rs.getString("PhoneNumber");
                String bloodGroup = rs.getString("BloodGroup");

                ActivePatient activePatient = new ActivePatient(patientID, firstName, lastName, dateOfBirth, gender, phoneNumber, bloodGroup);
                activePatientsList.add(activePatient);
            }
        }

        return activePatientsList;
    }

}
