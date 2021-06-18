package com.example.blogkimscafe.contoroller;



import java.util.List;

import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.boardimage.boardimagedao;
import com.example.blogkimscafe.model.boardimage.boardimagevo;
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
    @Autowired
    private boardimagedao boardimagedao;


    @GetMapping("/auth/joinpage")
    public String joinPage() {
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
    public String loginPage() {
        return "loginpage";
    }
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        model.addAttribute("uservo", principaldetail.getUservo());
        return "mypage";
    }
    @GetMapping("/updatepwdpage")
    public String updatePwdPage(@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        model.addAttribute("uservo", principaldetail.getUservo());
        return "updatepwdpage";
    }
    @GetMapping("/auth/findpwdpage")
    public String findPwdPage() {
        return "findpwdpage";
    }
    @GetMapping("/writearticle")
    public String writeArticlePage(@AuthenticationPrincipal principaldetail principaldetail) {
        if(userservice.getEmailCheck(principaldetail.getUsername()).equals("true")){
            return "writearticlepage";
        }
        return "/auth/boardlist";
    }
    @GetMapping("/auth/content")
    public String name(@RequestParam("bid")int bid,Model model) {
         model.addAttribute("boardvo", boardservice.getArticle(bid));
         return "content";
    }
    @GetMapping("/auth/test")
    public String test(Model model) {
        List<boardimagevo>array=boardimagedao.findByBidOrderById(1);
        for(boardimagevo boardimagevo: array){
            System.out.println(boardimagevo.getImageurl());
        }
        model.addAttribute("array", array);
        return "test";
    }
    @GetMapping("/auth/boardlist")
    public String boardList(Model model,@RequestParam(value = "page",defaultValue = "1")int page,@RequestParam(value = "title",defaultValue = "")String title) {
        if(title.equals("")){
            Page<boardvo>array=boardservice.getBoard(page);
            model.addAttribute("search", false);
            model.addAttribute("currentpage", page);
            model.addAttribute("array", array);
            model.addAttribute("totalpage", array.getTotalPages());
        }else{
            int totalpage=boardservice.getSearchAtBoardCount(title);
            model.addAttribute("title", title);
            model.addAttribute("search", true);
            model.addAttribute("currentpage", page);
            model.addAttribute("totalpage", totalpage);
            model.addAttribute("array", boardservice.getSearchAtBoard(title,page,totalpage));
        }
        return "boardlist";
    }


   

  

   
}
