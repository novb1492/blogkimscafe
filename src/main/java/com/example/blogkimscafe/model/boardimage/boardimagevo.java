package com.example.blogkimscafe.model.boardimage;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.blogkimscafe.model.board.boardvo;

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

    @Column(name = "imagename")
    private String imagename;

    @Column(name="created")
    @CreationTimestamp
    private Timestamp created;

    public boardimagevo(){};
    public  boardimagevo(boardvo boardvo,String imagename) {
        this.bid=boardvo.getBid();
        this.imagename=imagename;
        this.title=boardvo.getTitle();
    }

 


}
