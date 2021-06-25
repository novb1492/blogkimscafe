package com.example.blogkimscafe.service;








import com.example.blogkimscafe.config.security;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.enums.Role;
import com.example.blogkimscafe.enums.responResultEnum;
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
                return utilservice.makeJson(responResultEnum.alreadyEmail.getBool(), responResultEnum.alreadyEmail.getMessege());
           }
            uservo uservo=new uservo(userdto);
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            uservo.setPwd(bCryptPasswordEncoder.encode(uservo.getPwd()));
            uservo.setRole(Role.USER.getValue());
            uservo.setRandnum(utilservice.GetRandomNum(6));
            uservo.setEmailcheck("false");
           userdao.save(uservo);
            return utilservice.makeJson(responResultEnum.sucSingUp.getBool(), responResultEnum.sucSingUp.getMessege());
        
        } catch (RuntimeException e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");  
        } 
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public JSONObject updateRandnum(String email,String randnum) {
        try {
            if(userdao.existsByEmail(email)){
                uservo uservo=userdao.findByEmail(email);
                uservo.setRandnum(randnum);
                return utilservice.makeJson(responResultEnum.sendTempNumToEmail.getBool(),responResultEnum.sendTempNumToEmail.getMessege());
            }
            return utilservice.makeJson(responResultEnum.notExistsUser.getBool(), responResultEnum.notExistsUser.getMessege());
        } catch (Exception e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");
        }
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public JSONObject confrimRandnum(String email,String randnum) {
        try {
            if(userdao.existsByEmail(email)){
                uservo uservo=userdao.findByEmail(email);
                if(uservo.getRandnum().equals(randnum)){
                    uservo.setEmailcheck("true");
                    return utilservice.makeJson(responResultEnum.rightTempNum.getBool(), responResultEnum.rightTempNum.getMessege());
                }
                return utilservice.makeJson(responResultEnum.wrongTempNum.getBool(), responResultEnum.wrongTempNum.getMessege());
            }
            return utilservice.makeJson(responResultEnum.notExistsUser.getBool(), responResultEnum.notExistsUser.getMessege());
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시후 다시시도 바랍니다");
        }
    }
    public JSONObject getEmailCheck(String email) {
        if(userdao.existsByEmail(email)){
           if(userdao.getEmailCheckfindByEmail(email).equals("true")){
                return utilservice.makeJson(responResultEnum.emailCheckIsTrue.getBool(), responResultEnum.emailCheckIsTrue.getMessege());
           }
           return utilservice.makeJson(responResultEnum.emailCheckIsFalse.getBool(), responResultEnum.emailCheckIsFalse.getMessege()); 
        }
        return utilservice.makeJson(responResultEnum.notExistsUser.getBool(), responResultEnum.notExistsUser.getMessege());
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public JSONObject updatePwd(@AuthenticationPrincipal principaldetail principaldetail,pwddto pwddto ) {
        try {
            uservo uservo=userdao.findByEmail(principaldetail.getUsername());
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            if(bCryptPasswordEncoder.matches(pwddto.getPwd(), uservo.getPwd())){
                if(pwddto.getNpwd().equals(pwddto.getNpwd2())){
                    String hashpwd=bCryptPasswordEncoder.encode(pwddto.getNpwd2());
                    userdao.updatePwdNative(hashpwd,uservo.getEmail());
                    principaldetail.getUservo().setPwd(hashpwd);
                    return utilservice.makeJson(responResultEnum.sucUpdatePwd.getBool(),responResultEnum.sucUpdatePwd.getMessege());
                }
                return utilservice.makeJson(responResultEnum.notEqualsNpwd.getBool(), responResultEnum.notEqualsNpwd.getMessege());
            }
            return utilservice.makeJson(responResultEnum.notEqalsPwd.getBool(),responResultEnum.notEqalsPwd.getMessege());
        } catch (Exception e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");
        }
    }
    @Transactional(rollbackFor = {Exception.class}) 
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
