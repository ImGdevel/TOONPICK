package com.toonpick.repository;

import com.toonpick.entity.MemberSocialLogin;
import com.toonpick.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSocialLoginRepository extends JpaRepository<MemberSocialLogin, Long> {

    Optional<MemberSocialLogin> findByProviderAndProviderId(Provider provider, String providerId);
}
