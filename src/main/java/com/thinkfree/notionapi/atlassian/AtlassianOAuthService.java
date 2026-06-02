package com.thinkfree.notionapi.atlassian;

import com.thinkfree.notionapi.atlassian.config.AtlassianOAuthProperties;
import com.thinkfree.notionapi.atlassian.dto.AtlassianOAuthResponse;
import com.thinkfree.notionapi.atlassian.dto.AtlassianUserIdentifiedResponse;
import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtlassianOAuthService {

    private final AuthenticationRepository authenticationRepository;
    private final AtlassianOAuthProperties properties;
    private final RestClient atlassianOAuthRestClient;

    @Transactional
    public String exchangeCodeToAccessToken(String code) {

        Map<String, String> requestBody = Map.of(
                "grant_type", "authorization_code",
                "client_id", properties.clientId(),
                "client_secret", properties.clientSecret(),
                "code", code,
                "redirect_uri", properties.redirectUri()
        );

        log.info("code = {}",code);

        AtlassianOAuthResponse response = atlassianOAuthRestClient.post()
                .uri(properties.tokenUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .retrieve()
                .body(AtlassianOAuthResponse.class);
        log.info("response = {}", response);
        String accessToken = response.accessToken();
        AtlassianUserIdentifiedResponse userInfo = getUserInfo(accessToken);


        authenticationRepository.save(new Authentication(
                userInfo.email(),
                accessToken,
                response.refreshToken(),
                ProviderType.ATLASSIAN
        ));

        return accessToken;
    }

    private AtlassianUserIdentifiedResponse getUserInfo(String accessToken) {

        AtlassianUserIdentifiedResponse response = atlassianOAuthRestClient.get()
                .uri(properties.userIdentifierUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(AtlassianUserIdentifiedResponse.class);

        return response;
    }



}
