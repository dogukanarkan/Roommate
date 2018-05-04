package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.AdminDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    private static final String getAdminByIdQuery = "SELECT * FROM Admin WHERE account_id = ?";
    private static final String getAdminForLoginQuery = "SELECT * FROM Admin WHERE username = ? AND password = ?";
    private static final String updateAdminQuery = "UPDATE Admin SET username = ?, password = ?, first_name = ?, last_name = ?, register_date = ?, last_login = NOW() WHERE account_id = ?";

    public static AdminDTO getAdminById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getAdminByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static AdminDTO getAdminForLogin(String mail, String hashPassword) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getAdminForLoginQuery);

            statement.setString(1, mail);
            statement.setString(2, hashPassword);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static boolean updateAdmin(AdminDTO admin) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(updateAdminQuery);

            statement.setString(1, admin.getUsername());
            statement.setString(2, admin.getPassword());
            statement.setString(3, admin.getFirstName());
            statement.setString(4, admin.getLastName());
            statement.setTimestamp(5,admin.getRegisterDate());
            statement.setInt(6, admin.getAccountId());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static AdminDTO extractAdminFromResultSet(ResultSet resultSet) throws SQLException {

        AdminDTO admin = new AdminDTO();

        admin.setAccountId(resultSet.getInt("account_id"));
        admin.setUsername(resultSet.getString("username"));
        admin.setPassword(resultSet.getString("password"));
        admin.setFirstName(resultSet.getString("first_name"));
        admin.setLastName(resultSet.getString("last_name"));
        admin.setRegisterDate(resultSet.getTimestamp("register_date"));
        admin.setLastLogin(resultSet.getTimestamp("last_login"));

        return admin;
    }
}
