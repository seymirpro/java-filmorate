package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.validation.constraints.MinReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
