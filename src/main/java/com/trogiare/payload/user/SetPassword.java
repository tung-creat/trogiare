package com.trogiare.payload.user;

import jakarta.validation.constraints.NotBlank;


public class SetPassword {
    @NotBlank(message="User ID can not be blank")
    String uid;

    @NotBlank(message="Token can not be blank")
    String token;

    @NotBlank(message="Password can not be blank")
    String yourPassword;

    @NotBlank(message="Retype Password can not be blank")
    String retypePassword;

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public String getYourPassword() {
        return yourPassword;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setYourPassword(String yourPassword) {
        this.yourPassword = yourPassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

}
