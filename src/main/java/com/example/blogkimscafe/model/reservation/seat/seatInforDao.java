package com.example.blogkimscafe.model.reservation.seat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface seatInforDao extends JpaRepository<seatInforVo,Integer> {
    
    public seatInforVo findBySeat(String seat);
}
