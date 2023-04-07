package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    public Film addFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || films.get(film.getId()) != null || films.get(film.getId()) != null) {
            log.error("Film params invalid", ValidationException.class);
            throw new ValidationException();
        }

        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Film added");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || films.get(film.getId()) == null
        ) {
            log.error("Film update operation invalid", ValidationException.class);
            throw new ValidationException();
        }

        log.info("Film updated");
        films.put(film.getId(), film);
        return film;
    }
}
