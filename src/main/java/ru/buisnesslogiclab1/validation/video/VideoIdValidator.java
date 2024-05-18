package ru.buisnesslogiclab1.validation.video;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.validation.video.ValidVideoId;

@Slf4j
@Service
public class VideoIdValidator implements ConstraintValidator<ValidVideoId, String> {
    @Override
    public void initialize(ValidVideoId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
            return true;
        } catch (Exception e){
            log.warn(e.toString(), e);
            return false;
        }
    }
}
