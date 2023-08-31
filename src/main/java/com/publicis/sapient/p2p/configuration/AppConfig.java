package com.publicis.sapient.p2p.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${secret}")
    private String jwtSecret;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Validating request Via filter chain");
        http.cors()
                .configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("https://p2pmarket.dev", "https://api.p2pmarket.dev", "http://localhost:4200", "http://localhost:4201", "http://localhost:4202","http://localhost:4203", "http://localhost:4204", "http://localhost:4205", "http://34.93.177.114", "https://34.93.177.114"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Collections.singletonList("Authorization"));
                    config.setMaxAge(3600L);

                    return config;
                })
                .and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**","/rest-api-docs/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers(HttpMethod.POST,"/authenticate").permitAll()
                .requestMatchers(HttpMethod.GET,"/authenticate").permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated();
        return http.build();
    }

}
