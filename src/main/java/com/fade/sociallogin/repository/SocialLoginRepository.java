package com.fade.sociallogin.repository;

import com.fade.sociallogin.constant.SocialType;
import com.fade.sociallogin.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    boolean existsBySocialTypeAndCode(SocialType socialType, String code);
    Optional<SocialLogin> findBySocialTypeAndCode(SocialType socialType, String code);
}