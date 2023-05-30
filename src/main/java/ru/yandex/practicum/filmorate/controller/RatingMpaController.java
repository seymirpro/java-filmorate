package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingMpaController {
    private RatingMpaService ratingMpaService;

    @Autowired
    public RatingMpaController(RatingMpaService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    @GetMapping("/{id}")
    public RatingMpa getRatingMpaByID(@PathVariable Integer id) {
        return ratingMpaService.getRatingMpaByID(id);
    }

    @GetMapping
    public List<RatingMpa> getAllRatingMpas() {
        return ratingMpaService.getAllRatingMpas();
    }
}
