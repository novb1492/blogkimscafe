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
    
}