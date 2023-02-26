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

    private static Map<String, TypeRealEstateEnum> map = new HashMap<>();
    static {
        for (TypeRealEstateEnum type : TypeRealEstateEnum.values()) {
            map.put(type.value.toLowerCase(), type);
        }
    }

    private final String value;

    TypeRealEstateEnum(String value) {
        this.value = value;
    }

    public static TypeRealEstateEnum valueOfIgnoreCase(String value) {
        if(value == null){
            return null;
        }
        return map.get(value.toLowerCase());
    }

    public String getValue() {
        return value;
    }
}
