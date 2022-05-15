package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.config.Config;
import ru.yandex.practicum.filmorate.exceprion.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (validateFilm(film))
            films.put(film.getId(), film);
        log.info("добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (validateFilm(film))
            films.put(film.getId(), film);
        log.info("Изменён фильм {}", film);
        return film;
    }

    private boolean validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("невалидное название");
            throw new ValidationException("название фильма не может быть пустым");
        }
        if (film.getDescription().length() > Config.MAX_LENGTH_DESCRIPTION) {
            log.warn("Слишком длинное описание");
            throw new ValidationException("максимальная длина описания " + Config.MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getDescription().isEmpty() || film.getDescription() == null) {
            log.warn("Отсутствует описание");
            throw new ValidationException("не заполнено описание");
        }
        if (film.getReleaseDate().isBefore(Config.MIN_DATE_RELEASE)) {
            log.warn("дата релиза меньше {}", Config.MIN_DATE_RELEASE);
            throw new ValidationException("дата релиза не может быть раньше " + Config.MIN_DATE_RELEASE);
        }
        if (film.getDuration() <= 0) {
            log.warn("продолжительность {}, должна быть положительной", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
