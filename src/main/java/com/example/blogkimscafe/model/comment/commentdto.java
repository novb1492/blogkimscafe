package com.example.blogkimscafe.model.comment;


import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class commentdto {
    
    private int bid;
 
    @Size(min = 1,max = 50, message = "댓글은 1~50글자사이 입니다")
    @NotBlank(message = "댓글창이 빈칸입니다")
    private String comment;

}
