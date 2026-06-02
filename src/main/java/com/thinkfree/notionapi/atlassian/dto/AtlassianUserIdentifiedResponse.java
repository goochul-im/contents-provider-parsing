package com.thinkfree.notionapi.atlassian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AtlassianUserIdentifiedResponse(
        @JsonProperty("account_id")
        String accountId,
        String email,
        String name,
        String picture
) {
}
