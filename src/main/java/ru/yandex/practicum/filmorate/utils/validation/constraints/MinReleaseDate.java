package ru.yandex.practicum.filmorate.utils.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinReleaseDateValidator.class)
@Documented

public @interface MinReleaseDate {
    String message() default "must be a valid release date." +
            " found: ${validatedValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
