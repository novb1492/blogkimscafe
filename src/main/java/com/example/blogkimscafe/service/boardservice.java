package com.example.blogkimscafe.service;





import java.util.ArrayList;
import java.util.List;

import com.example.blogkimscafe.enums.responResultEnum;
import com.example.blogkimscafe.model.board.boarddao;
import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.board.boardvo;
import com.example.blogkimscafe.model.boardimage.boardimagevo;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class boardservice {

    private final int pagesize=3;

    @Autowired
    private boarddao boarddao;
    @Autowired
    private uploadimageservice uploadimageservice;
    @Autowired
    private utilservice utilservice;
    @Autowired
    private commentservice commentservice;

    @Transactional(rollbackFor = {Exception.class})
    public JSONObject insertArticle(String email,boarddto boarddto,List<MultipartFile> file) {
        boolean emthy=file.get(0).isEmpty();
        System.out.println(emthy+"비었나요?");

        List<boardimagevo>array=new ArrayList<>();
        boardvo boardvo=new boardvo(boarddto);
        try {
            if(emthy==false){
                if(uploadimageservice.confrimOnlyImage(file)){
                    System.out.println("imgage가아닌걸 발견함 등록");
                    return utilservice.makeJson(responResultEnum.findNoImageFile.getBool(), responResultEnum.findNoImageFile.getMessege());   
                }else{
                    System.out.println("사진 저장 시작");
                   array=uploadimageservice.insertImageLocal(file, boarddto, email);
                }
            }
            boardvo.setEmail(email);
            boarddao.save(boardvo);
            if(emthy==false){
                uploadimageservice.insertImageToDb(array, boardvo.getBid());
            }
            return utilservice.makeJson(responResultEnum.sucInsertArticle.getBool(), responResultEnum.sucInsertArticle.getMessege());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("insertArticle 글 등록에 실패 했습니다 잠시후 다시시도 바랍니다");
        }      
    }
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject updateArticle(String email,boarddto boarddto,int bid,List<MultipartFile>file,List<Integer>alreadyimages) {
       
        boardvo boardvo=getBoardVo(bid);
        confrimWriter(boardvo.getEmail(), email);
        uploadimageservice.deleteImage(alreadyimages, bid);
        try { 
            if(file.get(0).isEmpty()==false){
                boarddto.setBid(bid);
                List<boardimagevo>array=uploadimageservice.insertImageLocal(file, boarddto,email);
                uploadimageservice.insertImageToDb(array, bid);
            }
                boardvo.setTitle(boarddto.getTitle());
                boardvo.setContent(boarddto.getContent());
                return utilservice.makeJson(responResultEnum.sucUpdateArticle.getBool(), responResultEnum.sucUpdateArticle.getMessege());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("updateArticle 글 수정에 실패 했습니다 잠시후 다시시도 바랍니다");
        }  

    }
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject deleteArticle(boardvo boardvo,String email) {
        confrimWriter(boardvo.getEmail(),email);
        try {
            int bid=boardvo.getBid();
            boarddao.deleteById(bid);
            commentservice.deleteCommentByBid(bid);
            List<Integer>alreadyimages=new ArrayList<>();
            uploadimageservice.deleteImage(alreadyimages, bid);

            return utilservice.makeJson(responResultEnum.sucDeleteArticle.getBool(), responResultEnum.sucDeleteArticle.getMessege());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("deleteArticle 오류가 발생했습니다 다시시도 부탁드립니다");
        }
    }
    public boardvo getBoardVo(int bid) {
        return boarddao.findById(bid).orElseThrow(()->new RuntimeException("존재하지 않는 게시글입니다"));
    }
    public List<boardvo> getAllBordVo(String email) {
        return boarddao.findAllByEmail(email);
    }
    @Transactional
    public boardvo getArticle(int bid) {
        boardvo boardvo= getBoardVo(bid);
        try {
            boardvo.setHit(boardvo.getHit()+1);
            return boardvo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getArticle 오류가 발생했습니다 다시시도 부탁드립니다");
        }
    }
    public Page<boardvo> getBoard(int page) {
        try {
            return boarddao.findAll(PageRequest.of(page-1, pagesize,Sort.by(Sort.Direction.DESC,"bid")));
        } catch (Exception e) {
            throw new RuntimeException("getBoard 목록을 불러오는데 실패했습니다 다시시도 바랍니다");
        }
    }
    public int getSearchAtBoardCount(String title){
        return utilservice.getTotalpages(boarddao.countByTitleLikeNative(title), pagesize);
    }
    public List<boardvo> getSearchAtBoard(String title,int page,int totalpage) {
        List<boardvo>array=new ArrayList<>();
        try {
            if(totalpage>1){
              int fisrt=utilservice.getFirst(page, pagesize);
                array=boarddao.findByTitleLikeOrderByBidLimitNative(title,fisrt-1,utilservice.getEnd(fisrt, pagesize)-fisrt+1);
            }else{
                array=boarddao.findByTitleLikeOrderByBidNative(title);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException("getSearchAtBoard 오류가 발생했습니다 잠시 후 다시시도 바랍니다");
        }
    }
    private void confrimWriter(String dbEmail,String email) {
        if(email.equals(email)==false){
            throw new RuntimeException("confrimWriter 작성자가 일치 하지 않습니다");
        }
    }
   
   /* public String blobToString(Blob content) {
        try {
            return new String(content.getBytes(1, (int) content.length()));
        } catch (Exception e) {
            throw new RuntimeException("blobToString중 예외발생");
        }
    }
    private Blob stringToBlob(String content){
        try {
            return new SerialBlob(content.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("stringToBlob중 예외발생");
        }
    }*/
    

    
    
    
}
