package ru.buisnesslogiclab1.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.buisnesslogiclab1.entity.RutubeAccountEntity;

public interface RutubeAccountRepository extends JpaRepository<RutubeAccountEntity, UUID> {

    Optional<RutubeAccountEntity> findByUserId(UUID id);

    @Query(nativeQuery = true,
            value = "UPDATE rutube_account " +
                    "SET value = value + :amount " +
                    "WHERE user_id = :userId")
    void addMoneyToAccount(BigDecimal amount, UUID userId);

    @Query(nativeQuery = true,
            value = "UPDATE rutube_account " +
                    "SET value = value - :amount " +
                    "WHERE user_id = :userId")
    void withdrawMoneyFromAccount(BigDecimal amount, UUID userId);




}
