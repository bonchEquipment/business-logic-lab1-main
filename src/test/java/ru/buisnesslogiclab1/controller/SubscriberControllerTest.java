package ru.buisnesslogiclab1.controller;

import java.util.UUID;
import lombok.SneakyThrows;
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
import ru.buisnesslogiclab1.dto.Role;
import ru.buisnesslogiclab1.entity.UserEntity;
import ru.buisnesslogiclab1.entity.VideoEntity;
import ru.buisnesslogiclab1.repository.CommentRepository;
import ru.buisnesslogiclab1.repository.LikeRepository;
import ru.buisnesslogiclab1.repository.UserRepository;
import ru.buisnesslogiclab1.repository.VideoRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.buisnesslogiclab1.model.ModelFactory.createAddCommentURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createAddLikeURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createCommentEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createLikeEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createRevokeLikeURL;
import static ru.buisnesslogiclab1.model.ModelFactory.createUserEntity;
import static ru.buisnesslogiclab1.model.ModelFactory.createVideoEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriberControllerTest extends AbstractIntegrationTest {

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

    private static UserEntity SUBSCRIBER = createUserEntity(Role.SUBSCRIBER);


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Поставить лайк")
    void addLikeTest() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        assertEquals(0, likeRepository.findAll().size());

        performPostRequest(createAddLikeURL(), video.getId(), SUBSCRIBER.getId(), null);

        var likesList = likeRepository.findAll();
        assertEquals(1, likesList.size());
        var like = likesList.get(0);
        assertEquals(createLikeEntity(video.getId(), SUBSCRIBER.getId()), like);

        performPostRequest(createAddLikeURL(), video.getId(), SUBSCRIBER.getId(), null);
        assertEquals(1, likeRepository.findAll().size());

        likeRepository.deleteAll();
    }

    @Test
    @DisplayName("Убрать лайк")
    void revokeLikeTest() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        likeRepository.save(createLikeEntity(video.getId(), SUBSCRIBER.getId()));
        assertEquals(1, likeRepository.findAll().size());

        performPostRequest(createRevokeLikeURL(), video.getId(), SUBSCRIBER.getId(), null);
        assertEquals(0, likeRepository.findAll().size());

        likeRepository.deleteAll();
    }

    @Test
    @DisplayName("Добавить комментарий")
    void addCommentTest() throws Exception {
        videoRepository.deleteAll();
        var video = createVideoEntity();
        video = videoRepository.save(video);

        var commentText = "this video is super helpful!";
        performPostRequest(createAddCommentURL(), video.getId(), SUBSCRIBER.getId(),
                commentText);
        var comments = commentRepository.findAll();
        assertEquals(1, comments.size());

        var actualComment = comments.get(0);
        var expectedComment = createCommentEntity(video.getId(), SUBSCRIBER.getId(), commentText);

        assertEquals(expectedComment, actualComment);

        commentRepository.deleteAll();
    }


    @SneakyThrows
    private String performPostRequest(String path, UUID videoId, UUID subscriberId, @Nullable String text){
        return mockMvc.perform(post(path)
                        .header(HeaderConstant.VIDEO_ID, videoId)
                        .header(HeaderConstant.SUBSCRIBER_ID, subscriberId)
                        .header(HeaderConstant.TEXT, text == null ? "default text" : text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.status.httpCode", is(200)))
                .andExpect(jsonPath("$.status.businessCode", is("BLPS.0000")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
