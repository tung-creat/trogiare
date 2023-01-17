package com.trogiare.controller;

import com.trogiare.common.enumrate.*;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;
import com.trogiare.model.UserToken;
import com.trogiare.payload.UserPayload;
import com.trogiare.repo.UserRepo;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.repo.UserTokenRepo;
import com.trogiare.service.EmailService;
import com.trogiare.utils.TokenUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.trogiare.respone.MessageResp;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/v1/reg")
public class UserRegisterCtrl {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepo userRoleRepo;
    @Autowired
    private UserTokenRepo userTokenRepo;
    @Autowired
    private EmailService emailService;
    @Transactional
    @RequestMapping(path="",method = RequestMethod.POST)
    public HttpEntity<?> register(@Valid @RequestBody UserPayload payload) throws MessagingException, URISyntaxException {
        if(!payload.getRePassword().equals(payload.getPassword())){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.REPASSWORD_NOT_EQUALS_PASSWORD));
        }
        Optional<User> userByUserName = userRepo.findByUserName(payload.getUserName());
        if(userByUserName.isPresent()){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.USERNAME_EXIST));
        }
        Optional<User> userBySDT = userRepo.findBySdt(payload.getSdt());
        if(userBySDT.isPresent()){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.SDT_EXIST));
        }
        Optional<User> userByEmail = userRepo.findByEmail(payload.getEmail());
        if(userByEmail.isPresent()){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.EMAIL_EXIST));
        }
        User user = new User();
        user.setUserName(payload.getUserName());
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setUserName(payload.getUserName());
        user.setEmail(payload.getEmail());
        user.setSdt(payload.getSdt());
        user.setStatus(UserStatusEnum.PENDING.name());
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        user = userRepo.save(user);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleName(UserRoleEnum.USER.name());
        userRoleRepo.save(userRole);

        String token = TokenUtil.generateToken(64);
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setToken(token);
        userToken.setTokenType(TokenTypeEnum.VERIFYING_EMAIL.name());
        userToken.setStatus(TokenStatusEnum.WAITING.name());
        userToken.setCreatedTime(LocalDateTime.now());
        userToken.setExpiredTime(LocalDateTime.now().plusHours(TokenUtil.EXPRIED_TOKEN));
        userTokenRepo.save(userToken);
        emailService.verifyEmail(user,token);
        return ResponseEntity.ok().body(MessageResp.ok(user));
    }
//    @PostMapping("/sendMail")
//    public String
//    sendMail(@RequestBody EmailDetails details)
//    {
//        String status
//                = emailService.sendSimpleMail(details);
//
//        return status;
//    }

}
