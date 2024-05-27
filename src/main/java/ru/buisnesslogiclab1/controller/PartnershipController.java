package ru.buisnesslogiclab1.controller;

import java.util.List;
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
import ru.buisnesslogiclab1.validation.video.ValidVideoId;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/partnership")
public class PartnershipController {

    @GetMapping("/")
    public ResponseEntity<Response<List<CommentEntity>>> getComments(
            @RequestHeader(value = HeaderConstant.VIDEO_ID, required = true)
            @ValidVideoId
            String videoId) {
        return null;
    }

}
