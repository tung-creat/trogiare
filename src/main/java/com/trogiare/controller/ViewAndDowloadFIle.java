package com.trogiare.controller;

import com.trogiare.model.FileSystem;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.service.GcsService;
import jakarta.mail.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Optional;

@RestController
public class ViewAndDowloadFIle {
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private GcsService gcsService;

    @Transactional
    @RequestMapping(path="/trogiare/image/{typeImage}/{nameFile}",method = RequestMethod.GET)
//
        public HttpEntity<?> getImage(HttpServletRequest request) throws GeneralSecurityException, IOException {
        System.out.println(request.getServletPath());
        String pathImage = request.getServletPath().substring(1);
        Optional<FileSystem> fileSystemOp=fileSystemRepo.findByPath(pathImage);
        if(!fileSystemOp.isPresent()){
            return ResponseEntity.ok().body(MessageResp.ok());
        }
        FileSystem fileSystem = fileSystemOp.get();
        String hash = fileSystem.getPath();
        byte[] result = gcsService.downloadFile(hash);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(result);

    }
}
