package com.example.blogkimscafe.model.reservation;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;




@AllArgsConstructor
@Data
@Table(name="blogreservation")
@Entity
public class reservationvo {
    
    @Id
    @Column(name="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)///테이블은 여기서 만들고 mysql에서 오토인크리먼트하면된다 자동으로 들어기도하네 주
    private int id;                                   //////주의 할점 아예생성시 붙히고 생성해라 안그러면  @CreationTimestamp가 난리침 20210524
    
    @Column(name="seat",nullable = false)
    private String seat;
    
    @Column(name="rname",nullable = false)
    private String name;

    @Column(name="remail",nullable = false)
    private String email;

    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;

    @Column(name = "requesthour",nullable = false)
    private int requesthour;
    
    @Column(name="reservationdatetime",nullable = false)
    private Timestamp reservationdatetime;

    @Column(name = "imp_uid",nullable = false)
    private String imp_uid;

    @Column(name = "price",nullable = false)
    private int price;

    public reservationvo(){}

    public reservationvo (reservationdto reservationdto) {
        this.requesthour=reservationdto.getRequesthour();
        this.reservationdatetime=reservationdto.getReservationdatetime();
        this.seat=reservationdto.getSeat();
        this.price=reservationdto.getPrice();
    }

}
