package com.trogiare.controller;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.component.GoogleFileManager;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogCtrl {
    static final Logger logger = LoggerFactory.getLogger(BlogCtrl.class);
    @Autowired
    private GoogleFileManager googleFileManager;
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Value("${app.path.save.image-blogs}")
    private String PATH_IMAGE_BLOGS;
    @Transactional
    @RequestMapping(path = "/upload-image-blog", method = RequestMethod.POST)
    @ApiOperation(value = "Save Image to blog", response = MessageResp.class)
    public HttpEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name-blog") String nameBlog, HttpServletRequest request) throws IOException {
        if(!(file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) || file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE))){
            throw new BadRequestException(ErrorCodesEnum.INVALID_FILE);
        }
        FileSystem fileSystem =googleFileManager.uploadFile(file,PATH_IMAGE_BLOGS,nameBlog);
        fileSystemRepo.save(fileSystem);
        String authority = Constants.getAuthority(request);
        String result = new StringBuilder(authority).append(Constants.PATH_GET_IMAGE).append(fileSystem.getName()).toString();
        return ResponseEntity.ok().body(result);
    }
}
