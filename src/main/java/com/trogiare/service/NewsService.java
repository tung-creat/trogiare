package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.ErrorCodesEnum;
import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.controller.NewsCtrl;
import com.trogiare.exception.BadRequestException;
import com.trogiare.model.FileSystem;
import com.trogiare.model.News;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.UserToken;
import com.trogiare.model.impl.PostIddAndPathImages;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.NewsRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.UserUtil;
import com.trogiare.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        FileSystem fileSystemImage = gcsService.storeFile(payload.getImageAvatar(), path);
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
            FileSystem fileSystemImage = gcsService.storeFile(payload.getImageAvatar(), path);
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
        objectMediaRepo.delete(objectMedia);
        fileSystemRepo.delete(fileSystem);
        return MessageResp.ok();
    }


}
