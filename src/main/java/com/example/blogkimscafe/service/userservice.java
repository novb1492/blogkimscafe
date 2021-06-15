package com.example.blogkimscafe.service;




import javax.transaction.Transactional;

import com.example.blogkimscafe.config.security;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.user.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class userservice {
    
    private final boolean yes=true;
    private final boolean no=false;

    @Autowired
    private userdao userdao; 
    @Autowired
    private security security;
    @Autowired
    private utilservice utilservice;
  
    
    public boolean confrimEmail(String email) {

        System.out.println(email+"중복검사");
       uservo vo=userdao.findByEmail(email);
        if(vo==null)
        {
             return yes;
        }
     return no;
    }
    public boolean insertUser(userdto userdto) {

        try {
            System.out.println("회원가입 이메일"+ userdto.getEmail());
            uservo uservo=new uservo(userdto);
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            uservo.setPwd(bCryptPasswordEncoder.encode(uservo.getPwd()));
            uservo.setRole("ROLE_USER");
            uservo.setRandnum(utilservice.GetRandomNum(6));
            uservo.setEmailcheck("false");
            userdao.save(uservo);
            return yes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
    @Transactional
    public boolean updateRandnum(String email,String randnum) {
        try {
            uservo uservo=userdao.findByEmail(email);
            uservo.setRandnum(randnum);
            return yes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
    @Transactional
    public boolean confrimRandnum(String email,String randnum) {
        try {
            uservo uservo=userdao.findByEmail(email);
            if(uservo.getRandnum().equals(randnum)){
                uservo.setEmailcheck("true");
                return yes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
    public String getEmailCheck(String email) {
        try {
            return userdao.getEmailCheckfindByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updatePwd(@AuthenticationPrincipal principaldetail principaldetail,pwddto pwddto ) {
        try {
            uservo uservo=userdao.findByEmail(principaldetail.getUsername());
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            if(bCryptPasswordEncoder.matches(pwddto.getPwd(), uservo.getPwd())){
                if(pwddto.getNpwd().equals(pwddto.getNpwd2())){
                    String hashpwd=bCryptPasswordEncoder.encode(pwddto.getNpwd2());
                    userdao.updatePwdNative(hashpwd,uservo.getEmail());
                    principaldetail.getUservo().setPwd(hashpwd);
                    return yes;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
    
    
    

}
