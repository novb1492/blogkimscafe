package com.example.blogkimscafe.service;

import java.util.ArrayList;
import java.util.List;

import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.comment.commentdao;
import com.example.blogkimscafe.model.comment.commentdto;
import com.example.blogkimscafe.model.comment.commentvo;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class commentservice {

    private final int pagesize=3;

    @Autowired
    private commentdao commentdao;
    @Autowired
    private utilservice utilservice;

    public JSONObject insertComment(commentdto commentdto,String email) {
        try {
            commentvo commentvo=new commentvo(commentdto,email);
            commentdao.save(commentvo);
            return utilservice.makeJson(responResultEnum.sucInsertComment.getBool(), responResultEnum.sucInsertComment.getMessege());
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    @Transactional
    public JSONObject updateComment(commentdto commentdto,String email) {
        commentvo commentvo=commentdao.findById(commentdto.getCid()).orElseThrow(()->new RuntimeException("존재 하지 않은 댓글입니다"));
        confirmWriter(commentvo.getEmail(), email);
        try {
            commentvo.setComment(commentdto.getComment());
            return utilservice.makeJson(responResultEnum.sucUpdateComment.getBool(),responResultEnum.sucUpdateComment.getMessege());
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    public JSONObject deleteCommentByCid(int cid,String email) {
        commentvo commentvo=commentdao.findById(cid).orElseThrow(()->new RuntimeException("존재하지 않는 댓글입니다"));
        confirmWriter(commentvo.getEmail(), email);
        try {
            commentdao.deleteById(cid);
            return utilservice.makeJson(responResultEnum.sucDeleteCommnet.getBool(), responResultEnum.sucDeleteCommnet.getMessege());
        } catch (Exception e) {
           throw new RuntimeException("삭제중 문제가 생겼습니다");
        }  
    }
    public int totalCommentCount(int bid) {

        return utilservice.getTotalpages(commentdao.countByBid(bid), pagesize);
    }
    public List<commentvo> getComment(int bid,int page,int totalpages) {
       List<commentvo>array=new ArrayList<>();
       try {
            if(totalpages>1){
                int fisrt=utilservice.getFirst(page, pagesize);
                array=commentdao.getCommentNative(bid,fisrt-1,utilservice.getEnd(fisrt, pagesize)-fisrt+1);
            }else{
                array=commentdao.findByBidOrderByCidDesc(bid);
            }
                return array;
       } catch (Exception e) {
           throw new RuntimeException("댓글 불러오기에 실패했습니다 다시시도 바랍니다");
       }
    }
    public void deleteCommentByBid(int bid) {

        try {
            commentdao.deleteByBidNative(bid);
        } catch (Exception e) {
            throw new RuntimeException("오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    private void confirmWriter(String dbEmail,String email){
        if(email.equals(dbEmail)==false){
            throw new RuntimeException("작성자와 일치 하지 않습니다");
        }
    }
    
}
