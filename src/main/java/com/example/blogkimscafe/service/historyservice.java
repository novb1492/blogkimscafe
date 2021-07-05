package com.example.blogkimscafe.service;

import java.sql.Timestamp;



import com.example.blogkimscafe.model.history.historydao;
import com.example.blogkimscafe.model.history.historyvo;
import com.example.blogkimscafe.model.reservation.reservationvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class historyservice {
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
}
