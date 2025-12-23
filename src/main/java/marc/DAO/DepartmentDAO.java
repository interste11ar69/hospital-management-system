package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.Class.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public void createDepartment(Department department) throws SQLException {
        String query = "INSERT INTO department (DepartmentName) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, department.getDepartmentName());
            statement.executeUpdate();
        }
    }

    public Department getDepartmentById(int id) throws SQLException {
        String query = "SELECT * FROM department WHERE DepartmentID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt("DepartmentID"));
                department.setDepartmentName(rs.getString("DepartmentName"));
                return department;
            }
        }
        return null;
    }

    public List<Department> getAllDepartments() throws SQLException {
        String query = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt("DepartmentID"));
                department.setDepartmentName(rs.getString("DepartmentName"));
                departments.add(department);
            }
        }
        return departments;
    }

    public void updateDepartment(Department department) throws SQLException {
        String query = "UPDATE department SET DepartmentName = ? WHERE DepartmentID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, department.getDepartmentName());
            statement.setInt(2, department.getDepartmentId());
            statement.executeUpdate();
        }
    }

    public void deleteDepartment(int id) throws SQLException {
        String query = "DELETE FROM department WHERE DepartmentID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    public Department getDepartmentByName(String departmentName) throws SQLException {
        String query = "SELECT * FROM department WHERE DepartmentName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, departmentName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt("DepartmentID"));
                department.setDepartmentName(rs.getString("DepartmentName"));
                return department;
            }
        }
        return null; // Return null if no department is found
    }
}

