package ru.buisnesslogiclab1.validation;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.buisnesslogiclab1.dto.Response;
import ru.buisnesslogiclab1.dto.StatusCode;
import ru.buisnesslogiclab1.repository.CommentRepository;
import ru.buisnesslogiclab1.repository.LikeRepository;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;
import ru.buisnesslogiclab1.util.ResponseHelper;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdValidator {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoApprovalRepository videoApprovalRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final ResponseHelper responseHelper;


    public boolean isIdExisting(String videoId, String userId){
        if (videoId != null)
            return videoRepository.existsById(UUID.fromString(videoId));
        if (userId != null)
            return userRepository.existsById(UUID.fromString(userId));

        return false;
    }

    public StatusCode createErrorStatus(String videoId, String userId){
        if (videoId != null)
            return StatusCode.THERE_IS_NO_SUCH_VIDEO;

        if (userId != null)
            return StatusCode.THERE_IS_NO_SUCH_USER;

        return StatusCode.OK;
    }
}
