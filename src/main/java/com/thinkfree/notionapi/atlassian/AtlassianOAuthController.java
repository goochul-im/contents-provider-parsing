package com.thinkfree.notionapi.atlassian;

import com.thinkfree.notionapi.atlassian.config.AtlassianOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/atlassian/oauth")
public class AtlassianOAuthController {

    private final AtlassianOAuthProperties properties;
    private final AtlassianOAuthService atlassianOAuthService;

    @GetMapping("/authorize")
    public String authorize() {
        String scope = String.join(" ",
//                "read:jira-work",
//                "read:jira-user",
                "read:confluence-content.all",
                "read:confluence-content.summary",
                "offline_access",
                "read:me"
        );

        URI uri = UriComponentsBuilder
                .fromUriString(properties.authUri())
                .queryParam("audience", "api.atlassian.com")
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope",
                        scope
                )
                .queryParam("prompt","consent")
                .build()
                .toUri();

        return "redirect:" + uri;
    }
    
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code){

        return ResponseEntity.ok(
                atlassianOAuthService.exchangeCodeToAccessToken(code)
        );
    }

}
