package com.example.blogkimscafe.config;

import com.example.blogkimscafe.config.auth.principaldetail;
import com.example.blogkimscafe.model.user.userdao;
import com.example.blogkimscafe.model.user.uservo;
import com.example.blogkimscafe.service.utilservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//구글로그인 버튼 클릭->구글로그인창->로그인완료->code(ouath-client라이브러리)가받음->accesstoken요청
//accesstoken받음=userequset정보임->회원프로필 받아야함->loadUser호출->회원프로필받아줌
@Service
public class oauth2service extends DefaultOAuth2UserService {
    
    @Autowired
    private userdao userdao;
    @Autowired
    private utilservice utilservice;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userrequest="+userRequest.getClientRegistration());///어떤 oauth로 로그인했는지 확인가능 ex google이면 google이라고 나옴
        System.out.println("userrequestaccesstoken="+userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User=super.loadUser(userRequest);///얘가 우리프로필을 다들고있는거다
        System.out.println("oauth2로그인시도 "+oAuth2User);
        String email=(String)oAuth2User.getAttributes().get("email");
        uservo uservo=new uservo();
        if(userdao.findByEmail(email)==null){
            System.out.println(email+"은 처음임 회원가입 진행");
            uservo.setEmail(email);
            uservo.setEmailcheck("true");
            uservo.setName((String)oAuth2User.getAttributes().get("name"));
            uservo.setProvider("google");
            uservo.setProviderid((String)oAuth2User.getAttributes().get("sub"));
            uservo.setPwd("절대노출되지않는비번으로해줘야한다");
            uservo.setRandnum(utilservice.GetRandomNum(6));
            uservo.setRole("ROLE_USER");
            userdao.save(uservo);
        }
        return new principaldetail(uservo, oAuth2User.getAttributes());///principal이니 가능하다 이걸하면 authen거기로
    }
}
