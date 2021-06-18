package com.example.blogkimscafe.model.comment;

import java.sql.Timestamp;

import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

import javax.persistence.Entity;


@Data
@Entity
@Table(name="blogcomment")
public class commentvo {
    
    @Id////이거 꼭javax.persistence.Id;로해야한다 다른게 들어가있어서 당홨네 persistence 기억하자 20210517
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cid",nullable = false)
    private int cid;

    @Column(name="bid",nullable = false)
    private int bid;

    @Column(name="email",nullable = false,length = 50)
    private String email;

    @Column(name="comment",nullable = false,length = 50)
    private String comment;

    @CreationTimestamp
    private Timestamp created;

    public commentvo () {}

    public commentvo (commentdto commentdto,String email) {
        this.bid=commentdto.getBid();
        this.comment=commentdto.getComment();
        this.email=email;
    }
    
}