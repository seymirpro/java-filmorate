package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO USERS (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, holder);
        int userId = holder.getKey().intValue();
        user.setId(userId);
        return user;
    }

    @Override
    public boolean existsInStorage(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sqlQuery,
                new Object[]{id}, Boolean.class);
        return result != null && result;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
                "WHERE id=?";
        int res = jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserByID(Integer id) {
        String sqlQuery = "SELECT id, email, login, name, birthday FROM users " +
                "WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "WITH friends AS (SELECT friend_id FROM user_friends " +
                "WHERE user_id = ?) " +
                "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends)";
        List<User> friends = jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(User.class),
                id
        );
        return friends;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO user_friends(user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM user_friends WHERE user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getMutualFriends(Integer userId, Integer friendId) {

        String sqlSelectMutualFriends = "SELECT u.* FROM user_friends u1 JOIN user_friends u2 " +
                "ON u1.friend_id = u2.friend_id " +
                "JOIN users u ON u1.friend_id = u.id" +
                "   WHERE 1=1 " +
                "   AND u1.user_id = ? " +
                "   AND u2.user_id = ? ";
        List<User> mutualFriends = jdbcTemplate.query(sqlSelectMutualFriends,
                new BeanPropertyRowMapper<>(User.class),
                userId,
                friendId
        );
        return mutualFriends;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    }
}
