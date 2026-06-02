package com.thinkfree.notionapi.atlassian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AtlassianAccessibleResponse(
        String id,
        String url,
        String name,
        List<String> scopes,
        String avatarUrl
) {
}
