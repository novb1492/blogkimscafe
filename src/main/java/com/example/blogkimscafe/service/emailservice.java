package com.example.blogkimscafe.service;

import com.example.blogkimscafe.email.sendemail;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class emailservice {
    @Autowired
    private sendemail sendemail;
     @Autowired
    private utilservice utilservice;
    @Autowired
    private userservice userservice;

    public JSONObject sendEmail(String email,int num) {
    
            String randnum=utilservice.GetRandomNum(num);
            JSONObject jsonObject=new JSONObject();
            jsonObject=userservice.updateRandnum(email,randnum);
            if((boolean) jsonObject.get("result")){
                sendemail.sendEmail(email,"안녕하세요 kim's cafe입니다", "인증번호는"+randnum+"입니다");
            }
            return jsonObject;
    }
    public JSONObject sendTempPwd(String email,int num) {
            String temppwd=utilservice.GetRandomNum(num);
            JSONObject jsonObject=new JSONObject();
            jsonObject=userservice.updateTempPwd(email,temppwd);
            if((boolean) jsonObject.get("result")){
               sendemail.sendEmail(email,"안녕하세요 kim's cafe입니다", "임시비밀번호는"+temppwd+"입니다");
            }
       
        return jsonObject;
        
    }
    
    
}
