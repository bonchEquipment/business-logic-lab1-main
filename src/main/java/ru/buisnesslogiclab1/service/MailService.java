package ru.buisnesslogiclab1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.dto.SendEmailDto;
import ru.buisnesslogiclab1.event.producer.KafkaProducerService;
import ru.buisnesslogiclab1.util.ObjectParser;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final KafkaProducerService kafkaProducerService;

    public void sendEmail(SendEmailDto dto){
        kafkaProducerService.sendMessage("send-email", ObjectParser.parse(dto));
    }

}
