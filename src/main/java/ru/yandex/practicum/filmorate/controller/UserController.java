package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (validateUser(user)) {
            if (user.getName() == null || user.getName().isEmpty())
                user.setName(user.getLogin());
            users.put(user.getEmail(), user);
        }
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        if (validateUser(user)) {
            if (user.getName() == null || user.getName().isEmpty())
                user.setName(user.getLogin());
            users.put(user.getEmail(), user);
        }
        log.info("Изменён пользователь {}", user);
        return user;
    }

    private boolean validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("невалидная почта");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("невалидный логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения больше текущей {}", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        return true;
    }
}
