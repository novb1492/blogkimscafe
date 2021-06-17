package com.example.blogkimscafe.service;



import java.util.ArrayList;
import java.util.List;

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
    public int getSearchAtBoardCount(String title){
        int totalpage=0;
        try {
            int count=boarddao.countByTitleLikeNative(title);
            totalpage=count/pagesize;
            if(count%pagesize>0){
                totalpage++;
            }
            return totalpage;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return 1;
    }
    public List<boardvo> getSearchAtBoard(String title,int page,int totalpage) {
        List<boardvo>array=new ArrayList<>();
        try {
            int fisrt=0,end=0;
            if(totalpage>1){
                fisrt=(page-1)*pagesize+1;
                end=fisrt+pagesize-1; 
                array=boarddao.findByTitleLikeOrderByBidLimitNative(title,fisrt-1,end-fisrt+1);
            }else{
                array=boarddao.findByTitleLikeOrderByBidNative(title);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return array;
    }
    
    
}
