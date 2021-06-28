package com.example.blogkimscafe.model.reservation;

import java.sql.Timestamp;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class reservationdto {
    
    @NotBlank
    private String seat;
    
    private int rid;    
  
    @NotNull
    private int requesthour;
    
    private Timestamp reservationdatetime;
}
