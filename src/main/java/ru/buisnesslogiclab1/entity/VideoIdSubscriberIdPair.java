package ru.buisnesslogiclab1.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VideoIdSubscriberIdPair.class)
public class VideoIdSubscriberIdPair implements Serializable {

    @Column(name = "subscriber_id")
    private UUID subscriberId;


    @Column(name = "video_id")
    private UUID videoId;

}
