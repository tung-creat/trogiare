package com.trogiare.common.enumrate;

import lombok.ToString;


public enum ErrorCodesEnum {
    INVALID_USER_ID("INVALID USER ID"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    REPASSWORD_NOT_EQUALS_PASSWORD("Repassword not equals passsword"),
    EMAIL_EXIST("Email exist"),
    SDT_EXIST("Sdt exist"),
    INVALID_TOKEN("invalid token"),
    FIND_NOT_FOUND("Not found email"),
    TOKEN_EXPIRED("Token is expired"),
    AUTHENTICATION_REQUEST_EXCEEDED("AUTHENTICATION REQUEST EXCEEDED"),
    PASSWORD_HAS_BEEN_USED_BEFORE("password has been used before"),
    PASSWORD_AND_REPASSWORD_NOT_EQUALS("password and repassword not equal"),
    SAVE_FILE_NOT_SUCCESS("save file not success"),
    EMAIL_NOT_VERIFIED("email not verified"),
    INFORMATION_USER_NOT_VALID("Thông tin tài khoản hoặc mật khẩu không chính xác"),
    ACCOUNT_NOT_ACTIVATED("Tài khoản chưa được kích hoạt"),
    EMAIL_DOES_NOT_EXISTED("EMAIL_DOES_NOT_EXISTED"),
    INVALID_INPUT_PARAMETER("INVALID_INPUT_PARAMETER"),
    INVALID_ID("Invalid id"),
    NOT_NULL_FILE_IMAGE("not null file image"),
    INVALID_NEWS("invalid news"),
    NOT_FOUND_POST("not found post"),
    ACCOUNT_NOT_EXIST("Tài khoản không tồn tại"),
    INVALID_INPUT("invalid input"),
    INVALID_UID("invalid uid"),
    ACCESS_DENIED("access denied"),
    INVALID_FILE("file invalid"),
    USERNAME_EXIST("Username exist"),
    NOT_FOUND_NEWS("not found news");

    private final String desc;
    ErrorCodesEnum(final String desc) {
        this.desc = desc;
    }
    @Override
    public String toString(){
        return desc;
    }
}
