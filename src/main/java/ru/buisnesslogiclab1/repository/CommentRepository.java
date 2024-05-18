package ru.buisnesslogiclab1.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.CommentEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

public interface CommentRepository extends JpaRepository<CommentEntity, VideoIdSubscriberIdPair> {

    List<CommentEntity> findByVideoId(UUID videoId);

}
