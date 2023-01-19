package com.trogiare.controller;

import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.common.enumrate.UserStatusEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;
import com.trogiare.payload.user.UserLogin;
import com.trogiare.repo.UserRepo;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.UserResp;
import com.trogiare.security.LocalTokenProvider;
import com.trogiare.utils.TokenUtil;
import com.trogiare.utils.ValidateUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenCtrl {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserRoleRepo userRoleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LocalTokenProvider localTokenProvider;
    @RequestMapping(path="/local",method = RequestMethod.POST)
    public HttpEntity<?> localAuthen(@Valid @RequestBody UserLogin payload){
        if(ValidateUtil.isEmpty(payload.getUserName()) || ValidateUtil.isEmpty(payload.getPassword())){
            throw new BadRequestException(ErrorCodesEnum.INVALID_INPUT);
        }
        Optional<User> userOp =userRepo.findByUsernameOrEmail(payload.getUserName());
        if(!userOp.isPresent()){
            throw new BadRequestException(ErrorCodesEnum.ACCOUNT_NOT_EXIST);
        }
        User user = userOp.get();
        if(!passwordEncoder.matches(payload.getPassword(),user.getPassword())){
            throw new BadRequestException(ErrorCodesEnum.INFORMATION_USER_NOT_VALID);
        }
        if(!user.getStatus().equals(UserStatusEnum.ACTIVATED.name())){
            throw new BadRequestException(ErrorCodesEnum.ACCOUNT_NOT_ACTIVATED);
        }
        List<UserRole> userRole =userRoleRepo.findByUserId(user.getId());
        String token = localTokenProvider.generateToken(user,userRole);
        UserResp userResp = new UserResp();
        userResp.setUser(user);
        userResp.setRoles(userRole.stream().map(e ->e.getRoleName() ).collect(Collectors.toList()));
        userResp.setToken(token);
        return ResponseEntity.ok().body(MessageResp.ok(userResp));
    }
}
