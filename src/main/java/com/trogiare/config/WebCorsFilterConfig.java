package com.trogiare.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@Slf4j
public class WebCorsFilterConfig {

    @Value("${app.cors.allow_domain}")
    private List<String> allowedDomain;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        log.info("WebCorsFilterConfig allowedDomain {}", allowedDomain);
        if(allowedDomain.contains("*")){
            config.addAllowedOrigin("*");
            log.info("WebCorsFilterConfig disable CORS");
        } else {
            config.setAllowCredentials(true);
            config.setAllowedOriginPatterns(allowedDomain);
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}