package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.model.ConverStation;
import com.trogiare.model.Message;
import com.trogiare.repo.ConverStationRepo;
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
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/messsages")
public class MessageCtrl {
    @Autowired
    ConverStationRepo converStationRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;

    @RequestMapping(path = "/get-converstation", method = RequestMethod.GET)
    @ApiOperation(value = "get converstation of user", response = MessageResp.class)
    public HttpEntity<?> getAllMessage(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size, HttpServletRequest request) {
        String uid = UserUtil.getUserId();
        String URI_AUTHORITY = Constants.getAuthority(request);
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size < 0) {
            size = Constants.ITEM_PER_PAGE;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ConverStation> converStationsPage = converStationRepo.findByParticipantsContaining(uid, pageable);
        if (converStationsPage == null) {
            return ResponseEntity.ok().body(MessageResp.ok());
        }
        List<ConverStation> converStationList = converStationsPage.getContent();
        List<String> messageIdList = new ArrayList<>();
        for (ConverStation x : converStationList) {
            messageIdList.add(x.getIdLastMessage());
        }
        List<Message> messageList = messageRepo.findAllByIdInOrderByTimestampDesc(messageIdList);
        if (messageList == null) {
            return ResponseEntity.ok().body(MessageResp.ok());
        }
        List<String> listUidConverstation = new ArrayList<>();
        for (Message x : messageList) {
            if (!uid.equals(x.getUidSender())) {
                listUidConverstation.add(x.getUidSender());
            } else {
                listUidConverstation.add(x.getUidRecipient());
            }
        }
        List<Object[]> objects = userRepo.getInformationUserByListUid(listUidConverstation);
        Map<String, ConverStationMessageResp> converStationMessageRespMap = new HashMap<>();
        for (Object[] x : objects) {
            ConverStationMessageResp converStationMessageResp = new ConverStationMessageResp();
            converStationMessageResp.setUserId((String) x[0]);
            String firstName = (String) x[1];
            String lastName = (String) x[2];
            converStationMessageResp.setFullName(firstName + " " + lastName);
            if (x[3] != null) {
                StringBuilder pathImage = new StringBuilder((String) x[3]);
                converStationMessageResp.setAvatarImage(pathImage.insert(0, URI_AUTHORITY + "/").toString());
            }
            converStationMessageRespMap.put((String) x[0], converStationMessageResp);
        }
        List<ConverStationMessageResp> converStationMessageRespMapResult = new ArrayList<>();
        for (Message x : messageList) {
            if (!x.getUidSender().equals(uid)) {
                converStationMessageRespMap.get(x.getUidSender()).setMessageList(List.of(x));
                converStationMessageRespMap.get(x.getUidSender()).setConverStationId(x.getConversationId());
                converStationMessageRespMapResult.add(converStationMessageRespMap.get(x.getUidSender()));
            } else {
                converStationMessageRespMap.get(x.getUidRecipient()).setMessageList(List.of(x));
                converStationMessageRespMap.get(x.getUidRecipient()).setConverStationId(x.getConversationId());
                converStationMessageRespMapResult.add(converStationMessageRespMap.get(x.getUidRecipient()));
            }
        }
        return ResponseEntity.ok().body(MessageResp.page(converStationsPage, converStationMessageRespMapResult));
    }

    @RequestMapping(path = "/get-converstation-by-user-id/{uidConnect}", method = RequestMethod.GET)
    @ApiOperation(value = "get conberstation by user id", response = MessageResp.class)
    public HttpEntity<?> getConverstationDetail(@RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @PathVariable("uidConnect") String uidConnect, HttpServletRequest request) {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size < 0) {
            size = Constants.ITEM_MESSAGE;
        }
        String uid = UserUtil.getUserId();
        String URI_AUTHORITY = Constants.getAuthority(request);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Message> messagePage = messageRepo.findLatestMessages(uid, uidConnect, pageable);
        List<Message> messageList = messagePage.getContent();
        List<Object[]> objects = userRepo.getInformationUserByListUid(List.of(uidConnect));
        ConverStationMessageResp converStationMessageResp = new ConverStationMessageResp();
        for (Object[] x : objects) {
            converStationMessageResp.setUserId((String) x[0]);
            String firstName = (String) x[1];
            String lastName = (String) x[2];
            converStationMessageResp.setFullName(firstName + " " + lastName);
            if (x[3] != null) {
                StringBuilder pathImage = new StringBuilder((String) x[3]);
                converStationMessageResp.setAvatarImage(pathImage.insert(0, URI_AUTHORITY + "/").toString());
            }
        }
        List<Message> result = new ArrayList<>();
        for (int i = messageList.size() - 1; i >= 0; i--) {
            messageList.get(i).setIsRead(true);
            result.add(messageList.get(i));
        }
        messageRepo.saveAll(result);
        converStationMessageResp.setMessageList(result);
        converStationMessageResp.setConverStationId(result.get(0).getConversationId());
        return ResponseEntity.ok().body(MessageResp.page(messagePage, converStationMessageResp));

    }

}
