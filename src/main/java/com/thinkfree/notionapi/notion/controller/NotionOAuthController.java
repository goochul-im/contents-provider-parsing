package com.thinkfree.notionapi.notion.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.thinkfree.notionapi.notion.service.NotionOAuthService;
import com.thinkfree.notionapi.notion.config.NotionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/notion/oauth")
@RequiredArgsConstructor
public class NotionOAuthController {

    private final NotionProperties properties;
    private final NotionOAuthService notionOAuthService;

    @GetMapping("/authorize")
    public String authorize() {
        URI uri = UriComponentsBuilder
                .fromUriString(properties.authUrl())
                .queryParam("client_id", properties.clientId())
                .queryParam("response_type", "code")
                .queryParam("owner", "user")
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("state","random")
                .build()
                .toUri();

        return "redirect:" + uri;
    }

    @GetMapping("/callback")
    public JsonNode callback(
            @RequestParam String code,
            @RequestParam String state
    ){
        return notionOAuthService.exchangeCodeForToken(code);
    }

}
