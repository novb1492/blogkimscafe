package com.example.blogkimscafe.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.blogkimscafe.model.history.historydao;
import com.example.blogkimscafe.model.history.historyvo;
import com.example.blogkimscafe.model.reservation.reservationvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class historyservice {
    private final int pagesize=3;

    @Autowired
    private historydao historydao;
    @Autowired
    private utilservice utilservice;

    public void insertHistory(reservationvo reservationvo) {
        try {
            Timestamp timestamp=utilservice.makeToTimestamp(reservationvo.getRequesthour());
            historyvo historyvo=new historyvo(reservationvo, timestamp);
            historydao.save(historyvo);
        } catch (Exception e) {
            System.out.println("history 저장중 오류가 발생 했습니다");
            throw new RuntimeException();
        }
    }
    public int getCountHistories(String email){
       return utilservice.getTotalpages(historydao.countByEmailNative(email), pagesize);
    }
    public List<historyvo> getHistories(String email,int page,int totalpages) {
        List<historyvo>array=new ArrayList<>();
        try {
            if(totalpages>1){
               int fisrt=utilservice.getFirst(page, pagesize);
                array=historydao.findByEmailNative(email,fisrt-1, utilservice.getEnd(fisrt, pagesize)-fisrt+1);
            }else{
                array=historydao.findByEmailNative2(email);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("history 불러오기에 실패 했습니다");
        }
        
    }
    public void deleteHistory(int rid) {
        try {
            historydao.deleteByRid(rid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("history삭제에 실패했습니다");
        }
    }
}
