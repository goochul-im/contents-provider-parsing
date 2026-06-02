package com.thinkfree.notionapi.gmail.service;

import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationJpaRepository;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import com.thinkfree.notionapi.gmail.controller.MessageBodyResponse;
import com.thinkfree.notionapi.gmail.dto.GmailMessageResponse;
import com.thinkfree.notionapi.gmail.dto.GmailMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService {

    private final RestClient gmailRestClient;
    private final AuthenticationRepository authenticationRepository;
    private final GmailBodyExtractor gmailBodyExtractor;

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

    public MessageBodyResponse getMessage(String email, String messageId) {
        GmailMessageResponse body = gmailRestClient.get()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{messageId}?format=full",
                        messageId)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(email))
                .retrieve()
                .body(GmailMessageResponse.class);

        Objects.requireNonNull(body);
        log.info("body = {}", body);

        String text = gmailBodyExtractor.extractTextPlain(body);
        String html = Jsoup.parse(gmailBodyExtractor.extractTextHtml(body)).text();
        String from = gmailBodyExtractor.getFrom(body);
        String date = gmailBodyExtractor.getDate(body);
        String subject = gmailBodyExtractor.getSubject(body);

        return new MessageBodyResponse(
                subject,
                from,
                date,
                text,
                html
        );
    }

    private String getAccessToken(String email) {
        Authentication authentication = authenticationRepository.find(
                email, ProviderType.GMAIL
        );

        String token = authentication.getAccessToken();
        return "Bearer " + token;
    }

}
