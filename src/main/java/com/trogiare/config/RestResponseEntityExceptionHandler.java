package com.trogiare.config;

import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.exception.SendMailVerificationFailException;
import com.trogiare.respone.MessageResp;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("unexpect exception ", ex);
        return ResponseEntity.internalServerError()
                .body(MessageResp.error(ErrorCodesEnum.INVALID_INPUT_PARAMETER.name(), ex.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        log.error("org.springframework.security.access.AccessDeniedException exception ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessageResp.error(ErrorCodesEnum.ACCESS_DENIED));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadExceptions(BadRequestException ex) {
        MessageResp errorResponse = new MessageResp();
        errorResponse.setResponseCode(ex.getError());
        errorResponse.setResponseDesc(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(MessageResp.error(ErrorCodesEnum.INVALID_INPUT_PARAMETER));
    }

    @ExceptionHandler({JDBCException.class})
    public ResponseEntity<Object> handleJDBCException(JDBCException ex) {
        MessageResp errorResponse = new MessageResp();
        errorResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
        SQLException sqlException = ex.getSQLException();
        if (sqlException instanceof SQLIntegrityConstraintViolationException) {
            errorResponse.setResponseDesc("CONSTRAINT_VIOLATION");
        } else {
            errorResponse.setResponseDesc(ex.getMessage());
        }
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({com.trogiare.exception.InputInvalidException.class})
    public ResponseEntity<Object> handleInputInvalidException(com.trogiare.exception.InputInvalidException ex) {
        MessageResp errorResponse = new MessageResp();
        errorResponse.setResponseCode(ErrorCodesEnum.INVALID_INPUT_PARAMETER.name());
        errorResponse.setResponseDesc(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({SendMailVerificationFailException.class})
    public ResponseEntity<Object> handleSendMailVerificationFailException(SendMailVerificationFailException ex) {
        MessageResp errorResponse = new MessageResp();
        errorResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
        errorResponse.setResponseDesc(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": Try again!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    // handle invalid payload
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String desc = null;
        if(ex.getBindingResult() != null
                && ex.getBindingResult().getFieldErrors() != null
                && !ex.getBindingResult().getFieldErrors().isEmpty()){
            FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
            desc = String.format(fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResp.error(ErrorCodesEnum.INVALID_INPUT_PARAMETER.name(), desc));
    }



}
