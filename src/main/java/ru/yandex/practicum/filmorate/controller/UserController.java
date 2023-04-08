package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
        Collection<User> res = users.values();
        return res;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now())
                || users.get(user.getId()) != null
        ) {
            log.error("Invalid user params", ValidationException.class);
            throw new ValidationException();
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }


        user.setId(++id);
        users.put(user.getId(), user);
        log.info("User's been added {}", users.keySet());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

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
