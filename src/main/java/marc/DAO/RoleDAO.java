package marc.DAO;

import marc.Class.Role;
import marc.Connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    public void createRole(Role role) throws SQLException {
        String query = "INSERT INTO role (RoleDesc) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role.getRoleDesc());
            statement.executeUpdate();
        }
    }

    public Role getRoleById(int id) throws SQLException {
        String query = "SELECT * FROM role WHERE RoleID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleDesc(rs.getString("RoleDesc"));
                return role;
            }
        }
        return null;
    }

    public List<Role> getAllRoles() throws SQLException {
        String query = "SELECT * FROM role";
        List<Role> roles = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleDesc(rs.getString("RoleDesc"));
                roles.add(role);
            }
        }
        return roles;
    }

    public void updateRole(Role role) throws SQLException {
        String query = "UPDATE role SET RoleDesc = ? WHERE RoleID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role.getRoleDesc());
            statement.setInt(2, role.getRoleID());
            statement.executeUpdate();
        }
    }

    public void deleteRole(int id) throws SQLException {
        String query = "DELETE FROM role WHERE RoleID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    public Role getRoleByName(String roleName) throws SQLException {
        String query = "SELECT * FROM role WHERE RoleDesc = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roleName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleDesc(rs.getString("RoleDesc"));
                return role;
            }
        }
        return null; // Return null if no role is found
    }

}


