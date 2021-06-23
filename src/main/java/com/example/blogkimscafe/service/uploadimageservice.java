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
    @Transactional(rollbackFor = {Exception.class})
    public List<boardimagevo> uploadImage(List<MultipartFile> file,boarddto boarddto,String email) {
        try {
            List<boardimagevo>array=new ArrayList<>();
            for(MultipartFile f:file){
                String filename=f.getOriginalFilename();
                String savename=utilservice.getUUID()+filename;
                f.transferTo(new File(saveUrl+savename));
                boardimagevo boardimagevo=new boardimagevo(boarddto,email,saveDbName+savename,savename);
                array.add(boardimagevo);
            }
            return array;
          
        }catch (Exception e) {
            throw new RuntimeException("사진 로컬에 저장중 예외발생");
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void insertImageToDb(List<boardimagevo>array,int bid) {
        try {
            for(boardimagevo b:array){
                b.setBid(bid);
                boardimagedao.save(b);
            } 
        } catch (Exception e) {
           throw new RuntimeException("사진 db에 저장중 예외발생");
        }
    }
   
    public void deleteImage(List<Integer>alreadyimages,int bid) {
        try {
         
            List<Integer>array=boardimagedao.findByBidIdNative(bid);
            for(int i=0;i<alreadyimages.size();i++){
                System.out.println(array.contains(alreadyimages.get(i))+"존재함");
                if(array.contains(alreadyimages.get(i))){
                    array.remove(alreadyimages.get(i));
                }
            }
          
            System.out.println("삭제예정"+array);
            for(int i=0;i<array.size();i++){
                boardimagedao.deleteById(array.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("사진 db에 삭제중 예외발생");
        }
        
    }
}
