package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.model.FileSystem;
import com.trogiare.model.News;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.UserToken;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.NewsRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.respone.MessageResp;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {
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
    public MessageResp addNews(NewsPayload payload, HttpServletRequest request) throws IOException {
       String userId = UserUtil.getUserId();
       String path = PATH_IMAGE_BLOGS +"/" + HandleStringAndNumber.removeAccent(payload.getTitle());
        News news = new News();
        news.setStatusNews(NewsStatusEnum.PUBLIC);
        news.setTitle(payload.getTitle());
        news.setMetaTitle(payload.getMetaTitle());
        news.setContent(payload.getContent());
        news.setTopic(payload.getTopic());
        news.setAuthorId(userId);
        news.setCreatedTime(LocalDateTime.now());
        news.setShortDescription(payload.getShortDescription());
        news = newsRepo.save(news);
        FileSystem fileSystemImage = gcsService.storeFile(payload.getImageAvatar(),path);
        FileSystem fileSystemFavicon = gcsService.storeFile(payload.getFavicon(),path);
        ObjectMedia objectMediaImage = new ObjectMedia();
        objectMediaImage.setObjectId(news.getId());
        objectMediaImage.setMediaId(fileSystemImage.getId());
        objectMediaImage.setObjectType(ObjectTypeEnum.NEWS.name());
        objectMediaImage.setRefType(ObjectMediaRefValueEnum.IMAGE_NEWS.name());
        ObjectMedia objectMediaFavicon = new ObjectMedia();
        objectMediaFavicon.setObjectId(news.getId());
        objectMediaFavicon.setObjectType(ObjectTypeEnum.NEWS.name());
        objectMediaFavicon.setMediaId(fileSystemFavicon.getId());
        objectMediaFavicon.setRefType(ObjectMediaRefValueEnum.FAVICON_NEWS.name());
        objectMediaFavicon.setMediaId(fileSystemFavicon.getId());
        objectMediaRepo.saveAll(List.of(objectMediaImage,objectMediaFavicon));
        fileSystemRepo.saveAll(List.of(fileSystemImage,fileSystemFavicon));
        return MessageResp.ok();
    }
}
