package com.example.blogkimscafe.contoroller;







import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.comment.commentdto;
import com.example.blogkimscafe.model.reservation.reservationdto;
import com.example.blogkimscafe.model.user.pwddto;
import com.example.blogkimscafe.model.user.userdto;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.commentservice;
import com.example.blogkimscafe.service.emailservice;
import com.example.blogkimscafe.service.iamportservice;
import com.example.blogkimscafe.service.reservationservice;
import com.example.blogkimscafe.service.userservice;
import com.example.blogkimscafe.service.utilservice;
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
    @Autowired
    private utilservice utilservice;
    @Autowired
    private reservationservice reservationservice;
    @Autowired
    private iamportservice iamportservice;
    
    @PostMapping("/auth/insertuser")
    public JSONObject insertUser(@Valid userdto userdto) {
        String text=userservice.insertUser(userdto);
        return responToFront(text);
    }
    @PostMapping("/auth/emailconfirm")
    public boolean emailConfrim(@RequestBody userdto userdto ) {
        return userservice.confrimEmail(userdto.getEmail());
    }
    @PostMapping("/sendemail")
    public JSONObject sendEmail(@AuthenticationPrincipal principaldetail principaldetail) {
  
        return  emailservice.sendEmail(principaldetail.getUsername(),6);
    }
    @PostMapping("/sendemailnologin")
    public JSONObject sendEmailnologin(@RequestBody Map<String,Object>map) {
        String email=(String)map.get("email");
        return  emailservice.sendEmail(email,6);
    }
    @PostMapping("/confrimrandnum")
    public JSONObject confrimRandnum(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody Map<String,Object> randnum) {
        System.out.println(randnum.get("randnum")+"입력한번호");
        String email=principaldetail.getUsername();
        JSONObject jsonObject=userservice.confrimRandnum(email, (String)randnum.get("randnum"));
        if((boolean) jsonObject.get("result")){
            principaldetail.getUservo().setEmailcheck("true");
        }
        return jsonObject;
        
    }
    @PostMapping("/sendtemppwd")
    public JSONObject sendTempPwd(@RequestParam("email")String email,@RequestParam("randnum")String randnum) {
        JSONObject jsonObject=userservice.confrimRandnum(email, randnum);
        if((boolean) jsonObject.get("result")){
           return emailservice.sendTempPwd(email,8);
        }
        return jsonObject;
    }   
    @PostMapping("/updatepwd")
    public JSONObject updatePwd(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody@Valid pwddto pwddto) {

        return  userservice.updatePwd(principaldetail,pwddto);
        
    }
    @PostMapping("/confrimemailcheck")
    public JSONObject writeArticlePage(@AuthenticationPrincipal principaldetail principaldetail) {
       return userservice.getEmailCheck(principaldetail.getUsername());
    }
    @PostMapping("/insertarticle")
    public JSONObject insertArticle(@AuthenticationPrincipal principaldetail principaldetail,@Valid boarddto boarddto,@RequestParam(value = "file", required = false)List<MultipartFile> file) {
        return boardservice.insertArticle(principaldetail.getUsername(), boarddto,file);
    }
    @PostMapping("/updatearticle")
    public JSONObject updateArticle(@AuthenticationPrincipal principaldetail principaldetail,@Valid boarddto boarddto,@RequestParam("bid")int bid,@RequestParam(value = "file", required = false)List<MultipartFile> file,@RequestParam(value =  "alreadyimages", required = false)List<Integer>alreadyimages) {
       System.out.println(alreadyimages.isEmpty()+"비웠나요"+alreadyimages);
        return boardservice.updateArticle(principaldetail.getUsername(), boarddto, bid,file,alreadyimages);
    }
    @PostMapping("/deletearticle")
    public JSONObject deleteArticle(@RequestBody boarddto boarddto ,@AuthenticationPrincipal principaldetail principaldetail) { 
        System.out.println("삭제할 게시물 "+boarddto.getBid());
        return boardservice.deleteArticle(boarddto.getBid(),principaldetail.getUsername());
    }
    @PostMapping("/insertcomment")
    public JSONObject insertComment(@RequestBody@Valid commentdto commentdto,@AuthenticationPrincipal principaldetail principaldetail) {
        String email= principaldetail.getUsername();
        JSONObject jsonObject=userservice.getEmailCheck(email);
        if((boolean) jsonObject.get("result")){
            return commentservice.insertComment(commentdto,email); 
        }
        return jsonObject;
       
    }
    @PostMapping("/updatecomment")
    public JSONObject updateComment(@RequestBody@Valid commentdto commentdto,@AuthenticationPrincipal principaldetail principaldetail) {
        return commentservice.updateComment(commentdto, principaldetail.getUsername()); 
    }
    @PostMapping("/deletecomment")
    public JSONObject deleteComment(@RequestParam("cid")int cid,@AuthenticationPrincipal principaldetail principaldetail) {
        return commentservice.deleteCommentByCid(cid, principaldetail.getUsername());
    }
    @PostMapping("/getimebyseat")
    public List<Integer> getReservation(@RequestParam("seat") String seat,@AuthenticationPrincipal principaldetail principaldetail) {
        System.out.println("예약시간가지러옴" +seat);
        return reservationservice.getCanRerserTime(seat);
    }
    @PostMapping("/insertreservation")
    public JSONObject insertReservation(@AuthenticationPrincipal principaldetail principaldetail,@Valid reservationdto reservationdto,@RequestParam(value = "requesthour[]")List<Integer> requestTime) {
        String email=principaldetail.getUsername();
        JSONObject jsonObject=userservice.getEmailCheck(email);
        if((boolean) jsonObject.get("result")){
            return reservationservice.insertReservation(reservationdto,principaldetail.getUsername(),principaldetail.getUservo().getName(),requestTime);
        }
        return jsonObject;
    }
    @PostMapping("/deletereservation")
    public JSONObject deleteReservation(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody reservationdto reservationdto  ) {
        System.out.println("취소할 예약 번호"+reservationdto.getRid());
        return reservationservice.deleteReservation(principaldetail.getUsername() ,reservationdto);
    }
    private JSONObject responToFront(String text) {  
        return utilservice.makeJson(responResultEnum.valueOf(text).getBool(), responResultEnum.valueOf(text).getMessege());
    }
    @PostMapping("/confrimPay")
    public void confrimPay(@RequestParam("imp_uid")String imp_uid) {
        iamportservice.getToken();
    }

    

    
}
