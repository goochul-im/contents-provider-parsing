package com.thinkfree.notionapi.atlassian.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "atlassian")
public record AtlassianOAuthProperties(
        String clientId,
        String clientSecret,
        String redirectUri,
        String authUri,
        String tokenUri,
        String accessibleResourcesUri,
        String userIdentifierUri,
        String getPageUri
) {
}
