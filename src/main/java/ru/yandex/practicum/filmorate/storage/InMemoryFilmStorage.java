package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private static int id;
    private Map<Integer, Film> films;

    public InMemoryFilmStorage(){
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
        Collection<Film> films = this.films.values();
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean existsInStorage(Film film) {
        return films.get(film.getId()) != null;
    }
}
