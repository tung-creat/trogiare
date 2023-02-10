package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.payload.news.DeleteFormNewsPayload;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.security.UserPrincipal;
import com.trogiare.service.GcsService;
import com.trogiare.service.NewsService;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/news")
public class NewsCtrl {
    static final  Logger logger = LoggerFactory.getLogger(NewsCtrl.class);
    @Autowired
    private GcsService gcsService;
    @Autowired
    private NewsService newsService;
    @Value("${app.path.save.image-blogs}")
    private String PATH_IMAGE_BLOGS;

    @Transactional
    @RequestMapping(path = "/upload-image-blog", method = RequestMethod.POST)
    @ApiOperation(value = "upload image blog then get url image", response = MessageResp.class)
    public HttpEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("nameBlog") String nameBlog, HttpServletRequest request) throws IOException {
        if (!(file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) ||
                file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE))) {
            throw new BadRequestException(ErrorCodesEnum.INVALID_FILE);
        }
        StringBuilder x = new StringBuilder(Constants.getAuthority(request));
        String pathFile = PATH_IMAGE_BLOGS +"/"+  HandleStringAndNumber.removeAccent(nameBlog);
        FileSystem fileSystem = gcsService.storeFile(file,pathFile);
        x.append("/");
        x.append(fileSystem.getPath());
        return ResponseEntity.ok().body(x.toString());
    }
    @RequestMapping(path="",method = RequestMethod.POST)
    @ApiOperation(value = "add news ", response = MessageResp.class)
    public HttpEntity<?> addNews(@Valid @ModelAttribute NewsPayload newsPayload) throws IOException {
        UserUtil.checkAuthorize("ADMIN","WRITER");
        MessageResp messageResp = newsService.addNews(newsPayload);
        return ResponseEntity.ok(messageResp);
    }
    @RequestMapping(path="",method = RequestMethod.PUT)
    @ApiOperation(value = "update news ",response = MessageResp.class)
    public HttpEntity<?> updateNews(@Valid @ModelAttribute NewsPayload newsPayload,HttpServletRequest request) throws IOException {
        UserUtil.checkAuthorize("ADMIN","WRITER");
        MessageResp messageResp = newsService.updateNews(newsPayload,request);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="",method = RequestMethod.DELETE)
    @ApiOperation(value = "delete news ",response = MessageResp.class)
    public HttpEntity<?> deleteNews(@Valid @RequestBody DeleteFormNewsPayload payload){
        MessageResp messageResp = newsService.deletedNews(payload.getNewsId());
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/get-news-by-id/{newsId}",method =RequestMethod.GET)
    @ApiOperation(value ="get news by id",response = MessageResp.class)
    public HttpEntity<?> getNewsById(@PathVariable(value ="newsId") String newsId,HttpServletRequest request){
        MessageResp messageResp = newsService.getNewsById(newsId,request);
        return ResponseEntity.ok().body(messageResp);
    }
    @RequestMapping(path="/filter",method = RequestMethod.GET)
    @ApiOperation(value ="get all news and filter",response = MessageResp.class)
    public HttpEntity<?> getAllNews(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) LocalDate timeStart,
                                    @RequestParam(required = false) LocalDate timeEnd,
                                    @RequestParam(required = false) String topic,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = false) Integer page,
                                    HttpServletRequest request){
        if(page == null){
            page =0;
        }
        if(size == null){
            size =  Constants.ITEM_PER_PAGE;
        }
        MessageResp messageResp = newsService.getAllNewsByFilter(keyword,timeStart,timeEnd,topic,size,page,request);
        return  ResponseEntity.ok().body(messageResp);
    }


}
