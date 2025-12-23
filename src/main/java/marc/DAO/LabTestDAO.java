package marc.DAO;

import marc.Class.LabTest;
import marc.Class.Test;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabTestDAO {
    private static Connection connection;

    // Constructor to initialize the database connection
    public LabTestDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();  // Initialize connection here
    }

    // Add a new LabTest
    public void createLabTest(LabTest labTest) throws SQLException {
        String sql = "INSERT INTO labtest (TestID, CreatedOn, ModifiedOn) VALUES (?, NOW(), NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, labTest.getTestId());
            stmt.executeUpdate();
        }
    }

    // Update an existing LabTest
    public void updateLabTest(LabTest labTest) throws SQLException {
        String sql = "UPDATE labtest SET TestID = ?, ModifiedOn = NOW() WHERE LabTestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, labTest.getTestId());
            stmt.setInt(2, labTest.getLabTestID());
            stmt.executeUpdate();
        }
    }

    // Delete a LabTest by its ID
    public void deleteLabTest(int labTestID) throws SQLException {
        String sql = "DELETE FROM labtest WHERE LabTestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, labTestID);
            stmt.executeUpdate();
        }
    }

    // Get all LabTests with their corresponding Test names
    public List<LabTest> getAllLabTests() throws SQLException {
        List<LabTest> labTests = new ArrayList<>();
        String sql = "SELECT lt.LabTestID, lt.TestID, lt.CreatedOn, lt.ModifiedOn, t.TestName " +
                "FROM labtest lt " +
                "JOIN test t ON lt.TestID = t.TestID";  // Ensure the correct table name
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int labTestID = rs.getInt("LabTestID");
                int testID = rs.getInt("TestID");
                String testName = rs.getString("TestName");
                String createdOn = rs.getString("CreatedOn");
                String modifiedOn = rs.getString("ModifiedOn");
                labTests.add(new LabTest(labTestID, testID, testName, createdOn, modifiedOn));
            }
        }
        return labTests;
    }

    public List<String> getLabTestDropdownData() throws SQLException {
        List<String> labTestDropdownData = new ArrayList<>();
        String sql = "SELECT lt.LabTestID, t.TestName " +
                "FROM labtest lt " +
                "JOIN test t ON lt.TestID = t.TestID"; // Ensure correct table names

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int labTestID = rs.getInt("LabTestID");
                String testName = rs.getString("TestName");
                labTestDropdownData.add(labTestID + " - " + testName); // Format: "ID - Test Name"
            }
        }
        return labTestDropdownData;
    }


    public LabTest getLabTestById(int labTestID) throws SQLException {
        String sql = "SELECT lt.LabTestID, lt.TestID, lt.CreatedOn, lt.ModifiedOn, t.TestName " +
                "FROM labtest lt " +
                "JOIN test t ON lt.TestID = t.TestID " +
                "WHERE lt.LabTestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, labTestID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int testID = rs.getInt("TestID");
                    String testName = rs.getString("TestName");
                    String createdOn = rs.getString("CreatedOn");
                    String modifiedOn = rs.getString("ModifiedOn");
                    return new LabTest(labTestID, testID, testName, createdOn, modifiedOn);
                }
            }
        }
        return null;  // Return null if no LabTest is found
    }
    public static String getLabTestNameById(int labTestID) throws SQLException {
        String sql = "SELECT t.TestName " +
                "FROM labtest lt " +
                "JOIN test t ON lt.TestID = t.TestID " +
                "WHERE lt.LabTestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, labTestID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TestName");  // Return the TestName
                }
            }
        }
        return null;  // Return null if no LabTest is found
    }

}
