package com.example.blogkimscafe.model.reservation.seat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Table(name="blogseatinfor")
@Entity
public class seatInforVo {
    
    @Id
    @Column(name="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)///테이블은 여기서 만들고 mysql에서 오토인크리먼트하면된다 자동으로 들어기도하네 주
    private int id;                    

    @Column(name = "seat",nullable = false)
    private String seat;

    @Column(name = "limited",nullable = false)
    private int limited;

    @Column(name="price",nullable = false)
    private int price;


}
