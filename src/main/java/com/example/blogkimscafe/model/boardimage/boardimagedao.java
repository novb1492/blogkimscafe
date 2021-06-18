package com.example.blogkimscafe.model.boardimage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface boardimagedao extends JpaRepository<boardimagevo,Integer> {
    
    public List<boardimagevo> findByBidOrderById(int bid);
}
