package com.example.blogkimscafe.contoroller;







import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.comment.commentdto;
import com.example.blogkimscafe.model.reservation.reservationdto;
import com.example.blogkimscafe.model.reservation.seat.seatInforVo;
import com.example.blogkimscafe.model.user.pwddto;
import com.example.blogkimscafe.model.user.userdto;
import com.example.blogkimscafe.service.aboutSeatService;
import com.example.blogkimscafe.service.boardservice;
import com.example.blogkimscafe.service.commentservice;
import com.example.blogkimscafe.service.coolSmsService;
import com.example.blogkimscafe.service.emailservice;
import com.example.blogkimscafe.service.iamportservice;
import com.example.blogkimscafe.service.kakaoLoginService;
import com.example.blogkimscafe.service.naverLoingService;
import com.example.blogkimscafe.service.reservationservice;
import com.example.blogkimscafe.service.userservice;
import com.example.blogkimscafe.service.utilservice;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Autowired
    private aboutSeatService aboutSeatService;
    @Autowired
    private coolSmsService coolSmsService;
    @Autowired
    private naverLoingService naverLoingService;
    @Autowired
    private kakaoLoginService kakaoLoginService;

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
    @PostMapping("/sendSms")
    public JSONObject sendSms(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody JSONObject phone,HttpSession httpSession) {
        if(principaldetail.getUsername()!=null){
            String smsRandNum=utilservice.GetRandomNum(6);
            httpSession.setAttribute("phoneNum", phone.get("phone"));
            httpSession.setAttribute("smsRandNum", smsRandNum);
            if(coolSmsService.sendMessege((String)phone.get("phone"), smsRandNum)){
                return responToFront("sendNumToSms");
            }
            return responToFront("failNumToSms");
        } 
        return responToFront("noLoginUser");
    }
    @PostMapping("/confrimsmsnum")
    public JSONObject confrimSmsNum(@AuthenticationPrincipal principaldetail principaldetail,HttpSession httpSession,@RequestBody JSONObject jsonObject) {
        if(principaldetail.getUsername()!=null){
            if(httpSession.getAttribute("smsRandNum").equals(jsonObject.get("randnum"))){
                principaldetail.getUservo().setPhonecheck("true");
                principaldetail.getUservo().setPhone((String)httpSession.getAttribute("phoneNum"));
                userservice.changeSmsCheck(principaldetail.getUsername(),(String)httpSession.getAttribute("phoneNum"));
                httpSession.removeAttribute("phoneNum");
                httpSession.removeAttribute("smsRandNum");
                return responToFront("rightTempNum");
            }
            return responToFront("wrongTempNum");
        }
        return responToFront("noLoginUser");
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
    public boolean writeArticlePage(@AuthenticationPrincipal principaldetail principaldetail) {
    
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
        
        return boardservice.deleteArticle(boardservice.getBoardVo(boarddto.getBid()),principaldetail.getUsername());
    }
    @PostMapping("/insertcomment")
    public JSONObject insertComment(@RequestBody@Valid commentdto commentdto,@AuthenticationPrincipal principaldetail principaldetail) {
        String email= principaldetail.getUsername();
        if(userservice.getEmailCheck(email)){
            return commentservice.insertComment(commentdto,email); 
        }
        return responToFront("emailCheckIsFalse");
       
    }
    @PostMapping("/updatecomment")
    public JSONObject updateComment(@RequestBody@Valid commentdto commentdto,@AuthenticationPrincipal principaldetail principaldetail) {
        return commentservice.updateComment(commentdto, principaldetail.getUsername()); 
    }
    @PostMapping("/deletecomment")
    public JSONObject deleteComment(@RequestParam("cid")int cid,@AuthenticationPrincipal principaldetail principaldetail) {
        
        return commentservice.deleteCommentByCid(commentservice.getCommentVo(cid), principaldetail.getUsername());
    }
    @PostMapping("/getimebyseat")
    public List<Integer> getReservation(@RequestParam("seat") String seat,@AuthenticationPrincipal principaldetail principaldetail) {
        System.out.println("예약시간가지러옴" +seat);
        return reservationservice.getCanRerserTime(seat);
    }
    @PostMapping("/insertreservation")
    public JSONObject insertReservation(@AuthenticationPrincipal principaldetail principaldetail,@Valid reservationdto reservationdto,@RequestParam(value = "requesthour[]")List<Integer> requestTime,@RequestParam("imp_uid")String imp_uid,HttpSession httpSession) {
        String email=principaldetail.getUsername();
        try {
            if(userservice.getEmailCheck(email)){
                if(userservice.getPhoneCheck(email)){
                    seatInforVo seatInforVo=(seatInforVo)httpSession.getAttribute("seat");
                    if(iamportservice.confrimBuyerInfor(imp_uid,reservationservice.getPrice(seatInforVo.getPrice(), requestTime.size()),email)){
                        reservationdto.setPrice(seatInforVo.getPrice());
                        return reservationservice.insertReservation(reservationdto,email,principaldetail.getUservo().getName(),requestTime,imp_uid,httpSession,principaldetail.getUservo().getPhone());
                    }
                    iamportservice.cancleBuy(imp_uid,0);
                    return responToFront("failConfrimBuyerInfor");
                }
                iamportservice.cancleBuy(imp_uid,0);
                return responToFront("failPhoneCheck");
            }
            iamportservice.cancleBuy(imp_uid,0);
            return responToFront("emailCheckIsFalse");
        } catch (Exception e) {
            e.printStackTrace();
            iamportservice.cancleBuy(imp_uid,0);
            utilservice.emthySession(httpSession);
            throw new RuntimeException("insertreservation에서 오류가 발생했습니다 ");
        }
    }
    @PostMapping("/deletereservation")
    public JSONObject deleteReservation(@AuthenticationPrincipal principaldetail principaldetail,@RequestBody reservationdto reservationdto  ) {
        System.out.println("취소할 예약 번호"+reservationdto.getRid());
        return reservationservice.deleteReservation(principaldetail.getUsername() ,reservationdto.getRid(),principaldetail.getUservo().getPhone());
    }
    @PostMapping("/updatereservation")
    public JSONObject updateReservation(@RequestParam("canclerid")int canclerid,@AuthenticationPrincipal principaldetail principaldetail,@Valid reservationdto reservationdto,@RequestParam(value = "requesthour[]")List<Integer> requestTime,@RequestParam("imp_uid")String imp_uid,HttpSession httpSession) {
        String email=principaldetail.getUsername();
        try {
            if(userservice.getEmailCheck(email)){
                if(userservice.getPhoneCheck(email)){
                    seatInforVo seatInforVo=(seatInforVo)httpSession.getAttribute("seat");
                    if(iamportservice.confrimBuyerInfor(imp_uid,reservationservice.getPrice(seatInforVo.getPrice(), requestTime.size()),email)){
                        reservationdto.setPrice(seatInforVo.getPrice());
                        if((boolean) reservationservice.insertReservation(reservationdto,email,principaldetail.getUservo().getName(),requestTime,imp_uid,httpSession,principaldetail.getUservo().getPhone()).get("result")){
                            return reservationservice.deleteReservation(email, canclerid,principaldetail.getUservo().getPhone());
                        }
                    }
                    iamportservice.cancleBuy(imp_uid,0);
                    return responToFront("failConfrimBuyerInfor");
                }
                iamportservice.cancleBuy(imp_uid,0);
                return responToFront("failPhoneCheck");
            }
            iamportservice.cancleBuy(imp_uid,0);
            return responToFront("emailCheckIsFalse");
        } catch (Exception e) {
            e.printStackTrace();
            iamportservice.cancleBuy(imp_uid,0);
            utilservice.emthySession(httpSession);
            throw new RuntimeException("updateReservation 오류가 발생했습니다 ");
        }
    }
    @PostMapping("/confrimseat")
    public boolean getPriceOneHour(@RequestParam("seat")String seat,HttpSession httpSession) {
        seatInforVo seatInforVo=aboutSeatService.confrimSeat(seat);
        if(seatInforVo!=null){
            httpSession.setAttribute("seat", seatInforVo);
            return true;
        }
        return false;
    }
    @PostMapping("/cofrimemailcheck")
    public JSONObject confirmEmailCheck(@AuthenticationPrincipal principaldetail principaldetail,@RequestParam("name")String name,@RequestParam("email")String email) {
        String loginEmail=principaldetail.getUsername();
        if(loginEmail.equals(email)&&principaldetail.getUservo().getName().equals(name)){
            if(userservice.getEmailCheck(loginEmail)){
                return  responToFront("");
            }
            return responToFront("emailCheckIsFalse");
        }
        return responToFront("notEqualsUser"); 
    }
    @PostMapping("/getpricebyhour")
    public int getPriceByHour(HttpSession session) {
        System.out.println("getPriceByHour 진행중");
        seatInforVo seatInforVo=(seatInforVo)session.getAttribute("seat");
        if(seatInforVo!=null){
            return seatInforVo.getPrice();
        }
        return 0;
    }
    @PostMapping("/emthysession")
    public void emthysession(HttpSession session) {
        utilservice.emthySession(session);
    }
    @PostMapping("/deleteuser")
    public JSONObject deleteUser(userdto userdto,@AuthenticationPrincipal principaldetail principaldetail,@RequestParam(value = "choice", required = false)List<String>choice) {
        userdto.setEmail(principaldetail.getUsername());
        return userservice.deleteUser(userdto, choice);
    }
    @PostMapping("/auth/naver")
    public String naverLogin() {
        return  naverLoingService.naverLogin();
    }
    
    @PostMapping("/auth/kakao")
    public String  kakaoLogin() {
        return kakaoLoginService.kakaoGetCode();
    }   
    @PostMapping("/cofrimphone")
    public boolean doConfrimPhone(@RequestBody String phone,@AuthenticationPrincipal principaldetail principaldetail) {
        System.out.println( userservice.confrimPhone(phone,principaldetail.getUservo())+"결과");
        return userservice.confrimPhone(phone,principaldetail.getUservo());
    }
    @PostMapping("/changephone")
    public JSONObject doConfrimPhone(@AuthenticationPrincipal principaldetail principaldetail,HttpSession httpSession,@RequestBody JSONObject jsonObject) {
        if(principaldetail.getUsername()!=null){
            if(httpSession.getAttribute("smsRandNum").equals(jsonObject.get("randnum"))){
                principaldetail.getUservo().setPhonecheck("true");
                principaldetail.getUservo().setPhone((String)httpSession.getAttribute("phoneNum"));
                userservice.changeSmsCheck(principaldetail.getUsername(),(String)httpSession.getAttribute("phoneNum"));
                httpSession.removeAttribute("phoneNum");
                httpSession.removeAttribute("smsRandNum");
                return responToFront("rightTempNum");
            }
            return responToFront("wrongTempNum");
        }
        return responToFront("noLoginUser");
    }
    private JSONObject responToFront(String text) {  
        return utilservice.makeJson(responResultEnum.valueOf(text).getBool(), responResultEnum.valueOf(text).getMessege());
    }
   


    

    
}
