package com.trogiare.service;

import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectMediaTypeEnum;
import com.trogiare.component.GoogleFileManager;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.Post;
import com.trogiare.payload.PostPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.repo.PostRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.utils.HandleStringAndNumber;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;
    @Value("${app.path.save.image-post}")
    private String PATH_IMAGE_FILE_POST;
    @Autowired
    private GoogleFileManager googleFileManager;
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private ObjectMediaRepo objectMediaRepo;

    @Transactional
    public MessageResp savePost(PostPayload payload,String uid) {
        Post post = new Post();
        post.setInformationFromPayLoad(payload);
        post.setCreatedTime(LocalDateTime.now());
        post.setUpdatedTime(LocalDateTime.now());
        post.setOwnerId(uid);
        post = postRepo.save(post);
        saveImagesPost(payload, post);
        return MessageResp.ok(post);
    }

    private void saveImagesPost(PostPayload payload, Post post) {
        List<FileSystem> fileSystems = new ArrayList<>();
        List<ObjectMedia> listObjectMedia = new ArrayList<>();
        FileSystem fileSystem = googleFileManager.uploadFile(payload.getImage(), PATH_IMAGE_FILE_POST, HandleStringAndNumber.removeAccent(payload.getName()));
        ObjectMedia objectMedia = new ObjectMedia();
        objectMedia.setMediaId(fileSystem.getId());
        objectMedia.setObjectId(post.getId());
        objectMedia.setObjectType(ObjectMediaTypeEnum.POST.name());
        objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_POST.name());
        fileSystems.add(fileSystem);
        listObjectMedia.add(objectMedia);

        if (payload.getImagesDetails() != null) {
            for (MultipartFile multipartFile : payload.getImagesDetails()) {
                fileSystem = googleFileManager.uploadFile(multipartFile, PATH_IMAGE_FILE_POST, HandleStringAndNumber.removeAccent(payload.getName()));
                fileSystems.add(fileSystem);
                objectMedia = new ObjectMedia();
                objectMedia.setMediaId(fileSystem.getId());
                objectMedia.setObjectId(post.getId());
                objectMedia.setObjectType(ObjectMediaTypeEnum.POST.name());
                objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_DETAILS_POST.name());
                listObjectMedia.add(objectMedia);
            }
        }
        fileSystemRepo.saveAll(fileSystems);
        objectMediaRepo.saveAll(listObjectMedia);
    }
}
