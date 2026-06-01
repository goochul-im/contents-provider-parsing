package com.thinkfree.notionapi.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/google/oauth")
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final GoogleOAuthProperties properties;
    private final GoogleOAuthService googleOAuthService;

    @GetMapping("/authorize")
    public String authorize() {
        URI uri = UriComponentsBuilder
                .fromUriString(properties.authUri())
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/gmail.readonly")
                .queryParam("access_type", "offline")
                .build()
                .toUri();

        return "redirect:" + uri;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam String code
    ){

        log.info("code = {}",code);
        String accessToken = googleOAuthService.getAccessTokenFromAuthorizationCode(code);
        return ResponseEntity.ok(accessToken);
    }



}
