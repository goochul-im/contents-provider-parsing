package com.thinkfree.notionapi.notion.service;

import com.fasterxml.jackson.databind.JsonNode;
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
    public String apiToken = "no used yet";

    public JsonNode retrievePage(String pageId) {
        String apiToken1 = getApiToken();
        log.info("token = {}", apiToken1);
        JsonNode body = notionRestClient.get()
                .uri("/v1/pages/{pageId}", pageId)
                .header(HttpHeaders.AUTHORIZATION, apiToken1)
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePage body = {}", body.toPrettyString());

        return body;
    }

    public JsonNode retrieveMarkdownContent(String pageId) {
        JsonNode body = notionRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/pages/{blockId}/markdown")
                        .queryParam("page_size", 100)
                        .build(pageId))
                .header(HttpHeaders.AUTHORIZATION, getApiToken())
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePageContent body = {}", body.toPrettyString());

        return body;
    }

    public JsonNode retrievePageContent(String pageId) {
        JsonNode body = notionRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/blocks/{blockId}/children")
                        .queryParam("page_size", 100)
                        .build(pageId))
                .header(HttpHeaders.AUTHORIZATION, getApiToken())
                .retrieve()
                .body(JsonNode.class);

        log.info("retrievePageContent body = {}", body.toPrettyString());

        return body;
    }

    private String getApiToken() {
        return "Bearer " + apiToken;
    }

}
