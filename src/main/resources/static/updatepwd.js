window.onload=function(){
    var updatepwd=document.getElementById("updatePwdButton");
    updatepwd.addEventListener('click',function(){
        var xhr,url='/updatepwd',data='pwd='+document.getElementById('pwd').value+'&npwd='+document.getElementById('npwd').value+'&npwd2='+document.getElementById('npwd2').value;
        xhr=doajax(url,data);
        xhr.onload = function() { 
            var text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    text="비밀번호변경완료";
                    location.href="/mypage";
                }else{
                    text="뭔가틀립니다";
                }
                alert(text);
            }
        }
    })
}
