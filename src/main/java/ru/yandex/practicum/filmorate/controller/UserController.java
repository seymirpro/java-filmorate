package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") ||
                user.getLogin().contains(" ") || user.getLogin().isEmpty() ||
                user.getBirthday().isAfter(LocalDate.now())
        ) {
            log.error("Invalid user params", ValidationException.class);
            throw new ValidationException();
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (users.get(user.getId()) == null){
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("User's been added {}", users.keySet());
        } else {
            throw new ValidationException();
        }

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") ||
                user.getLogin().contains(" ") || user.getLogin().isEmpty() ||
                user.getBirthday().isAfter(LocalDate.now())
        ) {
            log.error("Invalid user params", ValidationException.class);
            throw new ValidationException();
        }

        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (users.get(user.getId()) != null) {

            users.put(user.getId(), user);
        } else {
            log.error("User details => {}", user);
            log.error("Current map state => {}", users.keySet());
            throw new ValidationException();
        }

        return user;
    }
}
