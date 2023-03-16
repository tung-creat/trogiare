package com.trogiare.utils;

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
    public static boolean isEmpty(List list){
        if(list == null ||  list.isEmpty()){
            return true;
        }
        return  false;
    }
}
