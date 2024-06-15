package ru.buisnesslogiclab1.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.dto.SendEmailDto;
import ru.buisnesslogiclab1.entity.UserSubscriptionEntity;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.UserSubscriptionRepository;
import ru.buisnesslogiclab1.service.MailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotifyAboutSubscriptionExpirationJob {

    private final MailService service;
    private final UserSubscriptionRepository repository;
    private final UserRepository userRepository;

    @Scheduled(cron = "${cron-expressions.notify-about-subscription-expiration}")
    public void process() {
        log.info("notify-about-subscription job started");
        var now = LocalDateTime.now();
        var list = repository.findAllByExpirationDateIsBetween(
                now.plusDays(1), now.plusDays(1).plusMinutes(5));
        if (list.isEmpty())
            log.info("there is no one to notify");


        for (var userSubscription : list)
            sendNotificationToUser(userSubscription);

        log.info("notify-about-subscription job finished");
    }

    private void sendNotificationToUser(UserSubscriptionEntity entity) {
        var user = userRepository.findById(entity.getUserId()).get();
        var text = """
                Dear %s!
                Your subscription %s is gonna expire at %s.
                If there is not enough money on your rutube account subscription will be canceled.
                It there is enough money the subscription will be prolonged.
                                
                Thank you for staying with us!
                                
                                
                Please do not reply to this email.
                If you have any questions contact arceniy.devyatkin@yandex.ru
                """.formatted(user.getNickName(), entity.getSubscriptionName(),
                entity.getExpirationDate().truncatedTo(ChronoUnit.MINUTES).toString());

        service.sendEmail(SendEmailDto.builder()
                .userEmail(user.getEmail())
                .theme("Subscription is about to expire")
                .text(text)
                .build());
    }

}
