package com.example.blogkimscafe.config.auth;

import com.example.blogkimscafe.model.user.userdao;
import com.example.blogkimscafe.model.user.uservo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class principalservice implements UserDetailsService{

    @Autowired
    private userdao userdao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        uservo uservo=userdao.findByEmail(username);
        if(uservo==null){
           throw new UsernameNotFoundException("존재하지 않습니다");
        }
       return new principaldetail(uservo);
    }
    
}
