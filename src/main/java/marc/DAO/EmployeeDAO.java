package marc.DAO;

import marc.Class.Employee;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Existing methods...

    public void createEmployee(Employee employee) throws SQLException {
        String employeeQuery = "INSERT INTO employee (EmployeeNumber, Email) VALUES (?, ?)";
        String detailsQuery = """
        INSERT INTO employeedetails 
        (EmployeeID, FirstName, LastName, DateOfBirth, Gender, PhoneNumber, RoleID)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;
        String departmentQuery = """
        INSERT INTO employeedepartment 
        (EmployeeID, DepartmentID) 
        VALUES (?, ?)
    """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Insert into `employee` table
            try (PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery, Statement.RETURN_GENERATED_KEYS)) {
                employeeStmt.setString(1, employee.getEmployeeNumber());
                employeeStmt.setString(2, employee.getEmail());
                employeeStmt.executeUpdate();

                ResultSet rs = employeeStmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedEmployeeID = rs.getInt(1);
                    employee.setEmployeeID(generatedEmployeeID);

                    // Insert into `employeedetails` table
                    try (PreparedStatement detailsStmt = connection.prepareStatement(detailsQuery)) {
                        detailsStmt.setInt(1, generatedEmployeeID);
                        detailsStmt.setString(2, employee.getFirstName());
                        detailsStmt.setString(3, employee.getLastName());
                        detailsStmt.setDate(4, java.sql.Date.valueOf(employee.getDateOfBirth()));
                        detailsStmt.setString(5, employee.getGender());
                        detailsStmt.setString(6, employee.getPhoneNumber());
                        detailsStmt.setInt(7, employee.getRoleID());
                        detailsStmt.executeUpdate();
                    }

                    // Insert into `employeedepartment` table
                    try (PreparedStatement departmentStmt = connection.prepareStatement(departmentQuery)) {
                        departmentStmt.setInt(1, generatedEmployeeID);
                        departmentStmt.setInt(2, employee.getDepartmentID());
                        departmentStmt.executeUpdate();
                    }

                    connection.commit(); // Commit transaction
                } else {
                    throw new SQLException("Failed to insert employee: no ID obtained.");
                }
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on failure
                throw e;
            } finally {
                connection.setAutoCommit(true); // Restore auto-commit
            }
        }
    }

    public void deleteEmployee(int employeeID) throws SQLException {
        String departmentQuery = "DELETE FROM employeedepartment WHERE EmployeeID = ?";
        String detailsQuery = "DELETE FROM employeedetails WHERE EmployeeID = ?";
        String employeeQuery = "DELETE FROM employee WHERE EmployeeID = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Delete from EmployeeDepartment
            try (PreparedStatement departmentStmt = connection.prepareStatement(departmentQuery)) {
                departmentStmt.setInt(1, employeeID);
                departmentStmt.executeUpdate();
            }

            // Delete from EmployeeDetails
            try (PreparedStatement detailsStmt = connection.prepareStatement(detailsQuery)) {
                detailsStmt.setInt(1, employeeID);
                detailsStmt.executeUpdate();
            }

            // Delete from Employee
            try (PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)) {
                employeeStmt.setInt(1, employeeID);
                employeeStmt.executeUpdate();
            }

            connection.commit();
        }
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String updateEmployeeDetailsQuery = "UPDATE employeedetails SET FirstName = ?, LastName = ?, PhoneNumber = ?, Gender = ?, " +
                "DateOfBirth = ?, RoleID = ? WHERE EmployeeID = ?";

        String updateEmployeeEmailQuery = "UPDATE employee SET Email = ? WHERE EmployeeID = ?";

        String updateDepartmentQuery = "UPDATE employeedepartment SET DepartmentID = ? WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateDetailsStmt = conn.prepareStatement(updateEmployeeDetailsQuery);
             PreparedStatement updateEmailStmt = conn.prepareStatement(updateEmployeeEmailQuery);
             PreparedStatement updateDepartmentStmt = conn.prepareStatement(updateDepartmentQuery)) {

            conn.setAutoCommit(false);

            // Update employeedetails table
            updateDetailsStmt.setString(1, employee.getFirstName());
            updateDetailsStmt.setString(2, employee.getLastName());
            updateDetailsStmt.setString(3, employee.getPhoneNumber());
            updateDetailsStmt.setString(4, employee.getGender());
            updateDetailsStmt.setDate(5, java.sql.Date.valueOf(employee.getDateOfBirth()));
            updateDetailsStmt.setInt(6, employee.getRoleID());
            updateDetailsStmt.setInt(7, employee.getEmployeeID());
            updateDetailsStmt.executeUpdate();

            // Update email in the employee table
            updateEmailStmt.setString(1, employee.getEmail());
            updateEmailStmt.setInt(2, employee.getEmployeeID());
            updateEmailStmt.executeUpdate();

            // Update employeeDepartment table
            updateDepartmentStmt.setInt(1, employee.getDepartmentID());
            updateDepartmentStmt.setInt(2, employee.getEmployeeID());
            updateDepartmentStmt.executeUpdate();

            conn.commit();
            System.out.println("Employee updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating employee: " + e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        String query = """
        SELECT e.EmployeeID, e.EmployeeNumber, e.Email, 
               ed.FirstName, ed.LastName, ed.DateOfBirth, ed.Gender, ed.PhoneNumber, ed.RoleID, 
               r.RoleDesc, edep.DepartmentID, d.DepartmentName
        FROM employee e
        JOIN employeedetails ed ON e.EmployeeID = ed.EmployeeID
        JOIN employeedepartment edep ON e.EmployeeID = edep.EmployeeID
        JOIN role r ON ed.RoleID = r.RoleID
        JOIN department d ON edep.DepartmentID = d.DepartmentID
    """;

        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setEmployeeNumber(rs.getString("EmployeeNumber"));
                employee.setEmail(rs.getString("Email"));
                employee.setFirstName(rs.getString("FirstName"));
                employee.setLastName(rs.getString("LastName"));
                employee.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
                employee.setGender(rs.getString("Gender"));
                employee.setPhoneNumber(rs.getString("PhoneNumber"));
                employee.setRoleID(rs.getInt("RoleID"));
                employee.setRole(rs.getString("RoleDesc"));
                employee.setDepartmentID(rs.getInt("DepartmentID"));
                employee.setDepartment(rs.getString("DepartmentName"));

                employees.add(employee);
            }
        }
        return employees;
    }

    // New Method: Get Employee Name by ID
    public String getEmployeeNameById(int employeeID) throws SQLException {
        String query = "SELECT CONCAT(FirstName, ' ', LastName) AS FullName FROM employeedetails WHERE EmployeeID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("FullName");
            }
        }
        return "Unknown";
    }
}
