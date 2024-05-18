package ru.buisnesslogiclab1.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;



@Component
@RequiredArgsConstructor
public class ResponseHelper {

    public <T> ResponseEntity<Response<T>> asResponseEntity(StatusCode StatusCode) {
        return asResponseEntity(StatusCode, null);
    }

    public <T> ResponseEntity<Response<T>> asResponseEntity(StatusCode statusCode, @Nullable T data) {
        var response = asResponse(statusCode, data);
        return ResponseEntity.status(statusCode.getHttpCode()).body(response);
    }

    public <T> Response<T> asResponse(StatusCode statusCode, @Nullable T data) {
        return new Response<>(data, statusCode);
    }

}
