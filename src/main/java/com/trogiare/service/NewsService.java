package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.component.CompressFileComponent;
import com.trogiare.dto.NewsDto;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.News;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.impl.ObjectIddAndPathImages;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.NewsRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.respone.NewsResp;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewsService {
    static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    @Autowired
    private NewsRepo newsRepo;
    @Value("${app.path.save.image-blogs}")
    private String PATH_IMAGE_BLOGS;
    @Autowired
    private ObjectMediaRepo objectMediaRepo;
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private GcsService gcsService;
    @Autowired
    private CompressFileComponent compressFileComponent;


    @Transactional
    public MessageResp addNews(NewsPayload payload) throws IOException {
        String userId = UserUtil.getUserId();
        if (payload.getImageAvatar() == null) {
            throw new BadRequestException(ErrorCodesEnum.NOT_NULL_FILE_IMAGE);
        }
        String path = PATH_IMAGE_BLOGS + "/" + HandleStringAndNumber.removeAccent(payload.getTitle());
        News news = new News();
        news.setStatusNews(NewsStatusEnum.PUBLIC);
        news.setTitle(payload.getTitle());
        news.setMetaTitle(payload.getMetaTitle());
        news.setContent(payload.getContent());
        news.setTopic(payload.getTopic());
        news.setAuthorId(userId);
        news.setCreatedTime(LocalDateTime.now());
        news.setUpdatedTime(LocalDateTime.now());
        news.setShortDescription(payload.getShortDescription());
        news = newsRepo.save(news);
        FileSystem fileSystemImage = gcsService.storeImage(compressFileComponent.compressImage(payload.getImageAvatar()), path);
        ObjectMedia objectMediaImage = new ObjectMedia();
        objectMediaImage.setObjectId(news.getId());
        objectMediaImage.setMediaId(fileSystemImage.getId());
        objectMediaImage.setObjectType(ObjectTypeEnum.NEWS.name());
        objectMediaImage.setRefType(ObjectMediaRefValueEnum.IMAGE_NEWS.name());
        objectMediaRepo.save(objectMediaImage);
        fileSystemRepo.save(fileSystemImage);
        return MessageResp.ok();
    }

    @Transactional
    public MessageResp updateNews(NewsPayload payload, HttpServletRequest request) throws IOException {
        if (payload.getId() == null) {
            throw new BadRequestException(ErrorCodesEnum.INVALID_ID);
        }
        String userID = UserUtil.getUserId();
        Optional<News> newsOp = newsRepo.findByAuthorIdAndId(userID, payload.getId());
        if (!newsOp.isPresent()) {
            throw new BadRequestException(ErrorCodesEnum.INVALID_NEWS);
        }
        News news = newsOp.get();
        news.setStatusNews(NewsStatusEnum.PUBLIC);
        news.setTitle(payload.getTitle());
        news.setMetaTitle(payload.getMetaTitle());
        news.setContent(payload.getContent());
        news.setTopic(payload.getTopic());
        news.setUpdatedTime(LocalDateTime.now());
        news.setShortDescription(payload.getShortDescription());
        news = newsRepo.save(news);
        if (ValidateUtil.isNotEmpty(payload.getLinkImageBeDelete())) {
            String authority = Constants.getAuthority(request);
            logger.info(authority);
            logger.info(payload.getLinkImageBeDelete());
            String pathImage = payload.getLinkImageBeDelete().replace(authority + "/","");
            logger.info(pathImage);
            gcsService.deleteFile(pathImage);
            FileSystem fileSystem = fileSystemRepo.findByPath(pathImage).get();
            ObjectMedia objectMedia = objectMediaRepo.findByMediaId(fileSystem.getId()).get();
            fileSystemRepo.delete(fileSystem);
            objectMediaRepo.delete(objectMedia);

        }
        if (payload.getImageAvatar() != null) {
            String path = PATH_IMAGE_BLOGS + "/" + HandleStringAndNumber.removeAccent(payload.getTitle());
            FileSystem fileSystemImage = gcsService.storeImage(compressFileComponent.compressImage(payload.getImageAvatar()), path);
            ObjectMedia objectMediaImage = new ObjectMedia();
            objectMediaImage.setObjectId(news.getId());
            objectMediaImage.setMediaId(fileSystemImage.getId());
            objectMediaImage.setObjectType(ObjectTypeEnum.NEWS.name());
            objectMediaImage.setRefType(ObjectMediaRefValueEnum.IMAGE_NEWS.name());
            objectMediaRepo.save(objectMediaImage);
            fileSystemRepo.save(fileSystemImage);
        }

        return MessageResp.ok();
    }
//    public MessageResp getNewsById(String newsId){
//        Str
//    }
    @Transactional
    public MessageResp deletedNews(String newsId) {
        String uid = UserUtil.getUserId();
        Optional<News> newsOp = newsRepo.findById(newsId);
        if (!newsOp.isPresent()) {
            throw new BadRequestException(ErrorCodesEnum.NOT_FOUND_NEWS);
        }
        News news = newsOp.get();
        if (!news.getAuthorId().equals(uid)) {
            throw new BadRequestException(ErrorCodesEnum.ACCESS_DENIED);
        }
        newsRepo.delete(news);
        ObjectMedia objectMedia = objectMediaRepo.findByObjectId(newsId).get();
        FileSystem fileSystem = fileSystemRepo.findById(objectMedia.getMediaId()).get();
        gcsService.deleteFile(fileSystem.getPath());
        objectMediaRepo.delete(objectMedia);
        fileSystemRepo.delete(fileSystem);
        return MessageResp.ok();
    }
    public MessageResp getNewsById(String newId,HttpServletRequest request){
        Optional<News> newsOp = newsRepo.findById(newId);
        if(!newsOp.isPresent()){
            throw new BadRequestException(ErrorCodesEnum.INVALID_NEWS);
        }
        News news = newsOp.get();
        System.out.println(news.getContent());
        List<ObjectIddAndPathImages> objectIddAndPathImages = objectMediaRepo.getImagesByObjectIds(
                List.of(news.getId())
                ,ObjectMediaRefValueEnum.IMAGE_NEWS.name());
        StringBuilder  uriAuthority =new StringBuilder(Constants.getAuthority(request));
        uriAuthority.append("/"+objectIddAndPathImages.get(0).getPath());
        NewsResp newsResp = new NewsResp(news);
        newsResp.setImageAvatar(uriAuthority.toString());
        return MessageResp.ok(newsResp);
    }
    public MessageResp getAllNewsByFilter(String keyword, LocalDate timeStart, LocalDate timeEnd, String topic,
                                          Integer size, Integer page, HttpServletRequest request)
    {
        Pageable pageable = PageRequest.of(page,size);
        String  uriAuthority =Constants.getAuthority(request);
        Page<NewsDto> listNewsPage = newsRepo.getAllNewsByParams(pageable,keyword,timeStart,timeEnd,topic);
        if(listNewsPage.getTotalElements() == 0){
            return MessageResp.ok();
        }
        List<NewsDto> listNews = listNewsPage.getContent();
        List<String> newsIdList = new ArrayList<>();
        Map<String,NewsResp> mapNews = new HashMap<>();
        for(NewsDto x : listNews){
            newsIdList.add(x.getId());
            NewsResp newsResp = new NewsResp(x);
            mapNews.put(x.getId(),newsResp);
        }

        List<ObjectIddAndPathImages> ObjectIddAndPathImagesList =objectMediaRepo.getImagesByObjectIds(newsIdList,ObjectMediaRefValueEnum.IMAGE_NEWS.name());
        for(ObjectIddAndPathImages x : ObjectIddAndPathImagesList){
            for(Map.Entry<String,NewsResp> y : mapNews.entrySet()){
                if(y.getKey().equals(x.getObjectId())){
                    StringBuilder uriAuthorityStringBuilder  = new StringBuilder(uriAuthority);
                    uriAuthorityStringBuilder.append("/"+x.getPath());
                    y.getValue().setImageAvatar(uriAuthorityStringBuilder.toString());
                }
            }
        }
        List<NewsResp> newsRespList = new ArrayList<>();
        for(Map.Entry<String,NewsResp> x : mapNews.entrySet()){
            newsRespList.add(x.getValue());
        }
        return MessageResp.page(listNewsPage,newsRespList);
    }

}
