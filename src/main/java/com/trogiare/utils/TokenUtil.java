package com.trogiare.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class TokenUtil {

    static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /** different dictionaries used */
    public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMERIC = "0123456789";
    public static final String SPECIAL_CHARS = "!@#$%^&*_=+-/.?<>)";// "!@#$%^&*_=+-/";
    public static final String DICTIONARY = ALPHA_CAPS + ALPHA + NUMERIC;
    public static final int EXPRIED_TOKEN = 2;

    public static final int BCrypt_SALT = 10;
    public static final SimpleDateFormat dateFormater = new SimpleDateFormat("yyMM");



    public static String generateToken(int size) {
        String chars =  DICTIONARY;
        String pw = RandomStringUtils.random(size, chars);
        return pw;
    }

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }



    public static String generateNumber(int size) {
        String pw = RandomStringUtils.random(size, NUMERIC);
        return pw;
    }
    public static String generateAlphaNumber(int size) {
        String pw = RandomStringUtils.random(size, DICTIONARY);
        return pw;
    }

    public static String getTokenFrom(HttpServletRequest request) {
        String authToken = request.getParameter("access_token");
        if (!StringUtils.hasText(authToken)) {
            authToken = request.getHeader("Authorization");
            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7).trim();
            }
        }
        return authToken;
    }
}
