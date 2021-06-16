package com.example.blogkimscafe.service;

import com.example.blogkimscafe.email.sendemail;

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

    public boolean sendEmail(String email,int num) {
        try {
            String randnum=utilservice.GetRandomNum(num);
            if(userservice.updateRandnum(email,randnum)){
                return sendemail.sendEmail(email,"안녕하세요 kim's cafe입니다", "인증번호는"+randnum+"입니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean sendTempPwd(String email,int num) {
        try {
            String temppwd=utilservice.GetRandomNum(8);
            if(userservice.updateTempPwd(email,temppwd)){
                return sendemail.sendEmail(email,"안녕하세요 kim's cafe입니다", "임시비밀번호는"+temppwd+"입니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        
    }
    
    
}