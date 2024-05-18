package ru.buisnesslogiclab1.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.buisnesslogiclab1.entity.VideoEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideosDto {

    private List<VideoEntity> videos;

}
