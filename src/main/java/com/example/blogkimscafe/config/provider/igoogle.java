package com.example.blogkimscafe.config.provider;

import java.util.Map;

public class igoogle implements ioauth2 {

    private Map<String,Object>attributes;//oauth2user의 getattribute받는다

    public igoogle(Map<String,Object>attributes){
          this.attributes=attributes;
    }
    @Override
    public String getProviderid() {
       
        return (String)attributes.get("sub");
    }

    @Override
    public String getProvider() {
       
        return "google";
    }

    @Override
    public String getEmail() {
       
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
       
        return (String)attributes.get("name");
    }
    
}
