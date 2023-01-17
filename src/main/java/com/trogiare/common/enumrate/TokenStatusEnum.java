package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum TokenStatusEnum {
    WAITING("WATTING"),ACCEPTED("ACCEPTED"),EXPIRED("EXPIRED"),DISABLED("DISABLE");
    String value;

    private static Map<String, TokenStatusEnum> map = new HashMap<String, TokenStatusEnum>();
    TokenStatusEnum(String value) {
        this.value = value;
    }
    public static TokenStatusEnum getUserTokenType(String value){
        if(!map.isEmpty()){
            return map.get(value.toUpperCase());
        }
        for(TokenStatusEnum u: TokenStatusEnum.values()){
            map.put(u.name(),u);
        }
        return map.get(value.toUpperCase());
    }
}
