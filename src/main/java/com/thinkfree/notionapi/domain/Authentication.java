package com.thinkfree.notionapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "authentication")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "provide_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    // TODO: 인증 토큰별 부가 정보들(구글의 스코프, 노션의 페이지 등)을 어떻게 저장하지??

    public Authentication(String memberId, String accessToken, String refreshToken, ProviderType providerType) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.providerType = providerType;
    }

}
