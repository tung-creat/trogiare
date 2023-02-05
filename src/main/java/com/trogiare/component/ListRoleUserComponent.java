package com.trogiare.component;

import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.UserRole;
import com.trogiare.repo.UserRoleRepo;
import com.trogiare.respone.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Component
public class ListRoleUserComponent {
    @Autowired
    private  UserRoleRepo userRoleRepo;
    private static  Map<String, List<UserRole>> userRoleMap = new HashMap<>();
    private static LocalDateTime timeStart ;
    private final  Long TIME_EXPIRATION = 5l;
    public List<UserRole> getRole(String userId){
        checkTimeValid();
        List<UserRole> userRoles = userRoleMap.get(userId);
        if(userRoles != null && userRoles.size() > 0){
            return userRoles;
        }
        userRoles = userRoleRepo.findByUserId(userId);
        if(userRoles == null || userRoles.isEmpty()){
            throw new BadRequestException(ErrorCodesEnum.INVALID_UID);
        }
        userRoleMap.put(userId,userRoles);
        return userRoles;
    }
    public void addRole(UserRole userRole){
        if(!(userRoleMap == null || userRoleMap.isEmpty())){
            List<UserRole> x = userRoleMap.get(userRole.getUserId());
            if(!(x == null || x.isEmpty())){
                x.add(userRole);
                userRoleMap.put(userRole.getUserId(),x);
            }
        }
    }
    private void checkTimeValid(){
        if(timeStart == null){
            timeStart = LocalDateTime.now();

        }else{
            if(ChronoUnit.HOURS.between(timeStart, LocalDateTime.now()) >= TIME_EXPIRATION){
                userRoleMap.clear();
                timeStart = LocalDateTime.now();
            }
        }
    }
}
