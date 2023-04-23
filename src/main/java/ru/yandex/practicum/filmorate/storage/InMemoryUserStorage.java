package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
    private Map<Integer, User> users;
    private static Integer id = 0;

    public InMemoryUserStorage(){
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
        return users.get(id) != null;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return null;
    }

    @Override
    public User getUserByID(Integer id){
        return users.get(id);
    }
}
