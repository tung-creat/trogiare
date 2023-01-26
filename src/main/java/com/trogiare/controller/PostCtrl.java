package com.trogiare.controller;

import com.trogiare.payload.PostPayload;
import com.trogiare.respone.MessageResp;
import com.trogiare.service.PostService;
import com.trogiare.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post")
public class PostCtrl {
    @Autowired
    private PostService postService;
    @RequestMapping(path="",method = RequestMethod.POST)
    public HttpEntity<?> savePost(@ModelAttribute PostPayload payload){
        String uid = UserUtil.getUserId();
        MessageResp messageResp = postService.savePost(payload,uid);
        return ResponseEntity.ok().body(messageResp);
    }
}
