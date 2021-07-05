package com.example.blogkimscafe.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.reservation.reservationdao;
import com.example.blogkimscafe.model.reservation.reservationdto;
import com.example.blogkimscafe.model.reservation.reservationvo;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class reservationservice {

    private final int openTime=6;
    private final int closeTime=23;
    
    @Autowired
    private reservationdao reservationdao;
    @Autowired
    private utilservice utilservice;


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
    public JSONObject insertReservation(reservationdto reservationdto,String email,String name,List<Integer>requestTime) {
        try {
            String seat=reservationdto.getSeat();
            String confirm=confrimTime(seat, requestTime);
            if("true".equals(confirm)){
                for(int i=0;i<requestTime.size();i++){
                    reservationvo reservationvo=new reservationvo(reservationdto);
                    reservationvo.setRequesthour(requestTime.get(i));
                    reservationvo.setEmail(email);
                    reservationvo.setName(name);
                    Timestamp timestamp=utilservice.makeToTimestamp(requestTime.get(i));
                    reservationvo.setReservationdatetime(timestamp);
                    reservationdao.save(reservationvo);
                }
                return utilservice.makeJson(responResultEnum.sucInsertReservation.getBool(), responResultEnum.sucInsertReservation.getMessege());  
            }
            System.out.println(responResultEnum.valueOf(confirm).getMessege());
            return utilservice.makeJson(responResultEnum.valueOf(confirm).getBool(), responResultEnum.valueOf(confirm).getMessege());
        } catch (Exception e) {
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

}
