package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.config.Config;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void nameExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmGenerator("", "Описание1", LocalDate.of(2020, 12, 1), 100)));
        assertEquals("название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void descriptionExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmGenerator("name", generateDescription() + "1", Config.MIN_DATE_RELEASE.plusYears(10), 100)));
        assertEquals("максимальная длина описания " + Config.MAX_LENGTH_DESCRIPTION + " символов", exception.getMessage());
    }

    @Test
    void descriptionIsNullExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmGenerator("name", "", Config.MIN_DATE_RELEASE.plusYears(10), 100)));
        assertEquals("не заполнено описание", exception.getMessage());
    }

    @Test
    void releaseDateExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmGenerator("name", generateDescription(), Config.MIN_DATE_RELEASE.minusDays(1), 100)));
        assertEquals("дата релиза не может быть раньше " + Config.MIN_DATE_RELEASE, exception.getMessage());
    }

    @Test
    void durationExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(filmGenerator("name", generateDescription(), Config.MIN_DATE_RELEASE, 0)));
        assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    void putFilmTest() {
        Film film = filmGenerator("name", generateDescription(), Config.MIN_DATE_RELEASE, 1);
        filmController.putFilm(film);
        assertEquals(film.getName(), "name");
        assertEquals(film.getId(), 1);
    }

    public String generateDescription() {
        return "a".repeat(Config.MAX_LENGTH_DESCRIPTION);
    }

    private Film filmGenerator(String name, String description, LocalDate date, int duration) {
        Film film = new Film();
        film.setId(1L);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(date);
        film.setDuration(duration);
        return film;
    }
}
