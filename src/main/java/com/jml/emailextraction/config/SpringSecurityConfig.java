package com.jml.emailextraction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/notifications").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())  // updated way to enable OAuth2 login
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/notifications")
                );

        return http.build();
    }
}
