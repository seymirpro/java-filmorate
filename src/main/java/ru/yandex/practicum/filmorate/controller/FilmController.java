package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private static int id;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {

        if (film.getName().isEmpty() || film.getName() == null || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || films.get(film.getId()) != null || film.getDuration() < 0 || films.get(film.getId()) != null) {
            throw new ValidationException();
        }

        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {

        if (film.getName() == null || film.getDescription().isEmpty() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
        || films.get(film.getId()) == null
        ) {
            throw new ValidationException();
        }

        films.put(film.getId(), film);
        return film;
    }
}
