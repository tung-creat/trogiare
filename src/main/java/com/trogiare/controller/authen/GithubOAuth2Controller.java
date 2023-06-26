package com.trogiare.controller.authen;

import com.trogiare.respone.MessageResp;
import com.trogiare.oauth.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/github")
@Slf4j
public class GithubOAuth2Controller {

    final Logger logger  = LoggerFactory.getLogger(GithubOAuth2Controller.class);
    @Autowired
    private OAuth2AuthorizedClientService clientService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/github");
    }

    @GetMapping("/seen")
    public ResponseEntity<?> callback(OAuth2UserRequest userRequest) {
        logger.info(customOAuth2UserService.loadUser(userRequest).getName());
        return ResponseEntity.ok().body(MessageResp.ok());
    }


}