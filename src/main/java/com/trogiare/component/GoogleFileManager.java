package com.trogiare.component;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.trogiare.model.FileSystem;
import com.trogiare.security.LocalTokenAuth;
import com.trogiare.utils.TokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleFileManager {
    static final Logger logger = LoggerFactory.getLogger(GoogleFileManager.class);

    @Autowired
    private GoogleDriveConfig googleDriveConfig;

    // Get all file
    public List listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriveConfig.getInstance().files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();
    }

    // Get all folder
    public List listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)") // get field of google drive folder
                .execute();
        return result.getFiles();
    }

    // Download file by id
    public InputStream downloadFile(String id) throws IOException, GeneralSecurityException {
        if (id != null) {
           return googleDriveConfig.getInstance().files()
                    .get(id).executeMediaAsInputStream();
        }
        return null;
    }

    // Delete file by id
    public void deleteFileOrFolder(String fileId) throws Exception {
        googleDriveConfig.getInstance().files().delete(fileId).execute();
    }

    // Set permission drive file
    private Permission setPermission(String type, String role) {
        Permission permission = new Permission();
        permission.setType(type);
        permission.setRole(role);
        return permission;
    }


    // Upload file
    @Transactional
    public FileSystem uploadFile(MultipartFile file, String folderName, String name) {
        FileSystem fileSystem = new FileSystem();
        try {
            String folderId = getFolderId(folderName);
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(TokenUtil.generateToken(40));
                File uploadFile = googleDriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();
                fileSystem.setId(TokenUtil.generateToken(36));
//                logger.info("size " + ConvertByteToMB.getSize(uploadFile.getSize()));
//                logger.info("Hash " + uploadFile.getId());
//                logger.info("name " + name + "-" + fileSystem.getId() + "-" + uploadFile.getId());
                fileSystem.setSize(String.valueOf(uploadFile.getSize()));
                fileSystem.setHash(uploadFile.getId());
                StringBuilder sb = new StringBuilder(name);
                sb.append("-" + fileSystem.getId());
                sb.append("-"+uploadFile.getId());
                fileSystem.setName(String.valueOf(sb));
                fileSystem.setCreatedTime(LocalDateTime.now());
                fileSystem.setType(file.getContentType());
                logger.info("Chuyển");
                return fileSystem;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        logger.info("fail");
        return fileSystem;
    }

    // get id folder google drive
    public String getFolderId(String folderName) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");

        Drive driveInstance = googleDriveConfig.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result = null;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }

//    @Override
//    public void run() {
//        logger.info("Hello world");
//    }
}
