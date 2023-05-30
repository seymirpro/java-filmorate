package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addFilm(Film film) throws ValidationException {
        if (filmStorage.existsInStorage(film.getId())) {
            log.error("Film with id={} already exist", film.getId());
            throw new ValidationException();
        }


        filmStorage.addFilm(film);
        log.info("Film added");

    }

    public void updateFilm(Film film) {
        if (!filmStorage.existsInStorage(film.getId())) {
            throw new FilmDoesNotExistException();
        }


        filmStorage.updateFilm(film);
        filmStorage.updateFilm(film);
        log.info("Film updated");
    }

    public Collection<Film> getFilms() {
        log.info("Getting films...");
        Collection<Film> allFilms = filmStorage.getFilms();
        return allFilms;
    }

    public void addLike(Integer filmId, Integer userId) {

        if (!filmStorage.existsInStorage(filmId)) {
            throw new FilmDoesNotExistException();
        }

        if (!userStorage.existsInStorage(userId)) {
            throw new UserDoesNotExistException();
        }

        filmStorage.addLike(filmId, userId);

    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!filmStorage.existsInStorage(filmId)) {
            throw new FilmDoesNotExistException();
        }

        if (!userStorage.existsInStorage(userId)) {
            throw new UserDoesNotExistException();
        }


        filmStorage.removeLike(filmId, userId);

    }

    public Film getFilmByID(Integer id) {
        if (!filmStorage.existsInStorage(id)) {
            log.error("Film with id={} does not exist", id);
            throw new FilmDoesNotExistException();
        }
        log.info("Getting film with id={}", id);
        Film film = filmStorage.getFilmByID(id);

        return film;
    }

    public List<Film> getMostPopularFilms(String count) {
        log.info("getMostPopularFilmsList() method has been called");
        log.info("Count query param is {}", count);
        Integer countAsInt = count == null || count.isEmpty() || count.equals("")
                ? 10 : Integer.parseInt(count);
        log.info("Count value is {}", countAsInt);

        List<Film> popularFilms = filmStorage.getMostPopularFilms(countAsInt);

        return popularFilms;
    }
}
