package ru.buisnesslogiclab1.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.entity.CommentEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, VideoIdSubscriberIdPair> {

    List<CommentEntity> findByVideoId(UUID videoId);

}
