package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.View.PatientDiseaseHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDiseaseHistoryDAO {

    public List<PatientDiseaseHistory> getPatientDiseaseHistory() throws SQLException {
        List<PatientDiseaseHistory> diseaseHistoryList = new ArrayList<>();

        String query = "SELECT PatientRegisterID, FirstName, LastName, DiseaseName, AdmissionDate, DischargeDate " +
                "FROM PatientDiseaseHistory";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String patientRegisterID = rs.getString("PatientRegisterID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String diseaseName = rs.getString("DiseaseName");
                String admissionDate = rs.getString("AdmissionDate");
                String dischargeDate = rs.getString("DischargeDate");

                PatientDiseaseHistory history = new PatientDiseaseHistory(patientRegisterID, firstName, lastName, diseaseName, admissionDate, dischargeDate);
                diseaseHistoryList.add(history);
            }
        }

        return diseaseHistoryList;
    }
}
