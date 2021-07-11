package com.example.blogkimscafe.service;

import com.example.blogkimscafe.model.oauthLogin.kakaoLoginDto;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class kakaoLoginService {

    private final String apikey="2b8214590890931fb474d08986898680";
    private final String callBackUrl="http://localhost:8080/auth/kakaocallback";
    private RestTemplate restTemplate=new RestTemplate();
    private HttpHeaders headers=new HttpHeaders();
    private MultiValueMap<String,String> body=new LinkedMultiValueMap<>();

    public String kakaoGetCode() {
        try {
            return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+apikey+"&redirect_uri="+callBackUrl+"";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("kakaoGetCode 오류 발생");
        }
    }
    public kakaoLoginDto getKakaoToken(String code) {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        body.add("grant_type", "authorization_code");
        body.add("client_id", apikey);
        body.add("redirect_uri", callBackUrl);
        body.add("code", code);
        try {
            HttpEntity<MultiValueMap<String,String>>entity=new HttpEntity<>(body,headers);
            kakaoLoginDto kakaoLoginDto=restTemplate.postForObject("https://kauth.kakao.com/oauth/token",entity,kakaoLoginDto.class);
            System.out.println(kakaoLoginDto+" kakaotoken");
            return kakaoLoginDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getKakaoToken 오류가 발생했습니다");
        }finally{
            headers.clear();
            body.clear();
        }
    }
    public void getKakaoProfile(kakaoLoginDto kakaoLoginDto) {
        headers.add("Authorization", "Bearer "+kakaoLoginDto.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try {
            HttpEntity<MultiValueMap<String,String>>entity=new HttpEntity<>(headers);
            JSONObject jsonObject=restTemplate.postForObject("https://kapi.kakao.com",entity,JSONObject.class);
            System.out.println(jsonObject+" kakaoprofile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
