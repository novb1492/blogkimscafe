package com.example.blogkimscafe.model.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface historydao extends JpaRepository<historyvo,Integer> {
    
}
