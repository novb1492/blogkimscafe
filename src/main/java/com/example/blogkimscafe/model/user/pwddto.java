package com.example.blogkimscafe.model.user;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class pwddto {
    
   @NotBlank
   private String pwd;
   @NotBlank
   private String npwd;
   @NotBlank
   private String npwd2;
}
