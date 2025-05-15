package com.toonpick.repository;

import com.toonpick.entity.MemberSocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialLoginRepository extends JpaRepository<MemberSocialLogin, Long> {
}
