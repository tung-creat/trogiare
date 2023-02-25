package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.payload.PostPayload;
import com.trogiare.respone.MessageResp;
import com.trogiare.service.PostService;
import com.trogiare.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostCtrl {
    private static final Logger logger = LoggerFactory.getLogger(PostCtrl.class);
    @Autowired
    private PostService postService;

    @RequestMapping(path="",method = RequestMethod.POST)
    @ApiOperation(value = "save post", response = MessageResp.class)
    public HttpEntity<?> savePost(@ModelAttribute PostPayload payload) throws IOException {
        logger.info("type bat dong san " +payload.getTypeRealEstate().name());
        String uid = UserUtil.getUserId();
        MessageResp messageResp = postService.savePost(payload,uid);
        return ResponseEntity.ok().body(messageResp);
    }

    @RequestMapping(path="/filter",method = RequestMethod.GET)
    @ApiOperation(value = "get all product and filter", response = MessageResp.class)
    public HttpEntity<?> getAllPost(@RequestParam(required = false) Integer page ,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = false,name = "type") String type,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String address,
                                    @RequestParam(required = false) Long priceMin,
                                    @RequestParam(required = false) Long priceMax,
                                    @RequestParam(required = false) Long areaMin,
                                    @RequestParam(required = false) Long areaMax,
                                    @RequestParam(required = false) Long bedRoom,
                                    HttpServletRequest request) throws URISyntaxException {
        if(page == null){
            page = Constants.DEFAULT_PAGE;
        }
        if(size == null){
            size = Constants.ITEM_PER_PAGE;
        }

        MessageResp messageResp = postService.getPosts(request,size,page,address,priceMin,priceMax,keyword,areaMin,areaMax,bedRoom,PostTypeEnum.getEnum(type));
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/get-post-by-id/{postId}",method = RequestMethod.GET)
    @ApiOperation(value = "Get Product By Id", response = MessageResp.class)
    public HttpEntity<?> getPostById(@PathVariable(value="postId") String postId,HttpServletRequest request){
        MessageResp messageResp = postService.getPostById(request,postId);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/delete-post-by-id/{postId}",method = RequestMethod.PUT)
    @ApiOperation(value = "Delete Product", response = MessageResp.class)
    public HttpEntity<?> deletePostById(@PathVariable(value="postId") String postId){
        MessageResp messageResp = postService.deletePostById(postId);
        return ResponseEntity.ok().body(messageResp);
    }
}
