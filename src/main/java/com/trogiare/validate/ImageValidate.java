package com.trogiare.validate;

import com.trogiare.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidate {
    static final Logger logger = LoggerFactory.getLogger(ImageValidate.class);

    public static Boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        if(ValidateUtil.isEmpty(file)){
            return false;
        }
        logger.info("content Type " + contentType);
        if (contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("image/webp")) {
            logger.info("true roi");
            return true;
        }
        return false;
    }

}
