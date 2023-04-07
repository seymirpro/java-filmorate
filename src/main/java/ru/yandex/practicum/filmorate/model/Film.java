package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    //private static final AtomicInteger currentId = new AtomicInteger();
    private int id;// = currentId.incrementAndGet();
    @NonNull
    @NotBlank
    private String name;

    @Size(max = 200)
    @NotBlank
    private String description;

    private LocalDate releaseDate;

    @PositiveOrZero
    private double duration;
}
