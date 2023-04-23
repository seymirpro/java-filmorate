package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable Integer id){
        return filmService.getFilmByID(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId){
        filmService.addLike(filmService.getFilmByID(id), userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId){
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getMostPopularFilmsList(){
        return null;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            filmService.addFilm(film);
            log.info("Film added");
        } catch (ValidationException ex){
            log.error(ex.getLocalizedMessage());
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            filmService.updateFilm(film);
            log.info("Film updated");
        } catch (ValidationException ex){
            log.error("Film update operation invalid", ValidationException.class);
        }
        return film;
    }
}
