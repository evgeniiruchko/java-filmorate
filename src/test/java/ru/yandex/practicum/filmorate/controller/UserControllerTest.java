package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void emailNullExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(userGenerator(1L, "", "login", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void emailNoValidExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(userGenerator(1L, "email", "login", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void loginNullExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(userGenerator(1L, "email@email.ru", "", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void loginNoValidExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(userGenerator(1L, "email@email.ru", "my login", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void birthDayExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(userGenerator(1L, "email@email.ru", "login", "name", LocalDate.now().plusDays(1))));
        assertEquals("дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void addUserTest() {
        User user = userGenerator(1L, "emailemail.ru@", "login", "", LocalDate.now());
        userController.putUser(user);
        assertEquals(user.getName(), user.getLogin());
        assertEquals(user.getId(), 1L);
    }

    private User userGenerator(Long id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}
