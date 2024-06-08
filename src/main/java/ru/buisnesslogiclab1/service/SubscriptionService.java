package ru.buisnesslogiclab1.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.OperationStatusDto;
import ru.buisnesslogiclab1.entity.UserSubscriptionEntity;
import ru.buisnesslogiclab1.repository.SubscriptionTypeRepository;
import ru.buisnesslogiclab1.repository.UserSubscriptionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final AccountService accountService;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional(rollbackFor = Exception.class)
    public void addSubscription(UUID userId, String subscriptionName) throws Exception {
        var subscriptionTypeOptional = subscriptionTypeRepository.findById(subscriptionName);
        if (subscriptionTypeOptional.isEmpty())
            throw new Exception("Не существует подписки с названием " + subscriptionName);

        var subscriptionTypeEntity = subscriptionTypeOptional.get();
        var operationStatusDto = accountService.withdrawMoneyFromAccount(BigDecimal.valueOf(subscriptionTypeEntity.getMonthlyPayRub()),
                userId, AccountType.RUTUBE);

        if (!operationStatusDto.isOperationSucceed())
            throw new Exception(operationStatusDto.getInfo());

        var userSubscriptionEntity = UserSubscriptionEntity.builder()
                .subscriptionName(subscriptionName)
                .userId(userId)
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .build();

        userSubscriptionRepository.save(userSubscriptionEntity);
    }

}
