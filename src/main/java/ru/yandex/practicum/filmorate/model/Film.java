package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class Film {
    //private static final AtomicInteger currentId = new AtomicInteger();
    private int id;// = currentId.incrementAndGet();
    private String name;
    private String description;
    private LocalDate releaseDate;
    private double duration;
}
