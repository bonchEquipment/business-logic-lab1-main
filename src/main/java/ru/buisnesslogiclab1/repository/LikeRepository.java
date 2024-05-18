package ru.buisnesslogiclab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.LikeEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

public interface LikeRepository extends JpaRepository<LikeEntity, VideoIdSubscriberIdPair> {
}
