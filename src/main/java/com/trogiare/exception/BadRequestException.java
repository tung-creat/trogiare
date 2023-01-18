package com.trogiare.exception;

import com.trogiare.common.enumrate.ErrorCodesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    String error;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public BadRequestException(String error, String message) {
        super(message);
        this.error = error;
    }

    public BadRequestException(ErrorCodesEnum errorCodesEnum) {
        super(errorCodesEnum.toString());
        this.error = errorCodesEnum.name();
    }
}