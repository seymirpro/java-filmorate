package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExist;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addFilm(Film film) throws ValidationException{
        if (filmStorage.existsInStorage(film.getId())) {
            throw new ValidationException();
        }
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        if (!filmStorage.existsInStorage(film.getId())) {
            throw new ValidationException();
        }
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(Film film, Integer userId) {
        film.getUserLikes().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId){
        filmStorage.getFilmByID(filmId).getUserLikes().remove(userId);
    }

    public Film getFilmByID(Integer id) {
        if (!filmStorage.existsInStorage(id)) {
            throw new FilmDoesNotExist();
        }

        return filmStorage.getFilmByID(id);
    }
}
