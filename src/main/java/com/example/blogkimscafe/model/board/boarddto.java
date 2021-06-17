package com.example.blogkimscafe.model.board;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class boarddto {
    
    @NotBlank(message = "로그인후 사용해 주세요")
    @Email(message = "이메일이 올바르지 않습니다")
    private String email;
    
    @Size(min = 1, max = 1000, message = "1000글자 사이에서 입력해주세요")
    private String content;

    @Size(min = 1, max = 30, message = "30글자 사이에서 입력해주세요")
    private String title;

}
