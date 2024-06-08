package ru.buisnesslogiclab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buisnesslogiclab1.entity.LikeEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, VideoIdSubscriberIdPair> {
}
