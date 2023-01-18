package com.trogiare.exception;

public class SendMailVerificationFailException extends RuntimeException {

    String error;

    public SendMailVerificationFailException(String message) {
        super(message);
    }

    public SendMailVerificationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SendMailVerificationFailException(String error, String message) {
        super(message);
        this.error = error;
    }
}
