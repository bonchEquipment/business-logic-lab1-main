package ru.buisnesslogiclab1.controller;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.util.ResponseHelper;


/**
 * Перехватчик неудачных ответов
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseControllerAdvice {

    private final ResponseHelper responseHelper;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException ex) {
        var message = listToString(ex.getConstraintViolations());
        log.error(message, ex);

        return responseHelper.asResponseEntity(StatusCode.createConstraintViolationCode(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        log.error("Service general Exception thrown", ex);

        return responseHelper.asResponseEntity(StatusCode.INTERNAL_ERROR);
    }


    private String listToString(Set<ConstraintViolation<?>> set){
        if (set.size() == 1)
            return set.stream().toList().get(0).getMessageTemplate();

        if (set.size() > 1){
            var stringBuilder = new StringBuilder();
            for (var violation: set.stream().toList())
                stringBuilder.append(violation.getMessageTemplate() + "; ");
            return stringBuilder.toString();
        }

        return "Some constraint violation happened";
    }



}
