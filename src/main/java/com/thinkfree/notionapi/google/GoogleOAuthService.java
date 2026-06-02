package com.thinkfree.notionapi.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkfree.notionapi.domain.Authentication;
import com.thinkfree.notionapi.domain.AuthenticationRepository;
import com.thinkfree.notionapi.domain.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleOAuthProperties properties;
    private final RestClient googleOauthRestClient;
    private final AuthenticationRepository authenticationRepository;
    private final ObjectMapper objectMapper;

    public String getAccessTokenFromAuthorizationCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", properties.clientId());
        form.add("client_secret", properties.clientSecret());
        form.add("redirect_uri", properties.redirectUri());
        form.add("grant_type", "authorization_code");

        GoogleExchangeResponse body = googleOauthRestClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(form)
                .retrieve()
                .body(GoogleExchangeResponse.class);

        log.info("body = {}",body);
        GoogleIdTokenPayload idTokenPayload = parseIdToken(body.idToken());
        log.info("idTokenPayload = {}", idTokenPayload);

        String accessToken = body.accessToken();

        authenticationRepository.save(new Authentication(
                idTokenPayload.email(),
                accessToken,
                body.refreshToken(),
                ProviderType.GMAIL
        ));

        return accessToken;
    }

    private GoogleIdTokenPayload parseIdToken(String token) {
        try{

            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid ID token format");
            }

            String payload = parts[1];

            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload); // 페이로드 디코드
            String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8); // UTF-8 형식으로 제작?

            JsonNode jsonNode = objectMapper.readTree(payloadJson);

            return new GoogleIdTokenPayload(
                    jsonNode.path("iss").asText(null),
                    jsonNode.path("sub").asText(null),
                    jsonNode.path("email").asText(null),
                    jsonNode.path("email_verified").asBoolean(false),
                    jsonNode.path("name").asText(null),
                    jsonNode.path("picture").asText(null),
                    jsonNode.path("aud").asText(null),
                    jsonNode.path("exp").asLong(0)
            );

        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Failed to parse Google ID token" ,e);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse Google ID token" ,e);
        }

    }

}
