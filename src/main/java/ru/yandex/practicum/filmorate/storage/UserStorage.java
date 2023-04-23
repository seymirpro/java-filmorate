package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers();

    User createUser(User user);

    boolean existsInStorage(Integer id);

    User updateUser(User user);

    User getUserByID(Integer id);
}
