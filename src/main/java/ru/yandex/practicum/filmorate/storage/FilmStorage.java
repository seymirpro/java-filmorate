package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Collection<Film> getFilms();

    Film updateFilm(Film film);

    boolean existsInStorage(Integer id);

    Film getFilmByID(Integer id);

    List<Film> getMostPopularFilms(Integer count);

    List<Genre> getFilmGenres(Integer filmId);

    RatingMpa getFilmMpaDetails(Integer filmId);

    void addLike(Integer id, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
