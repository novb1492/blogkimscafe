package com.example.blogkimscafe.model.history;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.blogkimscafe.model.reservation.reservationvo;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "bloghistory")
@Entity
public class historyvo {
    
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "rid",nullable = false)
    int rid;

    @Column(name="email",nullable = false)
    String email;

    @Column(name = "seat",nullable = false)
    String seat;

    @Column(name = "requestTime",nullable = false)
    int requestTime;

    @Column(name = "requestDay",nullable = false)
    Timestamp requestDay;

    @Column(name = "imp_uid",nullable = false)
    private String imp_uid;

    @Column(name="created")
    @CreationTimestamp
    Timestamp created;

    @Column(name = "price",nullable = false)
    private int price;

    public historyvo(reservationvo reservationvo) {
        this.email=reservationvo.getEmail();
        this.requestTime=reservationvo.getRequesthour();
        this.requestDay=reservationvo.getReservationdatetime();
        this.rid=reservationvo.getId();
        this.seat=reservationvo.getSeat();
        this.imp_uid=reservationvo.getImp_uid();
        this.price=reservationvo.getPrice();
        
    }
}
