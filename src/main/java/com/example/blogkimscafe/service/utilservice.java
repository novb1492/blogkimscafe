package com.example.blogkimscafe.service;

import java.util.Random;
import java.util.UUID;


import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.stereotype.Service;

@Service
public class utilservice {

    public String GetRandomNum(int end) {
        String num="";
        Random random=new Random();
        for(int i=0;i<end;i++){
            num+=Integer.toString(random.nextInt(10));
        }
        return num;
    }
    public String getUUID(){
        return UUID.randomUUID().toString();
    }
    public JSONObject makeJson(boolean result,String sucupdatepwd) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result",result);
        jsonObject.put("messege", sucupdatepwd);
        return jsonObject;
    }
    
}
