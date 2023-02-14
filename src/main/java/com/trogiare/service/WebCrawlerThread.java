package com.trogiare.service;

import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.common.enumrate.ObjectTypeEnum;
import com.trogiare.common.enumrate.PostStatusEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.component.PostCodeComponent;
import com.trogiare.model.Address;
import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import com.trogiare.model.Post;
import com.trogiare.repo.AddressRepo;
import com.trogiare.repo.FileSystemRepo;
import com.trogiare.repo.ObjectMediaRepo;
import com.trogiare.repo.PostRepo;
import com.trogiare.utils.HandleStringAndNumber;
import com.trogiare.utils.TokenUtil;
import com.trogiare.utils.ValidateUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class WebCrawlerThread implements Runnable {

    private static List<String> links = new ArrayList<>();
    private String first_link;
    private Thread thread;
    private int Id;
    private int from_pageNumeber;
    private int max_PageNumber;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private FileSystemRepo fileSystemRepo;
    @Autowired
    private ObjectMediaRepo objectMediaRepo;
    @Autowired
    private PostCodeComponent postCodeComponent;

    public WebCrawlerThread(String link,AddressRepo addressRepo, PostRepo postRepo
            ,FileSystemRepo fileSystemRepo,ObjectMediaRepo objectMediaRepo,
                            PostCodeComponent postCodeComponent) {
        this.first_link = link;
        this.addressRepo = addressRepo;
        this.postRepo = postRepo;
        this.fileSystemRepo = fileSystemRepo;
        this.objectMediaRepo = objectMediaRepo;
        this.postCodeComponent = postCodeComponent;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            crawl(first_link);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void crawl(String url) throws ParseException, IOException {
        Document document = request(url);
        Elements elements = document.select("ul.props li div.prop-info a");
        String next_link = document.select("ul.pagination li:last-child a").attr("href");
        for (Element element : elements) {
            Document x = request(element.attr("href"));
            handleProcessing(x);
        }
        crawl(next_link);
    }

    private void handleProcessing(Document document) {
        Random random = new Random();
        int min = 15;
        int max = 100;
        int min1 = 1;
        int max1 = 3;
        Elements elementImg = document.select("div.media-item img");
        String addressDetails = document.select("div.main-info .address").text();
        String[] informationDetail = addressDetails.split(",");
        String province = informationDetail[informationDetail.length - 1];
        String district = informationDetail[informationDetail.length - 2];
        String ward = informationDetail[informationDetail.length - 3];
        Address address = new Address();
        address.setProvince(province);
        address.setAddressDetails(addressDetails);
        address.setDistrict(district);
        address.setVillage(ward);
        address = addressRepo.save(address);
        String title = document.select("div.main-info .title h1").text();
        String priceCompact = document.select("div.main-info .price").text();
        Long price = HandleStringAndNumber.getNumberFromString(priceCompact);
        String are = document.select("div.info-attr span:last-child").text();
        Double useAble = Double.valueOf(random.nextInt(max - min + 1) + min);
        Double landAble = Double.valueOf(random.nextInt(max - min + 1) + min);
        Integer bedRoom = random.nextInt(max1 - min1 + 1) + min1;
        Integer bathroom = random.nextInt(max1 - min1 + 1) + min1;
        String description = document.select("div.info-content-body").text();
        String juridical = "Sổ Hồng";
        Post post = new Post();
        post.setUseableArea(useAble);
        post.setPostCode(postCodeComponent.getCode());
        post.setStatus(PostStatusEnum.PUBLIC.name());
        post.setExpirationDate(LocalDateTime.now().plusDays(10));
        post.setUpdatedTime(LocalDateTime.now());
        post.setCreatedTime(LocalDateTime.now());
        post.setTypePost(PostTypeEnum.RENT);
        post.setOwnerId("40288285862b541001862b55a3900000");
        post.setAddressId(address.getId());
        post.setPrice(price);
        post.setName(title);
        post.setCompactNumber(priceCompact);
        post.setToilet(random.nextInt(max1 - min1 + 1) + min1);
        post.setLandArea(null);
        post.setBedroom(bedRoom);
        post.setDescription(description);
        postRepo.save(post);
        List<FileSystem> fileSystems = new ArrayList<>();
        List<ObjectMedia> objectMediaList = new ArrayList<>();
        Element x = elementImg.get(0);
        String path = ValidateUtil.isNotEmpty(x.attr("src")) ? x.attr("src") : x.attr("data-src");
        path = path.replaceAll("https://cloud.mogi.vn", "");
        FileSystem fileSystem = new FileSystem();
        fileSystem.setId(TokenUtil.generateToken(36));
        fileSystem.setType("img/jpeg");
        fileSystem.setCreatedTime(LocalDateTime.now());
        fileSystem.setPath(path);
        fileSystems.add(fileSystem);
        ObjectMedia objectMedia = new ObjectMedia();
        objectMedia.setObjectId(post.getId());
        objectMedia.setObjectType(ObjectTypeEnum.POST.name());
        objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_POST.name());
        objectMedia.setMediaId(fileSystem.getId());
        objectMediaList.add(objectMedia);
        // save Image
        for (Element element : elementImg) {
            path = ValidateUtil.isNotEmpty(element.attr("src")) ? element.attr("src") : element.attr("data-src");
            path = path.replaceAll("https://cloud.mogi.vn", "");
            fileSystem = new FileSystem();
            fileSystem.setId(TokenUtil.generateToken(36));
            fileSystem.setType("img/jpeg");
            fileSystem.setCreatedTime(LocalDateTime.now());
            fileSystem.setPath(path);
            objectMedia = new ObjectMedia();
            objectMedia.setObjectId(post.getId());
            objectMedia.setObjectType(ObjectTypeEnum.POST.name());
            objectMedia.setRefType(ObjectMediaRefValueEnum.IMAGE_DETAILS_POST.name());
            objectMedia.setMediaId(fileSystem.getId());
            objectMediaList.add(objectMedia);
            fileSystems.add(fileSystem);
        }
        fileSystemRepo.saveAll(fileSystems);
        objectMediaRepo.saveAll(objectMediaList);
    }

    private Document request(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            if (doc != null && con.response().statusCode() == 200) {
                System.out.println("\n**Bot Id: " + Id + " Recieve webpage at " + url);
                String title = doc.title();
                System.out.println(title);
                return doc;
            }
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }
}
