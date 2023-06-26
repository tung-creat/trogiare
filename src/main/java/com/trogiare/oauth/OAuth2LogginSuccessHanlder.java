package com.trogiare.oauth;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class OAuth2LogginSuccessHanlder extends SimpleUrlAuthenticationSuccessHandler {
    final Logger logger = LoggerFactory.getLogger(OAuth2LogginSuccessHanlder.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("Hello world");
        CustomOAuth2User customOAuth2User  = (CustomOAuth2User) authentication.getPrincipal();
        logger.info("Information received: {}", customOAuth2User.getAttributes());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
