package com.thinkfree.notionapi.gmail.dto;

import java.util.List;

public record GmailPayload(
        String partId,
        String mimeType,
        String filename,
        List<GmailHeader> headers,
        GmailBody body,
        List<GmailPayload> parts
) {
}
