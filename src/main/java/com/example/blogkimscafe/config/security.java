package com.example.blogkimscafe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration//빈등록: 스프링 컨테이너에서 객체에서 관리
@EnableWebSecurity/////필터를 추가해준다
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정 주소 접근을 미리체크 한다  이3개는 셋트임 20210520
public class security extends WebSecurityConfigurerAdapter {

    @Autowired
    private oauth2service oauth2service;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
  @Bean
    public BCryptPasswordEncoder pwdEncoder() {

       return  new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
         .csrf().disable()///ajax사용하기 위해 토큰은 나중에
         .authorizeRequests()////요청이발생
         .antMatchers("/","/auth/**","/static/**")////이 링크들은
         .permitAll()///허용한다
         .anyRequest()///그외 다른 요청운
         .authenticated()//인증이있어야한다(로그인)     
      .and()
         .formLogin()////로그인 발생시
         .loginPage("/auth/loginpage")///로그인페이지 지정
         .loginProcessingUrl("/auth/loginprocess")//여기로된링크를 가로채서    
         .defaultSuccessUrl("/")//성공시 여기로보낸다  
      .and()
         .oauth2Login()
         .userInfoEndpoint()
         .userService(oauth2service);
    }


}
