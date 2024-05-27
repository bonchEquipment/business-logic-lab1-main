package ru.buisnesslogiclab1.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.UserSubscriptionEntity;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, UUID> {
}
