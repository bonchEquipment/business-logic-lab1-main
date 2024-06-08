package ru.buisnesslogiclab1.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buisnesslogiclab1.entity.UserSubscriptionEntity;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionEntity, UUID> {
}
