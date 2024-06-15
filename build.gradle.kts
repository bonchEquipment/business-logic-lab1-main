plugins {
    java
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.1.4"
}


group = "ru.itmo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}



repositories {
    mavenCentral()
}


val logstashGelfVersion = "1.15.0"
val logstashEncoderVersion = "7.1.1"
val testContainerVersion = "1.17.3"
val lombokVersion = "1.18.24"
val slf4jVersion = "1.7.30"
val openfeignVersion = "12.0"
val springCloudVersion = "2021.0.4"
val shedlockVersion = "4.33.0"

dependencies {
    //core
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Spring Framework dependencies
    implementation ("org.springframework:spring-tx:5.3.9")

    /**
     * Spring cloud
     */
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-annotation-error-decoder")

    // Bitronix JTA implementation
    implementation ("org.codehaus.btm:btm:2.1.4")
    //implementation("org.springframework.boot:spring-boot-starter-jta-bitronix:2.1.4")


    //security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    //implementation("org.springframework.boot:spring-boot-starter-xml")
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")

    //beans
    //implementation("org.springframework:spring-beans")
    implementation ("org.springframework:spring-core:5.3.22") // Replace with the correct version




    //database
    implementation("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("com.vladmihalcea:hibernate-types-52:2.9.5")

    // scheduling
    implementation("net.javacrumbs.shedlock:shedlock-spring:${shedlockVersion}")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:${shedlockVersion}")


    //lombok
    implementation("org.projectlombok:lombok:${lombokVersion}")

    //kafka
    implementation("org.springframework.kafka:spring-kafka")

    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    /**
     * Slf4j
     */
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:${logstashEncoderVersion}")
    implementation("biz.paluch.logging:logstash-gelf:${logstashGelfVersion}")

    /**
     * TestContainers
     */
    implementation(platform("org.testcontainers:testcontainers-bom:${testContainerVersion}"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    /**
     * Open Feign
     */
    implementation ("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation ("io.github.openfeign:feign-okhttp")
    /*implementation("io.github.openfeign:feign-core:${openfeignVersion}")
    implementation("io.github.openfeign:feign-jackson:${openfeignVersion}")
    implementation("io.github.openfeign:feign-okhttp:${openfeignVersion}")*/

    /**
     * Spring Boot Test
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
