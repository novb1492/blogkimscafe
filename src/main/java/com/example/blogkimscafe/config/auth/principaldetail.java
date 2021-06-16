package com.example.blogkimscafe.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.example.blogkimscafe.model.user.uservo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;


@Data
public class principaldetail implements UserDetails,OAuth2User{

    private uservo uservo;
    private Map<String,Object>attributtes;
 
    public principaldetail(uservo uservo)
    {
        this.uservo=uservo;
    }
    public principaldetail(uservo uservo,Map<String,Object>attributes){
        this.uservo =uservo ;
        this.attributtes=attributes;
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
    @Override
    public Map<String, Object> getAttributes() {
         return attributtes;
    }
    @Override
    public String getName() {

        return (String)this.attributtes.get("name");
    }



    
    
}
