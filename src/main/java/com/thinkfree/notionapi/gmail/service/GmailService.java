package com.thinkfree.notionapi.gmail.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import com.thinkfree.notionapi.gmail.dto.GmailMessageResponse;
import com.thinkfree.notionapi.gmail.dto.GmailMessages;
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
    private final AuthenticationRepository authenticationRepository;

    public GmailMessages getMessages(String email, int maxResults) {
        GmailMessages body = gmailRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/gmail/v1/users/{userId}/messages")
                        .queryParam("maxResults", maxResults)
                        .build(email)
                )
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(email))
                .retrieve()
                .body(GmailMessages.class);

        return body;
    }

    public GmailMessageResponse getMessage(String email, String messageId) {
        GmailMessageResponse body = gmailRestClient.get()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{messageId}?format=full",
                        messageId)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(email))
                .retrieve()
                .body(GmailMessageResponse.class);

        log.info("body = {}", body);

        return body;
    }

    private String getAccessToken(String email) {
        Authentication authentication = authenticationRepository.findAuthenticationByMemberIdAndProviderType(
                email, ProviderType.GMAIL
        ).orElseThrow(
                () -> new IllegalArgumentException("필요한 토큰을 찾지 못함")
        );

        String token = authentication.getAccessToken();
        return "Bearer " + token;
    }

}
