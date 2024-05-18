package ru.buisnesslogiclab1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.buisnesslogiclab1.AbstractIntegrationTest;
import ru.buisnesslogiclab1.config.HeaderConstant;
import ru.buisnesslogiclab1.dto.ApprovalStatus;
import ru.buisnesslogiclab1.dto.Role;
import ru.buisnesslogiclab1.dto.VideosDto;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.entity.VideoEntity;
import ru.buisnesslogiclab1.repository.CommentRepository;
import ru.buisnesslogiclab1.repository.LikeRepository;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.VideoApprovalRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.buisnesslogiclab1.model.ModelFactory.createApproveVideosURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createRejectVideosURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createGetVideosForApprovalURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createUserEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createVideoApprovalEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createVideoEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    private VideoApprovalRepository videoApprovalRepository;

    private static final UserEntity ADMIN = createUserEntity(Role.ADMIN);


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
        userRepository.save(ADMIN);
    }

    @Test
    @DisplayName("Посмотреть видео на верификацию")
    void getVideoForApprovalTest() throws Exception {
        videoApprovalRepository.deleteAll();
        videoRepository.deleteAll();
        var video_1 = createVideoEntity();
        var video_2 = createVideoEntity();
        video_1 = videoRepository.save(video_1);
        video_2 = videoRepository.save(video_2);

        var listOfVideosForApproval = performGetVideoRequest();
        assertEquals(0, listOfVideosForApproval.size());
        videoApprovalRepository.save(createVideoApprovalEntity(video_1.getId(), ApprovalStatus.APPROVED, null));
        videoApprovalRepository.save(createVideoApprovalEntity(video_2.getId(), ApprovalStatus.REJECTED, "I don't like description"));

        listOfVideosForApproval = performGetVideoRequest();
        assertEquals(0, listOfVideosForApproval.size());

        videoApprovalRepository.save(createVideoApprovalEntity(video_1.getId(), ApprovalStatus.PENDING, null));
        listOfVideosForApproval = performGetVideoRequest();
        assertEquals(1, listOfVideosForApproval.size());

        videoApprovalRepository.deleteAll();
    }

    @Test
    @DisplayName("Разрешить видео")
    void approveVideoTest() throws Exception {
        videoApprovalRepository.deleteAll();
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        var approvalText = "nice video";
        videoApprovalRepository.save(createVideoApprovalEntity(video.getId(), ApprovalStatus.PENDING, null));
        performPostRequest(createApproveVideosURL(), video.getId(), ADMIN.getId(), approvalText);
        var videoApprovals = videoApprovalRepository.findAll();
        assertEquals(1, videoApprovals.size());

        var actualVideoApproval = videoApprovals.get(0);
        var expectedVideoApproval = createVideoApprovalEntity(video.getId(),
                ApprovalStatus.APPROVED, null);
        assertEquals(expectedVideoApproval, actualVideoApproval);
    }

    @Test
    @DisplayName("Отправить видео на доработку")
    void rejectVideoTest() throws Exception {
        videoApprovalRepository.deleteAll();
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        var rejectText = "disgusting";
        videoApprovalRepository.save(createVideoApprovalEntity(video.getId(), ApprovalStatus.PENDING, null));
        performPostRequest(createRejectVideosURL(), video.getId(), ADMIN.getId(), rejectText);
        var videoApprovals = videoApprovalRepository.findAll();
        assertEquals(1, videoApprovals.size());

        var actualVideoApproval = videoApprovals.get(0);
        var expectedVideoApproval = createVideoApprovalEntity(video.getId(),
                ApprovalStatus.REJECTED, rejectText);
        assertEquals(expectedVideoApproval, actualVideoApproval);

    }

    @SneakyThrows
    private String performPostRequest(String path, UUID videoId, UUID adminId, @Nullable String text) {
        return mockMvc.perform(post(path)
                        .header(HeaderConstant.VIDEO_ID, videoId)
                        .header(HeaderConstant.ADMIN_ID, adminId)
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
    private List<VideoEntity> performGetVideoRequest() {
        var response = mockMvc.perform(get(createGetVideosForApprovalURL())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var data = objectMapper.readValue(
                objectMapper.readTree(response).get("data").toString(), VideosDto.class);

        return data.getVideos();
    }

}
