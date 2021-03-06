package com.example.blogkimscafe.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class utilservice {

    @Autowired
    private AuthenticationManager authenticationManager;

    public String GetRandomNum(int end) {
        String num="";
        Random random=new Random();
        for(int i=0;i<end;i++){
            num+=Integer.toString(random.nextInt(10));
        }
        return num;
    }
    public String getUUID(){
        return UUID.randomUUID().toString();
    }
    public JSONObject makeJson(boolean result,String sucupdatepwd) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result",result);
        jsonObject.put("messege", sucupdatepwd);
        return jsonObject;
    }
    public int getNowHour() {
        Calendar cal = Calendar.getInstance();
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour+"íėŽėę°");
        return hour;  
    }
    public Timestamp makeToTimestamp(int requestTime) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = sdf.format(date);
        String reservationdatetime=today+" "+requestTime+":0:0";

        return Timestamp.valueOf(reservationdatetime);

    }
    public int getTotalpages(int totalCount,int pagesize) {
            int totalpage=0;
            totalpage=totalCount/pagesize;
            if(totalCount%pagesize>0){
                totalpage++;
            }
            System.out.println(totalpage);
            return totalpage;
    }
    public int getFirst(int page,int pagesize) {
        return (page-1)*pagesize+1;
    }
    public int getEnd(int fisrt,int pagesize) {
        return fisrt+pagesize-1;
    }
    public boolean compareDate(Timestamp timestamp) {
        LocalDateTime reservationTime=timestamp.toLocalDateTime();
        LocalDateTime today= LocalDateTime.now(); 
        if(reservationTime.isAfter(today)){
           return true;
        }
        return false;
    }    
    public boolean compareDate(Timestamp timestamp,LocalDateTime localDateTime) {
        LocalDateTime reservationTime=timestamp.toLocalDateTime();
        if(reservationTime.isEqual(localDateTime)){
           return true;
        }
        return false;
    }    
    public void emthySession(HttpSession httpSession) {
        httpSession.removeAttribute("seat");
    }
    public String sendReservtionOkMessege(List<Integer>requestTime,String seat) {
        String messege="";
        for(int i: requestTime){
            messege+=Integer.toString(i)+"ė~"+Integer.toString(i+1)+"ė ";
        }
        System.out.println(messege+"messege");
        return "ėëíėļė kimscafe ėëëĪ ėė―ëīė­ė ėë ĪëëĶ―ëëĪ ėëĶŽë "+seat+", ėę°ė "+messege+"ėëëĪ ę°ėŽíĐëëĪ";
        
    }
    public String sendReservationCandleMessege(int requestTime,String seat) {
        return "ėëíėļė kimscafe ėëëĪ ėė―ėī ė·Ļė ëėėĩëëĪ ėëĶŽ: "+seat+" ėę°: "+requestTime;
    }
    public void setAuthentication(String email,String pwd) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
