package ru.buisnesslogiclab1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HexFormat;
import java.util.UUID;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.buisnesslogiclab1.AbstractIntegrationTest;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.dto.AvailableMode;
import ru.buisnesslogiclab1.dto.Role;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.repository.CommentRepository;
import ru.buisnesslogiclab1.repository.LikeRepository;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.buisnesslogiclab1.model.ModelFactory.VIDEO_ID;
import static ru.buisnesslogiclab1.model.ModelFactory.createAddVideoURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createChangeAvailabilityStatusURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createSendVideoForApprovalURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createUserEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createVideoApprovalEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createVideoEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VideoApprovalRepository videoApprovalRepository;

    private static final UserEntity USER = createUserEntity(Role.USER);


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if (userRepository.findAll().isEmpty())
            userRepository.save(USER);
    }

    @Test
    @DisplayName("Отправить видео на модерацию")
    void sendVideoForApprovalTest() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);
        assertEquals(0, videoApprovalRepository.findAll().size());

        mockMvc.perform(post(createSendVideoForApprovalURL())
                        .header(HeaderConstant.VIDEO_ID, video.getId())
                        .header(HeaderConstant.USER_ID, USER.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(1, videoApprovalRepository.findAll().size());
        var videoApproval = videoApprovalRepository.findAll().get(0);
        assertEquals(createVideoApprovalEntity(video.getId(), ApprovalStatus.PENDING, null), videoApproval);

        var notUploadedVideo = createVideoEntity();

        mockMvc.perform(post(createSendVideoForApprovalURL())
                        .header(HeaderConstant.VIDEO_ID, notUploadedVideo.getId())
                        .header(HeaderConstant.USER_ID, USER.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0003")))
                .andExpect(jsonPath("$.status.description", is("There is no video with such id")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @DisplayName("Добавить видео")
    void addVideoTest() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video.setId(VIDEO_ID);
        byte[] content = HexFormat.ofDelimiter(":")
                .parseHex("e0:4f:d0:20:ea:3a:69:10:a2:d8:08:00:2b:30:30:9d");

        mockMvc.perform(post(createAddVideoURL())
                        .header(HeaderConstant.VIDEO_INFO, entityToString(video))
                        .header(HeaderConstant.VIDEO_CONTENT_MP4, (Object) content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var videos = videoRepository.findAll();
        assertEquals(1, videos.size());
        var actualVideo = videos.get(0);
        Assertions.assertThat(actualVideo)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(video);
        var videoWithoutId = createVideoEntity();
        videoWithoutId.setId(null);


        mockMvc.perform(post(createAddVideoURL())
                        .header(HeaderConstant.VIDEO_INFO, entityToString(videoWithoutId))
                        .header(HeaderConstant.VIDEO_CONTENT_MP4, content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        videos = videoRepository.findAll();
        assertEquals(2, videos.size());
        actualVideo = videos.stream().filter(v -> !v.getId().equals(video.getId())).toList().get(0);
        assertNotEquals(video.getId(), actualVideo.getId());
        Assertions.assertThat(actualVideo)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(videoWithoutId);

        videoRepository.deleteAll();
    }

    @Test
    @DisplayName("Поменять настройки доступа")
    void changeAvailabilityStatus() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        mockMvc.perform(post(createChangeAvailabilityStatusURL())
                        .header(HeaderConstant.VIDEO_ID, video.getId())
                        .header(HeaderConstant.AVAILABILITY_MODE, AvailableMode.BY_LINK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var actualVideo = videoRepository.findAll().get(0);
        var notUploadedVideo = createVideoEntity();

        mockMvc.perform(post(createChangeAvailabilityStatusURL())
                        .header(HeaderConstant.VIDEO_ID, notUploadedVideo.getId())
                        .header(HeaderConstant.AVAILABILITY_MODE, AvailableMode.BY_LINK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0003")))
                .andExpect(jsonPath("$.status.description", is("There is no video with such id")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @SneakyThrows
    private String performPostRequest(String path, UUID videoId, UUID userId, @Nullable String text) {
        return mockMvc.perform(post(path)
                        .header(HeaderConstant.VIDEO_ID, videoId)
                        .header(HeaderConstant.USER_ID, userId)
                        .header(HeaderConstant.TEXT, text == null ? "default text" : text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    private String entityToString(Object entity){
        return objectMapper.writeValueAsString(entity);
    }
}
