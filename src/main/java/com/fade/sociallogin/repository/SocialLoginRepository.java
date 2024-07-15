package com.fade.sociallogin.repository;

import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    boolean existsByCodeAndSocialType(String code, SocialType socialType);
    Optional<SocialLogin> findByCodeAndSocialType(String code, SocialType socialType);
}