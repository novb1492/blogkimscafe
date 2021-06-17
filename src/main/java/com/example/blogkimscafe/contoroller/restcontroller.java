package com.example.blogkimscafe.contoroller;







import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.user.pwddto;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.emailservice;
import com.example.blogkimscafe.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class restcontroller {

    @Autowired
    private userservice userservice;
    @Autowired
    private emailservice emailservice;
    @Autowired
    private boardservice boardservice;

    @PostMapping("/auth/emailconfirm")
    public boolean emailConfrim(@RequestParam("email")String email ) {
        return userservice.confrimEmail(email);
    }
    @PostMapping("/sendemail")
    public boolean sendEmail(@AuthenticationPrincipal principaldetail principaldetail) {
  
        return  emailservice.sendEmail(principaldetail.getUsername(),6);
    }
    @PostMapping("/sendemailnologin")
    public boolean sendEmailnologin(@RequestParam("email")String email) {
        if(userservice.confrimEmail(email)){
            return false;
        }
        return  emailservice.sendEmail(email,6);
    }
    @PostMapping("/confrimrandnum")
    public boolean confrimRandnum(@AuthenticationPrincipal principaldetail principaldetail,@RequestParam("randnum")String randnum) {
        String email=principaldetail.getUsername();
        if(userservice.confrimRandnum(email,randnum)){
            principaldetail.getUservo().setEmailcheck(userservice.getEmailCheck(email));
            return true;
        }
        return false;
        
    }
    @PostMapping("/sendtemppwd")
    public boolean sendTempPwd(@RequestParam("email")String email,@RequestParam("randnum")String randnum) {
        if(userservice.confrimRandnum(email,randnum)){
           return emailservice.sendTempPwd(email,8);
        }
        return false;
    }   
    @PostMapping("/updatepwd")
    public boolean updatePwd(@AuthenticationPrincipal principaldetail principaldetail,@Valid pwddto pwddto) {

        return  userservice.updatePwd(principaldetail,pwddto);
        
    }
    @PostMapping("/confrimemailcheck")
    public boolean writeArticlePage(@AuthenticationPrincipal principaldetail principaldetail) {
        if(userservice.getEmailCheck(principaldetail.getUsername()).equals("true")){
            return true;
        }
        return false;
    }
    @PostMapping("/insertarticle")
    public boolean insertArticle(@AuthenticationPrincipal principaldetail principaldetail,@Valid boarddto boarddto) {
        
        return boardservice.insertArticle(principaldetail.getUsername(), boarddto);
    }
    
}
