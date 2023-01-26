package com.trogiare.common.enumrate;

import lombok.ToString;


public enum ErrorCodesEnum {
    REPASSWORD_NOT_EQUALS_PASSWORD("Repassword not equals passsword"),
    EMAIL_EXIST("Email exist"),
    SDT_EXIST("Sdt exist"),
    INVALID_TOKEN("invalid token"),
    FIND_NOT_FOUND("Not found email"),
    TOKEN_EXPIRED("Token is expired"),
    AUTHENTICATION_REQUEST_EXCEEDED("AUTHENTICATION REQUEST EXCEEDED"),

    SAVE_FILE_NOT_SUCCESS("save file not success"),
    EMAIL_NOT_VERIFIED("email not verified"),
    INFORMATION_USER_NOT_VALID("Thông tin tài khoản hoặc mật khẩu không chính xác"),
    ACCOUNT_NOT_ACTIVATED("Tài khoản chưa được kích hoạt"),
    EMAIL_DOES_NOT_EXISTED("EMAIL_DOES_NOT_EXISTED"),
    INVALID_INPUT_PARAMETER("INVALID_INPUT_PARAMETER"),
    INVALID_ID("Invalid id"),
    ACCOUNT_NOT_EXIST("Tài khoản không tồn tại"),
    INVALID_INPUT("invalid input"),
    INVALID_UID("invalid uid"),
    ACCESS_DENIED("access denied"),
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
