package com.example.blogkimscafe.service;

import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.board.boardvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class boardservice {

    private boolean yes=true;
    private boolean no=false;

    @Autowired
    private boarddao boarddao;

    public boolean insertArticle(String email,boarddto boarddto) {
        try {
            boardvo boardvo=new boardvo(boarddto);
            boardvo.setEmail(email);
            boarddao.save(boardvo);
            return yes;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return no;
        
    }
    
}
