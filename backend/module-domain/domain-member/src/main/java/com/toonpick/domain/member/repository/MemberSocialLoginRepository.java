package com.toonpick.domain.member.repository;

import com.toonpick.domain.member.entity.MemberSocialLogin;
import com.toonpick.domain.member.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSocialLoginRepository extends JpaRepository<MemberSocialLogin, Long> {

    Optional<MemberSocialLogin> findByProviderAndProviderId(Provider provider, String providerId);
}
