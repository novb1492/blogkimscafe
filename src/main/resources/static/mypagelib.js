window.onload=function(){
        var sendemail=document.getElementsByClassName("sendemail")[0];
        sendemail.addEventListener('click',function(){
            var xhr = new XMLHttpRequest(); //new로 생성
            xhr.open('POST', '/sendemail', true); //j쿼리 $ajax.({type,url},true가 비동기)
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");////이게없으면 psot전송불가 조금 찾았네
            xhr.send(null); /// ajax data부분
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
            var xhr = new XMLHttpRequest(); //new로 생성
            var data='randnum='+document.getElementById('randnum').value;
            xhr.open('POST', '/confrimrandnum', true); //j쿼리 $ajax.({type,url},true가 비동기)
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");////이게없으면 psot전송불가 조금 찾았네
            xhr.send(data); /// ajax data부분
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