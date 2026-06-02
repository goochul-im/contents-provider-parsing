package com.thinkfree.notionapi.gmail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GmailMessageResponse(
        String id,
        String threadId,
        List<String> labelId,
        String snippet,
        GmailPayload payload,
        Integer sizeEstimate,
        String historyId,
        String internalDate
) {
}
