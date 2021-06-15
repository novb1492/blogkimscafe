window.onload=function(){
        var sendemail=document.getElementsByClassName("sendemail")[0];
        sendemail.addEventListener('click',function(){
            var xhr,url='/sendemail',data=null;
            xhr=doajax(url,data);
            xhr.onload = function() { 
                var text;
                if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                    if(xhr.response=='true'){
                        text="인증번호를 전송했습니다";
                    }else{
                        text="인증번호 전송에 실패했습니다";
                    }
                    alert(text);
                }
            }
        })    
        var confrimrandnum=document.getElementById("confrimrandnum");
        confrimrandnum.addEventListener('click',function(){
            var xhr,url='/confrimrandnum',data='randnum='+document.getElementById('randnum').value;
            xhr=doajax(url,data);
            xhr.onload = function() { 
                var text;
                if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                    if(xhr.response=='true'){
                        text="이메일인증완료";
                        location.href="/mypage";
                    }else{
                        text="번호를 다시 확인해주세요 ";
                    }
                    alert(text);
                }
            }
        })                              
}
