package com.trogiare.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidateUtil {

    public static boolean isEmpty(String input){
        if(input == null || input.trim().isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String input){
        if(input != null && !(input.trim().isEmpty())){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(MultipartFile multipartFile){
        if(multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() < 1){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(List list){
        if(list == null ||  list.isEmpty() || list.size() <1){
            return true;
        }
        return  false;
    }
}
