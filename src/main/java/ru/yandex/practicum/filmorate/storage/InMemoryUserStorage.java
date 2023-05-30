package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users;
    private static Integer id = 0;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> allUsers = users.values();
        return allUsers;
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean existsInStorage(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserByID(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> getFriends(Integer id) {
        return List.of();
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {

    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {

    }

    @Override
    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        return List.of();
    }
}
