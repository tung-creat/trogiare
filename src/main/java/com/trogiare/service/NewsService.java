package com.trogiare.service;

import com.trogiare.common.Constants;
import com.trogiare.common.enumrate.NewsStatusEnum;
import com.trogiare.model.FileSystem;
import com.trogiare.model.News;
import com.trogiare.model.UserToken;
import com.trogiare.payload.news.NewsPayload;
import com.trogiare.repo.NewsRepo;
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

@Service
public class NewsService {
    @Autowired
    private NewsRepo newsRepo;
    @Value("${app.path.save.image-blogs}")
    private String PATH_IMAGE_BLOGS;
    @Autowired
    private GcsService gcsService;
    @Transactional
    public MessageResp addNews(NewsPayload payload, HttpServletRequest request) throws IOException {
       String userId = UserUtil.getUserId();
       String path = PATH_IMAGE_BLOGS +"/" + HandleStringAndNumber.removeAccent(payload.getTitle());
        String authority = Constants.getAuthority(request);
        FileSystem fileSystemImage = gcsService.storeFile(payload.getImageAvatar(),path);
        FileSystem fileSystemFavicon = gcsService.storeFile(payload.getFavicon(),path);
        News news = new News();
        news.setStatusNews(NewsStatusEnum.PUBLIC);
        news.setTitle(payload.getTitle());
        news.setMetaTitle(payload.getMetaTitle());
        news.setContent(payload.getContent());
        news.setTopic(payload.getTopic());
        news.setAuthorId(userId);
        news.setCreatedTime(LocalDateTime.now());
        news.setFavicon(authority+"/" + fileSystemFavicon.getPath());
        news.setImageAvatar(authority +"/" + fileSystemImage.getPath());
        news.setShortDescription(payload.getShortDescription());
        newsRepo.save(news);
        return MessageResp.ok();
    }
}
