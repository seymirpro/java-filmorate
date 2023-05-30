package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class GenreDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT id, name FROM genre";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Genre.class));
        return genres;
    }

    public Genre getGenreByID(Integer id) {
        String sqlQuery = "SELECT id, name FROM genre WHERE id = ?";
        Genre genre = jdbcTemplate.queryForObject(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class),
                id);
        return genre;
    }

    public boolean existsInStorage(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM genre WHERE id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sqlQuery,
                new Object[]{id}, Boolean.class);
        return result != null && result;
    }
}
