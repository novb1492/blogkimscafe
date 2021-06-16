window.onload=function(){
    var emailconfrim=document.getElementById("email");///아이디중복검사
    emailconfrim.addEventListener('keyup',function(){
        var xhr,url='/auth/emailconfirm',data='email='+emailconfrim.value;
        xhr=doajax(url,data);
        xhr.onload = function() { 
        var color,text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    color='red',text='존재하지않는 이메일';
                }else{
                    color='blue',text="존재하는 이메일";
                }
                document.getElementById("emailcheck").innerHTML=text;
                emailconfrim.style.backgroundColor=color; 
            }
        }
    })
    var sendemail=document.getElementById("sendemail");
    sendemail.addEventListener('click',function(){
        var xhr,url='/sendemailnologin',data='email='+document.getElementById('email').value;
        xhr=doajax(url,data);
        xhr.onload = function() { 
            var text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    text="인증번호를 전송했습니다";
                    document.getElementById('confrimrandnum').disabled=false;
                }else{
                    text="인증번호 전송에 실패했습니다";
                }
                alert(text);
            }
        }
    })    
    var confrimrandnum=document.getElementById("confrimrandnum");
    confrimrandnum.addEventListener('click',function(){
        var xhr,url='/sendtemppwd',data='email='+document.getElementById('email').value+'&randnum='+document.getElementById('randnum').value;
        xhr=doajax(url,data);
        xhr.onload = function() { 
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    alert("임시 비밀번호 전송");
                    location.href="/auth/loginpage";
                }else{
                    alert("번호를 다시 확인해주세요");
                }
            }
        }
    })                              
}