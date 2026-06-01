package com.thinkfree.notionapi.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleExchangeResponse(
        @JsonProperty("access_token")
        String access_token,
        @JsonProperty("expires_in")
        long expires_in,
        @JsonProperty("refresh_token")
        String refresh_token,
        @JsonProperty("refresh_token_expires_in")
        long refresh_token_expires_in,
        @JsonProperty("scope")
        String scope,
        @JsonProperty("token_type")
        String token_type
) {
}
