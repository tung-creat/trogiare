package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum PostDirectionHouseEnum {

    NORTH("Bắc"),SOUTH("Nam"),EAST("Đông"),WEST("Tây"),
    NORTHEAST("Đông Bắc"),SOUTHEAST("Đông Nam"),
    NORTHWEST("Tây Bắc"),SOUTHWEST("Tây Nam");
    private String value;
    private static Map<String,PostDirectionHouseEnum> map = new HashMap<>();
     PostDirectionHouseEnum(String value){
        this.value = value;
    }
    String getValue(){
         return this.value;
    }
    public static PostDirectionHouseEnum getEnum(String name){
         if(map == null || map.size() == 0){
             return map.get(name.toUpperCase());
         }
         for(PostDirectionHouseEnum x : PostDirectionHouseEnum.values()){
             map.put(x.name(),x);
         }
         return map.get(name.toUpperCase());
    }

}
