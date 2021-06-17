package com.example.blogkimscafe.contoroller;



import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.user.userdto;
import com.example.blogkimscafe.service.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class controller {

    @Autowired
    private userservice userservice;


    @GetMapping("/auth/joinpage")
    public String joinpage() {
        return "joinpage";
    }
    @PostMapping("/auth/insertuser")
    public String insertUser(userdto userdto) {
        if(userservice.insertUser(userdto)){
            return "loginpage";
        }
        return "joinpage";
    }
    @GetMapping("/auth/loginpage")
    public String loginpage() {
        return "loginpage";
    }
    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        model.addAttribute("uservo", principaldetail.getUservo());
        return "mypage";
    }
    @GetMapping("/updatepwdpage")
    public String updatepwdpage(@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        model.addAttribute("uservo", principaldetail.getUservo());
        return "updatepwdpage";
    }
    @GetMapping("/auth/findpwdpage")
    public String findpwdpage() {
        return "findpwdpage";
    }
    @GetMapping("/auth/boardlist")
    public String boardlist() {
        return "boardlist";
    }
    @PostMapping("/writearticlepage")
    public String writearticlepage(@AuthenticationPrincipal principaldetail principaldetail) {
        if(userservice.confrimEmail(principaldetail.getUsername())){
            return "writearticlepage";
        }
        return "/auth/boardlist";
    }
  

   
}
