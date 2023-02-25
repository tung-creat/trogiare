package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum PostTypeReftValueEnum {
    HOUSE("Nhà"),VILLA("Biệt thự"),APARTMENT("Căn hộ"),
    MOTEL_ROOM("Nhà trọ"),OFFICE("Văn Phòng"),STREET_HOUSE("Nhà mặt phố"),
    COMMERCIAL_TOWNHOUSES("Nhà phố thương mại"),
    GROUND("Mặt bằng"),
    OTHER("Bất động sản khác");

    String value;
    private static Map<String,PostTypeReftValueEnum> map = new HashMap<String,PostTypeReftValueEnum>();
    PostTypeReftValueEnum(String value) {
        this.value = value;
    }
    public static PostTypeReftValueEnum getUserStatus(String value){
        if(!map.isEmpty()){
            return map.get(value.toUpperCase());
        }
        for(PostTypeReftValueEnum u: PostTypeReftValueEnum.values()){
            map.put(u.name(),u);
        }
        return map.get(value.toUpperCase());
    }
}
