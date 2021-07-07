package com.example.blogkimscafe.service;



import com.example.blogkimscafe.model.reservation.IamprotDto;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class iamportservice {
    
    private final String imp_key="7336595505277037";
    private final String imp_secret="19412b4bca453060662162083d1ccc8ee7c53bd98a2f33faedd7ebc3e6ad4c359c36f899ebd6ddec";

    public void getToken() {
         RestTemplate restTemplate=new RestTemplate();

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject body=new JSONObject();
        body.put("imp_key", imp_key);
        body.put("imp_secret", imp_secret);
        try {  
            HttpEntity<JSONObject>entity=new HttpEntity<>(body,headers);
            IamprotDto token=restTemplate.postForObject("https://api.iamport.kr/users/getToken",entity,IamprotDto.class);
            
            System.out.println(token+" FULLtoken");
            System.out.println(token.getResponse().get("access_token")+" token");
           

            
            //restTemplate.getForObject("https://api.iamport.kr/payments/"+imp_uid+"",JSONObject.class);
      
        } catch (Exception e) {
            e.printStackTrace();
           
        }
    }
}
