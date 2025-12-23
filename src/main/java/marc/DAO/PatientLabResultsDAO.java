package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.View.PatientLabResults;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientLabResultsDAO {

    public List<PatientLabResults> getPatientLabResults() throws SQLException {
        List<PatientLabResults> labResultsList = new ArrayList<>();

        String query = "SELECT PatientRegisterID, FirstName, LastName, TestName, Result, TestDate " +
                "FROM PatientLabResults";  // Using the view

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String patientRegisterID = rs.getString("PatientRegisterID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String testName = rs.getString("TestName");
                String result = rs.getString("Result");
                String testDate = rs.getString("TestDate");

                PatientLabResults labResults = new PatientLabResults(patientRegisterID, firstName, lastName,
                        testName, result, testDate);
                labResultsList.add(labResults);
            }
        }

        return labResultsList;
    }
}
