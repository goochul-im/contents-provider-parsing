package com.thinkfree.notionapi.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthenticationRepository {

    private final AuthenticationJpaRepository authenticationJpaRepository;

    @Transactional
    public void save(Authentication authentication) {
        Optional<Authentication> optionalAuthentication = authenticationJpaRepository.findAuthenticationByMemberIdAndProviderType(
                authentication.getMemberId(),
                authentication.getProviderType()
        );

        optionalAuthentication.ifPresentOrElse(
                authenticationJpaRepository::delete,
                () -> authenticationJpaRepository.save(authentication)
        );
    }

    @Transactional
    public Authentication find(String memberEmail, ProviderType providerType) {
        return authenticationJpaRepository.findAuthenticationByMemberIdAndProviderType(
                memberEmail, providerType
        ).orElseThrow(
                () -> new IllegalArgumentException("인증 정보를 찾을 수 없습니다.")
        );
    }

}
