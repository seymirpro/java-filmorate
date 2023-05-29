package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Film addFilm(Film film) {
        try {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            String sqlQuery1 = "INSERT INTO films (name, description, release_date, " +
                    "rating_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlQuery1, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getMpa().getId());
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                ps.setTimestamp(5, currentTimestamp);

                return ps;
            }, holder);
            int filmId = holder.getKey().intValue();
            film.setId(filmId);

        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new RuntimeException();
        }

        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.duration, r.id as rating_id, " +
                "r.name as rating_name, " +
                "f.release_date, f.duration, f.created_at FROM films f " +
                "LEFT JOIN rating r " +
                "ON f.rating_id=r.id ";
        List<Film> allFilms = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> {
                    Film.FilmBuilder film = Film.builder();
                    film.id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .duration(rs.getInt("duration"))
                            .releaseDate(rs.getDate("release_date").toLocalDate());

                    RatingMpa ratingMpa = RatingMpa.builder()
                            .id(rs.getInt("rating_id"))
                                    .name(rs.getString("rating_name"))
                            .build();
                    film.mpa(ratingMpa);

                    String sqlQueryFilmGenres = "SELECT g.id, g.name FROM films f " +
                            "JOIN film_genre fg " +
                            "ON f.id=fg.film_id " +
                            "JOIN genre g " +
                            "ON fg.genre_id=g.id " +
                            "WHERE f.id = ?";
                    List<Genre> genres = jdbcTemplate.query(sqlQueryFilmGenres,
                            new BeanPropertyRowMapper<>(Genre.class),
                            rs.getInt("id")
                            );
                    film.genres(genres);
                    return film.build();
                });
        return allFilms;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            String sqlQuery = "UPDATE films SET name = ?, " +
                    "release_date = ?, " +
                    "description = ?, " +
                    "duration = ?, " +
                    "rating_id = ? " +
                    "WHERE id = ?";
            int res = jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDescription(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } catch (Exception ex){
            System.out.println(film);
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new RuntimeException();
        }

        return film;
    }

    @Override
    public boolean existsInStorage(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM films WHERE id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sqlQuery,
                new Object[]{id}, Boolean.class);
        return result!=null && result;
    }

    @Override
    public Film getFilmByID(Integer id) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sqlQuery = "WITH popular AS (SELECT f.id as film_id " +
                "FROM films f LEFT JOIN film_likes fk " +
                "ON f.id=fk.film_id " +
                "GROUP BY 1 " +
                "ORDER BY count(*) DESC " +
                "LIMIT ?" +
                ")" +
                "SELECT f.id, f.name, f.description, r.id as rating_id," +
                "                r.name as rating_name," +
                "                f.release_date, f.duration, f.created_at FROM films f " +
                "                LEFT JOIN rating r  " +
                "                ON f.rating_id=r.id " +
                "WHERE f.id IN (SELECT film_id FROM popular)";
        List<Film> allFilms = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> {
                    Film.FilmBuilder film = Film.builder();
                    film.id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .duration(rs.getInt("duration"))
                            .releaseDate(rs.getDate("release_date").toLocalDate());

                    RatingMpa ratingMpa = RatingMpa.builder()
                            .id(rs.getInt("rating_id"))
                            .name(rs.getString("rating_name"))
                            .build();
                    film.mpa(ratingMpa);

                    List<Genre> genres = getFilmGenres(rs.getInt("id"));
                    film.genres(genres);
                    return film.build();
                }, count);
        return allFilms;
    }

    @Override
    public List<Genre> getFilmGenres(Integer filmId) {
        String sqlQueryFilmGenres = "SELECT g.id, g.name FROM films f " +
                "JOIN film_genre fg " +
                "ON f.id=fg.film_id " +
                "JOIN genre g " +
                "ON fg.genre_id=g.id " +
                "WHERE f.id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQueryFilmGenres,
                new BeanPropertyRowMapper<>(Genre.class),
                filmId
        );

        return genres;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(RatingMpa.builder().id(resultSet.getInt("id")).build())
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
        .build();
    }
}
