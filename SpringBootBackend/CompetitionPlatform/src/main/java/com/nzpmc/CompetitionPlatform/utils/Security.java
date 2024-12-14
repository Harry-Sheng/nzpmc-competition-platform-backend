package com.nzpmc.CompetitionPlatform.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection if appropriate
                .csrf(csrf -> csrf.disable())

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests to /api/user
                        .requestMatchers("/api/user").permitAll()

                        // Allow other specific endpoints if needed
                        // .requestMatchers("/public/**").permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Optionally, configure form-based login or HTTP Basic authentication
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}

