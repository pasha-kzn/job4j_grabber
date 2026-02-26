package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO post (name, text, link, created) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, post.getTitle());
            st.setString(2, post.getDescription());
            st.setString(3, post.getLink());
            st.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            st.execute();
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                post.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(format("Не удалось создать запись с name = '%s'", post.getTitle()), e);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM post")) {
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                list.add(getNewPost(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить все записи", e);
        }
        return list;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = null;
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                post = getNewPost(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(format("Не удалось удалить запись c id = %s", id), e);
        }
        return Optional.ofNullable(post);
    }

    private static Post getNewPost(ResultSet resultSet) throws SQLException {
        return new Post(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("text"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").getTime());
    }
}