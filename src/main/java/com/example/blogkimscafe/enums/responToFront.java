package com.example.blogkimscafe.enums;





public enum responToFront {
    
    successSingUp("회원가입 성공",true);
   
   
    private final String messege;
    private final boolean torf;
   

    responToFront(String messege,boolean torf){
        this.messege=messege;
        this.torf=torf;
    }
    public String getMessege() {
        return messege;
    }
    public Boolean getBool() {
        return torf;
    }
    

    
}
