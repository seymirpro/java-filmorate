package ru.yandex.practicum.filmorate.model;


import lombok.*;
import ru.yandex.practicum.filmorate.utils.validation.constraints.MinReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
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
    private Integer duration;

    private RatingMpa mpa;

    private LocalDateTime createdAt;

    private List<Genre> genres;
}
