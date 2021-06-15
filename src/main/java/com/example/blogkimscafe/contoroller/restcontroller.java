package com.example.blogkimscafe.contoroller;







import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.email.sendemail;
import com.example.blogkimscafe.model.user.pwddto;
import com.example.blogkimscafe.service.userservice;
import com.example.blogkimscafe.service.utilservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class restcontroller {

    @Autowired
    private userservice userservice;
    @Autowired
    private utilservice utilservice;
    @Autowired
    private sendemail sendemail;

    @PostMapping("/auth/emailconfirm")
    public boolean emailConfrim(@RequestParam("email")String email ) {
        return userservice.confrimEmail(email);
    }
    @PostMapping("/sendemail")
    public boolean sendEmail(@AuthenticationPrincipal principaldetail principaldetail) {
        String email=principaldetail.getUsername();
        String randnum=utilservice.GetRandomNum(6);
        if(userservice.updateRandnum(email,randnum)){
            return sendemail.sendEmail(email,"안녕하세요 kim's cafe입니다", "인증번호는"+randnum+"입니다");
        }
        return false;
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
    @PostMapping("/updatepwd")
    public boolean updatePwd(@AuthenticationPrincipal principaldetail principaldetail,@Valid pwddto pwddto) {

        return  userservice.updatePwd(principaldetail,pwddto);
        
    }
    
    
    
}
