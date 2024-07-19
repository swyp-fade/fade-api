package com.fade.global.filter;

import com.fade.global.component.JwtTokenProvider;
import com.fade.member.vo.MemberJwtClaim;
import com.fade.member.vo.UserVo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(
            JwtTokenProvider jwtTokenProvider
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        resolveToken(httpServletRequest)
            .filter(jwtTokenProvider::validateToken)
            .map((token) -> jwtTokenProvider.decodeJwt(token, MemberJwtClaim.class))
            .ifPresent((memberJwtClaim) -> {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        new UserVo(memberJwtClaim.getId(), memberJwtClaim.getMemberRoles()),
                        null,
                        memberJwtClaim.getMemberRoles()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });

        chain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        final String tokenType = "Bearer";

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenType + " ")) {
            return Optional.of(bearerToken.substring(tokenType.length() + 1));
        }

        return Optional.empty();
    }
}