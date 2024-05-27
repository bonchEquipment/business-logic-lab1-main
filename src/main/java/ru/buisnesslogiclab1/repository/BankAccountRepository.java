package ru.buisnesslogiclab1.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.buisnesslogiclab1.entity.BankAccountEntity;
import ru.buisnesslogiclab1.entity.RutubeAccountEntity;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {

    Optional<BankAccountEntity> findByUserId(UUID id);

    @Query(nativeQuery = true,
            value = "UPDATE bank_account " +
                    "SET value = value + :amount " +
                    "WHERE user_id = :userId")
    void addMoneyToAccount(BigDecimal amount, UUID userId);

}
