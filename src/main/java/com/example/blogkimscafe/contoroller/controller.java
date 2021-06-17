package com.example.blogkimscafe.contoroller;



import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.user.userdto;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class controller {

    @Autowired
    private userservice userservice;
    @Autowired
    private boardservice boardservice;


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
    @GetMapping("/writearticle")
    public String writearticlepage(@AuthenticationPrincipal principaldetail principaldetail) {
        if(userservice.getEmailCheck(principaldetail.getUsername()).equals("true")){
            return "writearticlepage";
        }
        return "/auth/boardlist";
    }
    @GetMapping("/auth/boardlist")
    public String boardlist(Model model,@RequestParam(value = "page",defaultValue = "1")int page) {
        Page<boardvo>array=boardservice.getBoard(page);
        model.addAttribute("search", false);
        model.addAttribute("currentpage", page);
        model.addAttribute("array", array);
        model.addAttribute("totalpage", array.getTotalPages());
        return "boardlist";
    }

  

   
}
