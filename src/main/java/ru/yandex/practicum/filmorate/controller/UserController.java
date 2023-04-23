package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id){
        return userService.getUserByID(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        userService.addFriend(userService.getUserByID(id), userService.getUserByID(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        userService.removeFriend(userService.getUserByID(id), userService.getUserByID(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id){
        return userService.getUserFriends(id);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        try {
            userService.createUser(user);
            log.info("User's been added {}", user);
        } catch (ValidationException e) {
            log.error("Invalid user params", ValidationException.class);
        }
        return user;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        return userService.getMutualFriends(userService.getUserByID(id), userService.getUserByID(otherId));
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        try {
            userService.updateUser(user);
        } catch (ValidationException e) {
            log.error("User details => {}", user);
        }
        return user;
    }
}
