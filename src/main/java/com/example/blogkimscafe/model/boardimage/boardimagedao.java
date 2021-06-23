package com.example.blogkimscafe.model.boardimage;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface boardimagedao extends JpaRepository<boardimagevo,Integer> {
    
    public List<boardimagevo> findByBidOrderById(int bid);

    public List<boardimagevo> findByBid(int bid);

     
    @Modifying 
    @Transactional
    @Query(value="delete from blogboardimage b where b.bid=?1",nativeQuery=true)
    public void deleteAllByBidNative(int bid);
    
    @Query(value = "select id from blogboardimage where bid=?1",nativeQuery = true)
    public List<Integer> findByBidIdNative(int bid);
}
