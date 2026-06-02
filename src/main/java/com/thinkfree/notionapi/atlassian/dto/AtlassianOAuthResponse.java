package com.thinkfree.notionapi.atlassian.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AtlassianOAuthResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        Long expiresIn,
        String scope,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("token_type")
        String tokenType
) {
}
