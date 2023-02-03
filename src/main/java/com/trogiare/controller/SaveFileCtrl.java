package com.trogiare.controller;

import com.trogiare.respone.MessageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
public class SaveFileCtrl {
    private static final Logger logger = LoggerFactory.getLogger(SaveFileCtrl.class);

    @Value("${app.path.save.image-post}")
    private String PATH_FILE_PRODUCT;
    @RequestMapping(value="/api/v1/file",method = RequestMethod.POST)
    public HttpEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        return null;
    }
}
