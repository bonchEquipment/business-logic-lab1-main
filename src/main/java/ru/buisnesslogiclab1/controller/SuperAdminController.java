package ru.buisnesslogiclab1.controller;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.security.XMLUserManager;
import ru.buisnesslogiclab1.service.SuperAdminService;
import ru.buisnesslogiclab1.service.TopUpBalanceService;
import ru.buisnesslogiclab1.service.UserService;
import ru.buisnesslogiclab1.util.ResponseHelper;
import ru.buisnesslogiclab1.validation.IdValidator;
import ru.buisnesslogiclab1.validation.user.ValidUserId;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/superAdmin")
public class SuperAdminController {


    private final SuperAdminService service;
    private final ResponseHelper responseHelper;
    private final IdValidator idValidator;
    private final UserRepository repository;
    private final UserService userService;


    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @SneakyThrows
    @PostMapping("/appointNewAdmin")
    public ResponseEntity<Response<StatusCode>> appointNewAdmin(
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            @ValidUserId
            String userId) {
        if (!idValidator.isIdExisting(null, userId))
            return responseHelper.asResponseEntity(idValidator.createErrorStatus(null, userId));

        var userIdUUID = UUID.fromString(userId);
        try {
            service.appointNewAdmin(userIdUUID);
            return responseHelper.asResponseEntity(StatusCode.OK);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return responseHelper.asResponseEntity(StatusCode.createRequestFailedCode(e.getMessage()));
        }
    }


}
