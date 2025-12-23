package marc.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import marc.Class.PatientLabReport;
import marc.Connection.DatabaseConnection;

import java.sql.*;

public class PatientLabReportDAO {

    // Create a new patient lab report in the database
    public void createPatientLabReport(PatientLabReport labReport) throws SQLException {
        String query = "INSERT INTO patientlabreport (PatientRegisterID, LabTestID, Result, EmployeeID) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, labReport.getPatientRegisterID());
            statement.setInt(2, labReport.getLabTestID());
            statement.setString(3, labReport.getLabTestResult());
            statement.setInt(4, labReport.getEmployeeID()); // Set the EmployeeID
            statement.executeUpdate();
        }
    }

    // Retrieve a patient lab report by ID
    public PatientLabReport getPatientLabReportById(int id) throws SQLException {
        String query = "SELECT plr.*, CONCAT(ed.FirstName, ' ', ed.LastName) AS EmployeeFullName " +
                "FROM patientlabreport plr " +
                "LEFT JOIN employee e ON plr.EmployeeID = e.EmployeeID " +
                "LEFT JOIN employeedetails ed ON e.EmployeeID = ed.EmployeeID " +
                "WHERE plr.PatientLabReportID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapToPatientLabReport(rs);
            }
        }
        return null;
    }

    // Retrieve all patient lab reports
    public ObservableList<PatientLabReport> getAllPatientLabReports() throws SQLException {
        String query = "SELECT plr.*, CONCAT(ed.FirstName, ' ', ed.LastName) AS EmployeeFullName " +
                "FROM patientlabreport plr " +
                "LEFT JOIN employee e ON plr.EmployeeID = e.EmployeeID " +
                "LEFT JOIN employeedetails ed ON e.EmployeeID = ed.EmployeeID";
        ObservableList<PatientLabReport> labReports = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                labReports.add(mapToPatientLabReport(rs));
            }
        }
        return labReports;
    }

    // Update a patient lab report in the database
    public void updatePatientLabReport(PatientLabReport labReport) throws SQLException {
        String query = "UPDATE patientlabreport SET PatientRegisterID = ?, LabTestID = ?, Result = ?, EmployeeID = ? " +
                "WHERE PatientLabReportID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, labReport.getPatientRegisterID());
            statement.setInt(2, labReport.getLabTestID());
            statement.setString(3, labReport.getLabTestResult());
            statement.setInt(4, labReport.getEmployeeID()); // Update EmployeeID
            statement.setInt(5, labReport.getPatientLabReportID());
            statement.executeUpdate();
        }
    }

    // Delete a patient lab report by ID
    public void deletePatientLabReport(int id) throws SQLException {
        String query = "DELETE FROM patientlabreport WHERE PatientLabReportID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Retrieve all lab reports by EmployeeID
    public ObservableList<PatientLabReport> getLabReportsByEmployeeID(int employeeID) throws SQLException {
        String query = "SELECT plr.*, CONCAT(ed.FirstName, ' ', ed.LastName) AS EmployeeFullName " +
                "FROM patientlabreport plr " +
                "LEFT JOIN employee e ON plr.EmployeeID = e.EmployeeID " +
                "LEFT JOIN employeedetails ed ON e.EmployeeID = ed.EmployeeID " +
                "WHERE plr.EmployeeID = ?";
        ObservableList<PatientLabReport> labReports = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                labReports.add(mapToPatientLabReport(rs));
            }
        }
        return labReports;
    }

    // Helper method to map result set to PatientLabReport object
    private PatientLabReport mapToPatientLabReport(ResultSet rs) throws SQLException {
        int patientLabReportID = rs.getInt("PatientLabReportID");
        int patientRegisterID = rs.getInt("PatientRegisterID");
        int labTestID = rs.getInt("LabTestID");
        String labTestResult = rs.getString("Result");
        String createdOn = rs.getString("CreatedOn");
        String modifiedOn = rs.getString("ModifiedOn");
        int employeeID = rs.getInt("EmployeeID");
        String employeeFullName = rs.getString("EmployeeFullName"); // Get the employee full name

        return new PatientLabReport(patientLabReportID, patientRegisterID, labTestID, labTestResult, createdOn, modifiedOn, employeeID, employeeFullName);
    }
}
