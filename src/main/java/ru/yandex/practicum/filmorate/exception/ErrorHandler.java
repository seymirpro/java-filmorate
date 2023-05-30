package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.GenresController;
import ru.yandex.practicum.filmorate.controller.RatingMpaController;
import ru.yandex.practicum.filmorate.controller.UserController;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class,
        RatingMpaController.class, GenresController.class})
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse(
                String.format("Ошибка с валидацией \"%s\".", e.getLocalizedMessage())
        );
    }

    @ExceptionHandler({
            UserDoesNotExist.class,
            FilmDoesNotExist.class,
            GenreDoesNotExist.class,
            RatingMpaDoesNotExist.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFound(final RuntimeException e) {
        return new ErrorResponse(
                String.format("Не найден ресурс \"%s\".", e.getLocalizedMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralExceptions(final Throwable t) {
        return new ErrorResponse(
                String.format("Ошибка сервера \"%s\".", t.getLocalizedMessage())
        );
    }
}
