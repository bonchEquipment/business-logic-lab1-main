package ru.buisnesslogiclab1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.buisnesslogiclab1.entity.SubscriptionTypeEntity;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, String> {
}
