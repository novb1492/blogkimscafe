package com.example.blogkimscafe.service;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        try {
            boardvo boardvo=new boardvo(boarddto);
            boardvo.setEmail(email);
            boarddao.save(boardvo);
            System.out.println(file+"file");
            if(file!=null){
                for(int i=0;i<file.size();i++)
                {
                    System.out.println(file.get(i).getOriginalFilename()+"파일이름");
                    System.out.println(file.get(i).getSize()+"파일사이즈");
                    if(file.get(i).getContentType().split("/")[0].equals("image")){
                        System.out.println(file.get(i).getOriginalFilename()+"이미지가 맞습니다");
                        String savename="2021"+file.get(i).getOriginalFilename();
                        file.get(i).transferTo(new File("C:/Users/Administrator/Desktop/blog/blogkimscafe/src/main/resources/static/images/"+savename));
                        boardimagevo boardimagevo=new boardimagevo(boardvo,"http://localhost:8080/static/images/"+savename);
                        boardimagedao.save(boardimagevo);
                        System.out.println("사진업로드");  
                    }
                }
            }
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
