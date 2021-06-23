package com.example.blogkimscafe.service;




import java.util.ArrayList;
import java.util.List;
import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.boardimage.boardimagevo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class boardservice {

    private final boolean yes=true;
    private final boolean no=false;
    private final int pagesize=3;

    @Autowired
    private boarddao boarddao;
    @Autowired
    private uploadimageservice uploadimageservice;

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertArticle(String email,boarddto boarddto,List<MultipartFile> file) {
        System.out.println(file.get(0).isEmpty()+"비었나요?");
        boolean emthy=file.get(0).isEmpty();
        List<boardimagevo>array=new ArrayList<>();
        boardvo boardvo=new boardvo(boarddto);
        try {
            if(emthy==false&&uploadimageservice.confrimOnlyImage(file)==false){
                System.out.println("imgage가아닌걸 발견함 등록");
                return no;   
            }else{
               array=uploadimageservice.uploadImage(file, boarddto, email);
               if(array==null){
                   return no;
               }
            }
            boardvo.setEmail(email);
            boarddao.save(boardvo);
            if(emthy==false){
                uploadimageservice.insertImageToDb(array, boardvo.getBid());
            }
            System.out.println(boardvo.getBid()+"게시글번호");
            return yes;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return no;
        
    }
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateArticle(String email,boarddto boarddto,int bid,List<MultipartFile>file,List<Integer>alreadyimages) {

        try {
           
            boardvo boardvo=boarddao.findById(bid).orElseThrow();
            if(email.equals(boardvo.getEmail())){
                uploadimageservice.deleteImage(alreadyimages, bid);
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
