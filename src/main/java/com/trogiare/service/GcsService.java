package com.trogiare.service;

import com.google.cloud.storage.*;
import com.trogiare.component.ConvertByteToMB;
import com.trogiare.model.FileSystem;
import com.trogiare.utils.IdUtil;
import com.trogiare.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GcsService {
    static final Logger logger = LoggerFactory.getLogger(GcsService.class);
    @Autowired
    private Storage storage;
    @Autowired
    private ConvertByteToMB convertByteToMB;
    @Value("${app.google-cloud-storage.bucket}")
    private String BUCKKET_NAME;

    public FileSystem storeImage(byte [] file, String path) throws IOException {
        path = path+TokenUtil.generateToken(10) + ".jpeg";
        BlobId blobId = BlobId.of(BUCKKET_NAME, path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo,file);
        FileSystem fileSystem = new FileSystem();
        fileSystem.setId(IdUtil.generate());
        fileSystem.setSize(String.valueOf(convertByteToMB.getSize(file.length)));
        fileSystem.setPath(path);
        fileSystem.setCreatedTime(LocalDateTime.now());
        fileSystem.setType("img/jpeg");
        return fileSystem;
    }
    public  String storeFileNewsWithHtml(String html,String path) throws IOException {
        path = path+TokenUtil.generateToken(10) + ".html";
        BlobId blobId = BlobId.of(BUCKKET_NAME,path);
        // Create a BlobInfo object to specify the metadata for the file
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/html").build();

        // Read the contents of the HTML file into a string
        String htmlContents = html;
        storage.create(blobInfo, htmlContents.getBytes());
        return path;

    }

    public byte[] downloadFile( String path) {
        BlobId blobId = BlobId.of(BUCKKET_NAME, path);
        Blob blob = storage.get(blobId);
        byte[] result = blob.getContent();
        return result;
    }
    public void deleteFile(String path){
        BlobId blobId = BlobId.of(BUCKKET_NAME,path);
        storage.delete(blobId);
         logger.info("File " + path + " was deleted from bucket " + BUCKKET_NAME);
    }

}
