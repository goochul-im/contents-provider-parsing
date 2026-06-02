package com.thinkfree.notionapi.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
public interface AuthenticationJpaRepository extends JpaRepository<Authentication, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Authentication> findAuthenticationByMemberIdAndProviderType(String memberId, ProviderType providerType);

}
