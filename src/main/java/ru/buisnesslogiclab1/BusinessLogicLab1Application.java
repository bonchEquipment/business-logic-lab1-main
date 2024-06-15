package ru.buisnesslogiclab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BusinessLogicLab1Application {

    public static void main(String[] args) {
        SpringApplication.run(BusinessLogicLab1Application.class, args);
    }

}
