package com.example.blogkimscafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class coolSmsService {
    
    private RestTemplate restTemplate=new RestTemplate();
    private HttpHeaders headers=new HttpHeaders();
    private JSONObject body=new JSONObject();
    private final String apikey="NCSFT0AZ2O3FHMAX";
    private final String APISecret="AHZNZ3IIMGSYIXFLR7HQDBYA5KPFSFCS";



    public void getToken() {
       
        String api_key = apikey;
        String api_secret = APISecret;
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", "01091443409");
        params.put("from", "01091443409");
        params.put("type", "SMS");
        params.put("text", "문자 내용");
  
        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            e.printStackTrace();
        }
       
    }

    public void sendMessege() {
        try {
            
           // headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            body.put("api_key", apikey);
            body.put("signature", "AHZNZ3IIMGSYIXFLR7HQDBYA5KPFSFCS");
            body.put("type","SMS");
            body.put("to", "01091443409");
            body.put("text", "kimscafe 입니다");
            HttpEntity<JSONObject>entity=new HttpEntity<>(body,headers);
            restTemplate.postForObject("https://api.coolsms.co.kr/sms/1.5/send",entity,JSONObject.class);
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException("오류가 발생 했습니다");
        }finally{
            System.out.println("finally");
            resetBodyAndHeader();
        }
    }
    private void resetBodyAndHeader(){
        headers.clear();
        body.clear();
    }
    private String getNowIso(){
        Date nowDate = new Date(); 
        System.out.println("포맷 지정 전 : " + nowDate); 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strNowDate = simpleDateFormat.format(nowDate);
        return strNowDate;
    }

}
