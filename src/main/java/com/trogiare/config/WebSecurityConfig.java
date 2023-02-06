package com.trogiare.config;

import com.trogiare.security.LocalTokenAuth;
import com.trogiare.security.OAuthEntryPointRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends GlobalMethodSecurityConfiguration {

    static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private OAuthEntryPointRest unauthorizedHandler;
    @Value("${app.cors.allow_domain}")
    private String allowedDomain1;
    @Value("${app.cors.allow_domain_deploy}")
    private String allowedDomain2;
    @Autowired
    private LocalTokenAuth localTokenAuth;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(allowedDomain2)
                        .allowCredentials(true);
                registry.addMapping("/**").allowedOrigins(allowedDomain1)
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

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
                        "/api/v1/socks/**",
                        "/api/v1/provinces/**",
                        "/image/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                .requestMatchers("/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
                .and()
                .addFilterBefore(localTokenAuth, UsernamePasswordAuthenticationFilter.class)
        ;


        return http.build();
    }


    @Bean
    public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Swagger Realm");
        return entryPoint;
    }
}