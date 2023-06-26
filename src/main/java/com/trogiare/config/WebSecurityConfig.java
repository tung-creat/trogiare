package com.trogiare.config;

import com.trogiare.oauth.OAuth2LogginSuccessHanlder;
import com.trogiare.security.LocalTokenAuth;
import com.trogiare.security.OAuthEntryPointRest;
import com.trogiare.oauth.CustomOAuth2UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private OAuthEntryPointRest unauthorizedHandler;
    @Autowired
    private LocalTokenAuth localTokenAuth;
    @Autowired
    private CustomOAuth2UserService userService;
    @Autowired
    private OAuth2LogginSuccessHanlder oAuth2LogginSuccessHanlder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        logger.info("Configuration for http security");
        http
                .cors()
                .and()
                .csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()//
                .antMatchers( "/", "/favicon.ico", "/**/*.png",
                        "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css",
                        "/**/*.js", "/api/v1/socks/**").permitAll()
                .antMatchers("/login/**",
                        "/api/v1/login/**",
                        "/api/v1/auth/**",
                        "/file/**",
                        "/logout/**",
                        "/api/v1/reg/**",
                        "/api/v1/file/**",
                        "/api/v1/socks/**",
                        "/api/v1/provinces/**",
                        "/image/**",
                        "/api/v1/message/**"
                ).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/news/**").permitAll()
                .antMatchers("/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**").permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .and()
                .oauth2Login()
                    .userInfoEndpoint()
                    .userService(userService)
                    .and()
                    .successHandler(oAuth2LogginSuccessHanlder)
                .and()
                .addFilterBefore(localTokenAuth, UsernamePasswordAuthenticationFilter.class);
    }

}