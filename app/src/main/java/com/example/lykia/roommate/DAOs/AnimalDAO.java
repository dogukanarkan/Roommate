package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    private static final String getAnimalByIdQuery = "SELECT * FROM Animal WHERE animal_id = ?";
    private static final String getAnimalByNameQuery = "SELECT * FROM Animal WHERE animal_name = ?";
    private static final String getAllAnimalsQuery = "SELECT * FROM Animal";

    public static AnimalDTO getAnimalById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getAnimalByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractAnimalFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static AnimalDTO getAnimalByName(String name) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getAnimalByNameQuery);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractAnimalFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<AnimalDTO> getAllAnimals() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllAnimalsQuery);

            List<AnimalDTO> animals = new ArrayList<>();

            while (resultSet.next()) {
                AnimalDTO animal = extractAnimalFromResultSet(resultSet);
                animals.add(animal);
            }

            return animals;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static AnimalDTO extractAnimalFromResultSet(ResultSet resultSet) throws SQLException {

        AnimalDTO animal = new AnimalDTO();

        animal.setAnimalId(resultSet.getInt("animal_id"));
        animal.setAnimalName(resultSet.getString("animal_name"));

        return animal;
    }
}
