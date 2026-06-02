package com.thinkfree.notionapi.notion.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationJpaRepository;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotionService {

    private final RestClient notionRestClient;
    private final AuthenticationRepository authenticationRepository;

    public JsonNode retrievePage(String email, String pageId) {
        String apiToken1 = getAccessToken(email);
        log.info("token = {}", apiToken1);
        JsonNode body = notionRestClient.get()
                .uri("/v1/pages/{pageId}", pageId)
                .header(HttpHeaders.AUTHORIZATION, apiToken1)
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePage body = {}", body.toPrettyString());

        return body;
    }

    public JsonNode retrieveMarkdownContent(String pageId, String email) {
        JsonNode body = notionRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/pages/{blockId}/markdown")
                        .queryParam("page_size", 100)
                        .build(pageId))
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(email))
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePageContent body = {}", body.toPrettyString());

        return body;
    }

    public JsonNode retrievePageContent(String pageId, String email) {
        JsonNode body = notionRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/blocks/{blockId}/children")
                        .queryParam("page_size", 100)
                        .build(pageId))
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(email))
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePageContent body = {}", body.toPrettyString());

        return body;
    }

    private String getAccessToken(String email) {

        Authentication authentication = authenticationRepository.find(email, ProviderType.NOTION);

        return "Bearer " + authentication.getAccessToken();
    }

}
