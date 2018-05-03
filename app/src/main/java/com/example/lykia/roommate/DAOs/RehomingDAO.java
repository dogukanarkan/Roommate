package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RehomingDAO {

    private static final String getRehomingPetByIdQuery = "SELECT * FROM Rehoming JOIN User ON Rehoming.owner_id = User.user_id JOIN Race ON Rehoming.race_id = Race.race_id JOIN Animal ON Race.animal_id = Animal.animal_id WHERE pet_id = ?";
    private static final String getRehomingPetsByOwnerIdQuery = "SELECT * FROM Rehoming JOIN User ON Rehoming.owner_id = User.user_id JOIN Race ON Rehoming.race_id = Race.race_id JOIN Animal ON Race.animal_id = Animal.animal_id WHERE owner_id = ?";
    private static final String getRehomingPetByCodeQuery = "SELECT * FROM Rehoming JOIN User ON Rehoming.owner_id = User.user_id JOIN Race ON Rehoming.race_id = Race.race_id JOIN Animal ON Race.animal_id = Animal.animal_id WHERE code = ?";
    private static final String getAllRehomingPetsQuery = "SELECT * FROM Rehoming JOIN User ON Rehoming.owner_id = User.user_id JOIN Race ON Rehoming.race_id = Race.race_id JOIN Animal ON Race.animal_id = Animal.animal_id";
    private static final String getRehomingCountQuery = "SELECT * FROM Rehoming ORDER BY pet_id DESC LIMIT 1";
    private static final String insertRehomingPetQuery = "INSERT INTO Rehoming(owner_id, race_id, code, image_path, gender, month_old, information, addition_date) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())";
    private static final String updateRehomingPetQuery = "UPDATE Rehoming SET owner_id = ?, race_id = ?, code = ?, image_path = ?, gender = ?, month_old = ?, information = ?, addition_date = ? WHERE pet_id = ?";
    private static final String deleteRehomingPetByIdQuery = "DELETE FROM Rehoming WHERE pet_id = ?";
    private static final String deleteRehomingPetByCodeQuery = "DELETE FROM Rehoming WHERE code = ?";

    public static RehomingDTO getRehomingPetById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getRehomingPetByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractRehomingPetFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<RehomingDTO> getRehomingPetsByOwnerId(int id) {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getRehomingPetsByOwnerIdQuery);

            List<RehomingDTO> pets = new ArrayList<>();

            while (resultSet.next()) {
                RehomingDTO pet = extractRehomingPetFromResultSet(resultSet);
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

    public static RehomingDTO getRehomingPetByCode(String code) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getRehomingPetByCodeQuery);

            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractRehomingPetFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<RehomingDTO> getAllRehomingPets() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllRehomingPetsQuery);

            List<RehomingDTO> pets = new ArrayList<>();

            while (resultSet.next()) {
                RehomingDTO pet = extractRehomingPetFromResultSet(resultSet);
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

    public static int getRehomingCount() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getRehomingCountQuery);

            if (resultSet.next()) {
                return resultSet.getInt("pet_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return 0;
    }

    public static boolean insertRehomingPet(RehomingDTO pet) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(insertRehomingPetQuery);

            statement.setInt(1, pet.getUser().getUserId());
            statement.setInt(2, pet.getRace().getRaceId());
            statement.setString(3, pet.getCode());
            statement.setString(4, pet.getImagePath());
            statement.setString(5, pet.getGender());
            statement.setInt(6, pet.getMonthOld());
            statement.setString(7, pet.getInformation());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean updateRehomingPet(RehomingDTO pet) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(updateRehomingPetQuery);

            statement.setInt(1, pet.getUser().getUserId());
            statement.setInt(2, pet.getRace().getRaceId());
            statement.setString(3, pet.getCode());
            statement.setString(4, pet.getImagePath());
            statement.setString(5, pet.getGender());
            statement.setInt(6, pet.getMonthOld());
            statement.setString(7, pet.getInformation());
            statement.setTimestamp(8, pet.getAdditionDate());
            statement.setInt(9, pet.getPetId());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean deleteRehomingPetById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(deleteRehomingPetByIdQuery);

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

    public static boolean deleteRehomingPetByCode(String code) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(deleteRehomingPetByCodeQuery);

            statement.setString(1, code);

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static RehomingDTO extractRehomingPetFromResultSet(ResultSet resultSet) throws SQLException {

        RehomingDTO pet = new RehomingDTO();

        pet.setPetId(resultSet.getInt("pet_id"));
        pet.setUser(UserDAO.extractUserFromResultSet(resultSet));
        pet.setRace(RaceDAO.extractRaceFromResultSet(resultSet));
        pet.setCode(resultSet.getString("code"));
        pet.setImagePath(resultSet.getString("image_path"));
        pet.setGender(resultSet.getString("gender"));
        pet.setMonthOld(resultSet.getInt("month_old"));
        pet.setInformation(resultSet.getString("information"));
        pet.setAdditionDate(resultSet.getTimestamp("addition_date"));

        return pet;
    }
}
