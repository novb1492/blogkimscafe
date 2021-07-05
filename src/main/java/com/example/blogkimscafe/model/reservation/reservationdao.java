package com.example.blogkimscafe.model.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface reservationdao extends JpaRepository<reservationvo,Integer>{
    
    public List<reservationvo>findBySeat(String seat); 
    
    @Query(value = "select * from blogreservation where remail=?1 order by id desc",nativeQuery = true)
    public List<reservationvo>findByEmail(String email);
}
