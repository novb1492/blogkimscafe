package com.example.blogkimscafe.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.blogkimscafe.model.naverDto;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class naverLoingService {
    
    private final String id="DrqDuzgTpM_sfreaZMly";
    private final String pwd="wCLQZ1kaQT";
    private final String callBackUrl="http://localhost:8080/auth/navercallback";

    private RestTemplate restTemplate=new RestTemplate();
    private HttpHeaders headers=new HttpHeaders();

    @Autowired
    private userservice userservice;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public String naverLogin() {
        String state="";
        try {
            state = URLEncoder.encode(callBackUrl, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        JSONObject naverDto=new JSONObject();
        naverDto.put("id", id);
        naverDto.put("state", state);
        naverDto.put("callbackUrl", callBackUrl);
        String naver="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+id+"&redirect_uri="+""+callBackUrl+""+"&state="+state+"";
        return naver;
    }
    public JSONObject getNaverToken(String code,String state) {
         JSONObject jsonObject= restTemplate.getForObject("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id="+id+"&client_secret="+pwd+"&code="+code+"&state="+state+"",JSONObject.class);
         System.out.println(jsonObject+" token"); 
         return jsonObject;
     }
     public void LoginNaver(JSONObject jsonObject) {
        headers.add("Authorization", "Bearer "+jsonObject.get("access_token"));
        HttpEntity<JSONObject>entity=new HttpEntity<JSONObject>(headers);
        try {
           naverDto naverDto =restTemplate.postForObject("https://openapi.naver.com/v1/nid/me",entity,naverDto.class);
           System.out.println(naverDto+ "정보");
           String email=(String)naverDto.getResponse().get("email");
           String[] email2=email.split("@");
           if(!email2[1].equals("naver.com")){
               email=email2[0]+"@naver.com";
           }
           if(userservice.confrimEmail(email)==false){
               String num=(String) naverDto.getResponse().get("mobile_e164");
               String[] num2=num.split("2");
               userservice.insertOauthLogin((JSONObject)naverDto.getResponse(),email,pwd,"0"+num2[1]);
           }
           Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
           SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            headers.clear();
        }
     }
     
}
