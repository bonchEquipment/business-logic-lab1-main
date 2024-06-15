package ru.buisnesslogiclab1.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.ChangeBalanceDto;
import ru.buisnesslogiclab1.dto.SendEmailDto;
import ru.buisnesslogiclab1.entity.UserSubscriptionEntity;
import ru.buisnesslogiclab1.feign.PaymentServiceFeign;
import ru.buisnesslogiclab1.repository.SubscriptionTypeRepository;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.UserSubscriptionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    private final PaymentServiceFeign feignClient;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional(rollbackFor = Exception.class)
    public void addSubscription(UUID userId, String subscriptionName) throws Exception {
        var subscriptionTypeOptional = subscriptionTypeRepository.findById(subscriptionName);
        if (subscriptionTypeOptional.isEmpty())
            throw new Exception("There is no subscription with a name " + subscriptionName);

        var subscriptionTypeEntity = subscriptionTypeOptional.get();
        var subscriptionPrice = BigDecimal.valueOf(subscriptionTypeEntity.getMonthlyPayRub());

        var response = feignClient.withdrawMoneyForSubscription(subscriptionPrice,
                userId, AccountType.RUTUBE);


        if (response.getStatusCodeValue() != 200)
            throw new Exception(response.getBody().getData().getDescription());

        var userSubscriptionEntity = UserSubscriptionEntity.builder()
                .subscriptionName(subscriptionName)
                .userId(userId)
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .build();

        userSubscriptionRepository.save(userSubscriptionEntity);
        sendCheckToUser(new ChangeBalanceDto(subscriptionPrice, userId), subscriptionName);
    }

    private void sendCheckToUser(ChangeBalanceDto dto, String subscriptionName) {
        var user = userRepository.findById(dto.getUserId()).get();
        var text = """
                Dear %s!
                You bought %s subscription for %s rubles. Thank you for that!
                Here is a bank check:
                inn: 1234556
                op_id: 12323
                ofd_id: 846123
                                
                Thank you for staying with us!
                                
                                
                Please do not reply to this email.
                If you have any questions contact arceniy.devyatkin@yandex.ru
                """.formatted(user.getNickName(), subscriptionName, dto.getAmount().toString());

        mailService.sendEmail(SendEmailDto.builder()
                .userEmail(user.getEmail())
                .theme("Bank check")
                .text(text)
                .build());
    }

}
