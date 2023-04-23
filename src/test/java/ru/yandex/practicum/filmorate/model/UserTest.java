package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldHaveValidEmail() {
        @Valid User user = new User(1, "aa","yandex", "Sevinc", LocalDate.of(1995, 2, 2), Set.of());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        @Valid User userValid = new User(1, "a@yandex.ru","yandex", "Sevinc", LocalDate.of(1995, 2, 2), Set.of());
        Set<ConstraintViolation<User>> violationsAll = validator.validate(userValid);
        assertTrue(violationsAll.isEmpty());
    }

    @Test
    public void shouldNotContainWhitespaceInLogin() {
        @Valid User userValid = new User(1, "a@yandex.ru","yandex     ", "Sevinc",
                LocalDate.of(1995, 2, 2), Set.of());
        Set<ConstraintViolation<User>> violationsAll = validator.validate(userValid);
        assertTrue(violationsAll.isEmpty());
    }


    @Test
    public void shouldHaveValidBirthday() {
        @Valid User userValid = new User(1, "a@yandex.ru","yandex     ", "Sevinc",
                LocalDate.of(2078, 2, 2), Set.of());
        Set<ConstraintViolation<User>> violationsAll = validator.validate(userValid);
        assertFalse(violationsAll.isEmpty());
    }

    @Test
    public void shouldUseLoginIfNameIsEmpty() {

    }
}