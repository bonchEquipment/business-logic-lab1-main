package ru.buisnesslogiclab1.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.dto.AvailableMode;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.dto.VideoId;
import ru.buisnesslogiclab1.entity.VideoApprovalEntity;
import ru.buisnesslogiclab1.entity.VideoEntity;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;
import ru.buisnesslogiclab1.service.UserService;
import ru.buisnesslogiclab1.util.ResponseHelper;
import ru.buisnesslogiclab1.validation.IdValidator;
import ru.buisnesslogiclab1.validation.user.ValidUserId;
import ru.buisnesslogiclab1.validation.video.ValidVideoId;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final VideoRepository videoRepository;
    private final ResponseHelper responseHelper;
    private final VideoApprovalRepository videoApprovalRepository;
    private final IdValidator idValidator;
    private final UserService userService;

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @GetMapping("/checkApprovalStatus")
    public ResponseEntity<Response<VideoApprovalEntity>> getVideosForApproval(
            @ValidVideoId
            @RequestHeader(value = HeaderConstant.VIDEO_ID, required = true)
            String videoId) {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);

        if (!idValidator.isIdExisting(videoId, null))
            return responseHelper.asResponseEntity(idValidator.createErrorStatus(videoId, null));

        var videoApprovalEntityOptional =
                videoApprovalRepository.findById(UUID.fromString(videoId));

        if (videoApprovalEntityOptional.isEmpty())
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_VIDEO);

        return responseHelper.asResponseEntity(StatusCode.OK, videoApprovalEntityOptional.get());
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/sendVideoForApproval")
    public ResponseEntity<Response<Void>> sendVideoForApproval(
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

        var approvalEntityOptional = videoApprovalRepository.findById(videoIdUUID);
        if (approvalEntityOptional.isEmpty()) {
            videoApprovalRepository.save(new VideoApprovalEntity(videoIdUUID, ApprovalStatus.PENDING, null));
            return responseHelper.asResponseEntity(StatusCode.OK);
        }

        var approvalEntity = approvalEntityOptional.get();
        if (approvalEntity.getApprovalStatus().equals(ApprovalStatus.APPROVED))
            return responseHelper.asResponseEntity(StatusCode.VIDEO_IS_ALREADY_APPROVED);

        if (approvalEntity.getApprovalStatus().equals(ApprovalStatus.REJECTED))
            return responseHelper.asResponseEntity(StatusCode.VIDEO_IS_ALREADY_REJECTED);

        return responseHelper.asResponseEntity(StatusCode.OK);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    @PostMapping("/addVideo")
    public ResponseEntity<Response<VideoId>> addVideo(
            @RequestHeader(value = HeaderConstant.VIDEO_INFO, required = true)
            VideoEntity videoInfo,
            @RequestBody()
            byte[] content) {
        var user = userService.findUserEntityForCurrentSession();
        if (user == null)
            return responseHelper.asResponseEntity(StatusCode.THERE_IS_NO_SUCH_USER);
        videoInfo.setId(user.getId());
        videoInfo.setContentMP4(content);
        var videoEntity = videoRepository.save(videoInfo);
        return responseHelper.asResponseEntity(StatusCode.OK, new VideoId(videoEntity.getId()));
    }
}
