package com.thinkfree.notionapi.atlassian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfluenceContentResponse(
        String id,
        String status,
        String title,
        @JsonProperty(namespace = "space_id")
        String spaceId,
        ConfluenceBody body
) {

    public record ConfluenceBody(ConfluenceStorage storage) {}

    public record ConfluenceStorage(
        String value,
        String representation
    ){}

}
