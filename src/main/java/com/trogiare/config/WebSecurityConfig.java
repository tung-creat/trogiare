package com.trogiare.config;

import com.trogiare.security.OAuthEntryPointRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private OAuthEntryPointRest unauthorizedHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuration for http security");
        http
                .cors()
                .and()
                .csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()//
//                .requestMatchers( "/", "/favicon.ico", "/**/*.png",
//                        "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css",
//                        "/**/*.js", "/api/v1/socks/**").permitAll()
                .requestMatchers("/login/**",
                        "/api/v1/login/**",
                        "/api/v1/auth/**",
                        "/logout/**",
                        "/api/v1/reg/**",
                        "/api/v1/file/**",
                        "/api/v1/socks/**"
                        ).permitAll()
                .requestMatchers("/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/v2/api-docs").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
        ;

        return http.build();
    }



}