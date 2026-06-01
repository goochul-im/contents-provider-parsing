package com.thinkfree.notionapi.common;

import com.thinkfree.notionapi.gmail.config.GmailProperties;
import com.thinkfree.notionapi.google.GoogleOAuthProperties;
import com.thinkfree.notionapi.notion.config.NotionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient notionRestClient(NotionProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.apiToken())
                .defaultHeader("Notion-Version", properties.version())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Bean
    public RestClient gmailRestClient(GmailProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.gmailApiBaseUrl())
                .build();
    }

    @Bean
    public RestClient googleOauthRestClient(GoogleOAuthProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.tokenUrl())
                .build();
    }

}
