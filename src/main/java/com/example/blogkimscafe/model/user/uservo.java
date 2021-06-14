package com.example.blogkimscafe.model.user;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Data
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

    public uservo () {}

    public uservo (userdto userdto) {
        this.setEmail(userdto.getEmail());
        this.setName(userdto.getName());
        this.setProvider(userdto.getProvider());
        this.setProviderid(userdto.getProviderid());
        this.setEmailcheck(userdto.getEmailcheck());
        this.setRandnum(userdto.getRandnum());
        this.setPwd(userdto.getPwd());
        this.setRole(userdto.getRole());
    
    }
}
