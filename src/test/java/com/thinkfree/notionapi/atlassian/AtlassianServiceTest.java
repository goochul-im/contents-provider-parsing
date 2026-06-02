package com.thinkfree.notionapi.atlassian;

import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationJpaRepository;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AtlassianServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private AtlassianOAuthService atlassianOAuthService;

    @Autowired
    private AuthenticationJpaRepository authenticationJpaRepository;

    @Test
    void DB_연결_및_저장_테스트(){
        //given
        Authentication authentication = new Authentication(
                "testmember",
                "testAccessToken",
                "testRefreshToken",
                ProviderType.ATLASSIAN
        );

        //when
        Authentication save = authenticationRepository.save(authentication);

        //then
        assertThat(save.getId()).isNotNull();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void 리프레시_토큰이_갱신되어도_예전_리프레시_토큰을_사용할_수_있어야_한다(){
        //given


        //when

        //then
    }


}
