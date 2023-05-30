package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreDoesNotExist;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDAO;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private GenreDAO genreDAO;

    @Autowired
    public GenreService(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    public Genre getGenreByID(Integer id) {
        if (!existsInStorage(id)) {
            throw new GenreDoesNotExist();
        }
        return genreDAO.getGenreByID(id);
    }

    public boolean existsInStorage(Integer id) {
        return genreDAO.existsInStorage(id);
    }
}
