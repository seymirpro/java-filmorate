package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class User {
    //private static final AtomicInteger currentId = new AtomicInteger();
    private int id; // = currentId.incrementAndGet();
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
