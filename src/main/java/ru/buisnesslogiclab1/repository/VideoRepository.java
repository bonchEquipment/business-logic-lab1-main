package ru.buisnesslogiclab1.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buisnesslogiclab1.entity.VideoEntity;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {
}
