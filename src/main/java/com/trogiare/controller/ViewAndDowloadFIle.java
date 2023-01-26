package com.trogiare.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewAndDowloadFIle {
    @RequestMapping(path="/image/{object}/{nameFile}")
        public HttpEntity<?> getImage(){
            return null;
        }
}
