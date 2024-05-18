package ru.buisnesslogiclab1.validation.admin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import ru.buisnesslogiclab1.validation.video.VideoIdValidator;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdminIdValidator.class)
public @interface ValidAdminId {
    String message() default "ADMIN_ID must be a valid UUID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}