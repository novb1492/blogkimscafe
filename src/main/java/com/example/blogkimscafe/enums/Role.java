package com.example.blogkimscafe.enums;

public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    failEmailSmsCheck("false"),
    sucEmailSmsCheck("true");
    private final String value;
    
    Role(String value){
        this.value = value;      
    }
    
    public String getValue(){
        return value;
    }
}
