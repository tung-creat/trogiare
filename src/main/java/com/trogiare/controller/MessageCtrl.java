package com.trogiare.controller;
import com.trogiare.common.Constants;
import com.trogiare.model.Message;
import com.trogiare.repo.MessageRepo;
import com.trogiare.repo.UserRepo;
import com.trogiare.respone.ConverStationMessageResp;
import com.trogiare.respone.MessageResp;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
@RestController
@RequestMapping("/api/v1/messsages")
public class MessageCtrl {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;
    @RequestMapping(path="/get-converstation",method = RequestMethod.GET)
    @ApiOperation(value = "get converstation of user", response = MessageResp.class)
    public HttpEntity<?> getAllMessage(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size, HttpServletRequest request){
        String uid = UserUtil.getUserId();
        String URI_AUTHORITY = Constants.getAuthority(request);
        if(page == null || page < 0){
            page = 0;
        }
        if(size == null || size < 0){
            size = Constants.ITEM_PER_PAGE;
        }
        int skip = page * size;
        List<Message> messageList = messageRepo.findMessagesByUid(uid, skip, size);
//        List<Message> messageList = messagePage.getContent();
        if(ValidateUtil.isEmpty(messageList)){
            return ResponseEntity.ok().body(MessageResp.ok());
        }
        List<String>  listUidConverstation = new ArrayList<>();
        for(Message x : messageList){
            if(uid.equals(x.getUidSender())){
                listUidConverstation.add(x.getUidRecipient());
            }else{
                listUidConverstation.add(x.getUidSender());
            }
        }
        List<Object[]> objects = userRepo.getInformationUserByListUid(listUidConverstation);
        Map<String,ConverStationMessageResp> converStationMessageRespMap = new HashMap<>();
        for(Object[] x : objects){
            ConverStationMessageResp converStationMessageResp = new ConverStationMessageResp();
            converStationMessageResp.setUserId((String) x[0]);
            String firstName = (String) x[1];
            String lastName = (String) x[2];
            converStationMessageResp.setFullName(firstName + " " + lastName);
            if(x[3] != null){
                StringBuilder pathImage = new StringBuilder((String) x[3]);
                converStationMessageResp.setAvatarImage(pathImage.insert(0,URI_AUTHORITY+"/").toString());
            }
            converStationMessageRespMap.put((String) x[0],converStationMessageResp);
        }
        for(Message x : messageList){
            if(!x.getUidSender().equals(uid)){
                converStationMessageRespMap.get(x.getUidSender()).setMessageList(List.of(x));
            }else{
                converStationMessageRespMap.get(x.getUidRecipient()).setMessageList(List.of(x));
            }
        }
        List<ConverStationMessageResp> converStationMessageResps = new ArrayList<>(converStationMessageRespMap.values());
        return ResponseEntity.ok().body(MessageResp.ok(converStationMessageResps));
    }
    @RequestMapping(path= "/get-converstation-by-user-id/{uidConnect}",method = RequestMethod.GET)
    @ApiOperation(value = "get conberstation by user id", response = MessageResp.class)
    public HttpEntity<?> getConverstationDetail(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size,
                                                @PathVariable("uidConnect") String uidConnect, HttpServletRequest request){
        if(page == null || page < 0){
            page = 0;
        }
        if(size == null || size < 0){
            size = Constants.ITEM_MESSAGE;
        }
        String uid = UserUtil.getUserId();
        String URI_AUTHORITY = Constants.getAuthority(request);
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());
        Page<Message> messagePage = messageRepo.findLatestMessages(uid,uidConnect,pageable);
        List<Message> messageList = messagePage.getContent();
        List<Object[]> objects = userRepo.getInformationUserByListUid(List.of(uidConnect));
        ConverStationMessageResp converStationMessageResp = new ConverStationMessageResp();
        for(Object[] x : objects){
            converStationMessageResp.setUserId((String) x[0]);
            String firstName = (String) x[1];
            String lastName = (String) x[2];
            converStationMessageResp.setFullName(firstName + " " + lastName);
            if(x[3] != null){
                StringBuilder pathImage = new StringBuilder((String) x[3]);
                converStationMessageResp.setAvatarImage(pathImage.insert(0,URI_AUTHORITY+"/").toString());
            }
        }
        List<Message> result = new ArrayList<>();
        for(int i  = messageList.size() -1 ; i >= 0 ; i--){
            messageList.get(i).setIsRead(true);
            result.add(messageList.get(i));
        }
        messageRepo.saveAll(result);
        converStationMessageResp.setMessageList(result);
        return ResponseEntity.ok().body(MessageResp.page(messagePage,converStationMessageResp));

    }

}
