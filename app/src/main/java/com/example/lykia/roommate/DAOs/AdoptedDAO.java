package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.AdoptedDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdoptedDAO {

    private static final String getAdoptedPetQuery = "SELECT * FROM Adopted JOIN User AS FromUser ON Adopted.from_user_id = FromUser.user_id JOIN User AS ToUser ON Adopted.to_user_id = ToUser.to_user_id JOIN Race ON Adopted.race_id = Race.race_id WHERE adopted_id = ?";
    private static final String getAllAdoptedPetsQuery = "SELECT * FROM Adopted JOIN User AS FromUser ON Adopted.from_user_id = FromUser.user_id JOIN User AS ToUser ON Adopted.to_user_id = ToUser.to_user_id JOIN Race ON Adopted.race_id = Race.race_id";
    private static final String insertAdoptedPetQuery = "INSERT INTO Adopted(from_user_id, to_user_id, race_id, image_path, gender, month_old, adopted_date) VALUES(?, ?, ?, ?, ?, ?, NOW())";
    private static final String deleteAdoptedPetQuery = "DELETE FROM Adopted WHERE adopted_id = ?";

    public static AdoptedDTO getAdoptedPet(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getAdoptedPetQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractAdoptedPetFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<AdoptedDTO> getAllAdoptedPets() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllAdoptedPetsQuery);

            List<AdoptedDTO> pets = new ArrayList<>();

            while (resultSet.next()) {
                AdoptedDTO pet = extractAdoptedPetFromResultSet(resultSet);
                pets.add(pet);
            }

            return pets;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static boolean deleteAdoptedPet(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(deleteAdoptedPetQuery);

            statement.setInt(1, id);

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean insertAdoptedPet(AdoptedDTO pet) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(insertAdoptedPetQuery);

            statement.setInt(1, pet.getFromUser().getUserId());
            statement.setInt(2, pet.getToUser().getUserId());
            statement.setInt(3, pet.getRace().getRaceId());
            statement.setString(4, pet.getImagePath());
            statement.setString(5, pet.getGender());
            statement.setInt(6, pet.getMonthOld());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static AdoptedDTO extractAdoptedPetFromResultSet(ResultSet resultSet) throws SQLException {

        AdoptedDTO pet = new AdoptedDTO();

        pet.setAdoptedId(resultSet.getInt("adopted_id"));
        pet.setFromUser(UserDAO.extractUserFromResultSet(resultSet));
        pet.setToUser(UserDAO.extractUserFromResultSet(resultSet));
        pet.setRace(RaceDAO.extractRaceFromResultSet(resultSet));
        pet.setImagePath(resultSet.getString("image_path"));
        pet.setGender(resultSet.getString("gender"));
        pet.setMonthOld(resultSet.getInt("month_old"));
        pet.setAdoptedDate(resultSet.getTimestamp("adopted_date"));

        return pet;
    }
}
