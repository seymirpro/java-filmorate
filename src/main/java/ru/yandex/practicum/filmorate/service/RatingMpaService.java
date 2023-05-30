package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RatingMpaDoesNotExistException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaDAO;

import java.util.List;

@Service
@Slf4j
public class RatingMpaService {
    private RatingMpaDAO ratingMpaDAO;

    public RatingMpaService(RatingMpaDAO ratingMpaDAO) {
        this.ratingMpaDAO = ratingMpaDAO;
    }

    public RatingMpa getRatingMpaByID(Integer id) {
        if (!existsInStorage(id)) {
            throw new RatingMpaDoesNotExistException();
        }
        return ratingMpaDAO.getRatingMpaByID(id);
    }

    public List<RatingMpa> getAllRatingMpas() {
        return ratingMpaDAO.getAllRatingMpas();
    }

    public boolean existsInStorage(Integer id) {
        return ratingMpaDAO.existsInStorage(id);
    }
}
