package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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
}
