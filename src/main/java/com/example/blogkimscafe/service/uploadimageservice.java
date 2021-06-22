package com.example.blogkimscafe.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.example.blogkimscafe.model.board.boarddto;
import com.example.blogkimscafe.model.boardimage.boardimagedao;
import com.example.blogkimscafe.model.boardimage.boardimagevo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class uploadimageservice {

    private final String saveUrl="C:/Users/Administrator/Desktop/blog/blogkimscafe/src/main/resources/static/images/";
    private final String saveDbName="http://localhost:8080/static/images/";
    
    @Autowired
    private boardimagedao boardimagedao;
    @Autowired
    private utilservice utilservice;
 
    public boolean confrimOnlyImage(List<MultipartFile> file) {
        for(int i=0;i<file.size();i++){
            String filename=file.get(i).getOriginalFilename();
            if(file.get(i).getContentType().split("/")[0].equals("image")){
                System.out.println(filename+"이미지가 맞습니다");
            }else{
                return false;
            }
        }
            return true;   
    }
    public List<boardimagevo> uploadImage(List<MultipartFile> file,boarddto boarddto,String email) {
        try {
            List<boardimagevo>array=new ArrayList<>();
            for(MultipartFile f:file){
                String filename=f.getOriginalFilename();
                String savename=utilservice.getUUID()+filename;
                f.transferTo(new File(saveUrl+savename));
                boardimagevo boardimagevo=new boardimagevo(boarddto,email,saveDbName+savename);
                array.add(boardimagevo);
            }
            return array;
          
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean insertImageToDb(List<boardimagevo>array,int bid) {
        try {
            for(boardimagevo b:array){
                b.setBid(bid);
                boardimagedao.save(b);
            }
            return true;
        } catch (Exception e) {
            System.out.println("예외발생");
           throw new RuntimeException("사진저장중 예외발생");
        }
    }
}
