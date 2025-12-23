package marc.DAO;

import marc.Connection.DatabaseConnection;
import marc.Class.Disease;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiseaseDAO {
    public void createDisease(Disease disease) throws SQLException {
        String query = "INSERT INTO disease (DiseaseName) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, disease.getDiseaseName());
            statement.executeUpdate();
        }
    }

    public Disease getDiseaseById(int id) throws SQLException {
        String query = "SELECT * FROM disease WHERE DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Disease disease = new Disease();
                disease.setDiseaseID(rs.getInt("DiseaseID"));
                disease.setDiseaseName(rs.getString("DiseaseName"));
                return disease;
            }
        }
        return null;
    }

    public static String getDiseaseNameById(int diseaseID) throws SQLException {
        String query = "SELECT DiseaseName FROM disease WHERE DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, diseaseID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("DiseaseName");
            }
        }
        return null; // Return null if disease is not found
    }


    public List<Disease> getAllDiseases() throws SQLException {
        String query = "SELECT * FROM disease";
        List<Disease> diseases = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Disease disease = new Disease();
                disease.setDiseaseID(rs.getInt("DiseaseID"));
                disease.setDiseaseName(rs.getString("DiseaseName"));
                diseases.add(disease);
            }
        }
        return diseases;
    }

    public void updateDisease(Disease disease) throws SQLException {
        String query = "UPDATE disease SET DiseaseName = ? WHERE DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, disease.getDiseaseName());
            statement.setInt(2, disease.getDiseaseID());
            statement.executeUpdate();
        }
    }

    public void deleteDisease(int id) throws SQLException {
        String query = "DELETE FROM disease WHERE DiseaseID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}

