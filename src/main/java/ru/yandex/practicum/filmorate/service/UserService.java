package ru.yandex.practicum.filmorate.service;

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

    public void addFriend(User user, User newFriend){
        Set<Integer> currentFriends = user.getFriends();
        currentFriends.add(newFriend.getId());
        Set<Integer> newFriendFriends = newFriend.getFriends();
        newFriendFriends.add(user.getId());
    }

    public void removeFriend(User user, User friend){
        Set<Integer> currentFriends = user.getFriends();
        currentFriends.remove(friend.getId());
        Set<Integer> newFriendFriends = friend.getFriends();
        newFriendFriends.remove(user.getId());
    }

    public Collection<User> getMutualFriends(User user, User friend){
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();
        Set<Integer> mutualFriends = new HashSet<>(userFriends);
        mutualFriends.retainAll(friendFriends);
        Collection<User> users = userStorage.getUsers().stream().filter(u -> mutualFriends.contains(user.getId()))
                .collect(Collectors.toList());
        return users;
    }

    public List<User> getUserFriends(Integer id){
        User u = getUserByID(id);
        Set<Integer> friends = u.getFriends();
        List<User> userFriends = friends.stream()
                .map(i -> userStorage.getUserByID(i)).collect(Collectors.toList());
        return userFriends;
    }

    public User getUserByID(Integer id){
        if (!userStorage.existsInStorage(id)){
            throw new UserDoesNotExist();
        }

        return userStorage.getUserByID(id);
    }
}
