package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.validation.constraints.MinReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    private int ratingId;

    @PastOrPresent
    @MinReleaseDate
    private LocalDate releaseDate;

    @PositiveOrZero
    private double duration;

    private LocalDateTime createdAt;

    @Getter(lazy = true)
    private final Set<Integer> userLikes = new HashSet<>();
}
