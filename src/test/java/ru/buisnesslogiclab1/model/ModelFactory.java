package ru.buisnesslogiclab1.model;

import java.util.HexFormat;
import java.util.UUID;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.dto.AvailableMode;
import ru.buisnesslogiclab1.dto.Role;
import ru.buisnesslogiclab1.entity.CommentEntity;
import ru.buisnesslogiclab1.entity.LikeEntity;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.entity.VideoApprovalEntity;
import ru.buisnesslogiclab1.entity.VideoEntity;

public class ModelFactory {

    public static final String SUBSCRIBER_URL = "/subscriber";
    public static final String ADMIN_URL = "/admin";
    public static final String USER_URL = "/user";

    public static final String VIDEO_STRING = "{\"id\":\"d5e8d5c9-49c9-4177-a725-a29d2a7190d6\",\"title\":\"JUnit5 crush course\",\"description\":\"Crush course for JUnit5 framework\",\"link\":\"Cv72I\",\"availableMode\":\"TO_ALL_USERS\",\"userId\":null,\"approvedBy\":null}";
    public static final String VIDEO_WITHOUT_ID_STRING = "{\"id\":null,\"title\":\"JUnit5 crush course\",\"description\":\"Crush course for JUnit5 framework\",\"link\":\"Cv72I\",\"availableMode\":\"TO_ALL_USERS\",\"userId\":null,\"approvedBy\":null}";
    public static final UUID VIDEO_ID = UUID.fromString("d5e8d5c9-49c9-4177-a725-a29d2a7190d6");



    public static VideoEntity createVideoEntity() {
        return VideoEntity.builder()
                .id(UUID.randomUUID())
                .approvedBy(null)
                .availableMode(AvailableMode.TO_ALL_USERS)
                .description("Crush course for JUnit5 framework")
                .title("JUnit5 crush course")
                .link("Cv72I")
                .contentMP4(HexFormat.ofDelimiter(":")
                .parseHex("e0:4f:d0:20:ea:3a:69:10:a2:d8:08:00:2b:30:30:9d"))
                .build();
    }

    public static UserEntity createUserEntity(Role role) {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .nickName("sava save")
                .role(role)
                .build();
    }

    public static LikeEntity createLikeEntity(UUID videoId, UUID userId) {
        return LikeEntity.builder()
                .videoId(videoId)
                .subscriberId(userId)
                .build();
    }

    public static CommentEntity createCommentEntity(UUID videoId, UUID subscriberId, String text) {
        return CommentEntity.builder()
                .videoId(videoId)
                .subscriberId(subscriberId)
                .text(text)
                .build();
    }

    public static VideoApprovalEntity createVideoApprovalEntity(UUID videoId,
                                                                ApprovalStatus approvalStatus, String comment) {
        return VideoApprovalEntity.builder()
                .videoId(videoId)
                .approvalStatus(approvalStatus)
                .comment(comment)
                .build();
    }

    public static String createAddLikeURL() {
        return SUBSCRIBER_URL + "/addLike";
    }

    public static String createRevokeLikeURL() {
        return SUBSCRIBER_URL + "/revokeLike";
    }

    public static String createAddCommentURL() {
        return SUBSCRIBER_URL + "/addComment";
    }

    public static String createGetVideosForApprovalURL() {
        return ADMIN_URL + "/getVideosForApproval";
    }

    public static String createApproveVideosURL() {
        return ADMIN_URL + "/approveVideo";
    }

    public static String createRejectVideosURL() {
        return ADMIN_URL + "/rejectVideo";
    }

    public static String createSendVideoForApprovalURL(){
        return USER_URL + "/sendVideoForApproval";
    }

    public static String createAddVideoURL(){
        return USER_URL + "/addVideo";
    }

    public static String createChangeAvailabilityStatusURL(){
        return USER_URL + "/changeAvailabilityStatus";
    }
}