package com.thinkfree.notionapi.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google")
public record GoogleOAuthProperties(
        String clientId,
        String clientSecret,
        String redirectUri,
        String authUri,
        String tokenUrl
) {
}
