package com.example.blogkimscafe.service;








import com.example.blogkimscafe.config.security;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.enums.Role;
import com.example.blogkimscafe.enums.responToFront;
import com.example.blogkimscafe.model.user.*;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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
        return userdao.existsByEmail(email);
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public JSONObject insertUser(userdto userdto) {

        try {
    
           System.out.println("회원가입 이메일"+ userdto.getEmail());
           if(userdao.existsByEmail(userdto.getEmail())){
                throw new RuntimeException();
           }
            uservo uservo=new uservo(userdto);
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            uservo.setPwd(bCryptPasswordEncoder.encode(uservo.getPwd()));
            uservo.setRole(Role.USER.getValue());
            uservo.setRandnum(utilservice.GetRandomNum(6));
            uservo.setEmailcheck("false");
           userdao.save(uservo);
            return utilservice.makeJson(responToFront.successSingUp.getBool(), responToFront.successSingUp.getMessege());
        
        } catch (RuntimeException e) {
            throw new RuntimeException("이미 존재하는 이메일입니다 ");  
        } 
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
    @Transactional
    public boolean updateTempPwd(String email,String randnum8) {
        try {
            uservo uservo=userdao.findByEmail(email);
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            uservo.setPwd(bCryptPasswordEncoder.encode(randnum8));
            return yes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
    }
    
    
    

}
