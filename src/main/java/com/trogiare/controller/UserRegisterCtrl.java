package com.trogiare.controller;

import com.trogiare.common.enumrate.*;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;
import com.trogiare.model.UserToken;
import com.trogiare.payload.ForgotPassword;
import com.trogiare.payload.UserPayload;
import com.trogiare.payload.VerifyEmail;
import com.trogiare.payload.user.SetPassword;
import com.trogiare.repo.UserRepo;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.repo.UserTokenRepo;
import com.trogiare.service.EmailService;
import com.trogiare.utils.TokenUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.trogiare.respone.MessageResp;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Slf4j
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
        Optional<User> userByUserName = userRepo.findByUserNameFirt(payload.getUserName());
        if(userByUserName.isPresent()){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.USERNAME_EXIST));
        }
        Optional<User> userBySDT = userRepo.findBySdtFirt(payload.getSdt());
        if(userBySDT.isPresent()){
            return ResponseEntity.status(400).body(MessageResp.error(ErrorCodesEnum.SDT_EXIST));
        }
        Optional<User> userByEmail = userRepo.findByEmailFirt(payload.getEmail());
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
        emailService.sendVerifyingReq(user,token);
        return ResponseEntity.ok().body(MessageResp.ok(user));
    }
    @RequestMapping(path = "/confirm", method = RequestMethod.POST)
    public HttpEntity<Object> emailConfirm(@Valid @RequestBody VerifyEmail payload) throws MessagingException {
        Optional<UserToken> userTokenOtp =  userTokenRepo.findByToken(payload.getToken());
        if(!userTokenOtp.isPresent()){
            log.info("NOT valid user token");
            throw new BadRequestException(ErrorCodesEnum.INVALID_TOKEN);
        }
        UserToken userToken = userTokenOtp.get();
        if(!userToken.getTokenType().equals(TokenTypeEnum.VERIFYING_EMAIL.name())){
            log.info("NOT valid user token");
            throw new BadRequestException(ErrorCodesEnum.INVALID_TOKEN);
        }
        if(!userToken.getStatus().equals(TokenStatusEnum.WAITING.name())){
            throw new BadRequestException(ErrorCodesEnum.INVALID_TOKEN);
        }
        if(userToken.getExpiredTime().isBefore(LocalDateTime.now())){
            log.info("Token expried");
            userToken.setStatus(TokenStatusEnum.EXPIRED.name());
            userTokenRepo.save(userToken);
            throw new BadRequestException(ErrorCodesEnum.TOKEN_EXPIRED);
        }
        // set Status user
        Optional<User> userOtp = userRepo.findById(userToken.getUserId());
        User user = userOtp.get();
        user.setStatus(UserStatusEnum.ACTIVATED.name());
        user.setUpdatedTime(LocalDateTime.now());
        user = userRepo.save(user);
        //set status token
        userToken.setStatus(TokenStatusEnum.ACCEPTED.name());
        userTokenRepo.save(userToken);
        emailService.sendWelcomeMember(user);
        return ResponseEntity.ok(MessageResp.ok(user));
    }

    @RequestMapping(path = "/resend/confirm", method = RequestMethod.POST)
    public HttpEntity<Object> resendEmailConfirm(@Valid @RequestBody ForgotPassword payload) throws MessagingException, URISyntaxException {
        String email = payload.getEmail();
        Optional<User> opUser = userRepo.findByEmailFirt(email);
        if (!opUser.isPresent()) {
            log.info("NOT valid user Email-ID, This email does NOT exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageResp.error(ErrorCodesEnum.FIND_NOT_FOUND));
        }
        User user = opUser.get();
        if(!user.getStatus().equals(UserStatusEnum.PENDING.name())){
            log.info("Email is activated");
            return ResponseEntity.ok().body(MessageResp.ok());
        }

        // get 3 token same type
        List<UserToken> tokensRequested = userTokenRepo.findTokenRequested(user.getId(),
                TokenTypeEnum.VERIFYING_EMAIL.name(),
                PageRequest.of(0,3));

        if(!checkValidNumberRequest(tokensRequested)){
            log.info("Authentication request exceeded the allowed number of times");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageResp.error(ErrorCodesEnum.AUTHENTICATION_REQUEST_EXCEEDED));
        }

        //update new status for token lastest request
        List<UserToken> TokensRequestedLastest = userTokenRepo.findTokenRequested(user.getId(),
                TokenTypeEnum.VERIFYING_EMAIL.name(), PageRequest.of(0,1));
        if(TokensRequestedLastest.size() == 1 && TokensRequestedLastest.get(0).getStatus().equals(TokenStatusEnum.WAITING.name())){
            UserToken usertokenRequestedLastest = TokensRequestedLastest.get(0);
            usertokenRequestedLastest.setStatus(TokenStatusEnum.DISABLED.name());
            userTokenRepo.save(usertokenRequestedLastest);
        }

        //save Token to Database
        String authToken = TokenUtil.generateToken(64);
        UserToken userToken = new UserToken();
        userToken.setToken(authToken);
        userToken.setUserId(user.getId());
        userToken.setCreatedTime(LocalDateTime.now());
        userToken.setExpiredTime(LocalDateTime.now().plusHours(TokenUtil.EXPRIED_TOKEN));
        userToken.setStatus(TokenStatusEnum.WAITING.name());
        userToken.setTokenType(TokenTypeEnum.VERIFYING_EMAIL.name());
        userTokenRepo.save(userToken);
        emailService.sendVerifyingReq(user,authToken);
        return ResponseEntity.ok(MessageResp.ok(user));
    }

    @RequestMapping(path = "/forgot-password", method = RequestMethod.POST)
    public HttpEntity<Object> forgotPassword(@Valid @RequestBody ForgotPassword payload) throws MessagingException {
        log.info("forgotPass: {}", payload);
        String email = payload.getEmail().trim().toLowerCase();
        Optional<User> opUser = userRepo.findByEmailFirt(email);
        if (!opUser.isPresent()) {
            log.info("NOT valid user Email-ID, This email does NOT exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.EMAIL_DOES_NOT_EXISTED));
        }
        User user = opUser.get();
        if (!(user.getStatus().equals(UserStatusEnum.ACTIVATED.name()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.EMAIL_NOT_VERIFIED));
        }
        String authToken = TokenUtil.generateNumber(6);

        // save token to database
        UserToken userToken =  new UserToken();
        userToken.setToken(authToken);
        userToken.setUserId(user.getId());
        userToken.setTokenType(TokenTypeEnum.FORGOT_PASSWORD.name());
        userToken.setStatus(TokenStatusEnum.WAITING.name());
        userToken.setCreatedTime(LocalDateTime.now());
        userToken.setExpiredTime(LocalDateTime.now().plusHours(TokenUtil.EXPRIED_TOKEN));
        userTokenRepo.save(userToken);
        emailService.sendForgotPassword(user, authToken);
        return ResponseEntity.ok(MessageResp.ok(user));
    }

    @RequestMapping(path = "/forgot-password/set-new", method = RequestMethod.POST)
    public HttpEntity<Object> setNewPasswordByToken(@Valid @RequestBody SetPassword payload) {
        if (!payload.getYourPassword().equals(payload.getRetypePassword())) {
            log.info("Password can NOT be empty, Password and Retype password must be matched each other");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.REPASSWORD_NOT_EQUALS_PASSWORD));
        }
        String token = payload.getToken();
        Optional<UserToken> userTokenOtp = userTokenRepo.findByToken(token);
        if (!userTokenOtp.isPresent()) {
            log.info("NOT valid Token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_TOKEN));
        }
        UserToken userToken = userTokenOtp.get();
        if(userToken.getExpiredTime().isBefore(LocalDateTime.now())){
            log.info("Token expried");
            userToken.setStatus(TokenStatusEnum.EXPIRED.name());
            userTokenRepo.save(userToken);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.TOKEN_EXPIRED));
        }
        if(userToken.getStatus().equals(TokenStatusEnum.ACCEPTED.name())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.TOKEN_EXPIRED));
        }
        if (!payload.getUid().equals(userToken.getUserId())) {
            log.info("NOT valid UID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_UID));
        }
        //change status Token
        userToken.setStatus(TokenStatusEnum.ACCEPTED.name());
        userTokenRepo.save(userToken);
        //update password for user
        Optional<User>  userOtp = userRepo.findById(userToken.getUserId());
        User user = userOtp.get();
        user.setPassword(passwordEncoder.encode(payload.getYourPassword()));
        userRepo.save(user);
//        mailSender.sendChangePasswordSuccess(user);
        return ResponseEntity.ok(MessageResp.ok(user));
    }

    private boolean checkValidNumberRequest(List<UserToken> userTokens){
        if(userTokens.size() < 3){
            return true;
        }
        for(int i = 0 ; i < userTokens.size(); i++){
            if(!userTokens.get(i).getStatus().equals(TokenStatusEnum.DISABLED.name())){
                return true;
            }
        }
        if(ChronoUnit.HOURS.between(userTokens.get(2).getCreatedTime(), LocalDateTime.now()) < TokenUtil.EXPRIED_TOKEN){
            return false;
        }
        return true;
    }
}
