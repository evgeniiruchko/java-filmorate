package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.config.Config;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1, max = Config.MAX_LENGTH_DESCRIPTION)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
