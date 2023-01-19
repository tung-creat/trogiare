package com.trogiare.respone;

import lombok.Data;

@Data
public class UnauthorizedResponse {
    String timestamp;
    Integer status;
    String error;
    String path;
}
