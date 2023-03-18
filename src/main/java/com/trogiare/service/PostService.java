package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.*;
import com.trogiare.component.CompressFileComponent;
import com.trogiare.component.PostCodeComponent;
import com.trogiare.exception.BadRequestException;
import com.trogiare.exception.InputInvalidException;
import com.trogiare.model.*;
import com.trogiare.model.impl.PostAndAddress;
import com.trogiare.model.impl.ObjectIddAndPathImages;
import com.trogiare.payload.post.PostPayload;
import com.trogiare.repo.*;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.PostResp;
import com.trogiare.respone.UserResp;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    @Autowired
    private  PostRepo postRepo;
    @Value("${app.path.save.image-post}")
    private String PATH_IMAGE_FILE_POST;

    @Autowired
    private GcsService gcsService;
    @Autowired
    private ProvincesRepo provincesRepo;
    @Autowired
    private FileSystemRepo fileSystemRepo;

    @Autowired
    private ObjectMediaRepo objectMediaRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private PostCodeComponent postCodeComponent;
    @Autowired
    private CompressFileComponent compressFileComponent;
    @Autowired
    private UserRepo userRepo;


    @Transactional
    public MessageResp savePost(PostPayload payload,String uid) throws IOException {
        List<Object[]> addressObject = provincesRepo.getDetailAddress(payload.getProvinceId(),payload.getDistrictId(),payload.getVillageId());
        if(addressObject == null || addressObject.size() < 0){
            throw new InvalidPropertiesFormatException("Address inputed not valid");
        }
        Address address = new Address();
        address.setAddressDetails(payload.getAddressDetails());
        address.setVillage((String) addressObject.get(0)[2]);
        address.setDistrict((String) addressObject.get(0)[1]);
        address.setProvince((String) addressObject.get(0)[0]);
        address =addressRepo.save(address);
        Post post = new Post();
        post.setInformationFromPayLoad(payload);
        post.setStatus(PostStatusEnum.PUBLIC);
        post.setCreatedTime(LocalDateTime.now());
        post.setUpdatedTime(LocalDateTime.now());
        post.setExpirationDate(LocalDateTime.now().plusDays(10));
        post.setAddressId(address.getId());
        post.setOwnerId(uid);
        post.setPostCode(postCodeComponent.getCode());
        post = postRepo.save(post);
        saveImagesPost(payload, post);
        return MessageResp.ok(post);
    }
    @Transactional
    public MessageResp updatePost(PostPayload payload){
        String userUtil = UserUtil.getUserId();
        Optional<Post> postOp = postRepo.findById(payload.getPostId());
        if(ValidateUtil.isEmpty(payload.getPostId())){
            throw new InputInvalidException("post Id is not null");
        }
        if(!postOp.isPresent()){
            throw new BadRequestException(ErrorCodesEnum.NOT_FOUND_POST);
        }
        if(!postOp.get().getOwnerId().equals(userUtil)){
            throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
        }
        Post post = postOp.get();
        post.setInformationFromPayLoad(payload);
        post = postRepo.save(post);

        return null;


    }

    public MessageResp getPosts(HttpServletRequest request , Integer size,
                                Integer page, String address, Long priceMin, Long priceMax, String keyword,
                                Long areaMin, Long areaMax, Long bedRoom, PostTypeEnum type,TypeRealEstateEnum typeRealEstate
                                ) throws URISyntaxException {
      String URI_AUTHORITY = Constants.getAuthority(request);
        Pageable pageable = PageRequest.of(page,size, Sort.by("price"));
       Page<PostAndAddress> postAndAddressPage = postRepo.getPosts(pageable,address,priceMin,priceMax,keyword,areaMin,areaMax,bedRoom,type,typeRealEstate == null ?null :typeRealEstate.name(),PostStatusEnum.PUBLIC);
        List<PostAndAddress>  postAndAddressList =  postAndAddressPage.getContent();
       Map<String,PostResp> postRespMap = new HashMap<>();
        List<String> postIds = new ArrayList<>();
        for(PostAndAddress x : postAndAddressList){
            PostResp postResp = new PostResp();
            postResp.setPost(x.getPost());
            postResp.setAddress(x.getAddress());
            postRespMap.put(postResp.getId(),postResp);
            postIds.add(postResp.getId());
        }
        List<ObjectIddAndPathImages> postIdAndImageNameList = objectMediaRepo.getImagesByObjectIds(postIds,ObjectMediaRefValueEnum.IMAGE_POST.name());
        Map<String,String> ImageMap = new HashMap<>();
        for(ObjectIddAndPathImages x : postIdAndImageNameList){
            ImageMap.put(x.getObjectId(),x.getPath());
            System.out.println(x.getTypeImage());
        }
        for(Map.Entry<String, String> x : ImageMap.entrySet()){
            StringBuilder nameImage = new StringBuilder(x.getValue());
            if(nameImage.toString().startsWith("/images")){
                nameImage.insert(0,URI_AUTHORITY+"/trogiare");
            }else{
                nameImage.insert(0,URI_AUTHORITY+"/");
            }

            ImageMap.put(x.getKey(),nameImage.toString());
        }
        for(Map.Entry<String,PostResp> x : postRespMap.entrySet()){
            for( Map.Entry<String,String> y : ImageMap.entrySet()){
                if(x.getKey().equals(y.getKey())){
                   x.getValue().setImage(y.getValue());
                }
            }
        }
        List<PostResp> result = new ArrayList<PostResp>(postRespMap.values());
        return MessageResp.page(postAndAddressPage,result);

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
        List<ObjectIddAndPathImages> postIddAndImagesList = objectMediaRepo.getImagesByObjectIds(List.of(postId),null);
        Optional<User> userOp = userRepo.findById(postResp.getOwnerId());
        if(!userOp.isPresent()){
            throw new BadRequestException("not found user");
        }
        User user = userOp.get();
        UserResp userResp = new UserResp();
        userResp.setUser(user);
        List<ObjectIddAndPathImages> userIdAndPathImage= objectMediaRepo.getImagesByObjectIds(List.of(user.getId()),ObjectMediaRefValueEnum.AVATAR.name());
        if(userIdAndPathImage != null && userIdAndPathImage.size() >=1){
            userResp.setAvatar(handelConvertLinkImage(URI_AUTHORITY,userIdAndPathImage.get(0)));
        }
        postResp.setUser(userResp);
        List<String> imageDetails = new ArrayList<>();
        for(ObjectIddAndPathImages x : postIddAndImagesList){
            if(x.getTypeImage().equals(ObjectMediaRefValueEnum.IMAGE_POST.name())){
                postResp.setImage(handelConvertLinkImage(URI_AUTHORITY,x));
            }else{
                imageDetails.add(handelConvertLinkImage(URI_AUTHORITY,x));
            }
        }
        postResp.setImageDetails(imageDetails);
        return MessageResp.ok(postResp);

    }
    public MessageResp deletePostByIds(List<String> postIds){
        String userId = UserUtil.getUserId();
        List<String> userIdsOfListProduct = postRepo.checkUserIdByListPostId(postIds);
        if(userIdsOfListProduct.size() >1){
            throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
        }
        if(!userIdsOfListProduct.get(0).equals(userId)){
            throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
        }
        postRepo.deletePostByListId(postIds);
        List<String> pathList = objectMediaRepo.getPathImageFromObjectid(postIds);
        for(String path :pathList){
            try{
                gcsService.deleteFile(path);
            }catch(Exception ex){
                continue;
            }
        }
        objectMediaRepo.deleteObjectMediaAndMediaByObjectIds(postIds);
        return MessageResp.ok();

    }


    private void saveImagesPost(PostPayload payload, Post post) throws IOException {
        List<FileSystem> fileSystems = new ArrayList<>();
        List<ObjectMedia> listObjectMedia = new ArrayList<>();
        String path = new StringBuilder(PATH_IMAGE_FILE_POST).append("/" +HandleStringAndNumber.removeAccent(payload.getName())).toString();
        FileSystem fileSystem = gcsService.storeImage(compressFileComponent.compressImage(payload.getImage(),2f),path);
        ObjectMedia objectMedia = new ObjectMedia();
        objectMedia.setMediaId(fileSystem.getId());
        objectMedia.setObjectId(post.getId());
        objectMedia.setObjectType(ObjectTypeEnum.POST.name());
        objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_POST.name());
        fileSystems.add(fileSystem);
        listObjectMedia.add(objectMedia);

        if (payload.getImagesDetails() != null && payload.getImagesDetails().size() >0) {
            for (MultipartFile multipartFile : payload.getImagesDetails()) {
                path = new StringBuilder(PATH_IMAGE_FILE_POST).append("/" +HandleStringAndNumber.removeAccent(payload.getName())).toString();
                fileSystem = gcsService.storeImage(compressFileComponent.compressImage(multipartFile,1.5f),path);
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
    private String handelConvertLinkImage(String URI_AUTHORITY,ObjectIddAndPathImages x){
        StringBuilder nameImage = new StringBuilder(x.getPath());
        if(nameImage.toString().startsWith("/images")){
            nameImage.insert(0,URI_AUTHORITY+"/trogiare");
        }else{
            nameImage.insert(0,URI_AUTHORITY+"/");
        }
        return nameImage.toString();
    }

}
