package ru.buisnesslogiclab1.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VideoIdSubscriberIdPair.class)
@Entity(name = "likee")
public class LikeEntity {

    @Id
    @Column(name = "subscriber_id")
    private UUID subscriberId;

    @Id
    @Column(name = "video_id")
    private UUID videoId;

}
