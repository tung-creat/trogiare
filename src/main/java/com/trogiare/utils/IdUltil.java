package com.trogiare.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

public class IdUltil {
    public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMERIC = "0123456789";
    public static final String SPECIAL_CHARS = "!@#$%^&*_=+-/.?<>)";// "!@#$%^&*_=+-/";
    public static final String DICTIONARY = ALPHA_CAPS + ALPHA + NUMERIC;
    public static String generate()  {
        String id = RandomStringUtils.random(36,DICTIONARY);
        return id;
    }
}
