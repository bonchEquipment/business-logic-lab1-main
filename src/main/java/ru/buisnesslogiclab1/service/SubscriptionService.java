package ru.buisnesslogiclab1.service;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.OperationStatusDto;
import ru.buisnesslogiclab1.repository.SubscriptionTypeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final AccountService accountService;
    private final SubscriptionTypeRepository subscriptionTypeRepository;


    public OperationStatusDto addSubscription(UUID userId, String subscriptionType){
        try {
            addSubscriptionWrapper(userId, subscriptionType);
            return new OperationStatusDto(true, null);
        } catch (Exception e){
            log.warn(e.getMessage(), e);
            return new OperationStatusDto(false, e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    protected void addSubscriptionWrapper(UUID userId, String subscriptionType) throws Exception {
        var subscriptionTypeOptional = subscriptionTypeRepository.findById(subscriptionType);
        if (subscriptionTypeOptional.isEmpty())
            throw new Exception("Не существует подписки с названием " + subscriptionType);

        var subscriptionTypeEntity = subscriptionTypeOptional.get();
        var operationStatusDto = accountService.withdrawMoneyFromAccount(BigDecimal.valueOf(subscriptionTypeEntity.getMonthlyPayRub()),
                userId, AccountType.RUTUBE);

        if (!operationStatusDto.isOperationSucceed())
            throw new Exception(operationStatusDto.getInfo());
    }

}
