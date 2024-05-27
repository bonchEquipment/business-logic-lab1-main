package ru.buisnesslogiclab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.buisnesslogiclab1.entity.SubscriptionTypeEntity;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, String> {
}
