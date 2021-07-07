package com.example.blogkimscafe.service;

import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            ResponseEntity<JSONObject> token=restTemplate.postForEntity("https://api.iamport.kr/users/getToken",entity,JSONObject.class);
            
            System.out.println(token+"fulltoken");
            System.out.println(token.getStatusCode()+"tgetsoken");
            System.out.println(token.getStatusCodeValue()+"getvaltoken");
            System.out.println(token.getBody()+"bodytoken");
            System.out.println(token.getBody().get("response")+"bodytoken");

            //restTemplate.getForObject("https://api.iamport.kr/payments/"+imp_uid+"",JSONObject.class);
      
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
