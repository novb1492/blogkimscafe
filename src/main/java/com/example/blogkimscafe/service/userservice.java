package com.example.blogkimscafe.service;








import java.util.List;

import com.example.blogkimscafe.config.security;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.enums.Role;
import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.comment.commentdao;
import com.example.blogkimscafe.model.user.*;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class userservice {
    
    @Autowired
    private userdao userdao; 
    @Autowired
    private security security;
    @Autowired
    private utilservice utilservice;
    @Autowired
    private boardservice boardservice;
    @Autowired
    private commentdao commentdao;
    @Autowired
    private reservationservice reservationservice;
  
    
    public boolean confrimEmail(String email) {
        System.out.println(email+"이메일 조회");
        return userdao.existsByEmail(email);
    }
    public String insertUser(userdto userdto) {
        try {
           System.out.println("회원가입 이메일"+ userdto.getEmail());
           if(confrimEmail(userdto.getEmail())){
                return "alreadyEmail"; 
           }
            uservo uservo=new uservo(userdto);
            BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
            uservo.setPwd(bCryptPasswordEncoder.encode(uservo.getPwd()));
            uservo.setRole(Role.USER.getValue());
            uservo.setRandnum(utilservice.GetRandomNum(6));
            uservo.setEmailcheck("false");
            userdao.save(uservo);
            return "sucSingUp";
        } catch (RuntimeException e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");  
        } 
    }
    @Transactional
    public JSONObject updateRandnum(String email,String randnum) {
        try {
            if(confrimEmail(email)){
                uservo uservo=userdao.findByEmail(email);
                uservo.setRandnum(randnum);
                return utilservice.makeJson(responResultEnum.sendTempNumToEmail.getBool(),responResultEnum.sendTempNumToEmail.getMessege());
            }
            return callNotExistsUser();
        } catch (Exception e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");
        }
    }
    @Transactional
    public JSONObject confrimRandnum(String email,String randnum) {
        try {
            if(confrimEmail(email)){
                uservo uservo=userdao.findByEmail(email);
                if(uservo.getRandnum().equals(randnum)){
                    uservo.setEmailcheck("true");
                    return utilservice.makeJson(responResultEnum.rightTempNum.getBool(), responResultEnum.rightTempNum.getMessege());
                }
                return utilservice.makeJson(responResultEnum.wrongTempNum.getBool(), responResultEnum.wrongTempNum.getMessege());
            }
            return callNotExistsUser();
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시후 다시시도 바랍니다");
        }
    }
    public JSONObject getEmailCheck(String email) {
        if(confrimEmail(email)){
           if(userdao.getEmailCheckfindByEmail(email).equals("true")){
                return utilservice.makeJson(responResultEnum.emailCheckIsTrue.getBool(), responResultEnum.emailCheckIsTrue.getMessege());
           }
           return utilservice.makeJson(responResultEnum.emailCheckIsFalse.getBool(), responResultEnum.emailCheckIsFalse.getMessege()); 
        }
        return callNotExistsUser();
    }
    @Transactional
    public JSONObject updatePwd(@AuthenticationPrincipal principaldetail principaldetail,pwddto pwddto ) {
        try {
            String email=principaldetail.getUsername();
            if(confrimEmail(email)){
                uservo uservo=userdao.findByEmail(email);
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
            }
            return callNotExistsUser();
        } catch (Exception e) {
            throw new RuntimeException("처리중 에러가 발생했습니다 다시 시도 바랍니다");
        }
    }
    @Transactional
    public JSONObject updateTempPwd(String email,String randnum8) {
        try {
            if(confrimEmail(email)){
                uservo uservo=userdao.findByEmail(email);
                BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
                uservo.setPwd(bCryptPasswordEncoder.encode(randnum8));
                return utilservice.makeJson(responResultEnum.sendTempPwd.getBool(),responResultEnum.sendTempPwd.getMessege());
            }
            return callNotExistsUser();
        } catch (Exception e) {
           throw new RuntimeException("오류가 발생했습니다 잠시후 다시시도 바랍니다");
        }
    }
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject deleteUser(userdto userdto,List<String>aList) {
        try {
            uservo uservo=userdao.findByEmail(userdto.getEmail());
            String email=uservo.getEmail();
            if(confrimEmail(email)){
                BCryptPasswordEncoder bCryptPasswordEncoder=security.pwdEncoder();
                if(bCryptPasswordEncoder.matches(userdto.getPwd(),uservo.getPwd())){
                    if(reservationservice.getReservationByEmail(email).isEmpty()){
                        if(aList!=null){
                            for(String s: aList){
                                if(s.equals("board")){
                                List<boardvo>array=boardservice.getAllBordVo(email);
                                for(boardvo v:array){
                                    boardservice.deleteArticle(v, email);
                                }
                                }else if(s.equals("comment")){
                                    commentdao.deleteByEmail(email);
                                }
                            }
                        }
                        userdao.delete(uservo);
                        return utilservice.makeJson(responResultEnum.sucDeleteUser.getBool(), responResultEnum.sucDeleteUser.getMessege());
                    }
                return utilservice.makeJson(responResultEnum.existReservation.getBool(), responResultEnum.existReservation.getMessege());
                }
                return utilservice.makeJson(responResultEnum.notEqalsPwd.getBool(),responResultEnum.notEqalsPwd.getMessege());
            }
            return callNotExistsUser();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("deleteUser 오류가 발생했습니다");
        }
    }
    private JSONObject callNotExistsUser() {
        return utilservice.makeJson(responResultEnum.notExistsUser.getBool(), responResultEnum.notExistsUser.getMessege());
    }
    
    
    

}
