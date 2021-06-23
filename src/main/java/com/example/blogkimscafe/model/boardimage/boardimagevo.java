package com.example.blogkimscafe.model.boardimage;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.blogkimscafe.model.board.boarddto;


import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Table(name="blogboardimage")
@Entity
public class boardimagevo {
    
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bid",nullable = false)
    private int bid;

    @Column(name = "title",nullable = false,length = 30)
    private String title;

    @Column(name="email",nullable = false,length = 50)
    private String email;

    @Column(name = "imageurl",nullable = false)
    private String imageurl;

    @Column(name = "imagename",nullable = false)
    private String imagename;

    @Column(name = "imagelocal",nullable = false)
    private String imagelocal;

    @Column(name="created")
    @CreationTimestamp
    private Timestamp created;

    public boardimagevo(){};
    public  boardimagevo(boarddto boarddto,String email,String imageurl,String imagename,String imagelocal) {
        this.imageurl=imageurl;
        this.title=boarddto.getTitle();
        this.email=email;
        this.imagename=imagename;
        this.imagelocal=imagelocal;
    }
    

 


}
