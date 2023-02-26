package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum TypeRealEstateEnum {
    HOUSE("Nhà"),
    VILLA("Biệt thự"),
    APARTMENT("Căn hộ"),
    MOTEL_ROOM("Nhà trọ"),
    OFFICE("Văn Phòng"),
    STREET_HOUSE("Nhà mặt phố"),
    COMMERCIAL_TOWNHOUSES("Nhà phố thương mại"),
    GROUND("Mặt bằng"),
    OTHER("Bất động sản khác");
    String value;
    private static Map<String,TypeRealEstateEnum> map = new HashMap<>();
    TypeRealEstateEnum(String value){
        this.value = value;
    }
    public static TypeRealEstateEnum getEnum(String value){
        if(map.size() != 0){
            return map.get(value.toUpperCase());
        }
        for(TypeRealEstateEnum x : TypeRealEstateEnum.values()){
            map.put(x.name().toLowerCase(),x);
        }
        return map.get(value.toLowerCase());
    }
}
