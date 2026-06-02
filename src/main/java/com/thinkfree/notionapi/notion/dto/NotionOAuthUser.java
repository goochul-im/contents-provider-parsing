package com.thinkfree.notionapi.notion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionOAuthUser(
        String object,
        String id,
        String name,
        @JsonProperty("avatar_url")
        String avatarUrl,
        String type,
        NotionOAuthPerson person
) {
}
