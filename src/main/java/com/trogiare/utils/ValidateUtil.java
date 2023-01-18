package com.trogiare.utils;

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
}
