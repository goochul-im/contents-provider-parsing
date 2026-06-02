package com.thinkfree.notionapi.notion.service;

import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationJpaRepository;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import com.thinkfree.notionapi.notion.config.NotionProperties;
import com.thinkfree.notionapi.notion.dto.NotionOAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotionOAuthService {

    private final NotionProperties notionProperties;
    private final RestClient notionRestClient;
    private final AuthenticationRepository authenticationRepository;


    public NotionOAuthResponse exchangeCodeForToken(String code) {
        String basicAuth = createBasicAuth();

        NotionOAuthResponse body = notionRestClient.post()
                .uri(notionProperties.tokenUrl())
                .header(HttpHeaders.AUTHORIZATION, basicAuth)
                .body(Map.of(
                        "grant_type", "authorization_code",
                        "code", code,
                        "redirect_uri", notionProperties.redirectUri()
                ))
                .retrieve()
                .body(NotionOAuthResponse.class);

        log.info("body = {}", body);
        String accessToken = body.accessToken();
        log.info("access_token = {}", accessToken);

        authenticationRepository.save(new Authentication(
                body.owner().user().person().email(),
                body.accessToken(),
                body.refreshToken(),
                ProviderType.NOTION
        ));

        return body;
    }

    private String createBasicAuth() {
        String clientId = notionProperties.clientId();
        String clientSecret = notionProperties.clientSecret();

        String raw = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(
                raw.getBytes(StandardCharsets.UTF_8)
        );

        return "Basic " + encoded;
    }


}
