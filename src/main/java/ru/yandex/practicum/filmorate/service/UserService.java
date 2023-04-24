package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExist;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
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

        try {
            userStorage.createUser(user);
            log.info("User's been added {}", user);
        } catch (ValidationException e) {
            log.error("Invalid user params", ValidationException.class);
        }

        return user;
    }

    public User updateUser(User user) {
        if (!userStorage.existsInStorage(user.getId())) {
            throw new UserDoesNotExist();
        }

        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (userStorage.existsInStorage(user.getId())) {
            userStorage.updateUser(user);
        } else {
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
            User user = userStorage.getUserByID(userId);
            User newFriend = userStorage.getUserByID(newFriendId);
            log.info("user friend info {}", newFriend.toString());
            Set<Integer> currentFriends = user.getFriends();
            currentFriends.add(newFriend.getId());
            Set<Integer> newFriendFriends = newFriend.getFriends();
            newFriendFriends.add(user.getId());
            log.info("User with id={} added new friend", userId);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> currentFriends = user.getFriends();
        currentFriends.remove(friend.getId());
        Set<Integer> newFriendFriends = friend.getFriends();
        newFriendFriends.remove(user.getId());
        log.info("User with id={} removed friend with id={}", userId, friendId);
    }

    public Collection<User> getMutualFriends(Integer userId, Integer friendId) {
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        Set<Integer> mutualFriends = new HashSet<>(userFriends);
        mutualFriends.retainAll(friendFriends);
        Collection<User> users = mutualFriends.stream()
                .map(uID -> userStorage.getUserByID(uID))
                .collect(Collectors.toList());
        log.info("Getting list of mutual friends...");
        return users;
    }

    public List<User> getUserFriends(Integer id) {
        User u = getUserByID(id);
        Set<Integer> friends = u.getFriends();
        List<User> userFriends = friends.stream()
                .map(i -> userStorage.getUserByID(i)).collect(Collectors.toList());
        log.info("Getting list of User's friends...");
        return userFriends;
    }

    public User getUserByID(Integer id) {
        if (!userStorage.existsInStorage(id)) {
            throw new UserDoesNotExist();
        }
        log.info("Getting user details...");
        return userStorage.getUserByID(id);
    }
}
