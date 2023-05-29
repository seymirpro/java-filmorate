package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExist;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        if (user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now())
        ) {
            throw new ValidationException();
        }


        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User newUserInDB = null;
        try {
            newUserInDB = userStorage.createUser(user);
            log.info("User's been added {}", user);
        } catch (ValidationException e) {
            log.error("Invalid user params", ValidationException.class);
        }

        return newUserInDB;
    }

    public User updateUser(User user) {
        try {
            System.out.println(userStorage.existsInStorage(user.getId()));
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        if (!userStorage.existsInStorage(user.getId())) {
            throw new UserDoesNotExist();
        }

        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        try {
            userStorage.updateUser(user);
        } catch (Exception ex){
            log.error("User details => {}", user);
            throw new ValidationException();
        }

        return user;
    }

    public void addFriend(Integer userId, Integer newFriendId) {
        if (!userStorage.existsInStorage(userId)) {
            log.info("User with id = {} does not exist", userId);
            throw new UserDoesNotExist();
        }

        if (!userStorage.existsInStorage(newFriendId)) {
            log.info("User with id = {} does not exist", newFriendId);
            throw new UserDoesNotExist();
        }

        try {
            userStorage.addFriend(userId, newFriendId);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (!userStorage.existsInStorage(userId)){
            throw new UserDoesNotExist();
        }

        if (!userStorage.existsInStorage(friendId)){
            throw new UserDoesNotExist();
        }

        try {
            userStorage.removeFriend(userId, friendId);
            log.info("User with id={} removed friend with id={}", userId, friendId);
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public Collection<User> getMutualFriends(Integer userId, Integer friendId) {
        log.info("Getting list of mutual friends...");
        Optional<List<User>> mutualFriends = null;
        try {
            mutualFriends = userStorage.getMutualFriends(userId, friendId);

        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return mutualFriends.get();
    }

    public List<User> getUserFriends(Integer id) {
        log.info("Getting list of User's friends...");
        if (!userStorage.existsInStorage(id)){
            throw new UserDoesNotExist();
        }
        Optional<List<User>> friends = null;
        try {
            friends = userStorage.getFriends(id);
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        return friends.get();
    }

    public User getUserByID(Integer id) {

        if (!userStorage.existsInStorage(id)) {
            throw new UserDoesNotExist();
        }
        log.info("Getting user details...");
        return userStorage.getUserByID(id);
    }
}
