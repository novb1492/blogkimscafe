package com.example.blogkimscafe.model.reservation;

import com.nimbusds.jose.shaded.json.JSONObject;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class IamprotDto {
    private String code;
    private String message;
    private JSONObject response=new JSONObject();
    
}
