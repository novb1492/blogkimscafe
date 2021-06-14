window.onload=function(){
/////////////////////////////계정에대한것들
    var emailconfrim=document.getElementById("useremail");///아이디중복검사
    emailconfrim.addEventListener('keyup',function(){
        var xhr = new XMLHttpRequest(); //new로 생성
        var value='email='+emailconfrim.value;
        xhr.open('POST', '/auth/emailconfirm', true); //j쿼리 $ajax.({type,url},true가 비동기)
        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");////이게없으면 psot전송불가 조금 찾았네
        xhr.send(value); /// ajax data부분
        xhr.onload = function() { 
            var color;
            var text;
            if(xhr.status==200){ // success:function(data)부분 통신 성공시 200반환
                if(xhr.response=='true'){
                    color='blue';
                    text="";
                }else{
                    color='red';
                    text='중복된 아이디 입니다';
                }
                document.getElementById("emailcheck").innerHTML=text;
                emailconfrim.style.backgroundColor=color; 
            }
        }
    })
    var pwdlength=document.getElementById('pwd');
        pwdlength.addEventListener('keyup',function(){
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
        })
    var namelength=document.getElementById('username');
        namelength.addEventListener('keyup',function(){
            var color;
            if(namelength.value.length>1){
               color='blue';
            }else{
                color='red';
            }
            namelength.style.backgroundColor=color;
        })

        
         
}