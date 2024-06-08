package ru.buisnesslogiclab1.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.entity.BankAccountEntity;
import ru.buisnesslogiclab1.entity.RutubeAccountEntity;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {

    Optional<BankAccountEntity> findByUserId(UUID id);

  /*  @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE bank_account " +
                    "SET value = value + :amount " +
                    "WHERE user_id = :userId")
    void addMoneyToAccount(BigDecimal amount, UUID userId);*/

}
