package com.thinkfree.notionapi.gmail.controller;

public record MessageBodyResponse(
        String subject,
        String from,
        String date,
        String text,
        String html
) {
}
