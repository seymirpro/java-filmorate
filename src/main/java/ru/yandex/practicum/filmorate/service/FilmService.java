package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExist;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExist;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addFilm(Film film) throws ValidationException {
        if (filmStorage.existsInStorage(film.getId())) {
            log.error("Film with id={} already exist", film.getId());
            throw new ValidationException();
        }

        try {
            filmStorage.addFilm(film);
            log.info("Film added");
        } catch (ValidationException ex) {
            log.error(ex.getLocalizedMessage());
            throw new ValidationException();
        }
    }

    public void updateFilm(Film film) {
        if (!filmStorage.existsInStorage(film.getId())) {
            throw new FilmDoesNotExist();
        }

        try {
            filmStorage.updateFilm(film);
            log.info("Film updated");
        } catch (ValidationException ex) {
            log.error("Film update operation invalid", ValidationException.class);
        }
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        log.info("Getting films...");
        return filmStorage.getFilms();
    }

    public void addLike(Integer id, Integer userId) {
        try {
            Film film = filmStorage.getFilmByID(id);
            film.getUserLikes().add(userId);
        } catch (FilmDoesNotExist exception) {
            throw new FilmDoesNotExist();
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
        }
    }

    public void removeLike(Integer filmId, Integer userId) {
        boolean isRemoved = filmStorage.getFilmByID(filmId).getUserLikes().remove(userId);
        if (!isRemoved) {
            throw new UserDoesNotExist();
        }
    }

    public Film getFilmByID(Integer id) {
        if (!filmStorage.existsInStorage(id)) {
            log.error("Film with id={} does not exist", id);
            throw new FilmDoesNotExist();
        }
        log.info("Getting film with id={}", id);
        return filmStorage.getFilmByID(id);
    }

    public List<Film> getMostPopularFilms(String count) {
        log.info("getMostPopularFilmsList() method has been called");
        log.info("Count query param is {}", count);
        Integer countAsInt = count == null || count.isEmpty() ? 10 : Integer.parseInt(count);

        List<Film> popularFilms = filmStorage.getFilms().stream()
                .sorted(Comparator.<Film>comparingInt(f -> f.getUserLikes().size())
                        .thenComparing(f -> f.getId()).reversed())
                .limit(countAsInt).collect(Collectors.toList());
        return popularFilms;
    }
}
