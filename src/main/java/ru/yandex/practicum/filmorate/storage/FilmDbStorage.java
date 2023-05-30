package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
                    "rating_id, created_at, duration) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlQuery1, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getMpa().getId());
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                ps.setTimestamp(5, currentTimestamp);
                ps.setInt(6, film.getDuration());
                return ps;
            }, holder);
            int filmId = holder.getKey().intValue();
            film.setId(filmId);

            Optional<Object> objectOptional = Optional.ofNullable(film.getGenres());
            if (objectOptional.isPresent()){
                List<Genre> genres = film.getGenres();
                String insertQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?)";

                jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
            }

        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new RuntimeException();
        }

        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.duration, " +
                "f.rating_id as rating_id, " +
                "r.name as rating_name, " +
                "f.release_date, f.duration, f.created_at FROM films f " +
                "INNER JOIN rating r " +
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
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDescription(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            Optional<Object> objectOptional = Optional.ofNullable(film.getGenres());

            String sqlRemovePrevious = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sqlRemovePrevious, film.getId());

            if (objectOptional.isPresent() && !film.getGenres().isEmpty()) {
                List<Genre> genresDistinct = film.getGenres().stream()
                        .distinct().collect(Collectors.toList());
                film.setGenres(genresDistinct);
                System.out.println("Trying to update " +  genresDistinct);
                String insertQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?)";

                jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        System.out.println("Trying to update values = " + film.getId() + " " +
                                genresDistinct.get(i).getId());
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genresDistinct.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genresDistinct.size();
                    }
                });
            }

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
        String sqlQuery = "SELECT id, name, description, duration, release_date, " +
                "rating_id, created_at FROM films " +
                "WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        List<Genre> genres = getFilmGenres(id);
        film.setGenres(genres);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sqlQuery = "WITH popular AS (SELECT f.id as film_id " +
                "FROM films f LEFT JOIN film_likes fk " +
                "ON f.id=fk.film_id " +
                "GROUP BY f.id " +
                "ORDER BY count(fk.user_id) DESC " +
                "LIMIT ?" +
                ")" +
                "SELECT f.id, f.name, f.description, r.id as rating_id," +
                "                r.name as rating_name," +
                "                f.release_date, f.duration, " +
                "                f.created_at FROM films f " +
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
                            .releaseDate(rs.getDate("release_date")
                                    .toLocalDate());

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
        String sqlQueryFilmGenres = "SELECT DISTINCT g.id, g.name FROM films f " +
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

    @Override
    public RatingMpa getFilmMpaDetails(Integer filmId) {
        String sqlQuery = "SELECT r.id, r.name FROM films f join rating r " +
                "ON f.rating_id=r.id " +
                "WHERE f.id = ?";
        RatingMpa ratingMpa = jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<>(RatingMpa.class),
                filmId);
        return ratingMpa;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(getFilmMpaDetails(resultSet.getInt("id")))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
        .build();
    }
}
