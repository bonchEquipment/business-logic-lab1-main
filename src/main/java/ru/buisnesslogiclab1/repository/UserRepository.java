package ru.buisnesslogiclab1.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.entity.VideoIdSubscriberIdPair;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
