package com.trogiare.payload.user;

import com.trogiare.common.enumrate.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserAddRolePayload {
    @NotBlank
    private UserRoleEnum role;
    @NotBlank
    private String userId;

}
