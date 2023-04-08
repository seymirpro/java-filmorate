package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.validation.constraints.MinReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NonNull
    @NotBlank
    private String name;

    @Size(max = 200)
    @NotBlank
    private String description;


    @PastOrPresent
    @MinReleaseDate
    private LocalDate releaseDate;

    @PositiveOrZero
    private double duration;
}
