package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum UserStatusEnum {
    DISABLE("disable"),DELETED("deleted"),PENDING("pending"),ACTIVATED("activated");
    String value;
    private static Map<String,UserStatusEnum>  map = new HashMap<String,UserStatusEnum>();
    UserStatusEnum(String value) {
        this.value = value;
    }
    public static UserStatusEnum getUserStatus(String value){
        if(!map.isEmpty()){
            return map.get(value.toUpperCase());
        }
        for(UserStatusEnum u: UserStatusEnum.values()){
            map.put(u.name(),u);
        }
        return map.get(value.toUpperCase());
    }
}
