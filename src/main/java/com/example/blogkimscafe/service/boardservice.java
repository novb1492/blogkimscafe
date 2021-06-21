package com.example.blogkimscafe.service;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.boardimage.boardimagedao;
import com.example.blogkimscafe.model.boardimage.boardimagevo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class boardservice {

    private boolean yes=true;
    private boolean no=false;
    private final int pagesize=3;

    @Autowired
    private boarddao boarddao;
    @Autowired
    private boardimagedao boardimagedao;

    public boolean insertArticle(String email,boarddto boarddto,List<MultipartFile> file) {
        System.out.println(file.get(0).isEmpty()+"비었나요?");
        String filename=null;
        int bid=0;
        boolean emthy=file.get(0).isEmpty();
        try {
            if(emthy==false){
                for(int i=0;i<file.size();i++){
                    filename=file.get(i).getOriginalFilename();
                    if(file.get(i).getContentType().split("/")[0].equals("image")){
                        System.out.println(filename+"이미지가 맞습니다");
                    }else{
                        System.out.println("imgage가아닌걸 발견함");
                        throw new Exception("imgage가아닌걸 발견함");
                    }
                }
            }
            boardvo boardvo=new boardvo(boarddto);
            boardvo.setEmail(email);
            boarddao.save(boardvo);
            bid=boardvo.getBid();
            if(emthy==false){
                for(int i=0;i<file.size();i++){
                    filename=file.get(i).getOriginalFilename();
                    String savename="2021"+filename;
                    file.get(i).transferTo(new File("C:/Users/Administrator/Desktop/blog/blogkimscafe/src/main/resources/static/images/"+savename));
                    boardimagevo boardimagevo=new boardimagevo(boardvo,"http://localhost:8080/static/images/"+savename);
                    boardimagedao.save(boardimagevo);
                    System.out.println("사진업로드");  
                    System.out.println(file+"file");
                }
            }
            System.out.println(boardvo.getBid()+"게시글번호");
            return yes;
        } catch (Exception e) {
            System.out.println("등록중 예외발생");
            boarddao.deleteById(bid);
           e.printStackTrace();
        }
        return no;
        
    }
    @Transactional
    public boolean updateArticle(String email,boarddto boarddto,int bid) {

        try {
            boardvo boardvo=boarddao.findById(bid).orElseThrow();
            if(email.equals(boardvo.getEmail())){
                boardvo.setTitle(boarddto.getTitle());
                boardvo.setContent(boarddto.getContent());
                return yes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return no;
        
    }
    @Transactional
    public boardvo getArticle(int bid) {
        try {
            boardvo boardvo= boarddao.findById(bid).orElseThrow();
            boardvo.setHit(boardvo.getHit()+1);
            return boardvo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
