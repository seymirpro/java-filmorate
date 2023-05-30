package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldContainNonEmptyName() {
        @Valid Film film = Film.builder()
                .id(1)
                .name("")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(20)
                .createdAt(LocalDateTime.now())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldContainDescriptionLengthLessOrEqual200() {
        @Valid Film film = Film.builder()
                .id(1)
                .name("The gentlemen")
                .description("descriptiondescriptiondescript" +
                        "iondescriptiondescriptionde" +
                        "scriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescripti" +
                        "ondescriptiondescript" +
                        "iondescriptiondescriptiondescriptiondescriptiondescriptiondescriptio" +
                        "ndescriptiondescript" +
                        "iondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescr" +
                        "iptiondescriptiondescriptiondescriptiondescriptiondescriptionde" +
                        "scriptiondescriptiondescription")
                .releaseDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .duration(2)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.stream().forEach(System.out::println);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotHaveReleaseDateBefore18951228() {
        @Valid Film film = Film.builder()
                .id(1)
                .name("The Gentlemen")
                .description("description")
                .releaseDate(LocalDate.of(1700, 1, 1))
                .duration(2)
                .createdAt(LocalDateTime.now())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldHavePositiveDuration() {
        @Valid Film film = Film.builder()
                .id(1)
                .name("The Gentlemen")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .mpa(RatingMpa.builder().build())
                .createdAt(LocalDateTime.now())
                .genres(new ArrayList<Genre>())
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}