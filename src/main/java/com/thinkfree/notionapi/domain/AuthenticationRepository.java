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
    public Authentication save(Authentication authentication) {
        Optional<Authentication> optionalAuthentication = authenticationJpaRepository.findAuthenticationByMemberIdAndProviderType(
                authentication.getMemberId(),
                authentication.getProviderType()
        );

        optionalAuthentication.ifPresent(authenticationJpaRepository::delete);

        return authenticationJpaRepository.save(authentication);
    }

    @Transactional
    public void refresh(long id, String refreshToken) {
        Authentication authentication = authenticationJpaRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        authentication.refresh(refreshToken);
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
