package com.example.blogkimscafe.service;

import java.util.ArrayList;
import java.util.List;



import com.example.blogkimscafe.model.comment.commentdao;
import com.example.blogkimscafe.model.comment.commentdto;
import com.example.blogkimscafe.model.comment.commentvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class commentservice {

    private final int pagesize=3;
    private final boolean yes=true;


    @Autowired
    private commentdao commentdao;

    @Transactional(rollbackFor = {Exception.class}) 
    public boolean insertComment(commentdto commentdto,String email) {

        try {

            commentvo commentvo=new commentvo(commentdto,email);
            commentdao.save(commentvo);
            return yes;
        } catch (Exception e) {
            throw new RuntimeException("등록중 문제가 생겼습니다");
        }
     
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public boolean updateComment(commentdto commentdto,String email) {

        try {
            commentvo commentvo=commentdao.findById(commentdto.getCid()).orElseThrow(()->new Exception("존재 하지 않은 댓글입니다"));
            if(email.equals(commentvo.getEmail())){
                commentvo.setComment(commentdto.getComment());
                commentdao.save(commentvo);
                return yes;
            }
            throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException("댓글 수정중 예외발생");
        }
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public boolean deleteCommentByCid(int cid,String email) {
        try {
            if(commentdao.findById(cid).orElseThrow(()->new Exception("존재하지 않는 댓글입니다")).getEmail().equals(email)){
                commentdao.deleteById(cid);
                return yes;
            }
            return yes;
        } catch (Exception e) {
           throw new RuntimeException("삭제중 문제가 생겼습니다");
        }  
    }
    public int totalCommentCount(int bid) {

        int count=commentdao.countByBid(bid);
        int totalpages=count/pagesize;
        if(count%pagesize>0){
            totalpages++;
        }
        System.out.println(bid+"글의 총 댓글"+count);
        System.out.println("댓글총페이지"+totalpages);
        return totalpages;
    }
    public List<commentvo> getComment(int bid,int page,int totalpages) {
       List<commentvo>array=new ArrayList<>();
       int fisrt=0,end=0;
       try {
            if(totalpages>1){
                fisrt=(page-1)*pagesize+1;
                end=fisrt+pagesize-1; 
                array=commentdao.getCommentNative(bid,fisrt-1,end-fisrt+1);
            }else{
                array=commentdao.findByBidOrderByCid(bid);
            }
                return array;
       } catch (Exception e) {
           e.printStackTrace();
       }
     
        return null;
    }
    @Transactional(rollbackFor = {Exception.class}) 
    public boolean deleteCommentByBid(int bid) {

        try {
            commentdao.deleteBybidNative(bid);
            return yes;
        } catch (Exception e) {
            throw new RuntimeException("삭제중 문제가 생겼습니다");
        }
    }
    
}
