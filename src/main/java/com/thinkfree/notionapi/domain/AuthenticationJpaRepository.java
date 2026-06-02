package com.thinkfree.notionapi.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
public interface AuthenticationJpaRepository extends JpaRepository<Authentication, Long> {

    Optional<Authentication> findAuthenticationByMemberIdAndProviderType(String memberId, ProviderType providerType);

}
