package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum UserRoleEnum {
    ADMIN("admin"),
    USER("user"),
    WRITER("writer");
    String value;
    private static Map<String,UserRoleEnum> map = new HashMap<>();
    UserRoleEnum(String value){
        this.value = value;
    }
    public static UserRoleEnum getUserRole(String value){
        if(map.size() > 0){
            return map.get(value.toUpperCase());
        }
        for(UserRoleEnum u: UserRoleEnum.values()){
            map.put(u.name(),u);
        }
        return map.get(value.toUpperCase());
    }
}
