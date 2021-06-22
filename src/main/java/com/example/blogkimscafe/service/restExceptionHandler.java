package com.example.blogkimscafe.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class restExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Map<String,String> globalHandler(Exception e) {
        Map<String,String>map=new HashMap<>();
        map.put("errmsg", e.getMessage());
        return  map;
    }
    
}
