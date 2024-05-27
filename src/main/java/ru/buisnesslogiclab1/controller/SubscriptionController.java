package ru.buisnesslogiclab1.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.entity.CommentEntity;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.service.SubscriptionService;
import ru.buisnesslogiclab1.util.ResponseHelper;
import ru.buisnesslogiclab1.validation.IdValidator;
import ru.buisnesslogiclab1.validation.user.ValidUserId;
import ru.buisnesslogiclab1.validation.video.ValidVideoId;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {


    private final SubscriptionService subscriptionService;
    private final ResponseHelper responseHelper;
    private final IdValidator idValidator;


    @GetMapping("/addSubscription")
    public ResponseEntity<Response<Void>> getComments(
            @RequestHeader(value = HeaderConstant.USER_ID, required = true)
            @ValidUserId
            String userId,
            @RequestHeader(value = HeaderConstant.SUBSCRIPTION_TYPE, required = true)
            String subscriptionType) {
        if (!idValidator.isIdExisting(null, userId))
            return responseHelper.asResponseEntity(idValidator.createErrorStatus(null, userId));

        var userIdUUID = UUID.fromString(userId);
        var operationStatusDto = subscriptionService.addSubscription(userIdUUID, subscriptionType);
        return null;
    }

}
