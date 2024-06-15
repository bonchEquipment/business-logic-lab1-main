package ru.buisnesslogiclab1.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.OperationStatusDto;
import ru.buisnesslogiclab1.repository.BankAccountRepository;
import ru.buisnesslogiclab1.repository.RutubeAccountRepository;

import static ru.buisnesslogiclab1.dto.AccountType.RUTUBE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {


    private final RutubeAccountRepository rutubeAccountRepository;
    private final BankAccountRepository bankAccountRepository;

    public OperationStatusDto addMoneyToAccount(BigDecimal amount, UUID userId, AccountType accountType) {
        if (lessThanZero(amount) || equalToZero(amount))
            return createFailedResponse("amount to add cannot be less or equal to 0");
        if (accountType.equals(RUTUBE))
            return changeRutubeBalanceBy(amount, userId);

        return changeBankBalanceBy(amount, userId);
    }

    public OperationStatusDto withdrawMoneyFromAccount(BigDecimal amount, UUID userId, AccountType accountType) {
        if (lessThanZero(amount) || equalToZero(amount))
            return createFailedResponse("amount to withdraw cannot be less or equal to 0");
        if (accountType.equals(RUTUBE))
            return changeRutubeBalanceBy(amount.negate(), userId);

        return changeBankBalanceBy(amount.negate(), userId);
    }


    private OperationStatusDto changeBankBalanceBy(BigDecimal amount, UUID userId) {
        var accountOptional = bankAccountRepository.findByUserId(userId);
        if (accountOptional.isEmpty())
            return createFailedResponse("there is no account with user_id " + userId);

        var account = accountOptional.get();

        if (lessThanZero(amount) &&
                lessThanZero(account.getValue().add(amount)))
            return createFailedResponse("amount to withdraw cannot be more than current account value");


        try {
            var bankAccount = bankAccountRepository.findByUserId(userId).get();
            bankAccount.setValue(bankAccount.getValue().add(amount));
            bankAccountRepository.save(bankAccount);
            log.info("log after saving bankAccount {}", bankAccount);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return createFailedResponse("exception during attempt to add or withdraw money " +
                    e.getMessage());
        }

        return new OperationStatusDto(true, null);
    }



    private OperationStatusDto changeRutubeBalanceBy(BigDecimal amount, UUID userId) {
        var accountOptional = rutubeAccountRepository.findByUserId(userId);
        if (accountOptional.isEmpty())
            return createFailedResponse("there is no account with user_id " + userId);

        var account = accountOptional.get();

        if (lessThanZero(amount) &&
                lessThanZero(account.getValue().add(amount)))
            return createFailedResponse("amount to withdraw cannot be more than current account value");


        try {
            var rutubeAccount = rutubeAccountRepository.findByUserId(userId).get();
            rutubeAccount.setValue(rutubeAccount.getValue().add(amount));
            rutubeAccountRepository.save(rutubeAccount);
            log.info("log after saving rututbeAccount {}", rutubeAccount);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return createFailedResponse("exception during attempt to add or withdraw money " +
                    e.getMessage());
        }

        return new OperationStatusDto(true, null);
    }



    private OperationStatusDto createFailedResponse(String info) {
        return new OperationStatusDto(false, info);
    }

    private boolean biggerThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean lessThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean equalToZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }


}
