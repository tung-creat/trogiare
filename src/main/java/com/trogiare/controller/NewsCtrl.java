package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.security.UserPrincipal;
import com.trogiare.service.GcsService;
import com.trogiare.service.NewsService;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/blogs")
public class NewsCtrl {
    static final Logger logger = LoggerFactory.getLogger(NewsCtrl.class);
    @Autowired
    private GcsService gcsService;
    @Autowired
    private NewsService newsService;
    @Value("${app.path.save.image-blogs}")
    private String PATH_IMAGE_BLOGS;

    @Transactional
    @RequestMapping(path = "/upload-image-blog", method = RequestMethod.POST)
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
    public HttpEntity<?> addNews(@ModelAttribute NewsPayload newsPayload,HttpServletRequest request) throws IOException {
        UserUtil.checkAuthorize("ADMIN","WRITER");
        MessageResp messageResp = newsService.addNews(newsPayload,request);
        return ResponseEntity.ok(messageResp);
    }

}
