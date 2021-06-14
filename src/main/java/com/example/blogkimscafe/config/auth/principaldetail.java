package com.example.blogkimscafe.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import com.example.blogkimscafe.model.user.uservo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;


@Data
public class principaldetail implements UserDetails{

    private uservo uservo;
 
    public principaldetail(uservo uservo)
    {
        this.uservo=uservo;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority>collet=new ArrayList<>();
        collet.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                return uservo.getRole();
            }
            
        });
        return collet;
    }

    @Override
    public String getPassword() {
        return uservo.getPwd();
    }

    @Override
    public String getUsername() {
        return uservo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    
    
}
