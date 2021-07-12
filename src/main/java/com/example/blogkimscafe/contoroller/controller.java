package com.example.blogkimscafe.contoroller;







import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.boardimage.boardimagedao;
import com.example.blogkimscafe.model.user.uservo;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.commentservice;
import com.example.blogkimscafe.service.historyservice;
import com.example.blogkimscafe.service.reservationservice;
import com.example.blogkimscafe.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class controller {

    @Autowired
    private userservice userservice;
    @Autowired
    private boardservice boardservice;
    @Autowired
    private boardimagedao boardimagedao;
    @Autowired
    private commentservice commentservice;
    @Autowired
    private reservationservice reservationservice;
    @Autowired
    private historyservice historyservice;


    @GetMapping("/auth/joinpage")
    public String joinPage() {
        return "joinpage";
    }
    @GetMapping("/auth/loginpage")
    public String loginPage() {
        return "loginpage";
    }
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        if(existsByEmail(principaldetail.getUsername())){
            model.addAttribute("uservo", principaldetail.getUservo());
            return "mypage"; 
        }
        return "/";
    }
    @GetMapping("/updatepwdpage")
    public String updatePwdPage() {
        return "updatepwdpage";
    }
    @GetMapping("/auth/findpwdpage")
    public String findPwdPage() {
        return "findpwdpage";
    }
    @GetMapping("/writearticle")
    public String writeArticlePage(@AuthenticationPrincipal principaldetail principaldetail) {
        if(userservice.getEmailCheck(principaldetail.getUsername())){
            return "writearticlepage";
        }
        return "/auth/boardlist";
    }
    @GetMapping("/auth/content")
    public String content(@RequestParam("bid")int bid,Model model,@RequestParam(value = "page",defaultValue = "1")int page) {
        int totalpage=commentservice.totalCommentCount(bid);
        boardvo boardvo=boardservice.getArticle(bid);
        model.addAttribute("imagearray", boardimagedao.findByBidOrderById(bid));
        model.addAttribute("currentpage", page);
        model.addAttribute("totalpage", totalpage);
        model.addAttribute("array", commentservice.getComment(bid, page, totalpage));
        model.addAttribute("boardvo", boardvo);
        return "content";
    }
    @GetMapping("/updatearticlepage")
    public String updateArticlePage(@RequestParam("bid")int bid,@AuthenticationPrincipal principaldetail principaldetail,Model model) {
        boardvo boardvo=boardservice.getArticle(bid);
        if(principaldetail.getUsername().equals(boardvo.getEmail())){
           model.addAttribute("imagearray",boardimagedao.findByBidOrderById(bid));
           model.addAttribute("boardvo", boardvo);
            return "updatearticlepage";
        }
        return "boardlist";
        
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
    @GetMapping("/auth/reservationpage")
    public String reservationPage(Model model,@AuthenticationPrincipal principaldetail principaldetail) {
        uservo uservo=principaldetail.getUservo();
        if(uservo!=null){
            model.addAttribute("uservo",uservo);

        }
        return "reservationpage";
    }
    private boolean existsByEmail(String email){
        return userservice.confrimEmail(email);
    }
    @GetMapping("/showreservationpage")
    public String showReservationPage(@AuthenticationPrincipal principaldetail principaldetail,Model model,@RequestParam(value = "page",defaultValue = "1")int page) {
        String email=principaldetail.getUsername();
        int totalpages=historyservice.getCountHistories(email);
        model.addAttribute("array", reservationservice.getReservationByEmail(email));
        model.addAttribute("harray", historyservice.getHistories(email,page,totalpages));
        model.addAttribute("currentpage", page);
        model.addAttribute("totalpages",totalpages);
        return "showreservationpage";
    }
    @GetMapping("/deleteuserpage")
    public String deleteUserPage() {
        return "deleteuserpage";
    }
    @GetMapping("/test")
    public String name() {
        return "test";
    }
    @GetMapping("/updatereservationpage")
    public String updateReservationPage(@RequestParam("id")int id,Model model,@AuthenticationPrincipal principaldetail principaldetail) {
        model.addAttribute("cancleRid", id);
        model.addAttribute("uservo",principaldetail.getUservo());
        return "updatereservationpage";
    }
    @GetMapping("/updatephonepage")
    public String updatePhonePage() {
        return "updatephonepage";
    }

  

   

  

   
}
