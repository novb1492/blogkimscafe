package com.example.blogkimscafe.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class restExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public Map<String,Object> globalHandler(RuntimeException e) {
        System.out.println("μμΈλ°μ");
        Map<String,Object>map=new HashMap<>();
        map.put("result",false);
        map.put("messege", e.getMessage());
        return  map;
    }
    
}
