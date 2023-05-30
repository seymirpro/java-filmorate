package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[^\\s]+$")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @Getter(lazy = true)
    private final Set<Integer> friends = new HashSet<>();
}
