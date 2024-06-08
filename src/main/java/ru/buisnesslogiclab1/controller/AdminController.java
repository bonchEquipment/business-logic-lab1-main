package ru.buisnesslogiclab1.controller;

import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.dto.VideosDto;
import ru.buisnesslogiclab1.entity.VideoApprovalEntity;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;
import ru.buisnesslogiclab1.service.UserService;
import ru.buisnesslogiclab1.util.ResponseHelper;
import ru.buisnesslogiclab1.validation.IdValidator;
import ru.buisnesslogiclab1.validation.admin.ValidAdminId;
import ru.buisnesslogiclab1.validation.video.ValidVideoId;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final VideoRepository videoRepository;
    private final ResponseHelper responseHelper;
    private final VideoApprovalRepository videoApprovalRepository;
    private final SubscriberController subscriberController;
    private final IdValidator idValidator;
    private final UserService userService;


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @GetMapping("/getListOfVideosForApproval")
    public ResponseEntity<Response<VideosDto>> getVideosForApproval() {
        var videosForApproval = videoApprovalRepository.findByApprovalStatus(ApprovalStatus.PENDING.toString());
        var ids = videosForApproval.stream().map(v -> v.getVideoId()).collect(Collectors.toList());
        var videos = videoRepository.findAllById(ids);
        videos.forEach(v -> v.setContentMP4(null));
        return responseHelper.asResponseEntity(StatusCode.OK, new VideosDto(videos));
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @GetMapping("/getVideoForApproval")
    public ResponseEntity<ByteArrayResource> getVideoForApproval(
            @RequestHeader(value = HeaderConstant.VIDEO_ID, required = true)
            @ValidVideoId
            String videoId) {
        if (!idValidator.isIdExisting(videoId, null))
            return ResponseEntity.status(404).build();

        return subscriberController.getVideo(videoId);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/approveVideo")
    public ResponseEntity<Response<Void>> approveVideo(
            @RequestHeader(value = HeaderConstant.VIDEO_ID, required = true)
            @ValidVideoId
            String videoId) {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);

        if (!idValidator.isIdExisting(videoId, null))
            return responseHelper.asResponseEntity(idValidator.createErrorStatus(videoId, null));

        var videoIdUUID = UUID.fromString(videoId);
        if (!videoRepository.existsById(videoIdUUID))
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_VIDEO);

        if (!videoApprovalRepository.existsById(videoIdUUID))
            return responseHelper.asResponseEntity(StatusCode.VIDEO_DOES_NOT_SENT_FOR_APPROVAL);

        videoApprovalRepository.save(new VideoApprovalEntity(videoIdUUID, ApprovalStatus.APPROVED, null));

        return responseHelper.asResponseEntity(StatusCode.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/rejectVideo")
    public ResponseEntity<Response<Void>> rejectVideo(
            @RequestHeader(value = HeaderConstant.VIDEO_ID, required = true)
            @ValidVideoId
            String videoId,
            @RequestHeader(value = HeaderConstant.TEXT, required = true)
            String text) {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);

        if (!idValidator.isIdExisting(videoId, null))
            return responseHelper.asResponseEntity(idValidator.createErrorStatus(videoId, null));

        var videoIdUUID = UUID.fromString(videoId);
        if (!videoRepository.existsById(videoIdUUID))
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_VIDEO);

        if (!videoApprovalRepository.existsById(videoIdUUID))
            return responseHelper.asResponseEntity(StatusCode.VIDEO_DOES_NOT_SENT_FOR_APPROVAL);

        videoApprovalRepository.save(new VideoApprovalEntity(videoIdUUID, ApprovalStatus.REJECTED, text));

        return responseHelper.asResponseEntity(StatusCode.OK);
    }

}
