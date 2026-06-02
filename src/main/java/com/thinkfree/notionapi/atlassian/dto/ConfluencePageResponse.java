package com.thinkfree.notionapi.atlassian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfluencePageResponse(
        String id,
        String status,
        String title,
        String spaceId,
        String parentId
) {
}
