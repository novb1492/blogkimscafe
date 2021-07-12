package com.example.blogkimscafe.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.example.blogkimscafe.email.sendemail;
import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.reservation.reservationdao;
import com.example.blogkimscafe.model.reservation.reservationdto;
import com.example.blogkimscafe.model.reservation.reservationvo;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class reservationservice {

    private final int openTime=6;
    private final int closeTime=23;
    
    @Autowired
    private reservationdao reservationdao;
    @Autowired
    private utilservice utilservice;
    @Autowired
    private historyservice historyservice;
    @Autowired
    private iamportservice iamportservice;
    @Autowired
    private coolSmsService coolSmsService;
    @Autowired
    private sendemail sendemail;


    public List<Integer> getCanRerserTime(String seat) {
        int nowHour=utilservice.getNowHour();
        List<reservationvo>alreadyTimes=getAlreadyTime(seat);
        List<Integer>array=new ArrayList<>();
        try {
           if(alreadyTimes.isEmpty()){
            for(int i=openTime;i<=closeTime;i++){
                if(i>nowHour){
                    array.add(i);  
                }
            }
           }else{
            for(int i=openTime;i<=closeTime;i++){
                for(int ii=0;ii<alreadyTimes.size();ii++){
                    if(i>nowHour){
                        LocalDateTime localDateTime=LocalDateTime.now().withHour(i).withMinute(0).withSecond(0).withNano(0);
                        if(utilservice.compareDate(alreadyTimes.get(ii).getReservationdatetime(), localDateTime)){//utilservice.compareDate(alreadyTimes.get(ii).getReservationdatetime())==false){
                            System.out.println("불가능 시간 "+i);
                            break;
                        }else if(ii==alreadyTimes.size()-1){
                            System.out.println("가능한 시간 "+i);
                            array.add(i);    
                        } 
                    }
                } 
            }
           }
            return array;
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject insertReservation(reservationdto reservationdto,String email,String name,List<Integer>requestTime,String imp_uid,HttpSession httpSession,String phone) {
        try {
            String seat=reservationdto.getSeat();
            String confirm=confrimTime(seat, requestTime);
            if("true".equals(confirm)){
                for(int i=0;i<requestTime.size();i++){
                    Timestamp timestamp=utilservice.makeToTimestamp(requestTime.get(i));
                    reservationvo reservationvo=new reservationvo(0, reservationdto.getSeat(), name, email, null, requestTime.get(i), timestamp, imp_uid, reservationdto.getPrice());
                    reservationdao.save(reservationvo);
                    historyservice.insertHistory(reservationvo);
                }
                String done=utilservice.sendReservtionOkMessege(requestTime,seat);
                sendemail.sendEmail(email, "안녕하세요 예약내역을 보내드립니다", done);
                coolSmsService.sendMessege(phone, done);
                utilservice.emthySession(httpSession);
                return utilservice.makeJson(responResultEnum.sucInsertReservation.getBool(), responResultEnum.sucInsertReservation.getMessege());  
            }
            failInsert(httpSession, imp_uid);
            return utilservice.makeJson(responResultEnum.valueOf(confirm).getBool(), responResultEnum.valueOf(confirm).getMessege());
            } catch (Exception e) {
            e.printStackTrace();
            failInsert(httpSession, imp_uid);
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    private String confrimTime(String seat,List<Integer>requestTime){
        List<reservationvo>alreadyTimes=reservationdao.findBySeat(seat);
        int nowHour=utilservice.getNowHour();
            for(int i=0;i<requestTime.size();i++){
                System.out.println("요청시간"+requestTime.get(i)+"현재시간"+nowHour);
                if(requestTime.get(i)<=nowHour){
                    System.out.println("불가이유:이전시간 요청");
                    return "beforeTime";
                }
                for(reservationvo r:alreadyTimes){
                    LocalDateTime localDateTime=LocalDateTime.now().withHour(i).withMinute(0).withSecond(0).withNano(0);
                    if(utilservice.compareDate(r.getReservationdatetime(), localDateTime)){
                        System.out.println("불가이유: 중복시간 요청");
                        return "alreadyTime";
                    }
                }
            }
            return "true";
    }
    private List<reservationvo> getAlreadyTime(String seat){
        try {
            return reservationdao.findBySeat(seat);
        } catch (Exception e) {
           throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    public List<reservationvo> getReservationByEmail(String email) {
        try {
            List<reservationvo>array=new ArrayList<>();
            for(reservationvo reservationvo: reservationdao.findByEmail(email)){
                if(utilservice.compareDate(reservationvo.getReservationdatetime())){
                    array.add(reservationvo);
                }else{
                    reservationdao.deleteById(reservationvo.getId());
                }
            }
            return array;
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject deleteReservation(String email,int rid,String phone) {
        System.out.println(phone+" 예약취소");
        try {
        reservationvo reservationvo=confrimReservation(rid,email);
          if(reservationvo!=null){
                iamportservice.cancleBuy(reservationvo.getImp_uid(),reservationvo.getPrice());
                reservationdao.deleteById(rid);
                historyservice.deleteHistory(rid);
                String done=utilservice.sendReservationCandleMessege(reservationvo.getRequesthour(),reservationvo.getSeat());
                sendemail.sendEmail(email,"예약 취소를 알려드립니다 ", done);
                coolSmsService.sendMessege(phone,done);
                return utilservice.makeJson(responResultEnum.sucDeleteRerservation.getBool(), responResultEnum.sucDeleteRerservation.getMessege());
          }
          return utilservice.makeJson(responResultEnum.failDeleteReservation.getBool(), responResultEnum.failDeleteReservation.getMessege());
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException("deleteReservation에서 오류가 발생 했습니다");
        }
    }
    @Transactional
    public JSONObject updateReservation(String email,reservationdto reservationdto) {
        reservationvo reservationvo=getReservationByid(reservationdto.getRid());
        iamportservice.cancleBuy(reservationvo.getImp_uid(),reservationvo.getPrice());
        try {
            return utilservice.makeJson(responResultEnum.updateReservation.getBool(),responResultEnum.updateReservation.getMessege());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("updateReservation 오류가 발생했습니다");
        }
    }
    public int getPrice(int priceByHour,int times) {
        try {
          return priceByHour*times;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("계산중 오류가 발생했습니다");
        }
    }
    private reservationvo getReservationByid(int id){
        return reservationdao.findById(id).orElseThrow(()-> new RuntimeException("존재 하지 않는 예약입니다"));
    }
    private void failInsert(HttpSession httpSession ,String imp_uid) {
        iamportservice.cancleBuy(imp_uid,0);
        utilservice.emthySession(httpSession);
    }
    private reservationvo confrimReservation(int rid,String email) {
        reservationvo reservationvo=reservationdao.findById(rid).orElseThrow(()->new RuntimeException("예약자와 취소자가 일치 하지 않습니다"));
        if(reservationvo.getEmail().equals(email)){
            return reservationvo;
        }
        return null;
    }
 
  
   

}
