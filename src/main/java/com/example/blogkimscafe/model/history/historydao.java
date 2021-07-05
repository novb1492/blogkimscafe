package com.example.blogkimscafe.model.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface historydao extends JpaRepository<historyvo,Integer> {
    
    @Query(value = "select count(*) from bloghistory where email=?1",nativeQuery = true)
    public int countByEmailNative(String email);

    @Query(value = "select * from bloghistory where email=?1 order by id desc limit ?2,?3",nativeQuery = true)
    public List<historyvo>findByEmailNative(String email,int fisrt,int end);

    @Query(value = "select * from bloghistory where email=?1 order by id desc ",nativeQuery = true)
    public List<historyvo>findByEmailNative2(String email,int fisrt,int end);
}
