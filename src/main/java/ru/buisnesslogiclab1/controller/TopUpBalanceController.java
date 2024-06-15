package ru.buisnesslogiclab1.controller;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.AccountType;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.feign.PaymentServiceFeign;
import ru.buisnesslogiclab1.service.AccountService;
import ru.buisnesslogiclab1.service.TopUpBalanceService;
import ru.buisnesslogiclab1.service.UserService;
import ru.buisnesslogiclab1.util.ResponseHelper;
import ru.buisnesslogiclab1.validation.IdValidator;
import ru.buisnesslogiclab1.validation.user.ValidUserId;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/topUpBalance")
public class TopUpBalanceController {

    private final TopUpBalanceService service;
    private final AccountService accountService;
    private final ResponseHelper responseHelper;
    private final PaymentServiceFeign feignClient;
    private final UserService userService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/fromBankToRutube")
    public ResponseEntity<Response<StatusCode>> topUpBalanceFromBankToRutube(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            Integer amount) throws Exception {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);

        return service.topUpBalance(BigDecimal.valueOf(amount), user.getId());

    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/fromAirToBank")
    public ResponseEntity<Response<StatusCode>> topUpBalanceFromAirToBank(
            @RequestHeader(value = HeaderConstant.AMOUNT, required = true)
            Integer amount) {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);

        return feignClient.addMoneyToBankAccountFromAir(new BigDecimal(amount), user.getId());
    }

}
