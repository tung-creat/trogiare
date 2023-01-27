package com.trogiare.controller;

import com.trogiare.component.GoogleFileManager;
import com.trogiare.model.FileSystem;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.respone.MessageResp;
import jakarta.mail.Message;
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
    private GoogleFileManager googleFileManager;
    @RequestMapping(path="/image/{nameFile}",method = RequestMethod.GET)
        public HttpEntity<?> getImage(@PathVariable(name="nameFile") String nameFile) throws GeneralSecurityException, IOException {
        Optional<FileSystem> fileSystemOp=fileSystemRepo.findByName(nameFile);
        if(!fileSystemOp.isPresent()){
            return ResponseEntity.ok().body(MessageResp.ok());
        }
        FileSystem fileSystem = fileSystemOp.get();
        String hash = fileSystem.getHash();
        InputStream result = googleFileManager.downloadFile(hash);
        byte[] allBytes = result.readAllBytes();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(allBytes);
    }
}
