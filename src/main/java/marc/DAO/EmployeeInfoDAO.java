package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.View.EmployeeInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeInfoDAO {

    // Method to fetch all employee information from the EmployeeInfo view
    public List<EmployeeInfo> getAllEmployeeInfo() throws SQLException {
        String query = "SELECT EmployeeID, FirstName, LastName, Gender, DateOfBirth, PhoneNumber, Role, DepartmentName FROM EmployeeInfo";

        List<EmployeeInfo> employeeInfoList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and populate the list
            while (rs.next()) {
                String employeeID = String.valueOf(rs.getInt("EmployeeID"));
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String gender = rs.getString("Gender");
                String dateOfBirth = rs.getString("DateOfBirth");
                String phoneNumber = rs.getString("PhoneNumber");
                String role = rs.getString("Role");
                String department = rs.getString("DepartmentName");

                EmployeeInfo employeeInfo = new EmployeeInfo(employeeID, firstName, lastName, gender, dateOfBirth, phoneNumber, role, department);
                employeeInfoList.add(employeeInfo);
            }
        }

        return employeeInfoList;
    }

}
