package com.thinkfree.notionapi.notion.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkfree.notionapi.notion.config.NotionProperties;
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
    private final NotionService notionService;

    public JsonNode exchangeCodeForToken(String code) {
        String basicAuth = createBasicAuth();

        JsonNode body = notionRestClient.post()
                .uri(notionProperties.tokenUrl())
                .header(HttpHeaders.AUTHORIZATION, basicAuth)
                .body(Map.of(
                        "grant_type", "authorization_code",
                        "code", code,
                        "redirect_uri", notionProperties.redirectUri()
                ))
                .retrieve()
                .body(JsonNode.class);

        log.info("body = {}",body.toPrettyString());
        String accessToken = body.get("access_token").toString();
        accessToken = accessToken.substring(1, accessToken.length() - 1);
        log.info("access_token = {}", accessToken);
        notionService.apiToken = accessToken;

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
