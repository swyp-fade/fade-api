package com.fade.global.config;

import com.fade.global.component.JwtTokenProvider;
import com.fade.global.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true
)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final List<String> allowOriginHosts;

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
                                .anyRequest()
                                .authenticated()
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
