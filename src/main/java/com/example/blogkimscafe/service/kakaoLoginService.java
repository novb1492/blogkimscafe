package com.example.blogkimscafe.service;

import java.util.LinkedHashMap;

import com.example.blogkimscafe.model.oauthLogin.kakaoAccountDto;
import com.example.blogkimscafe.model.oauthLogin.kakaoLoginDto;
import com.example.blogkimscafe.model.oauthLogin.kakaoToketnDto;
import com.nimbusds.jose.shaded.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private userservice userservice;
    @Autowired
    private utilservice utilservice;

    public String kakaoGetCode() {
        try {
            return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+apikey+"&redirect_uri="+callBackUrl+"";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("kakaoGetCode 오류 발생");
        }
    }
    public kakaoToketnDto getKakaoToken(String code) {
        System.out.println(code+" kakaocode");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        body.add("grant_type", "authorization_code");
        body.add("client_id", apikey);
        body.add("redirect_uri", callBackUrl);
        body.add("code", code);
        try {
            HttpEntity<MultiValueMap<String,String>>entity=new HttpEntity<>(body,headers);
            kakaoToketnDto kakaoLoginDto=restTemplate.postForObject("https://kauth.kakao.com/oauth/token",entity,kakaoToketnDto.class);
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
    public boolean getKakaoProfile(kakaoToketnDto kakaoToketnDto) {
        headers.add("Authorization", "Bearer "+kakaoToketnDto.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try {
            HttpEntity<MultiValueMap<String,String>>entity=new HttpEntity<>(headers);
            kakaoLoginDto kakaoLoginDto =restTemplate.postForObject("https://kapi.kakao.com/v2/user/me",entity,kakaoLoginDto.class);
            System.out.println(kakaoLoginDto+" kakaoprofile");
           
            kakaoAccountDto kakaoAccountDto =new kakaoAccountDto((boolean)kakaoLoginDto.getKakao_account().get("email_needs_agreement"),(boolean)kakaoLoginDto.getKakao_account().get("profile_nickname_needs_agreement"),(LinkedHashMap<String,String>)kakaoLoginDto.getKakao_account().get("profile"),(boolean)kakaoLoginDto.getKakao_account().get("is_email_valid"),(boolean)kakaoLoginDto.getKakao_account().get("is_email_verified"),(boolean)kakaoLoginDto.getKakao_account().get("has_email"),(String)kakaoLoginDto.getKakao_account().get("email"));
            System.out.println(kakaoAccountDto+" kakaoAccountDto");

            String email=kakaoAccountDto.getEmail();
            if(userservice.confrimEmail(email)==false){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id", kakaoLoginDto.getId());
                jsonObject.put("name", kakaoAccountDto.getProfile().get("nickname"));
                jsonObject.put("provider", "kakao");
                userservice.insertOauthLogin(jsonObject,email,apikey,"010-테스트중-못받음");
            }
            utilservice.setAuthentication(email, apikey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
