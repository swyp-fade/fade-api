package com.fade.global.config;

import com.fade.global.component.JwtTokenProvider;
import com.fade.global.constant.ErrorCode;
import com.fade.global.dto.response.Response;
import com.fade.global.exception.ApplicationException;
import com.fade.global.filter.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.ErrorResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true
)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final List<String> allowOriginHosts;

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ApplicationException fail = new ApplicationException(ErrorCode.UNAUTHORIZED);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(Response.error(
                        fail.getErrorCode().getHttpStatus().value(),
                        fail.getMessage(),
                        Map.of(
                                "errorCode", fail.getErrorCode().name(),
                                "data", fail.getData()
                        )));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ApplicationException fail = new ApplicationException(ErrorCode.FORBIDDEN);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(Response.error(
                        fail.getErrorCode().getHttpStatus().value(),
                        fail.getMessage(),
                        Map.of(
                                "errorCode", fail.getErrorCode().name(),
                                "data", fail.getData()
                        )));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private static final String[] SWAGGER_URIS = {
            "/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            @Value("${cors.allow-origin-hosts}")
            List<String> allowOriginHosts
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.allowOriginHosts = allowOriginHosts;
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                        .requestMatchers(SWAGGER_URIS).permitAll()
                                        .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/members/search").permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(unauthorizedEntryPoint)
                )
                .addFilterBefore(new JwtFilter(this.jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .cors((cors) -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(this.allowOriginHosts);
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "set-cookie"));
                    configuration.setAllowCredentials(true);

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);

                    cors.configurationSource(source);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
        ;

        return http.build();
    }
}
