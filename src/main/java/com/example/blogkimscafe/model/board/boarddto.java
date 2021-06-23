package com.example.blogkimscafe.model.board;




import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class boarddto {
    
    
    @Size(min = 1, message = "1000글자 이하로 입력해주세요")
    private String content;

    @Size(min = 1, max = 30, message = "30글자 이하로 입력해주세요")
    private String title;

}
