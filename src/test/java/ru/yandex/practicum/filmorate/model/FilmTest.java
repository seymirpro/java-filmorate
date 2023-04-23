package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldContainNonEmptyName() {
        @Valid Film film = new Film(1, "", "description", LocalDate.now(), 20, Set.of());
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldContainDescriptionLengthLessOrEqual200() {
        @Valid Film film = new Film(1, "The gentlemen", "descriptiondescriptiondescript" +
                "iondescriptiondescriptionde" +
                "scriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescripti" +
                "ondescriptiondescript" +
                "iondescriptiondescriptiondescriptiondescriptiondescriptiondescriptio" +
                "ndescriptiondescript" +
                "iondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                "descriptiondescription" +
                "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescr" +
                "iptiondescriptiondescriptiondescriptiondescriptiondescriptionde" +
                "scriptiondescriptiondescription", LocalDate.now(), 20, Set.of());
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        violations.stream().forEach(System.out::println);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotHaveReleaseDateBefore18951228() {
        @Valid Film film = new Film(1, "The Gentlemen", "description", LocalDate.of(1700, 1, 1),
                20, Set.of());
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldHavePositiveDuration() {
        @Valid Film film = new Film(1, "The Gentlemen", "description", LocalDate.of(1900, 1, 1)
                , -19, Set.of());
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}