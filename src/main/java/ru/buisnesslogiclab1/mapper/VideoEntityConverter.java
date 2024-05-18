package ru.buisnesslogiclab1.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.buisnesslogiclab1.entity.VideoEntity;

/**
 * Конвертер для преобразования из строки в UserHeader
 */
@Slf4j
@Component
@AllArgsConstructor
public class VideoEntityConverter implements Converter<String, VideoEntity> {

    private final ObjectMapper objectMapper;

    @Override
    public VideoEntity convert(@NonNull String source) {
        try {
            return objectMapper.readValue(source, VideoEntity.class);
        } catch (Exception e) {
            log.warn("Cannot parse videoEntity header from value = {}", source);
            throw new RuntimeException();
        }
    }
}
