package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id;
    private Map<Integer, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        return this.films.values();
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean existsInStorage(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Film getFilmByID(Integer id) {
        return films.get(id);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return List.of();
    }

    @Override
    public List<Genre> getFilmGenres(Integer filmId) {
        return null;
    }

    @Override
    public RatingMpa getFilmMpaDetails(Integer filmId) {
        return null;
    }

    @Override
    public void addLike(Integer id, Integer userId) {

    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {

    }
}
