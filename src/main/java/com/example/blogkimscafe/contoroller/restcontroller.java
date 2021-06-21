package com.example.blogkimscafe.contoroller;







import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.comment.commentdto;
import com.example.blogkimscafe.model.user.pwddto;
import com.example.blogkimscafe.model.user.userdto;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.commentservice;
import com.example.blogkimscafe.service.emailservice;
import com.example.blogkimscafe.service.userservice;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class restcontroller {

    @Autowired
    private userservice userservice;
    @Autowired
    private emailservice emailservice;
    @Autowired
    private boardservice boardservice;
    @Autowired
    private commentservice commentservice; 

    @PostMapping("/auth/emailconfirm")
    public boolean emailConfrim(@RequestBody userdto userdto ) {
        return userservice.confrimEmail(userdto.getEmail());
    }
    @PostMapping("/sendemail")
    public boolean sendEmail(@AuthenticationPrincipal principaldetail principaldetail) {
  
        return  emailservice.sendEmail(principaldetail.getUsername(),6);
    }
    @PostMapping("/sendemailnologin")
    public boolean sendEmailnologin(@RequestBody Map<String,Object>map) {
        String email=(String)map.get("email");
        if(userservice.confrimEmail(email)){
            return false;
        }
        return  emailservice.sendEmail(email,6);
    }
    @PostMapping("/confrimrandnum")
    public boolean confrimRandnum(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody Map<String,Object> randnum) {
        System.out.println(randnum.get("randnum")+"입력한번호");
        String email=principaldetail.getUsername();
        if(userservice.confrimRandnum(email,(String)randnum.get("randnum"))){
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
    public boolean updatePwd(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody@Valid pwddto pwddto) {

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
    public boolean insertArticle(@AuthenticationPrincipal principaldetail principaldetail,@Valid boarddto boarddto,@RequestParam(value = "file", required = false)List<MultipartFile> file) {
        return boardservice.insertArticle(principaldetail.getUsername(), boarddto,file);
    }
    @PostMapping("/updatearticle")
    public boolean updateArticle(@AuthenticationPrincipal principaldetail principaldetail,@Valid boarddto boarddto,@RequestParam("bid")int bid) {
        return boardservice.updateArticle(principaldetail.getUsername(), boarddto, bid);
    }
    @PostMapping("/insertcomment")
    public boolean insertComment(@RequestBody@Valid commentdto commentdto,@AuthenticationPrincipal principaldetail principaldetail) {
        String email= principaldetail.getUsername();
        if(userservice.getEmailCheck(email).equals("true")){
            return commentservice.insertComment(commentdto,email); 
        }
        return false;
    }
    @PostMapping("/testjson")
    public JSONObject testJson(@RequestBody userdto userdto) {
       JSONObject jsonObject=new JSONObject();
        jsonObject.put("result", true);
        jsonObject.put("messege", "틀리네요json");
        jsonObject.put("email", userdto.getEmail());
        return jsonObject;
    }
    @PostMapping("/testmap")
    public Map<String,Object> testMap(@RequestBody userdto userdto) {
        Map<String,Object>map=new HashMap<>();
        map.put("result", true);
        map.put("messege", "틀리네요map");
        map.put("email", userdto.getEmail());
        return map;
    }
    
}
