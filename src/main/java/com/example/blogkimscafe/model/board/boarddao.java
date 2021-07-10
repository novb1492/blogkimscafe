package com.example.blogkimscafe.model.board;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface boarddao extends JpaRepository<boardvo,Integer> {
    
    @Query(value = "select *from blogboard where title like %?1% order by bid desc limit ?2,?3",nativeQuery = true)
    public List<boardvo> findByTitleLikeOrderByBidLimitNative(String title,int fisrt,int end);

    @Query(value = "select count(*) from blogboard where title like %?1%",nativeQuery = true)
    public int countByTitleLikeNative(String title);

    @Query(value = "select *from blogboard where title like %?1% order by bid desc",nativeQuery = true)
    public List<boardvo>findByTitleLikeOrderByBidNative(String title);

    public List<boardvo> findAllByEmail(String email);

}
