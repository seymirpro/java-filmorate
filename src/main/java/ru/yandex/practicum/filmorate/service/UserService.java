package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.validation.constraints.model_validators.UserValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        if (UserValidator.validate(user)) {
            throw new ValidationException();
        }


        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User newUserInDB = userStorage.createUser(user);
        log.info("User's been added {}", user);

        return newUserInDB;
    }

    public User updateUser(User user) {

        if (!userStorage.existsInStorage(user.getId())) {
            throw new UserDoesNotExistException();
        }

        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        Optional.ofNullable(userStorage.updateUser(user))
                .orElseThrow(() -> new ValidationException());
        return user;
    }

    public void addFriend(Integer userId, Integer newFriendId) {
        if (!userStorage.existsInStorage(userId)) {
            log.info("User with id = {} does not exist", userId);
            throw new UserDoesNotExistException();
        }

        if (!userStorage.existsInStorage(newFriendId)) {
            log.info("User with id = {} does not exist", newFriendId);
            throw new UserDoesNotExistException();
        }


        userStorage.addFriend(userId, newFriendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (!userStorage.existsInStorage(userId)) {
            throw new UserDoesNotExistException();
        }

        if (!userStorage.existsInStorage(friendId)) {
            throw new UserDoesNotExistException();
        }


        userStorage.removeFriend(userId, friendId);
        log.info("User with id={} removed friend with id={}", userId, friendId);

    }

    public Collection<User> getMutualFriends(Integer userId, Integer friendId) {
        log.info("Getting list of mutual friends...");
        List<User> mutualFriends = null;

        mutualFriends = userStorage.getMutualFriends(userId, friendId);

        return mutualFriends;
    }

    public List<User> getUserFriends(Integer id) {
        log.info("Getting list of User's friends...");
        if (!userStorage.existsInStorage(id)) {
            throw new UserDoesNotExistException();
        }
        List<User> friends = userStorage.getFriends(id);

        return friends;
    }

    public User getUserByID(Integer id) {

        if (!userStorage.existsInStorage(id)) {
            throw new UserDoesNotExistException();
        }
        log.info("Getting user details...");
        return userStorage.getUserByID(id);
    }
}
