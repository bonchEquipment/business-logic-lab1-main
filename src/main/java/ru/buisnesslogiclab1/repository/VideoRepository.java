package ru.buisnesslogiclab1.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {
}
