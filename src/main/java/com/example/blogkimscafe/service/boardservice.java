package com.example.blogkimscafe.service;



import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.board.boardvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class boardservice {

    private boolean yes=true;
    private boolean no=false;
    private final int pagesize=3;

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
    public Page<boardvo> getBoard(int page) {
        try {
            return boarddao.findAll(PageRequest.of(page-1, pagesize,Sort.by(Sort.Direction.DESC,"bid")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
