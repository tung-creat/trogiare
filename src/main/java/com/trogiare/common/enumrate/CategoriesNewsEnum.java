package com.trogiare.common.enumrate;

import java.util.HashMap;
import java.util.Map;

public enum CategoriesNewsEnum {
    REAL_ESTATE_NEWS("tin-tuc-bat-dong-san"),
    REAL_ESTATE_KNOWLEDGE("kien-thuc-bds"),
    FENG_SHUI("phong-thuy"),
    ADVICE("loi-khuyen"),
    HOUSING_LAW("luat-nha-dat"),
    PROJECT("du-an"),
    REVIEW("review"),
    TIPS("meo-vat"),
    OTHER("khac");
    private static Map<String, CategoriesNewsEnum> map = new HashMap<String, CategoriesNewsEnum>();
    String value;

    CategoriesNewsEnum(String value) {
        this.value = value;
    }

    public static CategoriesNewsEnum getEnum(String value) {
        if(value == null){
            return null;
        }
        if (!map.isEmpty()) {
            return map.get(value.toLowerCase());
        }
        for (CategoriesNewsEnum u : CategoriesNewsEnum.values()) {
            map.put(u.value.toLowerCase(), u);
        }
        return map.get(value.toLowerCase());
    }

}
