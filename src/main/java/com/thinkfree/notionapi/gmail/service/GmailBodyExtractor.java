package com.thinkfree.notionapi.gmail.service;

import com.thinkfree.notionapi.gmail.dto.GmailHeader;
import com.thinkfree.notionapi.gmail.dto.GmailMessageResponse;
import com.thinkfree.notionapi.gmail.dto.GmailPayload;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GmailBodyExtractor {

    public String extractTextPlain(GmailMessageResponse message) {
        return extractByMimeType(message.payload(), "text/plain");
    }

    public String extractTextHtml(GmailMessageResponse message) {
        return extractByMimeType(message.payload(), "text/html");
    }

    public String getFrom(GmailMessageResponse message) {
        return getHeader(message, "From");
    }

    public String getDate(GmailMessageResponse message) {
        return getHeader(message, "Date");
    }

    public String getSubject(GmailMessageResponse message) {
        return getHeader(message, "Subject");
    }

    private String getHeader(GmailMessageResponse message, String name) {
        if (message == null ||
                message.payload() == null ||
                message.payload().headers() == null) {
            return null;
        }

        return message.payload().headers().stream()
                .filter(header -> name.equalsIgnoreCase(header.name()))
                .map(GmailHeader::value)
                .findFirst()
                .orElse(null);
    }

    private String extractByMimeType(GmailPayload payload, String targetMimeType) {
        if (payload == null) {
            return null;
        }

        if (targetMimeType.equalsIgnoreCase(payload.mimeType())
                && payload.body() != null
                && payload.body().data() != null) {
            return decodeBase64Url(payload.body().data());
        }

        if (payload.parts() == null) {
            return null;
        }

        for (GmailPayload part : payload.parts()) {
            String result = extractByMimeType(part, targetMimeType);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private String decodeBase64Url(String data) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(data);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

}
