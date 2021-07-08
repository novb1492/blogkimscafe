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
                return true;

            }
        }
        return false;  
    }
    public List<boardimagevo> insertImageLocal(List<MultipartFile> file,boarddto boarddto,String email) {
        try {
            List<boardimagevo>array=new ArrayList<>();
            for(MultipartFile f:file){
                String filename=f.getOriginalFilename();
                String savename=utilservice.getUUID()+filename;
                String localLocation=saveUrl+savename;
                f.transferTo(new File(localLocation));
                boardimagevo boardimagevo=new boardimagevo(boarddto,email,saveDbName+savename,savename,localLocation);
                array.add(boardimagevo);
            }
            return array;
          
        }catch (Exception e) {
            e.printStackTrace();
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
    @Transactional(rollbackFor = {Exception.class})
    public void deleteImage(List<Integer>alreadyimages,int bid) {
        List<boardimagevo>deleteImages=selectDeleteImage(alreadyimages, bid);
        try {
                if(deleteImages.isEmpty()==false){
                   for(boardimagevo b:deleteImages){
                       boardimagedao.deleteById(b.getId());
                       deleteLocalFile(b.getImagelocal());
                   }
                }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("사진 교체중 예외발생");
        }
        
    }
    private List<boardimagevo> selectDeleteImage(List<Integer>alreadyimages,int bid) {
        try {
            List<boardimagevo>deleteImages=new ArrayList<>();
            List<boardimagevo>dbImages=boardimagedao.findByBid(bid);
            if(alreadyimages.isEmpty()){/////////만약 비웠다면 기존사진이 다 삭제되어 온경우
                return dbImages;
            }
                for(int i=0;i<dbImages.size();i++){
                    for(int ii=0;ii<alreadyimages.size();ii++){ 
                        if(dbImages.get(i).getId()!=alreadyimages.get(ii)){
                            if(ii==alreadyimages.size()-1){
                                    deleteImages.add(dbImages.get(i));
                             }
                        }else{
                            break;
                        }
                            
                    }
                }
                return deleteImages;
        } catch (Exception e) {
            throw new RuntimeException("selectDeleteImage 예외발생");
        }
    }
    private void deleteLocalFile(String localLocation) {
        System.out.println(localLocation+"삭제경로");
        File file=new File(localLocation);
        try {
            if(file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            System.out.println("사진 삭제중 예외 발생");
            throw new RuntimeException("deleteLocalFile 예외발생");
        }
            
    }
}

