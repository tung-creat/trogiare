package com.trogiare.payload.user;

import com.trogiare.common.enumrate.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddRolePayload {
    @NotBlank
    private UserRoleEnum role;
    @NotBlank
    private String userId;

}
