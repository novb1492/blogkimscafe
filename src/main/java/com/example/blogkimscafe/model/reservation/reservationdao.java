package com.example.blogkimscafe.model.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface reservationdao extends JpaRepository<reservationvo,Integer>{
    
    public List<reservationvo>findBySeat(String seat); 
}
