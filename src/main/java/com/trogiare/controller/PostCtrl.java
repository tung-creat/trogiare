package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.payload.PostPayload;
import com.trogiare.respone.MessageResp;
import com.trogiare.service.PostService;
import com.trogiare.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostCtrl {
    @Autowired
    private PostService postService;
    @RequestMapping(path="",method = RequestMethod.POST)
    public HttpEntity<?> savePost(@ModelAttribute PostPayload payload){
        String uid = UserUtil.getUserId();
        MessageResp messageResp = postService.savePost(payload,uid);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="",method = RequestMethod.GET)
    public HttpEntity<?> getAllPost(@RequestParam(required = false) Integer page , @RequestParam(required = false) Integer size, HttpServletRequest request) throws URISyntaxException {
        if(page == null){
            page = Constants.DEFAULT_PAGE;
        }
        if(size == null){
            size = Constants.ITEM_PER_PAGE;
        }
        MessageResp messageResp = postService.getPosts(request,size,page);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/get-post-by-id/{postId}")
    public HttpEntity<?> getPostById(@PathVariable(value="postId") String postId,HttpServletRequest request){
        MessageResp messageResp = postService.getPostById(request,postId);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/delete-post-by-id/{postId}")
    public HttpEntity<?> deletePostById(@PathVariable(value="postId") String postId){
        MessageResp messageResp = postService.deletePostById(postId);
        return ResponseEntity.ok().body(messageResp);
    }
}
