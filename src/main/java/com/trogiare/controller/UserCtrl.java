package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.model.User;
import com.trogiare.repo.UserRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.UserResp;
import com.trogiare.security.UserPrincipal;
import com.trogiare.service.EmailService;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/users")
public class UserCtrl {
    static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);

    @Autowired
    private UserRepo userRepo;


    @Autowired
    private EmailService mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity<Object> getAll(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size) {
        logger.info(String.valueOf(UserUtil.getAuth().getAuthorities()));

        if (page == null || page <= 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = Constants.ITEM_PER_PAGE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAll(pageable);

        return ResponseEntity.ok(MessageResp.page(users));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Object> getById(@PathVariable("id") String id) {
        if (ValidateUtil.isEmpty(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        Optional<User> opUser = userRepo.findById(id);
        if (!opUser.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        User user1 = opUser.get();
//        Optional<ObjectMedia> objectMedia = objectMediaRepo.findAvatarObject(ObjectTypeEnum.USER.name(),
//                user1.getId(),
//                ObjectRefTypeEnum.AVARTAR.name());
//        if (!objectMedia.isPresent()){
//            return ResponseEntity.ok(MessageResp.ok(user1));
//        }
//        ObjectMedia ob = objectMedia.get();
        UserResp userResp = new UserResp();
        userResp.setUser(user1);
        return ResponseEntity.ok(MessageResp.ok(userResp));
    }

//    @PreAuthorize("#payload.userId == authentication.principal.id")
//    @RequestMapping(path = "", method = RequestMethod.PUT)
//    @ApiOperation(value = "Update User", response = MessageResp.class)
//    public HttpEntity<Object> update(@Valid @ModelAttribute UserForm payload) {
//        String id = payload.getUserId();
//        if (ValidateUtil.isEmpty(id)) {
//            throw new InputInvalidException();
//        }
//        Optional<User> optionalUser = userRepo.findById(id);
//        if(!optionalUser.isPresent()){
//            logger.info("NOT valid ID");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
//        }
//        if(payload.getAvatar() != null){
//            String idOb = UserUtil.getUserId();
//            Optional<ObjectMedia> objectMediaOP = objectMediaRepo.findAvatarObject(ObjectTypeEnum.USER.name(),idOb,ObjectRefTypeEnum.AVARTAR.name());
//            if(objectMediaOP.isPresent()){
//                ObjectMedia objectMedia =objectMediaOP.get();
//                String idMe = objectMedia.getMediaId();
//                Media me = mediaRepo.findById(idMe).get();
//                MediaDelete mediaDelete = new MediaDelete(me.getId(),me.getHash());
//                mediaDeleteRepo.save(mediaDelete);
//                mediaRepo.deleteById(me.getId());
//                objectMediaRepo.deleteById(objectMedia.getId());
//                createAvatar(payload);
//            }else{
//                createAvatar(payload);
//            }
//        }
//        User user = optionalUser.get();
//        // TODO set new properties
//        user.setFullName(payload.getFullName());
//        user.setUpdaterId(UserUtil.getUserId());
//        user.setUpdatedTime(LocalDateTime.now());
//        user.setPhone(payload.getPhone());
//        user = userRepo.save(user);
//        return ResponseEntity.ok(MessageResp.ok(user));
//    }
//    @PreAuthorize("#payload.userId == authentication.principal.id")
//    @RequestMapping(path = "/change-password", method = RequestMethod.PUT)
//    @ApiOperation(value = "User do change their password", response = MessageResp.class)
//    public HttpEntity<Object> changePassword(@Valid @RequestBody ChangePassword payload) {
//        Optional<User> userOpt = userRepo.findById(payload.getUserId());
//        if (!userOpt.isPresent()) {
//            logger.info("NOT valid user ID");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_USER_ID));
//        }
//        User user = userOpt.get();
//        boolean isValidPassword = passwordEncoder.matches(payload.getPassword(), user.getPassword());
//        if (!isValidPassword) {
//            logger.info("NOT valid password");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_CURRENT_PASSWORD));
//        }
//        user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
//        userRepo.save(user);
//        mailSender.sendChangePasswordSuccess(user);
//        return ResponseEntity.ok(MessageResp.ok());
//    }
}
