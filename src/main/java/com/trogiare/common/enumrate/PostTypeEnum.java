package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum PostTypeEnum {
    RENT,SELL;
    private static Map<String,PostTypeEnum> map = new HashMap<>();
    public static PostTypeEnum getEnum(String value){
        if(value ==null){
            return null;
        }
        if(map.size() != 0){
            return map.get(value.toUpperCase());
        }
        for(PostTypeEnum x : PostTypeEnum.values()){
            map.put(x.name(),x);
        }
        return map.get(value.toUpperCase());
    }
}
