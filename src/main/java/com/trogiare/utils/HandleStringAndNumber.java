package com.trogiare.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HandleStringAndNumber {
//    public Map<String,String> compactNumberToString(){
//        Map<String,String> map = new HashMap<>();

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.toLowerCase().replaceAll("đ", "d").replaceAll("\\s+", "-").replaceAll("[^a-zA-Z0-9 ]","-").trim();
    }
    public static Long getNumberFromString(String s){
        String[] x = s.split(" ");
        Long number = convertStringToNumber(Long.valueOf(x[0]),x[1]);
        for(int i = 2 ;i < x.length;i++){
            if(i % 2== 0){
                number += convertStringToNumber(Long.valueOf(x[i]),x[i+1]);
          }
        }
        return number;
    }
    private static Long convertStringToNumber(Long x , String y){

        switch(y.toLowerCase()){
            case "tỷ":
              x = Math.round(x * Math.pow(10, 9));
              return x;
            case "triệu":
                x =  Math.round(x * Math.pow(10, 6));
                return x;
            case "nghìn":
                x = Math.round(x*Math.pow(10,3));
                return x;
            default:
                return x;

        }
    }

    //    }
    public static String  compactNumber(Long number) {
        Map<String, String> map = new HashMap<>();
        Boolean checkNumber = false;
        Double numberArrived = number.doubleValue();
        String numberUnit = "";
        Boolean checkDivisible = false;

        if (number / (Math.pow(10, 9)) >= 1) {
            numberArrived = numberArrived / (Math.pow(10, 9));
            checkDivisible = number % (Math.pow(10, 9)) == 0 ? true : false;
            numberUnit = "tỷ" + numberUnit;
        } else if (numberArrived / (Math.pow(10, 6)) >= 1) {
            numberArrived = numberArrived / (Math.pow(10, 6));
            checkDivisible = number % (Math.pow(10, 6)) == 0 ? true: false;
            numberUnit = "triệu" + numberUnit;
        } else if (numberArrived / (Math.pow(10, 3)) >= 1) {
            numberArrived = numberArrived / (Math.pow(10, 3));
            checkDivisible = number % (Math.pow(10, 6)) == 0 ? true : false;
            numberUnit = "nghìn" + numberUnit;
        }
        numberArrived = Math.round(numberArrived * 10.0) / 10.0;
        map.put("unit", numberUnit);
        map.put("number",checkDivisible || numberArrived % 10 == 0 ? String.valueOf(numberArrived.intValue()) : String.valueOf(numberArrived));
        String result = map.get("number") +" " + map.get("unit");
        return result;
    }


}

