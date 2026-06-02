package com.thinkfree.notionapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Table(name = "authentication")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Lob
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "access_token_expires_in")
    private Instant accessTokenExpiresIn;

    @Lob
    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expires_in")
    private Instant refreshTokenExpiresIn;

    @Column(name = "provide_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    public Authentication(String memberId, String accessToken, String refreshToken, ProviderType providerType) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.providerType = providerType;
    }

    public Authentication(String memberId, String accessToken, String refreshToken, ProviderType providerType, long accessTokenExpiresSeconds, long refreshTokenExpiresSeconds) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.providerType = providerType;
        this.accessTokenExpiresIn = Instant.now().plusSeconds(accessTokenExpiresSeconds);
        this.refreshTokenExpiresIn = Instant.now().plusSeconds(refreshTokenExpiresSeconds);
    }

    public void refresh(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
