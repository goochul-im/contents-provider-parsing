package com.thinkfree.notionapi.notion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notion")
public record NotionProperties(
        String version,
        String baseUrl,
        String clientId,
        String clientSecret,
        String redirectUri,
        String authUrl,
        String tokenUrl
) {
}
