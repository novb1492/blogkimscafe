function confrimPwdLength(){
    var pwdlength=document.getElementById('pwd');
    var color;
    var text;
    if(pwdlength.value.length>=4){
       color='blue';
       text='';
    }else{
        color='red';
        text='비밀번호가 짧습니다';
    }
    document.getElementById("passwordlength").innerHTML=text;
    pwdlength.style.backgroundColor=color;
}
function confrimNameLength(){
    var namelength=document.getElementById('username');
    var color;
    if(namelength.value.length>1){
       color='blue';
    }else{
        color='red';
    }
    namelength.style.backgroundColor=color;
}