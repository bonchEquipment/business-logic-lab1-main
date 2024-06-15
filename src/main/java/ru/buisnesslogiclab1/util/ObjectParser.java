package ru.buisnesslogiclab1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ObjectParser {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String parse(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn(e.toString(), e);
            return e.toString();
        }
    }

    public static <T> T readValue(String s, Class<T> tClass) throws JsonProcessingException {
        return objectMapper.readValue(s, tClass);
    }


}
