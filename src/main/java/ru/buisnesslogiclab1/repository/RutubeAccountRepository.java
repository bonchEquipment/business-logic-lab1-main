package ru.buisnesslogiclab1.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.entity.RutubeAccountEntity;

@Repository
public interface RutubeAccountRepository extends JpaRepository<RutubeAccountEntity, UUID> {


    Optional<RutubeAccountEntity> findByUserId(UUID id);

  /*  @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE rutube_account " +
                    "SET value = value + :amount " +
                    "WHERE user_id = :userId")
    void addMoneyToAccount(BigDecimal amount, UUID userId);*/

    /*@Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE rutube_account " +
                    "SET value = value - :amount " +
                    "WHERE user_id = :userId")
    void withdrawMoneyFromAccount(BigDecimal amount, UUID userId);*/




}
