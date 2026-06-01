package com.thinkfree.notionapi.gmail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmail")
public record GmailProperties(
        String gmailApiBaseUrl
) {
}
