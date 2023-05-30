package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

@Repository
public class RatingMpaDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingMpaDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public RatingMpa getRatingMpaByID(Integer id) {
        String sqlQuery = "SELECT id, name FROM rating WHERE id = ?";
        RatingMpa ratingMpa = jdbcTemplate.queryForObject(sqlQuery,
                new BeanPropertyRowMapper<>(RatingMpa.class), id);
        return ratingMpa;
    }

    public List<RatingMpa> getAllRatingMpas() {
        String sqlQuery = "SELECT id, name FROM rating";
        List<RatingMpa> ratingMpaList = jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(RatingMpa.class));
        return ratingMpaList;
    }

    public boolean existsInStorage(Integer id){
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM rating WHERE id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sqlQuery,
                new Object[]{id}, Boolean.class);
        return result!=null && result;
    }
}
