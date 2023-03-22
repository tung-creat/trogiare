package com.trogiare.controller;

import com.trogiare.exception.InputInvalidException;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.service.GcsService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@RestController
public class ViewAndDowloadFIle {
    static Logger logger = LoggerFactory.getLogger(ViewAndDowloadFIle.class);
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private GcsService gcsService;

    @Transactional
    @RequestMapping(path = "/trogiare/images/**", method = RequestMethod.GET)
    @ApiOperation(value = "get image ",response = MessageResp.class)
    public HttpEntity<?> getImage(HttpServletRequest request) throws IOException {
        System.out.println(request.getServletPath());
        String pathImage = request.getServletPath().substring(1);
        logger.info(pathImage);
        byte[] result;
        try {
            result = gcsService.downloadFile(pathImage);
        } catch (NullPointerException ex) {
            String path = pathImage.substring(pathImage.indexOf("/images"));
            System.out.println(path);
            URL url = new URL("https://cloud.mogi.vn" + path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(url.openStream().readAllBytes());
            result = baos.toByteArray();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(result);
    }
    @Transactional
    @RequestMapping(path = "/trogiare/news/**", method = RequestMethod.GET)
    @ApiOperation(value = "get image ",response = MessageResp.class)
    public HttpEntity<?> getFile(HttpServletRequest request) throws IOException {
        System.out.println(request.getServletPath());
        String path = request.getServletPath().substring(1);
        logger.info(path);
        byte[] result;
        try {
            result = gcsService.downloadFile(path);

        } catch (Exception ex) {
            throw new InputInvalidException("not found file");
        }
        String resultText = new String(result, StandardCharsets.UTF_8);
        return ResponseEntity.ok().body(resultText);
    }

}
