package com.example.blogkimscafe.model.board;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
@Data
@Table(name="blogboard")
@Entity
public class boardvo {
    
    @Id
    @Column(name = "bid",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bid;

    @Column(name="email",nullable = false,length = 50)
    private String email;

    @Column(name="content",nullable = false,length = 1000)
    private String content;

    @Column(name = "title",nullable = false,length = 100)
    private String title;

    @Column(name = "hit",nullable = false)
    @ColumnDefault("0")
    private int hit;

    @Column(name="created",nullable = false)
    @CreationTimestamp
    private Timestamp created;

    public boardvo (){}
    public boardvo (boarddto boarddto) {
        this.email=boarddto.getEmail();
        this.content=boarddto.getContent();
        this.title=boarddto.getTitle();
    }

 


}
