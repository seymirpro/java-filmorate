package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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
