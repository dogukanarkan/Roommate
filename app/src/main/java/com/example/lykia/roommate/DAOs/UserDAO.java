package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.UserDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String getUserByIdQuery = "SELECT * FROM User WHERE user_id = ?";
    private static final String getUserByMailQuery = "SELECT * FROM User WHERE mail = ?";
    private static final String getUserForLoginQuery = "SELECT * FROM User WHERE mail = ? AND password = ?";
    private static final String getAllUsersQuery = "SELECT * FROM User";
    private static final String insertUserQuery = "INSERT INTO User(mail, image_path, first_name, last_name, location, password, register_date) VALUES(?, ?, ?, ?, ?, ?, NOW())";
    private static final String updateUserQuery = "UPDATE User SET mail = ?, image_path = ?, first_name = ?, last_name = ?, location = ?, password = ?, register_date = ? WHERE user_id = ?";
    private static final String deleteUserQuery = "DELETE FROM User WHERE user_id = ?";

    public static UserDTO getUserById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getUserByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static UserDTO getUserByMail(String mail) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getUserByMailQuery);

            statement.setString(1, mail);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static UserDTO getUserForLogin(String mail, String hashPassword) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getUserForLoginQuery);

            statement.setString(1, mail);
            statement.setString(2, hashPassword);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<UserDTO> getAllUsers() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllUsersQuery);

            List<UserDTO> users = new ArrayList<>();

            while (resultSet.next()) {
                UserDTO user = extractUserFromResultSet(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static boolean insertUser(UserDTO user) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(insertUserQuery);

            statement.setString(1, user.getMail());
            statement.setString(2, user.getImagePath());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getLocation());
            statement.setString(6, user.getPassword());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean updateUser(UserDTO user) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(updateUserQuery);

            statement.setString(1, user.getMail());
            statement.setString(2, user.getImagePath());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getLocation());
            statement.setString(6, user.getPassword());
            statement.setDate(7, user.getRegisterDate());
            statement.setInt(8, user.getUserId());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean deleteUser(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(deleteUserQuery);

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

    public static UserDTO extractUserFromResultSet(ResultSet resultSet) throws SQLException {

        UserDTO user = new UserDTO();

        user.setUserId(resultSet.getInt("user_id"));
        user.setMail(resultSet.getString("mail"));
        user.setImagePath(resultSet.getString("image_path"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setLocation(resultSet.getString("location"));
        user.setPassword(resultSet.getString("password"));
        user.setRegisterDate(resultSet.getDate("register_date"));

        return user;
    }
}
