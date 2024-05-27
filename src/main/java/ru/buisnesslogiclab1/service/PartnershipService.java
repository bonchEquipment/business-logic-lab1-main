package ru.buisnesslogiclab1.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.OperationStatusDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnershipService {

    private final AccountService accountService;

    @Transactional(rollbackFor = Exception.class)
    public OperationStatusDto withdrawSubscriptionMoney(BigDecimal amount, UUID userId) {
        try {
            withdrawSubscriptionMoneyWrapper(amount, userId);
            return new OperationStatusDto(true, null);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return new OperationStatusDto(false, e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void withdrawSubscriptionMoneyWrapper(BigDecimal amount, UUID userId) throws Exception {
        var operationStatus = accountService.withdrawMoneyFromAccount(amount, userId, AccountType.RUTUBE);
        if (!operationStatus.isOperationSucceed())
            throw new Exception(operationStatus.getInfo());

        operationStatus = accountService.addMoneyToAccount(amount, userId, AccountType.BANK);
        if (!operationStatus.isOperationSucceed())
            throw new Exception(operationStatus.getInfo());
    }
}
