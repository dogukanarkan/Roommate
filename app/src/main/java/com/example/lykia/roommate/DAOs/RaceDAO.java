package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.RaceDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {

    private static final String getRaceByIdQuery = "SELECT * FROM Race JOIN Animal ON Race.animal_id = Animal.animal_id WHERE race_id = ?";
    private static final String getRacesByAnimalIdQuery = "SELECT * FROM Race JOIN Animal ON Race.animal_id = Animal.animal_id WHERE Race.animal_id = ?";
    private static final String getRacesByAnimalNameQuery = "SELECT * FROM Race JOIN Animal ON Race.animal_id = Animal.animal_id WHERE Race.animal_id IN (SELECT animal_id FROM Animal WHERE animal_name = ?)";
    private static final String getAllRacesQuery = "SELECT * FROM Race JOIN Animal ON Race.animal_id = Animal.animal_id";

    public static RaceDTO getRaceById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getRaceByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractRaceFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<RaceDTO> getRacesByAnimalId(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getRacesByAnimalIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            List<RaceDTO> races = new ArrayList<>();

            while (resultSet.next()) {
                RaceDTO race = extractRaceFromResultSet(resultSet);
                races.add(race);
            }

            return races;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<RaceDTO> getRacesByAnimalName(String name) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getRacesByAnimalNameQuery);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            List<RaceDTO> races = new ArrayList<>();

            while (resultSet.next()) {
                RaceDTO race = extractRaceFromResultSet(resultSet);
                races.add(race);
            }

            return races;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<RaceDTO> getAllRaces() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllRacesQuery);

            List<RaceDTO> races = new ArrayList<>();

            while (resultSet.next()) {
                RaceDTO race = extractRaceFromResultSet(resultSet);
                races.add(race);
            }

            return races;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static RaceDTO extractRaceFromResultSet(ResultSet resultSet) throws SQLException {

        RaceDTO race = new RaceDTO();

        race.setRaceId(resultSet.getInt("race_id"));
        race.setAnimal(AnimalDAO.extractAnimalFromResultSet(resultSet));
        race.setRaceName(resultSet.getString("race_name"));

        return race;
    }
}
