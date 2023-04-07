package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class User {
    //private static final AtomicInteger currentId = new AtomicInteger();
    private int id; // = currentId.incrementAndGet();

    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
}
