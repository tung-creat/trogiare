package com.trogiare.security;

import com.trogiare.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenObject {
    String userName;
    String fullName;
    List<UserRole> roles;
}
