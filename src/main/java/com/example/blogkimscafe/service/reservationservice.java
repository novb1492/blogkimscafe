package com.example.blogkimscafe.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.reservation.reservationdao;
import com.example.blogkimscafe.model.reservation.reservationdto;
import com.example.blogkimscafe.model.reservation.reservationvo;
import com.example.blogkimscafe.model.reservation.seat.seatInforDao;
import com.example.blogkimscafe.model.reservation.seat.seatInforVo;
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
    private seatInforDao seatInfordao;
    @Autowired
    private iamportservice iamportservice;


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
                        if(i==alreadyTimes.get(ii).getRequesthour()){
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
    public JSONObject insertReservation(reservationdto reservationdto,String email,String name,List<Integer>requestTime,String imp_uid) {
        try {
            String seat=reservationdto.getSeat();
            String confirm=confrimTime(seat, requestTime);
            if("true".equals(confirm)){
                for(int i=0;i<requestTime.size();i++){
                    reservationvo reservationvo=new reservationvo(reservationdto);
                    reservationvo.setRequesthour(requestTime.get(i));
                    reservationvo.setEmail(email);
                    reservationvo.setName(name);
                    reservationvo.setImp_uid(imp_uid);
                    Timestamp timestamp=utilservice.makeToTimestamp(requestTime.get(i));
                    reservationvo.setReservationdatetime(timestamp);
                    reservationdao.save(reservationvo);
                    historyservice.insertHistory(reservationvo);
                }
                return utilservice.makeJson(responResultEnum.sucInsertReservation.getBool(), responResultEnum.sucInsertReservation.getMessege());  
            }
            System.out.println(responResultEnum.valueOf(confirm).getMessege());
            iamportservice.cancleBuy(imp_uid);
            return utilservice.makeJson(responResultEnum.valueOf(confirm).getBool(), responResultEnum.valueOf(confirm).getMessege());
            } catch (Exception e) {
            iamportservice.cancleBuy(imp_uid);
            e.printStackTrace();
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
                    if(r.getRequesthour()==requestTime.get(i)){
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
    public JSONObject deleteReservation(String email,reservationdto reservationdto) {
        int rid=reservationdto.getRid();
        try {
          if(confrimReservation(reservationdto.getRid(),email)){
                reservationdao.deleteById(rid);
                historyservice.deleteHistory(rid);
                return utilservice.makeJson(responResultEnum.sucDeleteRerservation.getBool(), responResultEnum.sucDeleteRerservation.getMessege());
          }
          return utilservice.makeJson(responResultEnum.failDeleteReservation.getBool(), responResultEnum.failDeleteReservation.getMessege());
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException("deleteReservation에서 오류가 발생 했습니다");
        }
    }
    public int getPrice(String seat,int times) {
        try {
            seatInforVo vo=seatInfordao.findBySeat(seat);
            return vo.getPrice()*times;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("계산중 오류가 발생했습니다");
        }
    }
    private boolean confrimReservation(int rid,String email) {
        reservationvo reservationvo=reservationdao.findById(rid).orElseThrow(()->new RuntimeException("예약자와 취소자가 일치 하지 않습니다"));
        if(reservationvo.getEmail().equals(email)){
            return true;
        }
        return false;
    }
 
  
   

}
