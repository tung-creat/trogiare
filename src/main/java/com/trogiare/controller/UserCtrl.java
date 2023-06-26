package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.component.CompressFileComponent;
import com.trogiare.exception.BadRequestException;
import com.trogiare.exception.InputInvalidException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;
import com.trogiare.payload.user.ChangePassword;
import com.trogiare.payload.user.UserAddRolePayload;
import com.trogiare.payload.user.UserFormPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.repo.UserRepo;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.UserResp;
import com.trogiare.service.EmailService;
import com.trogiare.service.GcsService;
import com.trogiare.utils.IdUtil;
import com.trogiare.utils.TokenUtil;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import com.trogiare.validate.ImageValidate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserCtrl {
    static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);
    @Value("${app.path.save.image-avatar}")
    String PATH_IMAGE_AVATAR;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GcsService gcsService;
    @Autowired
    private EmailService mailSender;
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private ObjectMediaRepo objectMediaRepo;

    @Autowired
    private UserRoleRepo userRoleRepo;
    @Autowired
    private CompressFileComponent compressFileComponent;

    @Authorization("ADMIN")
    @RequestMapping(path = "", method = RequestMethod.GET)
    @ApiOperation(value = "get ALl user", response = MessageResp.class)
    public HttpEntity<?> getAll(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        if (page == null || page <= 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = Constants.ITEM_PER_PAGE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAll(pageable);
        List<User> userList = users.getContent();
        Map<String, UserResp> userRespMap = userList.stream()
                .collect(Collectors.toMap(
                        user -> user.getId(), // key mapper
                        user -> {
                            UserResp userResp = new UserResp();
                            userResp.setUser(user);
                            return userResp;
                        }// value mapper

                ));
        List<String> userIds = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
        List<UserRole> userRoleList = userRoleRepo.findByUserIdList(userIds);
        for (UserRole x : userRoleList) {
            userRespMap.get(x.getUserId()).getRoles().add(x.getRoleName());
        }
        List<UserResp> listUserResp = new ArrayList<>(userRespMap.values());
        return ResponseEntity.ok(MessageResp.page(users, listUserResp));
    }

    @RequestMapping(path = "/addRole", method = RequestMethod.POST)
    @ApiOperation(value = "add Role to user", response = MessageResp.class)
    public HttpEntity<?> addRoleForUser(@Valid @RequestBody UserAddRolePayload payload) {
        UserUtil.checkAuthorize("ADMIN");
        Optional<User> userOp = userRepo.findById(payload.getUserId());
        if (!userOp.isPresent()) {
            throw new BadRequestException(ErrorCodesEnum.INVALID_UID);
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(payload.getUserId());
        userRole.setRoleName(payload.getRole().name());
        userRoleRepo.save(userRole);
        return ResponseEntity.ok().body(MessageResp.ok());
    }

    @RequestMapping(path = "/get-user-by-id/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "get user by id", response = MessageResp.class)
    public HttpEntity<Object> getById(@PathVariable("uid") String uid, HttpServletRequest request) {
        if (ValidateUtil.isEmpty(uid)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        Optional<User> opUser = userRepo.findById(uid);
        if (!opUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        String authority = Constants.getAuthority(request);
        User user = opUser.get();
        UserResp userResp = new UserResp();
        userResp.setUser(user);
        String path = objectMediaRepo.getPathImageAvatarFromObjectId(uid, ObjectMediaRefValueEnum.AVATAR.name());
        userResp.setAvatar(new StringBuilder(authority).append("/" + path).toString());
        return ResponseEntity.ok(MessageResp.ok(userResp));
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    @ApiOperation(value = "update user", response = MessageResp.class)
    public HttpEntity<Object> updateUser(@Valid @ModelAttribute UserFormPayload payload) throws IOException {
        String uid = UserUtil.getUserId();
        Optional<User> userOp = userRepo.findById(uid);
        FileSystem fileSystem = new FileSystem();
        if (!ValidateUtil.isEmpty(payload.getAvatar()) && ImageValidate.isImage(payload.getAvatar())) {
            String pathImageAvatar = objectMediaRepo.getPathImageAvatarFromObjectId(uid, ObjectMediaRefValueEnum.AVATAR.name());
            if(pathImageAvatar != null){
                gcsService.deleteFile(pathImageAvatar);
            }
            String path = new StringBuilder(PATH_IMAGE_AVATAR).append("/" + TokenUtil.generateToken(20) + uid).toString();
            fileSystem = gcsService.storeImage(compressFileComponent.compressImage(payload.getAvatar(), 1.5f), path);
            ObjectMedia objectMedia = new ObjectMedia();
            objectMedia.setObjectType(ObjectTypeEnum.USER.name());
            objectMedia.setMediaId(fileSystem.getId());
            objectMedia.setRefType(ObjectMediaRefValueEnum.AVATAR.name());
            objectMedia.setObjectId(uid);
            fileSystemRepo.save(fileSystem);
            objectMediaRepo.save(objectMedia);
        }
        User user = userOp.get();
        user.setLastName(payload.getLastName());
        user.setUpdatedTime(LocalDateTime.now());
        user.setFirstName(payload.getFirstName());
        userRepo.save(user);
        return ResponseEntity.ok().body(MessageResp.ok());
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.PUT)
    @ApiOperation(value = "User do change their password", response = MessageResp.class)
    public HttpEntity<Object> changePassword(@Valid @RequestBody ChangePassword payload) {
        if (payload.getPassword().equals(payload.getNewPassword())) {
            throw new InputInvalidException("passsword can't equals new Password");
        }
        if (!payload.getNewPassword().equals(payload.getReTypePassword())) {
            throw new InputInvalidException("passsword not equals repassword");
        }
        String userId = UserUtil.getUserId();
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            logger.info("NOT valid user ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_USER_ID));
        }
        User user = userOpt.get();
        boolean isValidPassword = passwordEncoder.matches(payload.getPassword(), user.getPassword());
        if (!isValidPassword) {
            logger.info("NOT valid password");
            throw new InputInvalidException("password can't not equals password before");
        }
        user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(MessageResp.ok());
    }

}
