package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        if (user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now())
                || userStorage.existsInStorage(user.getId())
        ) {
            throw new ValidationException();
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        userStorage.createUser(user);
        return user;
    }

    public User updateUser(User user){
        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (userStorage.existsInStorage(user.getId())) {
            userStorage.updateUser(user);
        } else {
            throw new ValidationException();
        }

        return user;
    }
}
