package com.example.blogkimscafe.model.user;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.blogkimscafe.config.provider.ioauth2;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table(name="blogusers")
@Entity
public class uservo {
    
    @Id
    @Column(name="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)///테이블은 여기서 만들고 mysql에서 오토인크리먼트하면된다
    private int id;

    @Column(name="email",length = 30,unique=true,nullable = false)
    private String email;

    @Column(name="pwd",length = 100,nullable = false )
    private String pwd;

    @Column(name="name",length = 20,nullable = false)
    private String name;

    @Column(name="created")
    @CreationTimestamp  
    private Timestamp created;

    @Column(name="role",nullable=false)
    private String role;

    @Column(name="provider")
    private String provider;
    
    @Column(name="providerid")
    private String providerid;
    
    @Column(name="emailcheck")
    private String emailcheck;
    
    @Column(name="randnum")
    private String randnum;

    @Column(name="phone")
    private String phone;

    @Column(name="phonecheck")
    private String phonecheck;

    public uservo () {}

    public uservo (userdto userdto) {
        this.email=userdto.getEmail();
        this.name=userdto.getName();
        this.pwd=userdto.getPwd();
    }
    public uservo (ioauth2 ioauth2,String pwd,String randnum,String role) {
        this.email=ioauth2.getEmail();
        this.emailcheck="true";
        this.name=ioauth2.getName();
        this.provider=ioauth2.getProvider();
        this.providerid=ioauth2.getProviderid();
        this.pwd=pwd;
        this.randnum=randnum;
        this.role=role;
    }
}
