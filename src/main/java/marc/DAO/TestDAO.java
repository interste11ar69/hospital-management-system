package marc.DAO;

import marc.Class.Test;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestDAO {

    // Create a new test
    public void createTest(Test test) throws SQLException {
        String query = "INSERT INTO test (TestName, Description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, test.getTestName());
            statement.setString(2, test.getDescription());
            statement.executeUpdate();
        }
    }

    // Get a test by its ID
    public Test getTestById(int id) throws SQLException {
        String query = "SELECT * FROM test WHERE TestID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Test test = new Test();
                test.setTestId(rs.getInt("TestID"));
                test.setTestName(rs.getString("TestName"));
                test.setDescription(rs.getString("Description"));
                return test;
            }
        }
        return null;
    }

    // Get all tests
    public List<Test> getAllTests() throws SQLException {
        String query = "SELECT * FROM test";
        List<Test> tests = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Test test = new Test();
                test.setTestId(rs.getInt("TestID"));
                test.setTestName(rs.getString("TestName"));
                test.setDescription(rs.getString("Description"));
                tests.add(test);
            }
        }
        return tests;
    }

    // Update an existing test
    public void updateTest(Test test) throws SQLException {
        String query = "UPDATE test SET TestName = ?, Description = ? WHERE TestID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, test.getTestName());
            statement.setString(2, test.getDescription());
            statement.setInt(3, test.getTestId());
            statement.executeUpdate();
        }
    }

    // Delete a test by its ID
    public void deleteTest(int id) throws SQLException {
        String query = "DELETE FROM test WHERE TestID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
