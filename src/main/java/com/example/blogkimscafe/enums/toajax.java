package com.example.blogkimscafe.enums;

public enum toajax {
    error("서버의 에러입니다",false),
    nullemail("존재하지 않는 이메일입니다",false),
    wrongpwd("잘못된 비밀번호 입니다",false),
    falseemailcheck("이메일인증 미사용자입니다",false),
    wrongnpwd("새비밀번호가 일치하지 않습니다",false),
    wrongrandnum("인증번호가 일치하지 않습니다",false);


    private final String messege;
    private final boolean torf;
    toajax(String messege,boolean torf){
        this.messege=messege;
        this.torf=torf;
    }
    public String getMessege() {
        return messege;
    }
    public boolean getTorf() {
        return torf;
    }
}
