package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.component.ListRoleUserComponent;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.User;
import com.trogiare.model.UserRole;
import com.trogiare.payload.user.ChangePassword;
import com.trogiare.payload.user.UserAddRolePayload;
import com.trogiare.payload.user.UserFormPayload;
import com.trogiare.repo.UserRepo;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.UserResp;
import com.trogiare.service.EmailService;
import com.trogiare.service.GcsService;
import com.trogiare.utils.TokenUtil;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private ListRoleUserComponent userRoles;
    @Autowired
    private UserRoleRepo userRoleRepo;

    @RequestMapping(path = "", method = RequestMethod.GET)

    public HttpEntity<?> getAll(@RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        UserUtil.checkAuthorize("ADMIN");
        if (page == null || page <= 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = Constants.ITEM_PER_PAGE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAll(pageable);
        List<User> userList = users.getContent();
        List<UserResp> listUserResp = new ArrayList<>();
        for (User x : userList) {
            UserResp userResp = new UserResp();
            userResp.setUser(x);
            userResp.setRoles(userRoles.getRole(x.getId()).stream().map(userRole -> userRole.getRoleName()).collect(Collectors.toList()));
            listUserResp.add(userResp);
        }

        return ResponseEntity.ok(MessageResp.page(users, listUserResp));
    }

    @RequestMapping(path = "/addRole", method = RequestMethod.POST)
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
        userRoles.addRole(userRole);
        return ResponseEntity.ok().body(MessageResp.ok());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Object> getById(@PathVariable("id") String id,HttpServletRequest request) {
        if (ValidateUtil.isEmpty(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        Optional<User> opUser = userRepo.findById(id);
        if (!opUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_ID));
        }
        String authority = Constants.getAuthority(request);
        User user = opUser.get();
        UserResp userResp = new UserResp();
        userResp.setUser(user);
        userResp.setAvatar(new StringBuilder(authority).append("/").append(user.getAvatar()).toString());
        return ResponseEntity.ok(MessageResp.ok(userResp));
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public HttpEntity<Object> updateUser(@Valid @ModelAttribute UserFormPayload payload) throws IOException {
        String uid = UserUtil.getUserId();
        Optional<User> userOp = userRepo.getUserExists(payload.getUsername(), payload.getSdt(), payload.getEmail());
        User user = userOp.get();
        if (!user.getId().equals(uid)) {
            if (user.getEmail().equals(payload.getEmail())) {
                throw new BadRequestException(ErrorCodesEnum.EMAIL_EXIST);
            }
            if (user.getSdt().equals(payload.getSdt())) {
                throw new BadRequestException(ErrorCodesEnum.SDT_EXIST);
            }
            if (user.getUsername().equals(payload.getUsername())) {
                throw new BadRequestException(ErrorCodesEnum.USERNAME_EXIST);
            }
        }
        userOp = userRepo.findById(uid);
        FileSystem fileSystem = new FileSystem();
        if (payload.getAvatar() != null) {
            String path = new StringBuilder(PATH_IMAGE_AVATAR).append("/" + TokenUtil.generateToken(20)).toString();
            fileSystem = gcsService.storeFile(payload.getAvatar(), path);
        }
        user = userOp.get();
        user.setSdt(payload.getSdt());
        user.setLastName(payload.getLastName());
        user.setUpdatedTime(LocalDateTime.now());
        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setFirstName(user.getFirstName());
        user.setAvatar(fileSystem.getPath());
        userRepo.save(user);
        return ResponseEntity.ok().body(MessageResp.ok());
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.PUT)
    public HttpEntity<Object> changePassword(@Valid @RequestBody ChangePassword payload) {
        String uid = UserUtil.getUserId();
        Optional<User> userOpt = userRepo.findById(uid);
        if (!userOpt.isPresent()) {
            logger.info("NOT valid user ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_UID));
        }
        if (!payload.getRePassword().equals(payload.getRePassword())) {
            logger.info("Password and repassword not equal");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.REPASSWORD_NOT_EQUALS_PASSWORD));
        }
        User user = userOpt.get();
        boolean isValidPassword = passwordEncoder.matches(payload.getPassword(), user.getPassword());
        if (!isValidPassword) {
            logger.info("NOT valid password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.PASSWORD_HAS_BEEN_USED_BEFORE));
        }
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(MessageResp.ok());
    }

}
