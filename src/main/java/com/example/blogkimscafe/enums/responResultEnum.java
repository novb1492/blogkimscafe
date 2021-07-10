package com.example.blogkimscafe.enums;





public enum responResultEnum {
    
    /////////user/////////////////////////////////
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
    sendTempPwd("임시비밀번호를 이메일로 전송했습니다",true),
    notEqualsUser("사용자 정보가 일치 하지 않습니다", false),
    sucDeleteUser("회원 탈퇴 완료", true),
    //////////////board///////////////////////////////////
    findNoImageFile("이미지가 아닌 파일이 존재합니다 ",false),
    sucInsertArticle("게시글을 등록했습니다",true),
    notExistsArticle("존재하지 않은 게시물입니다",false),
    notEqualsWriter("작성자가 일치 하지 않습니다",false),
    sucUpdateArticle("글 수정에 성공했습니다",true),
    sucDeleteArticle("글 삭제에 성공했습니다",true),
    /////////////댓글/////////////////////////////////////
    sucInsertComment("댓글을 등록 했습니다",true),
    sucUpdateComment("댓글을 수정 했습니다",true),
    sucDeleteCommnet("댓글을 삭제 했습니다",true),
    ///////////////예약///////////////////////////////////
    sucInsertReservation("예약에 성공했습니다",true),
    beforeTime("지난 시간의 예약시도 입니다",false),
    alreadyTime("예약이 다 찼습니다",false),
    sucDeleteRerservation("에약 취소 성공",true),
    failDeleteReservation("예약자가 아니거나 잘못된 시간입니다",false),
    failConfrimBuyerInfor("상품금액과 결제 금액이 다릅니다",false),
    ExistsSeat("존재하는 자리입니다",true),
    notExistsSeat("존재하지 않은 자리입니다",false),
    existReservation("사용하지 않는 예약이 존재합니다 예약을 취소 하시거나 예약 시간이 지난 뒤 시도 해주세요",false);
   
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
