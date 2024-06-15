package ru.buisnesslogiclab1.feign;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;



import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@FeignClient(
        name = "PaymentServiceFeign",
        url = "${payment.host-port}",
        path = ""
        //configuration = PaymentServiceFeignConfiguration.class
)
public interface PaymentServiceFeign {

    @PostMapping(path = "/withdrawPartnershipMoney", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response<StatusCode>> withdrawPartnershipMoney(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            BigDecimal amount,
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            UUID userId);

    @PostMapping(path = "/withdrawMoneyForSubscription", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response<StatusCode>> withdrawMoneyForSubscription(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            BigDecimal amount,
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            UUID userId,
            @RequestHeader(value = HeaderConstant.ACCOUNT_TYPE, required = true)
            AccountType accountType);

    @PostMapping(path = "/topUpBalance", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response<StatusCode>> topUpBalance(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            BigDecimal amount,
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            UUID userId);

    @PostMapping(path = "/addMoneyToBankAccountFromAir", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response<StatusCode>> addMoneyToBankAccountFromAir(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            BigDecimal amount,
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            UUID userId);


}
