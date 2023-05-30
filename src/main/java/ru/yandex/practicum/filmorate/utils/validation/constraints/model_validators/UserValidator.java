package ru.yandex.practicum.filmorate.utils.validation.constraints.model_validators;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {
    public static boolean validate(User user) {
        return user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now());
    }
}
