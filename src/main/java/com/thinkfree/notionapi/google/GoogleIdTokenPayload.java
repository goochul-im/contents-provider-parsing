package com.thinkfree.notionapi.google;

public record GoogleIdTokenPayload(
        String issuer,
        String subject,
        String email,
        boolean emailVerified,
        String name,
        String picture,
        String audience,
        long expireAt
) {
}
