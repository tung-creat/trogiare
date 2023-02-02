package com.trogiare.common;

import com.trogiare.utils.ValidateUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Constants {
    public static Integer ITEM_PER_PAGE = 20;
    public static Integer DEFAULT_PAGE = 0;
    public static String URI_AUTHORITY;
    public static final String PATH_GET_IMAGE = "/image/";

    public static String getAuthority(HttpServletRequest request) {
        if (ValidateUtil.isNotEmpty(URI_AUTHORITY)) {
            return URI_AUTHORITY;
        }
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        URI_AUTHORITY = baseUrl;
        return URI_AUTHORITY;
    }
}
