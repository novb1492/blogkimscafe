package com.example.blogkimscafe.enums;





public enum responResultEnum {
    
    
    sucSingUp("회원가입 성공",true),
    alreadyEmail("이미 존재하는 이메일 입니다",false),
    sucUpdatePwd("비밀번호 변경성공 ",true),
    notEqualsNpwd("새 비밀번호가 다릅니다",false),
    notEqalsPwd("현재 비밀번호가 틀립니다",false),
    emailCheckIsTrue("",true),
    emailCheckIsFalse("이메일 인증 부탁드립니다",false),
    notExistsUser("존재하지 않는 사용자입니다",false),
    sendTempNumToEmail("이메일로 인증번호를 전송했습니다",true),
    rightTempNum("이메일인증이 완료 되었습니다",true),
    wrongTempNum("인증번호를 다시 확인해 주세요",false),
    sendTempPwd("임시비밀번호를 이메일로 전송했습니다",true);
   
   
    private final String messege;
    private final boolean torf;
   

    responResultEnum(String messege,boolean torf){
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