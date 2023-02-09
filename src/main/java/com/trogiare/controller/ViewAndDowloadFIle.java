package com.trogiare.controller;

import com.trogiare.repo.FileSystemRepo;
import com.trogiare.service.GcsService;
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

import java.util.Optional;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
public class ViewAndDowloadFIle {
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private GcsService gcsService;

    @Transactional
    @RequestMapping(path="/trogiare/images/**",method = RequestMethod.GET)
//
        public HttpEntity<?> getImage(HttpServletRequest request) throws IOException {
        System.out.println(request.getServletPath());
        String pathImage = request.getServletPath().substring(1);
        byte[] result;
        try{
             result = gcsService.downloadFile(pathImage);
        }catch(NullPointerException ex){
            String path = pathImage.substring(pathImage.indexOf("/images"));
            System.out.println(path);
            URL url = new URL("https://cloud.mogi.vn"+path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(url.openStream().readAllBytes());
            result = baos.toByteArray();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(result);
    }
}
