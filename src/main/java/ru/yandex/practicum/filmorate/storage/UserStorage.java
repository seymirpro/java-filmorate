package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getUsers();

    User createUser(User user);

    boolean existsInStorage(Integer id);

    User updateUser(User user);

    User getUserByID(Integer id);

    List<User> getFriends(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getMutualFriends(Integer userId, Integer friendId);
}
