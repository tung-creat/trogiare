package com.trogiare.respone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class MessageResp  implements Serializable {
    private static final long serialVersionUID = -40943944218239318L;
    static final String SUCCESS = "SUCCESS";
    String responseCode = "";
    String responseDesc = "";
    Object result = null;
    Long total = null;
    Integer size = null;
    Integer page = null;
    Integer totalPage = null;
    LocalDateTime localDateTime = LocalDateTime.now();
    public MessageResp(){
        super();
    }
    public static MessageResp ok(String result){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResponseDesc("");
        messageResp.setResult(result);
        return messageResp;
    }
    public static MessageResp ok(){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResponseDesc("");
        return messageResp;
    }
    public static MessageResp ok(Object object){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResponseDesc("");
        messageResp.setResult(object);
        return messageResp;
    }
    public static MessageResp page(Page page){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResult(page.getContent());
        messageResp.setPage(page.getNumber());
        messageResp.setSize(page.getSize());
        messageResp.setTotal(page.getTotalElements());
        messageResp.setTotalPage(page.getTotalPages());
        return messageResp;
    }
    public static MessageResp page(Page page,Object object){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResult(object);
        messageResp.setPage(page.getNumber());
        messageResp.setSize(page.getSize());
        messageResp.setTotal(page.getTotalElements());
        messageResp.setTotalPage(page.getTotalPages());
        return messageResp;
    }
    public static MessageResp page(Page page, List listContent){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(SUCCESS);
        messageResp.setResult(listContent);
        messageResp.setPage(page.getNumber());
        messageResp.setSize(page.getSize());
        messageResp.setTotal(page.getTotalElements());
        messageResp.setTotalPage(page.getTotalPages());
        return messageResp;
    }
    public static  MessageResp error(ErrorCodesEnum errorCodesEnum){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(errorCodesEnum.name());
        messageResp.setResponseDesc(errorCodesEnum.toString());
        return messageResp;

    }
    public static  MessageResp error(String codeError,String desc){
        MessageResp messageResp = new MessageResp();
        messageResp.setResponseCode(codeError);
        messageResp.setResponseDesc(desc);
        return messageResp;
    }
}
