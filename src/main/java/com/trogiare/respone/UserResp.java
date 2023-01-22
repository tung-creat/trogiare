package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserResp implements Serializable {
    private static final long serialVersionUID = -40943944218239318L;
    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String sdt;
    private String status;
    @Column(name ="created_time")
    private LocalDateTime createdTime;
    @Column(name="updated_time")
    private LocalDateTime updatedTime;
    private String token;
    private List<String> roles;
    public void setUser(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.sdt = user.getSdt();
        this.status = user.getStatus();
        this.createdTime = user.getCreatedTime();
        this.updatedTime = user.getUpdatedTime();
    }


}
