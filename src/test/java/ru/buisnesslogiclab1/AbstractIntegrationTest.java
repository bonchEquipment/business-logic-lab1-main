package ru.buisnesslogiclab1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    public static final TestcontainersConfiguration TESTCONTAINERS_CONFIGURATION =
            TestcontainersConfiguration.getInstance();

    public static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    private static final String TEST_DATABASE_NAME = "itmo";
    private static final String TEST_USER = "postgres";
    private static final String TEST_PASSWORD = "postgres";

    static {
        DockerImageName postgreSqlImage = DockerImageName.parse(
                        TESTCONTAINERS_CONFIGURATION.getEnvVarOrProperty("postgres.container.image", null))
                .asCompatibleSubstituteFor("postgres");
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(postgreSqlImage)
                .withDatabaseName(TEST_DATABASE_NAME)
                .withUsername(TEST_USER)
                .withPassword(TEST_PASSWORD);
        POSTGRESQL_CONTAINER.start();
    }

    @BeforeAll
    static void check_containers_running() {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.url", POSTGRESQL_CONTAINER::getJdbcUrl);
    }
}
