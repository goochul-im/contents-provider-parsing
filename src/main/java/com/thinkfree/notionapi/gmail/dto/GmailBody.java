package com.thinkfree.notionapi.gmail.dto;

public record GmailBody(
        Integer size,
        String data,
        String attachmentId
) {
}
