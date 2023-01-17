package com.trogiare.common.enumrate;

import lombok.ToString;


public enum ErrorCodesEnum {
    REPASSWORD_NOT_EQUALS_PASSWORD("Repassword not equals passsword"),
    EMAIL_EXIST("Email exist"),
    SDT_EXIST("Sdt exist"),
    USERNAME_EXIST("Username exist");

    private final String desc;
    ErrorCodesEnum(final String desc) {
        this.desc = desc;
    }
    @Override
    public String toString(){
        return desc;
    }
}
