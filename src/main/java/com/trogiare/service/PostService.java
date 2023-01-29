package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.component.GoogleFileManager;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.Address;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.Post;
import com.trogiare.model.impl.PostAndAddress;
import com.trogiare.model.impl.PostIddAndImages;
import com.trogiare.payload.PostPayload;
import com.trogiare.repo.AddressRepo;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.repo.PostRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.PostResp;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.ValidateUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private  PostRepo postRepo;
    @Value("${app.path.save.image-post}")
    private String PATH_IMAGE_FILE_POST;
    @Autowired
    private GoogleFileManager googleFileManager;
    @Autowired
    private FileSystemRepo fileSystemRepo;

    @Autowired
    private ObjectMediaRepo objectMediaRepo;
    @Autowired
    private AddressRepo addressRepo;
    private final String PATH_GET_IMAGE = "/image/";

    @Transactional
    public MessageResp savePost(PostPayload payload,String uid) {
        Address address = new Address();
        address.setAddressDetails(payload.getAddressDetails());
        address.setVillage(payload.getVillage());
        address.setDistrict(payload.getDistrict());
        address.setProvince(payload.getProvince());
        address =addressRepo.save(address);
        Post post = new Post();
        post.setInformationFromPayLoad(payload);
        post.setCreatedTime(LocalDateTime.now());
        post.setUpdatedTime(LocalDateTime.now());
        post.setAddressId(address.getId());
        post.setOwnerId(uid);
        post = postRepo.save(post);
        saveImagesPost(payload, post);
        return MessageResp.ok(post);
    }

    public MessageResp getPosts(HttpServletRequest request ,Integer size , Integer page) throws URISyntaxException {
      String URI_AUTHORITY = Constants.getAuthority(request);
        Pageable pageable = PageRequest.of(page,size);
        List<PostAndAddress> postAndAddressList = postRepo.getPosts(pageable);
        Map<String,PostResp> postRespMap = new HashMap<>();
        List<String> postIds = new ArrayList<>();
        for(PostAndAddress x : postAndAddressList){
            PostResp postResp = new PostResp();
            postResp.setPost(x.getPost());
            postResp.setAddress(x.getAddress());
            postRespMap.put(postResp.getId(),postResp);
            postIds.add(postResp.getId());
        }
        List<PostIddAndImages> postIdAndImageNameList = objectMediaRepo.getImagesByPostIds(postIds,ObjectMediaRefValueEnum.IMAGE_POST.name());
        Map<String,String> ImageMap = new HashMap<>();
        for(PostIddAndImages x : postIdAndImageNameList){
            ImageMap.put(x.getPostId(),x.getImageName());
        }
        for(var x : ImageMap.entrySet()){
            StringBuilder nameImage = new StringBuilder(x.getValue());
            nameImage.insert(0,PATH_GET_IMAGE);
            nameImage.insert(0,URI_AUTHORITY);
            ImageMap.put(x.getKey(),nameImage.toString());
        }
        for(var x : postRespMap.entrySet()){
            for(var y : ImageMap.entrySet()){
                if(x.getKey().equals(y.getKey())){
                   x.getValue().setImage(y.getValue());
                }
            }
        }
        List<PostResp> result = new ArrayList<PostResp>(postRespMap.values());
        return MessageResp.ok(result);

    }
    public MessageResp getPostById(HttpServletRequest request,String postId){
        Optional<PostAndAddress> postAndAddressOp = postRepo.getPostById(postId);
        String URI_AUTHORITY = Constants.getAuthority(request);
        if(!postAndAddressOp.isPresent()){
            throw new BadRequestException(ErrorCodesEnum.NOT_FOUND_POST);
        }
        PostAndAddress postAndAddress = postAndAddressOp.get();
        PostResp postResp = new PostResp();
        postResp.setPost(postAndAddress.getPost());
        postResp.setAddress(postAndAddress.getAddress());
        List<PostIddAndImages> postIddAndImagesList = objectMediaRepo.getImagesByPostIds(List.of(postId),null);
        List<String> imageDetails = new ArrayList<>();
        for(PostIddAndImages x : postIddAndImagesList){
            if(x.getTypeImage().equals(ObjectMediaRefValueEnum.IMAGE_POST.name())){
                StringBuilder nameImage = new StringBuilder(x.getImageName());
                nameImage.insert(0,PATH_GET_IMAGE);
                nameImage.insert(0,URI_AUTHORITY);
                postResp.setImage(nameImage.toString());
            }else{
                StringBuilder nameImage = new StringBuilder(x.getImageName());
                nameImage.insert(0,PATH_GET_IMAGE);
                nameImage.insert(0,URI_AUTHORITY);
                imageDetails.add(nameImage.toString());
            }
        }
        postResp.setImageDetails(imageDetails);
        return MessageResp.ok(postResp);
    }


    private void saveImagesPost(PostPayload payload, Post post) {
        List<FileSystem> fileSystems = new ArrayList<>();
        List<ObjectMedia> listObjectMedia = new ArrayList<>();
        FileSystem fileSystem = googleFileManager.uploadFile(payload.getImage(), PATH_IMAGE_FILE_POST, HandleStringAndNumber.removeAccent(payload.getName()));
        ObjectMedia objectMedia = new ObjectMedia();
        objectMedia.setMediaId(fileSystem.getId());
        objectMedia.setObjectId(post.getId());
        objectMedia.setObjectType(ObjectTypeEnum.POST.name());
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
                objectMedia.setObjectType(ObjectTypeEnum.POST.name());
                objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_DETAILS_POST.name());
                listObjectMedia.add(objectMedia);
            }
        }
        fileSystemRepo.saveAll(fileSystems);
        objectMediaRepo.saveAll(listObjectMedia);
    }

}