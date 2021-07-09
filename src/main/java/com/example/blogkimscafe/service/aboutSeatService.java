package com.example.blogkimscafe.service;

import com.example.blogkimscafe.model.reservation.seat.seatInforDao;
import com.example.blogkimscafe.model.reservation.seat.seatInforVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class aboutSeatService {
    @Autowired
    private seatInforDao seatInfordao;

    public seatInforVo confrimSeat(String seat) {
        try {
            seatInforVo seatInforVo= seatInfordao.findBySeat(seat);
            if(seatInforVo!=null){
                return seatInforVo;
            }
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
}
