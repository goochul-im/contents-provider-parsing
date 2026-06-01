package com.thinkfree.notionapi.gmail.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService {

    private final RestClient gmailRestClient;

    public JsonNode getMessages(String userId, int maxResults, String token) {
        JsonNode body = gmailRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/gmail/v1/users/{userId}/messages")
                        .queryParam("maxResults", maxResults)
                        .build(userId)
                )
                .header(HttpHeaders.AUTHORIZATION, getApiToken(token))
                .retrieve()
                .body(JsonNode.class);

        log.info("body = {}", body);

        return body;
    }



    private String getApiToken(String token) {
        return "Bearer " + token;
    }

}
