package com.trogiare.utils;


import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.User;
import com.trogiare.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class UserUtil {

    static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

    public static Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    public static UserPrincipal getPrincipal() {
        Authentication auth = getAuth();
        if (auth != null) {
            Object u = auth.getPrincipal();
            if (u instanceof UserPrincipal || u instanceof User) {
                return (UserPrincipal) auth.getPrincipal();
            }
        }
        return null;
    }

    public static String getUserId() {
        User u = getCurrentUser();
        if (u != null) {
            return u.getId();
        }
        return "";
    }

    public static String getUserName() {
        User u = getCurrentUser();
        if (u != null) {
            return u.getUsername();
        }
        return "";
    }
    public static String getName() {
        User u = getCurrentUser();
        String name = "";
        if (u != null) {
            if (!StringUtils.isEmpty(u.getFirstName())){
                name = u.getFirstName() ;
            } else {
                name = u.getFirstName() + " " + u.getLastName();
            }
        }
        return name;
    }

    public static List<String> getUserRoles() {
        Authentication au = getAuth();
        if (au == null)
            return null;

        return au.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toList());
    }
    public static Boolean checkAuthorize(String ... roles){
        for(String x : roles){
          if(getUserRoles().contains(x)){
            return true;
          }
        }
        throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
    }
    public static Boolean checkAuthorize(Boolean check ){
        if(check) {
            return true;
        }else{
            throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
        }

    }

    public static User getCurrentUser() {
        return getPrincipal();
    }
}
