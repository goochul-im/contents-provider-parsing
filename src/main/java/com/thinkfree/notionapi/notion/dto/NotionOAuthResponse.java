package com.thinkfree.notionapi.notion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NotionOAuthResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("bot_id")
        String botId,
        @JsonProperty("workspace_name")
        String workspaceName,
        @JsonProperty("workspace_icon")
        String workspaceIcon,
        @JsonProperty("workspace_id")
        String workspaceId,
        NotionWorkspaceOwner owner,
        @JsonProperty("duplicated_template_id")
        String duplicatedTemplateId,
        @JsonProperty("request_id")
        String requestId
) {
}
