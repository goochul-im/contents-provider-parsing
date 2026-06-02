package com.thinkfree.notionapi.gmail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GmailMessages(
        List<GmailIdAndThreadId> messages
) {
}
