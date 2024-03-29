package ru.job4j.grabber;

import ru.job4j.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final Connection connection;

    public PsqlStore(Properties config) {
        try {
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO post(title, link, description, created) VALUES (?, ?, ? ,?) ON CONFLICT (link) DO NOTHING;")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> resPosts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    resPosts.add(new Post(
                            resultSet.getString("title"),
                            resultSet.getString("link"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resPosts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post WHERE id=(?)")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    post = collectPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private Post collectPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getString("title"),
                resultSet.getString("link"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}