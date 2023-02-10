package com.trogiare.service;

import com.google.cloud.storage.*;
import com.trogiare.component.ConvertByteToMB;
import com.trogiare.model.FileSystem;
import com.trogiare.security.LocalTokenProvider;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.IdUtil;
import com.trogiare.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class GcsService {
    static final Logger logger = LoggerFactory.getLogger(GcsService.class);
    @Autowired
    private Storage storage;
    @Autowired
    private ConvertByteToMB convertByteToMB;
    @Value("${app.google-cloud-storage.bucket}")
    private String BUCKKET_NAME;
    public FileSystem storeFile(MultipartFile file, String path) throws IOException {
        path = path+TokenUtil.generateToken(10) + ".png";
        BlobId blobId = BlobId.of(BUCKKET_NAME,path );
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        FileSystem fileSystem = new FileSystem();
        fileSystem.setId(IdUtil.generate());
        fileSystem.setSize(String.valueOf(convertByteToMB.getSize(file.getSize())));
        fileSystem.setPath(path);
        fileSystem.setCreatedTime(LocalDateTime.now());
        fileSystem.setType(file.getContentType());
        return fileSystem;
    }

    public byte[] downloadFile( String path) {
        BlobId blobId = BlobId.of(BUCKKET_NAME, path);
        Blob blob = storage.get(blobId);
        byte[] result = blob.getContent();
        return result;
    }
    public void deleteFile(String path){
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(BUCKKET_NAME,path);
        storage.delete(blobId);
         logger.info("File " + path + " was deleted from bucket " + BUCKKET_NAME);
    }
}
