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
    private int pagesize=3;

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
        int totalpages=0;
        try {
            int count=historydao.countByEmailNative(email);
            totalpages=count/pagesize;
            if(count%pagesize>0){
                totalpages++;
            }
            return totalpages;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return 0;
    }
    public List<historyvo> getHistories(String email,int page,int totalpages) {
        int fisrt=0,end=0;
        List<historyvo>array=new ArrayList<>();
        try {
            if(totalpages>1){
                fisrt=(page-1)*pagesize+1;
                end=fisrt+pagesize-1; 
                array=historydao.findByEmailNative(email,fisrt-1, end+1);
            }else{
                array=historydao.findByEmailNative2(email, fisrt, end);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("history 불러오기에 실패 했습니다");
        }
        
    }
}
