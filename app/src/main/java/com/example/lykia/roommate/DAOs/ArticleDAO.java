package com.example.lykia.roommate.DAOs;

import com.example.lykia.roommate.DTOs.ArticleDTO;
import com.example.lykia.roommate.DatabaseOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private static final String getArticleByIdQuery = "SELECT * FROM Article JOIN Admin ON Article.addition_id = Admin.account_id WHERE article_id = ?";
    private static final String getAllArticlesQuery = "SELECT * FROM Article JOIN Admin ON Article.addition_id = Admin.account_id";
    private static final String insertArticleQuery = "INSERT INTO Article(addition_id, image_path, header, text, addition_date) VALUES(?, ?, ?, ?, NOW())";
    private static final String deleteArticleQuery = "DELETE FROM Article WHERE article_id = ?";

    public static ArticleDTO getArticleById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(getArticleByIdQuery);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractArticleFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static List<ArticleDTO> getAllArticles() {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllArticlesQuery);

            List<ArticleDTO> articles = new ArrayList<>();

            while (resultSet.next()) {
                ArticleDTO article = extractArticleFromResultSet(resultSet);
                articles.add(article);
            }

            return articles;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return null;
    }

    public static boolean insertArticle(ArticleDTO article) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(insertArticleQuery);

            statement.setInt(1, article.getAdmin().getAccountId());
            statement.setString(2, article.getImagePath());
            statement.setString(3, article.getHeader());
            statement.setString(4, article.getText());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseOperations.closeStatement(statement);
            DatabaseOperations.closeConnection(connection);
        }

        return result == 1;
    }

    public static boolean deleteArticle(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        try {
            connection = DatabaseOperations.openConnection();
            statement = connection.prepareStatement(deleteArticleQuery);

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

    public static ArticleDTO extractArticleFromResultSet(ResultSet resultSet) throws SQLException {

        ArticleDTO article = new ArticleDTO();

        article.setArticleId(resultSet.getInt("article_id"));
        article.setAdmin(AdminDAO.extractAdminFromResultSet(resultSet));
        article.setImagePath(resultSet.getString("image_path"));
        article.setHeader(resultSet.getString("header"));
        article.setText(resultSet.getString("text"));
        article.setAdditionDate(resultSet.getDate("addition_date"));

        return article;
    }
}
