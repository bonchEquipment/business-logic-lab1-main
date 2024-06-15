package ru.buisnesslogiclab1.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.ChangeBalanceDto;
import ru.buisnesslogiclab1.dto.OperationStatusDto;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.SendEmailDto;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.event.producer.KafkaProducerService;
import ru.buisnesslogiclab1.feign.PaymentServiceFeign;
import ru.buisnesslogiclab1.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopUpBalanceService {

    private final PaymentServiceFeign feignClient;
    private final UserRepository userRepository;
    private final MailService mailService;


    public ResponseEntity<Response<StatusCode>> topUpBalance(BigDecimal amount, UUID userId) throws Exception {
        var response = feignClient.topUpBalance(amount, userId);
        if (response.getStatusCodeValue() == 200)
            sendCheckToUser(new ChangeBalanceDto(amount, userId));

        return response;
    }

    private void sendCheckToUser(ChangeBalanceDto dto) {
        var user = userRepository.findById(dto.getUserId()).get();
        var text = """
                Dear %s!
                You top up your rutube balance by %s rubles.
                Here is a bank check:
                inn: 1234556
                op_id: 12323
                ofd_id: 846123
                                
                Thank you for staying with us!
                                
                                
                Please do not reply to this email.
                If you have any questions contact arceniy.devyatkin@yandex.ru
                """.formatted(user.getNickName(), dto.getAmount().toString());

        mailService.sendEmail(SendEmailDto.builder()
                .userEmail(user.getEmail())
                .theme("Bank check")
                .text(text)
                .build());
    }
}
