package com.training.OrderService.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/orders/place").permitAll()
                        //.requestMatchers("/orders/place/**").authenticated() // Requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               *//* .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(
                               // jwt -> jwt
                                //        .jwkSetUri("http://localhost:8086/.well-known/jwks.json") // Replace with your Auth Server's JWK URI
                        ) // Validate JWT tokens
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //*//*
                );

        return http.build();
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
                .securityMatcher("/**"); // Apply to all endpoints

        return http.build();
    }
}
