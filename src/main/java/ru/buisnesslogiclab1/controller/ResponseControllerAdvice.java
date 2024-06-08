package ru.buisnesslogiclab1.controller;


import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.service.UserService;
import ru.buisnesslogiclab1.util.ResponseHelper;


/**
 * Перехватчик неудачных ответов
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseControllerAdvice {

    private final ResponseHelper responseHelper;
    private final UserService userService;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException ex) {
        var message = listToString(ex.getConstraintViolations());
        log.error(message, ex);

        return responseHelper.asResponseEntity(StatusCode.createConstraintViolationCode(message));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handle(AccessDeniedException ex) {
        log.error("access denied");

        return responseHelper.asResponseEntity(StatusCode.FORBIDDEN);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<?> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        String errorMessage = "Request have unknown params";
        log.error(errorMessage);
        return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode(errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Service general MethodArgumentTypeMismatchException thrown";
        log.error(errorMessage);
        return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode(errorMessage));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        String errorMessage = "Service general MissingRequestHeaderException thrown";
        log.error(errorMessage);
        return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode(errorMessage));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMessage = "Service general MissingServletRequestParameterException thrown";

        return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode(errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        log.error("Service general Exception thrown", ex);

        return responseHelper.asResponseEntity(StatusCode.INTERNAL_ERROR);
    }


    private String listToString(Set<ConstraintViolation<?>> set) {
        if (set.size() == 1)
            return new ArrayList<>(set).get(0).getMessageTemplate();

        if (set.size() > 1) {
            var stringBuilder = new StringBuilder();
            for (var violation : new ArrayList<>(set))
                stringBuilder.append(violation.getMessageTemplate()).append("; ");
            return stringBuilder.toString();
        }

        return "Some constraint violation happened";
    }


}
