package com.example.blogkimscafe.model;

import com.nimbusds.jose.shaded.json.JSONObject;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class naverDto {
    private String resultcode;
    private String message;
    private JSONObject response;
   

}
